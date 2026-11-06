/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.plugin.notice.engine;

import com.mdd.common.entity.notice.NoticeRecord;
import com.mdd.common.entity.smsLog.SmsLog;
import com.mdd.common.enums.NoticeEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.notice.NoticeRecordMapper;
import com.mdd.common.mapper.smsLog.SmsLogMapper;
import com.mdd.common.plugin.notice.template.SmsTemplate;
import com.mdd.common.plugin.notice.vo.NoticeSmsVo;
import com.mdd.common.plugin.sms.SmsDriver;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.SpringUtils;
import com.mdd.common.util.StringUtils;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class SmsNoticeHandle {
    public void send(NoticeSmsVo noticeSmsVo, SmsTemplate smsTemplate) {
        String mobile = noticeSmsVo.getMobile();
        Integer scene = noticeSmsVo.getScene();
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        if (StringUtils.isNotNull(noticeSmsVo.getParams())) {
            for (String s : noticeSmsVo.getParams()) {
                String[] arr = s.split(":");
                String key = arr[0].trim();
                String val = arr[1].trim();
                params.put(key, val);
            }
        }
        NoticeRecordMapper noticeRecordMapper = SpringUtils.getBean(NoticeRecordMapper.class);
        int expire = StringUtils.isNull(noticeSmsVo.getExpire()) ? 0 : noticeSmsVo.getExpire();
        NoticeRecord noticeRecord = new NoticeRecord();
        noticeRecord.setSceneId(scene);
        noticeRecord.setUserId(0);
        noticeRecord.setTitle(smsTemplate.getName());
        noticeRecord.setContent(this.getContent(params, smsTemplate.getContent()));
        noticeRecord.setRecipient(NoticeEnum.SENDER_SMS.getCode());
        noticeRecord.setRead(NoticeEnum.VIEW_UNREAD.getCode());
        noticeRecord.setCreateTime(System.currentTimeMillis() / 1000L);
        noticeRecord.setUpdateTime(System.currentTimeMillis() / 1000L);
        noticeRecord.setNoticeType(NoticeEnum.SENDER_SMS.getCode());
        noticeRecordMapper.insert(noticeRecord);
        SmsLogMapper smsLogMapper = SpringUtils.getBean(SmsLogMapper.class);
        SmsLog smsLog = new SmsLog();
        smsLog.setSceneId(noticeSmsVo.getScene());
        smsLog.setMobile(noticeSmsVo.getMobile());
        smsLog.setContent(this.getContent(params, smsTemplate.getContent()));
        smsLog.setCode(noticeSmsVo.getCode());
        if (StringUtils.isNotEmpty(mobile)) {
            try {
                new SmsDriver().setMobile(mobile).setTemplateCode(smsTemplate.getTemplateId()).setTemplateParam(this.getSmsParams(params, smsTemplate.getContent())).sendSms();
                noticeRecord.setUpdateTime(System.currentTimeMillis() / 1000L);
                noticeRecordMapper.updateById(noticeRecord);
                smsLog.setIsVerify(0);
                smsLog.setCheckNum(0);
                smsLog.setSendStatus(1);
                smsLog.setSendTime(System.currentTimeMillis() / 1000L);
                smsLog.setCreateTime(System.currentTimeMillis() / 1000L);
                smsLog.setUpdateTime(System.currentTimeMillis() / 1000L);
                smsLogMapper.insert(smsLog);
            }
            catch (OperateException e) {
                noticeRecord.setUpdateTime(System.currentTimeMillis() / 1000L);
                noticeRecordMapper.updateById(noticeRecord);
                smsLog.setIsVerify(0);
                smsLog.setCheckNum(0);
                smsLog.setSendStatus(2);
                smsLog.setResults(e.getMsg());
                smsLog.setSendTime(System.currentTimeMillis() / 1000L);
                smsLog.setCreateTime(System.currentTimeMillis() / 1000L);
                smsLog.setUpdateTime(System.currentTimeMillis() / 1000L);
                smsLogMapper.insert(smsLog);
                throw new OperateException(e.getMsg());
            }
        }
    }

    private String getContent(Map<String, String> params, String content) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String searchReplace = "\\$\\{" + entry.getKey() + "}";
            content = content.replaceAll(searchReplace, entry.getValue());
        }
        return content;
    }

    private Map<String, String> getSmsParams(Map<String, String> params, String content) {
        String engine = ConfigUtils.get("sms", "default", "");
        if (!engine.equals("tencent")) {
            return params;
        }
        LinkedList<String> arr = new LinkedList<String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String search = "\\$\\{" + entry.getKey() + "}";
            if (content.indexOf(search) == 1 || arr.contains(entry.getKey())) continue;
            arr.add(entry.getKey());
        }
        LinkedList<Integer> arrIndex = new LinkedList<Integer>();
        LinkedHashMap<Integer, String> arr2 = new LinkedHashMap<Integer, String>();
        if (arr.size() > 0) {
            for (String string : arr) {
                int k = content.indexOf(string);
                arrIndex.add(k);
                arr2.put(k, string);
            }
        }
        LinkedList<String> arr3 = new LinkedList<String>();
        Collections.sort(arrIndex);
        for (Integer i : arrIndex) {
            arr3.add((String)arr2.get(i));
        }
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        for (String v : arr3) {
            if (!StringUtils.isNotNull(params.get(v))) continue;
            linkedHashMap.put(params.get(v), "");
        }
        return linkedHashMap;
    }
}

