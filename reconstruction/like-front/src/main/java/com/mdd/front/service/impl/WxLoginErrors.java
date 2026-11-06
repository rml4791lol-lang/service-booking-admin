package com.mdd.front.service.impl;

/**
 * 微信 code2session 错误码 → 用户可见文案（#23）。
 *
 * <p>真实实现 {@link WxLoginServiceImpl} 与 mock 实现 {@link WxMockLoginServiceImpl}
 * 共用同一份映射，避免「本地 mock 验过的错误文案」与真实微信分支不一致。</p>
 */
final class WxLoginErrors {

    static String messageFor(int errcode) {
        switch (errcode) {
            case 40029:
            case 40163:
            case 40226:
                return "微信登录凭证无效或已过期，请重试";
            case 40013:
            case 40125:
            case 40164:
            case 41004:
                return "微信登录配置有误，请联系管理员";
            case 45011:
                return "微信登录请求过于频繁，请稍后重试";
            case -1:
                return "微信登录服务繁忙，请稍后重试";
            default:
                return "微信登录失败，请稍后重试";
        }
    }

    private WxLoginErrors() {
    }
}
