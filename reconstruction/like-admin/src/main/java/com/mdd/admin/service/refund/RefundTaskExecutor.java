package com.mdd.admin.service.refund;

import com.mdd.common.entity.refund.RefundTask;

/**
 * 退款执行器（#60）。
 *
 * 真实环境使用 {@link WechatRefundTaskExecutor}；本地/测试可用 mock 执行器替代，
 * outbox 状态机与重试逻辑不依赖具体渠道。
 */
public interface RefundTaskExecutor {
    RefundExecutionResult execute(RefundTask task) throws Exception;
}
