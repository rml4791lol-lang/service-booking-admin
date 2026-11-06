package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.util.AmountUtil;
import com.mdd.common.util.SensitiveUtils;
import com.mdd.front.service.IPayMockGatewayService;
import com.mdd.front.service.IPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 支付 mock 网关实现（T30.11、#124）。
 *
 * <p>成功/重复回调走真实回调服务；失败/取消按老系统口径把订单置为
 * {@code pay_status=4}（已取消），不写券、不写流水。这样本地可以把
 * “支付成功、支付失败、用户取消、微信重复通知”四种场景都验到。</p>
 *
 * <p>只在 {@code like.pay.mock-callback-enabled=true} 且 profile 不是
 * production/experience 时装配：生产/体验上下文里不存在该 bean，控制器改为
 * 可选注入并把 mock 路由统一返回「未开启」；开关误开由
 * {@code RuntimeModeGuard} 在启动时直接失败。</p>
 */
@Service
@Profile({"dev", "test", "local"})
@ConditionalOnProperty(prefix = "like.pay", name = "mock-callback-enabled", havingValue = "true")
public class PayMockGatewayServiceImpl implements IPayMockGatewayService {

    private static final Logger log = LoggerFactory.getLogger(PayMockGatewayServiceImpl.class);

    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    IPayService iPayService;

    @Value("${like.pay.mock-callback-enabled:false}")
    private boolean mockCallbackEnabled;

    @Override
    public boolean isMockCallbackEnabled() {
        return mockCallbackEnabled;
    }

    @Override
    @Transactional
    public Map<String, Object> notify(Integer userId, Integer orderId, String action, boolean couponOnly)
            throws WxPayException {
        if (!mockCallbackEnabled) {
            throw new OperateException("模拟支付网关未开启");
        }
        String normalized = normalizeAction(action);
        RechargeOrder order = requireOrder(userId, orderId);
        if (couponOnly && (order.getCouponGoodsId() == null || order.getCouponGoodsId() <= 0)) {
            throw new OperateException("订单不是券订单");
        }

        String attach = resolveAttach(order);
        int beforeCoupons = countCoupons(order.getId());
        boolean idempotent = false;

        switch (normalized) {
            case "success": {
                issue(order, attach, "MOCK" + System.currentTimeMillis());
                idempotent = beforeCoupons > 0;
                break;
            }
            case "duplicate": {
                // 同一笔交易连续通知两次，验证订单/券/流水的幂等。
                String transactionId = "MOCKDUP" + System.currentTimeMillis();
                issue(order, attach, transactionId);
                int afterFirst = countCoupons(order.getId());
                int flowsAfterFirst = countFlows(order.getId());
                issue(order, attach, transactionId);
                idempotent = countCoupons(order.getId()) == afterFirst
                        && countFlows(order.getId()) == flowsAfterFirst;
                break;
            }
            case "fail":
            case "cancel": {
                idempotent = closeOrder(order, normalized);
                break;
            }
            default: {
                throw new OperateException("不支持的 mock 支付动作：" + normalized);
            }
        }

        RechargeOrder fresh = requireOrder(userId, orderId);
        int couponCount = countCoupons(order.getId());
        int flowCount = countFlows(order.getId());
        CouponInstance coupon = couponInstanceMapper.selectOne(new QueryWrapper<CouponInstance>()
                .eq("order_id", order.getId()).orderByAsc("order_seq").last("limit 1"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("orderId", fresh.getId());
        result.put("orderSn", fresh.getSn());
        result.put("action", normalized);
        result.put("payStatus", fresh.getPayStatus());
        result.put("couponId", coupon == null ? null : coupon.getId());
        result.put("couponCount", couponCount);
        result.put("flowCount", flowCount);
        result.put("idempotent", idempotent);
        result.put("mocked", true);
        log.info("支付 mock 网关通知完成 action={} orderSn={} payStatus={} couponCount={} flowCount={}",
                normalized, SensitiveUtils.maskOrderSn(fresh.getSn()), fresh.getPayStatus(),
                couponCount, flowCount);
        return result;
    }

    private void issue(RechargeOrder order, String attach, String transactionId) throws WxPayException {
        Integer paidFen = order.getOrderAmount() == null
                ? null : AmountUtil.yuan2Fen(order.getOrderAmount().toPlainString());
        // 模拟的是「微信支付成功回调」，而真实链路的 pay_way 由 /api/pay/prepay 在发起支付时写入。
        // 本地 mock 绕过 prepay，这里把缺省渠道回填成微信支付，否则退款链路无法判定「原路退回」
        // （钱包支付走 prepay 时已写 pay_way=1，不会被覆盖）。
        if (order.getPayWay() == null) {
            order.setPayWay(PaymentEnum.WX_PAY.getCode());
            order.setUpdateTime(System.currentTimeMillis() / 1000L);
            rechargeOrderMapper.updateById(order);
        }
        iPayService.handlePaidNotify(attach, order.getSn(), transactionId, paidFen);
    }

    /**
     * 关闭订单。已是取消态则幂等返回 true；已支付订单不允许改为失败/取消。
     */
    private boolean closeOrder(RechargeOrder order, String action) {
        Integer status = order.getPayStatus();
        if (status != null && status == PaymentEnum.OK_PAID.getCode()) {
            throw new OperateException("订单已支付，不能标记为失败或取消");
        }
        if (status != null && status == PaymentEnum.CLOSED.getCode()) {
            return true;
        }
        long now = System.currentTimeMillis() / 1000L;
        order.setPayStatus(PaymentEnum.CLOSED.getCode());
        order.setUpdateTime(now);
        rechargeOrderMapper.updateById(order);
        log.info("支付 mock 网关将订单置为已取消 action={} orderSn={}",
                action, SensitiveUtils.maskOrderSn(order.getSn()));
        return false;
    }

    private RechargeOrder requireOrder(Integer userId, Integer orderId) {
        if (orderId == null || orderId <= 0) {
            throw new OperateException("订单不存在");
        }
        RechargeOrder order = rechargeOrderMapper.selectById(orderId);
        if (order == null || order.getDeleteTime() != null
                || userId == null || !userId.equals(order.getUserId())) {
            throw new OperateException("订单不存在");
        }
        return order;
    }

    private String resolveAttach(RechargeOrder order) {
        if (order.getCouponGoodsId() != null && order.getCouponGoodsId() > 0) {
            return "coupon";
        }
        if (order.getActivityId() != null && order.getActivityId() > 0) {
            // 活动订单 course_class_id 恒为 0，必须排在 course/recharge 之前，否则会被当成充值入账。
            return "activity";
        }
        if (order.getCourseClassId() != null && order.getCourseClassId() > 0) {
            return "course";
        }
        return "recharge";
    }

    private int countCoupons(Integer orderId) {
        Long count = couponInstanceMapper.selectCount(new QueryWrapper<CouponInstance>()
                .eq("order_id", orderId));
        return count == null ? 0 : count.intValue();
    }

    private int countFlows(Integer orderId) {
        Long count = couponFlowMapper.selectCount(new QueryWrapper<CouponFlow>()
                .eq("order_id", orderId));
        return count == null ? 0 : count.intValue();
    }

    private String normalizeAction(String action) {
        String text = action == null ? "" : action.trim().toLowerCase(Locale.ROOT);
        if (text.isEmpty() || "ok".equals(text) || "paid".equals(text) || "pay".equals(text)) {
            return "success";
        }
        if ("failed".equals(text) || "failure".equals(text)) {
            return "fail";
        }
        if ("cancelled".equals(text) || "canceled".equals(text)) {
            return "cancel";
        }
        if ("repeat".equals(text) || "retry".equals(text)) {
            return "duplicate";
        }
        return text;
    }
}
