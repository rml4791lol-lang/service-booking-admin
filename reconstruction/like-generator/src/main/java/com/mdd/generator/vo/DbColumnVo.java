/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package com.mdd.generator.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class DbColumnVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty(value="columnName")
    private String columnName;
    @JsonProperty(value="columnComment")
    private String columnComment;
    @JsonProperty(value="columnType")
    private String columnType;

    public String getColumnName() {
        return this.columnName;
    }

    public String getColumnComment() {
        return this.columnComment;
    }

    public String getColumnType() {
        return this.columnType;
    }

    @JsonProperty(value="columnName")
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @JsonProperty(value="columnComment")
    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    @JsonProperty(value="columnType")
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DbColumnVo)) {
            return false;
        }
        DbColumnVo other = (DbColumnVo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$columnName = this.getColumnName();
        String other$columnName = other.getColumnName();
        if (this$columnName == null ? other$columnName != null : !this$columnName.equals(other$columnName)) {
            return false;
        }
        String this$columnComment = this.getColumnComment();
        String other$columnComment = other.getColumnComment();
        if (this$columnComment == null ? other$columnComment != null : !this$columnComment.equals(other$columnComment)) {
            return false;
        }
        String this$columnType = this.getColumnType();
        String other$columnType = other.getColumnType();
        return !(this$columnType == null ? other$columnType != null : !this$columnType.equals(other$columnType));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DbColumnVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $columnName = this.getColumnName();
        result = result * 59 + ($columnName == null ? 43 : $columnName.hashCode());
        String $columnComment = this.getColumnComment();
        result = result * 59 + ($columnComment == null ? 43 : $columnComment.hashCode());
        String $columnType = this.getColumnType();
        result = result * 59 + ($columnType == null ? 43 : $columnType.hashCode());
        return result;
    }

    public String toString() {
        return "DbColumnVo(columnName=" + this.getColumnName() + ", columnComment=" + this.getColumnComment() + ", columnType=" + this.getColumnType() + ")";
    }
}

