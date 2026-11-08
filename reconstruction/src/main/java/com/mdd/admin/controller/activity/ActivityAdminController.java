package com.mdd.admin.controller.activity;

import cn.dev33.satoken.stp.StpUtil;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IActivityAdminService;
import com.mdd.admin.validate.activity.ActivityCreateValidate;
import com.mdd.admin.validate.activity.ActivityStatusValidate;
import com.mdd.admin.validate.activity.ActivityUpdateValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.activity.ActivityEnrollAlertDetailVo;
import com.mdd.admin.vo.activity.ActivityEnrollAlertListVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.activity.Activity;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
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
 * 活动 / 报名链接 / 报名名单 / 开班申请后台（T29.7 #27、T30.6 #38）。
 *
 * <p>权限字符串与 URI 一一对应：activity/enroll/options、activity/enroll/link、
 * activity/enroll/links、activity/enroll/link/status、activity/enroll/link/expire、
 * activity/enroll/list、activity/enroll/export、activity/apply/list。</p>
 */
@RestController
@RequestMapping(value = "/adminapi/activity")
@Api(tags = "活动报名链接、名单与开班申请（后台）")
public class ActivityAdminController {

    @Resource
    IActivityAdminService iActivityAdminService;

    @GetMapping(value = "/enroll/options")
    @ApiOperation(value = "可生成报名链接的课程场次")
    public AjaxResult<List<Map<String, Object>>> enrollOptions(
            @RequestParam(value = "keyword", required = false) String keyword) {
        return AjaxResult.success(iActivityAdminService.enrollOptions(keyword));
    }

    @PostMapping(value = "/enroll/link")
    @ApiOperation(value = "生成/复用报名链接")
    @Log(title = "生成报名链接")
    public AjaxResult<Map<String, Object>> createEnrollLink(@RequestBody Map<String, Object> params) {
        Integer sessionId = intValue(params.get("sessionId"));
        Integer expireDays = intValue(params.get("expireDays"));
        return AjaxResult.success(iActivityAdminService.createEnrollLink(
                LikeAdminThreadLocal.getAdminId(), sessionId, expireDays));
    }

    @GetMapping(value = "/enroll/links")
    @ApiOperation(value = "报名链接列表（含提交数/有效期/状态）")
    public AjaxResult<PageResult<Map<String, Object>>> enrollLinks(
            @Validated PageValidate pageValidate,
            @RequestParam(value = "sessionId", required = false) Integer sessionId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return AjaxResult.success(iActivityAdminService.enrollLinks(
                pageValidate, sessionId, status, keyword));
    }

    @PostMapping(value = "/enroll/link/status")
    @ApiOperation(value = "停用/启用报名链接")
    @Log(title = "停用/启用报名链接")
    public AjaxResult<Map<String, Object>> updateEnrollLinkStatus(
            @RequestBody Map<String, Object> params) {
        return AjaxResult.success(iActivityAdminService.updateEnrollLinkStatus(
                LikeAdminThreadLocal.getAdminId(), intValue(params.get("id")),
                intValue(params.get("status"))));
    }

    @PostMapping(value = "/enroll/link/expire")
    @ApiOperation(value = "设置报名链接有效期")
    @Log(title = "设置报名链接有效期")
    public AjaxResult<Map<String, Object>> updateEnrollLinkExpire(
            @RequestBody Map<String, Object> params) {
        return AjaxResult.success(iActivityAdminService.updateEnrollLinkExpire(
                LikeAdminThreadLocal.getAdminId(), intValue(params.get("id")),
                intValue(params.get("expireDays"))));
    }

    @GetMapping(value = "/enroll/list")
    @ApiOperation(value = "报名名单（筛选 + 去重汇总）")
    public AjaxResult<PageResult<Map<String, Object>>> enrollmentList(
            @Validated PageValidate pageValidate,
            @RequestParam(value = "sessionId", required = false) Integer sessionId,
            @RequestParam(value = "linkId", required = false) Integer linkId,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "studentName", required = false) String studentName,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "dateStart", required = false) String dateStart,
            @RequestParam(value = "dateEnd", required = false) String dateEnd) {
        return AjaxResult.success(iActivityAdminService.enrollmentList(
                pageValidate, sessionId, linkId, token, keyword, phone, studentName,
                status, dateStart, dateEnd));
    }

    @GetMapping(value = "/enroll/export")
    @ApiOperation(value = "导出报名名单 Excel")
    @Log(title = "导出报名名单")
    public AjaxResult<Map<String, Object>> exportEnrollments(
            @RequestParam(value = "sessionId", required = false) Integer sessionId,
            @RequestParam(value = "linkId", required = false) Integer linkId,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "studentName", required = false) String studentName,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "dateStart", required = false) String dateStart,
            @RequestParam(value = "dateEnd", required = false) String dateEnd) {
        Map<String, Object> result = iActivityAdminService.exportEnrollments(
                sessionId, linkId, token, keyword, phone, studentName, status, dateStart, dateEnd);
        // code=2：后台前端 request 钩子按 data.url 打开下载地址（与用户/教练导出既有约定一致）。
        return AjaxResult.success(2, result, ErrorEnum.SHOW_MSG.getCode());
    }

    @GetMapping(value = "/apply/list")
    @ApiOperation(value = "开班申请聚合列表（街道+课程类型）")
    public AjaxResult<PageResult<Map<String, Object>>> applyList(
            @Validated PageValidate pageValidate,
            @RequestParam(value = "streetId", required = false) Integer streetId,
            @RequestParam(value = "courseType", required = false) String courseType,
            @RequestParam(value = "status", required = false) String status) {
        return AjaxResult.success(iActivityAdminService.applyGroups(
                pageValidate, streetId, courseType, status));
    }

    // ---- 活动报名超员待处理告警（#61） ------------------------------------
    // 新路由没有独立菜单项，沿用页面权限 activity/enroll/list 做人工校验；
    // 超级管理员（admin id=1）放行，与 LikeAdminInterceptor 约定一致。

    @NotPower
    @GetMapping(value = "/exception/list")
    @ApiOperation(value = "异常报名列表（满员后支付/超员生效）")
    public AjaxResult<PageResult<ActivityEnrollAlertListVo>> exceptionList(
            @Validated PageValidate pageValidate,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "activityId", required = false) Integer activityId,
            @RequestParam(value = "keyword", required = false) String keyword) {
        checkEnrollPermission();
        return AjaxResult.success(iActivityAdminService.enrollExceptions(
                pageValidate, status, activityId, keyword));
    }

    @NotPower
    @GetMapping(value = "/exception/detail")
    @ApiOperation(value = "异常报名详情")
    public AjaxResult<ActivityEnrollAlertDetailVo> exceptionDetail(
            @RequestParam(value = "id", required = false) Integer id) {
        checkEnrollPermission();
        return AjaxResult.success(iActivityAdminService.enrollExceptionDetail(id));
    }

    @Log(title = "处理异常报名")
    @NotPower
    @PostMapping(value = "/exception/handle")
    @ApiOperation(value = "标记异常报名已处理")
    public AjaxResult<Map<String, Object>> exceptionHandle(@RequestBody Map<String, Object> params) {
        checkPermission("activity/enroll/handle");
        return AjaxResult.success(iActivityAdminService.handleEnrollException(
                LikeAdminThreadLocal.getAdminId(), intValue(params.get("id")),
                params.get("remark") == null ? "" : String.valueOf(params.get("remark"))));
    }

    @Log(title = "异常报名退款")
    @NotPower
    @PostMapping(value = "/exception/refund")
    @ApiOperation(value = "异常报名发起退款")
    public AjaxResult<Map<String, Object>> exceptionRefund(@RequestBody Map<String, Object> params) {
        checkPermission("activity/enroll/refund");
        return AjaxResult.success(iActivityAdminService.refundEnrollException(
                LikeAdminThreadLocal.getAdminId(), intValue(params.get("id")),
                params.get("remark") == null ? "" : String.valueOf(params.get("remark"))));
    }

    // ---- 活动报名名单（含支付状态） ---------------------------------------
    // 与异常报名一致：新路由无独立菜单项，沿用页面权限 activity/enroll/list。

    @NotPower
    @GetMapping(value = "/signup/list")
    @ApiOperation(value = "活动报名名单（含支付状态）")
    public AjaxResult<PageResult<Map<String, Object>>> activitySignupList(
            @Validated PageValidate pageValidate,
            @RequestParam(value = "activityId", required = false) Integer activityId,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "payStatus", required = false) Integer payStatus,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "dateStart", required = false) String dateStart,
            @RequestParam(value = "dateEnd", required = false) String dateEnd) {
        checkEnrollPermission();
        return AjaxResult.success(iActivityAdminService.activitySignupList(pageValidate, activityId,
                status, payStatus, keyword, dateStart, dateEnd));
    }

    @NotPower
    @Log(title = "取消活动报名")
    @PostMapping(value = "/enroll/cancel")
    @ApiOperation(value = "后台取消活动报名（免费释放名额，付费生成退款申请）")
    public AjaxResult<Map<String, Object>> cancelActivitySignup(@RequestBody Map<String, Object> params) {
        checkPermission("activity/enroll/cancel");
        return AjaxResult.success(iActivityAdminService.cancelActivitySignup(
                LikeAdminThreadLocal.getAdminId(), intValue(params.get("signupId")),
                params.get("remark") == null ? "" : String.valueOf(params.get("remark"))));
    }

    @NotPower
    @Log(title = "导出活动报名名单")
    @GetMapping(value = "/signup/export")
    @ApiOperation(value = "导出活动报名名单 Excel（含支付状态）")
    public AjaxResult<Map<String, Object>> exportActivitySignups(
            @RequestParam(value = "activityId", required = false) Integer activityId,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "payStatus", required = false) Integer payStatus,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "dateStart", required = false) String dateStart,
            @RequestParam(value = "dateEnd", required = false) String dateEnd) {
        checkEnrollPermission();
        Map<String, Object> result = iActivityAdminService.exportActivitySignups(activityId,
                status, payStatus, keyword, dateStart, dateEnd);
        return AjaxResult.success(2, result, ErrorEnum.SHOW_MSG.getCode());
    }

    // ---- 活动管理 CRUD（#186） --------------------------------------------
    // 权限走手写校验（@NotPower）：菜单权限字符串按计划为 activity/activity/*，
    // 而拦截器默认按 URI 派生 activity/*，两者不一致，故显式校验种子里的权限点；
    // 列表/详情沿用页面权限 activity/enroll/list（超级管理员放行）。

    @NotPower
    @GetMapping(value = "/list")
    @ApiOperation(value = "活动列表（软删过滤，关键词/状态筛选）")
    public AjaxResult<PageResult<Activity>> list(
            @Validated PageValidate pageValidate,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status) {
        checkEnrollPermission();
        return AjaxResult.success(iActivityAdminService.activityList(pageValidate, keyword, status));
    }

    @NotPower
    @GetMapping(value = "/detail")
    @ApiOperation(value = "活动详情")
    public AjaxResult<Map<String, Object>> detail(
            @RequestParam(value = "id", required = false) Integer id) {
        checkEnrollPermission();
        return AjaxResult.success(iActivityAdminService.activityDetail(id));
    }

    @Log(title = "新增活动")
    @NotPower
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增活动")
    public AjaxResult<Object> add(@Validated @RequestBody ActivityCreateValidate createValidate) {
        checkPermission("activity/activity/add");
        iActivityAdminService.addActivity(createValidate);
        return AjaxResult.success();
    }

    @Log(title = "编辑活动")
    @NotPower
    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑活动")
    public AjaxResult<Object> edit(@Validated @RequestBody ActivityUpdateValidate updateValidate) {
        checkPermission("activity/activity/edit");
        iActivityAdminService.editActivity(updateValidate);
        return AjaxResult.success();
    }

    @Log(title = "活动上下架")
    @NotPower
    @PostMapping(value = "/status")
    @ApiOperation(value = "活动上下架")
    public AjaxResult<Object> status(@Validated @RequestBody ActivityStatusValidate statusValidate) {
        checkPermission("activity/activity/status");
        iActivityAdminService.updateActivityStatus(statusValidate.getId(), statusValidate.getStatus());
        return AjaxResult.success();
    }

    @Log(title = "删除活动")
    @NotPower
    @PostMapping(value = "/del")
    @ApiOperation(value = "删除活动（已有已支付报名时拒绝）")
    public AjaxResult<Object> del(@RequestBody Map<String, Integer> params) {
        checkPermission("activity/activity/del");
        iActivityAdminService.deleteActivity(params.get("id"));
        return AjaxResult.success();
    }

    private void checkEnrollPermission() {
        checkPermission("activity/enroll/list");
    }

    private void checkPermission(String permission) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId != null && adminId == 1) {
            return;
        }
        boolean allowed;
        try {
            allowed = StpUtil.hasPermission(permission);
        } catch (Exception e) {
            allowed = false;
        }
        if (!allowed) {
            throw new LoginException(ErrorEnum.NO_PERMISSION.getCode(), ErrorEnum.NO_PERMISSION.getMsg());
        }
    }

    private Integer intValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            String text = String.valueOf(value).trim();
            return text.isEmpty() ? null : Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
