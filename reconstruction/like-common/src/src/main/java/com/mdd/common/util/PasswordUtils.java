package com.mdd.common.util;

import com.mdd.common.exception.OperateException;

/**
 * 后台账号口令强度策略。
 *
 * <p>规则：8~20 位；不能包含空白字符；不能是纯数字；
 * 必须包含大写字母、小写字母、数字、特殊符号中的至少 3 类。</p>
 */
public final class PasswordUtils {

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 20;

    private PasswordUtils() {
    }

    public static void validateAdminPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new OperateException("密码不能为空");
        }
        if (password.length() < MIN_LENGTH) {
            throw new OperateException("密码长度不能少于" + MIN_LENGTH + "位");
        }
        if (password.length() > MAX_LENGTH) {
            throw new OperateException("密码长度不能超过" + MAX_LENGTH + "位");
        }
        if (containsWhitespace(password)) {
            throw new OperateException("密码不能包含空格");
        }
        if (password.matches("\\d+")) {
            throw new OperateException("密码不能为纯数字");
        }
        int characterClasses = 0;
        if (password.matches(".*[A-Z].*")) {
            characterClasses++;
        }
        if (password.matches(".*[a-z].*")) {
            characterClasses++;
        }
        if (password.matches(".*[0-9].*")) {
            characterClasses++;
        }
        if (password.matches(".*[^a-zA-Z0-9].*")) {
            characterClasses++;
        }
        if (characterClasses < 3) {
            throw new OperateException("密码必须包含大小写字母、数字、特殊符号中的至少3种");
        }
    }

    /**
     * 供自检脚本使用：不抛出异常，只返回是否满足策略。
     */
    public static boolean isStrong(String password) {
        try {
            validateAdminPassword(password);
            return true;
        } catch (OperateException e) {
            return false;
        }
    }

    private static boolean containsWhitespace(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isWhitespace(value.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
