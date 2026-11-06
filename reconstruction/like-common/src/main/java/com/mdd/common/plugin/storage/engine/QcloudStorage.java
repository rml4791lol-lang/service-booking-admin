/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.qcloud.cos.COSClient
 *  com.qcloud.cos.ClientConfig
 *  com.qcloud.cos.auth.BasicCOSCredentials
 *  com.qcloud.cos.auth.COSCredentials
 *  com.qcloud.cos.exception.CosClientException
 *  com.qcloud.cos.http.HttpProtocol
 *  com.qcloud.cos.model.ObjectMetadata
 *  com.qcloud.cos.model.PutObjectRequest
 *  com.qcloud.cos.region.Region
 *  org.springframework.web.multipart.MultipartFile
 */
package com.mdd.common.plugin.storage.engine;

import com.mdd.common.exception.OperateException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public class QcloudStorage {
    private final Map<String, String> config;

    public QcloudStorage(Map<String, String> config) {
        this.config = config;
    }

    public COSClient cosClient() {
        String secretId = this.config.get("access_key");
        String secretKey = this.config.get("secret_key");
        BasicCOSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(this.config.get("region"));
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient((COSCredentials)cred, clientConfig);
    }

    public void upload(MultipartFile multipartFile, String key) {
        try {
            byte[] data = multipartFile.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            PutObjectRequest putObjectRequest = new PutObjectRequest(this.config.get("bucket"), key, (InputStream)inputStream, objectMetadata);
            this.cosClient().putObject(putObjectRequest);
        }
        catch (CosClientException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            throw new OperateException(e.getMessage());
        }
    }

    public void delete(String filePath) {
        this.cosClient().deleteObject(this.config.get("bucket"), filePath);
    }
}

