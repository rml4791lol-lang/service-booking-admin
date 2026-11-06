package com.mdd.admin.service.refund;

/**
 * 退款执行结果（#60）：执行器只负责调用渠道并返回渠道单号，状态落库由 outbox 服务统一处理。
 */
public class RefundExecutionResult {
    private final String refundId;
    private final String message;

    public RefundExecutionResult(String refundId, String message) {
        this.refundId = refundId;
        this.message = message;
    }

    public static RefundExecutionResult success(String refundId, String message) {
        return new RefundExecutionResult(refundId, message);
    }

    public String getRefundId() {
        return refundId;
    }

    public String getMessage() {
        return message;
    }
}
