/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.system.SystemAuthRoleVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7cfb\u7edf\u89d2\u8272Vo")
public class SystemAuthRoleVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u89d2\u8272\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u89d2\u8272\u5907\u6ce8")
    private String desc;
    @ApiModelProperty(value="\u5173\u8054\u83dc\u5355")
    private Object menusId;
    @ApiModelProperty(value="\u6210\u5458\u6570\u91cf")
    private Integer num;
    @ApiModelProperty(value="\u89d2\u8272\u6392\u5e8f")
    private Integer sort;
    @ApiModelProperty(value="\u662f\u5426\u7981\u7528: [0=\u5426, 1=\u662f]")
    private Integer isDisable;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private String updateTime;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public Object getMenusId() {
        return this.menusId;
    }

    public Integer getNum() {
        return this.num;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Integer getIsDisable() {
        return this.isDisable;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setMenusId(Object menusId) {
        this.menusId = menusId;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemAuthRoleVo)) {
            return false;
        }
        SystemAuthRoleVo other = (SystemAuthRoleVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$num = this.getNum();
        Integer other$num = other.getNum();
        if (this$num == null ? other$num != null : !((Object)this$num).equals(other$num)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Integer this$isDisable = this.getIsDisable();
        Integer other$isDisable = other.getIsDisable();
        if (this$isDisable == null ? other$isDisable != null : !((Object)this$isDisable).equals(other$isDisable)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$desc = this.getDesc();
        String other$desc = other.getDesc();
        if (this$desc == null ? other$desc != null : !this$desc.equals(other$desc)) {
            return false;
        }
        Object this$menusId = this.getMenusId();
        Object other$menusId = other.getMenusId();
        if (this$menusId == null ? other$menusId != null : !this$menusId.equals(other$menusId)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        String this$updateTime = this.getUpdateTime();
        String other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !this$updateTime.equals(other$updateTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemAuthRoleVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $num = this.getNum();
        result = result * 59 + ($num == null ? 43 : ((Object)$num).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $isDisable = this.getIsDisable();
        result = result * 59 + ($isDisable == null ? 43 : ((Object)$isDisable).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $desc = this.getDesc();
        result = result * 59 + ($desc == null ? 43 : $desc.hashCode());
        Object $menusId = this.getMenusId();
        result = result * 59 + ($menusId == null ? 43 : $menusId.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "SystemAuthRoleVo(id=" + this.getId() + ", name=" + this.getName() + ", desc=" + this.getDesc() + ", menusId=" + this.getMenusId() + ", num=" + this.getNum() + ", sort=" + this.getSort() + ", isDisable=" + this.getIsDisable() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

