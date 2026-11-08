/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.ArtCateController
 *  com.mdd.admin.service.IArtCateService
 *  com.mdd.admin.validate.article.ArtCateCreateValidate
 *  com.mdd.admin.validate.article.ArtCateSearchValidate
 *  com.mdd.admin.validate.article.ArtCateUpdateValidate
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.article.ArticleCateVo
 *  com.mdd.common.aop.NotPower
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
import com.mdd.admin.service.IArtCateService;
import com.mdd.admin.validate.article.ArtCateCreateValidate;
import com.mdd.admin.validate.article.ArtCateSearchValidate;
import com.mdd.admin.validate.article.ArtCateUpdateValidate;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.article.ArticleCateVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/article/articleCate", "/adminapi/article.articleCate"})
@Api(tags={"\u6587\u7ae0\u5206\u7c7b\u7ba1\u7406"})
public class ArtCateController {
    @Resource
    IArtCateService iArtCateService;

    @NotPower
    @GetMapping(value={"/all"})
    @ApiOperation(value="\u6240\u6709\u5206\u7c7b")
    public AjaxResult<List<ArticleCateVo>> all() {
        List list = this.iArtCateService.all();
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u5206\u7c7b\u5217\u8868")
    public AjaxResult<PageResult<ArticleCateVo>> list(@Validated PageValidate pageValidate, @Validated ArtCateSearchValidate searchValidate) {
        PageResult list = this.iArtCateService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u5206\u7c7b\u8be6\u60c5")
    public AjaxResult<ArticleCateVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        ArticleCateVo vo = this.iArtCateService.detail(id);
        return AjaxResult.success(vo);
    }

    @Log(title="\u6587\u7ae0\u5206\u7c7b\u65b0\u589e")
    @PostMapping(value={"/add"})
    @ApiOperation(value="\u5206\u7c7b\u65b0\u589e")
    public AjaxResult<Object> add(@Validated @RequestBody ArtCateCreateValidate createValidate) {
        this.iArtCateService.add(createValidate);
        return AjaxResult.success();
    }

    @Log(title="\u6587\u7ae0\u5206\u7c7b\u7f16\u8f91")
    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u5206\u7c7b\u7f16\u8f91")
    public AjaxResult<Object> edit(@Validated @RequestBody ArtCateUpdateValidate updateValidate) {
        this.iArtCateService.edit(updateValidate);
        return AjaxResult.success();
    }

    @Log(title="\u6587\u7ae0\u5206\u7c7b\u5220\u9664")
    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u5206\u7c7b\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        this.iArtCateService.del(idValidate.getId());
        return AjaxResult.success();
    }

    @Log(title="\u6587\u7ae0\u5206\u7c7b\u72b6\u6001")
    @PostMapping(value={"/updateStatus"})
    @ApiOperation(value="\u5206\u7c7b\u72b6\u6001")
    public AjaxResult<Object> change(@Validated @RequestBody IdValidate idValidate) {
        this.iArtCateService.change(idValidate.getId());
        return AjaxResult.success();
    }
}

