package com.mdd.front.service;

import com.mdd.common.entity.RechargeOrder;

import java.util.List;
import java.util.Map;

/**
 * 活动报名（T29.7 / #27）。
 *
 * <p>计费口径：活动 {@code fee} 为每人费用，订单金额 = fee ×（参与学员人数 + 陪同人数）；
 * 下单即按总人头预占名额，支付回调只把报名明细从待支付改为已支付。</p>
 */
public interface IActivityService {

    /** 活动列表（公开；登录时附带 signed）。 */
    List<Map<String, Object>> list(Integer userId);

    /** 活动详情（公开；登录时附带 signed）。 */
    Map<String, Object> detail(Integer userId, Integer activityId);

    /** 创建活动报名订单并按总人头（学员 + 陪同）预占名额。 */
    Map<String, Object> signup(Integer userId, Integer terminal, Integer activityId,
                              List<Integer> studentIds, Integer companionCount);

    /** 取消当前账号在该活动的报名：免费直接取消释放名额，付费生成退款申请（幂等）。 */
    Map<String, Object> cancel(Integer userId, Integer activityId);

    /** mock 回调前校验订单归属与类型。 */
    RechargeOrder requireMockableOrder(Integer userId, Integer orderId);

    /** 支付成功回调：订单置已支付，报名明细置已支付（幂等）。 */
    void issueForPaidOrder(String outTradeNo, String transactionId);

    /** 支付描述（微信 127 字节内）。 */
    String payDescription(RechargeOrder order);
}
