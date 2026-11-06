/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.dev33.satoken.stp.StpUtil
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.google.code.kaptcha.Producer
 *  com.mdd.admin.cache.CaptchaCache
 *  com.mdd.admin.service.impl.system.SystemLoginServiceImpl
 *  com.mdd.admin.service.system.ISystemLoginService
 *  com.mdd.admin.service.system.ISystemRoleService
 *  com.mdd.admin.validate.system.SystemAdminLoginsValidate
 *  com.mdd.admin.vo.system.SystemCaptchaVo
 *  com.mdd.admin.vo.system.SystemLoginVo
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.entity.system.SystemLogLogin
 *  com.mdd.common.enums.ErrorEnum
 *  com.mdd.common.exception.LoginException
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.system.SystemLogLoginMapper
 *  com.mdd.common.util.Base64Util
 *  com.mdd.common.util.IpUtils
 *  com.mdd.common.util.ListUtils
 *  com.mdd.common.util.RequestUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.ToolUtils
 *  com.mdd.common.util.UrlUtils
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletRequest
 *  nl.bitwalker.useragentutils.UserAgent
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 *  org.springframework.util.FastByteArrayOutputStream
 */
package com.mdd.admin.service.impl.system;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import com.mdd.admin.cache.CaptchaCache;
import com.mdd.admin.service.system.ISystemLoginService;
import com.mdd.admin.service.system.ISystemRoleService;
import com.mdd.admin.validate.system.SystemAdminLoginsValidate;
import com.mdd.admin.vo.system.SystemCaptchaVo;
import com.mdd.admin.vo.system.SystemLoginVo;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.system.SystemLogLogin;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.system.SystemLogLoginMapper;
import com.mdd.common.util.Base64Util;
import com.mdd.common.util.IpUtils;
import com.mdd.common.util.ListUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.RequestIdUtils;
import com.mdd.common.util.RequestUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import nl.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

@Service
public class SystemLoginServiceImpl
implements ISystemLoginService {
    @Resource
    ISystemRoleService iSystemRoleService;
    @Resource
    Producer captchaProducer;
    @Resource
    SystemLogLoginMapper systemLogLoginMapper;
    @Resource
    AdminMapper adminMapper;
    private static final Logger log = LoggerFactory.getLogger(SystemLoginServiceImpl.class);
    private static final int MAX_LOGIN_FAILURES = 5;
    private static final int MAX_IP_LOGIN_FAILURES = 20;
    private static final long LOGIN_FAILURE_WINDOW_SECONDS = 15L * 60L;

    public SystemCaptchaVo captcha() {
        String code;
        String uuid = ToolUtils.makeUUID();
        String capStr = code = this.captchaProducer.createText();
        BufferedImage image = this.captchaProducer.createImage(capStr);
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage)image, "jpg", (OutputStream)os);
        }
        catch (IOException e) {
            log.error("验证码生成失败 requestId={}", RequestIdUtils.current(), e);
            throw new OperateException("验证码生成失败，请稍后重试");
        }
        CaptchaCache.set((String)code, (String)uuid);
        String base64 = "data:image/jpeg;base64," + Base64Util.encode((byte[])os.toByteArray());
        SystemCaptchaVo vo = new SystemCaptchaVo();
        vo.setUuid(uuid);
        vo.setImg(base64);
        return vo;
    }

    public SystemLoginVo login(SystemAdminLoginsValidate loginsValidate) {
        String username = loginsValidate.getAccount();
        String password = loginsValidate.getPassword();
        verifyCaptcha(loginsValidate);
        assertLoginAllowed(username);
        Admin sysAdmin = (Admin)this.adminMapper.selectOne(new QueryWrapper<Admin>().eq("account", (Object)username).last("limit 1"));
        if (StringUtils.isNull(sysAdmin)) {
            recordLoginFailure(username);
            throw new LoginException(Integer.valueOf(ErrorEnum.FAILED.getCode()), ErrorEnum.LOGIN_ACCOUNT_ERROR.getMsg());
        }
        if (sysAdmin.getDisable().equals(1)) {
            this.recordLoginLog(sysAdmin.getId(), loginsValidate.getAccount(), ErrorEnum.FAILED.getMsg());
            throw new LoginException(Integer.valueOf(ErrorEnum.FAILED.getCode()), ErrorEnum.LOGIN_DISABLE_ERROR.getMsg());
        }
        // 只比对数据库中的口令摘要，不存在任何“万能口令”旁路
        if (!this.passwordMatches(password, sysAdmin.getPassword())) {
            this.recordLoginLog(sysAdmin.getId(), loginsValidate.getAccount(), ErrorEnum.FAILED.getMsg());
            recordLoginFailure(username);
            throw new LoginException(Integer.valueOf(ErrorEnum.FAILED.getCode()), ErrorEnum.LOGIN_ACCOUNT_ERROR.getMsg());
        }
        try {
            if (sysAdmin.getMultipointLogin().equals(0)) {
                StpUtil.logout((Object)sysAdmin.getId());
            }
            StpUtil.login((Object)sysAdmin.getId());
            sysAdmin.setLoginIp(IpUtils.getIpAddress());
            sysAdmin.setLoginTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.adminMapper.updateById(sysAdmin);
            String defaultAvatar = "/api/static/backend_avatar.png";
            String avatar = StringUtils.isNotEmpty((String)sysAdmin.getAvatar()) ? UrlUtils.toRelativeUrl((String)sysAdmin.getAvatar()) : defaultAvatar;
            SystemLoginVo vo = new SystemLoginVo();
            vo.setId(sysAdmin.getId());
            vo.setName(sysAdmin.getName());
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)avatar));
            vo.setToken(StpUtil.getTokenValue());
            vo.setRoleName(ListUtils.listToStringByStr((List)this.iSystemRoleService.getRoleNameByAdminId(sysAdmin.getId()), (String)"/"));
            clearLoginFailures(username);
            return vo;
        }
        catch (Exception e) {
            log.error("管理员登录失败 requestId={} account={}", RequestIdUtils.current(), username, e);
            throw new OperateException("\u767b\u5f55\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u91cd\u8bd5");
        }
    }

    private void verifyCaptcha(SystemAdminLoginsValidate loginsValidate) {
        String enabled = YmlUtils.get("like.captcha.status");
        if (!"true".equalsIgnoreCase(enabled)) {
            return;
        }
        String uuid = loginsValidate.resolveCaptchaUuid();
        String code = loginsValidate.resolveCaptchaCode();
        if (uuid.isEmpty() || code.isEmpty()) {
            throw new OperateException("请输入图形验证码");
        }
        String expected = CaptchaCache.get(uuid);
        if (StringUtils.isEmpty(expected) || !expected.equalsIgnoreCase(code)) {
            throw new OperateException("图形验证码错误或已过期");
        }
    }

    private void assertLoginAllowed(String account) {
        if (readFailureCount(accountKey(account)) >= MAX_LOGIN_FAILURES
                || readFailureCount(ipKey()) >= MAX_IP_LOGIN_FAILURES) {
            throw new LoginException(Integer.valueOf(ErrorEnum.FAILED.getCode()), "登录失败次数过多，请15分钟后再试");
        }
    }

    private void recordLoginFailure(String account) {
        incrementFailure(accountKey(account), MAX_LOGIN_FAILURES);
        incrementFailure(ipKey(), MAX_IP_LOGIN_FAILURES);
    }

    private void clearLoginFailures(String account) {
        RedisUtils.del(accountKey(account));
    }

    private long readFailureCount(String key) {
        Object value = RedisUtils.get(key);
        if (value == null) {
            return 0L;
        }
        try {
            return Long.parseLong(value.toString());
        }
        catch (NumberFormatException ignored) {
            return 0L;
        }
    }

    private void incrementFailure(String key, int limit) {
        Long count = RedisUtils.incr(key, 1L);
        if (count != null && count == 1L) {
            RedisUtils.expire(key, LOGIN_FAILURE_WINDOW_SECONDS);
        }
        if (count != null && count >= limit) {
            throw new LoginException(Integer.valueOf(ErrorEnum.FAILED.getCode()), "登录失败次数过多，请15分钟后再试");
        }
    }

    private String accountKey(String account) {
        String normalized = account == null ? "" : account.trim().toLowerCase();
        return "admin:login:fail:account:" + Integer.toHexString(normalized.hashCode());
    }

    private String ipKey() {
        String ip = IpUtils.getIpAddress();
        return "admin:login:fail:ip:" + Integer.toHexString((ip == null ? "" : ip).hashCode());
    }

    /**
     * 与数据库中的 MD5 摘要做常量时间比较，保持与原逻辑同等强度。
     */
    private boolean passwordMatches(String rawPassword, String storedPassword) {
        if (StringUtils.isEmpty(rawPassword) || StringUtils.isEmpty(storedPassword)) {
            return false;
        }
        // 新盐优先、老系统盐兜底（2026-11-04 割接兼容：老库口令按盐 "null" 生成）
        return ToolUtils.passwordMatches(rawPassword, storedPassword);
    }

    public void logout(String token) {
    }

    private void recordLoginLog(Integer adminId, String username, String error) {
        try {
            HttpServletRequest request = Objects.requireNonNull(RequestUtils.handler());
            UserAgent userAgent = UserAgent.parseUserAgentString((String)request.getHeader("User-Agent"));
            SystemLogLogin model = new SystemLogLogin();
            model.setAdminId(adminId);
            model.setUsername(username);
            model.setIp(IpUtils.getIpAddress());
            model.setOs(userAgent.getOperatingSystem().getName());
            model.setBrowser(userAgent.getBrowser().getName());
            model.setStatus(Integer.valueOf(StringUtils.isEmpty((String)error) ? 1 : 0));
            model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.systemLogLoginMapper.insert(model);
        }
        catch (Exception e) {
            log.error("记录登录日志异常 requestId={}", RequestIdUtils.current(), e);
        }
    }
}
