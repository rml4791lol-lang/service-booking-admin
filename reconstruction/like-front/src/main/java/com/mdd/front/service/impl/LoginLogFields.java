package com.mdd.front.service.impl;

import com.mdd.common.util.ToolUtils;

/**
 * #162 教练登录失败诊断日志的字段取值与纯函数。只补可观测性：
 * 响应体/文案/限频逻辑完全不变（#157 防枚举口径保持）。
 *
 * <p>日志固定字段：instructorId（查无账号为 0）、phoneMasked、reason、saltFp；
 * 占位符参数，禁止拼接字符串；禁止出现明文口令与明文盐。</p>
 *
 * <p>故意只依赖 JDK 与 like-common 纯工具（ToolUtils）：让 LoginLogFieldsTest 能在
 * 不挂全量运行时类路径的情况下直接加载本类做离线断言（与 LoginFailLimitGuardTest
 * 同款「带 main 断言器」）；打码/盐指纹逻辑不引 Spring、MyBatis 等运行时。</p>
 */
final class LoginLogFields {

    /** reason：账号不存在 */
    static final String REASON_NO_ACCOUNT = "NO_ACCOUNT";
    /** reason：教练账号未开通（口令尚未生成） */
    static final String REASON_NOT_OPENED = "NOT_OPENED";
    /** reason：口令不符（响应与 NO_ACCOUNT 同文案，仅日志区分） */
    static final String REASON_PASSWORD_MISMATCH = "PASSWORD_MISMATCH";
    /** reason：口令加盐配置缺失（#159 makePassword 抛 IllegalStateException） */
    static final String REASON_CONFIG = "CONFIG";

    /**
     * 手机号打码：11 位手机号保留前 3 后 4（138****1234），
     * 其余输入（异常长度/空串）整体打码为 "****"，null 归 "-"，确保不泄露原值。
     * #182 收编：实现委托 ToolUtils.maskPhone(phone, true)，口径不变。
     */
    static String maskPhone(String phone) {
        return ToolUtils.maskPhone(phone, true);
    }

    /**
     * 盐指纹：sha256(盐) 前 8 位十六进制。日志只落指纹，不落明文盐；
     * 盐缺失（null/空/空白）返回 "none"。
     */
    static String saltFingerprintOf(String salt) {
        if (salt == null || salt.trim().isEmpty()) {
            return "none";
        }
        try {
            byte[] digest = java.security.MessageDigest.getInstance("SHA-256")
                    .digest(salt.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(8);
            for (int i = 0; i < 4; i++) {
                hex.append(Character.forDigit((digest[i] >> 4) & 0xF, 16));
                hex.append(Character.forDigit(digest[i] & 0xF, 16));
            }
            return hex.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            // JVM 规范必带 SHA-256，理论不可达；兜底不抛出，避免掩盖登录主流程
            return "none";
        }
    }

    private LoginLogFields() {
    }
}
