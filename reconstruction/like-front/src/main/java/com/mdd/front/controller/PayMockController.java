package com.mdd.front.controller;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.exception.OperateException;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IPayMockGatewayService;
import com.mdd.common.util.ParamUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 本地/测试支付 mock 网关（T30.11、#124）。
 *
 * <p>路由刻意与真实回调 {@code /api/pay/notifyMnp} 分开，且受
 * {@code like.pay.mock-callback-enabled} 开关控制（默认 false，生产必须关闭）。
 * 该接口要求登录，只允许操作当前账号自己的订单。</p>
 *
 * <p>#124 起 mock 网关 bean 只在测试 profile + 开关打开时装配，因此这里改为可选注入：
 * 生产/体验上下文里没有该 bean，路由仍然存在但统一返回「模拟支付网关未开启」，
 * 不会因为缺 bean 导致启动失败，也不会暴露 mock 能力。</p>
 *
 * <p>action：{@code success}（默认）/ {@code fail} / {@code cancel} / {@code duplicate}。</p>
 */
@RestController
@RequestMapping(value = "/api/pay/mock")
@Api(tags = "本地支付 mock 网关（默认关闭）")
public class PayMockController {

    /** 生产/体验为 null（mock bean 不装配）。 */
    @Autowired(required = false)
    IPayMockGatewayService iPayMockGatewayService;

    @PostMapping(value = "/notify")
    @ApiOperation(value = "模拟支付结果通知（开关：like.pay.mock-callback-enabled）")
    public AjaxResult<Map<String, Object>> notify(@RequestBody Map<String, Object> params) throws WxPayException {
        IPayMockGatewayService gateway = requireMockGateway();
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer orderId = ParamUtils.requiredInt(params.get("orderId"), "\u8ba2\u5355ID");
        String action = params.get("action") == null ? "success" : String.valueOf(params.get("action"));
        Map<String, Object> result = gateway.notify(userId, orderId, action, false);
        return AjaxResult.success(result);
    }

    /** mock bean 未装配（生产/体验）或开关关闭时，与旧行为一致地返回业务错误。 */
    private IPayMockGatewayService requireMockGateway() {
        if (iPayMockGatewayService == null || !iPayMockGatewayService.isMockCallbackEnabled()) {
            throw new OperateException("模拟支付网关未开启");
        }
        return iPayMockGatewayService;
    }
}
