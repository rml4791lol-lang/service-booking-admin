/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.generator.constant;

public class SqlConstants {
    public static final String[] COLUMN_TYPE_STR = new String[]{"char", "varchar", "nvarchar", "varchar2"};
    public static final String[] COLUMN_TYPE_TEXT = new String[]{"tinytext", "text", "mediumtext", "longtext"};
    public static final String[] COLUMN_TYPE_TIME = new String[]{"datetime", "time", "date", "timestamp"};
    public static final String[] COLUMN_TYPE_NUMBER = new String[]{"tinyint", "smallint", "mediumint", "int", "number", "integer", "bit", "bigint", "float", "double", "decimal"};
    public static final String[] COLUMN_TIME_NAME = new String[]{"create_time", "update_time", "delete_time", "start_time", "end_time"};
    public static final String[] COLUMN_NAME_NOT_ADD = new String[]{"id", "is_delete", "create_time", "update_time", "delete_time"};
    public static final String[] COLUMN_NAME_NOT_EDIT = new String[]{"is_delete", "create_time", "update_time", "delete_time"};
    public static final String[] COLUMN_NAME_NOT_LIST = new String[]{"id", "intro", "content", "is_delete", "delete_time"};
    public static final String[] COLUMN_NAME_NOT_QUERY = new String[]{"is_delete", "create_time", "update_time", "delete_time"};
}

