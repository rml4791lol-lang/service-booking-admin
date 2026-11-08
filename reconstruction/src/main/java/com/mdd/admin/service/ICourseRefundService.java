/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ICourseRefundService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.refund.CourseRefundAuditValidate
 *  com.mdd.admin.validate.refund.CourseRefundSearchValidate
 *  com.mdd.admin.vo.refund.CourseRefundListedVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.refund.CourseRefundAuditValidate;
import com.mdd.admin.validate.refund.CourseRefundSearchValidate;
import com.mdd.admin.vo.refund.CourseRefundListedVo;
import com.mdd.common.core.PageResult;

public interface ICourseRefundService {
    public PageResult<CourseRefundListedVo> list(PageValidate var1, CourseRefundSearchValidate var2);

    public void audit(CourseRefundAuditValidate var1, Integer var2);
}

