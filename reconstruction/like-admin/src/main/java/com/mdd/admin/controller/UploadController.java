/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.aop.aspect.RequestType
 *  com.mdd.admin.controller.UploadController
 *  com.mdd.admin.service.IFileService
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.enums.AlbumEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.plugin.storage.StorageDriver
 *  com.mdd.common.plugin.storage.UploadFilesVo
 *  com.mdd.common.util.StringUtils
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 */
package com.mdd.admin.controller;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.aop.aspect.RequestType;
import com.mdd.admin.service.IFileService;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.AlbumEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.plugin.storage.StorageDriver;
import com.mdd.common.plugin.storage.UploadFilesVo;
import com.mdd.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.LinkedHashMap;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/adminapi/upload"})
@Api(tags={"\u4e0a\u4f20\u6587\u4ef6\u7ba1\u7406"})
public class UploadController {
    @Resource
    IFileService fileService;

    @Log(title="\u4e0a\u4f20\u56fe\u7247", requestType=RequestType.File)
    @PostMapping(value={"/image"})
    @NotPower
    @ApiOperation(value="\u4e0a\u4f20\u56fe\u7247")
    public AjaxResult image(@RequestParam(value="file") MultipartFile[] files, @RequestParam(value="cid") String requestCid) {
        String cid;
        String string = cid = StringUtils.isNotEmpty((String)requestCid) ? requestCid : "0";
        if (files == null || files.length == 0) {
            throw new OperateException("\u8bf7\u9009\u62e9\u4e0a\u4f20\u56fe\u7247!");
        }
        for (int i = 0; i < files.length; ++i) {
            StorageDriver storageDriver = new StorageDriver();
            UploadFilesVo vo = storageDriver.upload(files[i], "image", Integer.valueOf(AlbumEnum.IMAGE.getCode()));
            LinkedHashMap<String, String> album = new LinkedHashMap<String, String>();
            album.put("aid", String.valueOf(LikeAdminThreadLocal.getAdminId()));
            album.put("cid", cid);
            album.put("type", String.valueOf(AlbumEnum.IMAGE.getCode()));
            album.put("url", vo.getUrl());
            album.put("name", vo.getName());
            Integer id = this.fileService.fileAdd(album);
            vo.setId(id);
        }
        return AjaxResult.success();
    }

    @Log(title="\u4e0a\u4f20\u56fe\u7247", requestType=RequestType.File)
    @PostMapping(value={"/file"})
    @NotPower
    @ApiOperation(value="\u4e0a\u4f20\u6587\u4ef6")
    public AjaxResult file(@RequestParam(value="file") MultipartFile[] files, @RequestParam(value="cid") String requestCid) {
        String cid;
        String string = cid = StringUtils.isNotEmpty((String)requestCid) ? requestCid : "0";
        if (files == null || files.length == 0) {
            throw new OperateException("\u8bf7\u9009\u62e9\u4e0a\u4f20\u6587\u4ef6!");
        }
        for (int i = 0; i < files.length; ++i) {
            StorageDriver storageDriver = new StorageDriver();
            UploadFilesVo vo = storageDriver.upload(files[i], "image", Integer.valueOf(AlbumEnum.Doc.getCode()));
            LinkedHashMap<String, String> album = new LinkedHashMap<String, String>();
            album.put("aid", String.valueOf(LikeAdminThreadLocal.getAdminId()));
            album.put("cid", cid);
            album.put("type", String.valueOf(AlbumEnum.Doc.getCode()));
            album.put("url", vo.getUrl());
            album.put("name", vo.getName());
            Integer id = this.fileService.fileAdd(album);
            vo.setId(id);
        }
        return AjaxResult.success();
    }

    @Log(title="\u4e0a\u4f20\u89c6\u9891", requestType=RequestType.File)
    @PostMapping(value={"/video"})
    @NotPower
    @ApiOperation(value="\u4e0a\u4f20\u89c6\u9891")
    public AjaxResult video(@RequestParam(value="file") MultipartFile[] files, @RequestParam(value="cid") String requestCid) {
        String cid;
        String string = cid = StringUtils.isNotEmpty((String)requestCid) ? requestCid : "0";
        if (files == null || files.length == 0) {
            throw new OperateException("\u8bf7\u9009\u62e9\u4e0a\u4f20\u89c6\u9891!");
        }
        for (int i = 0; i < files.length; ++i) {
            StorageDriver storageDriver = new StorageDriver();
            UploadFilesVo vo = storageDriver.upload(files[i], "video", Integer.valueOf(AlbumEnum.Video.getCode()));
            LinkedHashMap<String, String> album = new LinkedHashMap<String, String>();
            album.put("cid", cid);
            album.put("aid", String.valueOf(LikeAdminThreadLocal.getAdminId()));
            album.put("type", String.valueOf(AlbumEnum.Video.getCode()));
            album.put("url", vo.getUrl());
            album.put("name", vo.getName());
            Integer id = this.fileService.fileAdd(album);
            vo.setId(id);
        }
        return AjaxResult.success();
    }
}

