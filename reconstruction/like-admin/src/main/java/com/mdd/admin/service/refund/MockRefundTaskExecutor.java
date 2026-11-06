package com.mdd.admin.service.refund;

import com.mdd.common.entity.refund.RefundTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 本地/测试退款执行器（#60、#124）：{@code like.refund.mock-enabled=true} 且
 * profile 不是 production/experience 时启用，生产/体验默认不存在该 bean。
 *
 * <p>生产/体验误开该开关时 {@code RuntimeModeGuard} 会在启动阶段直接失败，
 * 不会出现「退款显示成功、实际未到账」的假成功。</p>
 *
 * 为了让失败重试可端到端验证：原支付交易号以 MOCKFAIL 开头时，首次执行模拟渠道失败，
 * 重试（retry_count>0）后成功。其余情况直接成功并返回可对账的 mock 退款单号。
 */
@Primary
@Component
@Profile({"dev", "test", "local"})
@ConditionalOnProperty(prefix = "like.refund", name = "mock-enabled", havingValue = "true")
public class MockRefundTaskExecutor implements RefundTaskExecutor {
    private static final Logger log = LoggerFactory.getLogger(MockRefundTaskExecutor.class);

    private static final String FAIL_MARKER = "MOCKFAIL";

    @Override
    public RefundExecutionResult execute(RefundTask task) {
        int retryCount = task.getRetryCount() == null ? 0 : task.getRetryCount();
        String transactionId = task.getTransactionId() == null ? "" : task.getTransactionId();
        if (transactionId.startsWith(FAIL_MARKER) && retryCount == 0) {
            throw new IllegalStateException("mock 微信退款失败（可控故障，重试后成功）");
        }
        String refundId = "MOCKREFUND" + task.getId();
        log.info("mock 微信退款成功 taskId={} outRefundNo={} retryCount={}",
                task.getId(), task.getOutRefundNo(), retryCount);
        return RefundExecutionResult.success(refundId, "mock 微信退款受理成功");
    }
}
