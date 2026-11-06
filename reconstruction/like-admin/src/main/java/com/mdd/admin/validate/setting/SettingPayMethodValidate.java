/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.setting.SettingPayMethodValidate
 *  com.mdd.admin.vo.setting.SettingPaymentMethodVo
 *  io.swagger.annotations.ApiModel
 */
package com.mdd.admin.validate.setting;

import com.mdd.admin.vo.setting.SettingPaymentMethodVo;
import io.swagger.annotations.ApiModel;
import java.util.List;

@ApiModel(value="\u652f\u4ed8\u65b9\u5f0f\u8bbe\u7f6e\u53c2\u6570")
public class SettingPayMethodValidate {
    List<List<SettingPaymentMethodVo>> data;

    public List<List<SettingPaymentMethodVo>> getData() {
        return this.data;
    }

    public void setData(List<List<SettingPaymentMethodVo>> data) {
        this.data = data;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingPayMethodValidate)) {
            return false;
        }
        SettingPayMethodValidate other = (SettingPayMethodValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        List this$data = this.getData();
        List other$data = other.getData();
        return !(this$data == null ? other$data != null : !((Object)this$data).equals(other$data));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingPayMethodValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List $data = this.getData();
        result = result * 59 + ($data == null ? 43 : ((Object)$data).hashCode());
        return result;
    }

    public String toString() {
        return "SettingPayMethodValidate(data=" + this.getData() + ")";
    }
}

