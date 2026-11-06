/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.CourseOrderController
 *  com.mdd.front.service.ICourseOrderService
 *  com.mdd.front.service.IUserCourseService
 *  com.mdd.front.validate.course.CourseOrderPageValidate
 *  com.mdd.front.validate.course.CourseOrderValidate
 *  com.mdd.front.vo.course.CourseOrderListVo
 *  com.mdd.front.vo.course.CourseOrderStatsVo
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.ICourseOrderService;
import com.mdd.front.service.IUserCourseService;
import com.mdd.front.validate.course.CourseOrderPageValidate;
import com.mdd.front.validate.course.CourseOrderValidate;
import com.mdd.front.vo.course.CourseOrderListVo;
import com.mdd.front.vo.course.CourseOrderStatsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/course_order"})
@Api(tags={"\u8bfe\u7a0b\u8ba2\u5355\u7ba1\u7406"})
public class CourseOrderController {
    @Resource
    ICourseOrderService iCourseOrderService;
    @Resource
    IUserCourseService iUserCourseService;

    @PostMapping(value={"/placeOrder"})
    @ApiOperation(value="\u8bfe\u7a0b\u4e0b\u5355")
    public AjaxResult<Object> placeOrder(@Validated @RequestBody CourseOrderValidate courseOrderValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        Map result = this.iCourseOrderService.placeOrder(userId, terminal, courseOrderValidate);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u8bfe\u7a0b\u8ba2\u5355\u5217\u8868")
    public AjaxResult<PageResult<CourseOrderListVo>> list(@Validated CourseOrderPageValidate pageValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        PageResult result = this.iCourseOrderService.orderList(pageValidate, userId);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/stats"})
    @ApiOperation(value="\u8bfe\u7a0b\u8ba2\u5355\u7edf\u8ba1")
    public AjaxResult<CourseOrderStatsVo> stats() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        CourseOrderStatsVo result = this.iCourseOrderService.orderStats(userId);
        return AjaxResult.success(result);
    }

    @PostMapping(value={"/cancel"})
    @ApiOperation(value="\u53d6\u6d88\u8bfe\u7a0b\u8ba2\u5355")
    public AjaxResult<Object> cancel(@RequestBody Map<String, Integer> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer orderId = params.get("id");
        this.iCourseOrderService.cancelOrder(orderId, userId);
        return AjaxResult.success();
    }

    @GetMapping(value={"/checkPurchased"})
    @ApiOperation(value="\u68c0\u67e5\u662f\u5426\u5df2\u8d2d\u4e70\u8bfe\u7a0b")
    public AjaxResult<Object> checkPurchased(Integer courseId) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Map result = this.iUserCourseService.checkPurchased(userId, courseId);
        return AjaxResult.success(result);
    }
}

