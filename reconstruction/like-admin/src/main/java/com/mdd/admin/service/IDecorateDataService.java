/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.IDecorateDataService
 *  com.mdd.admin.vo.decorate.DecorateDataArticleVo
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.vo.decorate.DecorateDataArticleVo;
import java.util.List;

public interface IDecorateDataService {
    public List<DecorateDataArticleVo> article(Integer var1);

    public JSONObject pc();
}

