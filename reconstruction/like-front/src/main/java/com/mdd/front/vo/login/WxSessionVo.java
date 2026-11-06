package com.mdd.front.vo.login;

import java.io.Serializable;

/**
 * 微信小程序 code2session 结果。
 *
 * <p>只保留登录需要的 openid/unionid，session_key 不落库、不返回前端。</p>
 */
public class WxSessionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String openid;
    private String unionid;

    public WxSessionVo() {
    }

    public WxSessionVo(String openid, String unionid) {
        this.openid = openid;
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
