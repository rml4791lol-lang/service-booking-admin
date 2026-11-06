/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ICourseClassService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.course.CourseClassCreateValidate
 *  com.mdd.admin.validate.course.CourseClassSearchValidate
 *  com.mdd.admin.validate.course.CourseClassUpdateValidate
 *  com.mdd.admin.vo.course.CourseClassListedVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.course.CourseClassCreateValidate;
import com.mdd.admin.validate.course.CourseClassSearchValidate;
import com.mdd.admin.validate.course.CourseClassUpdateValidate;
import com.mdd.admin.vo.course.CourseClassListedVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface ICourseClassService {
    public PageResult<CourseClassListedVo> list(PageValidate var1, CourseClassSearchValidate var2);

    public Map<String, Object> detail(Integer var1);

    public void add(CourseClassCreateValidate var1);

    public void edit(CourseClassUpdateValidate var1);

    public void del(Integer var1);

    public void setShow(Integer var1, Integer var2);

    public Map<String, Object> stats();

    public List<Map<String, Object>> enrolledStudents(Integer var1);
}

