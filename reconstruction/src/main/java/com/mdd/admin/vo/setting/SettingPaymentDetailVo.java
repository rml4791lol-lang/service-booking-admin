/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.setting.SettingPaymentDetailVo
 */
package com.mdd.admin.vo.setting;

import java.io.Serializable;

public class SettingPaymentDetailVo
implements Serializable {
    private static final long serialVersionUID = 1L;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingPaymentDetailVo)) {
            return false;
        }
        SettingPaymentDetailVo other = (SettingPaymentDetailVo)o;
        return other.canEqual((Object)this);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingPaymentDetailVo;
    }

    public int hashCode() {
        boolean result = true;
        return 1;
    }

    public String toString() {
        return "SettingPaymentDetailVo()";
    }
}

