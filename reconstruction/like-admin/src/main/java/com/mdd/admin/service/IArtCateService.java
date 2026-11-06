/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IArtCateService
 *  com.mdd.admin.validate.article.ArtCateCreateValidate
 *  com.mdd.admin.validate.article.ArtCateSearchValidate
 *  com.mdd.admin.validate.article.ArtCateUpdateValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.article.ArticleCateVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.article.ArtCateCreateValidate;
import com.mdd.admin.validate.article.ArtCateSearchValidate;
import com.mdd.admin.validate.article.ArtCateUpdateValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.article.ArticleCateVo;
import com.mdd.common.core.PageResult;
import java.util.List;

public interface IArtCateService {
    public List<ArticleCateVo> all();

    public PageResult<ArticleCateVo> list(PageValidate var1, ArtCateSearchValidate var2);

    public ArticleCateVo detail(Integer var1);

    public void add(ArtCateCreateValidate var1);

    public void edit(ArtCateUpdateValidate var1);

    public void del(Integer var1);

    public void change(Integer var1);
}

