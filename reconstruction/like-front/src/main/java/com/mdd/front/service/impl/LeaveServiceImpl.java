/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.LeaveApplication
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.LeaveApplicationMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.student.StudentMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.util.MessageUtil
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.service.ILeaveService
 *  com.mdd.front.service.impl.LeaveServiceImpl
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.validate.leave.LeaveApplyValidate
 *  com.mdd.front.validate.leave.LeaveAuditValidate
 *  com.mdd.front.vo.leave.LeaveListVo
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.LeaveApplication;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.LeaveApplicationMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.MessageUtil;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.ILeaveService;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.validate.leave.LeaveApplyValidate;
import com.mdd.front.validate.leave.LeaveAuditValidate;
import com.mdd.front.vo.leave.LeaveListVo;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaveServiceImpl
implements ILeaveService {
    @Resource
    LeaveApplicationMapper leaveApplicationMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    StudentMapper studentMapper;

    public void apply(LeaveApplyValidate validate, Integer userId, boolean isInstructor) {
        LeaveApplication leave = new LeaveApplication();
        if (isInstructor) {
            Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("user_id", (Object)userId));
            if (instructor == null) {
                throw new OperateException("\u672a\u627e\u5230\u5bf9\u5e94\u7684\u6559\u7ec3\u4fe1\u606f!");
            }
            leave.setInstructorId(instructor.getId());
            leave.setApplicantType(Integer.valueOf(2));
            leave.setApplicantName(instructor.getName());
        } else {
            User user = (User)this.userMapper.selectById((Serializable)userId);
            if (user == null) {
                throw new OperateException("\u672a\u627e\u5230\u7528\u6237\u4fe1\u606f!");
            }
            leave.setUserId(userId);
            leave.setApplicantType(Integer.valueOf(1));
            leave.setApplicantName(StringUtils.isNotEmpty((String)user.getRealName()) ? user.getRealName() : user.getNickname());
        }
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)validate.getClassId());
        if (courseClass != null) {
            leave.setClassId(courseClass.getId());
            leave.setClassName(courseClass.getName());
        } else {
            leave.setClassId(Integer.valueOf(0));
            leave.setClassName("\u901a\u7528\u8bf7\u5047/\u65e0\u73ed\u7ea7");
        }
        leave.setStartTime(validate.getStartTime());
        leave.setEndTime(validate.getEndTime());
        leave.setReason(validate.getReason());
        leave.setDetail(validate.getDetail());
        if (validate.getStudentId() != null) {
            leave.setStudentId(validate.getStudentId());
        }
        leave.setStatus(Integer.valueOf(0));
        leave.setCreateTime(TimeUtils.timestamp());
        leave.setUpdateTime(TimeUtils.timestamp());
        this.leaveApplicationMapper.insert(leave);
    }

    public PageResult<LeaveListVo> myLeaves(PageValidate pageValidate, Integer userId, Integer status) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<LeaveApplication> queryWrapper = new QueryWrapper<LeaveApplication>();
        queryWrapper.eq("user_id", (Object)userId).eq("applicant_type", (Object)1).orderByDesc("id");
        if (status != null && status != -1) {
            queryWrapper.eq("status", (Object)status);
        }
        IPage<LeaveApplication> iPage = this.leaveApplicationMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        List<LeaveListVo> list = this.formatLeaveList(iPage.getRecords());
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), (List)list);
    }

    public PageResult<LeaveListVo> classLeaves(PageValidate pageValidate, Integer instructorUserId, Integer classId, Integer status) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        Instructor instructor = this.requireInstructor(instructorUserId);
        QueryWrapper<LeaveApplication> queryWrapper = new QueryWrapper<LeaveApplication>();
        queryWrapper.eq("applicant_type", (Object)1).orderByDesc("id");
        if (classId != null && classId > 0) {
            CourseClass courseClass = this.courseClassMapper.selectOne(new QueryWrapper<CourseClass>()
                    .eq("id", (Object)classId).eq("is_delete", (Object)0).last("limit 1"));
            if (courseClass == null || !instructor.getId().equals(courseClass.getInstructorId())) {
                throw new OperateException("无权查看该班级请假记录");
            }
            queryWrapper.eq("class_id", (Object)classId);
        } else {
            List<CourseClass> myClasses = this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().eq("instructor_id", (Object)instructor.getId()).eq("is_delete", (Object)0));
            if (myClasses.isEmpty()) {
                return PageResult.iPageHandle((Long)0L, Long.valueOf(page), Long.valueOf(limit), new LinkedList());
            }
            LinkedList<Integer> classIds = new LinkedList<Integer>();
            for (CourseClass c : myClasses) {
                classIds.add(c.getId());
            }
            queryWrapper.in("class_id", classIds);
        }
        if (status != null && status != -1) {
            queryWrapper.eq("status", (Object)status);
        }
        IPage<LeaveApplication> iPage = this.leaveApplicationMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        List<LeaveListVo> list = this.formatLeaveList(iPage.getRecords());
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), (List)list);
    }

    @Transactional(rollbackFor={Exception.class})
    public void audit(LeaveAuditValidate auditValidate, Integer instructorUserId) {
        if (auditValidate.getStatus() == null || (auditValidate.getStatus() != 1 && auditValidate.getStatus() != 2)) {
            throw new OperateException("审批状态不合法");
        }
        String[] idArray;
        String idsStr = auditValidate.getIds();
        if (StringUtils.isEmpty((String)idsStr)) {
            if (auditValidate.getId() != null) {
                idsStr = auditValidate.getId().toString();
            } else {
                throw new OperateException("\u8bb0\u5f55ID\u4e0d\u80fd\u4e3a\u7a7a!");
            }
        }
        if ((idArray = idsStr.split(",")).length == 0) {
            throw new OperateException("\u8bb0\u5f55ID\u4e0d\u80fd\u4e3a\u7a7a!");
        }
        Instructor instructor = this.requireInstructor(instructorUserId);
        for (String idString : idArray) {
            Integer id;
            LeaveApplication leave;
            if (StringUtils.isEmpty((String)idString)) {
                throw new OperateException("请假记录ID不能为空");
            }
            try {
                id = Integer.valueOf(Integer.parseInt(idString.trim()));
            } catch (NumberFormatException e) {
                throw new OperateException("请假记录ID格式错误");
            }
            leave = (LeaveApplication)this.leaveApplicationMapper.selectById((Serializable)id);
            if (leave == null) {
                throw new OperateException("请假记录不存在");
            }
            CourseClass courseClass = leave.getClassId() == null ? null
                    : (CourseClass)this.courseClassMapper.selectById((Serializable)leave.getClassId());
            if (courseClass == null || !instructor.getId().equals(courseClass.getInstructorId())) {
                throw new OperateException("无权审批该班级请假申请");
            }
            if (leave.getStatus() == null || leave.getStatus() != 0) {
                throw new OperateException("请假记录已处理，请刷新后重试");
            }
            int updated = this.leaveApplicationMapper.update(null, new UpdateWrapper<LeaveApplication>()
                    .eq("id", (Object)id)
                    .eq("status", (Object)0)
                    .set("status", (Object)auditValidate.getStatus())
                    .set("audit_id", (Object)instructor.getId())
                    .set("audit_name", instructor.getName() != null ? instructor.getName() : "教练审核")
                    .set("audit_time", TimeUtils.timestamp())
                    .set("audit_remark", auditValidate.getAuditRemark())
                    .set("update_time", TimeUtils.timestamp()));
            if (updated <= 0) {
                throw new OperateException("请假记录已处理，请刷新后重试");
            }
            if (leave.getUserId() == null) continue;
            MessageUtil.sendLeaveAuditNotice((Integer)leave.getUserId(), (String)"student", (String)instructor.getName(), (auditValidate.getStatus() == 1 ? 1 : 0) != 0, (String)auditValidate.getAuditRemark());
        }
    }

    private Instructor requireInstructor(Integer userId) {
        Instructor instructor = this.instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("user_id", (Object)userId).last("limit 1"));
        if (instructor == null || instructor.getDeleteTime() != null) {
            throw new OperateException("当前账号不是有效教练");
        }
        return instructor;
    }

    private List<LeaveListVo> formatLeaveList(List<LeaveApplication> records) {
        LinkedList<LeaveListVo> list = new LinkedList<LeaveListVo>();
        for (LeaveApplication item : records) {
            LeaveListVo vo = new LeaveListVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            vo.setStatusDesc(this.getStatusDesc(item.getStatus()));
            vo.setStartTime(TimeUtils.timestampToDate((Long)item.getStartTime()));
            vo.setEndTime(TimeUtils.timestampToDate((Long)item.getEndTime()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)item.getCreateTime()));
            vo.setClassId(item.getClassId());
            vo.setUserId(item.getUserId());
            if (item.getUserId() != null && item.getClassId() != null && item.getClassId() > 0) {
                UserCourse uc = (UserCourse)this.userCourseMapper.selectOne(new QueryWrapper<UserCourse>().eq("user_id", (Object)item.getUserId()).eq("course_class_id", (Object)item.getClassId()).isNull("delete_time").last("limit 1"));
                vo.setRemainLessons(Integer.valueOf(uc != null && uc.getRemainLessons() != null ? uc.getRemainLessons() : 0));
            } else {
                vo.setRemainLessons(Integer.valueOf(0));
            }
            if (item.getUserId() != null) {
                User user = (User)this.userMapper.selectById((Serializable)item.getUserId());
                vo.setAvatar(user != null ? UrlUtils.toAbsoluteUrl((String)user.getAvatar()) : "");
            }
            if (item.getStudentId() != null && item.getStudentId() > 0) {
                Student student = (Student)this.studentMapper.selectById((Serializable)item.getStudentId());
                vo.setStudentId(item.getStudentId());
                vo.setStudentName(student != null ? student.getName() : "");
            }
            list.add(vo);
        }
        return list;
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
        return "\u65e0";
    }
}
