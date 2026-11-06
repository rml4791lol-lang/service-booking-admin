/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 */
package com.mdd.common.plugin.notice;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.common.entity.smsLog.SmsLog;
import com.mdd.common.enums.NoticeEnum;
import com.mdd.common.enums.SmsEnum;
import com.mdd.common.enums.YesNoEnum;
import com.mdd.common.mapper.smsLog.SmsLogMapper;
import com.mdd.common.util.SpringUtils;
import com.mdd.common.util.StringUtils;

public class NoticeCheck {
    private static final int MAX_VERIFY_ATTEMPTS = 5;

    public static Boolean verify(Integer sceneId, String code, String mobile) {
        SmsLogMapper smsLogMapper = SpringUtils.getBean(SmsLogMapper.class);
        QueryWrapper<SmsLog> queryWrapper = new QueryWrapper<SmsLog>();
        queryWrapper.eq("mobile", (Object)mobile);
        queryWrapper.eq("send_status", (Object)SmsEnum.SEND_SUCCESS.getCode());
        queryWrapper.in("scene_id", NoticeEnum.getSmsScene());
        queryWrapper.eq("is_verify", (Object)YesNoEnum.NO.getCode());
        if (!sceneId.equals(0)) {
            queryWrapper.eq("scene_id", (Object)sceneId);
        }
        queryWrapper.orderByDesc("send_time");
        queryWrapper.last("limit 1");
        SmsLog smsLog = (SmsLog)smsLogMapper.selectOne((Wrapper)queryWrapper);
        if (StringUtils.isNull(smsLog)) {
            return false;
        }
        int checks = smsLog.getCheckNum() == null ? 0 : smsLog.getCheckNum();
        if (smsLog.getIsVerify().equals(1) || checks >= MAX_VERIFY_ATTEMPTS
                || smsLog.getSendTime() < System.currentTimeMillis() / 1000L - 300L) {
            return false;
        }
        boolean matched = smsLog.getCode() != null && smsLog.getCode().equals(code);
        UpdateWrapper<SmsLog> update = new UpdateWrapper<SmsLog>()
                .eq("id", smsLog.getId())
                .eq("is_verify", YesNoEnum.NO.getCode())
                .lt("check_num", MAX_VERIFY_ATTEMPTS)
                .setSql("check_num = COALESCE(check_num, 0) + 1");
        if (matched) {
            update.set("is_verify", YesNoEnum.YES.getCode());
        }
        int updated = smsLogMapper.update(null, update);
        // 原子抢占成功码/错误码计数，防止并发请求绕过尝试次数上限。
        return updated > 0 && matched;
    }
}
