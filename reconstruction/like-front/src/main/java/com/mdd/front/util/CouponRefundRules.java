package com.mdd.front.util;

import com.mdd.common.entity.coupon.CouponInstance;

import java.util.List;

/**
 * 券退款口径（#110 T12）：未使用的单券原路退；已使用 / 10 节课券包转客服。
 *
 * <p>口径来源：父票 #95 与决策 C3「未用单券原路退；已用/10 次包转客服」。券包按张落库
 * （V021，一单 N 行、每行 total=1），因此「10 节课券包」在本类的判据是<b>同一订单券实例数 &gt; 1</b>
 * 或<b>存量单行 total &gt; 1</b>（历史「一单一条 total=N」未核销时仍是券包，不能按单券原路退）。</p>
 *
 * <p>只读判定，不下发退款单；写库在 {@code CouponRefundServiceImpl}。</p>
 */
public final class CouponRefundRules {

    /** 可在线申请原路退（未使用单券）。 */
    public static final String MODE_REFUND = "refund";
    /** 只能转客服（已使用 / 券包 / 已退款待查询）。 */
    public static final String MODE_SERVICE = "service";
    /** 不展示退款入口（未支付、无券实例等）。 */
    public static final String MODE_NONE = "";

    private CouponRefundRules() {
    }

    /**
     * 是否已使用：任一张剩余次数小于购买次数，或券状态为已用完(2)/已作废(4)。
     *
     * @param instances 同一订单的券实例
     * @param hasConsumeFlow 该订单是否存在 type=2（核销）流水；为 true 时即使 remaining 被补发
     *                       调整过也按「已使用」处理
     */
    public static boolean used(List<CouponInstance> instances, boolean hasConsumeFlow) {
        if (hasConsumeFlow) {
            return true;
        }
        if (instances == null) {
            return false;
        }
        for (CouponInstance instance : instances) {
            int total = instance.getTotal() == null ? 0 : instance.getTotal();
            int remaining = instance.getRemaining() == null ? 0 : instance.getRemaining();
            if (remaining < total) {
                return true;
            }
            Integer status = instance.getStatus();
            if (status != null && (status == 2 || status == 4)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否券包：同一订单多张券，或存量单行 total&gt;1（历史「一单一条 total=N」）。
     * 券包一律转客服，即便尚未核销。
     */
    public static boolean pack(List<CouponInstance> instances) {
        if (instances == null) {
            return false;
        }
        if (instances.size() > 1) {
            return true;
        }
        for (CouponInstance instance : instances) {
            int total = instance.getTotal() == null ? 0 : instance.getTotal();
            if (total > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 券订单的退款模式。
     *
     * @param instanceCount 该订单的券实例数（按张落库后 = 购买张数）
     * @param used          是否已使用（见 {@link #used}）
     * @param pack          是否券包（见 {@link #pack}；存量单行 total&gt;1 也算）
     * @param payStatus     订单支付状态：0待支付/1已支付/4已取消
     * @param refundStatus  订单退款状态：0未退款/1已退款
     */
    public static String mode(int instanceCount, boolean used, boolean pack, Integer payStatus, Integer refundStatus) {
        if (payStatus == null || payStatus != 1) {
            // 未支付/已取消：订单页走「继续支付/取消」，不展示退款入口。
            return MODE_NONE;
        }
        if (refundStatus != null && refundStatus == 1) {
            // 已退款：入口改为客服（查询到账/异常处理），避免重复申请。
            return MODE_SERVICE;
        }
        if (instanceCount <= 0) {
            return MODE_NONE;
        }
        if (instanceCount > 1 || pack) {
            // 10 节课券包：一单多张，或存量单行 total>1，一律转客服（即使尚未核销）。
            return MODE_SERVICE;
        }
        return used ? MODE_SERVICE : MODE_REFUND;
    }

    /** 页面展示文案；{@link #MODE_NONE} 返回空串（不渲染入口）。 */
    public static String hint(String mode, int instanceCount, boolean used, boolean pack) {
        if (MODE_REFUND.equals(mode)) {
            return "本券未使用，可申请原路退款；审核通过后按原支付方式退回。";
        }
        if (!MODE_SERVICE.equals(mode)) {
            return "";
        }
        if (instanceCount > 1 || pack) {
            return "10 节课券包（一单多张券）不支持线上退款，请联系客服处理。";
        }
        if (used) {
            return "券已核销使用，不支持线上退款，请联系客服处理。";
        }
        return "退款已按原支付方式处理，到账时间以支付渠道为准；如有疑问请联系客服。";
    }
}
