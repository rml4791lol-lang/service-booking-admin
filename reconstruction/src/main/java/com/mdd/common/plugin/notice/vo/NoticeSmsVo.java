/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.plugin.notice.vo;

import java.util.Arrays;

public class NoticeSmsVo {
    private Integer scene;
    private String mobile;
    private Integer expire;
    private String[] params;
    private String code;

    public Integer getScene() {
        return this.scene;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Integer getExpire() {
        return this.expire;
    }

    public String[] getParams() {
        return this.params;
    }

    public String getCode() {
        return this.code;
    }

    public NoticeSmsVo setScene(Integer scene) {
        this.scene = scene;
        return this;
    }

    public NoticeSmsVo setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public NoticeSmsVo setExpire(Integer expire) {
        this.expire = expire;
        return this;
    }

    public NoticeSmsVo setParams(String[] params) {
        this.params = params;
        return this;
    }

    public NoticeSmsVo setCode(String code) {
        this.code = code;
        return this;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NoticeSmsVo)) {
            return false;
        }
        NoticeSmsVo other = (NoticeSmsVo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$scene = this.getScene();
        Integer other$scene = other.getScene();
        if (this$scene == null ? other$scene != null : !((Object)this$scene).equals(other$scene)) {
            return false;
        }
        Integer this$expire = this.getExpire();
        Integer other$expire = other.getExpire();
        if (this$expire == null ? other$expire != null : !((Object)this$expire).equals(other$expire)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        if (!Arrays.deepEquals(this.getParams(), other.getParams())) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        return !(this$code == null ? other$code != null : !this$code.equals(other$code));
    }

    protected boolean canEqual(Object other) {
        return other instanceof NoticeSmsVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $scene = this.getScene();
        result = result * 59 + ($scene == null ? 43 : ((Object)$scene).hashCode());
        Integer $expire = this.getExpire();
        result = result * 59 + ($expire == null ? 43 : ((Object)$expire).hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getParams());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }

    public String toString() {
        return "NoticeSmsVo(scene=" + this.getScene() + ", mobile=" + this.getMobile() + ", expire=" + this.getExpire() + ", params=" + Arrays.deepToString(this.getParams()) + ", code=" + this.getCode() + ")";
    }
}

