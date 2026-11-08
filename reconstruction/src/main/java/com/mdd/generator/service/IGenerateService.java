/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.PageResult
 */
package com.mdd.generator.service;

import com.mdd.common.core.PageResult;
import com.mdd.generator.validate.GenParam;
import com.mdd.generator.validate.PageParam;
import com.mdd.generator.vo.DbColumnVo;
import com.mdd.generator.vo.DbTableVo;
import com.mdd.generator.vo.GenTableVo;
import java.util.List;
import java.util.Map;

public interface IGenerateService {
    public PageResult<DbTableVo> db(PageParam var1, Map<String, String> var2);

    public List<DbTableVo> dbAll(Map<String, String> var1);

    public List<DbColumnVo> dbColumn(String var1);

    public PageResult<GenTableVo> list(PageParam var1, Map<String, String> var2);

    public Map<String, Object> detail(Integer var1);

    public void importTable(String[] var1);

    public void editTable(GenParam var1);

    public void deleteTable(List<Integer> var1);

    public void syncTable(Integer var1);

    public Map<String, String> previewCode(Integer var1);

    public void genCode(String var1);

    public byte[] downloadCode(String[] var1);
}

