package com.mdd.admin.service.refund;

/**
 * 退款执行结果（#60/#214）：执行器只负责调用渠道并返回渠道单号与渠道状态，
 * 状态落库由 outbox 服务统一处理。
 *
 * <p>#214 起区分「渠道已受理」与「渠道确认成功」：微信退款接口返回 PROCESSING 只代表
 * 受理，资金尚未到账；只有 SUCCESS（或后续对账查询 SUCCESS）才能把订单置为已退款。</p>
 */
public class RefundExecutionResult {

    /** 渠道状态。 */
    public enum Status {
        /** 渠道确认退款成功（资金已退出）。 */
        SUCCESS,
        /** 渠道已受理，等待最终结果（异步退款）。 */
        PROCESSING,
        /** 渠道明确拒绝/关闭，需人工处理。 */
        FAILED
    }

    private final Status status;
    private final String refundId;
    private final String message;

    private RefundExecutionResult(Status status, String refundId, String message) {
        this.status = status;
        this.refundId = refundId;
        this.message = message;
    }

    /** 渠道确认成功。 */
    public static RefundExecutionResult success(String refundId, String message) {
        return new RefundExecutionResult(Status.SUCCESS, refundId, message);
    }

    /** 渠道已受理，等待最终结果。 */
    public static RefundExecutionResult accepted(String refundId, String message) {
        return new RefundExecutionResult(Status.PROCESSING, refundId, message);
    }

    /** 渠道明确失败/关闭。 */
    public static RefundExecutionResult failed(String refundId, String message) {
        return new RefundExecutionResult(Status.FAILED, refundId, message);
    }

    public Status getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isProcessing() {
        return status == Status.PROCESSING;
    }

    public boolean isFailed() {
        return status == Status.FAILED;
    }

    public String getRefundId() {
        return refundId;
    }

    public String getMessage() {
        return message;
    }
}
