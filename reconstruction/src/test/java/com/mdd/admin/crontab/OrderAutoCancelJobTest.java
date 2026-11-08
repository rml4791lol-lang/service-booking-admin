package com.mdd.admin.crontab;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.service.UnpaidOrderCancelService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 未支付订单自动取消任务的漏跑补偿断言器。
 *
 * <p>seam：{@link OrderAutoCancelJob#execute()} —— 任务唯一入口。审计背景（2026-11-07）：
 * 旧实现单次只处理 200 单（{@code limit 200}），每 3 分钟一轮；容器停摆数小时后待取消
 * 订单积压，按 200/3min 排队，积压 1 万单需要 2.5 小时才清完，期间券/活动名额一直被占。
 * 另外 {@code orderByAsc("id") + limit} 下，排在最前的失败订单会反复占据名额（队头阻塞）。</p>
 *
 * <p>断言：一次执行按 id 游标连续处理多批直到清空（有上限），单笔失败不阻塞后续批次；
 * 未超时/已支付/已删除订单不被选中。</p>
 *
 * <p>Mapper 与取消服务用动态代理/子类注入，不连数据库。执行：
 * {@code bash tools/ops/run-jobs-resilience-test.sh}</p>
 */
public final class OrderAutoCancelJobTest {

    private static final long NOW = System.currentTimeMillis() / 1000L;
    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        drainsBacklogInOneRun();
        failureDoesNotBlockLaterBatches();
        stopsAtBatchCap();
        ignoresNotExpiredPaidAndDeleted();
        secondRunRetriesFailedOrdersOnly();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---- 1. 积压排空：401 单在一次执行内全部处理 ------------------------------

    private static void drainsBacklogInOneRun() {
        System.out.println("[积压补偿：401 单超过单批 200，一次执行内排空]");
        Fixture fx = new Fixture();
        for (int id = 1; id <= 401; id++) {
            fx.expired(id);
        }

        fx.job.execute();

        check("401 单全部尝试取消", fx.cancel.calls.size() == 401, String.valueOf(fx.cancel.calls.size()));
        check("执行后无超时未支付订单", fx.expiredCount() == 0, String.valueOf(fx.expiredCount()));
        check("按 id 升序处理", isAscending(fx.cancel.calls), String.valueOf(fx.cancel.calls.size()));
    }

    // ---- 2. 失败项不阻塞后续批次（队头阻塞） ---------------------------------

    private static void failureDoesNotBlockLaterBatches() {
        System.out.println("[队头阻塞：前 200 单持续失败，后 201 单仍要被处理]");
        Fixture fx = new Fixture();
        for (int id = 1; id <= 401; id++) {
            fx.expired(id);
            if (id <= 200) {
                fx.cancel.failing.add(id);
            }
        }

        fx.job.execute();

        check("401 单全部尝试（失败不影响后续批次）", fx.cancel.calls.size() == 401,
                String.valueOf(fx.cancel.calls.size()));
        check("失败的 200 单仍待支付", fx.expiredCount() == 200, String.valueOf(fx.expiredCount()));
        check("成功的 201 单已关闭", fx.closedCount() == 201, String.valueOf(fx.closedCount()));
    }

    // ---- 3. 单次执行有上限，剩余留给下一轮 ------------------------------------

    private static void stopsAtBatchCap() {
        System.out.println("[有界执行：积压远超单次处理量时，处理后终止并留下剩余]");
        Fixture fx = new Fixture();
        int total = 6000;
        for (int id = 1; id <= total; id++) {
            fx.expired(id);
        }

        fx.job.execute();

        check("一次执行至少排空 1000 单（远超单批 200）", fx.cancel.calls.size() >= 1000,
                String.valueOf(fx.cancel.calls.size()));
        check("单次执行有上限（不会无限跑）", fx.cancel.calls.size() < total,
                String.valueOf(fx.cancel.calls.size()));
        check("上限外订单留给下一轮", fx.expiredCount() > 0, String.valueOf(fx.expiredCount()));
    }

    // ---- 4. 查询条件：未超时/已支付/已删除都不动 ------------------------------

    private static void ignoresNotExpiredPaidAndDeleted() {
        System.out.println("[查询条件：未超时/已支付/已删除不下发取消]");
        Fixture fx = new Fixture();
        fx.freshUnpaid(1);
        fx.paid(2);
        fx.deleted(3);

        fx.job.execute();

        check("无取消调用", fx.cancel.calls.isEmpty(), String.valueOf(fx.cancel.calls.size()));
    }

    // ---- 5. 下一轮只重试失败单，不重复处理已关闭单 ----------------------------

    private static void secondRunRetriesFailedOrdersOnly() {
        System.out.println("[重跑幂等：第二轮只重试上一轮失败单]");
        Fixture fx = new Fixture();
        for (int id = 1; id <= 401; id++) {
            fx.expired(id);
            if (id <= 200) {
                fx.cancel.failing.add(id);
            }
        }
        fx.job.execute();
        int firstRunCalls = fx.cancel.calls.size();

        fx.cancel.failing.clear();
        fx.job.execute();

        check("第二轮只处理剩余 200 单", fx.cancel.calls.size() == firstRunCalls + 200,
                String.valueOf(fx.cancel.calls.size() - firstRunCalls));
        check("最终全部关闭", fx.expiredCount() == 0, String.valueOf(fx.expiredCount()));
    }

    // ---- 夹具 ----------------------------------------------------------------

    private static final class Fixture {
        final List<OrderRow> rows = new ArrayList<>();
        final FakeCancelService cancel = new FakeCancelService();
        final OrderAutoCancelJob job = new OrderAutoCancelJob();
        int queries;

        Fixture() {
            cancel.owner = this;
            job.rechargeOrderMapper = fakeMapper();
            job.unpaidOrderCancelService = cancel;
        }

        void expired(int id) {
            OrderRow row = new OrderRow();
            row.id = id;
            row.payStatus = 0;
            row.createTime = NOW - 3600;
            rows.add(row);
        }

        void freshUnpaid(int id) {
            OrderRow row = new OrderRow();
            row.id = id;
            row.payStatus = 0;
            row.createTime = NOW - 60;
            rows.add(row);
        }

        void paid(int id) {
            OrderRow row = new OrderRow();
            row.id = id;
            row.payStatus = 1;
            row.createTime = NOW - 3600;
            rows.add(row);
        }

        void deleted(int id) {
            OrderRow row = new OrderRow();
            row.id = id;
            row.payStatus = 0;
            row.createTime = NOW - 3600;
            row.deleteTime = NOW - 10;
            rows.add(row);
        }

        long expiredCount() {
            long count = 0;
            for (OrderRow row : rows) {
                if (row.payStatus == 0 && row.deleteTime == null && row.createTime <= NOW - 1800) {
                    count++;
                }
            }
            return count;
        }

        long closedCount() {
            long count = 0;
            for (OrderRow row : rows) {
                if (row.payStatus == 4) {
                    count++;
                }
            }
            return count;
        }

        private RechargeOrderMapper fakeMapper() {
            return proxy(RechargeOrderMapper.class, (proxy, method, args) -> {
                if ("selectList".equals(method.getName())) {
                    return select((Wrapper<?>) args[0]);
                }
                return 0;
            });
        }

        private List<RechargeOrder> select(Wrapper<?> wrapper) {
            if (++queries > 60) {
                throw new IllegalStateException("查询次数异常（疑似未推进游标/死循环）");
            }
            String segment = wrapper.getSqlSegment();
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> pairs =
                    ((com.baomidou.mybatisplus.core.conditions.AbstractWrapper<?, ?, ?>) wrapper)
                            .getParamNameValuePairs();
            Integer payStatus = intParam(segment, "pay_status =", pairs);
            Long createTime = longParam(segment, "create_time <=", pairs);
            Long cursor = longParam(segment, "id >", pairs);
            int limit = limit(segment, 200);

            List<OrderRow> matched = new ArrayList<>();
            for (OrderRow row : rows) {
                if (payStatus != null && row.payStatus != payStatus) continue;
                if (createTime != null && row.createTime > createTime) continue;
                if (segment.contains("delete_time IS NULL") && row.deleteTime != null) continue;
                if (cursor != null && row.id <= cursor) continue;
                matched.add(row);
            }
            Collections.sort(matched, Comparator.comparingInt(row -> row.id));
            List<RechargeOrder> result = new ArrayList<>();
            for (int i = 0; i < matched.size() && i < limit; i++) {
                RechargeOrder order = new RechargeOrder();
                order.setId(matched.get(i).id);
                result.add(order);
            }
            return result;
        }

        private static Integer intParam(String segment, String column, java.util.Map<String, Object> pairs) {
            Long value = longParam(segment, column, pairs);
            return value == null ? null : value.intValue();
        }

        private static Long longParam(String segment, String column, java.util.Map<String, Object> pairs) {
            Matcher matcher = Pattern.compile(Pattern.quote(column)
                            + "\\s*#\\{ew\\.paramNameValuePairs\\.(MPGENVAL\\d+)\\}")
                    .matcher(segment);
            if (!matcher.find()) {
                return null;
            }
            Object value = pairs.get(matcher.group(1));
            return value == null ? null : Long.valueOf(String.valueOf(value));
        }

        private static int limit(String segment, int fallback) {
            Matcher matcher = Pattern.compile("limit\\s+(\\d+)", Pattern.CASE_INSENSITIVE).matcher(segment);
            return matcher.find() ? Integer.parseInt(matcher.group(1)) : fallback;
        }
    }

    private static final class OrderRow {
        int id;
        int payStatus;
        long createTime;
        Long deleteTime;
    }

    /** 取消服务子类：记录调用、按订单号模拟失败、成功时把订单置为已取消。 */
    private static final class FakeCancelService extends UnpaidOrderCancelService {
        final List<Integer> calls = new ArrayList<>();
        final Set<Integer> failing = new HashSet<>();
        Fixture owner;

        @Override
        public boolean cancelExpired(Integer orderId, long expireTime) {
            calls.add(orderId);
            if (failing.contains(orderId)) {
                throw new SimulatedFailure("模拟关单失败 orderId=" + orderId);
            }
            for (OrderRow row : owner.rows) {
                if (row.id == orderId) {
                    row.payStatus = 4;
                    return true;
                }
            }
            return false;
        }
    }

    /** 模拟失败不带栈：避免测试输出被 200 条堆栈淹没。 */
    private static final class SimulatedFailure extends RuntimeException {
        SimulatedFailure(String message) {
            super(message, null, false, false);
        }
    }

    // ---- 通用 ----------------------------------------------------------------

    private static boolean isAscending(List<Integer> values) {
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i - 1) > values.get(i)) {
                return false;
            }
        }
        return true;
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
