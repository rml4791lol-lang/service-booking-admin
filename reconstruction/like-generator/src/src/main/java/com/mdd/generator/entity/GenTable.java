/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 */
package com.mdd.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

public class GenTable
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    private String tableName;
    private String tableComment;
    private String subTableName;
    private String subTableFk;
    private String subTableFr;
    private String authorName;
    private String entityName;
    private String moduleName;
    private String functionName;
    private String treePrimary;
    private String treeParent;
    private String treeName;
    private String genTpl;
    private Integer genType;
    private String genPath;
    private Integer menuStatus;
    private Integer menuPid;
    private String menuName;
    private String remarks;
    private Long createTime;
    private Long updateTime;

    public Integer getId() {
        return this.id;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getTableComment() {
        return this.tableComment;
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

    public String getAuthorName() {
        return this.authorName;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public String getFunctionName() {
        return this.functionName;
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

    public String getGenTpl() {
        return this.genTpl;
    }

    public Integer getGenType() {
        return this.genType;
    }

    public String getGenPath() {
        return this.genPath;
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

    public String getRemarks() {
        return this.remarks;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public void setSubTableName(String subTableName) {
        this.subTableName = subTableName;
    }

    public void setSubTableFk(String subTableFk) {
        this.subTableFk = subTableFk;
    }

    public void setSubTableFr(String subTableFr) {
        this.subTableFr = subTableFr;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public void setTreePrimary(String treePrimary) {
        this.treePrimary = treePrimary;
    }

    public void setTreeParent(String treeParent) {
        this.treeParent = treeParent;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public void setGenTpl(String genTpl) {
        this.genTpl = genTpl;
    }

    public void setGenType(Integer genType) {
        this.genType = genType;
    }

    public void setGenPath(String genPath) {
        this.genPath = genPath;
    }

    public void setMenuStatus(Integer menuStatus) {
        this.menuStatus = menuStatus;
    }

    public void setMenuPid(Integer menuPid) {
        this.menuPid = menuPid;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GenTable)) {
            return false;
        }
        GenTable other = (GenTable)o;
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
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
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
        String this$authorName = this.getAuthorName();
        String other$authorName = other.getAuthorName();
        if (this$authorName == null ? other$authorName != null : !this$authorName.equals(other$authorName)) {
            return false;
        }
        String this$entityName = this.getEntityName();
        String other$entityName = other.getEntityName();
        if (this$entityName == null ? other$entityName != null : !this$entityName.equals(other$entityName)) {
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
        String this$genTpl = this.getGenTpl();
        String other$genTpl = other.getGenTpl();
        if (this$genTpl == null ? other$genTpl != null : !this$genTpl.equals(other$genTpl)) {
            return false;
        }
        String this$genPath = this.getGenPath();
        String other$genPath = other.getGenPath();
        if (this$genPath == null ? other$genPath != null : !this$genPath.equals(other$genPath)) {
            return false;
        }
        String this$menuName = this.getMenuName();
        String other$menuName = other.getMenuName();
        if (this$menuName == null ? other$menuName != null : !this$menuName.equals(other$menuName)) {
            return false;
        }
        String this$remarks = this.getRemarks();
        String other$remarks = other.getRemarks();
        return !(this$remarks == null ? other$remarks != null : !this$remarks.equals(other$remarks));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GenTable;
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
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $tableName = this.getTableName();
        result = result * 59 + ($tableName == null ? 43 : $tableName.hashCode());
        String $tableComment = this.getTableComment();
        result = result * 59 + ($tableComment == null ? 43 : $tableComment.hashCode());
        String $subTableName = this.getSubTableName();
        result = result * 59 + ($subTableName == null ? 43 : $subTableName.hashCode());
        String $subTableFk = this.getSubTableFk();
        result = result * 59 + ($subTableFk == null ? 43 : $subTableFk.hashCode());
        String $subTableFr = this.getSubTableFr();
        result = result * 59 + ($subTableFr == null ? 43 : $subTableFr.hashCode());
        String $authorName = this.getAuthorName();
        result = result * 59 + ($authorName == null ? 43 : $authorName.hashCode());
        String $entityName = this.getEntityName();
        result = result * 59 + ($entityName == null ? 43 : $entityName.hashCode());
        String $moduleName = this.getModuleName();
        result = result * 59 + ($moduleName == null ? 43 : $moduleName.hashCode());
        String $functionName = this.getFunctionName();
        result = result * 59 + ($functionName == null ? 43 : $functionName.hashCode());
        String $treePrimary = this.getTreePrimary();
        result = result * 59 + ($treePrimary == null ? 43 : $treePrimary.hashCode());
        String $treeParent = this.getTreeParent();
        result = result * 59 + ($treeParent == null ? 43 : $treeParent.hashCode());
        String $treeName = this.getTreeName();
        result = result * 59 + ($treeName == null ? 43 : $treeName.hashCode());
        String $genTpl = this.getGenTpl();
        result = result * 59 + ($genTpl == null ? 43 : $genTpl.hashCode());
        String $genPath = this.getGenPath();
        result = result * 59 + ($genPath == null ? 43 : $genPath.hashCode());
        String $menuName = this.getMenuName();
        result = result * 59 + ($menuName == null ? 43 : $menuName.hashCode());
        String $remarks = this.getRemarks();
        result = result * 59 + ($remarks == null ? 43 : $remarks.hashCode());
        return result;
    }

    public String toString() {
        return "GenTable(id=" + this.getId() + ", tableName=" + this.getTableName() + ", tableComment=" + this.getTableComment() + ", subTableName=" + this.getSubTableName() + ", subTableFk=" + this.getSubTableFk() + ", subTableFr=" + this.getSubTableFr() + ", authorName=" + this.getAuthorName() + ", entityName=" + this.getEntityName() + ", moduleName=" + this.getModuleName() + ", functionName=" + this.getFunctionName() + ", treePrimary=" + this.getTreePrimary() + ", treeParent=" + this.getTreeParent() + ", treeName=" + this.getTreeName() + ", genTpl=" + this.getGenTpl() + ", genType=" + this.getGenType() + ", genPath=" + this.getGenPath() + ", menuStatus=" + this.getMenuStatus() + ", menuPid=" + this.getMenuPid() + ", menuName=" + this.getMenuName() + ", remarks=" + this.getRemarks() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

