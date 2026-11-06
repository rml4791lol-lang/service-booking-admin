/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.service.IUserCourseService
 */
package com.mdd.front.service;

import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface IUserCourseService {
    public PageResult<Object> myList(Integer var1, Integer var2, Integer var3);

    public Map<String, Object> checkPurchased(Integer var1, Integer var2);

    public Map<String, Object> stats(Integer var1);

    public Map<String, Object> courseStats(Integer var1, Integer var2);

    public List<Map<String, Object>> completedLessonList(Integer var1, String var2, Integer var3, Integer var4);

    public List<Map<String, Object>> todayLessons(Integer var1);

    public List<Map<String, Object>> upcomingLessons(Integer var1);

    public void bookLesson(Integer var1, Integer var2, Integer var3);

    public void cancelBooking(Integer var1, Integer var2, Integer var3);
}

