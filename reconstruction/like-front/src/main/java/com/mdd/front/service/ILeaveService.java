/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.service.ILeaveService
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.validate.leave.LeaveApplyValidate
 *  com.mdd.front.validate.leave.LeaveAuditValidate
 *  com.mdd.front.vo.leave.LeaveListVo
 */
package com.mdd.front.service;

import com.mdd.common.core.PageResult;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.validate.leave.LeaveApplyValidate;
import com.mdd.front.validate.leave.LeaveAuditValidate;
import com.mdd.front.vo.leave.LeaveListVo;

public interface ILeaveService {
    public void apply(LeaveApplyValidate var1, Integer var2, boolean var3);

    public PageResult<LeaveListVo> myLeaves(PageValidate var1, Integer var2, Integer var3);

    public PageResult<LeaveListVo> classLeaves(PageValidate var1, Integer var2, Integer var3, Integer var4);

    public void audit(LeaveAuditValidate var1, Integer var2);
}

