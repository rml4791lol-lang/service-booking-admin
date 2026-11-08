/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.CouponTemplateController
 *  com.mdd.admin.service.ICouponTemplateService
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.coupon.CouponTemplateCreateValidate
 *  com.mdd.admin.vo.coupon.CouponTemplateListedVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller;

import com.mdd.admin.service.ICouponTemplateService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.coupon.CouponTemplateListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/coupon_template"})
@Api(tags={"\u4f18\u60e0\u5238\u6a21\u677f\u7ba1\u7406"})
public class CouponTemplateController {
    /** #208：老抵扣券体系已下线（父票 #205）。模板读接口保留，增删改统一返回该文案。 */
    private static final String LEGACY_RETIRED_MSG = "老抵扣券体系已下线，请使用券商品发券";

    @Resource
    ICouponTemplateService iCouponTemplateService;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u6a21\u677f\u5217\u8868")
    public AjaxResult<Object> list(@Validated PageValidate pageValidate, @RequestParam(required=false) String name, @RequestParam(required=false) Integer status) {
        PageResult list = this.iCouponTemplateService.list(pageValidate, name, status);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/all"})
    @ApiOperation(value="\u5168\u90e8\u542f\u7528\u7684\u6a21\u677f(\u4e0b\u62c9\u7528)")
    public AjaxResult<List<CouponTemplateListedVo>> all() {
        List list = this.iCouponTemplateService.all();
        return AjaxResult.success(list);
    }

    @PostMapping(value={"/add"})
    @ApiOperation(value="新增模板(\u5df2\u4e0b\u7ebf)")
    public AjaxResult<Object> add() {
        return AjaxResult.failed(LEGACY_RETIRED_MSG);
    }

    @PostMapping(value={"/edit"})
    @ApiOperation(value="编辑模板(\u5df2\u4e0b\u7ebf)")
    public AjaxResult<Object> edit() {
        return AjaxResult.failed(LEGACY_RETIRED_MSG);
    }

    @PostMapping(value={"/del"})
    @ApiOperation(value="删除模板(\u5df2\u4e0b\u7ebf)")
    public AjaxResult<Object> del() {
        return AjaxResult.failed(LEGACY_RETIRED_MSG);
    }
}

