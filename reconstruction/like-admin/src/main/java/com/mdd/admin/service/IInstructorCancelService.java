/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IInstructorCancelService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.instructor.InstructorCancelAuditValidate
 *  com.mdd.admin.validate.instructor.InstructorCancelSearchValidate
 *  com.mdd.admin.vo.instructor.InstructorCancelListVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorCancelAuditValidate;
import com.mdd.admin.validate.instructor.InstructorCancelSearchValidate;
import com.mdd.admin.vo.instructor.InstructorCancelListVo;
import com.mdd.common.core.PageResult;

public interface IInstructorCancelService {
    public PageResult<InstructorCancelListVo> list(PageValidate var1, InstructorCancelSearchValidate var2);

    public void audit(InstructorCancelAuditValidate var1, Integer var2);
}

