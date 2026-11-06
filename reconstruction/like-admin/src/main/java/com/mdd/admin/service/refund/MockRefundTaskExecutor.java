package com.mdd.admin.service.refund;

import com.mdd.common.entity.refund.RefundTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 本地/测试退款执行器（#60、#124、#214）：{@code like.refund.mock-enabled=true} 且
 * profile 不是 production/experience 时启用，生产/体验默认不存在该 bean。
 *
 * <p>生产/体验误开该开关时 {@code RuntimeModeGuard} 会在启动阶段直接失败，
 * 不会出现「退款显示成功、实际未到账」的假成功。</p>
 *
 * <p>可控场景：</p>
 * <ul>
 *   <li>交易号以 {@code MOCKFAIL} 开头：首次执行失败，重试后成功；</li>
 *   <li>交易号以 {@code MOCKPROCESSING} 开头：执行只返回「受理」，对账查询在
 *       {@code result_msg=MOCK_SETTLE} 后返回成功 —— 用于验证「受理≠已退」；</li>
 *   <li>其余：直接成功。</li>
 * </ul>
 */
@Primary
@Component
@Profile({"dev", "test", "local"})
@ConditionalOnProperty(prefix = "like.refund", name = "mock-enabled", havingValue = "true")
public class MockRefundTaskExecutor implements RefundTaskExecutor {
    private static final Logger log = LoggerFactory.getLogger(MockRefundTaskExecutor.class);

    private static final String FAIL_MARKER = "MOCKFAIL";
    private static final String PROCESSING_MARKER = "MOCKPROCESSING";
    private static final String SETTLE_MARKER = "MOCK_SETTLE";

    @Override
    public RefundExecutionResult execute(RefundTask task) {
        int retryCount = task.getRetryCount() == null ? 0 : task.getRetryCount();
        String transactionId = task.getTransactionId() == null ? "" : task.getTransactionId();
        if (transactionId.startsWith(FAIL_MARKER) && retryCount == 0) {
            throw new IllegalStateException("mock 微信退款失败（可控故障，重试后成功）");
        }
        String refundId = "MOCKREFUND" + task.getId();
        if (transactionId.startsWith(PROCESSING_MARKER)) {
            log.info("mock 微信退款已受理（等待对账）taskId={} outRefundNo={}", task.getId(), task.getOutRefundNo());
            return RefundExecutionResult.accepted(refundId, "mock 微信退款已受理，等待最终结果");
        }
        log.info("mock 微信退款成功 taskId={} outRefundNo={} retryCount={}",
                task.getId(), task.getOutRefundNo(), retryCount);
        return RefundExecutionResult.success(refundId, "mock 微信退款成功");
    }

    @Override
    public RefundExecutionResult query(RefundTask task) {
        String refundId = "MOCKREFUND" + task.getId();
        String resultMsg = task.getResultMsg() == null ? "" : task.getResultMsg();
        if (resultMsg.contains(SETTLE_MARKER)) {
            log.info("mock 微信退款对账成功 taskId={} outRefundNo={}", task.getId(), task.getOutRefundNo());
            return RefundExecutionResult.success(refundId, "mock 微信退款对账成功");
        }
        return RefundExecutionResult.accepted(refundId, "mock 微信退款处理中");
    }
}
