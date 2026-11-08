package com.mdd.admin.service.refund;

import com.mdd.admin.service.IFinanceRechargerService;
import com.mdd.admin.service.IRefundTaskService;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.refund.RefundTask;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.refund.RefundTaskMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * #220 RefundSettlement 唯一出款入口断言器。
 *
 * <p>本仓库离线 Maven 仓库没有 junit / surefire provider，按仓库惯例写成
 * 「带 main 的纯 Java 断言器」，由 tools/refund/run-refund-settlement-test.sh 执行。</p>
 *
 * <p>seam：{@link RefundSettlement#settle(Intent)} —— 调用方（课程/活动/券退款审核）
 * 只构造退款意图，出款渠道分派与失败记录语义全部收敛在本模块：</p>
 * <ul>
 *   <li>微信（payWay=2 且有交易号）→ #60 outbox 异步任务（失败记 la_refund_task.last_error，可重试）；</li>
 *   <li>钱包（payWay=1）→ FinanceRechargerService.refund 同步退回余额（失败异常上抛回滚审核）；</li>
 *   <li>其它（线下转账/历史无交易号）→ 条件更新订单 refund_status 0→1，审核即完成；</li>
 *   <li>微信放行前同单唯一（#152 行锁 + 既有任务检查），refund_id 已有任务时复用不重放。</li>
 * </ul>
 *
 * <p>Mapper/服务用动态代理注入，不连数据库。</p>
 */
public final class RefundSettlementTest {

    private static final int ADMIN_ID = 9;
    private static final int ORDER_ID = 1001;
    private static final int REFUND_ID = 2001;
    private static final int USER_ID = 301;
    private static final BigDecimal AMOUNT = new BigDecimal("25.50");

    private static int passed;
    private static int failed;

    public static void main(String[] args) throws Exception {
        wechatPaidGoesToOutbox();
        wechatExistingTaskIsReusedNotReplayed();
        wechatClaimGuardRejectsChangedOrder();
        walletPaidRefundsBalanceAtomically();
        walletFailurePropagates();
        offlineOrderSettlesDirectly();
        nonPositiveAmountSettlesNothing();
        nonRefundableOrderSettlesNothing();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---- 1. 微信支付 → outbox 异步任务 ---------------------------------------

    private static void wechatPaidGoesToOutbox() {
        System.out.println("[微信渠道：payWay=2 + 交易号 → outbox 任务，不直接动余额/订单]");
        Fixture fx = new Fixture(wechatOrder(null));
        fx.tasks.add(null); // refund_id 无既有任务

        RefundSettlement.Result result = fx.settlement.settle(intent(wechatOrder(null), fx));

        check("微信渠道返回 WECHAT_OUTBOX",
                result.channel() == RefundSettlement.Channel.WECHAT_OUTBOX && !result.reused(),
                String.valueOf(result.channel()));
        check("创建退款任务 1 次（refund+order 原样透传）", fx.createdRefunds.size() == 1
                        && fx.createdRefunds.get(0) == fx.intentRefund
                        && fx.createdOrders.contains(fx.intentOrder),
                String.valueOf(fx.createdRefunds.size()));
        check("微信路径不动钱包退款", fx.walletRefunds.isEmpty(), String.valueOf(fx.walletRefunds.size()));
        check("微信路径不直接置订单已退", fx.orderUpdates.isEmpty(), String.valueOf(fx.orderUpdates.size()));
        check("微信放行前对订单行加锁（selectOne for update）", fx.orderLocked,
                String.valueOf(fx.orderLocked));
    }

    // ---- 2. 微信复用：同 refund_id 已有任务 → 不再放行 ------------------------

    private static void wechatExistingTaskIsReusedNotReplayed() {
        System.out.println("[微信复用：refund_id 已有任务 → 复用，不二次放行/二次退款]");
        Fixture fx = new Fixture(wechatOrder(null));
        RefundTask existing = new RefundTask();
        existing.setId(77);
        existing.setRefundId(REFUND_ID);
        fx.tasks.add(existing);

        RefundSettlement.Result result = fx.settlement.settle(intent(wechatOrder(null), fx));

        check("复用返回 WECHAT_OUTBOX + reused=true",
                result.channel() == RefundSettlement.Channel.WECHAT_OUTBOX && result.reused(),
                String.valueOf(result.channel()));
        check("复用不再创建任务", fx.createdRefunds.isEmpty(), String.valueOf(fx.createdRefunds.size()));
        check("复用不再锁单放行", !fx.orderLocked, String.valueOf(fx.orderLocked));
    }

    // ---- 3. 失败路径：放行时订单状态已变 → 拒绝且不落任务 ----------------------

    private static void wechatClaimGuardRejectsChangedOrder() {
        System.out.println("[失败路径：微信放行时订单已退款/状态变化 → 报错，不落任务]");
        Fixture fx = new Fixture(wechatOrder(null));
        RechargeOrder changed = wechatOrder(null);
        changed.setRefundStatus(1); // 行锁读到的已是已退款
        fx.lockedOrder = changed;
        fx.tasks.add(null);

        try {
            fx.settlement.settle(intent(wechatOrder(null), fx));
            check("订单状态已变时应拒绝", false, "未抛异常");
        } catch (OperateException e) {
            check("订单状态已变 → 拒绝放行",
                    String.valueOf(e.getMsg()).contains("退款状态已变化"), String.valueOf(e.getMsg()));
        }
        check("拒绝后不创建任务", fx.createdRefunds.isEmpty(), String.valueOf(fx.createdRefunds.size()));
    }

    // ---- 4. 钱包支付 → 同步退回余额 -------------------------------------------

    private static void walletPaidRefundsBalanceAtomically() {
        System.out.println("[钱包渠道：payWay=1 → FinanceRechargerService.refund 同步退回]");
        Fixture fx = new Fixture(walletOrder());

        RefundSettlement.Result result = fx.settlement.settle(intent(walletOrder(), fx));

        check("钱包渠道返回 WALLET", result.channel() == RefundSettlement.Channel.WALLET,
                String.valueOf(result.channel()));
        check("钱包退款 1 次：orderId/adminId/金额透传",
                fx.walletRefunds.size() == 1 && fx.walletRefunds.get(0)[0].equals(ORDER_ID)
                        && fx.walletRefunds.get(0)[1].equals(ADMIN_ID)
                        && AMOUNT.compareTo((BigDecimal) fx.walletRefunds.get(0)[2]) == 0,
                String.valueOf(fx.walletRefunds));
        check("钱包路径不建 outbox 任务", fx.createdRefunds.isEmpty(),
                String.valueOf(fx.createdRefunds.size()));
        check("钱包路径不直接置订单已退", fx.orderUpdates.isEmpty(),
                String.valueOf(fx.orderUpdates.size()));
    }

    // ---- 5. 失败路径：钱包退款异常 → 原样上抛（审核事务回滚） ------------------

    private static void walletFailurePropagates() {
        System.out.println("[失败路径：钱包退款抛错 → 异常上抛，审核整体回滚]");
        Fixture fx = new Fixture(walletOrder());
        fx.walletError = new OperateException("用户余额已不足退款金额");

        try {
            fx.settlement.settle(intent(walletOrder(), fx));
            check("钱包退款失败应上抛", false, "未抛异常");
        } catch (OperateException e) {
            check("钱包退款异常原样上抛", String.valueOf(e.getMsg()).contains("余额已不足"),
                    String.valueOf(e.getMsg()));
        }
        check("失败后不落其它渠道出款", fx.orderUpdates.isEmpty() && fx.createdRefunds.isEmpty(),
                "orderUpdates=" + fx.orderUpdates.size() + " tasks=" + fx.createdRefunds.size());
    }

    // ---- 6. 线下/其它渠道 → 条件更新 refund_status 0→1，审核即完成 -------------

    private static void offlineOrderSettlesDirectly() {
        System.out.println("[线下渠道：非微信/钱包 → 条件更新订单已退，不建任务不动余额]");
        Fixture fx = new Fixture(offlineOrder());

        RefundSettlement.Result result = fx.settlement.settle(intent(offlineOrder(), fx));

        check("线下渠道返回 OFFLINE", result.channel() == RefundSettlement.Channel.OFFLINE,
                String.valueOf(result.channel()));
        check("线下路径置订单已退 1 次", fx.orderUpdates.size() == 1, String.valueOf(fx.orderUpdates.size()));
        // 置已退写的是 refund_status 列；0→1 条件与并发闸门行为由真库 e2e 断言
        // （final2 课程线下渠道 / activity-cancel 活动线下渠道均核对订单 refund_status）。
        check("线下更新写 refund_status 列", fx.orderUpdates.size() == 1
                        && setSql(fx.orderUpdates.get(0)).contains("refund_status"),
                fx.orderUpdates.size() == 1 ? setSql(fx.orderUpdates.get(0)) : "");
        check("线下路径不建任务不动余额", fx.createdRefunds.isEmpty() && fx.walletRefunds.isEmpty(),
                "tasks=" + fx.createdRefunds.size() + " wallet=" + fx.walletRefunds.size());
    }

    // ---- 7. 金额非正 → 不出款 --------------------------------------------------

    private static void nonPositiveAmountSettlesNothing() {
        System.out.println("[零元/负额：不出款、不建任务、不动订单]");
        Fixture fxNull = new Fixture(walletOrder());
        check("金额 null → NONE 不出款",
                fxNull.settlement.settle(intent(walletOrder(), fxNull, null)).channel()
                        == RefundSettlement.Channel.NONE,
                "");
        check("金额 null 无任何副作用", fxNull.totalInteractions() == 0, fxNull.interactions());

        Fixture fxZero = new Fixture(walletOrder());
        check("金额 0 → NONE 不出款",
                fxZero.settlement.settle(intent(walletOrder(), fxZero, BigDecimal.ZERO)).channel()
                        == RefundSettlement.Channel.NONE,
                "");
        check("金额 0 无任何副作用", fxZero.totalInteractions() == 0, fxZero.interactions());
    }

    // ---- 8. 订单不可退 → 不出款（课程链路静默跳过语义） ------------------------

    private static void nonRefundableOrderSettlesNothing() {
        System.out.println("[订单不可退（已退款/已删/未支付）→ 跳过出款]");
        RechargeOrder refunded = walletOrder();
        refunded.setRefundStatus(1);
        Fixture fx = new Fixture(refunded);
        check("订单已退款 → NONE 不出款",
                fx.settlement.settle(intent(refunded, fx)).channel() == RefundSettlement.Channel.NONE,
                "");
        check("订单不可退无任何副作用", fx.totalInteractions() == 0, fx.interactions());
    }

    // ---- 夹具 ------------------------------------------------------------------

    private static RechargeOrder wechatOrder(String transactionId) {
        RechargeOrder order = baseOrder();
        order.setPayWay(2);
        order.setTransactionId(transactionId != null ? transactionId : "4200MOCKTXN0001");
        return order;
    }

    private static RechargeOrder walletOrder() {
        RechargeOrder order = baseOrder();
        order.setPayWay(1);
        return order;
    }

    private static RechargeOrder offlineOrder() {
        RechargeOrder order = baseOrder();
        order.setPayWay(3); // 线下转账/历史无交易号
        return order;
    }

    private static RechargeOrder baseOrder() {
        RechargeOrder order = new RechargeOrder();
        order.setId(ORDER_ID);
        order.setUserId(USER_ID);
        order.setPayStatus(1);
        order.setRefundStatus(0);
        order.setOrderAmount(new BigDecimal("49.00"));
        return order;
    }

    private static RefundSettlement.Intent intent(RechargeOrder order, Fixture fx) {
        return intent(order, fx, AMOUNT);
    }

    private static RefundSettlement.Intent intent(RechargeOrder order, Fixture fx, BigDecimal amount) {
        fx.intentRefund = refund();
        fx.intentOrder = order;
        return RefundSettlement.Intent.of(fx.intentRefund, order, ADMIN_ID,
                RefundSettlement.BusinessType.COURSE, amount);
    }

    private static CourseRefund refund() {
        CourseRefund refund = new CourseRefund();
        refund.setId(REFUND_ID);
        refund.setUserId(USER_ID);
        refund.setCourseClassId(88);
        refund.setRefundAmount(AMOUNT);
        return refund;
    }

    /** 记录出款交互的夹具：mapper/服务全为动态代理。 */
    private static class Fixture {
        final RefundSettlement settlement = new RefundSettlement();
        final List<RefundTask> tasks = new ArrayList<>();
        final List<Object[]> walletRefunds = new ArrayList<>();
        final List<CourseRefund> createdRefunds = new ArrayList<>();
        final List<RechargeOrder> createdOrders = new ArrayList<>();
        final List<Object> orderUpdates = new ArrayList<>();
        final List<String> orderUpdateValues = new ArrayList<>();
        final List<String> journal = new ArrayList<>();
        RechargeOrder lockedOrder;
        OperateException walletError;
        boolean orderLocked;
        CourseRefund intentRefund;
        RechargeOrder intentOrder;

        Fixture(RechargeOrder lockedOrderRead) {
            this.lockedOrder = lockedOrderRead;
            set(settlement, "refundTaskMapper", proxy(RefundTaskMapper.class, (self, method, args1) -> {
                journal.add("taskMapper." + method.getName());
                if ("selectOne".equals(method.getName())) {
                    return tasks.isEmpty() ? null : tasks.remove(0);
                }
                return 0;
            }));
            set(settlement, "rechargeOrderMapper", proxy(RechargeOrderMapper.class, (self, method, args1) -> {
                journal.add("orderMapper." + method.getName());
                if ("selectOne".equals(method.getName())) {
                    orderLocked = true;
                    return lockedOrder;
                }
                if ("update".equals(method.getName())) {
                    orderUpdates.add(args1[1]);
                    collectValues(args1[1]);
                    return 1;
                }
                return 0;
            }));
            set(settlement, "refundTaskService", proxy(IRefundTaskService.class, (self, method, args1) -> {
                journal.add("refundTaskService." + method.getName());
                if ("createForRefund".equals(method.getName())) {
                    createdRefunds.add((CourseRefund) args1[0]);
                    createdOrders.add((RechargeOrder) args1[1]);
                    return null;
                }
                return null;
            }));
            set(settlement, "financeRechargerService", proxy(IFinanceRechargerService.class,
                    (self, method, args1) -> {
                        journal.add("financeRechargerService." + method.getName());
                        if ("refund".equals(method.getName()) && args1.length == 3) {
                            walletRefunds.add(new Object[]{args1[0], args1[1], args1[2]});
                            if (walletError != null) {
                                throw walletError;
                            }
                        }
                        return null;
                    }));
        }

        /** UpdateWrapper 的 set/eq 值都落在 paramNameValuePairs，抽出来断言条件内容。 */
        private void collectValues(Object wrapper) {
            try {
                Method values = wrapper.getClass().getMethod("getParamNameValuePairs");
                Object pairs = values.invoke(wrapper);
                if (pairs instanceof Map) {
                    for (Object value : ((Map<?, ?>) pairs).values()) {
                        orderUpdateValues.add(String.valueOf(value));
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        int totalInteractions() {
            return journal.size();
        }

        String interactions() {
            return String.valueOf(journal);
        }

        RefundSettlement.Result settle(RefundSettlement.Intent intent) {
            return settlement.settle(intent);
        }
    }

    private static String setSql(Object wrapper) {
        try {
            Method sqlSet = wrapper.getClass().getMethod("getSqlSet");
            return String.valueOf(sqlSet.invoke(wrapper));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static void set(Object target, String field, Object value) {
        try {
            Field declared = target.getClass().getDeclaredField(field);
            declared.setAccessible(true);
            declared.set(target, value);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
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
