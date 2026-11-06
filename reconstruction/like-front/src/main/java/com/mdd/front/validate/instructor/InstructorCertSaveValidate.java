package com.mdd.front.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 教练资质保存草稿参数（#49）。
 *
 * <p>草稿允许字段不完整；提交审核前由服务端再做一次完整性与有效期校验。
 * 有效期统一 yyyy-MM-dd，页面日期选择器与后端 DATE 字段对齐。</p>
 */
@ApiModel(value = "教练资质保存参数")
public class InstructorCertSaveValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资质ID，新增时为空")
    private Long id;

    @NotBlank(message = "请选择证书类型")
    @Size(max = 50, message = "证书类型不能超过50个字符")
    @ApiModelProperty(value = "证书类型")
    private String type;

    @NotBlank(message = "请填写证书名称")
    @Size(max = 100, message = "证书名称不能超过100个字符")
    @ApiModelProperty(value = "证书名称")
    private String name;

    @Size(max = 100, message = "证书编号不能超过100个字符")
    @ApiModelProperty(value = "证书编号")
    private String no;

    @Size(max = 100, message = "发证机构不能超过100个字符")
    @ApiModelProperty(value = "发证机构")
    private String org;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "有效期起格式应为yyyy-MM-dd")
    @ApiModelProperty(value = "有效期起")
    private String expireStart;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "有效期止格式应为yyyy-MM-dd")
    @ApiModelProperty(value = "有效期止")
    private String expire;

    @Size(max = 500, message = "证书照片地址过长")
    @ApiModelProperty(value = "证书照片")
    private String front;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getExpireStart() {
        return expireStart;
    }

    public void setExpireStart(String expireStart) {
        this.expireStart = expireStart;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }
}
