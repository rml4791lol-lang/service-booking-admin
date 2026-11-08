package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorCertSearchValidate;
import com.mdd.admin.vo.instructor.InstructorCertListedVo;
import com.mdd.common.core.PageResult;
import java.util.Map;

/**
 * 后台教练资质审核（#49）。
 */
public interface IInstructorCertService {

    /**
     * 资质列表：按状态/教练/类型/关键词筛选，分页。
     */
    PageResult<InstructorCertListedVo> list(PageValidate pageValidate,
                                            InstructorCertSearchValidate searchValidate);

    /**
     * 资质详情 + 审核留痕（按时间倒序）。
     */
    Map<String, Object> detail(Long id);

    /**
     * 审核通过/驳回；驳回原因必填，写审核意见与留痕。
     */
    void audit(Long id, String action, String remark, Integer adminId, String adminName, String ip);
}
