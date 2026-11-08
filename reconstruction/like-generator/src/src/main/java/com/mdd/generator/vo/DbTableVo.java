/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package com.mdd.generator.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class DbTableVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty(value="tableName")
    private String tableName;
    @JsonProperty(value="tableComment")
    private String tableComment;
    @JsonProperty(value="authorName")
    private String authorName;
    @JsonProperty(value="createTime")
    private String createTime;
    @JsonProperty(value="updateTime")
    private String updateTime;

    public String getTableName() {
        return this.tableName;
    }

    public String getTableComment() {
        return this.tableComment;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    @JsonProperty(value="tableName")
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @JsonProperty(value="tableComment")
    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    @JsonProperty(value="authorName")
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @JsonProperty(value="createTime")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @JsonProperty(value="updateTime")
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DbTableVo)) {
            return false;
        }
        DbTableVo other = (DbTableVo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$tableName = this.getTableName();
        String other$tableName = other.getTableName();
        if (this$tableName == null ? other$tableName != null : !this$tableName.equals(other$tableName)) {
            return false;
        }
        String this$tableComment = this.getTableComment();
        String other$tableComment = other.getTableComment();
        if (this$tableComment == null ? other$tableComment != null : !this$tableComment.equals(other$tableComment)) {
            return false;
        }
        String this$authorName = this.getAuthorName();
        String other$authorName = other.getAuthorName();
        if (this$authorName == null ? other$authorName != null : !this$authorName.equals(other$authorName)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        String this$updateTime = this.getUpdateTime();
        String other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !this$updateTime.equals(other$updateTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DbTableVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $tableName = this.getTableName();
        result = result * 59 + ($tableName == null ? 43 : $tableName.hashCode());
        String $tableComment = this.getTableComment();
        result = result * 59 + ($tableComment == null ? 43 : $tableComment.hashCode());
        String $authorName = this.getAuthorName();
        result = result * 59 + ($authorName == null ? 43 : $authorName.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "DbTableVo(tableName=" + this.getTableName() + ", tableComment=" + this.getTableComment() + ", authorName=" + this.getAuthorName() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

