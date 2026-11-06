package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IClassApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 开班申请接口（T29.7 / #27）。
 *
 * <p>同一街道 + 课程类型合并计数；不收款、不扣券、不生成预约。</p>
 */
@RestController
@RequestMapping(value = "/api/classApply")
@Api(tags = "开班申请（街道+课程类型合并计数）")
public class ClassApplyController {

    @Resource
    IClassApplyService iClassApplyService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "我的开班申请（含合并人数）")
    public AjaxResult<List<Map<String, Object>>> list() {
        return AjaxResult.success(iClassApplyService.applications(LikeFrontThreadLocal.getUserId()));
    }

    @PostMapping(value = "/create")
    @ApiOperation(value = "提交开班申请（同街道同课程类型合并）")
    public AjaxResult<Map<String, Object>> create(@RequestBody Map<String, Object> params) {
        return AjaxResult.success(iClassApplyService.create(LikeFrontThreadLocal.getUserId(), params));
    }
}
