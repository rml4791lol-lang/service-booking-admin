/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.binarywang.wx.miniapp.api.WxMaService
 *  cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage
 *  cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage$MsgData
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.entity.user.UserAuth
 *  com.mdd.common.mapper.user.UserAuthMapper
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.front.controller.TestNotifyController
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.entity.user.UserAuth;
import com.mdd.common.mapper.user.UserAuthMapper;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.config.SubscribeMessageConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/test-notify"})
@Api(tags={"\u6d4b\u8bd5-\u8ba2\u9605\u6d88\u606f\u53d1\u9001"})
@ConditionalOnProperty(prefix="like.test-notify", name="enabled", havingValue="true")
public class TestNotifyController {
    @Resource
    private UserAuthMapper userAuthMapper;
    @Resource
    private SubscribeMessageConfig subscribeMessageConfig;

    @PostMapping(value={"/sendReminder"})
    @ApiOperation(value="\u53d1\u9001\u8bfe\u524d\u63d0\u9192\u8ba2\u9605\u6d88\u606f")
    public AjaxResult<Object> sendReminder(@RequestBody Map<String, Object> params) {
        UserAuth userAuth;
        Integer userId = LikeFrontThreadLocal.getUserId();
        if (userId == null) {
            return AjaxResult.failed((Integer)ErrorEnum.TOKEN_INVALID.getCode(), "请先登录");
        }
        String studentName = (String)params.get("studentName");
        String className = (String)params.get("className");
        String timeSlot = (String)params.get("timeSlot");
        String venue = (String)params.get("venue");
        String lessonDate = (String)params.get("lessonDate");
        if (!this.subscribeMessageConfig.hasLessonReminderTemplate()) {
            return AjaxResult.failed((String)"\u8bfe\u524d\u63d0\u9192\u6a21\u677f\u672a\u914d\u7f6e");
        }
        List authList = this.userAuthMapper.selectList(new QueryWrapper<UserAuth>().eq("user_id", (Object)userId).eq("terminal", (Object)1));
        UserAuth userAuth2 = userAuth = authList != null && !authList.isEmpty() ? (UserAuth)authList.get(0) : null;
        if (userAuth == null || userAuth.getOpenid() == null) {
            return AjaxResult.failed((String)"\u7528\u6237\u5fae\u4fe1\u6388\u6743\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        try {
            WxMaService wxMaService = WxMnpDriver.mnp();
            String startTime = timeSlot;
            if (timeSlot != null && timeSlot.contains("-")) {
                startTime = timeSlot.split("-")[0].trim();
            }
            ArrayList<WxMaSubscribeMessage.MsgData> dataList = new ArrayList<WxMaSubscribeMessage.MsgData>();
            dataList.add(new WxMaSubscribeMessage.MsgData("thing1", studentName != null ? studentName : "\u5b66\u5458"));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing2", className != null ? className : ""));
            dataList.add(new WxMaSubscribeMessage.MsgData("time3", (lessonDate != null ? lessonDate : "") + " " + (startTime != null ? startTime : "")));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing4", venue != null ? venue : "\u65e0"));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing10", "\u5982\u60a8\u60f3\u4e0a\u6b64\u8bfe\u7a0b\uff0c\u8bf7\u5230\u5c0f\u7a0b\u5e8f\u9884\u7ea6\u5f53\u524d\u8bfe\u65f6"));
            WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
                    .toUser(userAuth.getOpenid())
                    .templateId(this.subscribeMessageConfig.getLessonReminderTemplateId())
                    .page(this.subscribeMessageConfig.getPage())
                    .data(dataList)
                    .build();
            wxMaService.getMsgService().sendSubscribeMsg(message);
            return AjaxResult.success((String)"\u53d1\u9001\u6210\u529f");
        }
        catch (Exception e) {
            return AjaxResult.failed((String)("\u53d1\u9001\u5931\u8d25: " + e.getMessage()));
        }
    }

    @PostMapping(value={"/sendCancel"})
    @ApiOperation(value="\u53d1\u9001\u8bfe\u65f6\u53d6\u6d88\u8ba2\u9605\u6d88\u606f")
    public AjaxResult<Object> sendCancel(@RequestBody Map<String, Object> params) {
        UserAuth userAuth;
        Integer userId = LikeFrontThreadLocal.getUserId();
        if (userId == null) {
            return AjaxResult.failed((Integer)ErrorEnum.TOKEN_INVALID.getCode(), "请先登录");
        }
        String studentName = (String)params.get("studentName");
        String className = (String)params.get("className");
        String venue = (String)params.get("venue");
        String coachName = (String)params.get("coachName");
        if (!this.subscribeMessageConfig.hasLessonCancelTemplate()) {
            return AjaxResult.failed((String)"\u8bfe\u7a0b\u53d6\u6d88\u6a21\u677f\u672a\u914d\u7f6e");
        }
        List authList = this.userAuthMapper.selectList(new QueryWrapper<UserAuth>().eq("user_id", (Object)userId).eq("terminal", (Object)1));
        UserAuth userAuth2 = userAuth = authList != null && !authList.isEmpty() ? (UserAuth)authList.get(0) : null;
        if (userAuth == null || userAuth.getOpenid() == null) {
            return AjaxResult.failed((String)"\u7528\u6237\u5fae\u4fe1\u6388\u6743\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        try {
            WxMaService wxMaService = WxMnpDriver.mnp();
            ArrayList<WxMaSubscribeMessage.MsgData> dataList = new ArrayList<WxMaSubscribeMessage.MsgData>();
            dataList.add(new WxMaSubscribeMessage.MsgData("thing2", studentName != null ? studentName : "\u672a\u8bbe\u7f6e"));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing3", className != null ? className : ""));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing4", "\u62a5\u540d\u4eba\u6570\u4e0d\u8db3\uff0c\u8bfe\u65f6\u5df2\u53d6\u6d88"));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing6", venue != null ? venue : "\u65e0"));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing7", coachName != null ? coachName : "\u7cfb\u7edf"));
            WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
                    .toUser(userAuth.getOpenid())
                    .templateId(this.subscribeMessageConfig.getLessonCancelTemplateId())
                    .page(this.subscribeMessageConfig.getPage())
                    .data(dataList)
                    .build();
            wxMaService.getMsgService().sendSubscribeMsg(message);
            return AjaxResult.success((String)"\u53d1\u9001\u6210\u529f");
        }
        catch (Exception e) {
            return AjaxResult.failed((String)("\u53d1\u9001\u5931\u8d25: " + e.getMessage()));
        }
    }
}
