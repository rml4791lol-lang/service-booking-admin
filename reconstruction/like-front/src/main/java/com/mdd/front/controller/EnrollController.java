package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IEnrollService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 客服报名链接表单（T29.7 / #27）。
 *
 * <p>客服在后台生成 {@code pages/enroll/index?token=...}；家长点开链接即可填写，
 * 无需登录。同场次 + 手机号 + 学员名去重。</p>
 */
@RestController
@RequestMapping(value = "/api/enroll")
@Api(tags = "客服报名链接与表单")
public class EnrollController {

    @Resource
    IEnrollService iEnrollService;

    @NotLogin
    @GetMapping(value = "/detail")
    @ApiOperation(value = "报名页场次信息（token 优先，兼容 sessionId）")
    public AjaxResult<Map<String, Object>> detail(
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "sessionId", required = false) String sessionId) {
        return AjaxResult.success(iEnrollService.detail(token, sessionId));
    }

    @NotLogin
    @PostMapping(value = "/submit")
    @ApiOperation(value = "提交报名信息（公开，同场次+手机号+学员名去重）")
    public AjaxResult<Map<String, Object>> submit(@RequestBody Map<String, Object> params) {
        return AjaxResult.success(iEnrollService.submit(LikeFrontThreadLocal.getUserId(), params));
    }

    @GetMapping(value = "/mine")
    @ApiOperation(value = "当前账号已提交的报名（按场次）")
    public AjaxResult<List<Map<String, Object>>> mine(
            @RequestParam(value = "sessionId", required = false) String sessionId) {
        return AjaxResult.success(iEnrollService.mySubmissions(LikeFrontThreadLocal.getUserId(),
                intValue(sessionId)));
    }

    private Integer intValue(String value) {
        if (value == null) {
            return null;
        }
        try {
            String text = value.trim();
            return text.isEmpty() ? null : Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
