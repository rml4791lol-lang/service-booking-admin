/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.instructor.IInstructorWithdrawalService
 *  com.mdd.admin.validate.instructor.InstructorWithdrawalAuditValidate
 *  com.mdd.admin.validate.instructor.InstructorWithdrawalSearchValidate
 *  com.mdd.admin.vo.instructor.InstructorWithdrawalListedVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service.instructor;

import com.mdd.admin.validate.instructor.InstructorWithdrawalAuditValidate;
import com.mdd.admin.validate.instructor.InstructorWithdrawalSearchValidate;
import com.mdd.admin.vo.instructor.InstructorWithdrawalListedVo;
import com.mdd.common.core.PageResult;
import java.util.Map;

public interface IInstructorWithdrawalService {
    public PageResult<InstructorWithdrawalListedVo> list(InstructorWithdrawalSearchValidate var1, Integer var2, Integer var3);

    public Map<String, Object> stats();

    public void audit(InstructorWithdrawalAuditValidate var1);
}

