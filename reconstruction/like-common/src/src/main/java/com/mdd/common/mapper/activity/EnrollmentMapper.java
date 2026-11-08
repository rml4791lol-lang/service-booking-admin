package com.mdd.common.mapper.activity;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.activity.Enrollment;
import org.apache.ibatis.annotations.Mapper;

/**
 * Enrollment Mapper（T29.7 / #27）。
 */
@Mapper
public interface EnrollmentMapper extends IBaseMapper<Enrollment> {
}
