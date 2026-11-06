package com.mdd.front.service;

import com.mdd.front.validate.instructor.InstructorCertSaveValidate;
import com.mdd.front.vo.instructor.InstructorCertVo;
import java.util.List;
import java.util.Map;

/**
 * 教练端资质证件（#49）。
 *
 * <p>教练身份由 like-front 拦截器从 token 解析并写入 ThreadLocal，
 * 服务层所有查询/写入都带 instructor_id 条件，避免越权访问他人资质。</p>
 */
public interface IInstructorCertService {

    /**
     * 我的资质列表（含草稿/待审核/已通过/已驳回）。
     */
    List<InstructorCertVo> list(Integer instructorId);

    /**
     * 保存草稿：有 id 更新，无 id 新增；待审核/已通过不可编辑。
     */
    InstructorCertVo save(Integer instructorId, InstructorCertSaveValidate validate);

    /**
     * 提交审核（可多张）：只允许本人草稿/已驳回资质，提交后状态为待审核并写留痕。
     *
     * @return count=提交数量，ids=提交成功的资质ID
     */
    Map<String, Object> submit(Integer instructorId, List<Long> ids);
}
