/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.IArtCateService
 *  com.mdd.admin.service.impl.ArtCateServiceImpl
 *  com.mdd.admin.validate.article.ArtCateCreateValidate
 *  com.mdd.admin.validate.article.ArtCateSearchValidate
 *  com.mdd.admin.validate.article.ArtCateUpdateValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.article.ArticleCateVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.article.Article
 *  com.mdd.common.entity.article.ArticleCate
 *  com.mdd.common.mapper.article.ArticleCateMapper
 *  com.mdd.common.mapper.article.ArticleMapper
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.util.Assert
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IArtCateService;
import com.mdd.admin.validate.article.ArtCateCreateValidate;
import com.mdd.admin.validate.article.ArtCateSearchValidate;
import com.mdd.admin.validate.article.ArtCateUpdateValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.article.ArticleCateVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.article.Article;
import com.mdd.common.entity.article.ArticleCate;
import com.mdd.common.mapper.article.ArticleCateMapper;
import com.mdd.common.mapper.article.ArticleMapper;
import com.mdd.common.util.TimeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ArtCateServiceImpl
implements IArtCateService {
    @Resource
    ArticleCateMapper articleCategoryMapper;
    @Resource
    ArticleMapper articleMapper;

    public List<ArticleCateVo> all() {
        QueryWrapper<ArticleCate> queryWrapper = new QueryWrapper<ArticleCate>();
        queryWrapper.select(new String[]{"id", "name", "sort", "is_show", "create_time", "update_time"}).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id"));
        List<ArticleCate> lists = this.articleCategoryMapper.selectList((Wrapper)queryWrapper);
        ArrayList<ArticleCateVo> vos = new ArrayList<ArticleCateVo>();
        for (ArticleCate category : lists) {
            ArticleCateVo vo = new ArticleCateVo();
            BeanUtils.copyProperties((Object)category, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((String)vo.getUpdateTime()));
            vos.add(vo);
        }
        return vos;
    }

    public PageResult<ArticleCateVo> list(PageValidate pageValidate, ArtCateSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        QueryWrapper<ArticleCate> queryWrapper = new QueryWrapper<ArticleCate>();
        queryWrapper.select(new String[]{"id", "name", "sort", "is_show", "create_time", "update_time"}).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id"));
        this.articleCategoryMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"like:name:str", "=:isShow@is_show:int"});
        IPage<ArticleCate> iPage = this.articleCategoryMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), (Wrapper)queryWrapper);
        ArrayList<ArticleCateVo> list = new ArrayList<ArticleCateVo>();
        for (ArticleCate category : iPage.getRecords()) {
            ArticleCateVo vo = new ArticleCateVo();
            BeanUtils.copyProperties((Object)category, (Object)vo);
            Long number = this.articleMapper.selectCount(new QueryWrapper<Article>().eq("cid", (Object)category.getId()).isNull("delete_time"));
            vo.setArticleCount(number);
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((String)vo.getUpdateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public ArticleCateVo detail(Integer id) {
        ArticleCate model = (ArticleCate)this.articleCategoryMapper.selectOne(new QueryWrapper<ArticleCate>().select(ArticleCate.class, info -> !info.getColumn().equals("delete_time")).eq("id", (Object)id).isNull("delete_time"));
        Assert.notNull((Object)model, (String)"\u5206\u7c7b\u4e0d\u5b58\u5728");
        ArticleCateVo vo = new ArticleCateVo();
        BeanUtils.copyProperties((Object)model, (Object)vo);
        vo.setCreateTime(TimeUtils.timestampToDate((Long)model.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)model.getUpdateTime()));
        return vo;
    }

    public void add(ArtCateCreateValidate createValidate) {
        ArticleCate model = new ArticleCate();
        model.setName(createValidate.getName());
        model.setSort(createValidate.getSort());
        model.setIsShow(createValidate.getIsShow());
        model.setCreateTime(TimeUtils.timestamp());
        model.setUpdateTime(TimeUtils.timestamp());
        this.articleCategoryMapper.insert(model);
    }

    public void edit(ArtCateUpdateValidate updateValidate) {
        ArticleCate model = (ArticleCate)this.articleCategoryMapper.selectOne(new QueryWrapper<ArticleCate>().select(ArticleCate.class, info -> !info.getColumn().equals("delete_time")).eq("id", (Object)updateValidate.getId()).isNull("delete_time"));
        Assert.notNull((Object)model, (String)"\u5206\u7c7b\u4e0d\u5b58\u5728");
        model.setName(updateValidate.getName());
        model.setSort(updateValidate.getSort());
        model.setIsShow(updateValidate.getIsShow());
        model.setUpdateTime(TimeUtils.timestamp());
        this.articleCategoryMapper.updateById(model);
    }

    public void del(Integer id) {
        ArticleCate model = (ArticleCate)this.articleCategoryMapper.selectOne(new QueryWrapper<ArticleCate>().select(new String[]{"id,is_show"}).eq("id", (Object)id).isNull("delete_time"));
        Assert.notNull((Object)model, (String)"\u5206\u7c7b\u4e0d\u5b58\u5728");
        Article article = (Article)this.articleMapper.selectOne(new QueryWrapper<Article>().eq("cid", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.isNull(article, (String)"\u5f53\u524d\u5206\u7c7b\u5df2\u88ab\u6587\u7ae0\u4f7f\u7528,\u8bf7\u5148\u79fb\u9664!");
        model.setDeleteTime(TimeUtils.timestamp());
        this.articleCategoryMapper.updateById(model);
    }

    public void change(Integer id) {
        ArticleCate model = (ArticleCate)this.articleCategoryMapper.selectOne(new QueryWrapper<ArticleCate>().select(new String[]{"id,is_show"}).eq("id", (Object)id).isNull("delete_time"));
        Assert.notNull((Object)model, (String)"\u5206\u7c7b\u4e0d\u5b58\u5728");
        model.setIsShow(Integer.valueOf(model.getIsShow() == 0 ? 1 : 0));
        model.setUpdateTime(TimeUtils.timestamp());
        this.articleCategoryMapper.updateById(model);
    }
}

