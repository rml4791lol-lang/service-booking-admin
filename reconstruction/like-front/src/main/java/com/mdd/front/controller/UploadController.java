/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.enums.AlbumEnum
 *  com.mdd.common.enums.FileEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.plugin.storage.StorageDriver
 *  com.mdd.common.plugin.storage.UploadFilesVo
 *  com.mdd.common.util.StringUtils
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.UploadController
 *  com.mdd.front.service.IFileService
 *  com.mdd.front.vo.upload.UploadImagesVo
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 *  org.springframework.web.multipart.MultipartRequest
 */
package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.AlbumEnum;
import com.mdd.common.enums.FileEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.plugin.storage.StorageDriver;
import com.mdd.common.plugin.storage.UploadFilesVo;
import com.mdd.common.util.StringUtils;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IFileService;
import com.mdd.front.vo.upload.UploadImagesVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

@RestController
@RequestMapping(value={"/api/upload"})
@Api(tags={"\u4e0a\u4f20\u7ba1\u7406"})
public class UploadController {
    @Resource
    IFileService iFileService;

    @PostMapping(value={"/image"}, produces={"application/json;charset=utf-8"})
    @ApiOperation(value="\u4e0a\u4f20\u56fe\u7247")
    public AjaxResult<UploadImagesVo> image(HttpServletRequest request) {
        MultipartFile multipartFile;
        try {
            multipartFile = ((MultipartRequest)request).getFile("file");
        }
        catch (Exception e) {
            throw new OperateException("\u8bf7\u6b63\u786e\u9009\u62e9\u4e0a\u4f20\u56fe\u7247!");
        }
        if (multipartFile == null) {
            throw new OperateException("\u8bf7\u9009\u62e9\u4e0a\u4f20\u56fe\u7247!");
        }
        String contentType = multipartFile.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new OperateException("仅允许上传图片文件");
        }
        String dir = request.getParameter("dir");
        if (StringUtils.isEmpty(dir)) {
            dir = "image";
        }
        if (!("image".equals(dir) || "avatar".equals(dir) || "checkin".equals(dir) || "cert".equals(dir))) {
            throw new OperateException("上传目录不合法");
        }
        String folder = "image/" + dir;
        StorageDriver storageDriver = new StorageDriver();
        UploadFilesVo vo = storageDriver.upload(multipartFile, folder, Integer.valueOf(AlbumEnum.IMAGE.getCode()));
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cid", "0");
        params.put("uid", String.valueOf(LikeFrontThreadLocal.getUserId()));
        params.put("source", String.valueOf(FileEnum.SOURCE_USER.getCode()));
        params.put("type", String.valueOf(FileEnum.IMAGE_TYPE.getCode()));
        params.put("name", vo.getName());
        params.put("url", vo.getUrl());
        Integer id = this.iFileService.fileAdd(params);
        UploadImagesVo upVo = new UploadImagesVo();
        upVo.setUrl(vo.getUrl());
        upVo.setUri(vo.getPath());
        upVo.setId(id);
        upVo.setType(Integer.valueOf(FileEnum.IMAGE_TYPE.getCode()));
        upVo.setName(vo.getName());
        upVo.setCid(Integer.valueOf(0));
        return AjaxResult.success(upVo);
    }
}
