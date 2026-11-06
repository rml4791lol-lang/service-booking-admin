/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.service.ICourseOrderService
 *  com.mdd.front.validate.course.CourseOrderPageValidate
 *  com.mdd.front.validate.course.CourseOrderValidate
 *  com.mdd.front.vo.course.CourseOrderListVo
 *  com.mdd.front.vo.course.CourseOrderStatsVo
 */
package com.mdd.front.service;

import com.mdd.common.core.PageResult;
import com.mdd.front.validate.course.CourseOrderPageValidate;
import com.mdd.front.validate.course.CourseOrderValidate;
import com.mdd.front.vo.course.CourseOrderListVo;
import com.mdd.front.vo.course.CourseOrderStatsVo;
import java.util.Map;

public interface ICourseOrderService {
    public Map<String, Object> placeOrder(Integer var1, Integer var2, CourseOrderValidate var3);

    public PageResult<CourseOrderListVo> orderList(CourseOrderPageValidate var1, Integer var2);

    public CourseOrderStatsVo orderStats(Integer var1);

    public void cancelOrder(Integer var1, Integer var2);

    public boolean checkPurchased(Integer var1, Integer var2);
}

