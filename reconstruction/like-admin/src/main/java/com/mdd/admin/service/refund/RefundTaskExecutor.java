package com.mdd.admin.service.refund;

import com.mdd.common.entity.refund.RefundTask;

/**
 * 退款执行器（#60/#214）。
 *
 * 真实环境使用 {@link WechatRefundTaskExecutor}；本地/测试可用 mock 执行器替代，
 * outbox 状态机与重试逻辑不依赖具体渠道。
 *
 * <p>{@link #execute(RefundTask)} 负责发起退款（可能只得到「受理」）；
 * {@link #query(RefundTask)} 负责对账查询渠道最终状态，只有 SUCCESS 才能把订单置为已退款。</p>
 */
public interface RefundTaskExecutor {

    /** 发起退款。返回 PROCESSING 表示渠道已受理、等待最终结果。 */
    RefundExecutionResult execute(RefundTask task) throws Exception;

    /** 对账查询：按 out_refund_no 查询渠道最终状态。 */
    RefundExecutionResult query(RefundTask task) throws Exception;
}
