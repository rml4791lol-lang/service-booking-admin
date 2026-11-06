/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.entity.file.File
 *  com.mdd.common.mapper.album.FileCateMapper
 *  com.mdd.common.mapper.album.FileMapper
 *  com.mdd.front.service.IFileService
 *  com.mdd.front.service.impl.FileServiceImpl
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.mdd.common.entity.file.File;
import com.mdd.common.mapper.album.FileCateMapper;
import com.mdd.common.mapper.album.FileMapper;
import com.mdd.common.util.ParamUtils;
import com.mdd.front.service.IFileService;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl
implements IFileService {
    @Resource
    FileMapper fileMapper;
    @Resource
    FileCateMapper fileCateMapper;

    public Integer fileAdd(Map<String, String> params) {
        String name = params.get("name");
        if (name == null) {
            name = "";
        }
        if (name.length() >= 100) {
            name = name.substring(0, 99);
        }
        File album = new File();
        album.setCid(ParamUtils.intOrDefault(params.get("cid"), 0, "文件分类ID"));
        album.setSourceId(ParamUtils.intOrDefault(params.get("uid"), 0, "上传用户ID"));
        album.setType(ParamUtils.intOrDefault(params.get("type"), 0, "文件类型"));
        album.setSource(ParamUtils.intOrDefault(params.get("source"), 0, "文件来源"));
        album.setName(name);
        album.setUri(params.get("url"));
        album.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        album.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.fileMapper.insert(album);
        return album.getId();
    }
}

