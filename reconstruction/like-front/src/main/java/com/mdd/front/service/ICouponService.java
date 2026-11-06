package com.mdd.front.service;

import com.mdd.front.vo.CouponVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 小程序券服务。
 *
 * <p>两套模型并存：</p>
 * <ul>
 *   <li>老抵扣券 {@code la_coupon}（金额抵扣）：仅保留 legacy 方法给老接口；
 *       数据模型不动，本票不读写。</li>
 *   <li>T29.4 次数券：券商品 {@code la_coupon_goods} + 券实例 {@code la_coupon_instance}
 *       + 券流水 {@code la_coupon_flow}，对应 pages/coupons 与预约/核销的适用校验。</li>
 * </ul>
 */
public interface ICouponService {

    // ---- 老抵扣券（保留给老接口，勿删） ------------------------------------

    /**
     * 老抵扣券：支付时可用券列表（GET /api/coupon/available）。
     */
    List<CouponVo> legacyAvailableCoupons(Integer userId, BigDecimal orderAmount);

    /**
     * 老抵扣券：我的抵扣券（GET /api/coupon/legacy/myCoupons）。
     */
    List<CouponVo> legacyMyCoupons(Integer userId, Integer status);

    // ---- 次数券（T29.4） ----------------------------------------------------

    /**
     * 券商品列表：已上架、未软删，按 sort/id 升序。公开只读。
     */
    List<Map<String, Object>> products();

    /**
     * 我的券（按用户，可按学员过滤）。返回 mock 契约字段：
     * id/productId/title/total/remaining/scope/scopeType/courseIds/areaIds/expire/
     * status/studentId/studentName/scopeText/usable 等。
     */
    List<Map<String, Object>> myCoupons(Integer userId, Integer studentId);

    /**
     * 券流水（只读）。返回顺序与 mock 一致：按 id 升序；
     * 页面自行 reverse 后展示最新在前。
     */
    List<Map<String, Object>> flows(Integer userId, Integer studentId, Integer couponId);

    /**
     * 券适用统一校验入口。courseId 与 sessionId 二选一（sessionId 会换算成班级）。
     *
     * @return {applies:boolean, reason:string}
     */
    Map<String, Object> applies(Integer userId, String couponId, String courseId,
                                String sessionId, String studentId);

    /**
     * 自动匹配最优券：规则与 mock/golden 一致——可用的券中，到期时间最早者优先；
     * 到期时间相同按 id 升序。无可用券或课程不存在返回 null。
     */
    Map<String, Object> bestCoupon(Integer userId, String courseId, String sessionId, String studentId);

    /**
     * 教练核销扣券（#26）：按学员自动匹配一张可用的次数券并扣 1 次，
     * 同时追加一条 type=2（核销扣减）的券流水；次数用尽置为 2（已用完）。
     *
     * <p>必须在外层事务内调用：候选券会用 {@code SELECT ... FOR UPDATE} 重新加锁，
     * 避免同一张券被并发核销导致超扣。</p>
     *
     * @return {deducted, couponId, title, remaining, reason}；
     *         deducted=false 时 reason 说明原因（无可用次数券/券次数已用完/券不适用于本课程等），
     *         调用方按业务口径处理（#26 为不阻断核销、返回提示）。
     */
    Map<String, Object> deductForCheckin(Integer userId, Integer studentId, Integer courseClassId,
                                         String ref, Integer operatorId);
}
