package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.coupon.CouponCreateValidate;
import com.mdd.admin.validate.coupon.CouponIssueValidate;
import com.mdd.admin.validate.coupon.CouponSearchValidate;
import com.mdd.admin.vo.coupon.CouponDetailVo;
import com.mdd.admin.vo.coupon.CouponListedVo;
import com.mdd.admin.vo.coupon.CouponStudentVo;
import com.mdd.common.core.PageResult;

import java.util.List;

public interface ICouponService {
    PageResult<CouponListedVo> list(PageValidate pageValidate, CouponSearchValidate searchValidate);

    CouponDetailVo detail(Integer id);

    void add(CouponCreateValidate createValidate, Integer adminId);

    void del(Integer id);

    /**
     * 按模板批量发券。
     *
     * @return 本次实际生成（或幂等命中的上一次结果）的券数量
     */
    int issue(CouponIssueValidate issueValidate, Integer adminId);

    /**
     * 发券/筛券用的学员检索。
     */
    List<CouponStudentVo> students(String keyword);
}
