/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.plugin.notice.template;

import com.mdd.common.util.MapUtils;
import java.util.Map;

public class SmsTemplate {
    private String name;
    private Integer type;
    private String templateId;
    private String content;
    private Integer status;

    public void setParams(String smsNotice) {
        Map<String, String> config = MapUtils.jsonToMap(smsNotice);
        this.setTemplateId(config.getOrDefault("template_id", ""));
        this.setContent(config.getOrDefault("content", ""));
        this.setStatus(Integer.parseInt(config.getOrDefault("status", "0")));
    }

    public String getName() {
        return this.name;
    }

    public Integer getType() {
        return this.type;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public String getContent() {
        return this.content;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SmsTemplate)) {
            return false;
        }
        SmsTemplate other = (SmsTemplate)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$type = this.getType();
        Integer other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)this$type).equals(other$type)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$templateId = this.getTemplateId();
        String other$templateId = other.getTemplateId();
        if (this$templateId == null ? other$templateId != null : !this$templateId.equals(other$templateId)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        return !(this$content == null ? other$content != null : !this$content.equals(other$content));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SmsTemplate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $templateId = this.getTemplateId();
        result = result * 59 + ($templateId == null ? 43 : $templateId.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    public String toString() {
        return "SmsTemplate(name=" + this.getName() + ", type=" + this.getType() + ", templateId=" + this.getTemplateId() + ", content=" + this.getContent() + ", status=" + this.getStatus() + ")";
    }
}

