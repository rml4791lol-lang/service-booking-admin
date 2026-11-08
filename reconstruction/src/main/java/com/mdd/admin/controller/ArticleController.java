/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.ArticleController
 *  com.mdd.admin.service.IArticleService
 *  com.mdd.admin.validate.article.ArticleCreateValidate
 *  com.mdd.admin.validate.article.ArticleSearchValidate
 *  com.mdd.admin.validate.article.ArticleUpdateValidate
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.article.ArticleDetailVo
 *  com.mdd.admin.vo.article.ArticleListedVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.validator.annotation.IDMust
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IArticleService;
import com.mdd.admin.validate.article.ArticleCreateValidate;
import com.mdd.admin.validate.article.ArticleSearchValidate;
import com.mdd.admin.validate.article.ArticleUpdateValidate;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.article.ArticleDetailVo;
import com.mdd.admin.vo.article.ArticleListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"adminapi/article/article", "adminapi/article.article"})
@Api(tags={"\u6587\u7ae0\u6570\u636e\u7ba1\u7406"})
public class ArticleController {
    @Resource
    IArticleService iArticleService;

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u6587\u7ae0\u5217\u8868")
    public AjaxResult<PageResult<ArticleListedVo>> list(@Validated PageValidate pageValidate, @Validated ArticleSearchValidate searchValidate) {
        PageResult vos = this.iArticleService.list(pageValidate, searchValidate);
        return AjaxResult.success(vos);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u6587\u7ae0\u8be6\u60c5")
    public AjaxResult<ArticleDetailVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        ArticleDetailVo vo = this.iArticleService.detail(id);
        return AjaxResult.success(vo);
    }

    @Log(title="\u6587\u7ae0\u65b0\u589e")
    @PostMapping(value={"/add"})
    @ApiOperation(value="\u6587\u7ae0\u65b0\u589e")
    public AjaxResult<Object> add(@Validated @RequestBody ArticleCreateValidate createValidate) {
        this.iArticleService.add(createValidate);
        return AjaxResult.success();
    }

    @Log(title="\u6587\u7ae0\u7f16\u8f91")
    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u6587\u7ae0\u7f16\u8f91")
    public AjaxResult<Object> edit(@Validated @RequestBody ArticleUpdateValidate updateValidate) {
        this.iArticleService.edit(updateValidate);
        return AjaxResult.success();
    }

    @Log(title="\u6587\u7ae0\u5220\u9664")
    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u6587\u7ae0\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        this.iArticleService.del(idValidate.getId());
        return AjaxResult.success();
    }

    @Log(title="\u6587\u7ae0\u72b6\u6001")
    @PostMapping(value={"/updateStatus"})
    @ApiOperation(value="\u6587\u7ae0\u72b6\u6001")
    public AjaxResult<Object> change(@Validated @RequestBody IdValidate idValidate) {
        this.iArticleService.change(idValidate.getId());
        return AjaxResult.success();
    }
}

