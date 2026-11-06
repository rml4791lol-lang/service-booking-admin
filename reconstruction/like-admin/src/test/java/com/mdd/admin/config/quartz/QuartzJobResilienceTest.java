package com.mdd.admin.config.quartz;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.mdd.admin.config.quartz.exceution.AbstractQuartzJob;
import com.mdd.common.entity.DevCrontab;
import com.mdd.common.mapper.DevCrontabMapper;
import com.mdd.common.util.SpringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Quartz 定时任务容错断言器（2026-11-07 定时任务审计）。
 *
 * <p>覆盖三件事：</p>
 * <ol>
 *   <li><b>任务状态语义</b>：la_dev_crontab.status 1=运行、2=停止、3=错误。旧代码把
 *       「执行失败」写成 2（与管理员停止同值），而启动时 {@code status != 1} 的触发器一律
 *       pause——一次瞬时失败 + 一次重启 = 任务永久静默停摆。修复后失败写 3，只有 2 才暂停，
 *       3 在重启后恢复调度。</li>
 *   <li><b>重启不补跑</b>：启动时 {@code scheduler.clear()} 后按库重建触发器，新触发器从
 *       当前时间起算；{@code withMisfireHandlingInstructionIgnoreMisfires} 只对同一
 *       Scheduler 实例内错过的触发点生效，进程停机期间的时间点不会补跑。触发器 nextFireTime
 *       必须在未来。</li>
 *   <li><b>执行结果落库</b>：成功/失败都要更新 last_time；失败写 status=3 与截断后的错误；
 *       成功只在库中仍是 3 时恢复为 1，绝不把管理员停止的任务（2）重新拉起。</li>
 * </ol>
 *
 * <p>另外验证 {@link CronUtils#missedFireTime}：启动时用它判断「上次执行之后是否已错过
 * 至少一个计划点」，作为漏跑告警的判定口径。</p>
 *
 * <p>执行：{@code bash tools/ops/run-jobs-resilience-test.sh}</p>
 */
public final class QuartzJobResilienceTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) throws Exception {
        schedulerStates();
        restartDoesNotBackfill();
        missedFireTimeDetection();
        afterPersistsStatus();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---- 1. status 语义：1 运行 / 2 暂停 / 3 错误但继续调度 -------------------

    private static void schedulerStates() throws Exception {
        System.out.println("[状态语义：只有 2=停止 才暂停；3=错误 重启后继续调度]");
        Scheduler scheduler = newScheduler();
        DevCrontab running = crontab(1, 1, "0 0 3 * * ?");
        DevCrontab stopped = crontab(2, 2, "0 0 3 * * ?");
        DevCrontab error = crontab(3, 3, "0 0 3 * * ?");
        DevCrontab draft = crontab(4, 0, "0 0 3 * * ?");

        QuartzUtils.createScheduleJob(scheduler, running);
        QuartzUtils.createScheduleJob(scheduler, stopped);
        QuartzUtils.createScheduleJob(scheduler, error);
        QuartzUtils.createScheduleJob(scheduler, draft);

        check("status=1 → 正常等待触发", state(scheduler, running) == Trigger.TriggerState.NORMAL,
                String.valueOf(state(scheduler, running)));
        check("status=2 → 已暂停", state(scheduler, stopped) == Trigger.TriggerState.PAUSED,
                String.valueOf(state(scheduler, stopped)));
        check("status=3 → 不暂停（重启后自动恢复）", state(scheduler, error) == Trigger.TriggerState.NORMAL,
                String.valueOf(state(scheduler, error)));
        check("status=0（未运行）→ 暂停", state(scheduler, draft) == Trigger.TriggerState.PAUSED,
                String.valueOf(state(scheduler, draft)));
        scheduler.shutdown(true);
    }

    // ---- 2. 重启不补跑：新建触发器的 nextFireTime 必须在未来 -------------------

    private static void restartDoesNotBackfill() throws Exception {
        System.out.println("[重启补偿：从当前时间重建触发器，停机期间错过的时间点不补跑]");
        Scheduler scheduler = newScheduler();
        long now = System.currentTimeMillis();
        DevCrontab daily = crontab(1, 1, "0 0 3 * * ?");
        DevCrontab everyThreeMinutes = crontab(2, 1, "0 */3 * * * ?");

        QuartzUtils.createScheduleJob(scheduler, daily);
        QuartzUtils.createScheduleJob(scheduler, everyThreeMinutes);

        Date dailyNext = scheduler.getTrigger(QuartzUtils.getTriggerKey(daily.getId(),
                daily.getType().toString())).getNextFireTime();
        Date minutesNext = scheduler.getTrigger(QuartzUtils.getTriggerKey(everyThreeMinutes.getId(),
                everyThreeMinutes.getType().toString())).getNextFireTime();
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(QuartzUtils.getTriggerKey(
                daily.getId(), daily.getType().toString()));

        check("每天 3 点任务 nextFireTime 在未来", dailyNext != null && dailyNext.getTime() > now,
                String.valueOf(dailyNext));
        check("每 3 分钟任务 nextFireTime 在未来", minutesNext != null && minutesNext.getTime() > now,
                String.valueOf(minutesNext));
        check("misfire 策略 = IGNORE_MISFIRE_POLICY（仅同进程内生效）",
                trigger.getMisfireInstruction() == CronTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY,
                String.valueOf(trigger.getMisfireInstruction()));
        scheduler.shutdown(true);
    }

    // ---- 3. 漏跑判定口径 ------------------------------------------------------

    private static void missedFireTimeDetection() {
        System.out.println("[漏跑告警：last_time 之后是否已错过计划点]");
        long base = epoch(2026, 10, 7, 3, 0, 0); // 2026-11-07 03:00:00（毫秒）
        long baseSeconds = base / 1000L;

        check("每天 3 点：last=今天 3 点、now=+1h → 未漏跑",
                CronUtils.missedFireTime("0 0 3 * * ?", baseSeconds, base + 3600_000L) == null,
                "");
        check("每天 3 点：now=+25h → 漏跑（应报 next=次日 3 点）",
                CronUtils.missedFireTime("0 0 3 * * ?", baseSeconds, base + 25 * 3600_000L) != null,
                "");
        check("每 3 分钟：now=+3min 整 → 未漏跑（下一计划点刚到期，不算过期）",
                CronUtils.missedFireTime("0 */3 * * * ?", baseSeconds, base + 3 * 60_000L) == null,
                "");
        check("每 3 分钟：now=+3min+1s → 漏跑",
                CronUtils.missedFireTime("0 */3 * * * ?", baseSeconds, base + 3 * 60_000L + 1000L) != null,
                "");
        check("last_time 为空 → 不下漏跑结论",
                CronUtils.missedFireTime("0 0 3 * * ?", null, base + 86400_000L) == null, "");
        check("非法表达式 → 不抛异常、不下结论",
                CronUtils.missedFireTime("not-a-cron", baseSeconds, base + 86400_000L) == null, "");
    }

    // ---- 4. after() 落库语义 --------------------------------------------------

    private static void afterPersistsStatus() throws Exception {
        System.out.println("[执行结果落库：last_time 必写；失败=3；成功只从 3 恢复为 1]");
        DevCrontab row = crontab(1, 1, "0 0 3 * * ?");
        FakeMapper mapper = installMapper(row);
        ExposedJob job = new ExposedJob();
        long before = System.currentTimeMillis() / 1000L;

        job.begin();
        job.runAfter(crontabSnapshot(row), null);
        check("成功：last_time 已更新", row.getLastTime() != null && row.getLastTime() >= before,
                String.valueOf(row.getLastTime()));
        check("成功：状态保持 1", row.getStatus() == 1, String.valueOf(row.getStatus()));
        check("成功：error 清空", row.getError() == null || row.getError().isEmpty(),
                String.valueOf(row.getError()));

        // 失败：status 写 3（不是 2），错误落库
        job.begin();
        job.runAfter(crontabSnapshot(row), new IllegalStateException("模拟执行失败"));
        check("失败：状态写 3=错误（不占用 2=停止）", row.getStatus() == 3, String.valueOf(row.getStatus()));
        check("失败：错误原因落库", row.getError() != null && row.getError().contains("模拟执行失败"),
                String.valueOf(row.getError()));

        // 恢复：库中 3 + 下一次成功 → 回到 1
        job.begin();
        job.runAfter(crontabSnapshot(row), null);
        check("恢复：成功把 3 改回 1", row.getStatus() == 1, String.valueOf(row.getStatus()));

        // 管理员停止（2）后，即使有一次成功执行也不得复活
        row.setStatus(2);
        mapper.reset();
        job.begin();
        job.runAfter(crontabSnapshot(row), null);
        check("管理员停止 2 不被成功执行复活", row.getStatus() == 2, String.valueOf(row.getStatus()));

        // 运行中管理员点了停止：进程内对象仍是旧状态 1，也不能把库里的 2 覆盖回去
        row.setStatus(2);
        DevCrontab staleSnapshot = crontabSnapshot(row);
        staleSnapshot.setStatus(1);
        mapper.reset();
        job.begin();
        job.runAfter(staleSnapshot, null);
        check("运行中停止不被内存旧状态复活", row.getStatus() == 2, String.valueOf(row.getStatus()));

        // 管理员停止后，即使紧接着的执行失败，也不能把 2 改写成 3（否则重启会复活）
        row.setStatus(2);
        mapper.reset();
        job.begin();
        job.runAfter(crontabSnapshot(row), new IllegalStateException("停止后失败"));
        check("管理员停止 2 不被失败执行改写为 3", row.getStatus() == 2, String.valueOf(row.getStatus()));

        // 超长错误信息必须截断，否则 varchar(256) 写失败会连带丢 last_time
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longMessage.append("退款渠道返回了非常长的错误信息");
        }
        row.setStatus(1);
        mapper.reset();
        job.begin();
        job.runAfter(crontabSnapshot(row), new IllegalStateException(longMessage.toString()));
        check("超长错误截断到 <=240 字符", row.getError() != null && row.getError().length() <= 240,
                row.getError() == null ? "null" : String.valueOf(row.getError().length()));
        check("超长错误仍写入 last_time", row.getLastTime() != null && row.getLastTime() >= before,
                String.valueOf(row.getLastTime()));
    }

    // ---- 夹具 ----------------------------------------------------------------

    /** 暴露 protected before/after 的测试子类。 */
    private static final class ExposedJob extends AbstractQuartzJob {
        @Override
        protected void doExecute(JobExecutionContext context, DevCrontab crontab) {
        }

        void begin() {
            before();
        }

        void runAfter(DevCrontab crontab, Exception e) {
            after(crontab, e);
        }
    }

    private static DevCrontab crontab(int id, int status, String expression) {
        DevCrontab job = new DevCrontab();
        job.setId(id);
        job.setName("job-" + id);
        job.setType(1);
        job.setStatus(status);
        job.setExpression(expression);
        job.setCommand("noop");
        job.setError("");
        job.setLastTime(0L);
        job.setMaxTime(0L);
        job.setTime(0L);
        return job;
    }

    private static DevCrontab crontabSnapshot(DevCrontab source) {
        DevCrontab copy = new DevCrontab();
        copy.setId(source.getId());
        copy.setName(source.getName());
        copy.setType(source.getType());
        copy.setStatus(source.getStatus());
        copy.setExpression(source.getExpression());
        copy.setCommand(source.getCommand());
        copy.setError(source.getError());
        copy.setLastTime(source.getLastTime());
        copy.setMaxTime(source.getMaxTime());
        copy.setTime(source.getTime());
        return copy;
    }

    private static final class FakeMapper {
        final DevCrontab row;
        final List<String> setColumns = new ArrayList<>();

        FakeMapper(DevCrontab row) {
            this.row = row;
        }

        void reset() {
            setColumns.clear();
        }
    }

    /** 把 DevCrontabMapper 代理注册到 SpringUtils，after() 通过它落库。 */
    private static FakeMapper installMapper(DevCrontab row) throws Exception {
        FakeMapper fake = new FakeMapper(row);
        DevCrontabMapper mapper = proxy(DevCrontabMapper.class, (proxy, method, args) -> {
            if ("update".equals(method.getName())) {
                apply(fake, (Wrapper<?>) args[1]);
                return 1;
            }
            if ("updateById".equals(method.getName())) {
                DevCrontab entity = (DevCrontab) args[0];
                copyInto(fake.row, entity);
                fake.setColumns.add("ALL_FIELDS");
                return 1;
            }
            return 0;
        });
        org.springframework.context.ApplicationContext context = proxy(
                org.springframework.context.ApplicationContext.class, (proxy, method, args) -> {
                    if ("getBean".equals(method.getName()) && args != null && args.length == 1
                            && args[0] == DevCrontabMapper.class) {
                        return mapper;
                    }
                    return null;
                });
        Field field = SpringUtils.class.getDeclaredField("applicationContext");
        field.setAccessible(true);
        field.set(null, context);
        return fake;
    }

    private static void apply(FakeMapper fake, Wrapper<?> wrapper) {
        String set = ((com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<?>) wrapper).getSqlSet();
        String segment = wrapper.getSqlSegment();
        Map<String, Object> pairs = ((AbstractWrapper<?, ?, ?>) wrapper).getParamNameValuePairs();

        Integer id = intParam(segment, "id =", pairs);
        Integer status = intParam(segment, "status =", pairs);
        Integer notStatus = intParam(segment, "status <>", pairs);
        if (id != null && !id.equals(fake.row.getId())) {
            return;
        }
        if (status != null && !status.equals(fake.row.getStatus())) {
            return;
        }
        if (notStatus != null && notStatus.equals(fake.row.getStatus())) {
            return;
        }

        Matcher matcher = Pattern.compile("(\\w+)=#\\{ew\\.paramNameValuePairs\\.(MPGENVAL\\d+)\\}")
                .matcher(set);
        while (matcher.find()) {
            String column = matcher.group(1);
            Object value = pairs.get(matcher.group(2));
            fake.setColumns.add(column);
            if ("error".equals(column)) {
                fake.row.setError(value == null ? null : String.valueOf(value));
            } else if ("last_time".equals(column)) {
                fake.row.setLastTime(value == null ? null : Long.valueOf(String.valueOf(value)));
            } else if ("time".equals(column)) {
                fake.row.setTime(value == null ? null : Long.valueOf(String.valueOf(value)));
            } else if ("max_time".equals(column)) {
                fake.row.setMaxTime(value == null ? null : Long.valueOf(String.valueOf(value)));
            } else if ("status".equals(column)) {
                fake.row.setStatus(value == null ? null : Integer.valueOf(String.valueOf(value)));
            }
        }
    }

    private static void copyInto(DevCrontab target, DevCrontab source) {
        target.setStatus(source.getStatus());
        target.setError(source.getError());
        target.setLastTime(source.getLastTime());
        target.setTime(source.getTime());
        target.setMaxTime(source.getMaxTime());
    }

    private static Scheduler newScheduler() throws Exception {
        Properties props = new Properties();
        props.setProperty("org.quartz.scheduler.instanceName", "jobs-resilience-" + System.nanoTime());
        props.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
        props.setProperty("org.quartz.threadPool.threadCount", "1");
        props.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        return new StdSchedulerFactory(props).getScheduler();
    }

    private static Trigger.TriggerState state(Scheduler scheduler, DevCrontab job) throws Exception {
        return scheduler.getTriggerState(QuartzUtils.getTriggerKey(job.getId(), job.getType().toString()));
    }

    private static long epoch(int year, int month, int day, int hour, int minute, int second) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTimeInMillis();
    }

    private static Integer intParam(String segment, String column, Map<String, Object> pairs) {
        Matcher matcher = Pattern.compile(Pattern.quote(column)
                        + "\\s*#\\{ew\\.paramNameValuePairs\\.(MPGENVAL\\d+)\\}")
                .matcher(segment);
        if (!matcher.find()) {
            return null;
        }
        Object value = pairs.get(matcher.group(1));
        return value == null ? null : Integer.valueOf(String.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static void check(String name, boolean ok, String detail) {
        detail = detail == null ? "" : detail;
        if (ok) {
            passed += 1;
            System.out.println("  ✓ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        } else {
            failed += 1;
            System.out.println("  ✗ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        }
    }
}
