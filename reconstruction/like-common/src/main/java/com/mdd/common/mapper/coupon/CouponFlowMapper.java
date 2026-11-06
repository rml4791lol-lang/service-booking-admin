package com.mdd.common.mapper.coupon;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.coupon.CouponFlow;
import org.apache.ibatis.annotations.Mapper;

/**
 * 次数券流水 Mapper（la_coupon_flow）。业务只 INSERT，不 UPDATE / DELETE。
 */
@Mapper
public interface CouponFlowMapper extends IBaseMapper<CouponFlow> {
}
