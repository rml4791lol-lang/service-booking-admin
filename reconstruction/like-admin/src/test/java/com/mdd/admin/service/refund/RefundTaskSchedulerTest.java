package com.mdd.admin.service.refund;

import com.mdd.admin.service.IRefundTaskService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 退款任务清扫器的异常隔离断言器（2026-11-07 定时任务审计）。
 *
 * <p>seam：{@link RefundTaskScheduler#sweep()}。审计背景：退款任务不是 Quartz 任务，
 * 而是 Spring {@code @Scheduled(fixedDelay=5s)}，容器启动即执行一轮（无 initialDelay），
 * 重启天然补跑；但它在一个循环里逐个 dispatch。旧实现只要某个任务派发抛异常
 * （线程池拒绝、任务服务异常），本轮剩余任务与退款对账（reconcileProcessing）都会
 * 被跳过，坏任务会把整条队列拖住一个周期，且对账长期得不到执行。</p>
 *
 * <p>断言：单个任务派发失败不影响本轮其余任务与对账；服务层异常不冒泡到调度线程。</p>
 *
 * <p>执行：{@code bash tools/ops/run-jobs-resilience-test.sh}</p>
 */
public final class RefundTaskSchedulerTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        dispatchesAllDueTasks();
        continuesAfterDispatchFailure();
        serviceFailureIsSwallowed();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---- 1. 正常一轮：全部派发 + 对账 -----------------------------------------

    private static void dispatchesAllDueTasks() {
        System.out.println("[正常清扫：到期任务逐个派发，随后对账]");
        Fixture fx = new Fixture(Arrays.asList(11, 12, 13));

        fx.scheduler.sweep();

        check("三个到期任务都派发", fx.dispatched.equals(Arrays.asList(11, 12, 13)),
                String.valueOf(fx.dispatched));
        check("本轮执行了退款对账", fx.reconcileCalled, String.valueOf(fx.reconcileCalled));
        check("清扫器确实先扫超时任务", fx.requeueCalled, String.valueOf(fx.requeueCalled));
    }

    // ---- 2. 单个任务派发失败，不阻断同轮其它任务与对账 -------------------------

    private static void continuesAfterDispatchFailure() {
        System.out.println("[异常隔离：2 号任务派发抛异常，1/3 号与对账仍要执行]");
        Fixture fx = new Fixture(Arrays.asList(11, 21, 31));
        fx.failDispatchId = 21;

        fx.scheduler.sweep();

        check("失败的 2 号仍被尝试", fx.dispatched.contains(21), String.valueOf(fx.dispatched));
        check("1/3 号继续派发", fx.dispatched.equals(Arrays.asList(11, 21, 31)),
                String.valueOf(fx.dispatched));
        check("派发异常不跳过对账", fx.reconcileCalled, String.valueOf(fx.reconcileCalled));
    }

    // ---- 3. 服务层异常不冒泡（调度线程保活） ----------------------------------

    private static void serviceFailureIsSwallowed() {
        System.out.println("[兜底：任务服务异常不冒泡，下一轮继续]");
        Fixture fx = new Fixture(Arrays.asList(11));
        fx.listDueError = new IllegalStateException("模拟查询失败");

        boolean threw = false;
        try {
            fx.scheduler.sweep();
        } catch (Exception e) {
            threw = true;
        }

        check("sweep 不外抛异常", !threw, String.valueOf(threw));
        check("失败轮不执行对账", !fx.reconcileCalled, String.valueOf(fx.reconcileCalled));
    }

    // ---- 夹具 ----------------------------------------------------------------

    private static final class Fixture {
        final List<Integer> dueTaskIds;
        final List<Integer> dispatched = new ArrayList<>();
        final RefundTaskScheduler scheduler = new RefundTaskScheduler();
        Integer failDispatchId;
        RuntimeException listDueError;
        boolean reconcileCalled;
        boolean requeueCalled;

        Fixture(List<Integer> dueTaskIds) {
            this.dueTaskIds = dueTaskIds;
            scheduler.refundTaskService = fakeService();
            scheduler.refundTaskDispatcher = fakeDispatcher();
        }

        private IRefundTaskService fakeService() {
            return proxy(IRefundTaskService.class, (proxy, method, args) -> {
                if ("requeueStale".equals(method.getName())) {
                    requeueCalled = true;
                    return 1;
                }
                if ("listDueTaskIds".equals(method.getName())) {
                    if (listDueError != null) {
                        throw listDueError;
                    }
                    return dueTaskIds;
                }
                if ("reconcileProcessing".equals(method.getName())) {
                    reconcileCalled = true;
                    return 2;
                }
                return 0;
            });
        }

        private RefundTaskDispatcher fakeDispatcher() {
            return new RefundTaskDispatcher() {
                @Override
                public void dispatch(Integer taskId) {
                    dispatched.add(taskId);
                    if (failDispatchId != null && failDispatchId.equals(taskId)) {
                        throw new IllegalStateException("模拟线程池拒绝 taskId=" + taskId);
                    }
                }
            };
        }
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
