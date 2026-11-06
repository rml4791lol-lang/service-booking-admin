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
import com.mdd.common.entity.notice.NoticeSetting;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.notice.NoticeSettingMapper;
import com.mdd.common.plugin.notice.engine.SmsNoticeHandle;
import com.mdd.common.plugin.notice.template.SmsTemplate;
import com.mdd.common.plugin.notice.vo.NoticeSmsVo;
import com.mdd.common.util.SpringUtils;
import com.mdd.common.util.StringUtils;

public class NoticeDriver {
    public static void handle(NoticeSmsVo noticeSmsVo) {
        NoticeSettingMapper noticeSettingMapper = SpringUtils.getBean(NoticeSettingMapper.class);
        NoticeSetting noticeSetting = (NoticeSetting)noticeSettingMapper.selectOne(new QueryWrapper<NoticeSetting>().eq("scene_id", (Object)noticeSmsVo.getScene()).last("limit 1"));
        if (StringUtils.isNull(noticeSetting)) {
            throw new OperateException("\u6d88\u606f\u573a\u666f\u4e0d\u5b58\u5728!");
        }
        SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.setName(noticeSetting.getSceneName());
        smsTemplate.setType(noticeSetting.getType());
        smsTemplate.setParams(noticeSetting.getSmsNotice());
        if (StringUtils.isNotNull(smsTemplate.getStatus()) && smsTemplate.getStatus().equals(1)) {
            new SmsNoticeHandle().send(noticeSmsVo, smsTemplate);
        }
    }
}

