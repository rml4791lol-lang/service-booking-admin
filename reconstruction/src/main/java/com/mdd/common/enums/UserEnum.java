/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum UserEnum {
    SEX_OTHER(0, "\u672a\u77e5"),
    SEX_MEN(1, "\u7537"),
    SEX_WOMAN(2, "\u5973");

    private final int code;
    private final String msg;

    private UserEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static String getSexDesc(Integer code) {
        switch (code) {
            case 1: {
                return "\u7537";
            }
            case 2: {
                return "\u5973";
            }
        }
        return "\u672a\u77e5";
    }
}

