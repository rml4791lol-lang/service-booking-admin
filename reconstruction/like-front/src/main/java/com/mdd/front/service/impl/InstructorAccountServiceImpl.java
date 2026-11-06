package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.instructor.InstructorAccountAudit;
import com.mdd.common.entity.user.User;
import com.mdd.common.entity.user.UserSession;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.instructor.InstructorAccountAuditMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.mapper.user.UserSessionMapper;
import com.mdd.common.util.PasswordUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IInstructorAccountService;
import com.mdd.front.service.ILoginService;
import com.mdd.front.validate.instructor.InstructorPasswordChangeValidate;
import com.mdd.front.validate.login.LoginPwdValidate;
import com.mdd.front.vo.login.LoginTokenVo;
import com.mdd.front.vo.user.InstructorAccountInfoVo;
import java.io.Serializable;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstructorAccountServiceImpl implements IInstructorAccountService {
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    UserSessionMapper userSessionMapper;
    @Resource
    InstructorAccountAuditMapper auditMapper;
    @Resource
    ILoginService loginService;

    @Override
    public InstructorAccountInfoVo info(Integer instructorId) {
        Instructor instructor = requireInstructor(instructorId);
        InstructorAccountInfoVo vo = new InstructorAccountInfoVo();
        vo.setInstructorId(instructor.getId());
        vo.setName(instructor.getName());
        vo.setPhone(ToolUtils.maskPhone(instructor.getPhone(), false)); // #182 收编公共打码（展示脱敏口径）
        vo.setStatus(instructor.getStatus());
        vo.setStatusLabel(statusLabel(instructor.getStatus()));
        vo.setMustChangePassword(instructor.getMustChangePassword() != null && instructor.getMustChangePassword() == 1);
        vo.setLastLoginTime(instructor.getLastLoginTime() == null ? ""
                : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(instructor.getLastLoginTime()));
        return vo;
    }

    @Override
    @Transactional
    public LoginTokenVo changePassword(Integer instructorId, InstructorPasswordChangeValidate validate, String ip) {
        Instructor instructor = requireInstructor(instructorId);
        if (instructor.getStatus() != null && instructor.getStatus() == 0) {
            throw new OperateException("教练账号已停用");
        }
        if (instructor.getStatus() != null && instructor.getStatus() == 2) {
            throw new OperateException("资质尚未审核通过，暂不可修改密码");
        }
        if (instructor.getUserId() == null || instructor.getUserId() <= 0) {
            throw new OperateException("教练账号未开通，请联系管理员");
        }
        User user = userMapper.selectById((Serializable) instructor.getUserId());
        if (user == null || user.getDeleteTime() != null) {
            throw new OperateException("教练登录账号不存在，请联系管理员");
        }
        if (user.getIsDisable() != null && user.getIsDisable() == 1) {
            throw new OperateException("登录账号已被禁用");
        }

        String oldPassword = validate.getOldPassword();
        String newPassword = validate.getNewPassword() == null ? "" : validate.getNewPassword().trim();
        if (!passwordMatches(oldPassword, instructor.getPassword())) {
            throw new OperateException("当前密码不正确");
        }
        if (!newPassword.equals(validate.getConfirmPassword())) {
            throw new OperateException("两次输入的新密码不一致");
        }
        if (passwordMatches(newPassword, instructor.getPassword())) {
            throw new OperateException("新密码不能与当前密码相同");
        }
        if (StringUtils.isNotEmpty(instructor.getPhone()) && instructor.getPhone().equals(newPassword)) {
            throw new OperateException("新密码不能与手机号相同");
        }
        PasswordUtils.validateAdminPassword(newPassword);

        Date now = new Date();
        instructor.setPassword(ToolUtils.makePassword(newPassword));
        instructor.setMustChangePassword(0);
        instructor.setPasswordUpdatedTime(now);
        instructor.setUpdateTime(now);
        instructorMapper.updateById(instructor);

        revokeSessions(user.getId());

        LoginPwdValidate loginValidate = new LoginPwdValidate();
        loginValidate.setAccount(instructor.getPhone());
        loginValidate.setPassword(newPassword);
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        loginValidate.setTerminal(terminal == null || terminal <= 0 ? 1 : terminal);
        LoginTokenVo tokenVo = loginService.instructorPasswordLogin(loginValidate);
        tokenVo.setMustChangePassword(Boolean.FALSE);

        recordAudit(instructor, "CHANGE_PASSWORD", "COACH", instructor.getId(),
                instructor.getName(), ip, "教练自助修改登录密码，旧会话已失效", 1);
        return tokenVo;
    }

    private Instructor requireInstructor(Integer instructorId) {
        if (instructorId == null || instructorId <= 0) {
            throw new OperateException("请先登录教练账号");
        }
        Instructor instructor = instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("id", instructorId).isNull("delete_time").last("limit 1"));
        if (instructor == null) {
            throw new OperateException("教练账号不存在");
        }
        return instructor;
    }

    private boolean passwordMatches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        // 新盐优先、老系统盐兜底（2026-11-04 割接兼容：老库口令按盐 "null" 生成）
        return ToolUtils.passwordMatches(rawPassword, encodedPassword);
    }

    private void revokeSessions(Integer userId) {
        if (userId == null || userId <= 0) {
            return;
        }
        List<UserSession> sessions = userSessionMapper.selectList(
                new QueryWrapper<UserSession>().eq("user_id", userId));
        for (UserSession session : sessions) {
            if (StringUtils.isNotEmpty(session.getToken()) && session.getTerminal() != null) {
                RedisUtils.del("login:front:token:" + session.getTerminal() + ":" + session.getToken());
            }
        }
        userSessionMapper.delete(new QueryWrapper<UserSession>().eq("user_id", userId));
    }

    private void recordAudit(Instructor instructor, String action, String source, Integer operatorId,
                             String operatorName, String ip, String detail, int result) {
        InstructorAccountAudit audit = new InstructorAccountAudit();
        audit.setInstructorId(instructor.getId());
        audit.setUserId(instructor.getUserId() == null ? 0 : instructor.getUserId());
        audit.setAction(action);
        audit.setSource(source);
        audit.setOperatorId(operatorId == null ? 0 : operatorId);
        audit.setOperatorName(operatorName == null ? "" : operatorName);
        audit.setIp(ip == null ? "" : ip);
        audit.setDetail(detail == null ? "" : (detail.length() > 500 ? detail.substring(0, 500) : detail));
        audit.setResult(result);
        audit.setCreateTime(new Date());
        auditMapper.insert(audit);
    }

    private String statusLabel(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1:
                return "正常";
            case 2:
                return "待审核";
            default:
                return "已停用";
        }
    }

}
