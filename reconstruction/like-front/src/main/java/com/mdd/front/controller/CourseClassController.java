/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.CourseClassController
 *  com.mdd.front.service.ICourseClassService
 *  com.mdd.front.validate.course.CourseClassValidate
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.ICourseCatalogService;
import com.mdd.front.service.ICourseClassService;
import com.mdd.front.validate.course.CourseClassValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/course"})
@Api(tags={"\u8bfe\u7a0b\u73ed\u7ea7\u7ba1\u7406"})
public class CourseClassController {
    @Resource
    ICourseClassService iCourseClassService;
    @Resource
    ICourseCatalogService iCourseCatalogService;

    /**
     * 小程序课程列表。保留旧字段（name/instructor/venue/time/...），并补齐 mock 契约字段；
     * 支持小区/社区/街道/分区/类目/年龄/关键字筛选与 pageNo/pageSize 分页。
     */
    @NotLogin
    @GetMapping(value={"/list"})
    @ApiOperation(value="课程列表（小程序公开）")
    public AjaxResult<List<Map<String, Object>>> list(
            CourseClassValidate courseClassValidate,
            @RequestParam(value="areaId", required=false) Integer areaId,
            @RequestParam(value="communityId", required=false) Integer communityId,
            @RequestParam(value="streetId", required=false) Integer streetId,
            @RequestParam(value="districtId", required=false) Integer districtId,
            @RequestParam(value="categoryId", required=false) Integer categoryId,
            @RequestParam(value="age", required=false) Integer age,
            @RequestParam(value="pageNo", required=false) Integer pageNo,
            @RequestParam(value="pageSize", required=false) Integer pageSize) {
        Integer page = pageNo != null ? pageNo : courseClassValidate.getPage_no();
        Integer size = pageSize != null ? pageSize : courseClassValidate.getPage_size();
        List<Map<String, Object>> list = this.iCourseCatalogService.courses(areaId, communityId, streetId, districtId,
                categoryId, age, courseClassValidate.getKeyword(), page, size,
                courseClassValidate.getLat(), courseClassValidate.getLng());
        return AjaxResult.success(list);
    }

    @NotLogin
    @GetMapping(value={"/detail"})
    @ApiOperation(value="课程详情（含教练/场地/场次）")
    public AjaxResult<Map<String, Object>> detail(@RequestParam(value="id") Integer id) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Map detail = this.iCourseClassService.detail(id, Integer.valueOf(userId != null ? userId : 0));
        if (detail == null) {
            return AjaxResult.success(detail);
        }
        Map<String, Object> catalog = this.iCourseCatalogService.courseDetail(id);
        if (catalog != null) {
            detail.putAll(catalog);
        }
        return AjaxResult.success(detail);
    }

    @NotLogin
    @GetMapping(value={"/sessions"})
    @ApiOperation(value="课程场次列表")
    public AjaxResult<List<Map<String, Object>>> sessions(@RequestParam(value="courseId") Integer courseId) {
        return AjaxResult.success(this.iCourseCatalogService.sessions(courseId));
    }

    @NotLogin
    @GetMapping(value={"/quickSessions"})
    @ApiOperation(value="首页快捷场次（可约）")
    public AjaxResult<List<Map<String, Object>>> quickSessions(
            @RequestParam(value="limit", required=false) Integer limit) {
        return AjaxResult.success(this.iCourseCatalogService.quickSessions(limit));
    }

    @NotLogin
    @GetMapping(value={"/session"})
    @ApiOperation(value="场次详情")
    public AjaxResult<Map<String, Object>> session(@RequestParam(value="id") Integer id) {
        return AjaxResult.success(this.iCourseCatalogService.session(id));
    }

    @PostMapping(value={"/consume"})
    @ApiOperation(value="\u6838\u9500\u8bfe\u65f6(\u786e\u8ba4\u5b8c\u6210)")
    public AjaxResult<Object> consume(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iCourseClassService.consume(userId, params);
        return AjaxResult.success();
    }

    @PostMapping(value={"/applyRefund"})
    @ApiOperation(value="\u7533\u8bf7\u9000\u8bfe")
    public AjaxResult<Object> applyRefund(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iCourseClassService.applyRefund(userId, params);
        return AjaxResult.success();
    }
}

