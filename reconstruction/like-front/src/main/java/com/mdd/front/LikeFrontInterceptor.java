/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.entity.user.UserSession
 *  com.mdd.common.enums.ClientEnum
 *  com.mdd.common.enums.ErrorEnum
 *  com.mdd.common.exception.LoginException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.mapper.user.UserSessionMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.YmlUtils
 *  com.mdd.front.LikeFrontInterceptor
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.cache.TokenLoginCache
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.jetbrains.annotations.NotNull
 *  org.springframework.lang.NonNull
 *  org.springframework.stereotype.Component
 *  org.springframework.web.method.HandlerMethod
 *  org.springframework.web.servlet.HandlerInterceptor
 */
package com.mdd.front;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.user.User;
import com.mdd.common.entity.user.UserSession;
import com.mdd.common.enums.ClientEnum;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.mapper.user.UserSessionMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.cache.TokenLoginCache;
import java.lang.reflect.Method;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LikeFrontInterceptor
implements HandlerInterceptor {
    @Resource
    UserMapper userMapper;
    @Resource
    UserSessionMapper userSessionMapper;
    @Resource
    InstructorMapper instructorMapper;

    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        String reqUri = request.getRequestURI();
        if (!(handler instanceof HandlerMethod) || !reqUri.startsWith("/api")) {
            return true;
        }
        String token = request.getHeader(YmlUtils.get((String)"sa-token.token-name"));
        LikeFrontThreadLocal.put((String)"token", (Object)token);
        try {
            String terminal = request.getHeader("terminal");
            if (StringUtils.isEmpty((String)terminal)) {
                if (StringUtils.isEmpty((String)token)) {
                    LikeFrontThreadLocal.put((String)"terminal", (Object)ClientEnum.PC.getCode());
                } else {
                    UserSession userSession = (UserSession)this.userSessionMapper.selectOne(new QueryWrapper<UserSession>().eq("token", (Object)token).gt("expire_time", (Object)(System.currentTimeMillis() / 1000L)).orderByDesc("id").last("limit 1"));
                    LikeFrontThreadLocal.put((String)"terminal", (Object)(StringUtils.isNull(userSession) ? ClientEnum.H5.getCode() : userSession.getTerminal().intValue()));
                }
            } else {
                LikeFrontThreadLocal.put((String)"terminal", (Object)terminal);
            }
            Method method = this.obtainAop(handler);
            this.checkLogin(method, token, reqUri);
        }
        catch (LoginException e) {
            AjaxResult result = AjaxResult.failed((Integer)e.getCode(), (String)e.getMsg());
            response.getWriter().print(JSON.toJSONString((Object)result));
            return false;
        }
        return true;
    }

    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        LikeFrontThreadLocal.remove();
    }

    private Method obtainAop(@NotNull Object handler) {
        if (handler instanceof HandlerMethod) {
            return ((HandlerMethod) handler).getMethod();
        }
        return null;
    }

    private void checkLogin(Method method, String token, String reqUri) {
        boolean instructorUri = this.isInstructorUri(reqUri);
        for (int i = 0; i <= 0; ++i) {
            Integer tokenUserId;
            if (StringUtils.isNotNull(method) && method.isAnnotationPresent(NotLogin.class)) {
                try {
                    tokenUserId = TokenLoginCache.get();
                    if (tokenUserId <= 0) break;
                    if (instructorUri) {
                        Instructor instructor = this.resolveInstructor(tokenUserId);
                        if (instructor == null) break;
                        LikeFrontThreadLocal.put((String)"userId", (Object)instructor.getId());
                        break;
                    }
                    User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,sn,account"}).eq("id", (Object)tokenUserId).eq("is_disable", (Object)0).isNull("delete_time").last("limit 1"));
                    if (user == null) break;
                    LikeFrontThreadLocal.put((String)"userId", (Object)tokenUserId);
                }
                catch (Exception e2) {}
                break;
            }
            if (StringUtils.isNull(token) || StringUtils.isBlank((CharSequence)token)) {
                Integer errCode = ErrorEnum.TOKEN_EMPTY.getCode();
                String errMsg = ErrorEnum.TOKEN_EMPTY.getMsg();
                throw new LoginException(errCode, errMsg);
            }
            tokenUserId = TokenLoginCache.get();
            if (tokenUserId <= 0) {
                Integer errCode = ErrorEnum.TOKEN_INVALID.getCode();
                String errMsg = ErrorEnum.TOKEN_INVALID.getMsg();
                throw new LoginException(errCode, errMsg);
            }
            // 评价互动（interact）是家长/学员的普通社交行为，不能按教练接口校验；
            // 只有教练回复评价（reply）才要求教练身份。口径见 docs/security/账号安全与评价权限口径.md。
            boolean isInstructorApi = instructorUri || this.isInstructorReviewUri(reqUri);
            if (isInstructorApi && !reqUri.equals("/api/instructor/apply")
                    && !reqUri.equals("/api/instructor/cancelApply")
                    && !reqUri.equals("/api/instructor/applyStatus")) {
                Instructor instructor = this.resolveInstructor(tokenUserId);
                if (StringUtils.isNull(instructor)) {
                    Integer errCode = ErrorEnum.TOKEN_INVALID.getCode();
                    String errMsg = ErrorEnum.TOKEN_INVALID.getMsg();
                    throw new LoginException(errCode, errMsg);
                }
                this.checkInstructorAccount(instructor, tokenUserId, reqUri);
                LikeFrontThreadLocal.put((String)"userId", (Object)instructor.getId());
                LikeFrontThreadLocal.put((String)"username", (Object)instructor.getName());
                LikeFrontThreadLocal.put((String)"mobile", (Object)instructor.getPhone());
                continue;
            }
            User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,sn,account,nickname,mobile,is_disable"}).eq("id", (Object)tokenUserId).isNull("delete_time").last("limit 1"));
            if (StringUtils.isNull(user)) {
                Integer errCode = ErrorEnum.TOKEN_INVALID.getCode();
                String errMsg = ErrorEnum.TOKEN_INVALID.getMsg();
                throw new LoginException(errCode, errMsg);
            }
            if (user.getIsDisable().equals(1)) {
                Integer errCode = ErrorEnum.LOGIN_DISABLE_ERROR.getCode();
                String errMsg = ErrorEnum.LOGIN_DISABLE_ERROR.getMsg();
                throw new LoginException(errCode, errMsg);
            }
            LikeFrontThreadLocal.put((String)"userId", (Object)user.getId());
            LikeFrontThreadLocal.put((String)"userSn", (Object)user.getSn());
            LikeFrontThreadLocal.put((String)"username", (Object)user.getAccount());
            LikeFrontThreadLocal.put((String)"nickname", (Object)user.getNickname());
            LikeFrontThreadLocal.put((String)"mobile", (Object)user.getMobile());
        }
    }

    /**
     * 教练端口径：/api/instructor 与 /api/instructor/*；不要误伤 /api/instructorUser/*。
     */
    private boolean isInstructorUri(String reqUri) {
        return "/api/instructor".equals(reqUri) || reqUri.startsWith("/api/instructor/");
    }

    /**
     * 评价回复口径：仅教练可回复；interact（推荐/点赞）对所有登录账号开放，
     * 不能放进这里，否则家长互动会被误判为教练接口并返回 token 无效/登录超时。
     */
    private boolean isInstructorReviewUri(String reqUri) {
        return "/api/course_review/reply".equals(reqUri) || reqUri.startsWith("/api/course_review/reply/");
    }

    /**
     * 教练 token 主体是 la_user.id（与家长统一），通过 la_instructor.user_id 反查档案。
     * 仅当教练尚未绑定账号（user_id=0）时才兼容旧的 instructor.id token。
     */
    private Instructor resolveInstructor(Integer tokenUserId) {
        Instructor byUser = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .select(new String[]{"id,user_id,phone,name,status,must_change_password"})
                .eq("user_id", (Object)tokenUserId)
                .isNull("delete_time")
                .last("limit 1"));
        if (byUser != null) {
            return byUser;
        }
        Instructor byId = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .select(new String[]{"id,user_id,phone,name,status,must_change_password"})
                .eq("id", (Object)tokenUserId)
                .isNull("delete_time")
                .last("limit 1"));
        if (byId != null && byId.getUserId() != null && byId.getUserId() > 0) {
            return null;
        }
        return byId;
    }

    private void checkInstructorAccount(Instructor instructor, Integer tokenUserId, String reqUri) {
        if (instructor.getStatus() != null && instructor.getStatus() == 0) {
            throw new LoginException(ErrorEnum.LOGIN_DISABLE_ERROR.getCode(), "教练账号已停用");
        }
        if (instructor.getStatus() != null && instructor.getStatus() == 2) {
            throw new LoginException(ErrorEnum.LOGIN_DISABLE_ERROR.getCode(), "资质待审核，暂不可登录");
        }
        if (instructor.getUserId() != null && instructor.getUserId().equals(tokenUserId)) {
            User loginUser = (User)this.userMapper.selectOne(new QueryWrapper<User>()
                    .select(new String[]{"id,is_disable"})
                    .eq("id", (Object)tokenUserId)
                    .isNull("delete_time")
                    .last("limit 1"));
            if (loginUser == null) {
                throw new LoginException(ErrorEnum.TOKEN_INVALID.getCode(), ErrorEnum.TOKEN_INVALID.getMsg());
            }
            if (loginUser.getIsDisable() != null && loginUser.getIsDisable() == 1) {
                throw new LoginException(ErrorEnum.LOGIN_DISABLE_ERROR.getCode(), "教练登录账号已被禁用");
            }
        }
        boolean mustChange = instructor.getMustChangePassword() != null && instructor.getMustChangePassword() == 1;
        if (mustChange && !this.mustChangeAllowed(reqUri)) {
            throw new LoginException(ErrorEnum.FAILED.getCode(), "首次登录请先修改初始密码");
        }
    }

    private boolean mustChangeAllowed(String reqUri) {
        return "/api/instructor/account/info".equals(reqUri)
                || "/api/instructor/account/changePassword".equals(reqUri);
    }
}

