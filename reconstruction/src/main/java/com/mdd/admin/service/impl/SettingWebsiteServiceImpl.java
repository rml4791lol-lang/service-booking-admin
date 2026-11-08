/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingWebsiteService
 *  com.mdd.admin.service.impl.SettingWebsiteServiceImpl
 *  com.mdd.admin.validate.setting.SettingWebsiteValidate
 *  com.mdd.admin.vo.setting.SettingWebsiteVo
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.UrlUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.mdd.admin.service.ISettingWebsiteService;
import com.mdd.admin.validate.setting.SettingWebsiteValidate;
import com.mdd.admin.vo.setting.SettingWebsiteVo;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.UrlUtils;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SettingWebsiteServiceImpl
implements ISettingWebsiteService {
    public SettingWebsiteVo getWebsite() {
        Map<String, String> config = ConfigUtils.get((String)"website");
        SettingWebsiteVo vo = new SettingWebsiteVo();
        vo.setName(config.getOrDefault("name", ""));
        vo.setWebFavicon(UrlUtils.toAdminAbsoluteUrl((String)config.getOrDefault("web_favicon", "")));
        vo.setWebLogo(UrlUtils.toAdminAbsoluteUrl((String)config.getOrDefault("web_logo", "")));
        vo.setLoginImage(UrlUtils.toAdminAbsoluteUrl((String)config.getOrDefault("login_image", "")));
        vo.setShopName(config.getOrDefault("shop_name", ""));
        vo.setShopLogo(UrlUtils.toAdminAbsoluteUrl((String)config.getOrDefault("shop_logo", "")));
        vo.setPcLogo(UrlUtils.toAdminAbsoluteUrl((String)config.getOrDefault("pc_logo", "")));
        vo.setPcTitle(config.getOrDefault("pc_title", ""));
        vo.setPcIco(UrlUtils.toAdminAbsoluteUrl((String)config.getOrDefault("pc_ico", "")));
        vo.setPcDesc(config.getOrDefault("pc_desc", ""));
        vo.setPcKeywords(config.getOrDefault("pc_keywords", ""));
        vo.setH5Favicon(UrlUtils.toAdminAbsoluteUrl((String)config.getOrDefault("h5_favicon", "")));
        return vo;
    }

    public void setWebsite(SettingWebsiteValidate websiteValidate) {
        ConfigUtils.set((String)"website", (String)"name", (String)websiteValidate.getName());
        ConfigUtils.set((String)"website", (String)"web_logo", (String)UrlUtils.toRelativeUrl((String)websiteValidate.getWebLogo()));
        ConfigUtils.set((String)"website", (String)"web_favicon", (String)UrlUtils.toRelativeUrl((String)websiteValidate.getWebFavicon()));
        ConfigUtils.set((String)"website", (String)"login_image", (String)UrlUtils.toRelativeUrl((String)websiteValidate.getLoginImage()));
        ConfigUtils.set((String)"website", (String)"h5_favicon", (String)UrlUtils.toRelativeUrl((String)websiteValidate.getH5Favicon()));
        ConfigUtils.set((String)"website", (String)"shop_name", (String)websiteValidate.getShopName());
        ConfigUtils.set((String)"website", (String)"shop_logo", (String)UrlUtils.toRelativeUrl((String)websiteValidate.getShopLogo()));
        ConfigUtils.set((String)"website", (String)"pc_logo", (String)UrlUtils.toRelativeUrl((String)websiteValidate.getPcLogo()));
        ConfigUtils.set((String)"website", (String)"pc_ico", (String)UrlUtils.toRelativeUrl((String)websiteValidate.getPcIco()));
        ConfigUtils.set((String)"website", (String)"pc_title", (String)websiteValidate.getPcTitle());
        ConfigUtils.set((String)"website", (String)"pc_desc", (String)websiteValidate.getPcDesc());
        ConfigUtils.set((String)"website", (String)"pc_keywords", (String)websiteValidate.getPcKeywords());
    }
}

