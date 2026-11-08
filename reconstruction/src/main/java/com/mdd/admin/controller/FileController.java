/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.FileController
 *  com.mdd.admin.service.IFileService
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.IdsValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.file.FileCateValidate
 *  com.mdd.admin.validate.file.FileMoveValidate
 *  com.mdd.admin.validate.file.FileRenameValidate
 *  com.mdd.admin.validate.file.FileSearchValidate
 *  com.mdd.admin.vo.album.FileVo
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
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IFileService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.IdsValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.file.FileCateValidate;
import com.mdd.admin.validate.file.FileMoveValidate;
import com.mdd.admin.validate.file.FileRenameValidate;
import com.mdd.admin.validate.file.FileSearchValidate;
import com.mdd.admin.vo.album.FileVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/file"})
@Api(tags={"\u6587\u4ef6\u6570\u636e\u7ba1\u7406"})
public class FileController {
    @Resource
    IFileService fileService;

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u6587\u4ef6\u5217\u8868")
    public AjaxResult<PageResult<FileVo>> fileList(@Validated PageValidate pageValidate, @Validated FileSearchValidate searchValidate) {
        PageResult voPageResult = this.fileService.fileList(pageValidate, searchValidate);
        return AjaxResult.success(voPageResult);
    }

    @Log(title="\u6587\u4ef6\u91cd\u547d\u540d")
    @PostMapping(value={"/rename"})
    @ApiOperation(value="\u6587\u4ef6\u91cd\u547d\u540d")
    public AjaxResult<Object> rename(@Validated @RequestBody FileRenameValidate renameValidate) {
        this.fileService.fileRename(renameValidate.getId(), renameValidate.getName());
        return AjaxResult.success();
    }

    @Log(title="\u6587\u4ef6\u79fb\u52a8")
    @PostMapping(value={"/move"})
    @ApiOperation(value="\u76f8\u518c\u6587\u4ef6\u79fb\u52a8")
    public AjaxResult<Object> move(@Validated @RequestBody FileMoveValidate moveValidate) {
        this.fileService.fileMove(moveValidate.getIds(), moveValidate.getCid());
        return AjaxResult.success();
    }

    @Log(title="\u6587\u4ef6\u5220\u9664")
    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u76f8\u518c\u6587\u4ef6\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdsValidate idsValidate) {
        this.fileService.fileDel(idsValidate.getIds());
        return AjaxResult.success();
    }

    @GetMapping(value={"/listCate"})
    @ApiOperation(value="\u6587\u4ef6\u5206\u7c7b\u5217\u8868")
    public AjaxResult<JSONObject> cateList(@Validated FileSearchValidate searchValidate) {
        JSONObject result = this.fileService.cateList(searchValidate);
        return AjaxResult.success(result);
    }

    @Log(title="\u5206\u7c7b\u65b0\u589e")
    @PostMapping(value={"/addCate"})
    @ApiOperation(value="\u5206\u7c7b\u65b0\u589e")
    public AjaxResult<Object> cateAdd(@Validated @RequestBody FileCateValidate cateValidate) {
        this.fileService.cateAdd(cateValidate);
        return AjaxResult.success();
    }

    @Log(title="\u76f8\u518c\u5206\u7c7b\u91cd\u547d\u540d")
    @PostMapping(value={"/editCate"})
    @ApiOperation(value="\u76f8\u518c\u5206\u7c7b\u91cd\u547d\u540d")
    public AjaxResult<Object> cateRename(@Validated @RequestBody FileRenameValidate renameValidate) {
        this.fileService.cateRename(renameValidate.getId(), renameValidate.getName());
        return AjaxResult.success();
    }

    @Log(title="\u76f8\u518c\u5206\u7c7b\u5220\u9664")
    @PostMapping(value={"/delCate"})
    @ApiOperation(value="\u76f8\u518c\u5206\u7c7b\u5220\u9664")
    public AjaxResult<Object> cateDel(@Validated @RequestBody IdValidate idValidate) {
        this.fileService.cateDel(idValidate.getId());
        return AjaxResult.success();
    }
}

