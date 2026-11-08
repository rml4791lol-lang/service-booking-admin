package com.mdd.admin.service;

import com.mdd.admin.validate.coupon.CouponGrantBatchRowValidate;
import com.mdd.admin.vo.coupon.CouponGrantBatchResultVo;

import java.util.List;

/**
 * 券批量发放（#批量导入）。
 *
 * <p>逐行复用单条发券 {@link ICouponGoodsService#grant}：
 * 每行独立事务（单行失败回滚该行，不影响其他行）、每行独立 60 秒幂等键。</p>
 */
public interface ICouponGrantBatchService {

    /**
     * 批量发放次数券。
     *
     * @param rows    前端解析 CSV 后的 JSON 行数组（1–500 行）
     * @param adminId 当前后台管理员ID（写入券流水操作人）
     * @return 汇总（total/success/failed/errors）
     */
    CouponGrantBatchResultVo grantBatch(List<CouponGrantBatchRowValidate> rows, Integer adminId);
}
