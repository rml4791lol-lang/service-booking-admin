/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ILeaveApplicationService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.leave.LeaveAuditValidate
 *  com.mdd.admin.validate.leave.LeaveSearchValidate
 *  com.mdd.admin.vo.leave.LeaveListedVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.leave.LeaveAuditValidate;
import com.mdd.admin.validate.leave.LeaveSearchValidate;
import com.mdd.admin.vo.leave.LeaveListedVo;
import com.mdd.common.core.PageResult;

public interface ILeaveApplicationService {
    public PageResult<LeaveListedVo> list(PageValidate var1, LeaveSearchValidate var2);

    public void audit(LeaveAuditValidate var1, Integer var2);
}

