package com.mdd.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 活动报名名单（含支付状态）查询。
 *
 * <p>口径：一条 {@code la_activity_signup} 一行（一人一行），
 * 支付状态取自 {@code signup.order_id -> la_recharge_order.pay_status}；
 * 免费活动不生成订单（order_id=0），按「免费」处理。</p>
 */
@Mapper
public interface ActivityRosterQueryMapper {

    long countSignup(@Param("activityId") Integer activityId, @Param("status") Integer status,
                     @Param("payStatus") Integer payStatus, @Param("keyword") String keyword,
                     @Param("dateStart") Long dateStart, @Param("dateEnd") Long dateEnd);

    List<Map<String, Object>> pageSignup(@Param("activityId") Integer activityId,
                                         @Param("status") Integer status,
                                         @Param("payStatus") Integer payStatus,
                                         @Param("keyword") String keyword,
                                         @Param("dateStart") Long dateStart,
                                         @Param("dateEnd") Long dateEnd,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    Map<String, Object> summarySignup(@Param("activityId") Integer activityId,
                                      @Param("status") Integer status,
                                      @Param("payStatus") Integer payStatus,
                                      @Param("keyword") String keyword,
                                      @Param("dateStart") Long dateStart,
                                      @Param("dateEnd") Long dateEnd);
}
