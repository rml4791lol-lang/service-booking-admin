package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IBookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 小程序预约与上课记录接口（#25）。
 *
 * <p>都需要登录（请求头 {@code token}），数据按登录家长账号隔离：</p>
 * <ul>
 *   <li>{@code POST /api/booking/create}：创建预约，只占名额、不扣券；</li>
 *   <li>{@code GET /api/booking/list}：我的预约（status=1）；</li>
 *   <li>{@code GET /api/booking/next}：下一节课；</li>
 *   <li>{@code GET /api/booking/records}：上课记录（status=10，含照片/点评）；</li>
 *   <li>{@code GET /api/booking/record}：记录详情，仅本人。</li>
 * </ul>
 */
@RestController
@RequestMapping(value = "/api/booking")
@Api(tags = "预约与上课记录（小程序）")
public class BookingController {

    @Resource
    IBookingService iBookingService;

    @PostMapping(value = "/create")
    @ApiOperation(value = "创建预约（只占名额不扣券）")
    public AjaxResult<Map<String, Object>> create(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> result = iBookingService.createBooking(
                userId, intValue(params.get("sessionId")), intValue(params.get("studentId")),
                stringValue(params.get("couponId")));
        return AjaxResult.success(result);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "我的预约")
    public AjaxResult<List<Map<String, Object>>> list(
            @RequestParam(value = "studentId", required = false) Integer studentId,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iBookingService.bookings(userId, studentId, pageNo, pageSize));
    }

    @GetMapping(value = "/next")
    @ApiOperation(value = "下一节课")
    public AjaxResult<Map<String, Object>> next() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iBookingService.nextBooking(userId));
    }

    @GetMapping(value = "/records")
    @ApiOperation(value = "上课记录")
    public AjaxResult<List<Map<String, Object>>> records(
            @RequestParam(value = "studentId", required = false) Integer studentId,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iBookingService.records(userId, studentId, pageNo, pageSize));
    }

    @GetMapping(value = "/record")
    @ApiOperation(value = "记录详情（仅本人）")
    public AjaxResult<Map<String, Object>> record(@RequestParam(value = "id", required = false) String id) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iBookingService.record(userId, id));
    }

    private Integer intValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(String.valueOf(value).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
