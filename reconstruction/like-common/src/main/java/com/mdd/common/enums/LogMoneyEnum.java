/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum LogMoneyEnum {
    UM_INC_ADMIN(200, "\u5e73\u53f0\u589e\u52a0\u4f59\u989d"),
    UM_DEC_ADMIN(100, "\u5e73\u53f0\u51cf\u5c11\u4f59\u989d"),
    UM_INC_RECHARGE(201, "\u5145\u503c\u589e\u52a0\u4f59\u989d"),
    UM_DEC_RECHARGE(101, "\u5145\u503c\u8ba2\u5355\u9000\u6b3e\u51cf\u5c11\u4f59\u989d"),
    UM_DEC_COURSE_PAY(102, "\u8bfe\u7a0b\u8d2d\u4e70"),
    UM_INC_ACTIVITY_REFUND(202, "\u6d3b\u52a8\u62a5\u540d\u9000\u6b3e\u589e\u52a0\u4f59\u989d"),
    UM_INC_COURSE_REFUND(203, "\u8bfe\u7a0b\u9000\u6b3e\u589e\u52a0\u4f59\u989d"),
    UM_INC_COUPON_REFUND(204, "\u5238\u8ba2\u5355\u9000\u6b3e\u589e\u52a0\u4f59\u989d");

    private final int code;
    private final String msg;

    private LogMoneyEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static String getMsgByCode(Integer code) {
        for (LogMoneyEnum enumItem : LogMoneyEnum.values()) {
            if (enumItem.getCode() != code.intValue()) continue;
            return enumItem.getMsg();
        }
        return null;
    }

    public static Map<Integer, String> getTypeList() {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (LogMoneyEnum enumItem : LogMoneyEnum.values()) {
            map.put(enumItem.getCode(), enumItem.getMsg());
        }
        return map;
    }
}

