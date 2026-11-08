package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.coupon.CouponGoodsCreateValidate;
import com.mdd.admin.validate.coupon.CouponGoodsSearchValidate;
import com.mdd.admin.validate.coupon.CouponGoodsUpdateValidate;
import com.mdd.admin.validate.coupon.CouponGrantedSearchValidate;
import com.mdd.admin.validate.coupon.CouponGrantValidate;
import com.mdd.admin.validate.coupon.CouponRevokeValidate;
import com.mdd.admin.vo.coupon.CouponGoodsListedVo;
import com.mdd.admin.vo.coupon.CouponGrantedVo;
import com.mdd.admin.vo.coupon.CouponGrantVo;
import com.mdd.common.core.PageResult;

import java.util.Map;

/**
 * 次数券商品后台管理（#45 / T28.9）。
 */
public interface ICouponGoodsService {

    PageResult<CouponGoodsListedVo> list(PageValidate pageValidate, CouponGoodsSearchValidate searchValidate);

    void add(CouponGoodsCreateValidate createValidate);

    void edit(CouponGoodsUpdateValidate updateValidate);

    void status(Integer id, Integer status);

    void sort(Integer id, Integer sort);

    /**
     * 删除券商品。存在券实例或券订单引用时拒绝，提示改为下架。
     */
    void del(Integer id);

    /**
     * 表单下拉：课程（含团/私教/体验分类）与小区。
     */
    Map<String, Object> options();

    /**
     * 后台发次数券：按商品 + 家长手机号 + 可选学员 + 张数逐张发券并写发放流水。
     *
     * @param grantValidate 发放参数
     * @param adminId       当前后台管理员ID（写入流水操作人/操作日志）
     * @return 发放回执（张数、券 sn 摘要、给谁、券名）
     */
    CouponGrantVo grant(CouponGrantValidate grantValidate, Integer adminId);

    /**
     * 已发券查询（#213 C2）：按券号或家长手机号查券实例，供后台撤回核对。
     *
     * @param pageValidate   分页
     * @param searchValidate 券号（精确）/手机号（精确），至少填一个
     */
    PageResult<CouponGrantedVo> granted(PageValidate pageValidate, CouponGrantedSearchValidate searchValidate);

    /**
     * 发券撤回（#213 C2）：仅未核销（status=1 且 remaining=total）的券可撤回，
     * 置为已作废（status=4）并写一条作废流水。已核销/已用完/已作废/已过期均拒绝。
     *
     * @param revokeValidate 撤回参数（券号 + 可选备注）
     * @param adminId        当前后台管理员ID（写入流水操作人/操作日志）
     */
    void revoke(CouponRevokeValidate revokeValidate, Integer adminId);
}
