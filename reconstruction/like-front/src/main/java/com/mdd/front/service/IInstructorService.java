/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.IInstructorService
 *  com.mdd.front.validate.instructor.InstructorLeaveApplyValidate
 *  com.mdd.front.validate.instructor.InstructorProfileUpdateValidate
 *  com.mdd.front.validate.users.NewUserUpdateValidate
 */
package com.mdd.front.service;

import com.mdd.front.validate.instructor.InstructorLeaveApplyValidate;
import com.mdd.front.validate.instructor.InstructorProfileUpdateValidate;
import com.mdd.front.validate.users.NewUserUpdateValidate;
import java.util.List;
import java.util.Map;

public interface IInstructorService {
    public void mnpMobile(String var1, String var2);

    public void updateInstructorInfo(NewUserUpdateValidate var1, String var2);

    public Map<String, Object> getStudents(Integer var1, Integer var2, Integer var3, String var4);

    public Map<String, Object> getHomeData(Integer var1);

    public Map<String, Object> getProfile(Integer var1);

    public void updateProfile(Integer var1, InstructorProfileUpdateValidate var2);

    public Map<String, Object> getSchedule(Integer var1, String var2, Integer var3, Integer var4);

    public void coachCheckIn(Integer var1, Integer var2, Integer var3);

    public Map<String, Object> getAttendanceData(Integer var1, Integer var2, String var3, Integer var4);

    public Map<String, Object> changeAttendanceStatus(Integer var1, Integer var2, Integer var3, Integer var4, Integer var5, String var6, Integer var7, Integer var8);

    public void completeLesson(Integer var1, Integer var2, String var3, Integer var4, Integer var5, String var6, String var7);

    public Map<String, Object> getReportData(Integer var1);

    public void applyLeave(Integer var1, InstructorLeaveApplyValidate var2);

    public boolean checkLeave(Integer var1, Long var2, Long var3);

    public void cancelLeave(Integer var1);

    public List<Map<String, Object>> completedLessonList(Integer var1, String var2, Integer var3, Integer var4);

    public Map<String, Object> getStudentAccount(Integer var1, Integer var2);

    public Map<String, Object> getStudentLessonRecords(Integer var1, Integer var2, Integer var3, Integer var4, Integer var5);
}

