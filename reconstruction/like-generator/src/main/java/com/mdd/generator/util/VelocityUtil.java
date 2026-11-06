/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.util.StringUtils
 *  org.apache.velocity.VelocityContext
 *  org.apache.velocity.app.Velocity
 *  org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
 */
package com.mdd.generator.util;

import com.mdd.common.config.GlobalConfig;
import com.mdd.common.util.StringUtils;
import com.mdd.generator.config.GenConfig;
import com.mdd.generator.constant.SqlConstants;
import com.mdd.generator.entity.GenTable;
import com.mdd.generator.entity.GenTableColumn;
import com.mdd.generator.util.GenUtil;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityUtil {
    public static void initVelocity() {
        try {
            Properties p = new Properties();
            p.setProperty("resource.loader", "classpath");
            p.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            p.setProperty("resource.default_encoding", "UTF-8");
            Velocity.init((Properties)p);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static VelocityContext prepareContext(GenTable table, List<GenTableColumn> columns) {
        boolean isSearch = false;
        String primaryKey = "id";
        String primaryField = "id";
        LinkedList<String> allFields = new LinkedList<String>();
        LinkedList<String> listFields = new LinkedList<String>();
        LinkedList<String> detailFields = new LinkedList<String>();
        LinkedList<String> dictFields = new LinkedList<String>();
        for (GenTableColumn column : columns) {
            allFields.add(column.getColumnName());
            if (column.getIsList() == 1) {
                listFields.add(column.getColumnName());
            }
            if (column.getIsEdit() == 1) {
                detailFields.add(column.getColumnName());
            }
            if (column.getIsQuery() == 1) {
                isSearch = true;
            }
            if (column.getIsPk() == 1) {
                primaryKey = column.getJavaField();
                primaryField = column.getColumnName();
            }
            if (!StringUtils.isNotEmpty((String)column.getDictType()) || dictFields.contains(column.getDictType())) continue;
            dictFields.add(column.getDictType());
        }
        table.setSubTableName(table.getSubTableName().replace(GlobalConfig.tablePrefix, ""));
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("genTpl", (Object)table.getGenTpl());
        velocityContext.put("tableName", (Object)table.getTableName());
        velocityContext.put("authorName", (Object)table.getAuthorName());
        velocityContext.put("packageName", (Object)GenConfig.packageName);
        velocityContext.put("EntityName", (Object)table.getEntityName());
        velocityContext.put("entityName", (Object)StringUtils.uncapitalize((String)table.getEntityName()));
        velocityContext.put("moduleName", (Object)table.getModuleName());
        velocityContext.put("functionName", (Object)(StringUtils.isNotEmpty((String)table.getFunctionName()) ? table.getFunctionName() : "\u3010\u8bf7\u586b\u5199\u529f\u80fd\u540d\u79f0\u3011"));
        velocityContext.put("notesType", (Object)GenConfig.notesType);
        velocityContext.put("table", (Object)table);
        velocityContext.put("columns", columns);
        velocityContext.put("subTableAlias", (Object)GenUtil.getTableAlias(table.getSubTableName()));
        velocityContext.put("dateFields", (Object)SqlConstants.COLUMN_TIME_NAME);
        velocityContext.put("primaryKey", (Object)primaryKey);
        velocityContext.put("primaryField", (Object)primaryField);
        velocityContext.put("allFields", allFields);
        velocityContext.put("listFields", listFields);
        velocityContext.put("detailFields", detailFields);
        velocityContext.put("dictFields", dictFields);
        velocityContext.put("isSearch", (Object)isSearch);
        return velocityContext;
    }

    public static String getGenPath(GenTable table) {
        String genPath = table.getGenPath();
        if (StringUtils.equals((CharSequence)genPath, (CharSequence)"/")) {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator;
        }
        return genPath + File.separator;
    }

    public static List<String> getTemplateList(String genTpl, List<GenTableColumn> columns) {
        LinkedList<String> templates = new LinkedList<String>();
        templates.add("java/controller.java.vm");
        templates.add("java/service.java.vm");
        templates.add("java/serviceImpl.java.vm");
        templates.add("java/validateCreate.java.vm");
        templates.add("java/validateUpdate.java.vm");
        templates.add("java/validateSearch.java.vm");
        templates.add("java/voListed.java.vm");
        templates.add("java/voDetail.java.vm");
        templates.add("java/entity.java.vm");
        templates.add("java/mapper.java.vm");
        templates.add("vue/api.ts.vm");
        templates.add("vue/edit.vue.vm");
        if ("crud".equals(genTpl)) {
            templates.add("vue/index.vue.vm");
        } else if ("tree".equals(genTpl)) {
            templates.add("vue/index-tree.vue.vm");
        }
        return templates;
    }

    public static String getFileName(String template, GenTable genTable) {
        String fileName = "";
        String entityName = genTable.getEntityName();
        String moduleName = genTable.getModuleName();
        if (template.contains("controller.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/controller/{}Controller.java", (Object[])new Object[]{GenConfig.adminPackage, entityName});
        } else if (template.contains("service.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/service/I{}Service.java", (Object[])new Object[]{GenConfig.adminPackage, entityName});
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/service/impl/{}ServiceImpl.java", (Object[])new Object[]{GenConfig.adminPackage, entityName});
        } else if (template.contains("validateCreate.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/validate/{}CreateValidate.java", (Object[])new Object[]{GenConfig.adminPackage, entityName});
        } else if (template.contains("validateUpdate.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/validate/{}UpdateValidate.java", (Object[])new Object[]{GenConfig.adminPackage, entityName});
        } else if (template.contains("validateSearch.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/validate/{}SearchValidate.java", (Object[])new Object[]{GenConfig.adminPackage, entityName});
        } else if (template.contains("voListed.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/vo/{}ListedVo.java", (Object[])new Object[]{GenConfig.adminPackage, entityName});
        } else if (template.contains("voDetail.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/vo/{}DetailVo.java", (Object[])new Object[]{GenConfig.adminPackage, entityName});
        } else if (template.contains("entity.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/entity/{}.java", (Object[])new Object[]{GenConfig.commonPackage, entityName});
        } else if (template.contains("mapper.java.vm")) {
            fileName = StringUtils.format((String)"java/{}/mapper/{}Mapper.java", (Object[])new Object[]{GenConfig.commonPackage, entityName});
        } else if (template.contains("vue/api.ts.vm")) {
            fileName = StringUtils.format((String)"vue/api/{}.ts", (Object[])new Object[]{moduleName});
        } else if (template.contains("vue/edit.vue.vm")) {
            fileName = StringUtils.format((String)"vue/views/{}/edit.vue", (Object[])new Object[]{moduleName});
        } else if (template.contains("vue/index.vue.vm")) {
            fileName = StringUtils.format((String)"vue/views/{}/index.vue", (Object[])new Object[]{moduleName});
        } else if (template.contains("vue/index-tree.vue.vm")) {
            fileName = StringUtils.format((String)"vue/views/{}/index.vue", (Object[])new Object[]{moduleName});
        }
        return fileName;
    }
}

