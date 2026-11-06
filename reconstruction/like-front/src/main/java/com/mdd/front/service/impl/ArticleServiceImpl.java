/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.github.yulichang.interfaces.MPJBaseJoin
 *  com.github.yulichang.query.MPJQueryWrapper
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.article.Article
 *  com.mdd.common.entity.article.ArticleCate
 *  com.mdd.common.entity.article.ArticleCollect
 *  com.mdd.common.enums.YesNoEnum
 *  com.mdd.common.mapper.article.ArticleCateMapper
 *  com.mdd.common.mapper.article.ArticleCollectMapper
 *  com.mdd.common.mapper.article.ArticleMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.service.IArticleService
 *  com.mdd.front.service.impl.ArticleServiceImpl
 *  com.mdd.front.validate.article.ArticleSearchValidate
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.vo.article.ArticleCateVo
 *  com.mdd.front.vo.article.ArticleCollectVo
 *  com.mdd.front.vo.article.ArticleDetailVo
 *  com.mdd.front.vo.article.ArticleListedVo
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.MPJQueryWrapper;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.article.Article;
import com.mdd.common.entity.article.ArticleCate;
import com.mdd.common.entity.article.ArticleCollect;
import com.mdd.common.enums.YesNoEnum;
import com.mdd.common.mapper.article.ArticleCateMapper;
import com.mdd.common.mapper.article.ArticleCollectMapper;
import com.mdd.common.mapper.article.ArticleMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IArticleService;
import com.mdd.front.validate.article.ArticleSearchValidate;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.vo.article.ArticleCateVo;
import com.mdd.front.vo.article.ArticleCollectVo;
import com.mdd.front.vo.article.ArticleDetailVo;
import com.mdd.front.vo.article.ArticleListedVo;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl
implements IArticleService {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    ArticleCateMapper articleCategoryMapper;
    @Resource
    ArticleCollectMapper articleCollectMapper;

    public List<ArticleCateVo> category() {
        List<ArticleCate> articleCateVos = this.articleCategoryMapper.selectList(new QueryWrapper<ArticleCate>().select(new String[]{"id,name"}).eq("is_show", (Object)1).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id")));
        LinkedList<ArticleCateVo> list = new LinkedList<ArticleCateVo>();
        for (ArticleCate category : articleCateVos) {
            ArticleCateVo vo = new ArticleCateVo();
            BeanUtils.copyProperties((Object)category, (Object)vo);
            list.add(vo);
        }
        return list;
    }

    public PageResult<ArticleListedVo> list(Integer userId, PageValidate pageValidate, ArticleSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>();
        queryWrapper.select(new String[]{"id,cid,title,`desc`,image,click_virtual,click_actual,create_time"});
        queryWrapper.isNull("delete_time");
        queryWrapper.eq("is_show", (Object)1);
        this.articleMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"like:keyword@title:str"});
        if (StringUtils.isNotNull(searchValidate.getCid()) && searchValidate.getCid() > 0) {
            queryWrapper.eq("cid", (Object)searchValidate.getCid());
        }
        if (StringUtils.isNotNull(searchValidate.getSort())) {
            switch (searchValidate.getSort()) {
                case "hot": {
                    queryWrapper.orderByDesc("click_actual + click_virtual").orderByDesc("id");
                    break;
                }
                case "new": {
                    queryWrapper.orderByDesc("id");
                    break;
                }
                default: {
                    queryWrapper.orderByDesc(Arrays.asList("sort", "id"));
                }
            }
        }
        IPage<Article> iPage = this.articleMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), (Wrapper)queryWrapper);
        LinkedList<Integer> ids = new LinkedList<Integer>();
        LinkedList<ArticleListedVo> list = new LinkedList<ArticleListedVo>();
        for (Article article : iPage.getRecords()) {
            ArticleListedVo vo = new ArticleListedVo();
            BeanUtils.copyProperties((Object)article, (Object)vo);
            vo.setCollect(Boolean.valueOf(false));
            vo.setImage(UrlUtils.toAbsoluteUrl((String)article.getImage()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)article.getCreateTime()));
            vo.setClick(Integer.valueOf(article.getClickActual() + article.getClickVirtual()));
            list.add(vo);
            ids.add(article.getId());
        }
        if (userId != null && userId > 0 && ids.size() > 0) {
            List<ArticleCollect> articleCollects = this.articleCollectMapper.selectList(new QueryWrapper<ArticleCollect>().eq("user_id", (Object)userId).isNull("delete_time").eq("status", (Object)YesNoEnum.YES.getCode()).in("article_id", ids));
            LinkedList<Integer> collects = new LinkedList<Integer>();
            for (ArticleCollect c : articleCollects) {
                collects.add(c.getArticleId());
            }
            for (ArticleListedVo vo : list) {
                vo.setCollect(Boolean.valueOf(collects.contains(vo.getId())));
            }
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public ArticleDetailVo detail(Integer id, Integer userId) {
        Article article = (Article)this.articleMapper.selectOne(new QueryWrapper<Article>().eq("id", (Object)id).eq("is_show", (Object)1).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)article, (String)"\u6570\u636e\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        ArticleCollect articleCollect = (ArticleCollect)this.articleCollectMapper.selectOne(new QueryWrapper<ArticleCollect>().eq("user_id", (Object)userId).eq("article_id", (Object)article.getId()).isNull("delete_time").eq("status", (Object)YesNoEnum.YES.getCode()).last("limit 1"));
        ArticleDetailVo vo = new ArticleDetailVo();
        BeanUtils.copyProperties((Object)article, (Object)vo);
        vo.setCollect(Boolean.valueOf(articleCollect != null));
        vo.setImage(UrlUtils.toAbsoluteUrl((String)article.getImage()));
        vo.setCreateTime(TimeUtils.timestampToDate((Long)article.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)article.getUpdateTime()));
        vo.setClick(Integer.valueOf(article.getClickActual() + article.getClickVirtual()));
        article.setClickActual(Integer.valueOf(article.getClickActual() + 1));
        this.articleMapper.updateById(article);
        return vo;
    }

    public PageResult<ArticleCollectVo> collect(PageValidate pageValidate, Integer userId) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        MPJQueryWrapper<ArticleCollectVo> mpjQueryWrapper = new MPJQueryWrapper<>();
        ((((((mpjQueryWrapper.setAlias("a").select(new String[]{"c.id,c.article_id,a.title,a.image,a.desc,a.is_show,a.click_virtual, a.click_actual,a.create_time, c.create_time as collect_time"}).eq("c.user_id", (Object)userId)).isNull("c.delete_time")).isNull("a.delete_time")).eq("c.status", (Object)YesNoEnum.YES.getCode())).eq("a.is_show", (Object)YesNoEnum.YES.getCode())).orderByDesc("c.id")).innerJoin("?_article_collect c ON c.article_id=a.id".replace("?_", GlobalConfig.tablePrefix));
        IPage<ArticleCollectVo> iPage = this.articleMapper.selectJoinPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), ArticleCollectVo.class, (MPJBaseJoin)mpjQueryWrapper);
        for (ArticleCollectVo vo : iPage.getRecords()) {
            vo.setImage(UrlUtils.toAbsoluteUrl((String)vo.getImage()));
            vo.setCreateTime((Object)TimeUtils.timestampToDate((Long)Long.valueOf(vo.getCreateTime().toString())));
            vo.setCollectTime((Object)TimeUtils.timestampToDate((Long)Long.valueOf(vo.getCollectTime().toString())));
        }
        return PageResult.iPageHandle((IPage)iPage);
    }

    public void addCollect(Integer articleId, Integer userId) {
        ArticleCollect articleCollect = (ArticleCollect)this.articleCollectMapper.selectOne(new QueryWrapper<ArticleCollect>().eq("article_id", (Object)articleId).eq("user_id", (Object)userId).isNull("delete_time").last("limit 1"));
        if (StringUtils.isNotNull(articleCollect)) {
            articleCollect.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            articleCollect.setStatus(Integer.valueOf(YesNoEnum.YES.getCode()));
            this.articleCollectMapper.updateById(articleCollect);
        } else {
            ArticleCollect model = new ArticleCollect();
            model.setArticleId(articleId);
            model.setUserId(userId);
            model.setStatus(Integer.valueOf(YesNoEnum.YES.getCode()));
            model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.articleCollectMapper.insert(model);
        }
    }

    public void cancelCollect(Integer articleId, Integer userId) {
        ArticleCollect articleCollect = (ArticleCollect)this.articleCollectMapper.selectOne(new QueryWrapper<ArticleCollect>().eq("article_id", (Object)articleId).eq("user_id", (Object)userId).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)articleCollect, (String)"\u6536\u85cf\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        articleCollect.setStatus(Integer.valueOf(YesNoEnum.NO.getCode()));
        articleCollect.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.articleCollectMapper.updateById(articleCollect);
    }
}

