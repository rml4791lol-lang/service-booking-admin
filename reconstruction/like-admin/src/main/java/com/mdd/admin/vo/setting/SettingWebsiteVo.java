/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.setting.SettingWebsiteVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u57fa\u7840\u8bbe\u7f6eVo")
public class SettingWebsiteVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7f51\u7ad9\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u7f51\u7ad9logo")
    private String webLogo;
    @ApiModelProperty(value="\u7f51\u7ad9\u56fe\u6807")
    private String webFavicon;
    @ApiModelProperty(value="\u767b\u5f55\u9875\u5e7f\u544a\u56fe")
    private String loginImage;
    @ApiModelProperty(value="\u5546\u57ce\u540d\u79f0")
    private String shopName;
    @ApiModelProperty(value="\u5546\u57ceLOGO")
    private String shopLogo;
    @ApiModelProperty(value="PC\u7aefLOGO")
    private String pcLogo;
    @ApiModelProperty(value="\u7f51\u7ad9\u6807\u9898")
    private String pcTitle;
    @ApiModelProperty(value="\u7f51\u7ad9\u56fe\u6807")
    private String pcIco;
    @ApiModelProperty(value="\u7f51\u7ad9\u63cf\u8ff0")
    private String pcDesc;
    @ApiModelProperty(value="\u7f51\u7ad9\u5173\u952e\u8bcd")
    private String pcKeywords;
    @ApiModelProperty(value="h5\u56fe\u6807")
    private String h5Favicon;

    public String getName() {
        return this.name;
    }

    public String getWebLogo() {
        return this.webLogo;
    }

    public String getWebFavicon() {
        return this.webFavicon;
    }

    public String getLoginImage() {
        return this.loginImage;
    }

    public String getShopName() {
        return this.shopName;
    }

    public String getShopLogo() {
        return this.shopLogo;
    }

    public String getPcLogo() {
        return this.pcLogo;
    }

    public String getPcTitle() {
        return this.pcTitle;
    }

    public String getPcIco() {
        return this.pcIco;
    }

    public String getPcDesc() {
        return this.pcDesc;
    }

    public String getPcKeywords() {
        return this.pcKeywords;
    }

    public String getH5Favicon() {
        return this.h5Favicon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWebLogo(String webLogo) {
        this.webLogo = webLogo;
    }

    public void setWebFavicon(String webFavicon) {
        this.webFavicon = webFavicon;
    }

    public void setLoginImage(String loginImage) {
        this.loginImage = loginImage;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public void setPcLogo(String pcLogo) {
        this.pcLogo = pcLogo;
    }

    public void setPcTitle(String pcTitle) {
        this.pcTitle = pcTitle;
    }

    public void setPcIco(String pcIco) {
        this.pcIco = pcIco;
    }

    public void setPcDesc(String pcDesc) {
        this.pcDesc = pcDesc;
    }

    public void setPcKeywords(String pcKeywords) {
        this.pcKeywords = pcKeywords;
    }

    public void setH5Favicon(String h5Favicon) {
        this.h5Favicon = h5Favicon;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingWebsiteVo)) {
            return false;
        }
        SettingWebsiteVo other = (SettingWebsiteVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$webLogo = this.getWebLogo();
        String other$webLogo = other.getWebLogo();
        if (this$webLogo == null ? other$webLogo != null : !this$webLogo.equals(other$webLogo)) {
            return false;
        }
        String this$webFavicon = this.getWebFavicon();
        String other$webFavicon = other.getWebFavicon();
        if (this$webFavicon == null ? other$webFavicon != null : !this$webFavicon.equals(other$webFavicon)) {
            return false;
        }
        String this$loginImage = this.getLoginImage();
        String other$loginImage = other.getLoginImage();
        if (this$loginImage == null ? other$loginImage != null : !this$loginImage.equals(other$loginImage)) {
            return false;
        }
        String this$shopName = this.getShopName();
        String other$shopName = other.getShopName();
        if (this$shopName == null ? other$shopName != null : !this$shopName.equals(other$shopName)) {
            return false;
        }
        String this$shopLogo = this.getShopLogo();
        String other$shopLogo = other.getShopLogo();
        if (this$shopLogo == null ? other$shopLogo != null : !this$shopLogo.equals(other$shopLogo)) {
            return false;
        }
        String this$pcLogo = this.getPcLogo();
        String other$pcLogo = other.getPcLogo();
        if (this$pcLogo == null ? other$pcLogo != null : !this$pcLogo.equals(other$pcLogo)) {
            return false;
        }
        String this$pcTitle = this.getPcTitle();
        String other$pcTitle = other.getPcTitle();
        if (this$pcTitle == null ? other$pcTitle != null : !this$pcTitle.equals(other$pcTitle)) {
            return false;
        }
        String this$pcIco = this.getPcIco();
        String other$pcIco = other.getPcIco();
        if (this$pcIco == null ? other$pcIco != null : !this$pcIco.equals(other$pcIco)) {
            return false;
        }
        String this$pcDesc = this.getPcDesc();
        String other$pcDesc = other.getPcDesc();
        if (this$pcDesc == null ? other$pcDesc != null : !this$pcDesc.equals(other$pcDesc)) {
            return false;
        }
        String this$pcKeywords = this.getPcKeywords();
        String other$pcKeywords = other.getPcKeywords();
        if (this$pcKeywords == null ? other$pcKeywords != null : !this$pcKeywords.equals(other$pcKeywords)) {
            return false;
        }
        String this$h5Favicon = this.getH5Favicon();
        String other$h5Favicon = other.getH5Favicon();
        return !(this$h5Favicon == null ? other$h5Favicon != null : !this$h5Favicon.equals(other$h5Favicon));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingWebsiteVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $webLogo = this.getWebLogo();
        result = result * 59 + ($webLogo == null ? 43 : $webLogo.hashCode());
        String $webFavicon = this.getWebFavicon();
        result = result * 59 + ($webFavicon == null ? 43 : $webFavicon.hashCode());
        String $loginImage = this.getLoginImage();
        result = result * 59 + ($loginImage == null ? 43 : $loginImage.hashCode());
        String $shopName = this.getShopName();
        result = result * 59 + ($shopName == null ? 43 : $shopName.hashCode());
        String $shopLogo = this.getShopLogo();
        result = result * 59 + ($shopLogo == null ? 43 : $shopLogo.hashCode());
        String $pcLogo = this.getPcLogo();
        result = result * 59 + ($pcLogo == null ? 43 : $pcLogo.hashCode());
        String $pcTitle = this.getPcTitle();
        result = result * 59 + ($pcTitle == null ? 43 : $pcTitle.hashCode());
        String $pcIco = this.getPcIco();
        result = result * 59 + ($pcIco == null ? 43 : $pcIco.hashCode());
        String $pcDesc = this.getPcDesc();
        result = result * 59 + ($pcDesc == null ? 43 : $pcDesc.hashCode());
        String $pcKeywords = this.getPcKeywords();
        result = result * 59 + ($pcKeywords == null ? 43 : $pcKeywords.hashCode());
        String $h5Favicon = this.getH5Favicon();
        result = result * 59 + ($h5Favicon == null ? 43 : $h5Favicon.hashCode());
        return result;
    }

    public String toString() {
        return "SettingWebsiteVo(name=" + this.getName() + ", webLogo=" + this.getWebLogo() + ", webFavicon=" + this.getWebFavicon() + ", loginImage=" + this.getLoginImage() + ", shopName=" + this.getShopName() + ", shopLogo=" + this.getShopLogo() + ", pcLogo=" + this.getPcLogo() + ", pcTitle=" + this.getPcTitle() + ", pcIco=" + this.getPcIco() + ", pcDesc=" + this.getPcDesc() + ", pcKeywords=" + this.getPcKeywords() + ", h5Favicon=" + this.getH5Favicon() + ")";
    }
}

