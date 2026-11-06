package com.mdd.front.util;

import com.mdd.common.exception.OperateException;

/**
 * 退款文本长度上限（与 la_course_refund.reason varchar(50) / detail varchar(500) 对齐）。
 * 券退款与课程退费共用，避免两处各写一份 50/500 后漂移。
 */
public final class RefundTextLimits {

    public static final int REASON_MAX = 50;
    public static final int DETAIL_MAX = 500;

    private RefundTextLimits() {
    }

    public static void validate(String reason, String detail) {
        if (reason != null && reason.length() > REASON_MAX) {
            throw new OperateException("退款原因最多 " + REASON_MAX + " 字");
        }
        if (detail != null && detail.length() > DETAIL_MAX) {
            throw new OperateException("退款说明最多 " + DETAIL_MAX + " 字");
        }
    }
}
