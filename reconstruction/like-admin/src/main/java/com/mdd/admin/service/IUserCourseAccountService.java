/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IUserCourseAccountService
 *  com.mdd.common.entity.course.UserCourse
 */
package com.mdd.admin.service;

import com.mdd.common.entity.course.UserCourse;
import java.util.List;
import java.util.Map;

public interface IUserCourseAccountService {
    public Map<String, Object> getAccount(Integer var1);

    public Map<String, Object> getLessonRecords(Integer var1, Integer var2, Integer var3, Integer var4);

    public int countEffectiveUsedLessons(Integer var1, Integer var2);

    public int countEffectiveUsedLessons(Integer var1, Integer var2, Integer var3);

    public int countEffectiveUsedLessonsForEnrollment(UserCourse var1);

    /**
     * 批量口径：给定用户集合，返回其全部有效课包（userCourseId → 有效已上次数）。
     * 与学员详情共用消课流水/退款状态/FIFO 分配逻辑，供老课包列表展示系统口径。
     */
    Map<Integer, Integer> countEffectiveUsedLessonsByUserIds(List<Integer> userIds);
}

