/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONObject
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.entity.article.Article
 *  com.mdd.common.entity.article.ArticleCate
 *  com.mdd.common.entity.article.ArticleCollect
 *  com.mdd.common.entity.decorate.DecoratePage
 *  com.mdd.common.enums.YesNoEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.article.ArticleCateMapper
 *  com.mdd.common.mapper.article.ArticleCollectMapper
 *  com.mdd.common.mapper.article.ArticleMapper
 *  com.mdd.common.mapper.decorate.DecoratePageMapper
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.ListUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.service.IPcService
 *  com.mdd.front.service.impl.PcServiceImpI
 *  com.mdd.front.vo.article.PcArticleCenterVo
 *  com.mdd.front.vo.article.PcArticleDetailVo
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.entity.article.Article;
import com.mdd.common.entity.article.ArticleCate;
import com.mdd.common.entity.article.ArticleCollect;
import com.mdd.common.entity.decorate.DecoratePage;
import com.mdd.common.enums.YesNoEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.article.ArticleCateMapper;
import com.mdd.common.mapper.article.ArticleCollectMapper;
import com.mdd.common.mapper.article.ArticleMapper;
import com.mdd.common.mapper.decorate.DecoratePageMapper;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.ListUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IPcService;
import com.mdd.front.vo.article.PcArticleCenterVo;
import com.mdd.front.vo.article.PcArticleDetailVo;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class PcServiceImpI
implements IPcService {
    @Resource
    DecoratePageMapper decoratePageMapper;
    @Resource
    ArticleCateMapper articleCategoryMapper;
    @Resource
    ArticleCollectMapper articleCollectMapper;
    @Resource
    ArticleMapper articleMapper;

    public Map<String, Object> index() {
        LinkedHashMap<String, Object> indexData = new LinkedHashMap<String, Object>();
        DecoratePage decoratePage = (DecoratePage)this.decoratePageMapper.selectOne(new QueryWrapper<DecoratePage>().eq("id", (Object)4).last("limit 1"));
        List<Article> articlesAll = this.articleMapper.selectList(new QueryWrapper<Article>().eq("is_show", (Object)1).isNull("delete_time").orderByDesc("sort").orderByDesc("id").last("limit 5"));
        LinkedList<Object> articlesAllList = new LinkedList<Object>();
        for (Article article : articlesAll) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", article.getId());
            map.put("cid", article.getCid());
            map.put("author", article.getAuthor());
            map.put("title", article.getTitle());
            map.put("abstract", article.getAbstractField());
            map.put("click", article.getClickActual() + article.getClickVirtual());
            map.put("create_time", TimeUtils.timestampToDate((Long)article.getCreateTime()));
            map.put("desc", article.getDesc());
            map.put("image", UrlUtils.toAbsoluteUrl((String)article.getImage()));
            articlesAllList.add(map);
        }
        List<Article> articlesNew = this.articleMapper.selectList(new QueryWrapper<Article>().eq("is_show", (Object)1).isNull("delete_time").orderByDesc("id").last("limit 7"));
        LinkedList<Object> articlesNewList = new LinkedList<Object>();
        for (Article article : articlesNew) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", article.getId());
            map.put("cid", article.getCid());
            map.put("author", article.getAuthor());
            map.put("title", article.getTitle());
            map.put("abstract", article.getAbstractField());
            map.put("click", article.getClickActual() + article.getClickVirtual());
            map.put("create_time", TimeUtils.timestampToDate((Long)article.getCreateTime()));
            map.put("desc", article.getDesc());
            map.put("image", UrlUtils.toAbsoluteUrl((String)article.getImage()));
            articlesNewList.add(map);
        }
        List<Article> articlesHot = this.articleMapper.selectList(new QueryWrapper<Article>().eq("is_show", (Object)1).isNull("delete_time").orderByDesc("click_actual").last("limit 7"));
        LinkedList articlesHostList = new LinkedList();
        for (Article article : articlesHot) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", article.getId());
            map.put("cid", article.getCid());
            map.put("author", article.getAuthor());
            map.put("title", article.getTitle());
            map.put("abstract", article.getAbstractField());
            map.put("click", article.getClickActual() + article.getClickVirtual());
            map.put("create_time", TimeUtils.timestampToDate((Long)article.getCreateTime()));
            map.put("desc", article.getDesc());
            map.put("image", UrlUtils.toAbsoluteUrl((String)article.getImage()));
            articlesHostList.add(map);
        }
        String data = decoratePage.getData();
        JSONArray dataArray = JSONArray.parseArray((String)data, (JSONReader.Feature[])new JSONReader.Feature[0]);
        for (int i = 0; i < dataArray.size(); ++i) {
            JSONObject dataJson = dataArray.getJSONObject(i);
            JSONArray tempArray = dataJson.getJSONObject("content").getJSONArray("data");
            if (StringUtils.isNull(tempArray)) {
                JSONObject tempJSON = dataJson.getJSONObject("content");
                if (StringUtils.isNull(tempJSON)) continue;
                if (StringUtils.isNotNull(tempJSON.getString("image"))) {
                    tempJSON.put("image", (Object)UrlUtils.toAbsoluteUrl((String)tempJSON.getString("image")));
                }
                if (!StringUtils.isNotNull(tempJSON.getString("qrcode"))) continue;
                tempJSON.put("qrcode", (Object)UrlUtils.toAbsoluteUrl((String)tempJSON.getString("qrcode")));
                continue;
            }
            for (int j = 0; j < tempArray.size(); ++j) {
                JSONObject tempImageJson = tempArray.getJSONObject(j);
                tempImageJson.put("image", (Object)UrlUtils.toAbsoluteUrl((String)tempImageJson.getString("image")));
            }
        }
        decoratePage.setData(dataArray.toJSONString(new JSONWriter.Feature[0]));
        indexData.put("page", decoratePage);
        indexData.put("all", articlesAllList);
        indexData.put("new", articlesNewList);
        indexData.put("hot", articlesHostList);
        return indexData;
    }

    public Map<String, Object> getConfig() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> loginMap = new LinkedHashMap<String, Object>();
        Map<String, String> loginConfig = ConfigUtils.get((String)"login");
        loginMap.put("login_way", JSONArray.parseArray((String)loginConfig.getOrDefault("login_way", "[]"), (JSONReader.Feature[])new JSONReader.Feature[0]));
        loginMap.put("coerce_mobile", Integer.parseInt(loginConfig.getOrDefault("coerce_mobile", "0")));
        loginMap.put("login_agreement", Integer.parseInt(loginConfig.getOrDefault("login_agreement", "0")));
        loginMap.put("third_auth", Integer.parseInt(loginConfig.getOrDefault("third_auth", "0")));
        loginMap.put("wechat_auth", Integer.parseInt(loginConfig.getOrDefault("wechat_auth", "0")));
        loginMap.put("qq_auth", Integer.parseInt(loginConfig.getOrDefault("qq_auth", "0")));
        String siteStatistics = ConfigUtils.get((String)"siteStatistics", (String)"clarity_code", (String)"");
        Map<String, String> websiteConfig = ConfigUtils.get((String)"copyright");
        String copyright = websiteConfig.getOrDefault("config", "[]");
        List<Map<String, String>> copyrightMap = ListUtils.stringToListAsMapStr((String)copyright);
        String oaQrCode = ConfigUtils.get((String)"oa_setting", (String)"qr_code", (String)"");
        oaQrCode = StringUtils.isEmpty((String)oaQrCode) ? "" : UrlUtils.toAbsoluteUrl((String)oaQrCode);
        String mnpQrCode = ConfigUtils.get((String)"mnp_setting", (String)"qr_code", (String)"");
        mnpQrCode = StringUtils.isEmpty((String)mnpQrCode) ? "" : UrlUtils.toAbsoluteUrl((String)mnpQrCode);
        JSONObject qrcode = new JSONObject();
        qrcode.put("oa", (Object)oaQrCode);
        qrcode.put("mnp", (Object)mnpQrCode);
        JSONObject website = new JSONObject();
        website.put("shop_name", (Object)ConfigUtils.get((String)"website", (String)"shop_name", (String)""));
        website.put("shop_logo", (Object)UrlUtils.toAbsoluteUrl((String)ConfigUtils.get((String)"website", (String)"shop_logo", (String)"")));
        website.put("pc_logo", (Object)UrlUtils.toAbsoluteUrl((String)ConfigUtils.get((String)"website", (String)"pc_logo", (String)"")));
        website.put("pc_title", (Object)ConfigUtils.get((String)"website", (String)"pc_title", (String)""));
        website.put("pc_ico", (Object)UrlUtils.toAbsoluteUrl((String)ConfigUtils.get((String)"website", (String)"pc_ico", (String)"")));
        website.put("pc_desc", (Object)ConfigUtils.get((String)"website", (String)"pc_desc", (String)""));
        website.put("pc_keywords", (Object)ConfigUtils.get((String)"website", (String)"pc_keywords", (String)""));
        response.put("admin_url", "2");
        response.put("copyright", copyrightMap);
        response.put("domain", UrlUtils.domain());
        response.put("login", loginMap);
        response.put("version", GlobalConfig.version);
        response.put("siteStatistics", siteStatistics);
        response.put("website", website);
        response.put("qrcode", qrcode);
        return response;
    }

    public List<PcArticleCenterVo> infoCenter() {
        List<ArticleCate> articleCategoryList = this.articleCategoryMapper.selectList(new QueryWrapper<ArticleCate>().eq("is_show", (Object)1).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id")));
        LinkedList<PcArticleCenterVo> list = new LinkedList<PcArticleCenterVo>();
        for (ArticleCate articleCategory : articleCategoryList) {
            List<Article> articleList = this.articleMapper.selectList(new QueryWrapper<Article>().eq("cid", (Object)articleCategory.getId()).eq("is_show", (Object)1).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id")).last("limit 10"));
            LinkedList articles = new LinkedList();
            for (Article article : articleList) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("id", article.getId());
                map.put("cid", article.getCid());
                map.put("author", article.getAuthor());
                map.put("title", article.getTitle());
                map.put("abstract", article.getAbstractField());
                map.put("click", article.getClickActual() + article.getClickVirtual());
                map.put("create_time", TimeUtils.timestampToDate((Long)article.getCreateTime()));
                map.put("desc", article.getDesc());
                map.put("image", UrlUtils.toAbsoluteUrl((String)article.getImage()));
                map.put("is_show", article.getIsShow());
                map.put("sort", article.getSort());
                map.put("update_time", TimeUtils.timestampToDate((Long)article.getUpdateTime()));
                articles.add(map);
            }
            PcArticleCenterVo vo = new PcArticleCenterVo();
            vo.setId(articleCategory.getId());
            vo.setName(articleCategory.getName());
            vo.setArticle(articles);
            list.add(vo);
        }
        return list;
    }

    public PcArticleDetailVo articleDetail(Integer id, Integer userId) {
        Article article = (Article)this.articleMapper.selectOne(new QueryWrapper<Article>().eq("id", (Object)id).isNull("delete_time").eq("is_show", (Object)YesNoEnum.YES.getCode()).last("limit 1"));
        if (StringUtils.isNull(article)) {
            throw new OperateException("\u6587\u7ae0\u6570\u636e\u4e0d\u5b58\u5728!");
        }
        ArticleCate articleCategory = (ArticleCate)this.articleCategoryMapper.selectOne(new QueryWrapper<ArticleCate>().eq("id", (Object)article.getCid()).isNull("delete_time"));
        Article prev = (Article)this.articleMapper.selectOne(new QueryWrapper<Article>().select(new String[]{"id,title"}).lt("id", (Object)id).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id")).last("limit 1"));
        Article next = (Article)this.articleMapper.selectOne(new QueryWrapper<Article>().select(new String[]{"id,title"}).gt("id", (Object)id).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id")).last("limit 1"));
        ArticleCollect collect = (ArticleCollect)this.articleCollectMapper.selectOne(new QueryWrapper<ArticleCollect>().eq("article_id", (Object)article.getId()).eq("user_id", (Object)userId).eq("status", (Object)YesNoEnum.YES.getCode()).isNull("delete_time").last("limit 1"));
        List<Article> news = this.articleMapper.selectList(new QueryWrapper<Article>().select(new String[]{"id,title,image,create_time,update_time"}).eq("cid", (Object)article.getCid()).isNull("delete_time").orderByDesc("id").last("limit 8"));
        LinkedList newsList = new LinkedList();
        for (Article newArticle : news) {
            LinkedHashMap<String, Object> newsMap = new LinkedHashMap<String, Object>();
            newsMap.put("id", newArticle.getId());
            newsMap.put("title", newArticle.getTitle());
            newsMap.put("image", UrlUtils.toAbsoluteUrl((String)newArticle.getImage()));
            newsMap.put("createTime", TimeUtils.timestampToDate((Long)newArticle.getCreateTime()));
            newsMap.put("updateTime", TimeUtils.timestampToDate((Long)newArticle.getUpdateTime()));
            newsList.add(newsMap);
        }
        PcArticleDetailVo vo = new PcArticleDetailVo();
        BeanUtils.copyProperties((Object)article, (Object)vo);
        vo.setCreateTime(TimeUtils.timestampToDate((Long)article.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)article.getUpdateTime()));
        vo.setCateName(StringUtils.isNotNull(articleCategory) ? articleCategory.getName() : "");
        vo.setCollect(Boolean.valueOf(StringUtils.isNotNull(collect)));
        vo.setIsShow(article.getIsShow());
        vo.setClick(Integer.valueOf(article.getClickActual() + article.getClickVirtual()));
        vo.setNews(newsList);
        vo.setLast((Object)new JSONArray());
        vo.setNext((Object)new JSONArray());
        if (StringUtils.isNotNull(prev)) {
            LinkedHashMap<String, Object> prevMap = new LinkedHashMap<String, Object>();
            prevMap.put("id", prev.getId());
            prevMap.put("title", prev.getTitle());
            vo.setLast(prevMap);
        }
        if (StringUtils.isNotNull(next)) {
            LinkedHashMap<String, Object> nextMap = new LinkedHashMap<String, Object>();
            nextMap.put("id", next.getId());
            nextMap.put("title", next.getTitle());
            vo.setNext(nextMap);
        }
        article.setClickActual(Integer.valueOf(article.getClickActual() + 1));
        this.articleMapper.updateById(article);
        return vo;
    }
}

