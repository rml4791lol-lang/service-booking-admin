package com.mdd.admin.service.refund;

import com.mdd.admin.service.IRefundTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 退款任务后台清扫（#60）：兜底补偿 after-commit 事件丢失、进程中断、超时执行中的任务。
 * 条件更新抢占保证多实例/多线程下同一任务只执行一次。
 *
 * <p>漏跑补偿（2026-11-07 定时任务审计）：本任务不是 Quartz 任务，而是 Spring
 * {@code @Scheduled(fixedDelay)}，容器启动后立即执行第一轮（无 initialDelay），
 * 停机期间待执行/待重试任务会在启动后被扫到，天然具备重启补跑；每轮都按「到期」
 * 条件全量查询，不依赖上一轮是否执行。与 Quartz 任务不同，它不会出现在 la_dev_crontab，
 * 心跳以日志与 la_refund_task 队列状态体现；运维脚本按「待执行超过 10 分钟」
 * 判定清扫停摆。</p>
 *
 * <p>异常隔离：单个任务派发失败（线程池拒绝等）只跳过该任务并继续本轮剩余派发与对账，
 * 避免一个坏任务拖住整条队列一个周期。</p>
 */
@Component
public class RefundTaskScheduler {
    private static final Logger log = LoggerFactory.getLogger(RefundTaskScheduler.class);
    /** 心跳日志间隔（轮）：5 秒一轮时约 5 分钟一条，用于确认调度线程还活着。 */
    private static final int HEARTBEAT_SWEEPS = 60;

    @Resource
    IRefundTaskService refundTaskService;
    @Resource
    RefundTaskDispatcher refundTaskDispatcher;

    @Value("${like.refund.sweep-batch-size:20}")
    private int batchSize;

    private long sweeps;
    private long requeuedTotal;
    private long dispatchedTotal;
    private long confirmedTotal;

    /** #214：PROCESSING 超时告警阈值（秒）。 */
    @Value("${like.refund.processing-alert-seconds:1800}")
    private int processingAlertSeconds;

    @Scheduled(fixedDelayString = "${like.refund.sweep-interval-ms:5000}")
    public void sweep() {
        try {
            int requeued = refundTaskService.requeueStale(batchSize);
            if (requeued > 0) {
                log.warn("退款任务清扫：{} 条执行中超时任务已重排队或转失败", requeued);
            }
            List<Integer> taskIds = refundTaskService.listDueTaskIds(batchSize);
            int dispatched = 0;
            for (Integer taskId : taskIds) {
                try {
                    refundTaskDispatcher.dispatch(taskId);
                    dispatched++;
                } catch (Exception e) {
                    log.error("退款任务派发失败，本轮继续处理其余任务 taskId={}", taskId, e);
                }
            }
            // #214：微信退款受理后（PROCESSING）必须对账确认 SUCCESS 才能置订单已退款。
            int confirmed = refundTaskService.reconcileProcessing(batchSize);
            if (confirmed > 0) {
                log.info("退款任务对账：{} 条退款中任务确认成功", confirmed);
            }

            sweeps++;
            requeuedTotal += requeued;
            dispatchedTotal += dispatched;
            confirmedTotal += confirmed;
            if (sweeps % HEARTBEAT_SWEEPS == 0) {
                log.info("退款任务清扫心跳：已完成{}轮，累计重新排队{}，派发{}，对账确认{}",
                        sweeps, requeuedTotal, dispatchedTotal, confirmedTotal);
            }
        } catch (Exception e) {
            // 清扫失败不能影响应用主流程，下一轮继续。
            log.error("退款任务清扫异常", e);
        }
    }

    /**
     * 对账超时告警（#214 观测补强）：微信侧长期 PROCESSING 不改变任务状态，
     * 但必须留下 error 级别告警供值班/对账发现，避免资金异常静默。
     */
    @Scheduled(fixedDelayString = "${like.refund.processing-alert-interval-ms:60000}")
    public void alertStuckProcessing() {
        try {
            int count = refundTaskService.alertStuckProcessing(processingAlertSeconds, batchSize);
            if (count > 0) {
                log.error("退款任务对账超时告警：{} 条退款中任务超过 {} 秒未确认，需人工核查",
                        count, processingAlertSeconds);
            }
        } catch (Exception e) {
            log.error("退款任务对账超时告警检查异常", e);
        }
    }
}
