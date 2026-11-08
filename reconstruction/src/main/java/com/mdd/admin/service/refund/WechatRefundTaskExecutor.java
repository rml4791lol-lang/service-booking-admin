package com.mdd.admin.service.refund;

import com.mdd.common.entity.refund.RefundTask;
import com.mdd.common.plugin.wechat.WxPayDriver;
import com.mdd.common.plugin.wechat.request.RefundRequestV3;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 微信退款执行器（#60/#214）：真实商户号到位后只需保证该执行器的支付配置可用，
 * outbox 任务/重试/幂等不随之改动。out_refund_no 在任务创建时固定，重试复用。
 *
 * <p>#214：微信退款 v3 返回 {@code status=PROCESSING} 只代表受理，资金尚未到账；
 * 这里不能当成功返回，交给 outbox 落「退款中」，由对账查询确认 SUCCESS。</p>
 */
@Component
public class WechatRefundTaskExecutor implements RefundTaskExecutor {
    private static final Logger log = LoggerFactory.getLogger(WechatRefundTaskExecutor.class);

    @Override
    public RefundExecutionResult execute(RefundTask task) throws Exception {
        RefundRequestV3 request = new RefundRequestV3();
        request.setTransactionId(task.getTransactionId());
        request.setOutTradeNo(task.getOutTradeNo());
        request.setOutRefundNo(task.getOutRefundNo());
        request.setTotalAmount(task.getTotalFee());
        request.setRefundAmount(task.getRefundFee());
        request.setReason("用户退课退款");

        WxPayRefundV3Result result = WxPayDriver.refund(request);
        if (result == null) {
            // 没有渠道返回结果不能当成功，否则后台会把未受理退款显示成「退款成功」（#60）。
            throw new IllegalStateException("微信退款未返回受理结果");
        }
        String status = normalize(result.getStatus());
        if ("SUCCESS".equals(status)) {
            return RefundExecutionResult.success(result.getRefundId(), "微信退款成功");
        }
        if ("PROCESSING".equals(status) || status.isEmpty()) {
            log.warn("微信退款已受理，等待最终结果 taskId={} outRefundNo={} status={}",
                    task.getId(), task.getOutRefundNo(), status);
            return RefundExecutionResult.accepted(result.getRefundId(), "微信退款已受理，等待最终结果");
        }
        // CLOSED / ABNORMAL：渠道明确拒绝或异常，不能静默当成功。
        throw new IllegalStateException("微信退款返回异常状态: " + status);
    }

    @Override
    public RefundExecutionResult query(RefundTask task) throws Exception {
        WxPayRefundQueryV3Result result = WxPayDriver.queryRefund(task.getOutRefundNo());
        if (result == null) {
            throw new IllegalStateException("微信退款查询未返回结果");
        }
        String status = normalize(result.getStatus());
        if ("SUCCESS".equals(status)) {
            return RefundExecutionResult.success(result.getRefundId(), "微信退款对账成功");
        }
        if ("PROCESSING".equals(status) || status.isEmpty()) {
            return RefundExecutionResult.accepted(result.getRefundId(), "微信退款处理中");
        }
        // CLOSED / ABNORMAL：终态失败，订单不得置已退款，等待人工处理。
        return RefundExecutionResult.failed(result.getRefundId(), "微信退款查询状态异常: " + status);
    }

    private String normalize(String status) {
        return status == null ? "" : status.trim().toUpperCase(Locale.ROOT);
    }
}
