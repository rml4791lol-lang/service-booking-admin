/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.IUserService
 *  com.mdd.front.validate.users.NewUserUpdateValidate
 *  com.mdd.front.validate.users.ResetPasswordValidate
 *  com.mdd.front.validate.users.UserBindWechatValidate
 *  com.mdd.front.validate.users.UserPhoneBindValidate
 *  com.mdd.front.validate.users.UserUpdateValidate
 *  com.mdd.front.vo.user.UserCenterVo
 *  com.mdd.front.vo.user.UserInfoVo
 */
package com.mdd.front.service;

import com.mdd.front.validate.users.NewUserUpdateValidate;
import com.mdd.front.validate.users.ResetPasswordValidate;
import com.mdd.front.validate.users.UserBindWechatValidate;
import com.mdd.front.validate.users.UserPhoneBindValidate;
import com.mdd.front.validate.users.UserProfileValidate;
import com.mdd.front.validate.users.UserUpdateValidate;
import com.mdd.front.vo.user.UserCenterVo;
import com.mdd.front.vo.user.UserInfoVo;
import com.mdd.front.vo.user.UserProfileVo;

public interface IUserService {
    public UserCenterVo center(Integer var1, Integer var2);

    public UserInfoVo info(Integer var1);

    public UserProfileVo profile(Integer var1);

    public UserProfileVo saveProfile(UserProfileValidate var1, Integer var2);

    public void setInfo(UserUpdateValidate var1, Integer var2);

    public void changePwd(String var1, String var2, Integer var3);

    public void forgotPwd(String var1, String var2, String var3);

    public void bindMobile(UserPhoneBindValidate var1, Integer var2);

    public void mnpMobile(String var1);

    public void updateNewUserInfo(NewUserUpdateValidate var1, Integer var2);

    public void bindMnp(UserBindWechatValidate var1, Integer var2);

    public void bindOa(UserBindWechatValidate var1, Integer var2);

    public void resetPassword(ResetPasswordValidate var1);

    /**
     * 注销账号（PIPL 自助注销）：入参需 confirm=true；存在在途资产时抛业务异常并给出原因。
     */
    public void cancelAccount(Integer userId, java.util.Map<String, Object> params);
}

