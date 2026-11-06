/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.admin.IAdminService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.SystemAdminCreateValidate
 *  com.mdd.admin.validate.system.SystemAdminSearchValidate
 *  com.mdd.admin.validate.system.SystemAdminUpInfoValidate
 *  com.mdd.admin.validate.system.SystemAdminUpdateValidate
 *  com.mdd.admin.validate.system.SystemEditSelfValidate
 *  com.mdd.admin.vo.auth.AdminMySelfVo
 *  com.mdd.admin.vo.system.SystemAuthAdminDetailVo
 *  com.mdd.admin.vo.system.SystemAuthAdminListedVo
 *  com.mdd.admin.vo.system.SystemAuthAdminSelvesVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service.admin;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.SystemAdminCreateValidate;
import com.mdd.admin.validate.system.SystemAdminSearchValidate;
import com.mdd.admin.validate.system.SystemAdminUpInfoValidate;
import com.mdd.admin.validate.system.SystemAdminUpdateValidate;
import com.mdd.admin.validate.system.SystemEditSelfValidate;
import com.mdd.admin.vo.auth.AdminMySelfVo;
import com.mdd.admin.vo.system.SystemAuthAdminDetailVo;
import com.mdd.admin.vo.system.SystemAuthAdminListedVo;
import com.mdd.admin.vo.system.SystemAuthAdminSelvesVo;
import com.mdd.common.core.PageResult;
import java.util.List;

public interface IAdminService {
    public PageResult<SystemAuthAdminListedVo> list(PageValidate var1, SystemAdminSearchValidate var2);

    public SystemAuthAdminSelvesVo self(Integer var1);

    public SystemAuthAdminDetailVo detail(Integer var1);

    public void add(SystemAdminCreateValidate var1);

    public void edit(SystemAdminUpdateValidate var1, Integer var2);

    public void upInfo(SystemAdminUpInfoValidate var1, Integer var2);

    public void del(Integer var1, Integer var2);

    public void disable(Integer var1, Integer var2);

    public AdminMySelfVo mySelf(Integer var1, List<Integer> var2);

    public JSONObject getExportData(PageValidate var1, SystemAdminSearchValidate var2);

    public String export(SystemAdminSearchValidate var1);

    public void editSelf(SystemEditSelfValidate var1, Integer var2);
}

