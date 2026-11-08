package com.mdd.common.mapper.coupon;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * 次数券实例 Mapper（la_coupon_instance）。
 */
@Mapper
public interface CouponInstanceMapper extends IBaseMapper<CouponInstance> {

    /**
     * 生成券编号：CP + 秒级时间戳 + 6 位随机数，冲突时重试（与小程序购买发券口径一致）。
     *
     * <p>后台发券与支付回调共用同一编号空间，la_coupon_instance.uk_sn 兜底唯一。</p>
     */
    default String randMakeCouponInstanceSn() {
        String sn;
        String date = TimeUtils.timestampToDate(System.currentTimeMillis() / 1000L, "yyyyMMddHHmmss");
        do {
            sn = "CP" + date + ToolUtils.randomInt(6);
        } while (this.selectCount(new QueryWrapper<CouponInstance>().eq("sn", sn)) > 0);
        return sn;
    }
}
