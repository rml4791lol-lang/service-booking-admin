/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.github.yulichang.interfaces.MPJBaseJoin
 *  com.github.yulichang.query.MPJQueryWrapper
 *  com.mdd.admin.service.IArticleService
 *  com.mdd.admin.service.impl.ArticleServiceImpl
 *  com.mdd.admin.validate.article.ArticleCreateValidate
 *  com.mdd.admin.validate.article.ArticleSearchValidate
 *  com.mdd.admin.validate.article.ArticleUpdateValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.article.ArticleDetailVo
 *  com.mdd.admin.vo.article.ArticleListedVo
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.article.Article
 *  com.mdd.common.mapper.article.ArticleCateMapper
 *  com.mdd.common.mapper.article.ArticleMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
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
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.MPJQueryWrapper;
import com.mdd.admin.service.IArticleService;
import com.mdd.admin.validate.article.ArticleCreateValidate;
import com.mdd.admin.validate.article.ArticleSearchValidate;
import com.mdd.admin.validate.article.ArticleUpdateValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.article.ArticleDetailVo;
import com.mdd.admin.vo.article.ArticleListedVo;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.article.Article;
import com.mdd.common.mapper.article.ArticleCateMapper;
import com.mdd.common.mapper.article.ArticleMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import java.util.Arrays;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.mdd.common.entity.article.ArticleCate;

@Service
public class ArticleServiceImpl
implements IArticleService {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    ArticleCateMapper articleCategoryMapper;

    public PageResult<ArticleListedVo> list(PageValidate pageValidate, ArticleSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        MPJQueryWrapper<Article> mpjQueryWrapper = ((new MPJQueryWrapper<Article>().selectAll(Article.class).select(new String[]{"ac.name as cateName"}).innerJoin("?_article_cate ac ON ac.id=t.cid".replace("?_", GlobalConfig.tablePrefix))).isNull("t.delete_time")).orderByDesc(Arrays.asList("t.sort", "t.id"));
        this.articleMapper.setSearch(mpjQueryWrapper, (Object)searchValidate, new String[]{"like:title@t.title:str", "=:cid@t.cid:int", "=:is_show@t.is_show:int", "datetime:startTime-endTime@t.create_time:str"});
        IPage<ArticleListedVo> iPage = this.articleMapper.selectJoinPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), ArticleListedVo.class, (MPJBaseJoin)mpjQueryWrapper);
        for (ArticleListedVo vo : iPage.getRecords()) {
            vo.setImage(UrlUtils.toAdminAbsoluteUrl((String)vo.getImage()));
            Integer clickActual = vo.getClickActual() != null ? vo.getClickActual() : 0;
            Integer clickVirtual = vo.getClickVirtual() != null ? vo.getClickVirtual() : 0;
            Integer click = clickActual + clickVirtual;
            vo.setClick(click);
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((String)vo.getUpdateTime()));
        }
        return PageResult.iPageHandle((IPage)iPage);
    }

    public ArticleDetailVo detail(Integer id) {
        Article model = (Article)this.articleMapper.selectOne(new QueryWrapper<Article>().select(Article.class, info -> !info.getColumn().equals("delete_time")).eq("id", (Object)id).isNull("delete_time"));
        Assert.notNull((Object)model, (String)"\u6587\u7ae0\u4e0d\u5b58\u5728");
        ArticleDetailVo vo = new ArticleDetailVo();
        BeanUtils.copyProperties((Object)model, (Object)vo);
        vo.setContent(StringUtils.isNull(model.getContent()) ? "" : model.getContent());
        vo.setImage(UrlUtils.toAdminAbsoluteUrl((String)model.getImage()));
        vo.setCreateTime(TimeUtils.timestampToDate((Long)model.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)model.getUpdateTime()));
        return vo;
    }

    public void add(ArticleCreateValidate createValidate) {
        Article model = new Article();
        model.setCid(createValidate.getCid());
        model.setTitle(createValidate.getTitle());
        model.setImage(UrlUtils.toRelativeUrl((String)createValidate.getImage()));
        model.setContent(createValidate.getContent());
        model.setSort(createValidate.getSort());
        model.setIsShow(createValidate.getIsShow());
        model.setAuthor(createValidate.getAuthor());
        model.setAbstractField(createValidate.getAbstractFied());
        model.setDesc(createValidate.getDesc());
        model.setCreateTime(TimeUtils.timestamp());
        model.setUpdateTime(TimeUtils.timestamp());
        model.setClickVirtual(createValidate.getClick_virtual());
        this.articleMapper.insert(model);
    }

    public void edit(ArticleUpdateValidate updateValidate) {
        Article model = (Article)this.articleMapper.selectOne(new QueryWrapper<Article>().eq("id", (Object)updateValidate.getId()).isNull("delete_time"));
        Assert.notNull((Object)model, (String)"\u6587\u7ae0\u4e0d\u5b58\u5728!");
        Assert.notNull((Object)this.articleCategoryMapper.selectOne(new QueryWrapper<ArticleCate>().eq("id", (Object)updateValidate.getCid()).isNull("delete_time")), (String)"\u5206\u7c7b\u4e0d\u5b58\u5728");
        model.setCid(updateValidate.getCid());
        model.setTitle(updateValidate.getTitle());
        model.setImage(UrlUtils.toRelativeUrl((String)updateValidate.getImage()));
        model.setContent(updateValidate.getContent());
        model.setIsShow(updateValidate.getIsShow());
        model.setAuthor(updateValidate.getAuthor());
        model.setSort(updateValidate.getSort());
        model.setAbstractField(updateValidate.getAbstractFied());
        model.setDesc(updateValidate.getDesc());
        model.setUpdateTime(TimeUtils.timestamp());
        model.setClickVirtual(updateValidate.getClick_virtual());
        this.articleMapper.updateById(model);
    }

    public void del(Integer id) {
        Article article = (Article)this.articleMapper.selectOne(new QueryWrapper<Article>().select(new String[]{"id, is_show"}).eq("id", (Object)id).isNull("delete_time"));
        Assert.notNull((Object)article, (String)"\u6587\u7ae0\u4e0d\u5b58\u5728!");
        article.setDeleteTime(TimeUtils.timestamp());
        this.articleMapper.updateById(article);
    }

    public void change(Integer id) {
        Article article = (Article)this.articleMapper.selectOne(new QueryWrapper<Article>().select(new String[]{"id, is_show"}).eq("id", (Object)id).isNull("delete_time"));
        Assert.notNull((Object)article, (String)"\u6587\u7ae0\u4e0d\u5b58\u5728!");
        article.setIsShow(Integer.valueOf(article.getIsShow() == 0 ? 1 : 0));
        article.setUpdateTime(TimeUtils.timestamp());
        this.articleMapper.updateById(article);
    }
}

