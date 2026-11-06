/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.crontab.CourseClassStatusJob
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 */
package com.mdd.admin.crontab;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.util.TimeUtils;
import java.util.List;
import javax.annotation.Resource;

public class CourseClassStatusJob {
    @Resource
    CourseClassMapper courseClassMapper;

    public void execute() {
        long now = TimeUtils.timestamp();
        QueryWrapper<CourseClass> queryWrapper = new QueryWrapper<CourseClass>();
        queryWrapper.eq("status", (Object)1).eq("is_delete", (Object)0).lt("end_date", (Object)now);
        List<CourseClass> targets = this.courseClassMapper.selectList((Wrapper)queryWrapper);
        if (targets == null || targets.isEmpty()) {
            return;
        }
        for (CourseClass courseClass : targets) {
            courseClass.setStatus(Integer.valueOf(0));
            courseClass.setUpdateTime(Integer.valueOf(Long.valueOf(now).intValue()));
            this.courseClassMapper.updateById(courseClass);
        }
        System.out.println("\u73ed\u7ea7\u5230\u671f\u81ea\u52a8\u7ed3\u8bfe\u4efb\u52a1\u6267\u884c\u5b8c\u6210\uff0c\u5171\u5904\u7406\u73ed\u7ea7\u6570\u91cf\uff1a" + targets.size());
    }
}

