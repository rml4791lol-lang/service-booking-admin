/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.admin.service.IChannelOaMenusService
 *  com.mdd.admin.service.impl.ChannelOaMenusServiceImpl
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.ListUtils
 *  com.mdd.common.util.MapUtils
 *  me.chanjar.weixin.common.bean.menu.WxMenu
 *  me.chanjar.weixin.common.bean.menu.WxMenuButton
 *  me.chanjar.weixin.common.error.WxErrorException
 *  me.chanjar.weixin.mp.api.WxMpService
 *  me.chanjar.weixin.mp.api.impl.WxMpMenuServiceImpl
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.admin.service.IChannelOaMenusService;
import com.mdd.common.exception.OperateException;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.ListUtils;
import com.mdd.common.util.MapUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpMenuServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ChannelOaMenusServiceImpl
implements IChannelOaMenusService {
    public JSONArray detail() {
        String json = ConfigUtils.get((String)"oa_setting", (String)"menus", (String)"[]");
        return JSONArray.parseArray((String)json, (JSONReader.Feature[])new JSONReader.Feature[0]);
    }

    public void save(List<Object> objs, Boolean isPublish) {
        if (objs.size() > 3) {
            throw new OperateException("\u4e00\u7ea7\u83dc\u5355\u8d85\u51fa\u9650\u5236(\u6700\u591a3\u4e2a)");
        }
        LinkedList<Map<String, String>> params = new LinkedList<Map<String, String>>();
        for (Object object : objs) {
            params.add(MapUtils.objectToMap((Object)object));
        }
        LinkedList<WxMenuButton> menuButtons = new LinkedList<WxMenuButton>();
        for (Map<String, String> item : params) {
            Assert.notNull(item.get("name"), (String)"\u4e00\u7ea7\u83dc\u5355\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a", (Object[])new Object[0]);
            WxMenuButton wxMenuButton = new WxMenuButton();
            wxMenuButton.setName((String)item.get("name"));
            if (String.valueOf(item.get("has_menu")).equals("false")) {
                Assert.notNull(item.get("type"), (String)"\u4e00\u7ea7\u83dc\u5355type\u53c2\u6570\u7f3a\u5931", (Object[])new Object[0]);
                if (((String)item.get("type")).equals("miniprogram")) {
                    Assert.notNull(item.get("appid"), (String)"\u4e00\u7ea7\u83dc\u5355appId\u53c2\u6570\u7f3a\u5931", (Object[])new Object[0]);
                    Assert.notNull(item.get("url"), (String)"\u4e00\u7ea7\u83dc\u5355url\u6570\u7f3a\u5931", (Object[])new Object[0]);
                    Assert.notNull(item.get("pagepath"), (String)"\u4e00\u7ea7\u83dc\u5355pagePath\u6570\u7f3a\u5931", (Object[])new Object[0]);
                    wxMenuButton.setType((String)item.get("type"));
                    wxMenuButton.setAppId((String)item.get("appid"));
                    wxMenuButton.setUrl((String)item.get("url"));
                    wxMenuButton.setPagePath((String)item.get("pagepath"));
                } else {
                    Assert.notNull(item.get("url"), (String)"\u4e00\u7ea7\u83dc\u5355url\u6570\u7f3a\u5931", (Object[])new Object[0]);
                    wxMenuButton.setType((String)item.get("type"));
                    wxMenuButton.setUrl((String)item.get("url"));
                    wxMenuButton.setAppId(item.getOrDefault("appid", ""));
                }
                menuButtons.add(wxMenuButton);
            }
            if (!String.valueOf(item.get("has_menu")).equals("true")) continue;
            Assert.notNull(item.get("sub_button"), (String)"\u5b50\u7ea7\u83dc\u5355\u4e0d\u80fd\u4e3a\u7a7a", (Object[])new Object[0]);
            List<Map<String, String>> subButtons = ListUtils.stringToListAsMapStr((String)((String)item.get("sub_button")));
            ArrayList<WxMenuButton> subMenuButtons = new ArrayList<WxMenuButton>();
            if (subButtons.size() > 5) {
                throw new OperateException("\u5b50\u7ea7\u83dc\u5355\u8d85\u51fa\u9650\u5236(\u6700\u591a5\u4e2a)");
            }
            for (Map<String, String> subItem : subButtons) {
                WxMenuButton subMenuButton = new WxMenuButton();
                Assert.notNull(subItem.get("type"), (String)"\u5b50\u7ea7\u83dc\u5355type\u53c2\u6570\u7f3a\u5931!", (Object[])new Object[0]);
                if (((String)subItem.get("type")).equals("miniprogram")) {
                    Assert.notNull(subItem.get("appid"), (String)"\u5b50\u7ea7\u83dc\u5355appId\u53c2\u6570\u7f3a\u5931!", (Object[])new Object[0]);
                    Assert.notNull(subItem.get("url"), (String)"\u5b50\u7ea7\u83dc\u5355url\u6570\u7f3a\u5931!", (Object[])new Object[0]);
                    Assert.notNull(subItem.get("pagepath"), (String)"\u5b50\u7ea7\u83dc\u5355pagePath\u6570\u7f3a\u5931!", (Object[])new Object[0]);
                    subMenuButton.setType((String)subItem.get("type"));
                    subMenuButton.setName((String)subItem.get("name"));
                    subMenuButton.setAppId((String)subItem.get("appid"));
                    subMenuButton.setUrl((String)subItem.get("url"));
                    subMenuButton.setPagePath((String)subItem.get("pagepath"));
                } else {
                    Assert.notNull(subItem.get("url"), (String)"\u5b50\u7ea7\u83dc\u5355url\u6570\u7f3a\u5931", (Object[])new Object[0]);
                    subMenuButton.setType((String)subItem.get("type"));
                    subMenuButton.setName((String)subItem.get("name"));
                    subMenuButton.setUrl((String)subItem.get("url"));
                }
                subMenuButtons.add(subMenuButton);
            }
            wxMenuButton.setType("click");
            wxMenuButton.setSubButtons(subMenuButtons);
            menuButtons.add(wxMenuButton);
        }
        ConfigUtils.set((String)"oa_setting", (String)"menus", (String)JSON.toJSONString(objs));
        if (isPublish.booleanValue()) {
            try {
                WxMenu wxMenu = new WxMenu();
                wxMenu.setButtons(menuButtons);
                WxMpService wxMpService = WxMnpDriver.oa();
                WxMpMenuServiceImpl wxMpMenuService = new WxMpMenuServiceImpl(wxMpService);
                wxMpMenuService.menuCreate(wxMenu);
            }
            catch (WxErrorException wxErrorException) {
                throw new OperateException(wxErrorException.getError().getErrorCode() + ": " + wxErrorException.getError().getErrorMsg());
            }
        }
    }
}

