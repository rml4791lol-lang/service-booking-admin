/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 */
package com.mdd.common.enums;

import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.List;

public enum AccountLogEnum {
    UM(1, "\u53d8\u52a8\u5bf9\u8c61"),
    INC(1, "\u589e\u52a0"),
    DEC(2, "\u51cf\u5c11"),
    UM_DEC_ADMIN(100, "\u5e73\u53f0\u51cf\u5c11\u4f59\u989d"),
    UM_DEC_RECHARGE_REFUND(101, "\u5145\u503c\u8ba2\u5355\u9000\u6b3e\u51cf\u5c11\u4f59\u989d"),
    UM_INC_ADMIN(200, "\u5e73\u53f0\u589e\u52a0\u4f59\u989d"),
    UM_INC_RECHARGE(201, "\u5145\u503c\u589e\u52a0\u4f59\u989d"),
    UM_DEC_COURSE_PAY(102, "\u8bfe\u7a0b\u652f\u4ed8\u6263\u6b3e"),
    UM_DEC_ACTIVITY_PAY(103, "\u6d3b\u52a8\u62a5\u540d\u652f\u4ed8\u6263\u6b3e"),
    UM_INC_ACTIVITY_REFUND(202, "\u6d3b\u52a8\u62a5\u540d\u9000\u6b3e\u589e\u52a0\u4f59\u989d"),
    UM_INC_COURSE_REFUND(203, "\u8bfe\u7a0b\u9000\u6b3e\u589e\u52a0\u4f59\u989d"),
    UM_INC_COUPON_REFUND(204, "\u5238\u8ba2\u5355\u9000\u6b3e\u589e\u52a0\u4f59\u989d");

    private final int code;
    private final String msg;

    private AccountLogEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getChangeTypeDesc(Integer changeType) {
        JSONObject ret = new JSONObject();
        ret.put(String.valueOf(UM_DEC_ADMIN.getCode()), (Object)UM_DEC_ADMIN.getMsg());
        ret.put(String.valueOf(UM_INC_ADMIN.getCode()), (Object)UM_INC_ADMIN.getMsg());
        ret.put(String.valueOf(UM_INC_RECHARGE.getCode()), (Object)UM_INC_RECHARGE.getMsg());
        ret.put(String.valueOf(UM_DEC_RECHARGE_REFUND.getCode()), (Object)UM_DEC_RECHARGE_REFUND.getMsg());
        ret.put(String.valueOf(UM_DEC_COURSE_PAY.getCode()), (Object)UM_DEC_COURSE_PAY.getMsg());
        ret.put(String.valueOf(UM_DEC_ACTIVITY_PAY.getCode()), (Object)UM_DEC_ACTIVITY_PAY.getMsg());
        ret.put(String.valueOf(UM_INC_ACTIVITY_REFUND.getCode()), (Object)UM_INC_ACTIVITY_REFUND.getMsg());
        ret.put(String.valueOf(UM_INC_COURSE_REFUND.getCode()), (Object)UM_INC_COURSE_REFUND.getMsg());
        ret.put(String.valueOf(UM_INC_COUPON_REFUND.getCode()), (Object)UM_INC_COUPON_REFUND.getMsg());
        return ret.getString(String.valueOf(changeType));
    }

    public static Integer getChangeObject(Integer changeType) {
        List<Integer> typeList = AccountLogEnum.getUserMoneyChangeType();
        if (typeList.contains(changeType)) {
            return UM.getCode();
        }
        return 0;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static List<Integer> getUserMoneyChangeType() {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ret.add(UM_DEC_ADMIN.getCode());
        ret.add(UM_DEC_RECHARGE_REFUND.getCode());
        ret.add(UM_INC_ADMIN.getCode());
        ret.add(UM_INC_RECHARGE.getCode());
        ret.add(UM_DEC_COURSE_PAY.getCode());
        ret.add(UM_DEC_ACTIVITY_PAY.getCode());
        ret.add(UM_INC_ACTIVITY_REFUND.getCode());
        ret.add(UM_INC_COURSE_REFUND.getCode());
        ret.add(UM_INC_COUPON_REFUND.getCode());
        return ret;
    }
}

