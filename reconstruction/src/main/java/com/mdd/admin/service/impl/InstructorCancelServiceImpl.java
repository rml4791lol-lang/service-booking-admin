/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.IInstructorCancelService
 *  com.mdd.admin.service.impl.InstructorCancelServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.instructor.InstructorCancelAuditValidate
 *  com.mdd.admin.validate.instructor.InstructorCancelSearchValidate
 *  com.mdd.admin.vo.instructor.InstructorCancelListVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.InstructorCancel
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorCancelMapper
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.admin.AdminMapper
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IInstructorCancelService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorCancelAuditValidate;
import com.mdd.admin.validate.instructor.InstructorCancelSearchValidate;
import com.mdd.admin.vo.instructor.InstructorCancelListVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.InstructorCancel;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorCancelMapper;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.admin.AdminMapper;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class InstructorCancelServiceImpl
implements IInstructorCancelService {
    @Resource
    InstructorCancelMapper instructorCancelMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    AdminMapper adminMapper;

    public PageResult<InstructorCancelListVo> list(PageValidate pageValidate, InstructorCancelSearchValidate searchValidate) {
        boolean hasPhone;
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<InstructorCancel> queryWrapper = new QueryWrapper<InstructorCancel>();
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc("create_time");
        if (searchValidate.getStatus() != null) {
            queryWrapper.eq("status", (Object)searchValidate.getStatus());
        }
        String searchName = searchValidate.getName();
        String searchPhone = searchValidate.getPhone();
        boolean hasName = searchName != null && !searchName.trim().isEmpty();
        boolean bl = hasPhone = searchPhone != null && !searchPhone.trim().isEmpty();
        if (hasName || hasPhone) {
            List<Instructor> instructors;
            QueryWrapper<Instructor> iWrapper = new QueryWrapper<Instructor>();
            if (hasName) {
                iWrapper.like("name", (Object)searchName);
            }
            if (hasPhone) {
                iWrapper.like("phone", (Object)searchPhone);
            }
            if ((instructors = this.instructorMapper.selectList((Wrapper)iWrapper)).isEmpty()) {
                return PageResult.iPageHandle((Long)0L, Long.valueOf(page), Long.valueOf(limit), new LinkedList());
            }
            LinkedList<Integer> ids = new LinkedList<Integer>();
            for (Instructor instructor : instructors) {
                ids.add(instructor.getId());
            }
            queryWrapper.in("instructor_id", ids);
        }
        IPage<InstructorCancel> iPage = this.instructorCancelMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        LinkedList<InstructorCancelListVo> list = new LinkedList<InstructorCancelListVo>();
        for (InstructorCancel item : iPage.getRecords()) {
            InstructorCancelListVo vo = new InstructorCancelListVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)item.getInstructorId());
            if (instructor != null) {
                vo.setInstructorName(instructor.getName());
                vo.setInstructorPhone(instructor.getPhone());
            } else {
                vo.setInstructorName("\u5df2\u5220\u9664");
                vo.setInstructorPhone("");
            }
            if (item.getStatus() == 0) {
                vo.setStatusDesc("\u5f85\u5ba1\u6838");
            } else if (item.getStatus() == 1) {
                vo.setStatusDesc("\u5df2\u901a\u8fc7");
            } else if (item.getStatus() == 2) {
                vo.setStatusDesc("\u5df2\u9a73\u56de");
            }
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public void audit(InstructorCancelAuditValidate auditValidate, Integer adminId) {
        InstructorCancel cancel = (InstructorCancel)this.instructorCancelMapper.selectById((Serializable)auditValidate.getId());
        if (cancel == null) {
            throw new OperateException("\u6570\u636e\u4e0d\u5b58\u5728");
        }
        if (cancel.getStatus() != 0) {
            throw new OperateException("\u8be5\u8bb0\u5f55\u5df2\u88ab\u5ba1\u6279\uff0c\u8bf7\u52ff\u91cd\u590d\u64cd\u4f5c");
        }
        String remark = auditValidate.getRemark() == null ? "" : auditValidate.getRemark().trim();
        if (auditValidate.getStatus() == 2 && remark.isEmpty()) {
            throw new OperateException("\u62d2\u7edd\u6ce8\u9500\u5fc5\u987b\u586b\u5199\u539f\u56e0");
        }
        Admin admin = (Admin)this.adminMapper.selectById((Serializable)adminId);
        String auditName = admin == null ? "\u672a\u77e5" : admin.getName();
        cancel.setStatus(auditValidate.getStatus());
        cancel.setAuditId(adminId);
        cancel.setAuditName(auditName);
        cancel.setAuditRemark(remark);
        cancel.setAuditTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        cancel.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.instructorCancelMapper.updateById(cancel);
        if (auditValidate.getStatus() == 1) {
            this.instructorMapper.deleteById((Serializable)cancel.getInstructorId());
        }
    }
}

