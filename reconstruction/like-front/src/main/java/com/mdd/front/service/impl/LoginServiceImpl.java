/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.binarywang.wx.miniapp.api.WxMaService
 *  cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.InstructorAuth
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.entity.user.UserAuth
 *  com.mdd.common.entity.user.UserSession
 *  com.mdd.common.enums.LoginEnum
 *  com.mdd.common.enums.NoticeEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorAuthMapper
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.user.UserAuthMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.mapper.user.UserSessionMapper
 *  com.mdd.common.plugin.notice.NoticeCheck
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.HttpUtils
 *  com.mdd.common.util.IpUtils
 *  com.mdd.common.util.MapUtils
 *  com.mdd.common.util.RedisUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.ToolUtils
 *  com.mdd.common.util.YmlUtils
 *  com.mdd.front.cache.ScanLoginCache
 *  com.mdd.front.cache.TokenLoginCache
 *  com.mdd.front.service.ILoginService
 *  com.mdd.front.service.impl.LoginServiceImpl
 *  com.mdd.front.validate.login.LoginPwdValidate
 *  com.mdd.front.vo.login.LoginTokenVo
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpSession
 *  me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken
 *  me.chanjar.weixin.common.error.WxErrorException
 *  me.chanjar.weixin.mp.api.WxMpService
 *  me.chanjar.weixin.mp.api.impl.WxMpOAuth2ServiceImpl
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.front.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.InstructorAuth;
import com.mdd.common.entity.user.User;
import com.mdd.common.entity.user.UserAuth;
import com.mdd.common.entity.user.UserSession;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.enums.LoginEnum;
import com.mdd.common.enums.NoticeEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorAuthMapper;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.user.UserAuthMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.mapper.user.UserSessionMapper;
import com.mdd.common.plugin.notice.NoticeCheck;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.HttpUtils;
import com.mdd.common.util.IpUtils;
import com.mdd.common.util.MapUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.YmlUtils;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.cache.LoginFailLimitCache;
import com.mdd.front.cache.ScanLoginCache;
import com.mdd.front.cache.TokenLoginCache;
import com.mdd.front.service.ILoginService;
import com.mdd.front.service.IWechatLoginService;
import com.mdd.front.validate.login.LoginPwdValidate;
import com.mdd.front.vo.login.LoginTokenVo;
import com.mdd.front.vo.login.WxSessionVo;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpOAuth2ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginServiceImpl
implements ILoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Resource
    UserMapper userMapper;
    @Resource
    UserAuthMapper userAuthMapper;
    @Resource
    UserSessionMapper userSessionMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    InstructorAuthMapper instructorAuthMapper;
    @Resource
    IWechatLoginService wechatLoginService;

    public void register(String account, String password, String userType, Integer terminal) {
        String pwd = ToolUtils.makePassword((String)password);
        if ("instructor".equals(userType)) {
            this.registerInstructor(account, pwd);
        } else {
            this.registerStudent(account, pwd, terminal);
        }
    }

    private void registerStudent(String account, String password, Integer terminal) {
        User model = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,sn,account"}).eq("account", (Object)account).isNull("delete_time").last("limit 1"));
        Assert.isNull(model, (String)"\u8d26\u53f7\u5df2\u5b58\u5728,\u6362\u4e00\u4e2a\u5427!", (Object[])new Object[0]);
        Integer sn = this.__generateSn();
        String defaultAvatar = ConfigUtils.get((String)"default_image", (String)"user_avatar", (String)"/api/static/default_avatar.png");
        User user = new User();
        user.setSn(sn);
        user.setNickname("\u7528\u6237" + sn);
        user.setAccount(account);
        user.setMobile(account);
        user.setPassword(password);
        user.setAvatar(defaultAvatar);
        user.setChannel(terminal);
        user.setIsNewUser(Integer.valueOf(1));
        user.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        user.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userMapper.insert(user);
    }

    private void registerInstructor(String phone, String password) {
        Instructor existInstructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("phone", (Object)phone).isNull("delete_time").last("limit 1"));
        Assert.isNull(existInstructor, (String)"\u8be5\u624b\u673a\u53f7\u5df2\u6ce8\u518c\u6559\u7ec3\u8d26\u53f7!", (Object[])new Object[0]);
        String defaultAvatar = ConfigUtils.get((String)"default_image", (String)"user_avatar", (String)"/api/static/default_avatar.png");
        Instructor instructor = new Instructor();
        instructor.setPhone(phone);
        instructor.setPassword(password);
        instructor.setName("\u6559\u7ec3" + phone.substring(phone.length() - 4));
        instructor.setAvatar(defaultAvatar);
        instructor.setGender(Integer.valueOf(1));
        instructor.setSpecialty("");
        instructor.setExperienceYears(Integer.valueOf(0));
        instructor.setClassCount(Integer.valueOf(0));
        instructor.setHourlyRate(new BigDecimal("0.00"));
        instructor.setRatingScore(new BigDecimal("0.00"));
        instructor.setStatus(Integer.valueOf(2));
        instructor.setSort(Integer.valueOf(0));
        instructor.setCreateTime(new Date());
        instructor.setUpdateTime(new Date());
        this.instructorMapper.insert(instructor);
    }

    @Transactional
    public LoginTokenVo accountLogin(LoginPwdValidate loginPwdValidate) {
        JSONArray config = JSONArray.parseArray((String)ConfigUtils.get((String)"login", (String)"login_way", (String)"[]"), (JSONReader.Feature[])new JSONReader.Feature[0]);
        if (!config.contains((Object)loginPwdValidate.getScene().toString())) {
            throw new OperateException("\u4e0d\u652f\u6301\u7684\u767b\u5f55\u65b9\u5f0f");
        }
        String account = loginPwdValidate.getAccount();
        String password = loginPwdValidate.getPassword();
        Integer terminal = loginPwdValidate.getTerminal();
        String code = loginPwdValidate.getCode();
        Integer sceneId = loginPwdValidate.getScene();
        if (loginPwdValidate.getScene().equals(LoginEnum.ACCOUNT_PASSWORD.getCode())) {
            User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().nested(wq -> wq.eq("account", (Object)account).or().eq("mobile", (Object)account).or()).isNull("delete_time").last("limit 1"));
            Assert.notNull((Object)user, (String)"\u8d26\u53f7\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
            // 新盐优先、老系统盐兜底（2026-11-04 割接兼容：老库口令按盐 "null" 生成）
            Assert.isFalse(!ToolUtils.passwordMatches((String)password, user.getPassword()), (String)"\u8d26\u53f7\u6216\u5bc6\u7801\u9519\u8bef!", (Object[])new Object[0]);
            Assert.isFalse((!user.getIsDisable().equals(0) ? 1 : 0) != 0, (String)"\u8d26\u53f7\u5df2\u88ab\u7981\u7528!", (Object[])new Object[0]);
            return this.__loginToken(user.getId(), user.getMobile(), user.getIsNewUser(), terminal);
        }
        return this.mobileLogin(account, code, terminal, sceneId);
    }

    @Transactional
    public LoginTokenVo instructorPasswordLogin(LoginPwdValidate loginPwdValidate) {
        String account = loginPwdValidate.getAccount() == null ? "" : loginPwdValidate.getAccount().trim();
        String password = loginPwdValidate.getPassword();
        Integer terminal = loginPwdValidate.getTerminal();
        // #157 防爆破：账号+IP 失败计数达阈值先拒绝，不再查库（同时挡住手机号枚举）
        LoginFailLimitCache.assertNotBlocked(account);
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("phone", (Object)account).isNull("delete_time").last("limit 1"));
        if (instructor == null) {
            // #162 诊断日志：账号不存在。响应仍走统一文案，不泄露账号是否存在
            log.warn("instructor login failed: instructorId={}, phoneMasked={}, reason={}, saltFp={}",
                    0L, LoginLogFields.maskPhone(account), LoginLogFields.REASON_NO_ACCOUNT, saltFingerprint());
            LoginFailLimitCache.recordFailure(account);
            throw new OperateException(LoginFailLimitCache.MSG_LOGIN_FAILED);
        }
        this.__assertInstructorLoginAllowed(instructor);
        User user = this.__requireInstructorAccount(instructor);
        Assert.isFalse((user.getIsDisable() != null && user.getIsDisable() == 1 ? 1 : 0) != 0,
                (String)"\u767b\u5f55\u8d26\u53f7\u5df2\u88ab\u7981\u7528!", (Object[])new Object[0]);
        if (StringUtils.isEmpty(instructor.getPassword())) {
            // #162 诊断日志：教练账号未开通（口令尚未生成）。文案保持既有口径不变
            log.warn("instructor login failed: instructorId={}, phoneMasked={}, reason={}, saltFp={}",
                    instructor.getId(), LoginLogFields.maskPhone(account), LoginLogFields.REASON_NOT_OPENED, saltFingerprint());
            throw new OperateException("\u6559\u7ec3\u8d26\u53f7\u672a\u5f00\u901a\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
        }
        boolean passwordMatches;
        try {
            passwordMatches = this.__passwordMatches(password, instructor.getPassword());
        } catch (IllegalStateException e) {
            // #162 缺盐兜底（#159 makePassword 拒绝拼 null）：异常继续上抛由全局处理器
            // 兜底成系统错误响应，日志只记盐指纹不落明文盐/明文口令
            log.error("instructor login failed: instructorId={}, phoneMasked={}, reason={}, saltFp={}",
                    instructor.getId(), LoginLogFields.maskPhone(account), LoginLogFields.REASON_CONFIG, saltFingerprint(), e);
            throw e;
        }
        if (!passwordMatches) {
            // #162 诊断日志：口令不符。与「账号不存在」同走统一文案（防枚举），只在日志区分
            log.warn("instructor login failed: instructorId={}, phoneMasked={}, reason={}, saltFp={}",
                    instructor.getId(), LoginLogFields.maskPhone(account), LoginLogFields.REASON_PASSWORD_MISMATCH, saltFingerprint());
            LoginFailLimitCache.recordFailure(account);
            throw new OperateException(LoginFailLimitCache.MSG_LOGIN_FAILED);
        }
        LoginFailLimitCache.clear(account);
        return this.__instructorLoginToken(instructor, terminal);
    }

    @Transactional
    public LoginTokenVo instructorMobileLogin(LoginPwdValidate loginPwdValidate) {
        String account = loginPwdValidate.getAccount() == null ? "" : loginPwdValidate.getAccount().trim();
        String code = loginPwdValidate.getCode();
        Integer terminal = loginPwdValidate.getTerminal();
        int sceneCode = NoticeEnum.LOGIN_CAPTCHA.getCode();
        if (!NoticeCheck.verify((Integer)sceneCode, (String)code, (String)account).booleanValue()) {
            throw new OperateException("\u9a8c\u8bc1\u7801\u9519\u8bef!");
        }
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("phone", (Object)account).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)instructor, (String)"\u6559\u7ec3\u8d26\u53f7\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        this.__assertInstructorLoginAllowed(instructor);
        User user = this.__requireInstructorAccount(instructor);
        Assert.isFalse((user.getIsDisable() != null && user.getIsDisable() == 1 ? 1 : 0) != 0,
                (String)"\u767b\u5f55\u8d26\u53f7\u5df2\u88ab\u7981\u7528!", (Object[])new Object[0]);
        return this.__instructorLoginToken(instructor, terminal);
    }

    /**
     * 教练登录统一使用 la_user.id 作为 token 主体（与家长一致），
     * 教练业务接口再由拦截器用 la_instructor.user_id 反查教练档案。
     */
    private LoginTokenVo __instructorLoginToken(Instructor instructor, Integer terminal) {
        Integer tokenUserId = instructor.getId();
        if (instructor.getUserId() != null && instructor.getUserId() > 0) {
            tokenUserId = instructor.getUserId();
        }
        String token = ToolUtils.makeToken();
        TokenLoginCache.set((String)token, (Object)tokenUserId, (String)String.valueOf(terminal));
        this.userSessionMapper.delete(new QueryWrapper<UserSession>().eq("user_id", (Object)tokenUserId).eq("terminal", (Object)terminal));
        UserSession userSession = new UserSession();
        userSession.setUserId(tokenUserId);
        userSession.setToken(token);
        userSession.setTerminal(terminal);
        userSession.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        userSession.setExpireTime(Long.valueOf(System.currentTimeMillis() / 1000L + this.__sessionTimeout()));
        this.userSessionMapper.insert(userSession);

        if (instructor.getUserId() != null && instructor.getUserId() > 0) {
            User loginUser = new User();
            loginUser.setId(tokenUserId);
            loginUser.setLoginIp(IpUtils.getHostIp());
            loginUser.setLoginTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.userMapper.updateById(loginUser);
        }

        Instructor loginInstructor = new Instructor();
        loginInstructor.setId(instructor.getId());
        loginInstructor.setLastLoginTime(new Date());
        this.instructorMapper.updateById(loginInstructor);

        boolean mustChange = instructor.getMustChangePassword() != null && instructor.getMustChangePassword() == 1;
        LoginTokenVo vo = new LoginTokenVo();
        vo.setId(instructor.getId());
        vo.setInstructorId(instructor.getId());
        vo.setName(instructor.getName());
        vo.setIsBindMobile(Boolean.valueOf(!StringUtils.isEmpty((String)instructor.getPhone())));
        vo.setToken(token);
        vo.setIsNew(Integer.valueOf(0));
        vo.setMobile(instructor.getPhone());
        vo.setMustChangePassword(Boolean.valueOf(mustChange));
        return vo;
    }

    private void __assertInstructorLoginAllowed(Instructor instructor) {
        if (instructor.getStatus() != null && instructor.getStatus() == 0) {
            throw new OperateException("\u6559\u7ec3\u8d26\u53f7\u5df2\u88ab\u505c\u7528!");
        }
        if (instructor.getStatus() != null && instructor.getStatus() == 2) {
            throw new OperateException("\u8d44\u8d28\u5c1a\u672a\u5ba1\u6838\u901a\u8fc7\uff0c\u6682\u4e0d\u53ef\u767b\u5f55");
        }
    }

    private User __requireInstructorAccount(Instructor instructor) {
        if (instructor.getUserId() == null || instructor.getUserId() <= 0) {
            throw new OperateException("\u6559\u7ec3\u8d26\u53f7\u672a\u5f00\u901a\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
        }
        User user = (User)this.userMapper.selectById(instructor.getUserId());
        if (user == null || user.getDeleteTime() != null) {
            throw new OperateException("\u6559\u7ec3\u767b\u5f55\u8d26\u53f7\u4e0d\u5b58\u5728\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
        }
        return user;
    }

    // ------------------------------------------------ #162 登录失败诊断日志
    // reason 取值/手机号打码/盐指纹等纯逻辑集中在 LoginLogFields（无重依赖，可离线断言）。
    // 日志固定字段：instructorId（查无账号为 0）、phoneMasked、reason、saltFp；
    // 占位符参数，禁止拼接字符串；禁止出现明文口令与明文盐。

    /** 盐配置键（#182：字面量唯一来源在 ToolUtils，与 makePassword 读键一致） */
    private static final String SALT_CONFIG_KEY = ToolUtils.SALT_CONFIG_KEY;

    /** 盐指纹：sha256(盐) 前 8 位；盐缺失返回 "none"。 */
    private static String saltFingerprint() {
        return LoginLogFields.saltFingerprintOf(YmlUtils.get(SALT_CONFIG_KEY));
    }

    private boolean __passwordMatches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        // 新盐优先、老系统盐兜底（2026-11-04 割接兼容：老库口令按盐 "null" 生成）
        return ToolUtils.passwordMatches(rawPassword, encodedPassword);
    }

    public LoginTokenVo mobileLogin(String mobile, String code, Integer terminal, Integer sceneId) {
        int sceneCode = NoticeEnum.LOGIN_CAPTCHA.getCode();
        if (!NoticeCheck.verify((Integer)sceneCode, (String)code, (String)mobile).booleanValue()) {
            throw new OperateException("\u9a8c\u8bc1\u7801\u9519\u8bef!");
        }
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,account,mobile,is_disable,is_new_user"}).eq("mobile", (Object)mobile).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)user, (String)"\u8d26\u53f7\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        Assert.isFalse((user.getIsDisable() != 0 ? 1 : 0) != 0, (String)"\u8d26\u53f7\u5df2\u7981\u7528!", (Object[])new Object[0]);
        return this.__loginToken(user.getId(), user.getMobile(), user.getIsNewUser(), terminal);
    }

    @Transactional
    public LoginTokenVo mnpLogin(String code, Integer terminal) {
        WxSessionVo session = this.wechatLoginService.code2Session(code);
        String unionId = StringUtils.isEmpty(session.getUnionid()) ? "0" : session.getUnionid();
        return this.__wxLoginHandle(session.getOpenid(), unionId, "", "", terminal);
    }

    @Transactional
    public LoginTokenVo instructorMnpLogin(String code, Integer terminal) {
        try {
            WxMaService wxMaService = WxMnpDriver.mnp();
            WxMaJscode2SessionResult sessionResult = wxMaService.getUserService().getSessionInfo(code);
            String openId = sessionResult.getOpenid();
            String uniId = sessionResult.getUnionid();
            String unionId = uniId == null ? "0" : uniId;
            return this.__wxInstructorLoginHandle(openId, unionId, terminal);
        }
        catch (WxErrorException e) {
            throw new OperateException(e.getError().getErrorCode() + ", " + e.getError().getErrorMsg());
        }
    }

    public LoginTokenVo officeLogin(String code, Integer terminal) {
        try {
            WxMpService wxMpService = WxMnpDriver.oa();
            WxOAuth2AccessToken wxOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            String uniId = wxOAuth2AccessToken.getUnionId();
            String openId = wxOAuth2AccessToken.getOpenId();
            String unionId = uniId == null ? "0" : uniId;
            String avatar = "";
            String nickname = "";
            try {
                String accessToken = wxOAuth2AccessToken.getAccessToken();
                String userInfoUri = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
                String userInfoUrl = String.format(userInfoUri, accessToken, openId);
                String resultInfo = HttpUtils.sendGet((String)userInfoUrl);
                Map<String, String> resultMap = MapUtils.jsonToMap((String)resultInfo);
                avatar = (String)resultMap.get("headimgurl");
                nickname = (String)resultMap.get("nickname");
            }
            catch (Exception exception) {
                // empty catch block
            }
            return this.__wxLoginHandle(openId, unionId, avatar, nickname, terminal);
        }
        catch (WxErrorException e) {
            throw new OperateException(e.getError().getErrorCode() + ", " + e.getError().getErrorMsg());
        }
    }

    public String oaCodeUrl(String url) {
        WxMpService wxMpService = WxMnpDriver.oa();
        WxMpOAuth2ServiceImpl wxMpOAuth2Service = new WxMpOAuth2ServiceImpl(wxMpService);
        String state = ToolUtils.makeMd5((String)ToolUtils.makeToken());
        return wxMpOAuth2Service.buildAuthorizationUrl(url, "snsapi_userinfo", state);
    }

    public String scanCodeUrl(String url, HttpSession session) {
        String appId = ConfigUtils.get((String)"open_platform", (String)"app_id", (String)"");
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";
        String redirectUrl = url;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new OperateException(e.getMessage());
        }
        String state = ToolUtils.makeUUID().replaceAll("-", "");
        ScanLoginCache.set((String)session.getId(), (String)state);
        return String.format(baseUrl, appId, redirectUrl, state);
    }

    public LoginTokenVo scanLogin(String code, String state, Integer terminal, HttpSession session) {
        Map userinfoMap;
        Map resultMap;
        if (!ScanLoginCache.get((String)session.getId()).equals(state)) {
            throw new OperateException("\u4e8c\u7ef4\u7801\u5df2\u5931\u6548\u6216\u4e0d\u5b58\u5728,\u8bf7\u91cd\u65b0\u64cd\u4f5c");
        }
        String appId = ConfigUtils.get((String)"open_platform", (String)"app_id", (String)"");
        String appSecret = ConfigUtils.get((String)"open_platform", (String)"app_secret", (String)"");
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        try {
            String accessTokenUrl = String.format(baseAccessTokenUrl, appId, appSecret, code);
            String result = HttpUtils.sendGet((String)accessTokenUrl);
            resultMap = MapUtils.jsonToMap((String)result);
        }
        catch (Exception e) {
            throw new OperateException("\u83b7\u53d6access_token\u5931\u8d25:" + e.getMessage());
        }
        String accessToken = (String)resultMap.get("access_token");
        String openid = (String)resultMap.get("openid");
        String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
        try {
            String resultUserInfo = HttpUtils.sendGet((String)userInfoUrl);
            userinfoMap = MapUtils.jsonToMap((String)resultUserInfo);
        }
        catch (Exception e) {
            throw new OperateException("\u83b7\u53d6\u7528\u6237\u4fe1\u606f\u5931\u8d25:" + e.getMessage());
        }
        String openId = (String)userinfoMap.get("openid");
        String uniId = (String)userinfoMap.get("unionid");
        String unionId = uniId == null ? "0" : uniId;
        String avatar = (String)resultMap.getOrDefault("headimgurl", "");
        String nickname = (String)resultMap.getOrDefault("nickname", "");
        return this.__wxLoginHandle(openId, unionId, avatar, nickname, terminal);
    }

    public void logout() {
        String token = LikeFrontThreadLocal.getToken();
        TokenLoginCache.del();
        if (StringUtils.isNotEmpty(token)) {
            this.userSessionMapper.delete(new QueryWrapper<UserSession>().eq("token", token));
        }
    }

    private LoginTokenVo __wxLoginHandle(String openId, String unionId, String avatar, String nickname, Integer terminal) {
        QueryWrapper<UserAuth> queryWrapper = !StringUtils.equals((CharSequence)unionId, (CharSequence)"0")
                ? new QueryWrapper<UserAuth>().nested(wq -> wq.eq("unionid", (Object)unionId).or().eq("openid", (Object)openId)).last("limit 1")
                : new QueryWrapper<UserAuth>().nested(wq -> wq.eq("openid", (Object)openId)).last("limit 1");
        UserAuth userAuth = (UserAuth)this.userAuthMapper.selectOne((Wrapper)queryWrapper);
        User user = null;
        if (StringUtils.isNotNull(userAuth)) {
            user = (User)this.userMapper.selectOne(new QueryWrapper<User>().isNull("delete_time").eq("id", (Object)userAuth.getUserId()).last("limit 1"));
        }
        String defaultAvatar = ConfigUtils.get((String)"default_image", (String)"user_avatar", (String)"/api/static/default_avatar.png");
        if (StringUtils.isNull(user)) {
            Integer sn = this.__generateSn();
            String defaultNickname = "\u7528\u6237" + sn;
            if (StringUtils.isNotEmpty((String)avatar)) {
                try {
                    Long time = System.currentTimeMillis();
                    String date = TimeUtils.millisecondToDate((Long)time, (String)"yyyyMMdd");
                    String name = ToolUtils.makeMd5((String)(ToolUtils.makeUUID() + time)) + ".jpg";
                    String path = "avatar" + date + "/" + name;
                    String savePath = YmlUtils.get((String)"like.upload-directory") + path;
                    ToolUtils.download((String)avatar, (String)savePath);
                    defaultAvatar = path;
                }
                catch (IOException time) {
                    // empty catch block
                }
            }
            if (StringUtils.isNotEmpty((String)nickname)) {
                defaultNickname = nickname;
            }
            User model = new User();
            model.setSn(sn);
            model.setAvatar(defaultAvatar);
            model.setNickname(defaultNickname);
            model.setAccount("u" + sn);
            model.setChannel(terminal);
            model.setSex(Integer.valueOf(0));
            model.setLoginIp(IpUtils.getHostIp());
            model.setLoginTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            model.setIsNewUser(Integer.valueOf(1));
            this.userMapper.insert(model);
            user = model;
        }
        if (user.getIsDisable() != null && user.getIsDisable().intValue() == 1) {
            throw new OperateException(ErrorEnum.LOGIN_DISABLE_ERROR.getMsg());
        }
        if (StringUtils.isNull(userAuth)) {
            UserAuth authModel = new UserAuth();
            authModel.setUserId(user.getId());
            authModel.setUnionid(unionId);
            authModel.setOpenid(openId);
            authModel.setTerminal(terminal);
            authModel.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            authModel.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.userAuthMapper.insert(authModel);
        } else {
            boolean changed = false;
            if (!user.getId().equals(userAuth.getUserId())) {
                // 旧授权指向的用户已被删除：把 openid 重新绑定到本次新建的账号，避免每次登录重复建档。
                userAuth.setUserId(user.getId());
                changed = true;
            }
            if (StringUtils.isEmpty((String)userAuth.getUnionid()) && !StringUtils.equals((CharSequence)unionId, (CharSequence)"0")) {
                userAuth.setUnionid(unionId);
                changed = true;
            }
            if (changed) {
                userAuth.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.userAuthMapper.updateById(userAuth);
            }
        }
        return this.__loginToken(user.getId(), user.getMobile(), user.getIsNewUser(), terminal);
    }

    private LoginTokenVo __wxInstructorLoginHandle(String openId, String unionId, Integer terminal) {
        QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<UserAuth>();
        if (!StringUtils.equals((CharSequence)unionId, (CharSequence)"0")) {
            queryWrapper.nested(wq -> wq.eq("unionid", (Object)unionId).or().eq("openid", (Object)openId)).last("limit 1");
        } else {
            queryWrapper.eq("openid", (Object)openId).last("limit 1");
        }
        InstructorAuth instructorAuth = (InstructorAuth)this.instructorAuthMapper.selectOne((Wrapper)queryWrapper);
        Instructor instructor = null;
        if (StringUtils.isNotNull(instructorAuth)) {
            instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().isNull("delete_time").eq("id", (Object)instructorAuth.getInstructorId()).last("limit 1"));
        }
        if (StringUtils.isNull(instructor)) {
            LoginTokenVo vo = new LoginTokenVo();
            vo.setId(Integer.valueOf(0));
            vo.setIsNew(Integer.valueOf(1));
            String tempToken = ToolUtils.makeToken();
            String redisKey = "instructor:wxlogin:temp:" + tempToken;
            String redisValue = openId + "," + unionId;
            RedisUtils.set((String)redisKey, (Object)redisValue, (long)7200L);
            vo.setToken(tempToken);
            return vo;
        }
        this.__assertInstructorLoginAllowed(instructor);
        if (instructor.getUserId() != null && instructor.getUserId() > 0) {
            User user = this.__requireInstructorAccount(instructor);
            Assert.isFalse((user.getIsDisable() != null && user.getIsDisable() == 1 ? 1 : 0) != 0,
                    (String)"\u6559\u7ec3\u767b\u5f55\u8d26\u53f7\u5df2\u88ab\u7981\u7528", (Object[])new Object[0]);
        }
        if (!StringUtils.isNull(instructorAuth) && StringUtils.isEmpty((String)instructorAuth.getUnionid()) && !StringUtils.equals((CharSequence)unionId, (CharSequence)"0")) {
            instructorAuth.setUnionid(unionId);
            instructorAuth.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.instructorAuthMapper.updateById(instructorAuth);
        }
        return this.__instructorLoginToken(instructor, terminal);
    }

    private LoginTokenVo __loginToken(Integer userId, String mobile, Integer isNew, Integer terminal) {
        String token = ToolUtils.makeToken();
        TokenLoginCache.set((String)token, (Object)userId, (String)String.valueOf(terminal));
        User user = new User();
        user.setLoginIp(IpUtils.getHostIp());
        user.setLoginTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userMapper.update(user, new QueryWrapper<User>().eq("id", (Object)userId));
        LoginTokenVo vo = new LoginTokenVo();
        vo.setId(userId);
        vo.setIsBindMobile(Boolean.valueOf(!StringUtils.isEmpty((String)mobile)));
        vo.setToken(token);
        vo.setIsNew(isNew == null ? Integer.valueOf(0) : isNew);
        vo.setMobile(mobile);
        this.userSessionMapper.delete(new QueryWrapper<UserSession>().eq("user_id", (Object)userId).eq("terminal", (Object)terminal));
        UserSession userSession = new UserSession();
        userSession.setUserId(userId);
        userSession.setToken(token);
        userSession.setTerminal(terminal);
        userSession.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        userSession.setExpireTime(Long.valueOf(System.currentTimeMillis() / 1000L + this.__sessionTimeout()));
        this.userSessionMapper.insert(userSession);
        return vo;
    }

    private long __sessionTimeout() {
        String value = YmlUtils.get((String)"like.login.timeout");
        if (StringUtils.isBlank(value)) {
            value = YmlUtils.get((String)"sa-token.timeout");
        }
        if (StringUtils.isBlank(value)) {
            return 7200L;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 7200L;
        }
    }

    private Integer __generateSn() {
        Integer sn;
        User snModel;
        do {
            sn = Integer.parseInt(ToolUtils.randomInt((int)8));
        } while ((snModel = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,sn"}).eq("sn", (Object)sn).last("limit 1"))) != null);
        return sn;
    }
}

