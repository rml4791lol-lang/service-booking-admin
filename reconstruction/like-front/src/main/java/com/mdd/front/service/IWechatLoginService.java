package com.mdd.front.service;

import com.mdd.front.vo.login.WxSessionVo;

/**
 * 微信小程序登录凭证换取 openid。
 */
public interface IWechatLoginService {

    /**
     * 用 wx.login 的 code 换 openid。
     *
     * <p>真实模式读取 la_config.mnp_setting 的 app_id/app_secret 调微信 jscode2session；
     * 本地可用 like.wechat.login.mock-enabled 打开 mock，避免依赖外包方凭据。</p>
     */
    WxSessionVo code2Session(String code);
}
