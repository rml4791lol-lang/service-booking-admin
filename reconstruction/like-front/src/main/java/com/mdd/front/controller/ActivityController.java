package com.mdd.front.controller;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.exception.OperateException;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IActivityService;
import com.mdd.front.service.IPayService;
import com.mdd.common.util.ParamUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动报名接口（T29.7 / #27）。
 *
 * <p>列表/详情公开只读；报名需登录。金额按总人头（学员 + 陪同）计算，下单即占名额；
 * 支付成功以回调为准（真实微信 attach=activity / 本地 mockNotify）。</p>
 */
@RestController
@RequestMapping(value = "/api/activity")
@Api(tags = "活动报名（列表/详情/报名/回调）")
public class ActivityController {

    @Resource
    IActivityService iActivityService;
    @Resource
    IPayService iPayService;

    @Value("${like.pay.mock-callback-enabled:false}")
    private boolean mockCallbackEnabled;

    @NotLogin
    @GetMapping(value = "/list")
    @ApiOperation(value = "活动列表（公开）")
    public AjaxResult<List<Map<String, Object>>> list() {
        return AjaxResult.success(iActivityService.list(LikeFrontThreadLocal.getUserId()));
    }

    @NotLogin
    @GetMapping(value = "/detail")
    @ApiOperation(value = "活动详情（公开）")
    public AjaxResult<Map<String, Object>> detail(
            @RequestParam(value = "id", required = false) String id) {
        return AjaxResult.success(iActivityService.detail(LikeFrontThreadLocal.getUserId(), intValue(id)));
    }

    @PostMapping(value = "/signup")
    @ApiOperation(value = "活动报名（按学员人数计费、预占名额）")
    public AjaxResult<Map<String, Object>> signup(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        return AjaxResult.success(iActivityService.signup(
                userId, terminal, intValue(params.get("activityId")), intList(params.get("studentIds")),
                intValue(params.get("companionCount"))));
    }

    @PostMapping(value = "/cancel")
    @ApiOperation(value = "取消活动报名（免费直接取消，付费生成退款申请）")
    public AjaxResult<Map<String, Object>> cancel(@RequestBody Map<String, Object> params) {
        return AjaxResult.success(iActivityService.cancel(
                LikeFrontThreadLocal.getUserId(), intValue(params.get("activityId"))));
    }

    @PostMapping(value = "/mockNotify")
    @ApiOperation(value = "模拟支付成功回调（开关：like.pay.mock-callback-enabled）")
    public AjaxResult<Map<String, Object>> mockNotify(@RequestBody Map<String, Object> params) throws WxPayException {
        if (!mockCallbackEnabled) {
            throw new OperateException("模拟支付回调未开启");
        }
        Integer userId = LikeFrontThreadLocal.getUserId();
        RechargeOrder order = iActivityService.requireMockableOrder(userId, ParamUtils.requiredInt(params.get("orderId"), "\u8ba2\u5355ID"));
        iPayService.handlePaidNotify("activity", order.getSn(), "MOCK" + System.currentTimeMillis());
        RechargeOrder fresh = iActivityService.requireMockableOrder(userId, ParamUtils.requiredInt(params.get("orderId"), "\u8ba2\u5355ID"));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mocked", true);
        result.put("orderId", fresh.getId());
        result.put("orderSn", fresh.getSn());
        result.put("payStatus", fresh.getPayStatus());
        result.put("paid", fresh.getPayStatus() != null && fresh.getPayStatus() == 1);
        return AjaxResult.success(result);
    }

    private Integer intValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            String text = String.valueOf(value).trim();
            return text.isEmpty() ? null : Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private List<Integer> intList(Object value) {
        List<Integer> list = new ArrayList<>();
        if (!(value instanceof List)) {
            return list;
        }
        for (Object item : (List<?>) value) {
            Integer parsed = intValue(item);
            if (parsed != null) {
                list.add(parsed);
            }
        }
        return list;
    }
}
