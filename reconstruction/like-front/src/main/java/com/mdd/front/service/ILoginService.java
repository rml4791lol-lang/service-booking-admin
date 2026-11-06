/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.ILoginService
 *  com.mdd.front.validate.login.LoginPwdValidate
 *  com.mdd.front.vo.login.LoginTokenVo
 *  javax.servlet.http.HttpSession
 */
package com.mdd.front.service;

import com.mdd.front.validate.login.LoginPwdValidate;
import com.mdd.front.vo.login.LoginTokenVo;
import javax.servlet.http.HttpSession;

public interface ILoginService {
    public void register(String var1, String var2, String var3, Integer var4);

    public LoginTokenVo accountLogin(LoginPwdValidate var1);

    public LoginTokenVo instructorPasswordLogin(LoginPwdValidate var1);

    public LoginTokenVo instructorMobileLogin(LoginPwdValidate var1);

    public LoginTokenVo mobileLogin(String var1, String var2, Integer var3, Integer var4);

    public LoginTokenVo mnpLogin(String var1, Integer var2);

    public LoginTokenVo instructorMnpLogin(String var1, Integer var2);

    public LoginTokenVo officeLogin(String var1, Integer var2);

    public String oaCodeUrl(String var1);

    public String scanCodeUrl(String var1, HttpSession var2);

    public LoginTokenVo scanLogin(String var1, String var2, Integer var3, HttpSession var4);

    public void logout();
}

