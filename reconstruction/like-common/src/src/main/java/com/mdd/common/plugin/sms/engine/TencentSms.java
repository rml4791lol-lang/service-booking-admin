/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.tencentcloudapi.common.Credential
 *  com.tencentcloudapi.common.profile.ClientProfile
 *  com.tencentcloudapi.common.profile.HttpProfile
 *  com.tencentcloudapi.sms.v20210111.SmsClient
 *  com.tencentcloudapi.sms.v20210111.models.SendSmsRequest
 *  com.tencentcloudapi.sms.v20210111.models.SendSmsResponse
 */
package com.mdd.common.plugin.sms.engine;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import java.util.LinkedList;
import java.util.Map;

public class TencentSms {
    private Integer sendResult;
    private String mobile;
    private String templateId;
    private String[] templateParams;
    private final Map<String, String> config;

    public TencentSms(Map<String, String> config) {
        this.config = config;
    }

    public TencentSms setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public TencentSms setTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public TencentSms setTemplateParams(Map<String, String> templateParams) {
        LinkedList<String> params = new LinkedList<String>();
        for (Map.Entry<String, String> entry : templateParams.entrySet()) {
            params.add(entry.getKey());
        }
        this.templateParams = params.toArray(new String[0]);
        return this;
    }

    public Integer getSendResult() {
        return this.sendResult;
    }

    public String send() {
        try {
            Credential cred = new Credential(this.config.get("secret_id"), this.config.get("secret_key"));
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            SendSmsRequest req = new SendSmsRequest();
            req.setSignName(this.config.get("sign"));
            req.setSmsSdkAppId(this.config.get("app_id"));
            req.setTemplateId(this.templateId);
            String[] phoneNumberSet = new String[]{"+86" + this.mobile};
            req.setPhoneNumberSet(phoneNumberSet);
            String[] templateParamSet = this.templateParams;
            req.setTemplateParamSet(templateParamSet);
            SendSmsResponse res = client.SendSms(req);
            System.out.println(res);
            if (!res.getSendStatusSet()[0].getCode().equals("Ok")) {
                this.sendResult = 2;
                return res.getSendStatusSet()[0].getMessage();
            }
            this.sendResult = 1;
            return res.getSendStatusSet()[0].getMessage();
        }
        catch (Exception e) {
            this.sendResult = 2;
            return e.getMessage();
        }
    }
}

