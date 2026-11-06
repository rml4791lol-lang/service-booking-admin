/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.decorate.DecorateTabsListsVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.decorate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u88c5\u4fee\u5e95\u90e8\u5bfc\u822a\u5217\u8868Vo")
public class DecorateTabsListsVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="id")
    private Integer id;
    @ApiModelProperty(value="\u5bfc\u822a\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u5df2\u9009\u56fe\u6807")
    private String selected;
    @ApiModelProperty(value="\u672a\u9009\u56fe\u6807")
    private String unselected;
    @ApiModelProperty(value="id")
    private Object link;
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a")
    private Integer isShow;
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

    public String getSelected() {
        return this.selected;
    }

    public String getUnselected() {
        return this.unselected;
    }

    public Object getLink() {
        return this.link;
    }

    public Integer getIsShow() {
        return this.isShow;
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

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void setUnselected(String unselected) {
        this.unselected = unselected;
    }

    public void setLink(Object link) {
        this.link = link;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
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
        if (!(o instanceof DecorateTabsListsVo)) {
            return false;
        }
        DecorateTabsListsVo other = (DecorateTabsListsVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$isShow = this.getIsShow();
        Integer other$isShow = other.getIsShow();
        if (this$isShow == null ? other$isShow != null : !((Object)this$isShow).equals(other$isShow)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$selected = this.getSelected();
        String other$selected = other.getSelected();
        if (this$selected == null ? other$selected != null : !this$selected.equals(other$selected)) {
            return false;
        }
        String this$unselected = this.getUnselected();
        String other$unselected = other.getUnselected();
        if (this$unselected == null ? other$unselected != null : !this$unselected.equals(other$unselected)) {
            return false;
        }
        Object this$link = this.getLink();
        Object other$link = other.getLink();
        if (this$link == null ? other$link != null : !this$link.equals(other$link)) {
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
        return other instanceof DecorateTabsListsVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $selected = this.getSelected();
        result = result * 59 + ($selected == null ? 43 : $selected.hashCode());
        String $unselected = this.getUnselected();
        result = result * 59 + ($unselected == null ? 43 : $unselected.hashCode());
        Object $link = this.getLink();
        result = result * 59 + ($link == null ? 43 : $link.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "DecorateTabsListsVo(id=" + this.getId() + ", name=" + this.getName() + ", selected=" + this.getSelected() + ", unselected=" + this.getUnselected() + ", link=" + this.getLink() + ", isShow=" + this.getIsShow() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

