package com.mdd.common.util;

import java.util.Locale;

/**
 * 异常文案脱敏工具。
 *
 * <p>只允许明显面向用户的短文案（业务校验提示）返回给客户端；
 * SQL、堆栈、类名、文件路径等内部信息一律返回 null，由调用方
 * 替换为通用文案并把原始异常写入日志。</p>
 */
public final class ErrorMessageUtils {

    private static final int MAX_CLIENT_MESSAGE_LENGTH = 200;

    private static final String[] SENSITIVE_MARKERS = new String[]{
            "select ", "insert into", "update ", "delete from", "drop table", "alter table",
            "create table", "jdbc", "sql", "sqlexception", "sqlstate", "syntax error", "mybatis",
            "com.baomidou", "org.springframework", "org.mybatis", "com.zaxxer", "java.lang",
            "java.sql", "com.mysql", "caused by", "stacktrace", "exception", "file:",
            "table ", "column", "field list", "doesn't exist", "does not exist",
            ".java", ".class", "/users/", "/home/", "/var/", "/www/", "/opt/", "/tmp/",
            "/data/", "/private/", "/etc/", "/app/", "/usr/", "c:\\"
    };

    private ErrorMessageUtils() {
    }

    /**
     * 返回可安全展示给客户端的文案；无法确认安全时返回 null。
     */
    public static String safeClientMessage(String rawMessage) {
        if (rawMessage == null) {
            return null;
        }
        String message = rawMessage.trim();
        if (message.isEmpty() || message.length() > MAX_CLIENT_MESSAGE_LENGTH) {
            return null;
        }
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (ch == '\r' || ch == '\n' || ch == '\t' || Character.isISOControl(ch)) {
                return null;
            }
        }
        String lowerCase = message.toLowerCase(Locale.ROOT);
        for (String marker : SENSITIVE_MARKERS) {
            if (lowerCase.contains(marker)) {
                return null;
            }
        }
        return message;
    }
}
