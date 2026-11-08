/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.github.pagehelper.PageHelper
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.system.SystemMenu
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.system.SystemMenuMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.IOUtils
 *  org.apache.velocity.Template
 *  org.apache.velocity.VelocityContext
 *  org.apache.velocity.app.Velocity
 *  org.apache.velocity.context.Context
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.system.SystemMenu;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.system.SystemMenuMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.generator.entity.GenTable;
import com.mdd.generator.entity.GenTableColumn;
import com.mdd.generator.mapper.GenTableColumnMapper;
import com.mdd.generator.mapper.GenTableMapper;
import com.mdd.generator.service.IGenerateService;
import com.mdd.generator.util.GenUtil;
import com.mdd.generator.util.VelocityUtil;
import com.mdd.generator.validate.GenParam;
import com.mdd.generator.validate.PageParam;
import com.mdd.generator.vo.DbColumnVo;
import com.mdd.generator.vo.DbTableVo;
import com.mdd.generator.vo.GenColumnVo;
import com.mdd.generator.vo.GenTableVo;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GenerateServiceImpl
implements IGenerateService {
    private static final Logger log = LoggerFactory.getLogger(GenerateServiceImpl.class);
    @Resource
    GenTableMapper genTableMapper;
    @Resource
    GenTableColumnMapper genTableColumnMapper;
    @Resource
    SystemMenuMapper systemAuthMenuMapper;

    @Override
    public PageResult<DbTableVo> db(PageParam pageParam, Map<String, String> params) {
        Integer page = pageParam.getPage_no();
        Integer limit = pageParam.getPage_size();
        PageHelper.startPage((int)page, (int)limit);
        List<DbTableVo> tables = this.genTableMapper.selectDbTableList(params);
        for (DbTableVo vo : tables) {
            if (vo.getUpdateTime() != null) continue;
            vo.setUpdateTime("");
        }
        return PageResult.pageHelper(tables);
    }

    @Override
    public List<DbTableVo> dbAll(Map<String, String> params) {
        List<DbTableVo> tables = this.genTableMapper.selectDbTableAllList(params);
        for (DbTableVo vo : tables) {
            if (vo.getUpdateTime() != null) continue;
            vo.setUpdateTime("");
        }
        return tables;
    }

    @Override
    public List<DbColumnVo> dbColumn(String tableName) {
        List<GenTableColumn> columns = this.genTableMapper.selectDbTableColumnsByName(tableName);
        LinkedList<DbColumnVo> list = new LinkedList<DbColumnVo>();
        for (GenTableColumn col : columns) {
            DbColumnVo vo = new DbColumnVo();
            vo.setColumnName(col.getColumnName());
            vo.setColumnType(col.getColumnType());
            vo.setColumnComment(col.getColumnComment());
            list.add(vo);
        }
        return list;
    }

    @Override
    public PageResult<GenTableVo> list(PageParam pageParam, Map<String, String> params) {
        Integer page = pageParam.getPage_no();
        Integer limit = pageParam.getPage_size();
        QueryWrapper<GenTable> queryWrapper = new QueryWrapper<GenTable>();
        queryWrapper.orderByDesc("id");
        queryWrapper.select(new String[]{"id,gen_type,table_name,table_comment,create_time,update_time"});
        this.genTableMapper.setSearch(queryWrapper, params, new String[]{"like:tableName@table_name:str", "like:tableComment@table_comment:str", "datetime:startTime-endTime@create_time:str"});
        IPage<GenTable> iPage = (IPage<GenTable>) this.genTableMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        LinkedList<GenTableVo> list = new LinkedList<GenTableVo>();
        for (GenTable item : iPage.getRecords()) {
            GenTableVo vo = new GenTableVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((Long)item.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)item.getUpdateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    @Override
    public Map<String, Object> detail(Integer id) {
        LinkedHashMap<String, Object> maps = new LinkedHashMap<String, Object>();
        GenTable genTable = (GenTable)this.genTableMapper.selectById(id);
        Assert.notNull((Object)genTable, (String)"\u67e5\u8be2\u7684\u6570\u636e\u4e0d\u5b58\u5728", (Object[])new Object[0]);
        LinkedHashMap<String, Object> base = new LinkedHashMap<String, Object>();
        base.put("id", genTable.getId());
        base.put("tableName", genTable.getTableName());
        base.put("tableComment", genTable.getTableComment());
        base.put("entityName", genTable.getEntityName());
        base.put("authorName", genTable.getAuthorName());
        base.put("remarks", genTable.getRemarks());
        base.put("createTime", TimeUtils.timestampToDate((Long)genTable.getCreateTime()));
        base.put("updateTime", TimeUtils.timestampToDate((Long)genTable.getUpdateTime()));
        maps.put("base", base);
        LinkedHashMap<String, Object> gen = new LinkedHashMap<String, Object>();
        gen.put("genTpl", genTable.getGenTpl());
        gen.put("genType", genTable.getGenType());
        gen.put("genPath", genTable.getGenPath());
        gen.put("moduleName", genTable.getModuleName());
        gen.put("functionName", genTable.getFunctionName());
        gen.put("treePrimary", genTable.getTreePrimary());
        gen.put("treeParent", genTable.getTreeParent());
        gen.put("treeName", genTable.getTreeName());
        gen.put("subTableName", genTable.getSubTableName());
        gen.put("subTableFk", genTable.getSubTableFk());
        gen.put("subTableFr", genTable.getSubTableFr());
        gen.put("menuStatus", genTable.getMenuStatus());
        gen.put("menuPid", genTable.getMenuPid());
        gen.put("menuName", genTable.getMenuName());
        maps.put("gen", gen);
        QueryWrapper<GenTable> queryWrapper = new QueryWrapper<GenTable>();
        queryWrapper.eq("table_id", (Object)id);
        queryWrapper.orderByAsc("sort");
        LinkedList<GenColumnVo> columns = new LinkedList<GenColumnVo>();
        for (GenTableColumn item : (java.util.List<GenTableColumn>) this.genTableColumnMapper.selectList((Wrapper)queryWrapper)) {
            GenColumnVo vo = new GenColumnVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((Long)item.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)item.getUpdateTime()));
            columns.add(vo);
        }
        maps.put("column", columns);
        return maps;
    }

    @Override
    @Transactional
    public void importTable(String[] tableNames) {
        try {
            List<DbTableVo> tables = this.genTableMapper.selectDbTableListByNames(tableNames);
            if (tables.size() <= 0) {
                throw new Exception("\u8868\u4e0d\u5b58\u5728!");
            }
            for (DbTableVo map : tables) {
                GenTable table = new GenTable();
                GenUtil.initTable(table, map);
                int row = this.genTableMapper.insert(table);
                if (row <= 0) continue;
                String tableName = map.getTableName();
                List<GenTableColumn> genTableColumns = this.genTableMapper.selectDbTableColumnsByName(tableName);
                for (GenTableColumn column : genTableColumns) {
                    GenUtil.initColumn(column, table);
                    this.genTableColumnMapper.insert(column);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new OperateException("\u5bfc\u5165\u5931\u8d25\uff1a" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void editTable(GenParam genParam) {
        if (genParam.getGenTpl().equals("tree")) {
            Assert.isFalse((boolean)genParam.getTreePrimary().equals(""), (String)"\u6811\u4e3bID\u4e0d\u80fd\u4e3a\u7a7a", (Object[])new Object[0]);
            Assert.isFalse((boolean)genParam.getTreeParent().equals(""), (String)"\u6811\u7236ID\u4e0d\u80fd\u4e3a\u7a7a", (Object[])new Object[0]);
        }
        GenTable model = (GenTable)this.genTableMapper.selectById(genParam.getId());
        Assert.notNull((Object)model, (String)"\u6570\u636e\u5df2\u4e22\u5931", (Object[])new Object[0]);
        model.setTableName(genParam.getTableName());
        model.setTableComment(genParam.getTableComment());
        model.setAuthorName(genParam.getAuthorName());
        model.setEntityName(genParam.getEntityName());
        model.setModuleName(genParam.getModuleName());
        model.setFunctionName(genParam.getFunctionName());
        model.setTreePrimary(genParam.getTreePrimary());
        model.setTreeParent(genParam.getTreeParent());
        model.setTreeName(genParam.getTreeName());
        model.setRemarks(genParam.getRemarks());
        model.setGenTpl(genParam.getGenTpl());
        model.setGenType(genParam.getGenType());
        model.setGenPath(genParam.getGenPath());
        model.setSubTableFk(genParam.getSubTableFk());
        model.setSubTableFr(genParam.getSubTableFr());
        model.setSubTableName(genParam.getSubTableName());
        model.setMenuStatus(genParam.getMenuStatus());
        model.setMenuPid(genParam.getMenuPid());
        model.setMenuName(genParam.getMenuName());
        this.genTableMapper.updateById(model);
        for (Map<String, String> item : genParam.getColumn()) {
            Integer id = Integer.parseInt(item.get("id"));
            GenTableColumn column = (GenTableColumn)this.genTableColumnMapper.selectById(id);
            column.setColumnComment(item.get("columnComment"));
            column.setJavaType(item.get("javaType"));
            column.setJavaField(item.get("javaField"));
            column.setIsRequired(Integer.parseInt(item.get("isRequired")));
            column.setIsInsert(Integer.parseInt(item.get("isInsert")));
            column.setIsEdit(Integer.parseInt(item.get("isEdit")));
            column.setIsList(Integer.parseInt(item.get("isList")));
            column.setIsQuery(Integer.parseInt(item.get("isQuery")));
            column.setQueryType(item.get("queryType"));
            column.setHtmlType(item.get("htmlType"));
            column.setDictType(item.get("dictType"));
            this.genTableColumnMapper.updateById(column);
        }
    }

    @Override
    @Transactional
    public void deleteTable(List<Integer> ids) {
        for (Integer id : ids) {
            this.genTableMapper.deleteById(id);
            this.genTableColumnMapper.delete(new QueryWrapper<com.mdd.generator.entity.GenTableColumn>().eq("table_id", (Object)id));
        }
    }

    @Override
    @Transactional
    public void syncTable(Integer id) {
        GenTable genTable = (GenTable)this.genTableMapper.selectById(id);
        Assert.notNull((Object)genTable, (String)"\u751f\u6210\u6570\u636e\u4e0d\u5b58\u5728\uff01", (Object[])new Object[0]);
        List<GenTableColumn> genTableColumns = (java.util.List<GenTableColumn>) this.genTableColumnMapper.selectList(new QueryWrapper<GenTableColumn>().eq("table_id", (Object)id).orderByAsc("sort"));
        Assert.isFalse((boolean)StringUtils.isEmpty((Collection)genTableColumns), (String)"\u65e7\u6570\u636e\u5f02\u5e38\uff01", (Object[])new Object[0]);
        Map<String, GenTableColumn> tableColumnMap = genTableColumns.stream().collect(Collectors.toMap(GenTableColumn::getColumnName, Function.identity()));
        List<GenTableColumn> columns = this.genTableMapper.selectDbTableColumnsByName(genTable.getTableName());
        if (StringUtils.isNull(columns)) {
            throw new OperateException("\u540c\u6b65\u7ed3\u6784\u5931\u8d25,\u539f\u8868\u7ed3\u6784\u4e0d\u5b58\u5728\uff01");
        }
        columns.forEach(column -> {
            GenUtil.initColumn(column, genTable);
            if (tableColumnMap.containsKey(column.getColumnName())) {
                GenTableColumn prevColumn = (GenTableColumn)tableColumnMap.get(column.getColumnName());
                column.setId(prevColumn.getId());
                if (column.getIsList() != null && column.getIsList() == 1) {
                    column.setDictType(prevColumn.getDictType());
                    column.setQueryType(prevColumn.getQueryType());
                }
                if (prevColumn.getIsRequired() == 1 && prevColumn.getIsPk() == 0 && (prevColumn.getIsInsert() == 1 || prevColumn.getIsEdit() == 1)) {
                    column.setHtmlType(prevColumn.getHtmlType());
                    column.setIsRequired(prevColumn.getIsRequired());
                }
                this.genTableColumnMapper.updateById(column);
            } else {
                this.genTableColumnMapper.insert(column);
            }
        });
        List<String> dbTableColumnNames = columns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());
        List<GenTableColumn> delColumns = genTableColumns.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delColumns)) {
            for (GenTableColumn item : delColumns) {
                this.genTableColumnMapper.deleteById(item);
            }
        }
    }

    @Override
    public Map<String, String> previewCode(Integer id) {
        GenTable table = (GenTable)this.genTableMapper.selectById(id);
        Assert.notNull((Object)table, (String)"\u8bb0\u5f55\u4e22\u5931\uff01", (Object[])new Object[0]);
        List columns = this.genTableColumnMapper.selectList(new QueryWrapper<GenTableColumn>().eq("table_id", (Object)id).orderByAsc("sort"));
        VelocityUtil.initVelocity();
        VelocityContext context = VelocityUtil.prepareContext(table, columns);
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        List<String> templates = VelocityUtil.getTemplateList(table.getGenTpl(), columns);
        for (String template : templates) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate((String)template, (String)"UTF-8");
            tpl.merge((Context)context, (Writer)sw);
            map.put(template.replace(".vm", ""), sw.toString());
        }
        return map;
    }

    @Override
    public byte[] downloadCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            this.genZipCode(tableName, zip);
        }
        IOUtils.closeQuietly((OutputStream)zip);
        return outputStream.toByteArray();
    }

    @Override
    public void genCode(String tableName) {
        GenTable table = (GenTable)this.genTableMapper.selectOne(new QueryWrapper<GenTable>().eq("table_name", (Object)tableName).last("limit 1"));
        Integer tableId = table.getId();
        List columns = this.genTableColumnMapper.selectList(new QueryWrapper<GenTableColumn>().orderByAsc("sort").eq("table_id", (Object)tableId));
        VelocityUtil.initVelocity();
        VelocityContext context = VelocityUtil.prepareContext(table, columns);
        List<String> templates = VelocityUtil.getTemplateList(table.getGenTpl(), columns);
        for (String template : templates) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate((String)template, (String)"UTF-8");
            tpl.merge((Context)context, (Writer)sw);
            try {
                String basePath = VelocityUtil.getGenPath(table);
                String tplPaths = VelocityUtil.getFileName(template, table);
                if (tplPaths.startsWith("vue")) continue;
                String filePath = basePath + VelocityUtil.getFileName(template, table);
                filePath = filePath.replaceFirst("java/", "");
                FileUtils.writeStringToFile((File)new File(filePath), (String)sw.toString(), (String)"UTF-8");
            }
            catch (IOException e) {
                log.error("\u751f\u6210\u6e32\u67d3\u6a21\u677f\u5931\u8d25: " + e.getMessage());
            }
        }
        this.genAutoMenu(table);
    }

    private void genZipCode(String tableName, ZipOutputStream zip) {
        GenTable table = (GenTable)this.genTableMapper.selectOne(new QueryWrapper<GenTable>().eq("table_name", (Object)tableName).last("limit 1"));
        List columns = this.genTableColumnMapper.selectList(new QueryWrapper<GenTableColumn>().orderByAsc("sort").eq("table_id", (Object)table.getId()));
        VelocityUtil.initVelocity();
        VelocityContext context = VelocityUtil.prepareContext(table, columns);
        List<String> templates = VelocityUtil.getTemplateList(table.getGenTpl(), columns);
        for (String template : templates) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate((String)template, (String)"UTF-8");
            tpl.merge((Context)context, (Writer)sw);
            try {
                zip.putNextEntry(new ZipEntry(VelocityUtil.getFileName(template, table)));
                IOUtils.write((String)sw.toString(), (OutputStream)zip, (String)"UTF-8");
                zip.flush();
                zip.closeEntry();
            }
            catch (IOException e) {
                log.error("\u751f\u6210\u6e32\u67d3\u6a21\u677f\u5931\u8d25: " + e.getMessage());
            }
        }
    }

    private void genAutoMenu(GenTable table) {
        if (table.getMenuStatus().equals(1)) {
            SystemMenu authMenu = new SystemMenu();
            authMenu.setPid(table.getMenuPid());
            authMenu.setType("C");
            authMenu.setName(table.getMenuName());
            authMenu.setPaths(table.getModuleName());
            authMenu.setPerms(table.getModuleName() + ":list");
            authMenu.setComponent(table.getModuleName() + "/index");
            authMenu.setName(table.getMenuName());
            authMenu.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            authMenu.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.systemAuthMenuMapper.insert(authMenu);
            for (String op : Arrays.asList("detail", "add", "edit", "del")) {
                String menuName = "";
                switch (op) {
                    case "detail": {
                        menuName = "\u8be6\u60c5";
                        break;
                    }
                    case "add": {
                        menuName = "\u65b0\u589e";
                        break;
                    }
                    case "edit": {
                        menuName = "\u7f16\u8f91";
                        break;
                    }
                    case "del": {
                        menuName = "\u5220\u9664";
                    }
                }
                String perms = table.getModuleName() + ":" + op;
                SystemMenu systemAuthMenu = new SystemMenu();
                systemAuthMenu.setPid(authMenu.getId());
                systemAuthMenu.setType("A");
                systemAuthMenu.setPerms(perms);
                systemAuthMenu.setName(table.getMenuName() + menuName);
                systemAuthMenu.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                systemAuthMenu.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.systemAuthMenuMapper.insert(systemAuthMenu);
            }
        }
    }
}

