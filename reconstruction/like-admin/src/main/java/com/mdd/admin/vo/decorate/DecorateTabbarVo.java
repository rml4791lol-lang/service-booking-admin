/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.decorate.DecorateTabbarVo
 *  com.mdd.admin.vo.decorate.DecorateTabsListsVo
 *  com.mdd.admin.vo.decorate.DecorateTabsStyleVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.decorate;

import com.mdd.admin.vo.decorate.DecorateTabsListsVo;
import com.mdd.admin.vo.decorate.DecorateTabsStyleVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value="\u88c5\u4fee\u5e95\u90e8\u5bfc\u822aVo")
public class DecorateTabbarVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u6837\u5f0f")
    private DecorateTabsStyleVo style;
    @ApiModelProperty(value="\u5217\u8868")
    private List<DecorateTabsListsVo> list;

    public DecorateTabsStyleVo getStyle() {
        return this.style;
    }

    public List<DecorateTabsListsVo> getList() {
        return this.list;
    }

    public void setStyle(DecorateTabsStyleVo style) {
        this.style = style;
    }

    public void setList(List<DecorateTabsListsVo> list) {
        this.list = list;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DecorateTabbarVo)) {
            return false;
        }
        DecorateTabbarVo other = (DecorateTabbarVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        DecorateTabsStyleVo this$style = this.getStyle();
        DecorateTabsStyleVo other$style = other.getStyle();
        if (this$style == null ? other$style != null : !this$style.equals(other$style)) {
            return false;
        }
        List this$list = this.getList();
        List other$list = other.getList();
        return !(this$list == null ? other$list != null : !((Object)this$list).equals(other$list));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DecorateTabbarVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        DecorateTabsStyleVo $style = this.getStyle();
        result = result * 59 + ($style == null ? 43 : $style.hashCode());
        List $list = this.getList();
        result = result * 59 + ($list == null ? 43 : ((Object)$list).hashCode());
        return result;
    }

    public String toString() {
        return "DecorateTabbarVo(style=" + this.getStyle() + ", list=" + this.getList() + ")";
    }
}

