/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum ClientEnum {
    MNP(1, "\u5fae\u4fe1\u5c0f\u7a0b\u5e8f"),
    OA(2, "\u5fae\u4fe1\u516c\u4f17\u53f7"),
    H5(3, "\u624b\u673aH5"),
    PC(4, "\u7535\u8111PC"),
    IOS(5, "\u82f9\u679cAPP"),
    APK(6, "\u5b89\u5353APP");

    private final int code;
    private final String msg;

    private ClientEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Integer getCodeByType(String type) {
        for (ClientEnum enumItem : ClientEnum.values()) {
            if (!enumItem.toString().equals(type.toUpperCase())) continue;
            return enumItem.getCode();
        }
        return null;
    }

    public static String getMsgByType(String type) {
        for (ClientEnum enumItem : ClientEnum.values()) {
            if (!enumItem.toString().equals(type)) continue;
            return enumItem.getMsg();
        }
        return null;
    }

    public static String getMsgByCode(Integer code) {
        for (ClientEnum enumItem : ClientEnum.values()) {
            if (enumItem.getCode() != code.intValue()) continue;
            return enumItem.getMsg();
        }
        return null;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}

