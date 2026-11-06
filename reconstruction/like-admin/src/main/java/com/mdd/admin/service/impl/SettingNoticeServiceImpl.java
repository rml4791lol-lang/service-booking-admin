/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.service.ISettingNoticeService
 *  com.mdd.admin.service.impl.SettingNoticeServiceImpl
 *  com.mdd.admin.vo.setting.SettingNoticeDetailVo
 *  com.mdd.admin.vo.setting.SettingNoticeListedVo
 *  com.mdd.common.entity.notice.NoticeSetting
 *  com.mdd.common.mapper.notice.NoticeSettingMapper
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.ISettingNoticeService;
import com.mdd.admin.vo.setting.SettingNoticeDetailVo;
import com.mdd.admin.vo.setting.SettingNoticeListedVo;
import com.mdd.common.entity.notice.NoticeSetting;
import com.mdd.common.mapper.notice.NoticeSettingMapper;
import com.mdd.common.util.ParamUtils;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SettingNoticeServiceImpl
implements ISettingNoticeService {
    @Resource
    NoticeSettingMapper noticeSettingMapper;

    public List<SettingNoticeListedVo> list(Integer recipient) {
        QueryWrapper<NoticeSetting> queryWrapper = new QueryWrapper<NoticeSetting>();
        queryWrapper.eq("recipient", (Object)recipient);
        queryWrapper.orderByAsc("id");
        List<NoticeSetting> noticeSettings = this.noticeSettingMapper.selectList((Wrapper)queryWrapper);
        LinkedList<SettingNoticeListedVo> list = new LinkedList<SettingNoticeListedVo>();
        for (NoticeSetting n : noticeSettings) {
            SettingNoticeListedVo vo = new SettingNoticeListedVo();
            BeanUtils.copyProperties((Object)n, (Object)vo);
            JSONObject systemNotice = JSONObject.parse((String)n.getSystemNotice(), (JSONReader.Feature[])new JSONReader.Feature[0]);
            JSONObject smsNotice = JSONObject.parse((String)n.getSmsNotice(), (JSONReader.Feature[])new JSONReader.Feature[0]);
            JSONObject oaNotice = JSONObject.parse((String)n.getOaNotice(), (JSONReader.Feature[])new JSONReader.Feature[0]);
            JSONObject mnpNotice = JSONObject.parse((String)n.getMnpNotice(), (JSONReader.Feature[])new JSONReader.Feature[0]);
            if (systemNotice != null && "1".equals(systemNotice.getString("status"))) {
                vo.setSmsNotice(systemNotice);
            }
            if (smsNotice != null && "1".equals(smsNotice.getString("status"))) {
                vo.setSmsNotice(smsNotice);
            }
            if (oaNotice != null && "1".equals(oaNotice.getString("status"))) {
                vo.setSmsNotice(oaNotice);
            }
            if (mnpNotice != null && "1".equals(mnpNotice.getString("status"))) {
                vo.setSmsNotice(mnpNotice);
            }
            vo.setTypeDesc(n.getType() == 1 ? "\u4e1a\u52a1\u901a\u77e5" : "\u9a8c\u8bc1\u7801");
            list.add(vo);
        }
        return list;
    }

    public SettingNoticeDetailVo detail(Integer id) {
        NoticeSetting noticeSetting = (NoticeSetting)this.noticeSettingMapper.selectOne(new QueryWrapper<NoticeSetting>().select(NoticeSetting.class, info -> !info.getColumn().equals("update_time")).eq("id", (Object)id).last("limit 1"));
        SettingNoticeDetailVo vo = new SettingNoticeDetailVo();
        BeanUtils.copyProperties((Object)noticeSetting, (Object)vo);
        vo.setType(noticeSetting.getType().equals(1) ? "\u4e1a\u52a1\u901a\u77e5" : "\u9a8c\u8bc1\u7801");
        vo.setSystemNotice(JSONObject.parse((String)noticeSetting.getSystemNotice(), (JSONReader.Feature[])new JSONReader.Feature[0]));
        vo.setOaNotice(JSONObject.parse((String)noticeSetting.getOaNotice(), (JSONReader.Feature[])new JSONReader.Feature[0]));
        vo.setMnpNotice(JSONObject.parse((String)noticeSetting.getMnpNotice(), (JSONReader.Feature[])new JSONReader.Feature[0]));
        vo.setSmsNotice(JSONObject.parse((String)noticeSetting.getSmsNotice(), (JSONReader.Feature[])new JSONReader.Feature[0]));
        return vo;
    }

    public void save(JSONObject params) {
        Integer id = ParamUtils.requiredInt(params.get("id"), "通知设置ID");
        NoticeSetting noticeSetting = (NoticeSetting)this.noticeSettingMapper.selectOne(new QueryWrapper<NoticeSetting>().eq("id", (Object)id).last("limit 1"));
        JSONObject template = params.getJSONObject("template");
        JSONObject mnp_notice = template.getJSONObject("mnp_notice");
        JSONObject oa_notice = template.getJSONObject("oa_notice");
        JSONObject sms_notice = template.getJSONObject("sms_notice");
        JSONObject system_notice = template.getJSONObject("system_notice");
        noticeSetting.setSystemNotice(system_notice.toJSONString(new JSONWriter.Feature[0]));
        noticeSetting.setSmsNotice(sms_notice.toJSONString(new JSONWriter.Feature[0]));
        noticeSetting.setOaNotice(oa_notice.toJSONString(new JSONWriter.Feature[0]));
        noticeSetting.setMnpNotice(mnp_notice.toJSONString(new JSONWriter.Feature[0]));
        noticeSetting.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.noticeSettingMapper.updateById(noticeSetting);
    }
}

