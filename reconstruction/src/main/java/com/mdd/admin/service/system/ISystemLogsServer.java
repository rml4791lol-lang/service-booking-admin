/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.system.ISystemLogsServer
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.SystemSearchLoginsValidate
 *  com.mdd.admin.validate.system.SystemSearchOperateValidate
 *  com.mdd.admin.vo.system.SystemLogLoginVo
 *  com.mdd.admin.vo.system.SystemLogOperateVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service.system;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.SystemSearchLoginsValidate;
import com.mdd.admin.validate.system.SystemSearchOperateValidate;
import com.mdd.admin.vo.system.SystemLogLoginVo;
import com.mdd.admin.vo.system.SystemLogOperateVo;
import com.mdd.common.core.PageResult;

public interface ISystemLogsServer {
    public PageResult<SystemLogOperateVo> operate(PageValidate var1, SystemSearchOperateValidate var2);

    public PageResult<SystemLogLoginVo> login(PageValidate var1, SystemSearchLoginsValidate var2);
}

