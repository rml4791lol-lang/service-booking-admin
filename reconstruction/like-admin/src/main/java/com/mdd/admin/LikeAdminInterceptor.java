/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.dev33.satoken.stp.StpUtil
 *  com.alibaba.fastjson2.JSON
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.LikeAdminInterceptor
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.service.admin.IAdminRoleService
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.enums.ErrorEnum
 *  com.mdd.common.exception.LoginException
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.YmlUtils
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.jetbrains.annotations.NotNull
 *  org.springframework.stereotype.Component
 *  org.springframework.web.method.HandlerMethod
 *  org.springframework.web.servlet.HandlerInterceptor
 */
package com.mdd.admin;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.admin.IAdminRoleService;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LikeAdminInterceptor
implements HandlerInterceptor {
    @Resource
    AdminMapper systemAuthAdminMapper;
    @Resource
    IAdminRoleService iAdminRoleService;

    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String reqUri = request.getRequestURI();
        if (!(handler instanceof HandlerMethod) || !reqUri.startsWith("/api") && !reqUri.startsWith("/adminapi")) {
            return true;
        }
        try {
            response.setContentType("application/json;charset=utf-8");
            Method method = this.obtainAop(handler);
            this.checkLogin(method, reqUri);
        }
        catch (LoginException e) {
            AjaxResult result = AjaxResult.failed((Integer)e.getCode(), (String)e.getMsg());
            response.getWriter().print(JSON.toJSONString((Object)result));
            return false;
        }
        String env = YmlUtils.get((String)"like.production");
        if (StringUtils.isNotNull(env) && env.equals("true")) {
            String route;
            String prefix = "/adminapi/";
            String auths = route = request.getRequestURI().replaceFirst(prefix, "");
            List<String> ignoreUrl = Arrays.asList("login/account", "login/logout");
            if (request.getMethod().equals("POST") && !ignoreUrl.contains(auths)) {
                String message = "\u6f14\u793a\u73af\u5883\u4e0d\u652f\u6301\u4fee\u6539\u6570\u636e\uff0c\u8bf7\u4e0b\u8f7d\u6e90\u7801\u672c\u5730\u90e8\u7f72\u4f53\u9a8c";
                AjaxResult result = AjaxResult.failed((Integer)ErrorEnum.NO_PERMISSION.getCode(), (String)message);
                response.getWriter().print(JSON.toJSONString((Object)result));
                return false;
            }
        }
        return true;
    }

    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        LikeAdminThreadLocal.remove();
    }

    private Method obtainAop(@NotNull Object handler) throws Exception {
        Method[] methods;
        String[] objArr = handler.toString().split("#");
        String methodStr = objArr[1].split("\\(")[0];
        String classStr = objArr[0];
        Class<?> clazz = Class.forName(classStr);
        for (Method method : methods = clazz.getDeclaredMethods()) {
            if (!method.getName().equals(methodStr)) continue;
            return method;
        }
        return null;
    }

    private void checkLogin(Method method, String reqUri) {
        for (int i = 0; !(i > 0 || StringUtils.isNotNull(method) && method.isAnnotationPresent(NotLogin.class)); ++i) {
            Integer errCode;
            String token = StpUtil.getTokenValue();
            if (StringUtils.isNull(token) || StringUtils.isBlank((CharSequence)token)) {
                Integer errCode2 = ErrorEnum.TOKEN_EMPTY.getCode();
                String errMsg = ErrorEnum.TOKEN_EMPTY.getMsg();
                throw new LoginException(errCode2, errMsg);
            }
            Object id = StpUtil.getLoginId();
            if (StringUtils.isNull(id)) {
                Integer errCode3 = ErrorEnum.TOKEN_INVALID.getCode();
                String errMsg = ErrorEnum.TOKEN_INVALID.getMsg();
                throw new LoginException(errCode3, errMsg);
            }
            Admin adminUser = (Admin)this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(new String[]{"id,name,disable"}).eq("id", (Object)Integer.parseInt(id.toString())).isNull("delete_time").last("limit 1"));
            if (StringUtils.isNull(adminUser)) {
                errCode = ErrorEnum.TOKEN_INVALID.getCode();
                String errMsg = ErrorEnum.TOKEN_INVALID.getMsg();
                throw new LoginException(errCode, errMsg);
            }
            if (adminUser.getDisable().equals(1)) {
                errCode = ErrorEnum.LOGIN_DISABLE_ERROR.getCode();
                String errMsg = ErrorEnum.LOGIN_DISABLE_ERROR.getMsg();
                throw new LoginException(errCode, errMsg);
            }
            LikeAdminThreadLocal.put((String)"adminId", (Object)id);
            LikeAdminThreadLocal.put((String)"username", (Object)adminUser.getName());
            String roleIds = StringUtils.join((Iterable)this.iAdminRoleService.getRoleIdAttr(Integer.valueOf(Integer.parseInt(String.valueOf(id)))), (String)",");
            LikeAdminThreadLocal.put((String)"roleIds", (Object)roleIds);
            if (adminUser.getId().equals(1)) continue;
            this.checkAuth(method, reqUri);
        }
    }

    private void checkAuth(Method method, String uri) {
        for (int i = 0; !(i > 0 || StringUtils.isNotNull(method) && method.isAnnotationPresent(NotPower.class)); ++i) {
            String route;
            String prefix = "/adminapi/";
            String auths = route = uri.replaceFirst(prefix, "");
            StpUtil.checkPermission((String)auths);
        }
    }
}

