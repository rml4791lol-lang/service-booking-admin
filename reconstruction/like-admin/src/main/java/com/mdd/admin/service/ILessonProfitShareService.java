/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.ILessonProfitShareService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.finance.LessonProfitShareSearchValidate
 *  com.mdd.admin.vo.finance.LessonProfitShareListVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.LessonProfitShareSearchValidate;
import com.mdd.admin.vo.finance.LessonProfitShareListVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface ILessonProfitShareService {
    public PageResult<LessonProfitShareListVo> lists(PageValidate var1, LessonProfitShareSearchValidate var2);

    public JSONObject getExportData(PageValidate var1, LessonProfitShareSearchValidate var2);

    public Map<String, Object> export(LessonProfitShareSearchValidate var1);

    public List<Map<String, Object>> communityOptions();
}

