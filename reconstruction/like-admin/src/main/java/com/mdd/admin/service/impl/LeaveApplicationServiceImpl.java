/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.ILeaveApplicationService
 *  com.mdd.admin.service.impl.LeaveApplicationServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.leave.LeaveAuditValidate
 *  com.mdd.admin.validate.leave.LeaveSearchValidate
 *  com.mdd.admin.vo.leave.LeaveListedVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.LeaveApplication
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.LeaveApplicationMapper
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.ILeaveApplicationService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.leave.LeaveAuditValidate;
import com.mdd.admin.validate.leave.LeaveSearchValidate;
import com.mdd.admin.vo.leave.LeaveListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.LeaveApplication;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.LeaveApplicationMapper;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import java.io.Serializable;
import java.util.LinkedList;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class LeaveApplicationServiceImpl
implements ILeaveApplicationService {
    @Resource
    LeaveApplicationMapper leaveApplicationMapper;
    @Resource
    AdminMapper adminMapper;

    public PageResult<LeaveListedVo> list(PageValidate pageValidate, LeaveSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<LeaveApplication> queryWrapper = new QueryWrapper<LeaveApplication>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("applicant_type", (Object)1);
        if (StringUtils.isNotNull(searchValidate.getStatus()) && searchValidate.getStatus() != -1) {
            queryWrapper.eq("status", (Object)searchValidate.getStatus());
        }
        if (StringUtils.isNotNull(searchValidate.getClassId())) {
            queryWrapper.eq("class_id", (Object)searchValidate.getClassId());
        }
        if (StringUtils.isNotEmpty((String)searchValidate.getApplicantName())) {
            queryWrapper.like("applicant_name", (Object)searchValidate.getApplicantName());
        }
        IPage<LeaveApplication> iPage = this.leaveApplicationMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        LinkedList<LeaveListedVo> list = new LinkedList<LeaveListedVo>();
        for (LeaveApplication item : iPage.getRecords()) {
            LeaveListedVo vo = new LeaveListedVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            vo.setApplicantTypeDesc(item.getApplicantType() == 1 ? "\u5b66\u751f" : "\u6559\u7ec3");
            vo.setStatusDesc(this.getStatusDesc(item.getStatus()));
            vo.setStartTime(TimeUtils.timestampToDate((Long)item.getStartTime()));
            vo.setEndTime(TimeUtils.timestampToDate((Long)item.getEndTime()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)item.getCreateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public void audit(LeaveAuditValidate auditValidate, Integer adminId) {
        LeaveApplication leave = (LeaveApplication)this.leaveApplicationMapper.selectById((Serializable)auditValidate.getId());
        if (leave == null) {
            throw new OperateException("\u8bf7\u5047\u8bb0\u5f55\u4e0d\u5b58\u5728!");
        }
        if (leave.getStatus() != 0) {
            throw new OperateException("\u53ea\u80fd\u5bf9\u3010\u5f85\u5ba1\u6279\u3011\u72b6\u6001\u7684\u8bb0\u5f55\u8fdb\u884c\u5ba1\u6279!");
        }
        Admin admin = (Admin)this.adminMapper.selectById((Serializable)adminId);
        leave.setStatus(auditValidate.getStatus());
        leave.setAuditId(adminId);
        leave.setAuditName(admin != null ? admin.getName() : "\u7ba1\u7406\u5458");
        leave.setAuditTime(TimeUtils.timestamp());
        leave.setAuditRemark(auditValidate.getAuditRemark());
        leave.setUpdateTime(TimeUtils.timestamp());
        this.leaveApplicationMapper.updateById(leave);
    }

    private String getStatusDesc(Integer status) {
        switch (status) {
            case 0: {
                return "\u5f85\u5ba1\u6279";
            }
            case 1: {
                return "\u5df2\u6279\u51c6";
            }
            case 2: {
                return "\u5df2\u62d2\u7edd";
            }
        }
        return "\u672a\u77e5";
    }
}

