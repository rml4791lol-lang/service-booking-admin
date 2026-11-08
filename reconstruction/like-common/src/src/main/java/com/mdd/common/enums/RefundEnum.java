/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum RefundEnum {
    TYPE_ADMIN(1, "\u540e\u53f0\u9000\u6b3e"),
    REFUND_ING(0, "\u9000\u6b3e\u4e2d"),
    REFUND_SUCCESS(1, "\u9000\u6b3e\u6210\u529f"),
    REFUND_ERROR(2, "\u9000\u6b3e\u5931\u8d25"),
    ORDER_TYPE_ORDER(1, "\u666e\u901a\u8ba2\u5355"),
    ORDER_TYPE_RECHARGE(2, "\u5145\u503c\u8ba2\u5355");

    private final int code;
    private final String msg;

    private RefundEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static String getOrderType(Integer code) {
        switch (code) {
            case 1: {
                return "order";
            }
            case 2: {
                return "recharge";
            }
        }
        return "\u672a\u77e5";
    }

    public static String getRefundTypeMsg(Integer code) {
        if (code == 1) {
            return TYPE_ADMIN.getMsg();
        }
        return "\u672a\u77e5";
    }

    public static String getRefundStatusMsg(Integer code) {
        switch (code) {
            case 0: {
                return REFUND_ING.getMsg();
            }
            case 1: {
                return REFUND_SUCCESS.getMsg();
            }
            case 2: {
                return REFUND_ERROR.getMsg();
            }
        }
        return "\u672a\u77e5";
    }
}

