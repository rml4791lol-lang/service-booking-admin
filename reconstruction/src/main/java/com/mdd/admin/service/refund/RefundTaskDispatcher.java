package com.mdd.admin.service.refund;

import com.mdd.admin.service.IRefundTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * 退款任务派发器（#60）：审核/重试事务提交后再把任务丢进线程池，
 * 避免在数据库事务内调用微信退款，也避免事务回滚后仍执行退款。
 */
@Component
public class RefundTaskDispatcher {
    private static final Logger log = LoggerFactory.getLogger(RefundTaskDispatcher.class);

    @Resource(name = "taskExecutor")
    Executor taskExecutor;
    @Resource
    IRefundTaskService refundTaskService;

    public void dispatch(Integer taskId) {
        taskExecutor.execute(() -> {
            try {
                refundTaskService.executeTask(taskId);
            } catch (Exception e) {
                log.error("退款任务异步执行异常 taskId={}", taskId, e);
            }
        });
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onTaskCreated(RefundTaskCreatedEvent event) {
        dispatch(event.getTaskId());
    }
}
