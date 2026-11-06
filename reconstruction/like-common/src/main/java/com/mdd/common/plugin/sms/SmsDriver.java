/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 */
package com.mdd.common.plugin.sms;

import com.alibaba.fastjson2.JSON;
import com.mdd.common.exception.OperateException;
import com.mdd.common.plugin.sms.engine.AliSms;
import com.mdd.common.plugin.sms.engine.TencentSms;
import com.mdd.common.util.ConfigUtils;
import java.util.Map;

public class SmsDriver {
    private String mobile;
    private String templateCode;
    private Map<String, String> templateParam;
    private final String engine = ConfigUtils.get("sms", "engine", "");
    private final Map<String, String> config = ConfigUtils.getMap("sms", this.engine);

    public SmsDriver setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public SmsDriver setTemplateCode(String code) {
        this.templateCode = code;
        return this;
    }

    public SmsDriver setTemplateParam(Map<String, String> param) {
        this.templateParam = param;
        return this;
    }

    public void sendSms() {
        String templateParam = JSON.toJSONString(this.templateParam);
        Integer sendResult = 0;
        String results = "";
        switch (this.engine) {
            case "ali": {
                AliSms aliSms = new AliSms(this.config);
                results = aliSms.setMobile(this.mobile).setTemplateId(this.templateCode).setTemplateParams(templateParam).send();
                sendResult = aliSms.getSendResult();
                break;
            }
            case "tencent": {
                TencentSms tencentSms = new TencentSms(this.config);
                results = tencentSms.setMobile(this.mobile).setTemplateId(this.templateCode).setTemplateParams(this.templateParam).send();
                sendResult = tencentSms.getSendResult();
                break;
            }
            default: {
                throw new OperateException("\u77ed\u4fe1\u7c7b\u578b\u9519\u8bef");
            }
        }
        if (sendResult == 2) {
            throw new OperateException(results);
        }
    }
}

