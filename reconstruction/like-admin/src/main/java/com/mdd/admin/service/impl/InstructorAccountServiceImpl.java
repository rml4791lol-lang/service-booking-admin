package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IInstructorAccountService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.instructor.InstructorAccountAuditVo;
import com.mdd.admin.vo.instructor.InstructorAccountVo;
import com.mdd.common.core.PageResult;
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
import com.mdd.common.util.YmlUtils;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 教练账号生命周期（#31）。
 *
 * <p>设计约定：
 * <ul>
 *   <li>教练资料 {@code la_instructor} 与登录账号 {@code la_user} 一对一，通过
 *       {@code la_instructor.user_id} 绑定；手机号用于登录。</li>
 *   <li>教练端口令摘要保存在 {@code la_instructor.password}，与家长账号口令互不覆盖。</li>
 *   <li>初始口令只在开通/重置响应中返回一次，绝不写日志、审计或数据库明文。</li>
 * </ul>
 */
@Service
public class InstructorAccountServiceImpl implements IInstructorAccountService {

    private static final String ACTION_OPEN = "OPEN";
    private static final String ACTION_RESET = "RESET";
    private static final String ACTION_DISABLE = "DISABLE";
    private static final String ACTION_ENABLE = "ENABLE";
    private static final String ACTION_CHANGE_PASSWORD = "CHANGE_PASSWORD";
    private static final String SOURCE_ADMIN = "ADMIN";

    private static final char[] UPPER = "ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWER = "abcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final char[] DIGIT = "23456789".toCharArray();
    private static final char[] SYMBOL = "!@#$%^&*".toCharArray();
    private static final char[] ALL = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%^&*".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    @Resource
    InstructorMapper instructorMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    UserSessionMapper userSessionMapper;
    @Resource
    InstructorAccountAuditMapper auditMapper;

    @Override
    public InstructorAccountVo status(Integer instructorId) {
        Instructor instructor = requireInstructor(instructorId);
        return buildStatus(instructor);
    }

    @Override
    @Transactional
    public InstructorAccountCredential open(Integer instructorId, String initialPassword,
                                            Integer adminId, String adminName, String ip) {
        Instructor instructor = requireInstructor(instructorId);
        InstructorAccountVo before = buildStatus(instructor);
        if (Boolean.TRUE.equals(before.getHasPassword()) && Boolean.TRUE.equals(before.getAccountLinked())
                && !Boolean.TRUE.equals(before.getMustChangePassword())) {
            throw new OperateException("教练账号已开通且已设置密码，如需重置请使用重置密码");
        }
        if (StringUtils.isEmpty(instructor.getPhone())) {
            throw new OperateException("教练手机号为空，无法开通登录账号");
        }

        String rawPassword = resolveInitialPassword(initialPassword);
        User user = ensureAccount(instructor);
        Date now = new Date();

        instructor.setUserId(user.getId());
        instructor.setPassword(ToolUtils.makePassword(rawPassword));
        instructor.setMustChangePassword(1);
        if (instructor.getAccountOpenedTime() == null) {
            instructor.setAccountOpenedTime(now);
        }
        instructor.setPasswordUpdatedTime(now);
        instructor.setUpdateTime(now);
        instructorMapper.updateById(instructor);

        recordAudit(instructor, ACTION_OPEN, SOURCE_ADMIN, adminId, adminName, ip,
                "开通教练账号，初始口令由后台一次性发给教练，强制首次登录改密", 1);

        return new InstructorAccountCredential(rawPassword, buildStatus(instructor));
    }

    @Override
    @Transactional
    public InstructorAccountCredential reset(Integer instructorId, String initialPassword,
                                             Integer adminId, String adminName, String ip) {
        Instructor instructor = requireInstructor(instructorId);
        InstructorAccountVo before = buildStatus(instructor);
        if (!Boolean.TRUE.equals(before.getAccountLinked()) || !Boolean.TRUE.equals(before.getHasPassword())) {
            throw new OperateException("账号未开通，请先开通账号");
        }

        String rawPassword = resolveInitialPassword(initialPassword);
        Date now = new Date();
        instructor.setPassword(ToolUtils.makePassword(rawPassword));
        instructor.setMustChangePassword(1);
        instructor.setPasswordUpdatedTime(now);
        instructor.setUpdateTime(now);
        instructorMapper.updateById(instructor);

        revokeSessions(instructor.getUserId());
        recordAudit(instructor, ACTION_RESET, SOURCE_ADMIN, adminId, adminName, ip,
                "重置教练登录密码，旧会话已失效，强制下次登录改密", 1);

        return new InstructorAccountCredential(rawPassword, buildStatus(instructor));
    }

    @Override
    @Transactional
    public void disable(Integer instructorId, String reason, Integer adminId, String adminName, String ip) {
        Instructor instructor = requireInstructor(instructorId);
        Date now = new Date();
        instructor.setStatus(0);
        instructor.setUpdateTime(now);
        instructorMapper.updateById(instructor);

        if (instructor.getUserId() != null && instructor.getUserId() > 0) {
            User user = userMapper.selectById(instructor.getUserId());
            if (user != null) {
                user.setIsDisable(1);
                user.setUpdateTime(System.currentTimeMillis() / 1000L);
                userMapper.updateById(user);
                revokeSessions(user.getId());
            }
        }

        String detail = "停用教练登录账号，已清除在线会话";
        if (StringUtils.isNotEmpty(reason)) {
            detail = detail + "；原因：" + reason.trim();
        }
        recordAudit(instructor, ACTION_DISABLE, SOURCE_ADMIN, adminId, adminName, ip, detail, 1);
    }

    @Override
    @Transactional
    public void enable(Integer instructorId, Integer adminId, String adminName, String ip) {
        Instructor instructor = requireInstructor(instructorId);
        if (instructor.getStatus() != null && instructor.getStatus() == 2) {
            throw new OperateException("资质待审核，请先在教练编辑页调整资质状态");
        }
        Date now = new Date();
        instructor.setStatus(1);
        instructor.setUpdateTime(now);
        instructorMapper.updateById(instructor);

        if (instructor.getUserId() != null && instructor.getUserId() > 0) {
            User user = userMapper.selectById(instructor.getUserId());
            if (user != null) {
                user.setIsDisable(0);
                user.setUpdateTime(System.currentTimeMillis() / 1000L);
                userMapper.updateById(user);
            }
        }

        recordAudit(instructor, ACTION_ENABLE, SOURCE_ADMIN, adminId, adminName, ip,
                "启用教练登录账号", 1);
    }

    @Override
    public PageResult<InstructorAccountAuditVo> audits(Integer instructorId, String action, PageValidate pageValidate) {
        requireInstructor(instructorId);
        QueryWrapper<InstructorAccountAudit> queryWrapper = new QueryWrapper<InstructorAccountAudit>();
        queryWrapper.eq("instructor_id", instructorId);
        if (StringUtils.isNotEmpty(action)) {
            queryWrapper.eq("action", action.trim());
        }
        queryWrapper.orderByDesc("id");
        IPage<InstructorAccountAudit> page = auditMapper.selectPage(
                new Page<InstructorAccountAudit>(pageValidate.getPage_no(), pageValidate.getPage_size()),
                queryWrapper);
        List<InstructorAccountAuditVo> list = new ArrayList<InstructorAccountAuditVo>();
        for (InstructorAccountAudit item : page.getRecords()) {
            list.add(toAuditVo(item));
        }
        return PageResult.iPageHandle(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    private Instructor requireInstructor(Integer instructorId) {
        Instructor instructor = instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("id", instructorId)
                .isNull("delete_time")
                .last("limit 1"));
        if (instructor == null) {
            throw new OperateException("教练不存在或已删除");
        }
        return instructor;
    }

    private InstructorAccountVo buildStatus(Instructor instructor) {
        InstructorAccountVo vo = new InstructorAccountVo();
        vo.setInstructorId(instructor.getId());
        vo.setName(instructor.getName());
        vo.setPhone(instructor.getPhone());
        vo.setInstructorStatus(instructor.getStatus());
        vo.setInstructorStatusLabel(instructorStatusLabel(instructor.getStatus()));

        User user = null;
        if (instructor.getUserId() != null && instructor.getUserId() > 0) {
            user = userMapper.selectById(instructor.getUserId());
            if (user != null && user.getDeleteTime() != null) {
                user = null;
            }
        }
        boolean linked = user != null;
        boolean hasPassword = StringUtils.isNotEmpty(instructor.getPassword());
        boolean mustChange = instructor.getMustChangePassword() != null && instructor.getMustChangePassword() == 1;
        boolean userDisabled = user != null && user.getIsDisable() != null && user.getIsDisable() == 1;

        vo.setAccountLinked(linked);
        vo.setAccountExists(linked);
        vo.setUserId(linked ? user.getId() : null);
        vo.setAccount(linked ? (StringUtils.isNotEmpty(user.getAccount()) ? user.getAccount() : user.getMobile()) : "");
        vo.setHasPassword(hasPassword);
        vo.setMustChangePassword(mustChange);
        vo.setUserDisabled(userDisabled);
        vo.setAccountOpenedTime(format(instructor.getAccountOpenedTime()));
        vo.setPasswordUpdatedTime(format(instructor.getPasswordUpdatedTime()));
        vo.setLastLoginTime(format(instructor.getLastLoginTime()));

        boolean statusDisabled = instructor.getStatus() != null && instructor.getStatus() == 0;
        boolean statusPending = instructor.getStatus() != null && instructor.getStatus() == 2;

        String accountStatus;
        String accountStatusLabel;
        if (statusDisabled) {
            accountStatus = "DISABLED";
            accountStatusLabel = "已停用";
        } else if (!linked || !hasPassword) {
            accountStatus = "UNOPENED";
            accountStatusLabel = "未开通";
        } else if (userDisabled) {
            accountStatus = "BLOCKED";
            accountStatusLabel = "账号禁用";
        } else if (mustChange) {
            accountStatus = "PENDING_CHANGE";
            accountStatusLabel = "首登待改密";
        } else if (statusPending) {
            accountStatus = "PENDING_REVIEW";
            accountStatusLabel = "资质待审核";
        } else {
            accountStatus = "ACTIVE";
            accountStatusLabel = "正常";
        }
        vo.setAccountStatus(accountStatus);
        vo.setAccountStatusLabel(accountStatusLabel);

        boolean loginAllowed = true;
        String blockReason = "";
        if (statusDisabled) {
            loginAllowed = false;
            blockReason = "账号已停用";
        } else if (statusPending) {
            loginAllowed = false;
            blockReason = "资质待审核，暂不可登录";
        } else if (!linked) {
            loginAllowed = false;
            blockReason = "账号未开通";
        } else if (!hasPassword) {
            loginAllowed = false;
            blockReason = "未设置登录密码";
        } else if (userDisabled) {
            loginAllowed = false;
            blockReason = "登录账号已被禁用";
        }
        vo.setLoginAllowed(loginAllowed);
        vo.setLoginBlockReason(blockReason);
        return vo;
    }

    private User ensureAccount(Instructor instructor) {
        User user = null;
        if (instructor.getUserId() != null && instructor.getUserId() > 0) {
            user = userMapper.selectById(instructor.getUserId());
            if (user != null && user.getDeleteTime() != null) {
                user = null;
            }
        }
        if (user != null) {
            return user;
        }

        String phone = instructor.getPhone().trim();
        user = userMapper.selectOne(new QueryWrapper<User>()
                .nested(wq -> wq.eq("mobile", phone).or().eq("account", phone))
                .isNull("delete_time")
                .last("limit 1"));
        if (user != null) {
            return user;
        }

        User model = new User();
        model.setSn(generateSn());
        model.setAvatar(StringUtils.isNotEmpty(instructor.getAvatar()) ? instructor.getAvatar() : "");
        model.setRealName(StringUtils.isNotEmpty(instructor.getName()) ? instructor.getName() : "");
        model.setNickname(StringUtils.isNotEmpty(instructor.getName()) ? instructor.getName() : "教练" + tail(phone, 4));
        model.setAccount(uniqueAccount(phone));
        model.setMobile(phone);
        model.setSex(instructor.getGender() == null ? 0 : instructor.getGender());
        model.setChannel(0);
        model.setIsDisable(0);
        model.setIsNewUser(0);
        model.setCreateTime(System.currentTimeMillis() / 1000L);
        model.setUpdateTime(System.currentTimeMillis() / 1000L);
        userMapper.insert(model);
        return model;
    }

    private String uniqueAccount(String phone) {
        long count = userMapper.selectCount(new QueryWrapper<User>().eq("account", phone));
        if (count == 0) {
            return phone;
        }
        String candidate = "coach" + phone;
        count = userMapper.selectCount(new QueryWrapper<User>().eq("account", candidate));
        if (count == 0) {
            return candidate;
        }
        return "coach" + phone + ToolUtils.randomInt(4);
    }

    private Integer generateSn() {
        for (int i = 0; i < 30; i++) {
            Integer sn = Integer.parseInt(ToolUtils.randomInt(8));
            long count = userMapper.selectCount(new QueryWrapper<User>().eq("sn", sn));
            if (count == 0) {
                return sn;
            }
        }
        throw new OperateException("生成用户编号失败，请重试");
    }

    private String resolveInitialPassword(String requested) {
        if (StringUtils.isNotEmpty(requested)) {
            String value = requested.trim();
            PasswordUtils.validateAdminPassword(value);
            return value;
        }
        String configured = YmlUtils.get("like.instructor.initial-password");
        if (StringUtils.isEmpty(configured) || configured.trim().isEmpty()) {
            configured = YmlUtils.get("LIKE_INSTRUCTOR_INITIAL_PASSWORD");
        }
        if (StringUtils.isNotEmpty(configured) && !configured.trim().isEmpty()) {
            String value = configured.trim();
            PasswordUtils.validateAdminPassword(value);
            return value;
        }
        return generatePassword();
    }

    private String generatePassword() {
        StringBuilder builder = new StringBuilder(12);
        builder.append(UPPER[RANDOM.nextInt(UPPER.length)]);
        builder.append(LOWER[RANDOM.nextInt(LOWER.length)]);
        builder.append(DIGIT[RANDOM.nextInt(DIGIT.length)]);
        builder.append(SYMBOL[RANDOM.nextInt(SYMBOL.length)]);
        for (int i = builder.length(); i < 12; i++) {
            builder.append(ALL[RANDOM.nextInt(ALL.length)]);
        }
        char[] chars = builder.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
        return new String(chars);
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

    private InstructorAccountAuditVo toAuditVo(InstructorAccountAudit item) {
        InstructorAccountAuditVo vo = new InstructorAccountAuditVo();
        vo.setId(item.getId());
        vo.setInstructorId(item.getInstructorId());
        vo.setUserId(item.getUserId());
        vo.setAction(item.getAction());
        vo.setActionLabel(actionLabel(item.getAction()));
        vo.setSource(item.getSource());
        vo.setSourceLabel(sourceLabel(item.getSource()));
        vo.setOperatorId(item.getOperatorId());
        vo.setOperatorName(item.getOperatorName());
        vo.setIp(item.getIp());
        vo.setDetail(item.getDetail());
        vo.setResult(item.getResult());
        vo.setCreateTime(format(item.getCreateTime()));
        return vo;
    }

    private String instructorStatusLabel(Integer status) {
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

    private String actionLabel(String action) {
        if (action == null) {
            return "";
        }
        switch (action) {
            case ACTION_OPEN:
                return "开通账号";
            case ACTION_RESET:
                return "重置密码";
            case ACTION_DISABLE:
                return "停用账号";
            case ACTION_ENABLE:
                return "启用账号";
            case ACTION_CHANGE_PASSWORD:
                return "修改密码";
            default:
                return action;
        }
    }

    private String sourceLabel(String source) {
        if ("ADMIN".equals(source)) {
            return "后台管理员";
        }
        if ("COACH".equals(source)) {
            return "教练自助";
        }
        if ("SYSTEM".equals(source)) {
            return "系统";
        }
        return source == null ? "" : source;
    }

    private String format(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    private String tail(String value, int length) {
        if (value == null) {
            return "";
        }
        return value.length() <= length ? value : value.substring(value.length() - length);
    }
}
