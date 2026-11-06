package com.mdd.admin.controller;

import com.mdd.admin.service.ICouponService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.coupon.CouponSearchValidate;
import com.mdd.admin.vo.coupon.CouponDetailVo;
import com.mdd.admin.vo.coupon.CouponStudentVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/adminapi/coupon")
@Api(tags = "优惠券管理")
public class CouponController {
    /** #208：老抵扣券体系已下线（父票 #205）。读接口保留，写接口统一返回该文案。 */
    private static final String LEGACY_RETIRED_MSG = "老抵扣券体系已下线，请使用券商品发券";

    @Resource
    ICouponService iCouponService;

    @GetMapping("/list")
    @ApiOperation(value = "优惠券列表")
    public AjaxResult<Object> list(@Validated PageValidate pageValidate,
                                   @Validated CouponSearchValidate searchValidate) {
        PageResult list = iCouponService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "优惠券详情")
    public AjaxResult<CouponDetailVo> detail(@Validated @IDMust @RequestParam("id") Integer id) {
        CouponDetailVo detail = iCouponService.detail(id);
        return AjaxResult.success(detail);
    }

    @GetMapping("/students")
    @ApiOperation(value = "发券/筛券的学员检索")
    public AjaxResult<List<CouponStudentVo>> students(@RequestParam(value = "keyword", required = false) String keyword) {
        return AjaxResult.success(iCouponService.students(keyword));
    }

    @PostMapping("/add")
    @ApiOperation(value = "发放优惠券(已下线，改用券商品发券)")
    public AjaxResult<Object> add() {
        return AjaxResult.failed(LEGACY_RETIRED_MSG);
    }

    @PostMapping("/del")
    @ApiOperation(value = "作废优惠券(已下线)")
    public AjaxResult<Object> del() {
        return AjaxResult.failed(LEGACY_RETIRED_MSG);
    }

    @PostMapping("/issue")
    @ApiOperation(value = "按模板批量发放优惠券(已下线)")
    public AjaxResult<Object> issue() {
        return AjaxResult.failed(LEGACY_RETIRED_MSG);
    }
}
