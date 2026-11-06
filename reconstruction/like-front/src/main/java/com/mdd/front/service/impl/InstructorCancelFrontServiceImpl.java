/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.entity.InstructorCancel
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorCancelMapper
 *  com.mdd.front.service.IInstructorCancelFrontService
 *  com.mdd.front.service.impl.InstructorCancelFrontServiceImpl
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.InstructorCancel;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorCancelMapper;
import com.mdd.front.service.IInstructorCancelFrontService;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class InstructorCancelFrontServiceImpl
implements IInstructorCancelFrontService {
    @Resource
    InstructorCancelMapper instructorCancelMapper;

    public void apply(Integer instructorId, Map<String, Object> params) {
        InstructorCancel existing = (InstructorCancel)this.instructorCancelMapper.selectOne(new QueryWrapper<InstructorCancel>().eq("instructor_id", (Object)instructorId).eq("status", (Object)0).isNull("delete_time").last("LIMIT 1"));
        if (existing != null) {
            throw new OperateException("\u60a8\u5df2\u6709\u5f85\u5ba1\u6838\u7684\u6ce8\u9500\u7533\u8bf7\uff0c\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4");
        }
        InstructorCancel cancel = new InstructorCancel();
        cancel.setInstructorId(instructorId);
        cancel.setReason(params.getOrDefault("reason", "").toString());
        cancel.setDetail(params.getOrDefault("detail", "").toString());
        cancel.setStatus(Integer.valueOf(0));
        cancel.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        cancel.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.instructorCancelMapper.insert(cancel);
    }

    public Map<String, Object> latestStatus(Integer instructorId) {
        InstructorCancel latest = (InstructorCancel)this.instructorCancelMapper.selectOne(new QueryWrapper<InstructorCancel>().eq("instructor_id", (Object)instructorId).isNull("delete_time").orderByDesc("create_time").last("LIMIT 1"));
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        if (latest != null) {
            result.put("hasApply", true);
            result.put("status", latest.getStatus());
            result.put("reason", latest.getReason());
            result.put("detail", latest.getDetail());
            result.put("auditRemark", latest.getAuditRemark());
            result.put("createTime", latest.getCreateTime());
        } else {
            result.put("hasApply", false);
        }
        return result;
    }
}

