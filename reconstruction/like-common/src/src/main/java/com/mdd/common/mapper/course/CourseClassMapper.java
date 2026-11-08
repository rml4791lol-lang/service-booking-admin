/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.ibatis.annotations.Mapper
 */
package com.mdd.common.mapper.course;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.course.CourseClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseClassMapper
extends IBaseMapper<CourseClass> {

    @Select("SELECT * FROM la_course_class WHERE id = #{id} AND is_delete = 0 FOR UPDATE")
    CourseClass selectByIdForUpdate(@Param("id") Integer id);
}
