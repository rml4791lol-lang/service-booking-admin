/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.common.validator.annotation.IntegerContains
 *  com.mdd.common.validator.annotation.StringContains
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.generator.validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdd.common.validator.annotation.IDMust;
import com.mdd.common.validator.annotation.IntegerContains;
import com.mdd.common.validator.annotation.StringContains;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class GenParam
implements Serializable {
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    private Integer id;
    @NotNull(message="ids\u53c2\u6570\u7f3a\u5931", groups={delete.class})
    private @NotNull(message="ids\u53c2\u6570\u7f3a\u5931", groups={delete.class}) List<Integer> ids;
    @NotNull(message="tableName\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u8868\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=200, message="\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26")
    @JsonProperty(value="tableName")
    private @NotNull(message="tableName\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u8868\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=200, message="\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26") String tableName;
    @NotNull(message="entityName\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u5b9e\u4f53\u7c7b\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=200, message="\u5b9e\u4f53\u7c7b\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26")
    @JsonProperty(value="entityName")
    private @NotNull(message="entityName\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u5b9e\u4f53\u7c7b\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=200, message="\u5b9e\u4f53\u7c7b\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26") String entityName;
    @NotNull(message="tableComment\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u8868\u63cf\u8ff0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=200, message="\u8868\u63cf\u8ff0\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26")
    @JsonProperty(value="tableComment")
    private @NotNull(message="tableComment\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u8868\u63cf\u8ff0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=200, message="\u8868\u63cf\u8ff0\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26") String tableComment;
    @NotNull(message="authorName\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u4f5c\u8005\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=100, message="\u4f5c\u8005\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e60\u4e2a\u5b57\u7b26")
    @JsonProperty(value="authorName")
    private @NotNull(message="authorName\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u4f5c\u8005\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=100, message="\u4f5c\u8005\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e60\u4e2a\u5b57\u7b26") String authorName;
    @Length(max=60, message="\u5907\u6ce8\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26")
    private @Length(max=60, message="\u5907\u6ce8\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26") String remarks;
    @NotNull(message="genTpl\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u8bf7\u9009\u62e9\u751f\u6210\u6a21\u677f")
    @StringContains(values={"crud", "tree"}, message="\u9009\u62e9\u7684\u751f\u6210\u6a21\u677f\u4e0d\u7b26\u5408")
    @JsonProperty(value="genTpl")
    private @NotNull(message="genTpl\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u8bf7\u9009\u62e9\u751f\u6210\u6a21\u677f") String genTpl;
    @NotNull(message="moduleName\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u751f\u6210\u6a21\u5757\u540d\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=60, message="\u751f\u6210\u6a21\u5757\u540d\u4e0d\u80fd\u5927\u4e8e60\u4e2a\u5b57\u7b26")
    @JsonProperty(value="moduleName")
    private @NotNull(message="moduleName\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u751f\u6210\u6a21\u5757\u540d\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=60, message="\u751f\u6210\u6a21\u5757\u540d\u4e0d\u80fd\u5927\u4e8e60\u4e2a\u5b57\u7b26") String moduleName;
    @NotNull(message="functionName\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u751f\u6210\u529f\u80fd\u540d\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=60, message="\u751f\u6210\u529f\u80fd\u540d\u4e0d\u80fd\u5927\u4e8e60\u4e2a\u5b57\u7b26")
    @JsonProperty(value="functionName")
    private @NotNull(message="functionName\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u751f\u6210\u529f\u80fd\u540d\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=60, message="\u751f\u6210\u529f\u80fd\u540d\u4e0d\u80fd\u5927\u4e8e60\u4e2a\u5b57\u7b26") String functionName;
    @NotNull(message="genType\u53c2\u6570\u7f3a\u5931")
    @IntegerContains(values={0, 1}, message="\u9009\u62e9\u7684\u751f\u6210\u4ee3\u7801\u65b9\u5f0f\u4e0d\u7b26\u5408")
    @JsonProperty(value="genType")
    private @NotNull(message="genType\u53c2\u6570\u7f3a\u5931") Integer genType;
    @Length(max=200, message="\u751f\u6210\u4ee3\u7801\u8def\u5f84\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26")
    @JsonProperty(value="genPath")
    private @Length(max=200, message="\u751f\u6210\u4ee3\u7801\u8def\u5f84\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26") String genPath = "/";
    private List<Map<String, String>> column = new ArrayList<Map<String, String>>();
    @JsonProperty(value="treePrimary")
    private String treePrimary = "";
    @JsonProperty(value="treeParent")
    private String treeParent = "";
    @JsonProperty(value="treeName")
    private String treeName = "";
    @JsonProperty(value="subTableName")
    private String subTableName = "";
    @JsonProperty(value="subTableFk")
    private String subTableFk = "";
    @JsonProperty(value="subTableFr")
    private String subTableFr = "";
    @JsonProperty(value="menuStatus")
    private Integer menuStatus = 2;
    @JsonProperty(value="menuPid")
    private Integer menuPid = 0;
    @JsonProperty(value="menuName")
    private String menuName = "";

    public Integer getId() {
        return this.id;
    }

    @NotNull(message="ids\u53c2\u6570\u7f3a\u5931", groups={delete.class})
    public @NotNull(message="ids\u53c2\u6570\u7f3a\u5931", groups={delete.class}) List<Integer> getIds() {
        return this.ids;
    }

    @NotNull(message="tableName\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="tableName\u53c2\u6570\u7f3a\u5931") String getTableName() {
        return this.tableName;
    }

    @NotNull(message="entityName\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="entityName\u53c2\u6570\u7f3a\u5931") String getEntityName() {
        return this.entityName;
    }

    @NotNull(message="tableComment\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="tableComment\u53c2\u6570\u7f3a\u5931") String getTableComment() {
        return this.tableComment;
    }

    @NotNull(message="authorName\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="authorName\u53c2\u6570\u7f3a\u5931") String getAuthorName() {
        return this.authorName;
    }

    public String getRemarks() {
        return this.remarks;
    }

    @NotNull(message="genTpl\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="genTpl\u53c2\u6570\u7f3a\u5931") String getGenTpl() {
        return this.genTpl;
    }

    @NotNull(message="moduleName\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="moduleName\u53c2\u6570\u7f3a\u5931") String getModuleName() {
        return this.moduleName;
    }

    @NotNull(message="functionName\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="functionName\u53c2\u6570\u7f3a\u5931") String getFunctionName() {
        return this.functionName;
    }

    @NotNull(message="genType\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="genType\u53c2\u6570\u7f3a\u5931") Integer getGenType() {
        return this.genType;
    }

    public String getGenPath() {
        return this.genPath;
    }

    public List<Map<String, String>> getColumn() {
        return this.column;
    }

    public String getTreePrimary() {
        return this.treePrimary;
    }

    public String getTreeParent() {
        return this.treeParent;
    }

    public String getTreeName() {
        return this.treeName;
    }

    public String getSubTableName() {
        return this.subTableName;
    }

    public String getSubTableFk() {
        return this.subTableFk;
    }

    public String getSubTableFr() {
        return this.subTableFr;
    }

    public Integer getMenuStatus() {
        return this.menuStatus;
    }

    public Integer getMenuPid() {
        return this.menuPid;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public GenParam setId(Integer id) {
        this.id = id;
        return this;
    }

    public GenParam setIds(@NotNull(message="ids\u53c2\u6570\u7f3a\u5931", groups={delete.class}) @NotNull(message="ids\u53c2\u6570\u7f3a\u5931", groups={delete.class}) List<Integer> ids) {
        this.ids = ids;
        return this;
    }

    @JsonProperty(value="tableName")
    public GenParam setTableName(@NotNull(message="tableName\u53c2\u6570\u7f3a\u5931") @NotNull(message="tableName\u53c2\u6570\u7f3a\u5931") String tableName) {
        this.tableName = tableName;
        return this;
    }

    @JsonProperty(value="entityName")
    public GenParam setEntityName(@NotNull(message="entityName\u53c2\u6570\u7f3a\u5931") @NotNull(message="entityName\u53c2\u6570\u7f3a\u5931") String entityName) {
        this.entityName = entityName;
        return this;
    }

    @JsonProperty(value="tableComment")
    public GenParam setTableComment(@NotNull(message="tableComment\u53c2\u6570\u7f3a\u5931") @NotNull(message="tableComment\u53c2\u6570\u7f3a\u5931") String tableComment) {
        this.tableComment = tableComment;
        return this;
    }

    @JsonProperty(value="authorName")
    public GenParam setAuthorName(@NotNull(message="authorName\u53c2\u6570\u7f3a\u5931") @NotNull(message="authorName\u53c2\u6570\u7f3a\u5931") String authorName) {
        this.authorName = authorName;
        return this;
    }

    public GenParam setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    @JsonProperty(value="genTpl")
    public GenParam setGenTpl(@NotNull(message="genTpl\u53c2\u6570\u7f3a\u5931") @NotNull(message="genTpl\u53c2\u6570\u7f3a\u5931") String genTpl) {
        this.genTpl = genTpl;
        return this;
    }

    @JsonProperty(value="moduleName")
    public GenParam setModuleName(@NotNull(message="moduleName\u53c2\u6570\u7f3a\u5931") @NotNull(message="moduleName\u53c2\u6570\u7f3a\u5931") String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    @JsonProperty(value="functionName")
    public GenParam setFunctionName(@NotNull(message="functionName\u53c2\u6570\u7f3a\u5931") @NotNull(message="functionName\u53c2\u6570\u7f3a\u5931") String functionName) {
        this.functionName = functionName;
        return this;
    }

    @JsonProperty(value="genType")
    public GenParam setGenType(@NotNull(message="genType\u53c2\u6570\u7f3a\u5931") @NotNull(message="genType\u53c2\u6570\u7f3a\u5931") Integer genType) {
        this.genType = genType;
        return this;
    }

    @JsonProperty(value="genPath")
    public GenParam setGenPath(String genPath) {
        this.genPath = genPath;
        return this;
    }

    public GenParam setColumn(List<Map<String, String>> column) {
        this.column = column;
        return this;
    }

    @JsonProperty(value="treePrimary")
    public GenParam setTreePrimary(String treePrimary) {
        this.treePrimary = treePrimary;
        return this;
    }

    @JsonProperty(value="treeParent")
    public GenParam setTreeParent(String treeParent) {
        this.treeParent = treeParent;
        return this;
    }

    @JsonProperty(value="treeName")
    public GenParam setTreeName(String treeName) {
        this.treeName = treeName;
        return this;
    }

    @JsonProperty(value="subTableName")
    public GenParam setSubTableName(String subTableName) {
        this.subTableName = subTableName;
        return this;
    }

    @JsonProperty(value="subTableFk")
    public GenParam setSubTableFk(String subTableFk) {
        this.subTableFk = subTableFk;
        return this;
    }

    @JsonProperty(value="subTableFr")
    public GenParam setSubTableFr(String subTableFr) {
        this.subTableFr = subTableFr;
        return this;
    }

    @JsonProperty(value="menuStatus")
    public GenParam setMenuStatus(Integer menuStatus) {
        this.menuStatus = menuStatus;
        return this;
    }

    @JsonProperty(value="menuPid")
    public GenParam setMenuPid(Integer menuPid) {
        this.menuPid = menuPid;
        return this;
    }

    @JsonProperty(value="menuName")
    public GenParam setMenuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public String toString() {
        return "GenParam(id=" + this.getId() + ", ids=" + this.getIds() + ", tableName=" + this.getTableName() + ", entityName=" + this.getEntityName() + ", tableComment=" + this.getTableComment() + ", authorName=" + this.getAuthorName() + ", remarks=" + this.getRemarks() + ", genTpl=" + this.getGenTpl() + ", moduleName=" + this.getModuleName() + ", functionName=" + this.getFunctionName() + ", genType=" + this.getGenType() + ", genPath=" + this.getGenPath() + ", column=" + this.getColumn() + ", treePrimary=" + this.getTreePrimary() + ", treeParent=" + this.getTreeParent() + ", treeName=" + this.getTreeName() + ", subTableName=" + this.getSubTableName() + ", subTableFk=" + this.getSubTableFk() + ", subTableFr=" + this.getSubTableFr() + ", menuStatus=" + this.getMenuStatus() + ", menuPid=" + this.getMenuPid() + ", menuName=" + this.getMenuName() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GenParam)) {
            return false;
        }
        GenParam other = (GenParam)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$genType = this.getGenType();
        Integer other$genType = other.getGenType();
        if (this$genType == null ? other$genType != null : !((Object)this$genType).equals(other$genType)) {
            return false;
        }
        Integer this$menuStatus = this.getMenuStatus();
        Integer other$menuStatus = other.getMenuStatus();
        if (this$menuStatus == null ? other$menuStatus != null : !((Object)this$menuStatus).equals(other$menuStatus)) {
            return false;
        }
        Integer this$menuPid = this.getMenuPid();
        Integer other$menuPid = other.getMenuPid();
        if (this$menuPid == null ? other$menuPid != null : !((Object)this$menuPid).equals(other$menuPid)) {
            return false;
        }
        List<Integer> this$ids = this.getIds();
        List<Integer> other$ids = other.getIds();
        if (this$ids == null ? other$ids != null : !((Object)this$ids).equals(other$ids)) {
            return false;
        }
        String this$tableName = this.getTableName();
        String other$tableName = other.getTableName();
        if (this$tableName == null ? other$tableName != null : !this$tableName.equals(other$tableName)) {
            return false;
        }
        String this$entityName = this.getEntityName();
        String other$entityName = other.getEntityName();
        if (this$entityName == null ? other$entityName != null : !this$entityName.equals(other$entityName)) {
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
        String this$remarks = this.getRemarks();
        String other$remarks = other.getRemarks();
        if (this$remarks == null ? other$remarks != null : !this$remarks.equals(other$remarks)) {
            return false;
        }
        String this$genTpl = this.getGenTpl();
        String other$genTpl = other.getGenTpl();
        if (this$genTpl == null ? other$genTpl != null : !this$genTpl.equals(other$genTpl)) {
            return false;
        }
        String this$moduleName = this.getModuleName();
        String other$moduleName = other.getModuleName();
        if (this$moduleName == null ? other$moduleName != null : !this$moduleName.equals(other$moduleName)) {
            return false;
        }
        String this$functionName = this.getFunctionName();
        String other$functionName = other.getFunctionName();
        if (this$functionName == null ? other$functionName != null : !this$functionName.equals(other$functionName)) {
            return false;
        }
        String this$genPath = this.getGenPath();
        String other$genPath = other.getGenPath();
        if (this$genPath == null ? other$genPath != null : !this$genPath.equals(other$genPath)) {
            return false;
        }
        List<Map<String, String>> this$column = this.getColumn();
        List<Map<String, String>> other$column = other.getColumn();
        if (this$column == null ? other$column != null : !((Object)this$column).equals(other$column)) {
            return false;
        }
        String this$treePrimary = this.getTreePrimary();
        String other$treePrimary = other.getTreePrimary();
        if (this$treePrimary == null ? other$treePrimary != null : !this$treePrimary.equals(other$treePrimary)) {
            return false;
        }
        String this$treeParent = this.getTreeParent();
        String other$treeParent = other.getTreeParent();
        if (this$treeParent == null ? other$treeParent != null : !this$treeParent.equals(other$treeParent)) {
            return false;
        }
        String this$treeName = this.getTreeName();
        String other$treeName = other.getTreeName();
        if (this$treeName == null ? other$treeName != null : !this$treeName.equals(other$treeName)) {
            return false;
        }
        String this$subTableName = this.getSubTableName();
        String other$subTableName = other.getSubTableName();
        if (this$subTableName == null ? other$subTableName != null : !this$subTableName.equals(other$subTableName)) {
            return false;
        }
        String this$subTableFk = this.getSubTableFk();
        String other$subTableFk = other.getSubTableFk();
        if (this$subTableFk == null ? other$subTableFk != null : !this$subTableFk.equals(other$subTableFk)) {
            return false;
        }
        String this$subTableFr = this.getSubTableFr();
        String other$subTableFr = other.getSubTableFr();
        if (this$subTableFr == null ? other$subTableFr != null : !this$subTableFr.equals(other$subTableFr)) {
            return false;
        }
        String this$menuName = this.getMenuName();
        String other$menuName = other.getMenuName();
        return !(this$menuName == null ? other$menuName != null : !this$menuName.equals(other$menuName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GenParam;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $genType = this.getGenType();
        result = result * 59 + ($genType == null ? 43 : ((Object)$genType).hashCode());
        Integer $menuStatus = this.getMenuStatus();
        result = result * 59 + ($menuStatus == null ? 43 : ((Object)$menuStatus).hashCode());
        Integer $menuPid = this.getMenuPid();
        result = result * 59 + ($menuPid == null ? 43 : ((Object)$menuPid).hashCode());
        List<Integer> $ids = this.getIds();
        result = result * 59 + ($ids == null ? 43 : ((Object)$ids).hashCode());
        String $tableName = this.getTableName();
        result = result * 59 + ($tableName == null ? 43 : $tableName.hashCode());
        String $entityName = this.getEntityName();
        result = result * 59 + ($entityName == null ? 43 : $entityName.hashCode());
        String $tableComment = this.getTableComment();
        result = result * 59 + ($tableComment == null ? 43 : $tableComment.hashCode());
        String $authorName = this.getAuthorName();
        result = result * 59 + ($authorName == null ? 43 : $authorName.hashCode());
        String $remarks = this.getRemarks();
        result = result * 59 + ($remarks == null ? 43 : $remarks.hashCode());
        String $genTpl = this.getGenTpl();
        result = result * 59 + ($genTpl == null ? 43 : $genTpl.hashCode());
        String $moduleName = this.getModuleName();
        result = result * 59 + ($moduleName == null ? 43 : $moduleName.hashCode());
        String $functionName = this.getFunctionName();
        result = result * 59 + ($functionName == null ? 43 : $functionName.hashCode());
        String $genPath = this.getGenPath();
        result = result * 59 + ($genPath == null ? 43 : $genPath.hashCode());
        List<Map<String, String>> $column = this.getColumn();
        result = result * 59 + ($column == null ? 43 : ((Object)$column).hashCode());
        String $treePrimary = this.getTreePrimary();
        result = result * 59 + ($treePrimary == null ? 43 : $treePrimary.hashCode());
        String $treeParent = this.getTreeParent();
        result = result * 59 + ($treeParent == null ? 43 : $treeParent.hashCode());
        String $treeName = this.getTreeName();
        result = result * 59 + ($treeName == null ? 43 : $treeName.hashCode());
        String $subTableName = this.getSubTableName();
        result = result * 59 + ($subTableName == null ? 43 : $subTableName.hashCode());
        String $subTableFk = this.getSubTableFk();
        result = result * 59 + ($subTableFk == null ? 43 : $subTableFk.hashCode());
        String $subTableFr = this.getSubTableFr();
        result = result * 59 + ($subTableFr == null ? 43 : $subTableFr.hashCode());
        String $menuName = this.getMenuName();
        result = result * 59 + ($menuName == null ? 43 : $menuName.hashCode());
        return result;
    }

    public static interface delete {
    }
}

