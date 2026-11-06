/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.common.SmsValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Pattern
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.front.validate.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u77ed\u4fe1\u53d1\u9001\u53c2\u6570")
public class SmsValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="scene\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u573a\u666f\u7801")
    private @NotNull(message="scene\u53c2\u6570\u7f3a\u5931") String scene;
    @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=11, max=11, message="\u624b\u673a\u53f7\u53ea\u80fd\u4e3a11\u4f4d")
    @Pattern(regexp="^[1][3,4,5,6,7,8,9][0-9]{9}$", message="\u624b\u673a\u53f7\u683c\u5f0f\u6709\u8bef")
    @ApiModelProperty(value="\u624b\u673a\u53f7")
    private @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=11, max=11, message="\u624b\u673a\u53f7\u53ea\u80fd\u4e3a11\u4f4d") @Pattern(regexp="^[1][3,4,5,6,7,8,9][0-9]{9}$", message="\u624b\u673a\u53f7\u683c\u5f0f\u6709\u8bef") String mobile;

    @NotNull(message="scene\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="scene\u53c2\u6570\u7f3a\u5931") String getScene() {
        return this.scene;
    }

    @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931") String getMobile() {
        return this.mobile;
    }

    public void setScene(@NotNull(message="scene\u53c2\u6570\u7f3a\u5931") @NotNull(message="scene\u53c2\u6570\u7f3a\u5931") String scene) {
        this.scene = scene;
    }

    public void setMobile(@NotNull(message="mobile\u53c2\u6570\u7f3a\u5931") @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931") String mobile) {
        this.mobile = mobile;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SmsValidate)) {
            return false;
        }
        SmsValidate other = (SmsValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$scene = this.getScene();
        String other$scene = other.getScene();
        if (this$scene == null ? other$scene != null : !this$scene.equals(other$scene)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        return !(this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SmsValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $scene = this.getScene();
        result = result * 59 + ($scene == null ? 43 : $scene.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        return result;
    }

    public String toString() {
        return "SmsValidate(scene=" + this.getScene() + ", mobile=" + this.getMobile() + ")";
    }
}

