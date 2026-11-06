package com.mdd.admin.service.refund;

import com.mdd.common.entity.refund.RefundTask;
import com.mdd.common.plugin.wechat.WxPayDriver;
import com.mdd.common.plugin.wechat.request.RefundRequestV3;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import org.springframework.stereotype.Component;

/**
 * 微信退款执行器（#60）：真实商户号到位后只需保证该执行器的支付配置可用，
 * outbox 任务/重试/幂等不随之改动。out_refund_no 在任务创建时固定，重试复用。
 */
@Component
public class WechatRefundTaskExecutor implements RefundTaskExecutor {

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
        return RefundExecutionResult.success(result.getRefundId(), "微信退款受理成功");
    }
}
