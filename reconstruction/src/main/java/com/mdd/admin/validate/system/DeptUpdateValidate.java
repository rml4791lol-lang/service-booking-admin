/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.DeptUpdateValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.DecimalMax
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Pattern
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.system;

import com.mdd.common.validator.annotation.IDMust;
import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u7cfb\u7edf\u90e8\u95e8\u66f4\u65b0\u53c2\u6570")
public class DeptUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="id", required=true)
    private Integer id;
    @NotNull(message="pid\u53c2\u6570\u7f3a\u5931")
    @DecimalMin(value="0", message="\u4e0a\u7ea7\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u4e0a\u7ea7ID", required=true)
    private @NotNull(message="pid\u53c2\u6570\u7f3a\u5931") @DecimalMin(value="0", message="\u4e0a\u7ea7\u503c\u4e0d\u80fd\u5c11\u4e8e0") Integer pid;
    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    @Length(min=1, max=100, message="\u90e8\u95e8\u540d\u79f0\u5fc5\u987b\u57281~100\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u90e8\u95e8\u540d\u79f0")
    private @NotNull(message="name\u53c2\u6570\u7f3a\u5931") @Length(min=1, max=100, message="\u90e8\u95e8\u540d\u79f0\u5fc5\u987b\u57281~100\u4e2a\u5b57\u7b26\u5185") String name;
    @Length(min=1, max=30, message="\u8d1f\u8d23\u4eba\u540d\u79f0\u5fc5\u987b\u57281~30\u4e2a\u5b57\u7b26\u5185")
    private @Length(min=1, max=30, message="\u8d1f\u8d23\u4eba\u540d\u79f0\u5fc5\u987b\u57281~30\u4e2a\u5b57\u7b26\u5185") String leader = "";
    @Length(min=11, max=11, message="\u624b\u673a\u53f7\u53ea\u80fd\u4e3a11\u4f4d")
    @Pattern(regexp="^[1][3,4,5,6,7,8,9][0-9]{9}$", message="\u624b\u673a\u53f7\u683c\u5f0f\u6709\u8bef")
    @ApiModelProperty(value="\u624b\u673a\u53f7\u7801")
    private @Length(min=11, max=11, message="\u624b\u673a\u53f7\u53ea\u80fd\u4e3a11\u4f4d") @Pattern(regexp="^[1][3,4,5,6,7,8,9][0-9]{9}$", message="\u624b\u673a\u53f7\u683c\u5f0f\u6709\u8bef") String mobile;
    @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001")
    @IntegerContains(values={0, 1})
    @ApiModelProperty(value="\u72b6\u6001")
    private @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001") Integer status;
    @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @DecimalMax(value="9999", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5927\u4e8e9999")
    @ApiModelProperty(value="\u6392\u5e8f")
    private @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0") @DecimalMax(value="9999", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5927\u4e8e9999") Integer sort;

    public Integer getId() {
        return this.id;
    }

    @NotNull(message="pid\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="pid\u53c2\u6570\u7f3a\u5931") Integer getPid() {
        return this.pid;
    }

    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String getName() {
        return this.name;
    }

    public String getLeader() {
        return this.leader;
    }

    public String getMobile() {
        return this.mobile;
    }

    @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001")
    public @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001") Integer getStatus() {
        return this.status;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPid(@NotNull(message="pid\u53c2\u6570\u7f3a\u5931") @NotNull(message="pid\u53c2\u6570\u7f3a\u5931") Integer pid) {
        this.pid = pid;
    }

    public void setName(@NotNull(message="name\u53c2\u6570\u7f3a\u5931") @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name) {
        this.name = name;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setStatus(@NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001") @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001") Integer status) {
        this.status = status;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeptUpdateValidate)) {
            return false;
        }
        DeptUpdateValidate other = (DeptUpdateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$pid = this.getPid();
        Integer other$pid = other.getPid();
        if (this$pid == null ? other$pid != null : !((Object)this$pid).equals(other$pid)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$leader = this.getLeader();
        String other$leader = other.getLeader();
        if (this$leader == null ? other$leader != null : !this$leader.equals(other$leader)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        return !(this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeptUpdateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $pid = this.getPid();
        result = result * 59 + ($pid == null ? 43 : ((Object)$pid).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $leader = this.getLeader();
        result = result * 59 + ($leader == null ? 43 : $leader.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        return result;
    }

    public String toString() {
        return "DeptUpdateValidate(id=" + this.getId() + ", pid=" + this.getPid() + ", name=" + this.getName() + ", leader=" + this.getLeader() + ", mobile=" + this.getMobile() + ", status=" + this.getStatus() + ", sort=" + this.getSort() + ")";
    }
}

