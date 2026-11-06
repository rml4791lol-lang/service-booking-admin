/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.PcController
 *  com.mdd.front.service.IPcService
 *  com.mdd.front.vo.article.PcArticleCenterVo
 *  com.mdd.front.vo.article.PcArticleDetailVo
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.validator.annotation.IDMust;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IPcService;
import com.mdd.front.vo.article.PcArticleCenterVo;
import com.mdd.front.vo.article.PcArticleDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/pc"})
@Api(tags={"\u7535\u8111\u7ba1\u7406"})
public class PcController {
    @Resource
    IPcService iPcService;

    @NotLogin
    @GetMapping(value={"/index"})
    @ApiOperation(value="\u4e3b\u9875\u6570\u636e")
    public AjaxResult<Map<String, Object>> index() {
        Map index = this.iPcService.index();
        return AjaxResult.success(index);
    }

    @NotLogin
    @GetMapping(value={"/config"})
    @ApiOperation(value="\u516c\u5171\u914d\u7f6e")
    public AjaxResult<Map<String, Object>> getConfig() {
        Map config = this.iPcService.getConfig();
        return AjaxResult.success(config);
    }

    @NotLogin
    @GetMapping(value={"/infoCenter"})
    @ApiOperation(value="\u8d44\u8baf\u4e2d\u5fc3")
    public AjaxResult<List<PcArticleCenterVo>> infoCenter() {
        List list = this.iPcService.infoCenter();
        return AjaxResult.success(list);
    }

    @NotLogin
    @GetMapping(value={"/articleDetail"})
    @ApiOperation(value="\u6587\u7ae0\u8be6\u60c5")
    public AjaxResult<PcArticleDetailVo> articleDetail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        PcArticleDetailVo vo = this.iPcService.articleDetail(id, userId);
        return AjaxResult.success(vo);
    }
}

