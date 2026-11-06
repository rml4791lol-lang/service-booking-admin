/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.web.multipart.MultipartFile
 */
package com.mdd.common.plugin.storage;

import com.mdd.common.config.GlobalConfig;
import com.mdd.common.exception.OperateException;
import com.mdd.common.plugin.storage.UploadFilesVo;
import com.mdd.common.plugin.storage.engine.AliyunStorage;
import com.mdd.common.plugin.storage.engine.LocalStorage;
import com.mdd.common.plugin.storage.engine.QcloudStorage;
import com.mdd.common.plugin.storage.engine.QiniuStorage;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class StorageDriver {
    /** 文档/素材类附件允许的扩展名（#58）：禁止 html/js/svg/jsp 等可被浏览器执行或服务端解释的类型。 */
    private static final List<String> UPLOAD_DOC_EXT = Arrays.asList(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "csv", "zip", "rar", "7z");
    /** 文档/素材类附件大小上限：20MB（spring multipart 的 100MB 是外层兜底）。 */
    private static final long UPLOAD_DOC_MAX_SIZE = 20L * 1024L * 1024L;

    private final String engine = ConfigUtils.get("storage", "default", "local");
    private final Map<String, String> config;

    public StorageDriver() {
        Map<String, String> config = ConfigUtils.getMap("storage", this.engine);
        if (config == null) {
            config = new HashMap<String, String>();
        }
        this.config = config;
    }

    public UploadFilesVo upload(MultipartFile multipartFile, String folder, Integer type) {
        this.checkFile(multipartFile, type);
        String key = this.buildSaveName(multipartFile);
        switch (this.engine) {
            case "local": {
                LocalStorage local = new LocalStorage();
                local.upload(multipartFile, key, folder);
                break;
            }
            case "qiniu": {
                QiniuStorage qiniu = new QiniuStorage(this.config);
                qiniu.upload(multipartFile, folder + "/" + key);
                break;
            }
            case "aliyun": {
                AliyunStorage aliyun = new AliyunStorage(this.config);
                aliyun.upload(multipartFile, folder + "/" + key);
                break;
            }
            case "qcloud": {
                QcloudStorage qcloud = new QcloudStorage(this.config);
                qcloud.upload(multipartFile, folder + "/" + key);
            }
        }
        String origFileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
        String origFileExt = this.extractExtension(origFileName);
        String newFileName = folder + "/" + key;
        UploadFilesVo vo = new UploadFilesVo();
        vo.setId(0);
        vo.setName(multipartFile.getOriginalFilename());
        vo.setSize(multipartFile.getSize());
        vo.setExt(origFileExt);
        vo.setUrl(newFileName);
        vo.setPath(UrlUtils.toAbsoluteUrl(newFileName));
        return vo;
    }

    private String buildSaveName(MultipartFile multipartFile) {
        String name = multipartFile.getOriginalFilename();
        String ext = this.extractExtension(name);
        if (ext.isEmpty()) {
            throw new OperateException("\u4e0d\u652f\u6301\u7684\u6587\u4ef6\u7c7b\u578b");
        }
        String date = TimeUtils.timestampToDate(TimeUtils.timestamp(), "yyyyMMdd");
        return date + "/" + UUID.randomUUID() + "." + ext;
    }

    /**
     * 取小写扩展名（不含点）。文件名没有扩展名时返回空串，不再抛 StringIndexOutOfBoundsException。
     */
    private String extractExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int index = fileName.lastIndexOf('.');
        if (index < 0 || index == fileName.length() - 1) {
            return "";
        }
        String ext = fileName.substring(index + 1).toLowerCase();
        return ext.length() > 10 ? "" : ext;
    }

    private void checkFile(MultipartFile multipartFile, Integer type) {
        String fileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
        String fileExt = this.extractExtension(fileName);
        long fileSize = multipartFile.getSize();
        if (fileExt.isEmpty()) {
            throw new OperateException("\u4e0d\u652f\u6301\u7684\u6587\u4ef6\u7c7b\u578b");
        }
        if (type != null && type == 30) {
            // 文档/素材：此前没有扩展名与大小校验（type=30 无分支），等于任意文件都能上传。
            if (!UPLOAD_DOC_EXT.contains(fileExt)) {
                throw new OperateException("\u4e0d\u88ab\u652f\u6301\u7684\u6269\u5c55:" + fileExt);
            }
            if (fileSize > UPLOAD_DOC_MAX_SIZE) {
                throw new OperateException("\u4e0a\u4f20\u6587\u4ef6\u4e0d\u80fd\u8d85\u51fa\u9650\u5236:20M");
            }
        }
        if (type == 10) {
            if (!Arrays.asList(GlobalConfig.uploadImageExt).contains(fileExt)) {
                throw new OperateException("\u4e0d\u88ab\u652f\u6301\u7684\u6269\u5c55:" + fileExt);
            }
            if (fileSize > (long)GlobalConfig.uploadImageSize.intValue()) {
                throw new OperateException("\u4e0a\u4f20\u56fe\u7247\u4e0d\u80fd\u8d85\u51fa\u9650\u5236:" + GlobalConfig.uploadImageSize / 1024 / 1024 + "M");
            }
        } else if (type == 20) {
            if (!Arrays.asList(GlobalConfig.uploadVideoExt).contains(fileExt)) {
                throw new OperateException("\u4e0d\u88ab\u652f\u6301\u7684\u6269\u5c55:" + fileExt);
            }
            if (fileSize > (long)GlobalConfig.uploadVideoSize.intValue()) {
                throw new OperateException("\u4e0a\u4f20\u89c6\u9891\u4e0d\u80fd\u8d85\u51fa\u9650\u5236:" + GlobalConfig.uploadVideoSize / 1024 / 1024 + "M");
            }
        }
    }

    public void deleteFile(String filePath) {
        switch (this.engine) {
            case "local": {
                LocalStorage local = new LocalStorage();
                local.delete(filePath);
                break;
            }
            case "qiniu": {
                QiniuStorage qiniu = new QiniuStorage(this.config);
                qiniu.delete(filePath);
                break;
            }
            case "aliyun": {
                AliyunStorage aliyun = new AliyunStorage(this.config);
                aliyun.delete(filePath);
                break;
            }
            case "qcloud": {
                QcloudStorage qcloud = new QcloudStorage(this.config);
                qcloud.delete(filePath);
            }
        }
    }
}

