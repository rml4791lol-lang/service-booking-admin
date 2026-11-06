/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.plugin.storage;

import java.io.Serializable;

public class UploadFilesVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Long size;
    private String ext;
    private String url;
    private String path;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Long getSize() {
        return this.size;
    }

    public String getExt() {
        return this.ext;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPath() {
        return this.path;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UploadFilesVo)) {
            return false;
        }
        UploadFilesVo other = (UploadFilesVo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$size = this.getSize();
        Long other$size = other.getSize();
        if (this$size == null ? other$size != null : !((Object)this$size).equals(other$size)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$ext = this.getExt();
        String other$ext = other.getExt();
        if (this$ext == null ? other$ext != null : !this$ext.equals(other$ext)) {
            return false;
        }
        String this$url = this.getUrl();
        String other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) {
            return false;
        }
        String this$path = this.getPath();
        String other$path = other.getPath();
        return !(this$path == null ? other$path != null : !this$path.equals(other$path));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UploadFilesVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $size = this.getSize();
        result = result * 59 + ($size == null ? 43 : ((Object)$size).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $ext = this.getExt();
        result = result * 59 + ($ext == null ? 43 : $ext.hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        String $path = this.getPath();
        result = result * 59 + ($path == null ? 43 : $path.hashCode());
        return result;
    }

    public String toString() {
        return "UploadFilesVo(id=" + this.getId() + ", name=" + this.getName() + ", size=" + this.getSize() + ", ext=" + this.getExt() + ", url=" + this.getUrl() + ", path=" + this.getPath() + ")";
    }
}

