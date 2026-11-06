/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.StudentController
 *  com.mdd.front.service.IStudentService
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IStudentService;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/student"})
public class StudentController {
    @Resource
    IStudentService studentService;

    @GetMapping(value={"/list"})
    public AjaxResult<Object> list() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(this.studentService.list(userId));
    }

    @GetMapping(value={"/default"})
    public AjaxResult<Object> getDefault() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Map data = this.studentService.getDefault(userId);
        return AjaxResult.success((data != null ? data : null));
    }

    @PostMapping(value={"/add"})
    public AjaxResult<Object> add(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(this.studentService.add(userId, params));
    }

    @PostMapping(value={"/edit"})
    public AjaxResult<Object> edit(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.studentService.edit(userId, params);
        return AjaxResult.success();
    }

    @PostMapping(value={"/del"})
    public AjaxResult<Object> del(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(this.studentService.del(userId, params));
    }

    @PostMapping(value={"/setDefault"})
    public AjaxResult<Object> setDefault(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.studentService.setDefault(userId, params);
        return AjaxResult.success();
    }
}

