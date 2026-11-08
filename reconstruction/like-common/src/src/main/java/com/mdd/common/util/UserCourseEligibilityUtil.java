/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.util;

public final class UserCourseEligibilityUtil {
    private static final int STATUS_REFUNDED = 2;
    private static final int PAY_STATUS_PAID = 1;
    private static final int REFUND_STATUS_REFUNDED = 1;

    private UserCourseEligibilityUtil() {
    }

    public static boolean isConsumable(Integer enrollmentStatus, Integer remainLessons, Integer payStatus, Integer refundStatus, Long orderDeleteTime) {
        return (enrollmentStatus == null || enrollmentStatus != 2) && remainLessons != null && remainLessons > 0 && UserCourseEligibilityUtil.isActiveOrder(payStatus, refundStatus, orderDeleteTime);
    }

    public static boolean isActiveOrder(Integer payStatus, Integer refundStatus, Long orderDeleteTime) {
        return payStatus != null && payStatus == 1 && (refundStatus == null || refundStatus != 1) && orderDeleteTime == null;
    }

    public static boolean canResolveWithoutStudent(int distinctStudentCount) {
        return distinctStudentCount <= 1;
    }

    public static boolean isExactOrder(Integer expectedOrderId, Integer actualOrderId) {
        return expectedOrderId != null && expectedOrderId > 0 && expectedOrderId.equals(actualOrderId);
    }

    public static int effectiveEnrollmentStatus(Integer enrollmentStatus, Integer orderRefundStatus) {
        if (orderRefundStatus != null && orderRefundStatus == 1) {
            return 2;
        }
        return enrollmentStatus != null ? enrollmentStatus : 0;
    }
}

