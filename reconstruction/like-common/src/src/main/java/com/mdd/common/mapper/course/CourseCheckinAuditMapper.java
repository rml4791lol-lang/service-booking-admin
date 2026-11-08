package com.mdd.common.mapper.course;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.course.CourseCheckinAudit;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人工补核销审计 Mapper（la_course_checkin_audit）。
 */
@Mapper
public interface CourseCheckinAuditMapper extends IBaseMapper<CourseCheckinAudit> {
}
