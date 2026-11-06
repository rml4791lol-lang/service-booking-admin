/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.aliyuncs.CommonRequest
 *  com.aliyuncs.CommonResponse
 *  com.aliyuncs.DefaultAcsClient
 *  com.aliyuncs.http.MethodType
 *  com.aliyuncs.profile.DefaultProfile
 *  com.aliyuncs.profile.IClientProfile
 */
package com.mdd.common.plugin.sms.engine;

import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import java.util.Map;

public class AliSms {
    private Integer sendResult;
    private String mobile;
    private String templateId;
    private String templateParams;
    private final Map<String, String> config;

    public AliSms(Map<String, String> config) {
        this.config = config;
    }

    public AliSms setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public AliSms setTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public AliSms setTemplateParams(String templateParams) {
        this.templateParams = templateParams;
        return this;
    }

    public Integer getSendResult() {
        return this.sendResult;
    }

    public String send() {
        DefaultProfile profile = DefaultProfile.getProfile((String)"cn-hangzhou", (String)this.config.get("app_key"), (String)this.config.get("secret_key"));
        DefaultAcsClient client = new DefaultAcsClient((IClientProfile)profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", this.mobile);
        request.putQueryParameter("SignName", this.config.get("sign"));
        request.putQueryParameter("TemplateCode", this.templateId);
        request.putQueryParameter("TemplateParam", this.templateParams);
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject res = JSONObject.parseObject((String)response.getData());
            if (!res.get("Code").equals("OK") || !res.get("Message").equals("OK")) {
                this.sendResult = 2;
                return res.get("Message").toString();
            }
            this.sendResult = 1;
            return response.getData();
        }
        catch (Exception e) {
            this.sendResult = 2;
            return e.getMessage();
        }
    }
}

