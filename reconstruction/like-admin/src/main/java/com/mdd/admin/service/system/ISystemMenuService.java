/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.system.ISystemMenuService
 *  com.mdd.admin.validate.system.SystemMenuCreateValidate
 *  com.mdd.admin.validate.system.SystemMenuUpdateValidate
 *  com.mdd.admin.vo.system.SystemAuthMenuVo
 */
package com.mdd.admin.service.system;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.validate.system.SystemMenuCreateValidate;
import com.mdd.admin.validate.system.SystemMenuUpdateValidate;
import com.mdd.admin.vo.system.SystemAuthMenuVo;
import java.util.List;

public interface ISystemMenuService {
    public JSONArray selectMenuByRoleId(List<Integer> var1);

    public JSONObject list();

    public JSONArray systemMenuLists();

    public JSONArray all();

    public SystemAuthMenuVo detail(Integer var1);

    public void add(SystemMenuCreateValidate var1);

    public void edit(SystemMenuUpdateValidate var1);

    public void del(Integer var1);
}

