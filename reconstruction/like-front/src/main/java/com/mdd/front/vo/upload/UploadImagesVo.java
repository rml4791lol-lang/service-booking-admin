/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.upload.UploadImagesVo
 */
package com.mdd.front.vo.upload;

import java.io.Serializable;

public class UploadImagesVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer cid;
    private Integer type;
    private String name;
    private String url;
    private String uri;

    public Integer getId() {
        return this.id;
    }

    public Integer getCid() {
        return this.cid;
    }

    public Integer getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUri() {
        return this.uri;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UploadImagesVo)) {
            return false;
        }
        UploadImagesVo other = (UploadImagesVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$cid = this.getCid();
        Integer other$cid = other.getCid();
        if (this$cid == null ? other$cid != null : !((Object)this$cid).equals(other$cid)) {
            return false;
        }
        Integer this$type = this.getType();
        Integer other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)this$type).equals(other$type)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$url = this.getUrl();
        String other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) {
            return false;
        }
        String this$uri = this.getUri();
        String other$uri = other.getUri();
        return !(this$uri == null ? other$uri != null : !this$uri.equals(other$uri));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UploadImagesVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $cid = this.getCid();
        result = result * 59 + ($cid == null ? 43 : ((Object)$cid).hashCode());
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        String $uri = this.getUri();
        result = result * 59 + ($uri == null ? 43 : $uri.hashCode());
        return result;
    }

    public String toString() {
        return "UploadImagesVo(id=" + this.getId() + ", cid=" + this.getCid() + ", type=" + this.getType() + ", name=" + this.getName() + ", url=" + this.getUrl() + ", uri=" + this.getUri() + ")";
    }
}

