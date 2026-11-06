/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package com.mdd.generator.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class GenColumnVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    @JsonProperty(value="columnName")
    private String columnName;
    @JsonProperty(value="columnComment")
    private String columnComment;
    @JsonProperty(value="columnLength")
    private Integer columnLength;
    @JsonProperty(value="columnType")
    private String columnType;
    @JsonProperty(value="javaType")
    private String javaType;
    @JsonProperty(value="javaField")
    private String javaField;
    @JsonProperty(value="isRequired")
    private Integer isRequired;
    @JsonProperty(value="isInsert")
    private Integer isInsert;
    @JsonProperty(value="isEdit")
    private Integer isEdit;
    @JsonProperty(value="isList")
    private Integer isList;
    @JsonProperty(value="isQuery")
    private Integer isQuery;
    @JsonProperty(value="queryType")
    private String queryType;
    @JsonProperty(value="htmlType")
    private String htmlType;
    @JsonProperty(value="dictType")
    private String dictType;
    @JsonProperty(value="createTime")
    private String createTime;
    @JsonProperty(value="updateTime")
    private String updateTime;

    public Integer getId() {
        return this.id;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getColumnComment() {
        return this.columnComment;
    }

    public Integer getColumnLength() {
        return this.columnLength;
    }

    public String getColumnType() {
        return this.columnType;
    }

    public String getJavaType() {
        return this.javaType;
    }

    public String getJavaField() {
        return this.javaField;
    }

    public Integer getIsRequired() {
        return this.isRequired;
    }

    public Integer getIsInsert() {
        return this.isInsert;
    }

    public Integer getIsEdit() {
        return this.isEdit;
    }

    public Integer getIsList() {
        return this.isList;
    }

    public Integer getIsQuery() {
        return this.isQuery;
    }

    public String getQueryType() {
        return this.queryType;
    }

    public String getHtmlType() {
        return this.htmlType;
    }

    public String getDictType() {
        return this.dictType;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty(value="columnName")
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @JsonProperty(value="columnComment")
    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    @JsonProperty(value="columnLength")
    public void setColumnLength(Integer columnLength) {
        this.columnLength = columnLength;
    }

    @JsonProperty(value="columnType")
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    @JsonProperty(value="javaType")
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    @JsonProperty(value="javaField")
    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    @JsonProperty(value="isRequired")
    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    @JsonProperty(value="isInsert")
    public void setIsInsert(Integer isInsert) {
        this.isInsert = isInsert;
    }

    @JsonProperty(value="isEdit")
    public void setIsEdit(Integer isEdit) {
        this.isEdit = isEdit;
    }

    @JsonProperty(value="isList")
    public void setIsList(Integer isList) {
        this.isList = isList;
    }

    @JsonProperty(value="isQuery")
    public void setIsQuery(Integer isQuery) {
        this.isQuery = isQuery;
    }

    @JsonProperty(value="queryType")
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    @JsonProperty(value="htmlType")
    public void setHtmlType(String htmlType) {
        this.htmlType = htmlType;
    }

    @JsonProperty(value="dictType")
    public void setDictType(String dictType) {
        this.dictType = dictType;
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
        if (!(o instanceof GenColumnVo)) {
            return false;
        }
        GenColumnVo other = (GenColumnVo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$columnLength = this.getColumnLength();
        Integer other$columnLength = other.getColumnLength();
        if (this$columnLength == null ? other$columnLength != null : !((Object)this$columnLength).equals(other$columnLength)) {
            return false;
        }
        Integer this$isRequired = this.getIsRequired();
        Integer other$isRequired = other.getIsRequired();
        if (this$isRequired == null ? other$isRequired != null : !((Object)this$isRequired).equals(other$isRequired)) {
            return false;
        }
        Integer this$isInsert = this.getIsInsert();
        Integer other$isInsert = other.getIsInsert();
        if (this$isInsert == null ? other$isInsert != null : !((Object)this$isInsert).equals(other$isInsert)) {
            return false;
        }
        Integer this$isEdit = this.getIsEdit();
        Integer other$isEdit = other.getIsEdit();
        if (this$isEdit == null ? other$isEdit != null : !((Object)this$isEdit).equals(other$isEdit)) {
            return false;
        }
        Integer this$isList = this.getIsList();
        Integer other$isList = other.getIsList();
        if (this$isList == null ? other$isList != null : !((Object)this$isList).equals(other$isList)) {
            return false;
        }
        Integer this$isQuery = this.getIsQuery();
        Integer other$isQuery = other.getIsQuery();
        if (this$isQuery == null ? other$isQuery != null : !((Object)this$isQuery).equals(other$isQuery)) {
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
        if (this$columnType == null ? other$columnType != null : !this$columnType.equals(other$columnType)) {
            return false;
        }
        String this$javaType = this.getJavaType();
        String other$javaType = other.getJavaType();
        if (this$javaType == null ? other$javaType != null : !this$javaType.equals(other$javaType)) {
            return false;
        }
        String this$javaField = this.getJavaField();
        String other$javaField = other.getJavaField();
        if (this$javaField == null ? other$javaField != null : !this$javaField.equals(other$javaField)) {
            return false;
        }
        String this$queryType = this.getQueryType();
        String other$queryType = other.getQueryType();
        if (this$queryType == null ? other$queryType != null : !this$queryType.equals(other$queryType)) {
            return false;
        }
        String this$htmlType = this.getHtmlType();
        String other$htmlType = other.getHtmlType();
        if (this$htmlType == null ? other$htmlType != null : !this$htmlType.equals(other$htmlType)) {
            return false;
        }
        String this$dictType = this.getDictType();
        String other$dictType = other.getDictType();
        if (this$dictType == null ? other$dictType != null : !this$dictType.equals(other$dictType)) {
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
        return other instanceof GenColumnVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $columnLength = this.getColumnLength();
        result = result * 59 + ($columnLength == null ? 43 : ((Object)$columnLength).hashCode());
        Integer $isRequired = this.getIsRequired();
        result = result * 59 + ($isRequired == null ? 43 : ((Object)$isRequired).hashCode());
        Integer $isInsert = this.getIsInsert();
        result = result * 59 + ($isInsert == null ? 43 : ((Object)$isInsert).hashCode());
        Integer $isEdit = this.getIsEdit();
        result = result * 59 + ($isEdit == null ? 43 : ((Object)$isEdit).hashCode());
        Integer $isList = this.getIsList();
        result = result * 59 + ($isList == null ? 43 : ((Object)$isList).hashCode());
        Integer $isQuery = this.getIsQuery();
        result = result * 59 + ($isQuery == null ? 43 : ((Object)$isQuery).hashCode());
        String $columnName = this.getColumnName();
        result = result * 59 + ($columnName == null ? 43 : $columnName.hashCode());
        String $columnComment = this.getColumnComment();
        result = result * 59 + ($columnComment == null ? 43 : $columnComment.hashCode());
        String $columnType = this.getColumnType();
        result = result * 59 + ($columnType == null ? 43 : $columnType.hashCode());
        String $javaType = this.getJavaType();
        result = result * 59 + ($javaType == null ? 43 : $javaType.hashCode());
        String $javaField = this.getJavaField();
        result = result * 59 + ($javaField == null ? 43 : $javaField.hashCode());
        String $queryType = this.getQueryType();
        result = result * 59 + ($queryType == null ? 43 : $queryType.hashCode());
        String $htmlType = this.getHtmlType();
        result = result * 59 + ($htmlType == null ? 43 : $htmlType.hashCode());
        String $dictType = this.getDictType();
        result = result * 59 + ($dictType == null ? 43 : $dictType.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "GenColumnVo(id=" + this.getId() + ", columnName=" + this.getColumnName() + ", columnComment=" + this.getColumnComment() + ", columnLength=" + this.getColumnLength() + ", columnType=" + this.getColumnType() + ", javaType=" + this.getJavaType() + ", javaField=" + this.getJavaField() + ", isRequired=" + this.getIsRequired() + ", isInsert=" + this.getIsInsert() + ", isEdit=" + this.getIsEdit() + ", isList=" + this.getIsList() + ", isQuery=" + this.getIsQuery() + ", queryType=" + this.getQueryType() + ", htmlType=" + this.getHtmlType() + ", dictType=" + this.getDictType() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

