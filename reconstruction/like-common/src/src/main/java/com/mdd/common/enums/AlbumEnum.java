/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum AlbumEnum {
    IMAGE(10, "\u56fe\u7247"),
    Video(20, "\u89c6\u9891"),
    Doc(30, "\u6587\u4ef6");

    private final int code;
    private final String msg;

    private AlbumEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}

