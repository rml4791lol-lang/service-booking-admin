/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.UserCourseController
 *  com.mdd.front.service.IUserCourseService
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

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IUserCourseService;
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
@RequestMapping(value={"/api/user_course"})
@Api(tags={"\u7528\u6237\u8bfe\u7a0b"})
public class UserCourseController {
    @Resource
    IUserCourseService iUserCourseService;

    @GetMapping(value={"/myList"})
    @ApiOperation(value="\u6211\u7684\u8bfe\u7a0b\u5217\u8868")
    public AjaxResult<PageResult<Object>> myList(@RequestParam(defaultValue="1") Integer pageNo, @RequestParam(defaultValue="10") Integer pageSize) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        PageResult result = this.iUserCourseService.myList(userId, pageNo, pageSize);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/checkPurchased"})
    @ApiOperation(value="\u68c0\u67e5\u662f\u5426\u5df2\u8d2d\u4e70\u8bfe\u7a0b")
    public AjaxResult<Map<String, Object>> checkPurchased(@RequestParam Integer courseClassId) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Map result = this.iUserCourseService.checkPurchased(userId, courseClassId);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/stats"})
    @ApiOperation(value="\u7528\u6237\u8bfe\u7a0b\u7edf\u8ba1")
    public AjaxResult<Map<String, Object>> stats() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Map result = this.iUserCourseService.stats(userId);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/completedLessons"})
    @ApiOperation(value="\u5df2\u5b8c\u6210\u8bfe\u8282\u5217\u8868")
    public AjaxResult<List<Map<String, Object>>> completedLessons(@RequestParam(required=false) String keyword, @RequestParam(defaultValue="1") Integer pageNo, @RequestParam(defaultValue="10") Integer pageSize) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        List result = this.iUserCourseService.completedLessonList(userId, keyword, pageNo, pageSize);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/todayLessons"})
    @ApiOperation(value="\u4eca\u65e5\u53ef\u9884\u7ea6\u8bfe\u8282")
    public AjaxResult<List<Map<String, Object>>> todayLessons() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        List result = this.iUserCourseService.todayLessons(userId);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/upcomingLessons"})
    @ApiOperation(value="\u4ece\u4eca\u5929\u5f00\u59cb\u5f80\u540e3\u6b21\u53ef\u9884\u7ea6\u8bfe\u8282")
    public AjaxResult<List<Map<String, Object>>> upcomingLessons() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        List result = this.iUserCourseService.upcomingLessons(userId);
        return AjaxResult.success(result);
    }

    @PostMapping(value={"/booking"})
    @ApiOperation(value="\u9884\u7ea6\u8bfe\u8282")
    public AjaxResult<Object> booking(@RequestBody Map<String, Integer> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer lessonId = params.get("lessonId");
        Integer studentId = params.get("studentId");
        this.iUserCourseService.bookLesson(userId, lessonId, studentId);
        return AjaxResult.success();
    }

    @PostMapping(value={"/cancelBooking"})
    @ApiOperation(value="\u53d6\u6d88\u9884\u7ea6\u8bfe\u8282")
    public AjaxResult<Object> cancelBooking(@RequestBody Map<String, Integer> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer lessonId = params.get("lessonId");
        Integer studentId = params.get("studentId");
        this.iUserCourseService.cancelBooking(userId, lessonId, studentId);
        return AjaxResult.success();
    }
}

