/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.aliyun.oss.OSS
 *  com.aliyun.oss.OSSClientBuilder
 *  com.aliyun.oss.OSSException
 *  com.aliyun.oss.model.PutObjectRequest
 *  org.springframework.web.multipart.MultipartFile
 */
package com.mdd.common.plugin.storage.engine;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.mdd.common.exception.OperateException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public class AliyunStorage {
    private final Map<String, String> config;

    public AliyunStorage(Map<String, String> config) {
        this.config = config;
    }

    public OSS ossClient() {
        String endpoint = this.config.get("endpoint");
        String accessKeyId = this.config.get("access_key");
        String accessKeySecret = this.config.get("secret_key");
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    public void upload(MultipartFile multipartFile, String key) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(this.config.get("bucket"), key, (InputStream)new ByteArrayInputStream(multipartFile.getBytes()));
            this.ossClient().putObject(putObjectRequest);
        }
        catch (OSSException oe) {
            throw new OperateException(oe.getMessage());
        }
        catch (Exception ce) {
            throw new OperateException(ce.getMessage());
        }
        finally {
            if (this.ossClient() != null) {
                this.ossClient().shutdown();
            }
        }
    }

    public void delete(String key) {
        this.ossClient().deleteObject(this.config.get("bucket"), key);
    }
}

