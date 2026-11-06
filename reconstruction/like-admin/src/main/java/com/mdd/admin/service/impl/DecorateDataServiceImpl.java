/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.admin.service.IDecorateDataService
 *  com.mdd.admin.service.impl.DecorateDataServiceImpl
 *  com.mdd.admin.vo.decorate.DecorateDataArticleVo
 *  com.mdd.common.entity.article.Article
 *  com.mdd.common.entity.decorate.DecoratePage
 *  com.mdd.common.mapper.article.ArticleMapper
 *  com.mdd.common.mapper.decorate.DecoratePageMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.YmlUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.admin.service.IDecorateDataService;
import com.mdd.admin.vo.decorate.DecorateDataArticleVo;
import com.mdd.common.entity.article.Article;
import com.mdd.common.entity.decorate.DecoratePage;
import com.mdd.common.mapper.article.ArticleMapper;
import com.mdd.common.mapper.decorate.DecoratePageMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class DecorateDataServiceImpl
implements IDecorateDataService {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    DecoratePageMapper decoratePageMapper;

    public List<DecorateDataArticleVo> article(Integer limit) {
        List<Article> articles = this.articleMapper.selectList(new QueryWrapper<Article>().eq("is_show", (Object)1).orderByDesc("id").last("limit " + limit));
        LinkedList<DecorateDataArticleVo> list = new LinkedList<DecorateDataArticleVo>();
        for (Article article : articles) {
            DecorateDataArticleVo vo = new DecorateDataArticleVo();
            BeanUtils.copyProperties((Object)article, (Object)vo);
            vo.setImage(UrlUtils.toAdminAbsoluteUrl((String)vo.getImage()));
            Integer clickActual = vo.getClickActual() != null ? vo.getClickActual() : 0;
            Integer clickVirtual = vo.getClickVirtual() != null ? vo.getClickVirtual() : 0;
            Integer click = clickActual + clickVirtual;
            vo.setClick(click);
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            list.add(vo);
        }
        return list;
    }

    public JSONObject pc() {
        JSONObject pc = new JSONObject();
        DecoratePage decoratePage = (DecoratePage)this.decoratePageMapper.selectById((Serializable)Integer.valueOf(4));
        Assert.notNull((Object)decoratePage, (String)"\u6570\u636e\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        String frontUrl = YmlUtils.get((String)"like.front-url");
        frontUrl = StringUtils.isNotEmpty((String)frontUrl) && frontUrl.endsWith("/") ? frontUrl + "pc" : frontUrl + "/pc";
        pc.put("update_time", TimeUtils.timestampToDate((Long)decoratePage.getUpdateTime()));
        pc.put("pc_url", frontUrl);
        return pc;
    }
}

