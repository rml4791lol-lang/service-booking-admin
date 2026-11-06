/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.mdd.admin.service.system.IDeptService
 *  com.mdd.admin.validate.system.DeptCreateValidate
 *  com.mdd.admin.validate.system.DeptSearchValidate
 *  com.mdd.admin.validate.system.DeptUpdateValidate
 *  com.mdd.admin.vo.system.DeptVo
 */
package com.mdd.admin.service.system;

import com.alibaba.fastjson2.JSONArray;
import com.mdd.admin.validate.system.DeptCreateValidate;
import com.mdd.admin.validate.system.DeptSearchValidate;
import com.mdd.admin.validate.system.DeptUpdateValidate;
import com.mdd.admin.vo.system.DeptVo;

public interface IDeptService {
    public JSONArray list(DeptSearchValidate var1);

    public JSONArray all();

    public DeptVo detail(Integer var1);

    public void add(DeptCreateValidate var1);

    public void edit(DeptUpdateValidate var1);

    public void del(Integer var1);
}

