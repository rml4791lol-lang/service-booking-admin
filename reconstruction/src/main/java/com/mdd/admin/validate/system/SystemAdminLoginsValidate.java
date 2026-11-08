/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.SystemAdminLoginsValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u7cfb\u7edf\u767b\u5f55\u53c2\u6570")
public class SystemAdminLoginsValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message="\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=2, max=20, message="\u8d26\u53f7\u6216\u5bc6\u7801\u9519\u8bef")
    @ApiModelProperty(value="\u767b\u5f55\u8d26\u53f7", required=true)
    private @NotEmpty(message="\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=2, max=20, message="\u8d26\u53f7\u6216\u5bc6\u7801\u9519\u8bef") String account;
    @NotEmpty(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=6, max=64, message="\u8d26\u53f7\u6216\u5bc6\u7801\u9519\u8bef")
    @ApiModelProperty(value="\u767b\u5f55\u5bc6\u7801", required=true)
    private @NotEmpty(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=6, max=64, message="\u8d26\u53f7\u6216\u5bc6\u7801\u9519\u8bef") String password;
    @ApiModelProperty(value="图形验证码 UUID")
    private String uuid;
    @ApiModelProperty(value="图形验证码")
    private String code;
    @ApiModelProperty(value="图形验证码对象（兼容旧客户端）")
    private Captcha captcha;
    @NotNull(message="\u8bf7\u9009\u62e9\u8bbe\u5907\u7c7b\u578b")
    @ApiModelProperty(value="\u8bbe\u5907\u7c7b\u578b", required=true)
    private @NotNull(message="\u8bf7\u9009\u62e9\u8bbe\u5907\u7c7b\u578b") Integer terminal;

    public String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getCode() {
        return this.code;
    }

    public Captcha getCaptcha() {
        return this.captcha;
    }

    public String resolveCaptchaUuid() {
        if (uuid != null && !uuid.trim().isEmpty()) {
            return uuid.trim();
        }
        return captcha == null || captcha.getUuid() == null ? "" : captcha.getUuid().trim();
    }

    public String resolveCaptchaCode() {
        if (code != null && !code.trim().isEmpty()) {
            return code.trim();
        }
        return captcha == null || captcha.getCode() == null ? "" : captcha.getCode().trim();
    }

    @NotNull(message="\u8bf7\u9009\u62e9\u8bbe\u5907\u7c7b\u578b")
    public @NotNull(message="\u8bf7\u9009\u62e9\u8bbe\u5907\u7c7b\u578b") Integer getTerminal() {
        return this.terminal;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }

    public void setTerminal(@NotNull(message="\u8bf7\u9009\u62e9\u8bbe\u5907\u7c7b\u578b") @NotNull(message="\u8bf7\u9009\u62e9\u8bbe\u5907\u7c7b\u578b") Integer terminal) {
        this.terminal = terminal;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemAdminLoginsValidate)) {
            return false;
        }
        SystemAdminLoginsValidate other = (SystemAdminLoginsValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$terminal = this.getTerminal();
        Integer other$terminal = other.getTerminal();
        if (this$terminal == null ? other$terminal != null : !((Object)this$terminal).equals(other$terminal)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        return !(this$password == null ? other$password != null : !this$password.equals(other$password));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemAdminLoginsValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $terminal = this.getTerminal();
        result = result * 59 + ($terminal == null ? 43 : ((Object)$terminal).hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        return result;
    }

    public String toString() {
        return "SystemAdminLoginsValidate(account=" + this.getAccount() + ", password=" + this.getPassword() + ", terminal=" + this.getTerminal() + ")";
    }

    public static class Captcha implements Serializable {
        private String uuid;
        private String code;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
