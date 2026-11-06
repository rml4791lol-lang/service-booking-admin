/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.util.StringUtils
 *  org.apache.commons.lang3.RegExUtils
 */
package com.mdd.generator.util;

import com.mdd.common.config.GlobalConfig;
import com.mdd.common.util.StringUtils;
import com.mdd.generator.config.GenConfig;
import com.mdd.generator.constant.GenConstants;
import com.mdd.generator.constant.SqlConstants;
import com.mdd.generator.entity.GenTable;
import com.mdd.generator.entity.GenTableColumn;
import com.mdd.generator.vo.DbTableVo;
import java.util.Arrays;
import org.apache.commons.lang3.RegExUtils;

public class GenUtil {
    public static void initTable(GenTable table, DbTableVo map) {
        String tableName = map.getTableName();
        String tableDesc = map.getTableComment();
        table.setTableName(tableName);
        table.setTableComment(tableDesc);
        table.setAuthorName(StringUtils.isEmpty((String)map.getAuthorName()) ? "LikeAdmin" : map.getAuthorName());
        table.setEntityName(GenUtil.toClassName(tableName));
        table.setModuleName(GenUtil.toModuleName(tableName));
        table.setFunctionName(GenUtil.replaceText(tableDesc));
        table.setCreateTime(System.currentTimeMillis() / 1000L);
        table.setUpdateTime(System.currentTimeMillis() / 1000L);
    }

    public static void initColumn(GenTableColumn column, GenTable table) {
        column.setTableId(table.getId());
        String columnName = column.getColumnName();
        String columnType = GenUtil.getDbType(column.getColumnType());
        column.setColumnLength(GenUtil.getColumnLength(column.getColumnType()));
        column.setJavaField(StringUtils.toCamelCase((String)columnName));
        column.setJavaType("String");
        column.setQueryType("=");
        column.setUpdateTime(table.getUpdateTime());
        column.setCreateTime(table.getCreateTime());
        if (GenUtil.isArraysContains(SqlConstants.COLUMN_TYPE_STR, columnType).booleanValue() || GenUtil.isArraysContains(SqlConstants.COLUMN_TYPE_TEXT, columnType).booleanValue()) {
            String columnLength = GenUtil.getColumnLength(column.getColumnType());
            String htmlType = Integer.parseInt(columnLength) >= 500 || GenUtil.isArraysContains(SqlConstants.COLUMN_TYPE_TEXT, columnType) != false ? "textarea" : "input";
            column.setHtmlType(htmlType);
        } else if (GenUtil.isArraysContains(SqlConstants.COLUMN_TYPE_TIME, columnType).booleanValue()) {
            column.setJavaType("Date");
            column.setHtmlType("datetime");
        } else if (GenUtil.isArraysContains(SqlConstants.COLUMN_TIME_NAME, columnName).booleanValue()) {
            column.setJavaType("Long");
            column.setHtmlType("datetime");
        } else if (GenUtil.isArraysContains(SqlConstants.COLUMN_TYPE_NUMBER, columnType).booleanValue()) {
            column.setHtmlType("input");
            String[] str = StringUtils.split((String)StringUtils.substringBetween((String)column.getColumnType(), (String)"(", (String)")"), (String)",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                column.setJavaType("BigDecimal");
            } else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 11) {
                column.setJavaType("Integer");
            } else {
                column.setJavaType("Long");
            }
        }
        if (GenUtil.isArraysContains(SqlConstants.COLUMN_NAME_NOT_EDIT, columnName).booleanValue()) {
            column.setIsRequired(0);
        }
        if (!GenUtil.isArraysContains(SqlConstants.COLUMN_NAME_NOT_ADD, columnName).booleanValue()) {
            column.setIsInsert(GenConstants.REQUIRE);
        }
        if (!GenUtil.isArraysContains(SqlConstants.COLUMN_NAME_NOT_EDIT, columnName).booleanValue()) {
            column.setIsEdit(GenConstants.REQUIRE);
            column.setIsRequired(GenConstants.REQUIRE);
        }
        if (!GenUtil.isArraysContains(SqlConstants.COLUMN_NAME_NOT_LIST, columnName).booleanValue() && column.getIsPk() == 0) {
            column.setIsList(GenConstants.REQUIRE);
        }
        if (!GenUtil.isArraysContains(SqlConstants.COLUMN_NAME_NOT_QUERY, columnName).booleanValue() && column.getIsPk() == 0) {
            column.setIsQuery(GenConstants.REQUIRE);
        }
        if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"name") || columnName.equals("nickname") || columnName.equals("username") || columnName.equals("title") || columnName.equals("mobile")) {
            column.setQueryType("LIKE");
        }
        if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"status") || columnName.equals("isShow") || columnName.equals("isDisable")) {
            column.setHtmlType("radio");
        } else if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"type") || StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"sex")) {
            column.setHtmlType("select");
        } else if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"image")) {
            column.setHtmlType("imageUpload");
        } else if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"file")) {
            column.setHtmlType("fileUpload");
        } else if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"content")) {
            column.setHtmlType("editor");
        }
    }

    public static String toModuleName(String tableName) {
        int lastIndex = tableName.lastIndexOf("_");
        int nameLength = tableName.length();
        return StringUtils.substring((String)tableName, (int)(lastIndex + 1), (int)nameLength);
    }

    public static String toClassName(String tableName) {
        String tablePrefix = GlobalConfig.tablePrefix;
        if (GenConfig.isRemoveTablePrefix.booleanValue() && StringUtils.isNotEmpty((String)tablePrefix)) {
            String[] searchList = StringUtils.split((String)tablePrefix, (String)",");
            tableName = GenUtil.replaceFirst(tableName, searchList);
        }
        return StringUtils.convertToCamelCase((String)tableName);
    }

    public static String replaceFirst(String replaceVal, String[] searchList) {
        String text = replaceVal;
        for (String searchString : searchList) {
            if (!replaceVal.startsWith(searchString)) continue;
            text = replaceVal.replaceFirst(searchString, "");
            break;
        }
        return text;
    }

    public static String replaceText(String text) {
        return RegExUtils.replaceAll((String)text, (String)"(?:\u8868)", (String)"");
    }

    public static Boolean isArraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    public static String getDbType(String columnType) {
        if (StringUtils.indexOf((CharSequence)columnType, (CharSequence)"(") > 0) {
            return StringUtils.substringBefore((String)columnType, (String)"(");
        }
        return columnType;
    }

    public static String getColumnLength(String columnType) {
        if (StringUtils.indexOf((CharSequence)columnType, (CharSequence)"(") > 0) {
            return StringUtils.substringBetween((String)columnType, (String)"(", (String)")");
        }
        return "0";
    }

    public static String getTableAlias(String tableName) {
        if (StringUtils.isNull(tableName) || StringUtils.isEmpty((String)tableName)) {
            return "";
        }
        StringBuilder val = new StringBuilder();
        for (String name : tableName.split("_")) {
            val.append(name.charAt(0));
        }
        return val.toString();
    }
}

