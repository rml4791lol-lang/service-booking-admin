/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.system.SystemCaptchaVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="\u9a8c\u8bc1\u7801")
public class SystemCaptchaVo {
    @ApiModelProperty(value="\u6807\u8bc6")
    private String uuid;
    @ApiModelProperty(value="\u56fe\u7247")
    private String img;

    public String getUuid() {
        return this.uuid;
    }

    public String getImg() {
        return this.img;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemCaptchaVo)) {
            return false;
        }
        SystemCaptchaVo other = (SystemCaptchaVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$uuid = this.getUuid();
        String other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) {
            return false;
        }
        String this$img = this.getImg();
        String other$img = other.getImg();
        return !(this$img == null ? other$img != null : !this$img.equals(other$img));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemCaptchaVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $uuid = this.getUuid();
        result = result * 59 + ($uuid == null ? 43 : $uuid.hashCode());
        String $img = this.getImg();
        result = result * 59 + ($img == null ? 43 : $img.hashCode());
        return result;
    }

    public String toString() {
        return "SystemCaptchaVo(uuid=" + this.getUuid() + ", img=" + this.getImg() + ")";
    }
}

