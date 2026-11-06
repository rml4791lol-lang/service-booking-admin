package com.mdd.admin.crontab;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.mapper.course.CourseClassMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 预售课程转进行中任务的幂等断言器。
 *
 * <p>seam：{@link CoursePreSaleActivateJob#execute()}。审计背景（2026-11-07）：
 * 旧实现按 status=2 查出整行后 {@code updateById(entity)}，有两个问题：</p>
 * <ul>
 *   <li>整行回写：select 到 update 之间管理员改了价格/教练/名称，会被旧快照覆盖（丢更新）；</li>
 *   <li>没有行级状态闸门：并发激活/删除时仍会写回；重复执行虽然最终状态一致，
 *       但无法证明「只动 status」。</li>
 * </ul>
 *
 * <p>断言：只激活「预报名 + 未删除 + 已到开课日期」的班级；只写 status/update_time；
 * 重复执行零变更；并发改动（改名 / 已被别人激活）不被覆盖。</p>
 *
 * <p>Mapper 用动态代理 + 内存行模拟，不连数据库。执行：
 * {@code bash tools/ops/run-jobs-resilience-test.sh}</p>
 */
public final class CoursePreSaleActivateJobTest {

    private static final long NOW = System.currentTimeMillis() / 1000L;
    private static final int PRE_SALE = 2;
    private static final int ACTIVE = 1;
    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        activatesDuePreSaleClassesOnly();
        rerunWritesNothing();
        doesNotOverwriteConcurrentEdit();
        skipsRowActivatedConcurrently();
        writesOnlyStatusAndUpdateTime();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---- 1. 只激活到期预售班 --------------------------------------------------

    private static void activatesDuePreSaleClassesOnly() {
        System.out.println("[范围：只激活 预报名 + 未删除 + 已到开课日期]");
        Fixture fx = new Fixture();
        fx.row(1, PRE_SALE, 0, NOW - 86400, "到期预售班");
        fx.row(2, PRE_SALE, 0, NOW + 86400, "未来预售班");
        fx.row(3, ACTIVE, 0, NOW - 86400, "已进行中");
        fx.row(4, PRE_SALE, 1, NOW - 86400, "已删除");

        fx.job.execute();

        check("到期预售班转为进行中", fx.statusOf(1) == ACTIVE, String.valueOf(fx.statusOf(1)));
        check("未来预售班保持预报名", fx.statusOf(2) == PRE_SALE, String.valueOf(fx.statusOf(2)));
        check("进行中班级不动", fx.statusOf(3) == ACTIVE, String.valueOf(fx.statusOf(3)));
        check("已删除班级不动", fx.statusOf(4) == PRE_SALE, String.valueOf(fx.statusOf(4)));
        check("只产生 1 次有效更新", fx.appliedUpdates == 1, String.valueOf(fx.appliedUpdates));
    }

    // ---- 2. 重复执行零变更（幂等） --------------------------------------------

    private static void rerunWritesNothing() {
        System.out.println("[幂等：同一批数据重复执行不产生第二次有效写]");
        Fixture fx = new Fixture();
        fx.row(1, PRE_SALE, 0, NOW - 86400, "到期预售班");

        fx.job.execute();
        fx.job.execute();

        check("两次执行只成功激活 1 次", fx.appliedUpdates == 1, String.valueOf(fx.appliedUpdates));
        check("状态仍为进行中", fx.statusOf(1) == ACTIVE, String.valueOf(fx.statusOf(1)));
    }

    // ---- 3. 不覆盖管理员并发改名（丢更新） ------------------------------------

    private static void doesNotOverwriteConcurrentEdit() {
        System.out.println("[丢更新：select 后管理员改名，激活不得回写旧名称]");
        Fixture fx = new Fixture();
        fx.row(1, PRE_SALE, 0, NOW - 86400, "旧名称");
        fx.beforeUpdate = () -> fx.rename(1, "管理员新名称");

        fx.job.execute();

        check("班级仍为新名称", "管理员新名称".equals(fx.nameOf(1)), fx.nameOf(1));
        check("班级已激活", fx.statusOf(1) == ACTIVE, String.valueOf(fx.statusOf(1)));
    }

    // ---- 4. 并发已激活的行不再回写 --------------------------------------------

    private static void skipsRowActivatedConcurrently() {
        System.out.println("[并发：select 后被别人激活，条件更新应影响 0 行]");
        Fixture fx = new Fixture();
        fx.row(1, PRE_SALE, 0, NOW - 86400, "并发班");
        final int[] updateTimeBefore = new int[1];
        fx.beforeUpdate = () -> {
            fx.activateDirectly(1);
            updateTimeBefore[0] = fx.updateTimeOf(1);
        };

        fx.job.execute();

        check("状态保持进行中", fx.statusOf(1) == ACTIVE, String.valueOf(fx.statusOf(1)));
        check("并发激活的 update_time 未被改写", fx.updateTimeOf(1) == updateTimeBefore[0],
                fx.updateTimeOf(1) + " vs " + updateTimeBefore[0]);
        check("本次没有有效更新", fx.appliedUpdates == 0, String.valueOf(fx.appliedUpdates));
    }

    // ---- 5. 只写 status / update_time，不整行回写 -----------------------------

    private static void writesOnlyStatusAndUpdateTime() {
        System.out.println("[列范围：一次激活只写 status 与 update_time]");
        Fixture fx = new Fixture();
        fx.row(1, PRE_SALE, 0, NOW - 86400, "班级A");

        fx.job.execute();

        Set<String> columns = fx.writtenColumns();
        check("写入列 = [status, update_time]", columns.equals(new LinkedHashSet<>(
                java.util.Arrays.asList("status", "update_time"))), String.valueOf(columns));
        check("名称/价格等字段未被回写", "班级A".equals(fx.nameOf(1))
                && fx.row(1).feePerLesson.compareTo(new java.math.BigDecimal("200.00")) == 0,
                fx.nameOf(1));
    }

    // ---- 夹具 ----------------------------------------------------------------

    private static final class Fixture {
        final List<Row> rows = new ArrayList<>();
        final CoursePreSaleActivateJob job = new CoursePreSaleActivateJob();
        final Set<String> columns = new LinkedHashSet<>();
        int appliedUpdates;
        Runnable beforeUpdate;
        private boolean updateHookRun;

        Fixture() {
            job.courseClassMapper = fakeMapper();
        }

        void row(int id, int status, int isDelete, long startDate, String name) {
            Row row = new Row();
            row.id = id;
            row.status = status;
            row.isDelete = isDelete;
            row.startDate = startDate;
            row.name = name;
            row.feePerLesson = new java.math.BigDecimal("200.00");
            row.updateTime = 1;
            rows.add(row);
        }

        Row row(int id) {
            for (Row row : rows) {
                if (row.id == id) return row;
            }
            throw new IllegalStateException("row not found: " + id);
        }

        int statusOf(int id) { return row(id).status; }
        int updateTimeOf(int id) { return row(id).updateTime; }
        String nameOf(int id) { return row(id).name; }

        void rename(int id, String name) { row(id).name = name; }

        void activateDirectly(int id) {
            Row row = row(id);
            row.status = ACTIVE;
            row.updateTime = row.updateTime + 100;
        }

        Set<String> writtenColumns() { return columns; }

        private CourseClassMapper fakeMapper() {
            return proxy(CourseClassMapper.class, (proxy, method, args) -> {
                if ("selectList".equals(method.getName())) {
                    return select((Wrapper<?>) args[0]);
                }
                if ("update".equals(method.getName())) {
                    return update((Wrapper<?>) args[1]);
                }
                if ("updateById".equals(method.getName())) {
                    return updateById((CourseClass) args[0]);
                }
                return 0;
            });
        }

        private List<CourseClass> select(Wrapper<?> wrapper) {
            String segment = wrapper.getSqlSegment();
            Map<String, Object> pairs = pairs(wrapper);
            Integer status = intParam(segment, "status =", pairs);
            Integer isDelete = intParam(segment, "is_delete =", pairs);
            Long startDate = longParam(segment, "start_date <=", pairs);

            List<Row> matched = new ArrayList<>();
            for (Row row : rows) {
                if (status != null && row.status != status) continue;
                if (isDelete != null && row.isDelete != isDelete) continue;
                if (startDate != null && row.startDate > startDate) continue;
                matched.add(row);
            }
            Collections.sort(matched, Comparator.comparingInt(row -> row.id));
            List<CourseClass> result = new ArrayList<>();
            for (Row row : matched) {
                result.add(toEntity(row));
            }
            return result;
        }

        private int update(Wrapper<?> wrapper) {
            runUpdateHook();
            String set = ((com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<?>) wrapper).getSqlSet();
            Map<String, Object> pairs = pairs(wrapper);
            String segment = wrapper.getSqlSegment();

            Matcher setMatcher = Pattern.compile("(\\w+)=#\\{ew\\.paramNameValuePairs\\.(MPGENVAL\\d+)\\}").matcher(set);
            List<String> setColumns = new ArrayList<>();
            List<Object> setValues = new ArrayList<>();
            while (setMatcher.find()) {
                setColumns.add(setMatcher.group(1));
                setValues.add(pairs.get(setMatcher.group(2)));
            }
            columns.addAll(setColumns);

            Integer id = intParam(segment, "id =", pairs);
            Integer status = intParam(segment, "status =", pairs);
            Integer isDelete = intParam(segment, "is_delete =", pairs);
            Long startDate = longParam(segment, "start_date <=", pairs);

            int affected = 0;
            for (Row row : rows) {
                if (id != null && row.id != id) continue;
                if (status != null && row.status != status) continue;
                if (isDelete != null && row.isDelete != isDelete) continue;
                if (startDate != null && row.startDate > startDate) continue;
                for (int i = 0; i < setColumns.size(); i++) {
                    applyColumn(row, setColumns.get(i), setValues.get(i));
                }
                affected++;
            }
            appliedUpdates += affected;
            return affected;
        }

        /** 旧实现的整行回写：把实体所有字段覆盖到行上。 */
        private int updateById(CourseClass entity) {
            runUpdateHook();
            Row row = row(entity.getId());
            row.name = entity.getName();
            row.status = entity.getStatus();
            row.isDelete = entity.getIsDelete();
            row.startDate = entity.getStartDate();
            row.feePerLesson = entity.getFeePerLesson();
            if (entity.getUpdateTime() != null) {
                row.updateTime = entity.getUpdateTime();
            }
            columns.add("ALL_FIELDS");
            appliedUpdates++;
            return 1;
        }

        private void runUpdateHook() {
            if (beforeUpdate != null && !updateHookRun) {
                updateHookRun = true;
                beforeUpdate.run();
            }
        }

        private void applyColumn(Row row, String column, Object value) {
            if ("status".equals(column)) {
                row.status = ((Number) value).intValue();
            } else if ("update_time".equals(column)) {
                row.updateTime = ((Number) value).intValue();
            } else if ("name".equals(column)) {
                row.name = String.valueOf(value);
            }
        }

        private CourseClass toEntity(Row row) {
            CourseClass entity = new CourseClass();
            entity.setId(row.id);
            entity.setStatus(row.status);
            entity.setIsDelete(row.isDelete);
            entity.setStartDate((int) row.startDate);
            entity.setName(row.name);
            entity.setFeePerLesson(row.feePerLesson);
            entity.setUpdateTime(row.updateTime);
            return entity;
        }
    }

    private static final class Row {
        int id;
        int status;
        int isDelete;
        long startDate;
        String name;
        java.math.BigDecimal feePerLesson;
        int updateTime;
    }

    // ---- 通用 ----------------------------------------------------------------

    private static Map<String, Object> pairs(Wrapper<?> wrapper) {
        return ((AbstractWrapper<?, ?, ?>) wrapper).getParamNameValuePairs();
    }

    private static Integer intParam(String segment, String column, Map<String, Object> pairs) {
        Long value = longParam(segment, column, pairs);
        return value == null ? null : value.intValue();
    }

    private static Long longParam(String segment, String column, Map<String, Object> pairs) {
        Matcher matcher = Pattern.compile(Pattern.quote(column)
                        + "\\s*#\\{ew\\.paramNameValuePairs\\.(MPGENVAL\\d+)\\}")
                .matcher(segment);
        if (!matcher.find()) {
            return null;
        }
        Object value = pairs.get(matcher.group(1));
        return value == null ? null : Long.valueOf(String.valueOf(value));
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
