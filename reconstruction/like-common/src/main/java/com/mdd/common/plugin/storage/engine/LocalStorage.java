/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.web.multipart.MultipartFile
 */
package com.mdd.common.plugin.storage.engine;

import com.mdd.common.exception.OperateException;
import com.mdd.common.util.YmlUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public class LocalStorage {
    public void upload(MultipartFile multipartFile, String key, String folder) {
        String directory = YmlUtils.get("like.upload-directory");
        if (directory == null || directory.equals("")) {
            throw new OperateException("\u8bf7\u914d\u7f6e\u4e0a\u4f20\u76ee\u5f55like.upload-directory");
        }
        try {
            File root = new File(directory).getCanonicalFile();
            String[] keyParts = key.split("/", 2);
            if (keyParts.length != 2) {
                throw new OperateException("上传文件路径不合法");
            }
            File dest = new File(root, folder + "/" + key).getCanonicalFile();
            Path rootPath = root.toPath();
            if (!dest.toPath().startsWith(rootPath) || dest.equals(root)) {
                throw new OperateException("上传文件路径越界");
            }
            File parent = dest.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new OperateException("\u521b\u5efa\u4e0a\u4f20\u76ee\u5f55\u5931\u8d25");
            }
            multipartFile.transferTo(dest);
        }
        catch (OperateException e) {
            throw e;
        }
        catch (Exception e) {
            throw new OperateException("\u4e0a\u4f20\u6587\u4ef6\u5931\u8d25:" + e.getMessage());
        }
    }

    public void delete(String filePath) {
        String directory = YmlUtils.get("like.upload-directory");
        try {
            File root = new File(directory).getCanonicalFile();
            File file = new File(root, filePath).getCanonicalFile();
            if (!file.toPath().startsWith(root.toPath()) || file.equals(root)) {
                throw new OperateException("文件路径不合法");
            }
            file.delete();
        }
        catch (IOException e) {
            throw new OperateException("删除文件失败");
        }
    }
}
