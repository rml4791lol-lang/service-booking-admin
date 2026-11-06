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
 */
@Component
public class RefundTaskScheduler {
    private static final Logger log = LoggerFactory.getLogger(RefundTaskScheduler.class);

    @Resource
    IRefundTaskService refundTaskService;
    @Resource
    RefundTaskDispatcher refundTaskDispatcher;

    @Value("${like.refund.sweep-batch-size:20}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${like.refund.sweep-interval-ms:5000}")
    public void sweep() {
        try {
            int requeued = refundTaskService.requeueStale(batchSize);
            if (requeued > 0) {
                log.warn("退款任务清扫：{} 条执行中超时任务重新排队", requeued);
            }
            List<Integer> taskIds = refundTaskService.listDueTaskIds(batchSize);
            for (Integer taskId : taskIds) {
                refundTaskDispatcher.dispatch(taskId);
            }
            // #214：微信退款受理后（PROCESSING）必须对账确认 SUCCESS 才能置订单已退款。
            int confirmed = refundTaskService.reconcileProcessing(batchSize);
            if (confirmed > 0) {
                log.info("退款任务对账：{} 条退款中任务确认成功", confirmed);
            }
        } catch (Exception e) {
            // 清扫失败不能影响应用主流程，下一轮继续。
            log.error("退款任务清扫异常", e);
        }
    }
}
