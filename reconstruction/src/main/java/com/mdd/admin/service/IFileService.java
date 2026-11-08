/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.IFileService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.file.FileCateValidate
 *  com.mdd.admin.validate.file.FileSearchValidate
 *  com.mdd.admin.vo.album.FileVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.file.FileCateValidate;
import com.mdd.admin.validate.file.FileSearchValidate;
import com.mdd.admin.vo.album.FileVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface IFileService {
    public PageResult<FileVo> fileList(PageValidate var1, FileSearchValidate var2);

    public void fileRename(Integer var1, String var2);

    public void fileMove(List<Integer> var1, Integer var2);

    public Integer fileAdd(Map<String, String> var1);

    public void fileDel(List<Integer> var1);

    public JSONObject cateList(FileSearchValidate var1);

    public void cateAdd(FileCateValidate var1);

    public void cateRename(Integer var1, String var2);

    public void cateDel(Integer var1);
}

