package com.mdd.front.controller;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.exception.OperateException;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.ICouponPurchaseService;
import com.mdd.front.service.ICouponRefundService;
import com.mdd.front.service.ICouponService;
import com.mdd.front.service.IPayMockGatewayService;
import com.mdd.common.util.ParamUtils;
import com.mdd.front.vo.CouponVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 小程序券接口（T29.4 次数券）。
 *
 * <p>老抵扣券接口保留：{@code /available} 与 {@code /legacy/myCoupons}；新页面不再使用。</p>
 *
 * <p>支付链路：{@code /buy} 只创建待支付订单；真实微信回调走
 * {@code POST /api/pay/notifyMnp}（attach=coupon），本地联调用
 * {@code /mockNotify}（受 like.pay.mock-callback-enabled 开关控制，默认关闭）。</p>
 */
@RestController
@RequestMapping(value = "/api/coupon")
@Api(tags = "次数券（商品/我的券/流水/购买/适用/最优）")
public class CouponController {

    @Resource
    ICouponService iCouponService;
    @Resource
    ICouponPurchaseService iCouponPurchaseService;
    @Resource
    ICouponRefundService iCouponRefundService;
    /** mock 支付网关只在测试 profile + 开关打开时装配；生产/体验为 null（见 PayMockController）。 */
    @Autowired(required = false)
    IPayMockGatewayService iPayMockGatewayService;

    // ---- 券商品 ------------------------------------------------------------

    @NotLogin
    @GetMapping(value = "/products")
    @ApiOperation(value = "券商品列表（8 档真实价格，公开只读）")
    public AjaxResult<List<Map<String, Object>>> products() {
        return AjaxResult.success(iCouponService.products());
    }

    // ---- 我的券 / 流水 -----------------------------------------------------

    @GetMapping(value = "/myCoupons")
    @ApiOperation(value = "我的券（次数券，按学员维度）")
    public AjaxResult<List<Map<String, Object>>> myCoupons(
            @RequestParam(value = "studentId", required = false) Integer studentId) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCouponService.myCoupons(userId, studentId));
    }

    @GetMapping(value = "/flows")
    @ApiOperation(value = "券流水（不可删除，按 id 升序）")
    public AjaxResult<List<Map<String, Object>>> flows(
            @RequestParam(value = "studentId", required = false) Integer studentId,
            @RequestParam(value = "couponId", required = false) Integer couponId) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCouponService.flows(userId, studentId, couponId));
    }

    // ---- 适用校验 / 最优券 -------------------------------------------------

    @GetMapping(value = "/applies")
    @ApiOperation(value = "券适用统一校验（课程/场次 + 学员 + 有效期/范围）")
    public AjaxResult<Map<String, Object>> applies(
            @RequestParam(value = "couponId", required = false) String couponId,
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "sessionId", required = false) String sessionId,
            @RequestParam(value = "studentId", required = false) String studentId) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCouponService.applies(userId, couponId, courseId, sessionId, studentId));
    }

    @GetMapping(value = "/best")
    @ApiOperation(value = "自动匹配最优券（最早到期的可用券优先）")
    public AjaxResult<Map<String, Object>> best(
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "sessionId", required = false) String sessionId,
            @RequestParam(value = "studentId", required = false) String studentId) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCouponService.bestCoupon(userId, courseId, sessionId, studentId));
    }

    // ---- 购买 --------------------------------------------------------------

    @PostMapping(value = "/buy")
    @ApiOperation(value = "券商品下单（免费券直接发放；付费券待支付回调发券）")
    public AjaxResult<Map<String, Object>> buy(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        String productId = stringValue(params.get("productId"));
        String studentId = stringValue(params.get("studentId"));
        return AjaxResult.success(iCouponPurchaseService.placeOrder(userId, terminal, productId, studentId));
    }

    // ---- 退款（#110 T12） --------------------------------------------------

    /**
     * 券退款申请：未使用的单券写退款单等后台审核；已使用 / 10 节课券包返回客服路径且不写单。
     *
     * <p>券订单没有 courseClassId，因此单独开接口，不复用 {@code /api/course/applyRefund}。
     * 开关 {@code like.coupon.refund-enabled}（默认 true）关闭时直接拒绝。</p>
     */
    @PostMapping(value = "/applyRefund")
    @ApiOperation(value = "券退款申请（未用单券原路退；已用/券包转客服）")
    public AjaxResult<Map<String, Object>> applyRefund(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCouponRefundService.applyRefund(userId, params));
    }

    // ---- mock 支付回调（本地联调；默认关闭） --------------------------------

    @PostMapping(value = "/mockNotify")
    @ApiOperation(value = "模拟支付回调（兼容旧入口；开关：like.pay.mock-callback-enabled）")
    public AjaxResult<Map<String, Object>> mockNotify(@RequestBody Map<String, Object> params) throws WxPayException {
        if (iPayMockGatewayService == null || !iPayMockGatewayService.isMockCallbackEnabled()) {
            throw new OperateException("模拟支付回调未开启");
        }
        Integer userId = LikeFrontThreadLocal.getUserId();
        String orderId = stringValue(params.get("orderId"));
        String action = params.get("action") == null ? "success" : stringValue(params.get("action"));
        RechargeOrder order = iCouponPurchaseService.requireMockableOrder(userId, orderId);
        Map<String, Object> result = iPayMockGatewayService.notify(
                userId, order.getId(), action, true);
        return AjaxResult.success(result);
    }

    // ---- 老抵扣券接口（保留给老调用方，数据模型未动） ----------------------

    @GetMapping(value = "/available")
    @ApiOperation(value = "老抵扣券-支付时可用券（保留）")
    public AjaxResult<List<CouponVo>> available(@RequestParam(value = "orderAmount") String orderAmount) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCouponService.legacyAvailableCoupons(userId, ParamUtils.requiredDecimal(orderAmount, "\u8ba2\u5355\u91d1\u989d")));
    }

    @GetMapping(value = "/legacy/myCoupons")
    @ApiOperation(value = "老抵扣券-我的抵扣券（保留）")
    public AjaxResult<List<CouponVo>> legacyMyCoupons(
            @RequestParam(value = "status", required = false) Integer status) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCouponService.legacyMyCoupons(userId, status));
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
