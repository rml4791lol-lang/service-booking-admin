package com.mdd.common.mapper.activity;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.activity.ActivityBooking;
import org.apache.ibatis.annotations.Mapper;

/**
 * ActivityBooking Mapper（V025 / #186）：活动报名批次。
 */
@Mapper
public interface ActivityBookingMapper extends IBaseMapper<ActivityBooking> {
}
