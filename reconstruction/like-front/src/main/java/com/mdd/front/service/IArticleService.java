/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.service.IArticleService
 *  com.mdd.front.validate.article.ArticleSearchValidate
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.vo.article.ArticleCateVo
 *  com.mdd.front.vo.article.ArticleCollectVo
 *  com.mdd.front.vo.article.ArticleDetailVo
 *  com.mdd.front.vo.article.ArticleListedVo
 */
package com.mdd.front.service;

import com.mdd.common.core.PageResult;
import com.mdd.front.validate.article.ArticleSearchValidate;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.vo.article.ArticleCateVo;
import com.mdd.front.vo.article.ArticleCollectVo;
import com.mdd.front.vo.article.ArticleDetailVo;
import com.mdd.front.vo.article.ArticleListedVo;
import java.util.List;

public interface IArticleService {
    public List<ArticleCateVo> category();

    public PageResult<ArticleListedVo> list(Integer var1, PageValidate var2, ArticleSearchValidate var3);

    public ArticleDetailVo detail(Integer var1, Integer var2);

    public PageResult<ArticleCollectVo> collect(PageValidate var1, Integer var2);

    public void addCollect(Integer var1, Integer var2);

    public void cancelCollect(Integer var1, Integer var2);
}

