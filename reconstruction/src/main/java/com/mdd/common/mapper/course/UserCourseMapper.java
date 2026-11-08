/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  org.apache.ibatis.annotations.Mapper
 */
package com.mdd.common.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mdd.common.entity.course.UserCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserCourseMapper
extends BaseMapper<UserCourse> {

    @Select("SELECT * FROM la_user_course WHERE id = #{id} AND delete_time IS NULL FOR UPDATE")
    UserCourse selectByIdForUpdate(@Param("id") Integer id);

    @Update("UPDATE la_user_course SET used_lessons = COALESCE(used_lessons, 0) + 1, "
            + "remain_lessons = COALESCE(remain_lessons, 0) - 1, update_time = #{now} "
            + "WHERE id = #{id} AND COALESCE(remain_lessons, 0) > 0 "
            + "AND (status IS NULL OR status <> 2) AND delete_time IS NULL")
    int consumeOneLesson(@Param("id") Integer id, @Param("now") Long now);
}
