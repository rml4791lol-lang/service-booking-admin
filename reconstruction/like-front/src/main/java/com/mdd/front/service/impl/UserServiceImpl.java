/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.binarywang.wx.miniapp.api.WxMaService
 *  cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl
 *  cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult
 *  cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo
 *  cn.binarywang.wx.miniapp.config.WxMaConfig
 *  cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.entity.user.UserAuth
 *  com.mdd.common.enums.ClientEnum
 *  com.mdd.common.enums.NoticeEnum
 *  com.mdd.common.enums.UserEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.user.UserAuthMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.plugin.notice.NoticeCheck
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.ToolUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.service.IUserService
 *  com.mdd.front.service.impl.UserServiceImpl
 *  com.mdd.front.validate.users.NewUserUpdateValidate
 *  com.mdd.front.validate.users.ResetPasswordValidate
 *  com.mdd.front.validate.users.UserBindWechatValidate
 *  com.mdd.front.validate.users.UserPhoneBindValidate
 *  com.mdd.front.validate.users.UserUpdateValidate
 *  com.mdd.front.vo.user.UserCenterVo
 *  com.mdd.front.vo.user.UserInfoVo
 *  javax.annotation.Resource
 *  me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken
 *  me.chanjar.weixin.common.error.WxErrorException
 *  me.chanjar.weixin.mp.api.WxMpService
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.Assert
 */
package com.mdd.front.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.alibaba.fastjson2.JSON;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.refund.RefundTask;
import com.mdd.common.entity.system.OperationLog;
import com.mdd.common.entity.user.User;
import com.mdd.common.entity.user.UserSession;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.user.UserAuth;
import com.mdd.common.enums.ClientEnum;
import com.mdd.common.enums.NoticeEnum;
import com.mdd.common.enums.UserEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseRefundMapper;
import com.mdd.common.mapper.refund.RefundTaskMapper;
import com.mdd.common.mapper.system.SystemLogOperateMapper;
import com.mdd.common.mapper.user.UserAuthMapper;
import com.mdd.common.mapper.user.UserSessionMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.plugin.notice.NoticeCheck;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.CourseBookingUtil;
import com.mdd.common.util.IpUtils;
import com.mdd.common.util.ParamUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.UserCancelAccountGuard;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IUserService;
import com.mdd.front.validate.users.NewUserUpdateValidate;
import com.mdd.front.validate.users.ResetPasswordValidate;
import com.mdd.front.validate.users.UserBindWechatValidate;
import com.mdd.front.validate.users.UserPhoneBindValidate;
import com.mdd.front.validate.users.UserProfileValidate;
import com.mdd.front.validate.users.UserUpdateValidate;
import com.mdd.front.vo.user.UserCenterVo;
import com.mdd.front.vo.user.UserInfoVo;
import com.mdd.front.vo.user.UserProfileVo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class UserServiceImpl
implements IUserService {
    @Resource
    UserMapper userMapper;
    @Resource
    UserAuthMapper userAuthMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CourseRefundMapper courseRefundMapper;
    @Resource
    RefundTaskMapper refundTaskMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    UserSessionMapper userSessionMapper;
    @Resource
    SystemLogOperateMapper systemLogOperateMapper;

    /**
     * 绑定/改绑手机号是否强制短信验证。
     *
     * <p>生产默认 true，且不允许关闭；本地短信通道未接入或联调时可临时置 false，
     * 但那只用于本地 mock 验证，不能作为线上绕过手段。口径见
     * {@code docs/security/账号安全与评价权限口径.md}。</p>
     */
    @Value("${like.user.bind-mobile.require-sms-code:true}")
    private boolean bindMobileRequireSmsCode;

    @Resource
    private Environment environment;

    /**
     * #70 生产安全闸：prod profile 下禁止通过配置关闭绑定手机号短信校验。
     * 本地联调可显式把 require-sms-code 设为 false，但生产启动即失败，避免配置绕过。
     */
    @PostConstruct
    public void validateBindMobileSmsCodeGuard() {
        if (environment.acceptsProfiles(Profiles.of("prod")) && !bindMobileRequireSmsCode) {
            throw new IllegalStateException(
                    "生产环境禁止关闭 like.user.bind-mobile.require-sms-code（绑定/改绑手机号必须短信验证）");
        }
    }

    public UserCenterVo center(Integer userId, Integer terminal) {
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,sn,sex,account,nickname,real_name,avatar,mobile,create_time,is_new_user,user_money,password"}).eq("id", (Object)userId).isNull("delete_time").last("limit 1"));
        UserCenterVo vo = new UserCenterVo();
        BeanUtils.copyProperties((Object)user, (Object)vo);
        if (user.getAvatar().equals("")) {
            String avatar = ConfigUtils.get((String)"user", (String)"defaultAvatar", (String)"");
            vo.setAvatar(UrlUtils.toAbsoluteUrl((String)avatar));
        } else {
            vo.setAvatar(UrlUtils.toAbsoluteUrl((String)user.getAvatar()));
        }
        UserAuth userAuth = (UserAuth)this.userAuthMapper.selectOne(new QueryWrapper<UserAuth>().select(new String[]{"id,openid,terminal"}).eq("user_id", (Object)userId).last("limit 1"));
        vo.setIsAuth(Boolean.valueOf(userAuth != null));
        vo.setHasPassword(Boolean.valueOf(StringUtils.isNotBlank((CharSequence)user.getPassword())));
        vo.setCreateTime(TimeUtils.timestampToDate((Long)user.getCreateTime()));
        vo.setSex(UserEnum.getSexDesc((Integer)user.getSex()));
        return vo;
    }

    public UserInfoVo info(Integer userId) {
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,sn,sex,birthday,account,password,nickname,real_name,avatar,mobile,create_time,user_money"}).eq("id", (Object)userId).last("limit 1"));
        UserAuth userAuth = (UserAuth)this.userAuthMapper.selectOne(new QueryWrapper<UserAuth>().select(new String[]{"id,openid"}).eq("user_id", (Object)userId).last("limit 1"));
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties((Object)user, (Object)vo);
        vo.setHasPassword(Boolean.valueOf(!user.getPassword().equals("")));
        vo.setHasAuth(Boolean.valueOf(userAuth != null));
        vo.setVersion(GlobalConfig.version);
        vo.setSex(UserEnum.getSexDesc((Integer)user.getSex()));
        vo.setCreateTime(TimeUtils.timestampToDate((Long)user.getCreateTime()));
        String birthday = user.getBirthday();
        vo.setBirthday(birthday != null ? birthday : "");
        if (birthday != null && !birthday.isEmpty()) {
            try {
                LocalDate birthDate = LocalDate.parse(birthday, DateTimeFormatter.ISO_LOCAL_DATE);
                vo.setAge(Integer.valueOf(Period.between(birthDate, LocalDate.now()).getYears()));
            }
            catch (Exception birthDate) {
                // empty catch block
            }
        }
        if (!user.getAvatar().equals("")) {
            vo.setAvatar(UrlUtils.toAbsoluteUrl((String)user.getAvatar()));
        } else {
            String avatar = ConfigUtils.get((String)"user", (String)"defaultAvatar", (String)"");
            vo.setAvatar(UrlUtils.toAbsoluteUrl((String)avatar));
        }
        return vo;
    }

    public UserProfileVo profile(Integer userId) {
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>()
                .select(new String[]{"id,real_name,nickname,mobile,sex,avatar,area_id,note"})
                .eq("id", (Object)userId).isNull("delete_time").last("limit 1"));
        if (StringUtils.isNull(user)) {
            throw new OperateException("用户不存在");
        }
        return this.buildProfile(user);
    }

    public UserProfileVo saveProfile(UserProfileValidate validate, Integer userId) {
        User exist = (User)this.userMapper.selectOne(new QueryWrapper<User>()
                .select(new String[]{"id,real_name,nickname,mobile,sex,avatar,area_id,note"})
                .eq("id", (Object)userId).isNull("delete_time").last("limit 1"));
        if (StringUtils.isNull(exist)) {
            throw new OperateException("用户不存在");
        }
        String name = validate.getName() == null ? "" : validate.getName().trim();
        if (name.isEmpty()) {
            throw new OperateException("请填写姓名");
        }
        if (name.length() > 32) {
            throw new OperateException("姓名最多 32 个字符");
        }
        String phone = validate.getPhone() == null ? "" : validate.getPhone().trim();
        if (!phone.isEmpty() && !phone.matches("^1\\d{10}$")) {
            throw new OperateException("请填写正确的手机号");
        }
        String currentPhone = exist.getMobile() == null ? "" : exist.getMobile().trim();
        if (!phone.equals(currentPhone)) {
            // 手机号参与登录/找回密码，资料接口不能成为绕过短信验证的改绑通道（issue #70 R3）。
            throw new OperateException("修改手机号需短信验证，请在「更换手机号」中验证或联系客服");
        }
        if (!phone.isEmpty()) {
            Long sameMobile = this.userMapper.selectCount(new QueryWrapper<User>()
                    .eq("mobile", (Object)phone).ne("id", (Object)userId).isNull("delete_time"));
            if (sameMobile != null && sameMobile > 0L) {
                throw new OperateException("该手机号已被其他账号使用");
            }
        }
        Integer sex = this.parseSex(validate.getGender(), exist.getSex());
        Integer areaId = this.parseAreaId(validate.getAreaId());
        String note = validate.getNote() == null ? "" : validate.getNote().trim();
        if (note.length() > 255) {
            throw new OperateException("备注最多 255 个字符");
        }
        String avatar = this.normalizeAvatar(validate.getAvatar(), exist.getAvatar());

        UpdateWrapper<User> update = new UpdateWrapper<User>();
        update.eq("id", userId);
        update.set("real_name", name);
        // 手机号只允许 /api/user/bindMobile 在短信验证通过后修改；这里不再写 mobile。
        update.set("sex", sex);
        update.set("avatar", avatar);
        update.set("area_id", areaId);
        update.set("note", note);
        update.set("update_time", System.currentTimeMillis() / 1000L);
        this.userMapper.update(null, update);

        User saved = (User)this.userMapper.selectOne(new QueryWrapper<User>()
                .select(new String[]{"id,real_name,nickname,mobile,sex,avatar,area_id,note"})
                .eq("id", (Object)userId).last("limit 1"));
        return this.buildProfile(saved);
    }

    private UserProfileVo buildProfile(User user) {
        UserProfileVo vo = new UserProfileVo();
        vo.setId(user.getId());
        String name = StringUtils.isNotBlank(user.getRealName()) ? user.getRealName() : user.getNickname();
        vo.setName(StringUtils.isNull(name) ? "" : name);
        vo.setPhone(StringUtils.isNull(user.getMobile()) ? "" : user.getMobile());
        // #182 收编公共打码（展示脱敏口径）。语义差异：11 位但非纯数字的输入由
        // 「原样回显」变为「打码」（公共实现按位截取），隐私更优；DB mobile 实际不出现该形态。
        vo.setPhoneMasked(ToolUtils.maskPhone(vo.getPhone(), false));
        vo.setGender(user.getSex() == null ? UserEnum.SEX_OTHER.getMsg() : UserEnum.getSexDesc(user.getSex()));
        vo.setAvatar(UrlUtils.toAbsoluteUrl(StringUtils.isNull(user.getAvatar()) ? "" : user.getAvatar()));
        vo.setAreaId(user.getAreaId());
        vo.setNote(StringUtils.isNull(user.getNote()) ? "" : user.getNote());
        if (user.getAreaId() != null && user.getAreaId() > 0) {
            Area area = (Area)this.areaMapper.selectById(user.getAreaId());
            if (area != null && area.getDeleteTime() == null) {
                vo.setAreaName(area.getName());
            }
        }
        return vo;
    }

    private Integer parseSex(String value, Integer defaultValue) {
        String text = value == null ? "" : value.trim();
        if (text.isEmpty()) {
            return defaultValue == null ? Integer.valueOf(0) : defaultValue;
        }
        if ("1".equals(text) || "男".equals(text) || "male".equalsIgnoreCase(text)) {
            return Integer.valueOf(1);
        }
        if ("2".equals(text) || "女".equals(text) || "female".equalsIgnoreCase(text)) {
            return Integer.valueOf(2);
        }
        if ("0".equals(text) || "未知".equals(text) || "unknown".equalsIgnoreCase(text)) {
            return Integer.valueOf(0);
        }
        throw new OperateException("性别参数错误");
    }

    private Integer parseAreaId(String value) {
        String text = value == null ? "" : value.trim();
        if (text.isEmpty() || "0".equals(text) || "null".equalsIgnoreCase(text)) {
            return null;
        }
        Integer areaId;
        try {
            areaId = Integer.valueOf(text);
        } catch (NumberFormatException e) {
            throw new OperateException("常去小区参数错误");
        }
        if (areaId <= 0) {
            return null;
        }
        Area area = (Area)this.areaMapper.selectById(areaId);
        if (area == null || area.getDeleteTime() != null) {
            throw new OperateException("常去小区不存在");
        }
        return areaId;
    }

    private String normalizeAvatar(String input, String current) {
        if (input == null) {
            return StringUtils.isNull(current) ? "" : current;
        }
        String avatar = input.trim();
        if (avatar.isEmpty()) {
            return "";
        }
        if (avatar.startsWith("http://") || avatar.startsWith("https://") || avatar.startsWith("/api/static/")) {
            return UrlUtils.toRelativeUrl(avatar);
        }
        // 小程序 chooseMedia 返回的是本地临时路径，上传能力不在本票范围；保留原头像，避免写入无效路径。
        return StringUtils.isNull(current) ? "" : current;
    }

    public void setInfo(UserUpdateValidate updateValidate, Integer userId) {
        String field = updateValidate.getField();
        String value = updateValidate.getValue();
        switch (field) {
            case "avatar": {
                User avatarUser = new User();
                avatarUser.setId(userId);
                avatarUser.setAvatar(UrlUtils.toRelativeUrl((String)value));
                avatarUser.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.userMapper.updateById(avatarUser);
                break;
            }
            case "account": {
                User usernameUser = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,account"}).eq("account", (Object)value).isNull("delete_time").last("limit 1"));
                if (StringUtils.isNotNull(usernameUser) && !usernameUser.getId().equals(userId)) {
                    throw new OperateException("\u8d26\u53f7\u5df2\u88ab\u4f7f\u7528!");
                }
                if (StringUtils.isNotNull(usernameUser) && usernameUser.getAccount().equals(value)) {
                    throw new OperateException("\u65b0\u8d26\u53f7\u4e0e\u65e7\u8d26\u53f7\u4e00\u81f4,\u4fee\u6539\u5931\u8d25!");
                }
                User u = new User();
                u.setId(userId);
                u.setAccount(value);
                u.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.userMapper.updateById(u);
                break;
            }
            case "nickname": {
                User nicknameUser = new User();
                nicknameUser.setId(userId);
                nicknameUser.setNickname(value);
                nicknameUser.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.userMapper.updateById(nicknameUser);
                break;
            }
            case "sex": {
                User sexUser = new User();
                sexUser.setId(userId);
                sexUser.setSex(ParamUtils.intOrDefault(value, 0, "性别"));
                sexUser.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.userMapper.updateById(sexUser);
                break;
            }
            case "birthday": {
                User birthdayUser = new User();
                birthdayUser.setId(userId);
                birthdayUser.setBirthday(value);
                birthdayUser.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.userMapper.updateById(birthdayUser);
                break;
            }
            default: {
                throw new OperateException("\u4e0d\u88ab\u652f\u6301\u7684\u7c7b\u578b");
            }
        }
    }

    public void changePwd(String password, String oldPassword, Integer userId) {
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,password"}).eq("id", (Object)userId).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)user, (String)"\u7528\u6237\u4e0d\u5b58\u5728");
        String pwd = ToolUtils.makePassword((String)password.trim());
        User u = new User();
        u.setId(userId);
        u.setPassword(pwd);
        u.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userMapper.updateById(u);
    }

    public void forgotPwd(String password, String mobile, String code) {
        int sceneCode = NoticeEnum.FIND_LOGIN_PASSWORD_CAPTCHA.getCode();
        if (!NoticeCheck.verify((Integer)sceneCode, (String)code, (String)mobile).booleanValue()) {
            throw new OperateException("\u9a8c\u8bc1\u7801\u9519\u8bef!");
        }
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,account,mobile,is_disable"}).isNull("delete_time").eq("mobile", (Object)mobile).last("limit 1"));
        com.baomidou.mybatisplus.core.toolkit.Assert.notNull((Object)user, (String)"\u8d26\u53f7\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        String pwd = ToolUtils.makePassword((String)password.trim());
        user.setPassword(pwd);
        user.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userMapper.updateById(user);
    }

    public void bindMobile(UserPhoneBindValidate mobileValidate, Integer userId) {
        String type = mobileValidate.getType();
        String mobile = mobileValidate.getMobile();
        String code = mobileValidate.getCode().toLowerCase();
        // 手机号参与登录/找回密码，绑定与改绑都必须校验短信验证码（issue #70 R3）。
        // bind 使用绑定场景码（BDSJHM），change 使用变更场景码（BGSJHM），两者不可混用。
        if (bindMobileRequireSmsCode) {
            int sceneCode = "change".equals(type)
                    ? NoticeEnum.CHANGE_MOBILE_CAPTCHA.getCode()
                    : NoticeEnum.BIND_MOBILE_CAPTCHA.getCode();
            if (!NoticeCheck.verify(Integer.valueOf(sceneCode), code, mobile).booleanValue()) {
                throw new OperateException("\u9a8c\u8bc1\u7801\u9519\u8bef!");
            }
        }
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,account,mobile"}).eq("mobile", (Object)mobile).isNull("delete_time").last("limit 1"));
        if (StringUtils.isNotNull(user) && !user.getId().equals(userId)) {
            throw new OperateException("\u624b\u673a\u53f7\u5df2\u88ab\u5176\u5b83\u8d26\u53f7\u7ed1\u5b9a!");
        }
        User u = new User();
        u.setId(userId);
        u.setMobile(mobile);
        u.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userMapper.updateById(u);
    }

    public void mnpMobile(String code) {
        Map<String, String> config = ConfigUtils.get((String)"mnp_setting");
        WxMaServiceImpl wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl wxConfig = new WxMaDefaultConfigImpl();
        wxConfig.setSecret(config.getOrDefault("app_secret", ""));
        wxConfig.setAppid(config.getOrDefault("app_id", ""));
        wxMaService.setWxMaConfig((WxMaConfig)wxConfig);
        try {
            WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxMaService.getUserService().getNewPhoneNoInfo(code);
            Integer userId = LikeFrontThreadLocal.getUserId();
            User userCheck = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,account,mobile"}).eq("mobile", (Object)wxMaPhoneNumberInfo.getPhoneNumber()).isNull("delete_time").last("limit 1"));
            if (StringUtils.isNotNull(userCheck) && !userCheck.getId().equals(userId)) {
                throw new OperateException("\u624b\u673a\u53f7\u5df2\u88ab\u5176\u5b83\u8d26\u53f7\u7ed1\u5b9a!");
            }
            User user = new User();
            user.setId(userId);
            user.setMobile(wxMaPhoneNumberInfo.getPhoneNumber());
            user.setAccount(wxMaPhoneNumberInfo.getPhoneNumber());
            user.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.userMapper.updateById(user);
        }
        catch (WxErrorException e) {
            throw new OperateException(e.getError().getErrorCode() + ", " + e.getError().getErrorMsg());
        }
    }

    public void updateNewUserInfo(NewUserUpdateValidate newUserUpdateValidate, Integer userId) {
        User user = new User();
        user.setId(userId);
        user.setNickname(newUserUpdateValidate.getNickname());
        user.setAvatar(UrlUtils.toRelativeUrl((String)newUserUpdateValidate.getAvatar()));
        user.setIsNewUser(Integer.valueOf(0));
        user.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userMapper.updateById(user);
    }

    public void bindMnp(UserBindWechatValidate bindMnpValidate, Integer userId) {
        try {
            String code = bindMnpValidate.getCode();
            WxMaService wxMaService = WxMnpDriver.mnp();
            WxMaJscode2SessionResult sessionResult = wxMaService.getUserService().getSessionInfo(code);
            String openId = sessionResult.getOpenid();
            String uniId = sessionResult.getUnionid();
            String unionId = uniId == null ? "0" : uniId;
            this.bindWechatAuth(openId, unionId, Integer.valueOf(ClientEnum.MNP.getCode()), userId);
        }
        catch (WxErrorException e) {
            throw new OperateException(e.getError().getErrorCode() + ", " + e.getError().getErrorMsg());
        }
    }

    public void bindOa(UserBindWechatValidate bindOaValidate, Integer userId) {
        try {
            WxMpService wxMpService = WxMnpDriver.oa();
            WxOAuth2AccessToken wxOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(bindOaValidate.getCode());
            String uniId = wxOAuth2AccessToken.getUnionId();
            String openId = wxOAuth2AccessToken.getOpenId();
            String unionId = uniId == null ? "0" : uniId;
            Integer terminal = LikeFrontThreadLocal.getTerminal();
            String key = bindOaValidate.getKey();
            this.bindWechatAuth(openId, unionId, terminal, userId);
        }
        catch (WxErrorException e) {
            throw new OperateException(e.getError().getErrorCode() + ", " + e.getError().getErrorMsg());
        }
    }

    @Transactional
    public void resetPassword(ResetPasswordValidate passwordValidate) {
        String code = passwordValidate.getCode();
        String mobile = passwordValidate.getMobile();
        String password = passwordValidate.getPassword();
        int sceneCode = NoticeEnum.FIND_LOGIN_PASSWORD_CAPTCHA.getCode();
        if (!NoticeCheck.verify((Integer)sceneCode, (String)code, (String)mobile).booleanValue()) {
            throw new OperateException("\u9a8c\u8bc1\u7801\u9519\u8bef!");
        }
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,account,mobile,is_disable"}).isNull("delete_time").eq("mobile", (Object)mobile).last("limit 1"));
        com.baomidou.mybatisplus.core.toolkit.Assert.notNull((Object)user, (String)"\u8d26\u53f7\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        String pwd = ToolUtils.makePassword((String)password.trim());
        user.setPassword(pwd);
        user.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userMapper.updateById(user);
    }

    /**
     * 注销账号（PIPL 自助注销入口）。
     *
     * <p>先校验在途资产（待支付订单 / 退款未完成 / 未结束预约 / 未使用次数券 / 余额），
     * 任一存在即拒绝并返回明确原因；全部清理后才执行注销：取消未结束预约 → 失效会话
     * （DB + Redis token）→ 解除微信 openid 绑定 → 软删 la_user。订单、核销、退款、
     * 资金流水等交易数据一律保留，供审计与对账。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAccount(Integer userId, Map<String, Object> params) {
        if (userId == null || userId <= 0) {
            throw new OperateException("用户未登录");
        }
        Object confirmValue = params == null ? null : params.get("confirm");
        boolean confirmed = Boolean.TRUE.equals(confirmValue)
                || (confirmValue != null && "true".equalsIgnoreCase(String.valueOf(confirmValue).trim()));
        if (!confirmed) {
            throw new OperateException("请确认注销账号");
        }
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>()
                .eq("id", userId).isNull("delete_time").last("limit 1"));
        if (user == null) {
            throw new OperateException("账号不存在或已注销");
        }

        Long pendingOrderCount = this.rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>()
                .eq("user_id", userId).eq("pay_status", Integer.valueOf(0)).isNull("delete_time"));
        int pendingOrders = pendingOrderCount == null ? 0 : pendingOrderCount.intValue();
        // 已支付但退款未完成：退课申请待审批 + 微信退款任务未成功（待执行/执行中/失败待补偿）。
        // “未完成”按任务状态 != 成功 判定，失败任务也要先人工重试/结案，避免注销后已付款项悬空。
        Long pendingRefundCount = this.courseRefundMapper.selectCount(new QueryWrapper<CourseRefund>()
                .eq("user_id", userId).eq("status", Integer.valueOf(0)).isNull("delete_time"));
        Long pendingTaskCount = this.refundTaskMapper.selectCount(new QueryWrapper<RefundTask>()
                .eq("user_id", userId).ne("status", Integer.valueOf(RefundTask.STATUS_SUCCESS)));
        int refundsInProgress = (pendingRefundCount == null ? 0 : pendingRefundCount.intValue())
                + (pendingTaskCount == null ? 0 : pendingTaskCount.intValue());
        Long unusedCouponCount = this.couponInstanceMapper.selectCount(new QueryWrapper<CouponInstance>()
                .eq("user_id", userId).eq("status", Integer.valueOf(1)).gt("remaining", Integer.valueOf(0)));
        int unusedCoupons = unusedCouponCount == null ? 0 : unusedCouponCount.intValue();
        List<CourseBooking> futureBookings = this.loadFutureBookings(userId);
        BigDecimal balance = user.getUserMoney() == null ? BigDecimal.ZERO : user.getUserMoney();

        String blockReason = UserCancelAccountGuard.check(
                pendingOrders, refundsInProgress, futureBookings.size(), unusedCoupons, balance);
        if (blockReason != null) {
            throw new OperateException(blockReason);
        }

        long now = TimeUtils.timestamp();
        // 先失效会话，注销立即生效（老 token 不再可用）；再兜底取消所有未结束预约。
        // 取消时重新查一次：校验通过后仍可能有并发新预约（或校验时课表数据刚更新），
        // 按「通过后取消未来预约」的要求一并置 status=3，不能只用校验时的快照。
        this.revokeSessions(userId);
        int cancelledBookings = this.cancelFutureBookings(userId, now);
        // la_user_auth 没有 delete_time，且 openid 是唯一键：清空 openid 会让第二个注销的
        // 账号撞唯一键，因此这里物理删除授权行以彻底解除微信绑定（注销后可用同一微信重新建档）。
        this.userAuthMapper.delete(new QueryWrapper<UserAuth>().eq("user_id", userId));
        User delete = new User();
        delete.setId(userId);
        delete.setDeleteTime(Long.valueOf(now));
        delete.setUpdateTime(Long.valueOf(now));
        this.userMapper.updateById(delete);

        String reason = params.get("reason") == null ? "" : String.valueOf(params.get("reason")).trim();
        this.writeCancelAccountLog(user, userId, reason.isEmpty() ? "用户自助注销" : reason, now, cancelledBookings);
    }

    private int cancelFutureBookings(Integer userId, long now) {
        List<CourseBooking> futureBookings = this.loadFutureBookings(userId);
        for (CourseBooking booking : futureBookings) {
            CourseBooking update = new CourseBooking();
            update.setId(booking.getId());
            update.setStatus(Integer.valueOf(CourseBookingUtil.STATUS_CANCELLED));
            update.setUpdateTime(Long.valueOf(now));
            this.courseBookingMapper.updateById(update);
        }
        return futureBookings.size();
    }

    private List<CourseBooking> loadFutureBookings(Integer userId) {
        List<CourseBooking> bookings = this.courseBookingMapper.selectList(new QueryWrapper<CourseBooking>()
                .eq("user_id", userId)
                .eq("status", Integer.valueOf(CourseBookingUtil.STATUS_BOOKED))
                .orderByAsc("id"));
        java.util.ArrayList<CourseBooking> future = new java.util.ArrayList<CourseBooking>();
        for (CourseBooking booking : bookings) {
            if (!this.isBookingEnded(booking)) {
                future.add(booking);
            }
        }
        return future;
    }

    private boolean isBookingEnded(CourseBooking booking) {
        CourseLesson lesson = this.resolveBookingLesson(booking);
        String lessonDate = booking.getLessonDate() != null && !booking.getLessonDate().isEmpty()
                ? booking.getLessonDate()
                : (lesson != null ? lesson.getLessonDate() : "");
        CourseClass courseClass = null;
        if (booking.getCourseClassId() != null && booking.getCourseClassId() > 0) {
            courseClass = (CourseClass)this.courseClassMapper.selectById(booking.getCourseClassId());
        }
        String timeSlot = lesson != null ? ClassScheduleUtil.resolveLessonTimeSlot(lesson, courseClass) : "";
        return CourseBookingUtil.isLessonEnded(lessonDate, timeSlot);
    }

    private CourseLesson resolveBookingLesson(CourseBooking booking) {
        if (booking.getLessonId() != null && booking.getLessonId() > 0) {
            return (CourseLesson)this.courseLessonMapper.selectById(booking.getLessonId());
        }
        if (booking.getCourseClassId() != null && booking.getLessonDate() != null) {
            return (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>()
                    .eq("class_id", booking.getCourseClassId())
                    .eq("lesson_date", booking.getLessonDate())
                    .last("limit 1"));
        }
        return null;
    }

    /** 会话失效：清 Redis 登录态 + 删除 la_user_session（否则注销后老 token 仍可访问）。 */
    private void revokeSessions(Integer userId) {
        List<UserSession> sessions = this.userSessionMapper.selectList(new QueryWrapper<UserSession>()
                .eq("user_id", userId));
        for (UserSession session : sessions) {
            if (StringUtils.isNotEmpty(session.getToken()) && session.getTerminal() != null) {
                RedisUtils.del("login:front:token:" + session.getTerminal() + ":" + session.getToken());
            }
        }
        this.userSessionMapper.delete(new QueryWrapper<UserSession>().eq("user_id", userId));
    }

    /** 注销审计日志（原因/时间/IP）；写 la_operation_log，admin_id=0 区分用户自助操作。 */
    private void writeCancelAccountLog(User user, Integer userId, String reason, long now, int cancelledBookings) {
        OperationLog log = new OperationLog();
        log.setAdminId(Integer.valueOf(0));
        log.setAdminName("用户自助");
        String account = user.getAccount() == null ? "" : user.getAccount();
        log.setAccount(account.length() > 16 ? account.substring(0, 16) : account);
        log.setAction("注销账号");
        log.setType("POST");
        log.setUrl("/api/user/cancelAccount");
        LinkedHashMap<String, Object> logParams = new LinkedHashMap<String, Object>();
        logParams.put("userId", userId);
        logParams.put("reason", reason);
        logParams.put("cancelTime", TimeUtils.timestampToDate(Long.valueOf(now)));
        logParams.put("cancelledBookings", Integer.valueOf(cancelledBookings));
        log.setParams(JSON.toJSONString(logParams));
        log.setResult("注销成功（保留订单/核销/退款等交易数据）");
        log.setIp(IpUtils.getIpAddress());
        log.setCreateTime(Long.valueOf(now));
        this.systemLogOperateMapper.insert(log);
    }

    public void bindWechatAuth(String openId, String unionId, Integer terminal, Integer userId) {
        UserAuth userAuth;
        UserAuth userAuthUnionId;
        UserAuth userAuthOpenId = (UserAuth)this.userAuthMapper.selectOne(new QueryWrapper<UserAuth>().eq("openid", (Object)openId).last("limit 1"));
        if (userAuthOpenId != null) {
            throw new OperateException("\u8be5\u5fae\u4fe1\u5df2\u7ed1\u5b9a");
        }
        if (!(StringUtils.isBlank((CharSequence)unionId) || unionId.equals("0") || (userAuthUnionId = (UserAuth)this.userAuthMapper.selectOne(new QueryWrapper<UserAuth>().eq("unionid", (Object)unionId).last("limit 1"))) == null || userId.equals(userAuthUnionId.getUserId()))) {
            throw new OperateException("\u8be5\u5fae\u4fe1\u5df2\u7ed1\u5b9a");
        }
        if (StringUtils.isNotNull(unionId) && !unionId.equals("0") && StringUtils.isNotNull((userAuth = (UserAuth)this.userAuthMapper.selectOne(new QueryWrapper<UserAuth>().eq("unionid", (Object)unionId).last("limit 1"))))) {
            throw new OperateException("\u8be5\u5fae\u4fe1\u5df2\u88ab\u7ed1\u5b9a");
        }
        UserAuth authModel = new UserAuth();
        authModel.setUserId(userId);
        authModel.setUnionid(unionId);
        authModel.setOpenid(openId);
        authModel.setTerminal(terminal);
        authModel.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        authModel.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userAuthMapper.insert(authModel);
    }
}

