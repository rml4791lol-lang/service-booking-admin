/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.IInstructorService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.instructor.InstructorCreateValidate
 *  com.mdd.admin.validate.instructor.InstructorSearchValidate
 *  com.mdd.admin.validate.instructor.InstructorUpdateValidate
 *  com.mdd.admin.vo.instructor.InstructorDetailVo
 *  com.mdd.admin.vo.instructor.InstructorListedVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorCreateValidate;
import com.mdd.admin.validate.instructor.InstructorSearchValidate;
import com.mdd.admin.validate.instructor.InstructorUpdateValidate;
import com.mdd.admin.vo.instructor.InstructorDetailVo;
import com.mdd.admin.vo.instructor.InstructorListedVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface IInstructorService {
    public PageResult<InstructorListedVo> list(PageValidate var1, InstructorSearchValidate var2);

    public List<Map<String, Object>> listAllForSelect();

    public Map<String, Object> options();

    public InstructorDetailVo detail(Integer var1);

    public void add(InstructorCreateValidate var1);

    public void edit(InstructorUpdateValidate var1);

    public void del(Integer var1);

    public JSONObject getExportData(PageValidate var1, InstructorSearchValidate var2);

    public String export(InstructorSearchValidate var1);
}

