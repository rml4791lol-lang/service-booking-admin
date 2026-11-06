/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IInstructorLeaveService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.instructor.InstructorLeaveAuditValidate
 *  com.mdd.admin.validate.instructor.InstructorLeaveSearchValidate
 *  com.mdd.admin.vo.instructor.InstructorLeaveListVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorLeaveAuditValidate;
import com.mdd.admin.validate.instructor.InstructorLeaveSearchValidate;
import com.mdd.admin.vo.instructor.InstructorLeaveListVo;
import com.mdd.common.core.PageResult;

public interface IInstructorLeaveService {
    public PageResult<InstructorLeaveListVo> list(PageValidate var1, InstructorLeaveSearchValidate var2);

    public void audit(InstructorLeaveAuditValidate var1, Integer var2);
}

