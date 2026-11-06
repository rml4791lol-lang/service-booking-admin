/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.CourseLessonLogController
 *  com.mdd.front.service.ICourseLessonLogService
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
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.ICourseLessonLogService;
import com.mdd.common.util.ParamUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/course_lesson"})
@Api(tags={"\u6559\u7ec3\u8bfe\u540e\u603b\u7ed3(\u8fdb\u5ea6)\u7ba1\u7406"})
public class CourseLessonLogController {
    @Resource
    ICourseLessonLogService iCourseLessonLogService;

    @PostMapping(value={"/submitSummary"})
    @ApiOperation(value="\u6559\u7ec3\u63d0\u4ea4\u8bfe\u540e\u603b\u7ed3")
    public AjaxResult<Object> submitSummary(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iCourseLessonLogService.submitSummary(params, userId);
        return AjaxResult.success();
    }

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u83b7\u53d6\u8bfe\u7a0b\u7684\u6240\u6709\u8bfe\u540e\u603b\u7ed3\u8bb0\u5f55")
    public AjaxResult<Object> list(@RequestParam(value="courseClassId", required=false) String courseClassId,
                                   @RequestParam(value="pageNo", required=false) String pageNo,
                                   @RequestParam(value="pageSize", required=false) String pageSize) {
        Integer classId = ParamUtils.requiredInt(courseClassId, "\u8bfe\u7a0bID");
        Integer page = ParamUtils.intOrDefault(pageNo, 1, "\u9875\u7801");
        Integer size = ParamUtils.intOrDefault(pageSize, 20, "\u6bcf\u9875\u6761\u6570");
        Integer userId = LikeFrontThreadLocal.getUserId();
        Map result = this.iCourseLessonLogService.listSummaryPaged(classId, userId, page, size);
        return AjaxResult.success(result);
    }
}

