/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.CourseReviewController
 *  com.mdd.front.service.ICourseReviewService
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
import com.mdd.front.service.ICourseReviewService;
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
@RequestMapping(value={"/api/course_review"})
@Api(tags={"\u8bfe\u7a0b\u8bc4\u4ef7"})
public class CourseReviewController {
    @Resource
    ICourseReviewService iCourseReviewService;

    @PostMapping(value={"/submit"})
    @ApiOperation(value="\u63d0\u4ea4\u8bc4\u4ef7")
    public AjaxResult<Object> submit(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iCourseReviewService.submit(userId, params);
        return AjaxResult.success();
    }

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u8bc4\u4ef7\u5217\u8868")
    public AjaxResult<List<Map<String, Object>>> list(@RequestParam Integer courseClassId) {
        List list = this.iCourseReviewService.list(courseClassId);
        return AjaxResult.success(list);
    }

    @PostMapping(value={"/interact"})
    @ApiOperation(value="\u8bc4\u4ef7\u4ea4\u4e92(\u63a8\u8350/\u70b9\u8d5e)")
    public AjaxResult<Object> interact(@RequestBody Map<String, Object> params) {
        Integer id = this.parseInt(params.get("id"));
        Integer type = this.parseInt(params.get("type"));
        if (id == null || id <= 0 || type == null) {
            return AjaxResult.failed("\u53c2\u6570\u9519\u8bef");
        }
        this.iCourseReviewService.interact(id, type);
        return AjaxResult.success();
    }

    @PostMapping(value={"/reply"})
    @ApiOperation(value="\u6559\u7ec3\u56de\u590d\u8bc4\u4ef7")
    public AjaxResult<Object> reply(@RequestBody Map<String, Object> params) {
        Integer id = this.parseInt(params.get("id"));
        if (id == null || id <= 0) {
            return AjaxResult.failed("\u53c2\u6570\u9519\u8bef");
        }
        Object rawContent = params.get("content");
        String content = rawContent == null ? "" : String.valueOf(rawContent);
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        this.iCourseReviewService.reply(instructorId, id, content);
        return AjaxResult.success();
    }

    private Integer parseInt(Object value) {
        if (value == null) {
            return null;
        }
        try {
            String text = String.valueOf(value).trim();
            return text.isEmpty() ? null : Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

