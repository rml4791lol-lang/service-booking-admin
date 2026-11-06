/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.ICourseClassService
 *  com.mdd.front.validate.course.CourseClassValidate
 */
package com.mdd.front.service;

import com.mdd.front.validate.course.CourseClassValidate;
import java.util.List;
import java.util.Map;

public interface ICourseClassService {
    public List<Map<String, Object>> list(CourseClassValidate var1);

    public Map<String, Object> detail(Integer var1, Integer var2);

    public void consume(Integer var1, Map<String, Object> var2);

    public void applyRefund(Integer var1, Map<String, Object> var2);
}

