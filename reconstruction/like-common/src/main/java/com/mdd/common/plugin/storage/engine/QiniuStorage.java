/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.qiniu.common.QiniuException
 *  com.qiniu.http.Response
 *  com.qiniu.storage.BucketManager
 *  com.qiniu.storage.Configuration
 *  com.qiniu.storage.Region
 *  com.qiniu.storage.UploadManager
 *  com.qiniu.storage.model.DefaultPutRet
 *  com.qiniu.util.Auth
 *  org.springframework.web.multipart.MultipartFile
 */
package com.mdd.common.plugin.storage.engine;

import com.google.gson.Gson;
import com.mdd.common.exception.OperateException;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.io.IOException;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public class QiniuStorage {
    private final Map<String, String> config;

    public QiniuStorage(Map<String, String> config) {
        this.config = config;
    }

    public String upToken() {
        String accessKey = this.config.getOrDefault("access_key", "");
        String secretKey = this.config.getOrDefault("secret_key", "");
        String bucket = this.config.getOrDefault("bucket", "");
        Auth auth = Auth.create((String)accessKey, (String)secretKey);
        return auth.uploadToken(bucket);
    }

    public void upload(MultipartFile multipartFile, String key) {
        Configuration cfg = new Configuration(Region.region2());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(multipartFile.getBytes(), key, this.upToken());
            DefaultPutRet ret = (DefaultPutRet)new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(ret);
        }
        catch (IOException ex) {
            throw new OperateException(ex.getMessage());
        }
    }

    public void delete(String filePath) {
        String accessKey = this.config.getOrDefault("access_key", "");
        String secretKey = this.config.getOrDefault("secret_key", "");
        String bucket = this.config.getOrDefault("bucket", "");
        Auth auth = Auth.create((String)accessKey, (String)secretKey);
        Configuration cfg = new Configuration(Region.region2());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response response = bucketManager.delete(bucket, filePath);
            System.out.println(response);
        }
        catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }
}

