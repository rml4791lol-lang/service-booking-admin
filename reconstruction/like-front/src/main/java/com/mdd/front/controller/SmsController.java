/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.entity.smsLog.SmsLog
 *  com.mdd.common.enums.NoticeEnum
 *  com.mdd.common.enums.SmsEnum
 *  com.mdd.common.enums.YesNoEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.smsLog.SmsLogMapper
 *  com.mdd.common.plugin.notice.NoticeDriver
 *  com.mdd.common.plugin.notice.vo.NoticeSmsVo
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.ToolUtils
 *  com.mdd.front.controller.SmsController
 *  com.mdd.front.validate.common.SmsValidate
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.entity.smsLog.SmsLog;
import com.mdd.common.enums.NoticeEnum;
import com.mdd.common.enums.SmsEnum;
import com.mdd.common.enums.YesNoEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.smsLog.SmsLogMapper;
import com.mdd.common.plugin.notice.NoticeDriver;
import com.mdd.common.plugin.notice.vo.NoticeSmsVo;
import com.mdd.common.util.IpUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.front.validate.common.SmsValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collection;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/sms"})
@Api(tags={"\u4e3b\u9875\u7ba1\u7406"})
public class SmsController {
    @Resource
    SmsLogMapper smsLogMapper;

    @NotLogin
    @PostMapping(value={"/sendCode"})
    @ApiOperation(value="\u53d1\u9001\u77ed\u4fe1")
    public AjaxResult<Object> sendSms(@Validated @RequestBody SmsValidate smsValidate, HttpServletRequest request) {
        String ip = IpUtils.getIpAddress();
        String ipKey = "sms:send:ip:" + Integer.toHexString((ip == null ? "" : ip).hashCode());
        Long ipCount = RedisUtils.incr(ipKey, 1L);
        if (ipCount != null && ipCount == 1L) {
            RedisUtils.expire(ipKey, 600L);
        }
        if (ipCount != null && ipCount > 10L) {
            throw new OperateException("验证码发送过于频繁，请稍后再试");
        }
        QueryWrapper<SmsLog> smsLogQueryWrapper = new QueryWrapper<SmsLog>();
        smsLogQueryWrapper.eq("mobile", (Object)smsValidate.getMobile());
        smsLogQueryWrapper.eq("send_status", (Object)SmsEnum.SEND_SUCCESS.getCode());
        smsLogQueryWrapper.in("scene_id", (Collection)NoticeEnum.getSmsScene());
        smsLogQueryWrapper.eq("is_verify", (Object)YesNoEnum.NO.getCode());
        if (StringUtils.isNotNull(smsValidate.getScene())) {
            smsLogQueryWrapper.eq("scene_id", (Object)NoticeEnum.getSceneByTag((String)smsValidate.getScene()));
        }
        smsLogQueryWrapper.orderByDesc("send_time");
        smsLogQueryWrapper.last("limit 1");
        SmsLog smsLog = (SmsLog)this.smsLogMapper.selectOne((Wrapper)smsLogQueryWrapper);
        if (StringUtils.isNotNull(smsLog) && smsLog.getSendTime() + 300L > System.currentTimeMillis() / 1000L) {
            throw new OperateException("\u5df2\u6709\u77ed\u4fe1\u8bb0\u5f55\uff0c\u8bf7\u52ff\u91cd\u590d\u53d1\u9001");
        }
        // 6 位验证码显著降低在线枚举空间；尝试上限在 NoticeCheck 中原子校验。
        String code = ToolUtils.randomInt((int)6);
        NoticeSmsVo params = new NoticeSmsVo().setScene(NoticeEnum.getSceneByTag((String)smsValidate.getScene())).setMobile(smsValidate.getMobile()).setExpire(Integer.valueOf(900)).setParams(new String[]{"code:" + code}).setCode(code);
        NoticeDriver.handle((NoticeSmsVo)params);
        return AjaxResult.success();
    }
}
