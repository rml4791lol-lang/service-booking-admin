/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.crontab.CoursePreSaleActivateJob
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Component
 */
package com.mdd.admin.crontab;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.util.TimeUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component(value="coursePreSaleActivateJob")
public class CoursePreSaleActivateJob {
    @Resource
    CourseClassMapper courseClassMapper;

    public void execute() {
        long now = TimeUtils.timestamp();
        QueryWrapper<CourseClass> queryWrapper = new QueryWrapper<CourseClass>();
        queryWrapper.eq("status", (Object)2).eq("is_delete", (Object)0).le("start_date", (Object)now);
        List<CourseClass> targets = this.courseClassMapper.selectList((Wrapper)queryWrapper);
        if (targets == null || targets.isEmpty()) {
            System.out.println("\u9884\u552e\u8bfe\u7a0b\u6fc0\u6d3b\u4efb\u52a1\uff1a\u4eca\u65e5\u65e0\u9700\u6fc0\u6d3b\u7684\u9884\u552e\u8bfe\u7a0b");
            return;
        }
        for (CourseClass courseClass : targets) {
            courseClass.setStatus(Integer.valueOf(1));
            courseClass.setUpdateTime(Integer.valueOf(Long.valueOf(now).intValue()));
            this.courseClassMapper.updateById(courseClass);
        }
        System.out.println("\u9884\u552e\u8bfe\u7a0b\u6fc0\u6d3b\u4efb\u52a1\u6267\u884c\u5b8c\u6210\uff0c\u5171\u6fc0\u6d3b\u8bfe\u7a0b\u6570\u91cf\uff1a" + targets.size());
    }
}

