package com.mdd.admin.service.refund;

/**
 * 退款任务创建/重试事件（#60）：事务提交后触发异步执行。
 */
public class RefundTaskCreatedEvent {
    private final Integer taskId;

    public RefundTaskCreatedEvent(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskId() {
        return taskId;
    }
}
