/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IArticleService
 *  com.mdd.admin.validate.article.ArticleCreateValidate
 *  com.mdd.admin.validate.article.ArticleSearchValidate
 *  com.mdd.admin.validate.article.ArticleUpdateValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.article.ArticleDetailVo
 *  com.mdd.admin.vo.article.ArticleListedVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.article.ArticleCreateValidate;
import com.mdd.admin.validate.article.ArticleSearchValidate;
import com.mdd.admin.validate.article.ArticleUpdateValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.article.ArticleDetailVo;
import com.mdd.admin.vo.article.ArticleListedVo;
import com.mdd.common.core.PageResult;

public interface IArticleService {
    public PageResult<ArticleListedVo> list(PageValidate var1, ArticleSearchValidate var2);

    public ArticleDetailVo detail(Integer var1);

    public void add(ArticleCreateValidate var1);

    public void edit(ArticleUpdateValidate var1);

    public void del(Integer var1);

    public void change(Integer var1);
}

