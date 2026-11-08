/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.IInstructorLeaveService
 *  com.mdd.admin.service.impl.InstructorLeaveServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.instructor.InstructorLeaveAuditValidate
 *  com.mdd.admin.validate.instructor.InstructorLeaveSearchValidate
 *  com.mdd.admin.vo.instructor.InstructorLeaveListVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.InstructorLeave
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorLeaveMapper
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.util.MessageUtil
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.util.Assert
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IInstructorLeaveService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorLeaveAuditValidate;
import com.mdd.admin.validate.instructor.InstructorLeaveSearchValidate;
import com.mdd.admin.vo.instructor.InstructorLeaveListVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.InstructorLeave;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorLeaveMapper;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.util.MessageUtil;
import java.io.Serializable;
import java.util.LinkedList;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.util.List;

@Service
public class InstructorLeaveServiceImpl
implements IInstructorLeaveService {
    @Resource
    InstructorLeaveMapper instructorLeaveMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    AdminMapper adminMapper;

    public PageResult<InstructorLeaveListVo> list(PageValidate pageValidate, InstructorLeaveSearchValidate searchValidate) {
        LinkedList<InstructorLeaveListVo> list;
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<InstructorLeave> queryWrapper = new QueryWrapper<InstructorLeave>();
        queryWrapper.orderByDesc("create_time");
        if (searchValidate.getStatus() != null) {
            queryWrapper.eq("status", (Object)searchValidate.getStatus());
        }
        if (searchValidate.getName() != null || searchValidate.getPhone() != null) {
            QueryWrapper<Instructor> iWrapper = new QueryWrapper<Instructor>();
            if (searchValidate.getName() != null) {
                iWrapper.like("name", (Object)searchValidate.getName());
            }
            if (searchValidate.getPhone() != null) {
                iWrapper.like("phone", (Object)searchValidate.getPhone());
            }
            List<Instructor> instructorList = this.instructorMapper.selectList((Wrapper)iWrapper);
            if (instructorList.isEmpty()) {
                return PageResult.iPageHandle((Long)0L, Long.valueOf(page), Long.valueOf(limit), new LinkedList());
            }
            LinkedList<Integer> ids = new LinkedList<Integer>();
            for (Instructor instructor : instructorList) {
                ids.add(instructor.getId());
            }
            queryWrapper.in("instructor_id", ids);
        }
        IPage<InstructorLeave> iPage = this.instructorLeaveMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        list = new LinkedList<InstructorLeaveListVo>();
        for (InstructorLeave item : iPage.getRecords()) {
            InstructorLeaveListVo instructorLeaveListVo = new InstructorLeaveListVo();
            BeanUtils.copyProperties((Object)item, (Object)instructorLeaveListVo);
            Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)item.getInstructorId());
            if (instructor != null) {
                instructorLeaveListVo.setInstructorName(instructor.getName());
                instructorLeaveListVo.setInstructorPhone(instructor.getPhone());
            }
            if (item.getStatus() == 0) {
                instructorLeaveListVo.setStatusDesc("\u5f85\u5ba1\u6279");
            } else if (item.getStatus() == 1) {
                instructorLeaveListVo.setStatusDesc("\u5df2\u901a\u8fc7");
            } else if (item.getStatus() == 2) {
                instructorLeaveListVo.setStatusDesc("\u5df2\u9a73\u56de");
            } else if (item.getStatus() == 4) {
                instructorLeaveListVo.setStatusDesc("\u5df2\u9500\u5047");
            }
            list.add(instructorLeaveListVo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public void audit(InstructorLeaveAuditValidate auditValidate, Integer adminId) {
        InstructorLeave leave = (InstructorLeave)this.instructorLeaveMapper.selectById((Serializable)auditValidate.getId());
        Assert.notNull((Object)leave, (String)"\u6570\u636e\u4e0d\u5b58\u5728");
        if (leave.getStatus() != 0) {
            throw new OperateException("\u8be5\u8bb0\u5f55\u5df2\u88ab\u5ba1\u6279\uff0c\u8bf7\u52ff\u91cd\u590d\u64cd\u4f5c");
        }
        String remark = auditValidate.getRemark() == null ? "" : auditValidate.getRemark().trim();
        if (auditValidate.getStatus() == 2 && remark.isEmpty()) {
            throw new OperateException("\u62d2\u7edd\u8bf7\u5047\u5fc5\u987b\u586b\u5199\u539f\u56e0");
        }
        Admin admin = (Admin)this.adminMapper.selectById((Serializable)adminId);
        String auditName = admin == null ? "\u672a\u77e5" : admin.getName();
        leave.setStatus(auditValidate.getStatus());
        leave.setAuditId(adminId);
        leave.setAuditName(auditName);
        leave.setAuditRemark(remark);
        leave.setAuditTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        leave.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.instructorLeaveMapper.updateById(leave);
        MessageUtil.sendLeaveAuditNotice((Integer)leave.getInstructorId(), (String)"instructor", (String)auditName, (auditValidate.getStatus() == 1 ? 1 : 0) != 0, (String)remark);
    }
}

