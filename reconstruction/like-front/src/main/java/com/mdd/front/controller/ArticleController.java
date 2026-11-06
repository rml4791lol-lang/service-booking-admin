/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.ArticleController
 *  com.mdd.front.service.IArticleService
 *  com.mdd.front.validate.article.ArticleCollectValidate
 *  com.mdd.front.validate.article.ArticleSearchValidate
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.vo.article.ArticleCateVo
 *  com.mdd.front.vo.article.ArticleCollectVo
 *  com.mdd.front.vo.article.ArticleDetailVo
 *  com.mdd.front.vo.article.ArticleListedVo
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
package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.validator.annotation.IDMust;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IArticleService;
import com.mdd.front.validate.article.ArticleCollectValidate;
import com.mdd.front.validate.article.ArticleSearchValidate;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.vo.article.ArticleCateVo;
import com.mdd.front.vo.article.ArticleCollectVo;
import com.mdd.front.vo.article.ArticleDetailVo;
import com.mdd.front.vo.article.ArticleListedVo;
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
@RequestMapping(value={"/api/article"})
@Api(tags={"\u6587\u7ae0\u7ba1\u7406"})
public class ArticleController {
    @Resource
    IArticleService iArticleService;

    @NotLogin
    @GetMapping(value={"/cate"})
    @ApiOperation(value="\u6587\u7ae0\u5206\u7c7b")
    public AjaxResult<List<ArticleCateVo>> category() {
        List list = this.iArticleService.category();
        return AjaxResult.success(list);
    }

    @NotLogin
    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u6587\u7ae0\u5217\u8868")
    public AjaxResult<PageResult<ArticleListedVo>> lists(@Validated PageValidate pageValidate, @Validated ArticleSearchValidate searchValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        PageResult list = this.iArticleService.list(userId, pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @NotLogin
    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u6587\u7ae0\u8be6\u60c5")
    public AjaxResult<ArticleDetailVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        ArticleDetailVo vo = this.iArticleService.detail(id, userId);
        return AjaxResult.success(vo);
    }

    @GetMapping(value={"/collect"})
    @ApiOperation(value="\u6536\u85cf\u5217\u8868")
    public AjaxResult<PageResult<ArticleCollectVo>> collect(@Validated PageValidate pageValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        PageResult list = this.iArticleService.collect(pageValidate, userId);
        return AjaxResult.success(list);
    }

    @PostMapping(value={"/addCollect"})
    @ApiOperation(value="\u6536\u85cf\u52a0\u5165")
    public AjaxResult<Object> addCollect(@Validated @RequestBody ArticleCollectValidate collectValidate) {
        Integer articleId = collectValidate.getId();
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iArticleService.addCollect(articleId, userId);
        return AjaxResult.success();
    }

    @PostMapping(value={"/cancelCollect"})
    @ApiOperation(value="\u6536\u85cf\u53d6\u6d88")
    public AjaxResult<Object> cancelCollect(@Validated @RequestBody ArticleCollectValidate collectValidate) {
        Integer articleId = collectValidate.getId();
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iArticleService.cancelCollect(articleId, userId);
        return AjaxResult.success();
    }
}

