/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.IPcService
 *  com.mdd.front.vo.article.PcArticleCenterVo
 *  com.mdd.front.vo.article.PcArticleDetailVo
 */
package com.mdd.front.service;

import com.mdd.front.vo.article.PcArticleCenterVo;
import com.mdd.front.vo.article.PcArticleDetailVo;
import java.util.List;
import java.util.Map;

public interface IPcService {
    public Map<String, Object> index();

    public Map<String, Object> getConfig();

    public List<PcArticleCenterVo> infoCenter();

    public PcArticleDetailVo articleDetail(Integer var1, Integer var2);
}

