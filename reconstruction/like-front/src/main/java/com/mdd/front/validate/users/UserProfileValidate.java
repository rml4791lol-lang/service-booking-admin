package com.mdd.front.validate.users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 家长资料保存参数（GET/POST /api/user/profile）。
 */
@ApiModel(value = "家长资料保存参数")
public class UserProfileValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请填写姓名")
    @Size(max = 32, message = "姓名最多 32 个字符")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @Pattern(regexp = "^$|^1\\d{10}$", message = "请填写正确的手机号")
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "性别：男/女/未知")
    private String gender;

    @Size(max = 255, message = "头像地址过长")
    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "常去小区ID")
    private String areaId;

    @Size(max = 255, message = "备注最多 255 个字符")
    @ApiModelProperty(value = "备注")
    private String note;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
