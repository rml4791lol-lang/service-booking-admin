/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum PaymentEnum {
    UN_PAID(0, "\u672a\u652f\u4ed8"),
    OK_PAID(1, "\u5df2\u652f\u4ed8"),
    /** 关闭/取消：与老系统 OrderAutoCancelJob 的 pay_status=4 口径一致。 */
    CLOSED(4, "\u5df2\u53d6\u6d88"),
    WALLET_PAY(1, "\u4f59\u989d\u652f\u4ed8"),
    WX_PAY(2, "\u5fae\u4fe1\u652f\u4ed8"),
    ALI_PAY(3, "\u652f\u4ed8\u5b9d\u652f\u4ed8");

    private final int code;
    private final String msg;

    private PaymentEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static String getPayWayMsg(Integer code) {
        switch (code) {
            case 1: {
                return "\u4f59\u989d\u652f\u4ed8";
            }
            case 2: {
                return "\u5fae\u4fe1\u652f\u4ed8";
            }
            case 3: {
                return "\u652f\u4ed8\u5b9d\u652f\u4ed8";
            }
            case 4: {
                return "\u7ebf\u4e0b\u652f\u4ed8";
            }
        }
        return "\u672a\u77e5";
    }

    public static String getPayStatusMsg(Integer status) {
        switch (status) {
            case 0: {
                return "\u672a\u652f\u4ed8";
            }
            case 1: {
                return "\u5df2\u652f\u4ed8";
            }
            case 4: {
                return "\u5df2\u53d6\u6d88";
            }
        }
        return "\u672a\u77e5";
    }
}

