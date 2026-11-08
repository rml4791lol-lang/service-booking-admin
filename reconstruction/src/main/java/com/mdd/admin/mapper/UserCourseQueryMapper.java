package com.mdd.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mdd.admin.validate.user.UserCourseSearchValidate;
import com.mdd.common.entity.course.UserCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 老课包列表查询（只读；写操作走 UserCourseMapper）。
 *
 * <p>姓名/手机号/课程名在关联表上，用 SQL JOIN 过滤；返回仍是一条 la_user_course 一行，
 * 便于按课包 id 精确调整。JOIN 均为 1:1，不会放大行数。</p>
 */
@Mapper
public interface UserCourseQueryMapper {

    IPage<UserCourse> page(IPage<UserCourse> page, @Param("q") UserCourseSearchValidate query);
}
