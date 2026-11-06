/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.controller.course.CourseClassController
 *  com.mdd.admin.service.ICourseClassService
 *  com.mdd.admin.service.ICourseManualConsumeService
 *  com.mdd.admin.service.ICourseTempLessonService
 *  com.mdd.admin.service.IInstructorService
 *  com.mdd.admin.service.ISportsCategoryService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.course.CourseClassCreateValidate
 *  com.mdd.admin.validate.course.CourseClassSearchValidate
 *  com.mdd.admin.validate.course.CourseClassUpdateValidate
 *  com.mdd.admin.validate.course.SportsCategoryCreateValidate
 *  com.mdd.admin.vo.course.CourseClassListedVo
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
package com.mdd.admin.controller.course;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ICourseClassService;
import com.mdd.admin.service.ICourseManualConsumeService;
import com.mdd.admin.service.ICourseTempLessonService;
import com.mdd.admin.service.IInstructorService;
import com.mdd.admin.service.ISportsCategoryService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.course.CourseClassCreateValidate;
import com.mdd.admin.validate.course.CourseClassSearchValidate;
import com.mdd.admin.validate.course.CourseClassStatusValidate;
import com.mdd.admin.validate.course.CourseClassUpdateValidate;
import com.mdd.admin.validate.course.SportsCategoryCreateValidate;
import com.mdd.admin.validate.course.SportsCategoryUpdateValidate;
import com.mdd.admin.vo.course.CourseClassListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"adminapi/course/class", "adminapi/course.class"})
@Api(tags={"\u73ed\u7ea7\u7ba1\u7406"})
public class CourseClassController {
    private static final int QUERY_FLAG_ON = 1;

    @Resource
    ICourseClassService iCourseClassService;
    @Resource
    ISportsCategoryService iSportsCategoryService;
    @Resource
    ICourseManualConsumeService iCourseManualConsumeService;
    @Resource
    ICourseTempLessonService iCourseTempLessonService;
    @Resource
    IInstructorService iInstructorService;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u73ed\u7ea7\u5217\u8868")
    public AjaxResult<PageResult<CourseClassListedVo>> list(@Validated PageValidate pageValidate, @Validated CourseClassSearchValidate searchValidate) {
        return AjaxResult.success(this.iCourseClassService.list(pageValidate, searchValidate));
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u73ed\u7ea7\u8be6\u60c5")
    public AjaxResult<Map<String, Object>> detail(@RequestParam(value="id") Integer id) {
        return AjaxResult.success(this.iCourseClassService.detail(id));
    }

    @PostMapping(value={"/add"})
    @ApiOperation(value="\u73ed\u7ea7\u65b0\u589e")
    @Log(title="新增课程班级")
    public AjaxResult<Object> add(@Validated @RequestBody CourseClassCreateValidate createValidate) {
        this.iCourseClassService.add(createValidate);
        return AjaxResult.success();
    }

    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u73ed\u7ea7\u7f16\u8f91")
    @Log(title="编辑课程班级")
    public AjaxResult<Object> edit(@Validated @RequestBody CourseClassUpdateValidate updateValidate) {
        this.iCourseClassService.edit(updateValidate);
        return AjaxResult.success();
    }

    @PostMapping(value={"/del"})
    @ApiOperation(value="\u73ed\u7ea7\u5220\u9664")
    @Log(title="删除课程班级")
    public AjaxResult<Object> del(@RequestBody Map<String, Integer> map) {
        this.iCourseClassService.del(map.get("id"));
        return AjaxResult.success();
    }

    @PostMapping(value={"/status"})
    @ApiOperation(value="课程上下架")
    @Log(title="课程上下架")
    public AjaxResult<Object> status(@Validated @RequestBody CourseClassStatusValidate statusValidate) {
        this.iCourseClassService.setShow(statusValidate.getId(), statusValidate.getIsShow());
        return AjaxResult.success();
    }

    @GetMapping(value={"/stats"})
    @ApiOperation(value="\u73ed\u7ea7\u7edf\u8ba1")
    public AjaxResult<Map<String, Object>> stats() {
        return AjaxResult.success(this.iCourseClassService.stats());
    }

    @GetMapping(value={"/categoryAll"})
    @ApiOperation(value="\u9879\u76ee\u5206\u7c7b\u5217\u8868")
    public AjaxResult<Object> categoryAll(
        @RequestParam(value="all", defaultValue="0") Integer all,
        @RequestParam(value="tree", defaultValue="0") Integer tree) {
        return AjaxResult.success(
            this.iSportsCategoryService.listAll(this.isQueryFlagOn(all), this.isQueryFlagOn(tree))
        );
    }

    private boolean isQueryFlagOn(Integer flag) {
        return flag != null && flag.intValue() == QUERY_FLAG_ON;
    }

    @PostMapping(value={"/categoryAdd"})
    @ApiOperation(value="\u9879\u76ee\u5206\u7c7b\u65b0\u589e")
    public AjaxResult<Object> categoryAdd(@Validated @RequestBody SportsCategoryCreateValidate createValidate) {
        this.iSportsCategoryService.add(createValidate);
        return AjaxResult.success();
    }

    @PostMapping(value={"/categoryEdit"})
    @ApiOperation(value="\u9879\u76ee\u5206\u7c7b\u7f16\u8f91")
    public AjaxResult<Object> categoryEdit(@Validated @RequestBody SportsCategoryUpdateValidate updateValidate) {
        this.iSportsCategoryService.edit(updateValidate);
        return AjaxResult.success();
    }

    @GetMapping(value={"/enrolledStudents"})
    @ApiOperation(value="\u73ed\u7ea7\u62a5\u540d\u5b66\u5458")
    public AjaxResult<Object> enrolledStudents(@RequestParam(value="classId") Integer classId) {
        return AjaxResult.success(this.iCourseClassService.enrolledStudents(classId));
    }

    @GetMapping(value={"/manual/list"})
    @ApiOperation(value="\u4eba\u5de5\u6263\u8bfe\u8bb0\u5f55\u5217\u8868")
    public AjaxResult<Object> manualList(@RequestParam(value="classId") Integer classId) {
        return AjaxResult.success(this.iCourseManualConsumeService.listByClass(classId));
    }

    @GetMapping(value={"/manual/instructors"})
    @ApiOperation(value="\u4eba\u5de5\u6263\u8bfe-\u5168\u90e8\u6559\u7ec3\u4e0b\u62c9")
    public AjaxResult<Object> manualInstructors() {
        return AjaxResult.success(this.iInstructorService.listAllForSelect());
    }

    @GetMapping(value={"/manual/detail"})
    @ApiOperation(value="\u4eba\u5de5\u6263\u8bfe\u8be6\u60c5")
    public AjaxResult<Object> manualDetail(@RequestParam(value="id") Integer id) {
        return AjaxResult.success(this.iCourseManualConsumeService.detail(id));
    }

    @PostMapping(value={"/manual/submit"})
    @Log(title = "人工扣课")
    @ApiOperation(value="\u63d0\u4ea4\u4eba\u5de5\u6263\u8bfe")
    public AjaxResult<Object> manualSubmit(@RequestBody Map<String, Object> params) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        this.iCourseManualConsumeService.submit(adminId, params);
        return AjaxResult.success();
    }

    @GetMapping(value={"/tempLesson/list"})
    @ApiOperation(value="\u4e34\u65f6\u52a0\u8bfe\u8bb0\u5f55\u5217\u8868")
    public AjaxResult<Object> tempLessonList(@RequestParam(value="classId") Integer classId) {
        return AjaxResult.success(this.iCourseTempLessonService.listByClass(classId));
    }

    @GetMapping(value={"/tempLesson/detail"})
    @ApiOperation(value="\u4e34\u65f6\u52a0\u8bfe\u8be6\u60c5")
    public AjaxResult<Object> tempLessonDetail(@RequestParam(value="id") Integer id) {
        return AjaxResult.success(this.iCourseTempLessonService.detail(id));
    }

    @PostMapping(value={"/tempLesson/create"})
    @Log(title = "创建临时加课")
    @ApiOperation(value="\u521b\u5efa\u4e34\u65f6\u52a0\u8bfe")
    public AjaxResult<Object> tempLessonCreate(@RequestBody Map<String, Object> params) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        this.iCourseTempLessonService.create(adminId, params);
        return AjaxResult.success();
    }

    @PostMapping(value={"/tempLesson/cancel"})
    @Log(title = "取消临时加课")
    @ApiOperation(value="\u53d6\u6d88\u4e34\u65f6\u52a0\u8bfe")
    public AjaxResult<Object> tempLessonCancel(@RequestBody Map<String, Integer> params) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        this.iCourseTempLessonService.cancel(adminId, params.get("id"));
        return AjaxResult.success();
    }
}
