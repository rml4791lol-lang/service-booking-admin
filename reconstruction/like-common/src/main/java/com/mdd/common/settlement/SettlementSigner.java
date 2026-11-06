package com.mdd.common.settlement;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 分账接口签名工具（HMAC-SHA256，v1）。
 *
 * <p>请求签名规范：
 * {@code appId + "\n" + timestamp + "\n" + nonce + "\n" + idempotencyKey + "\n" + sha256(body)}，
 * 使用双方约定的 secret 做 HMAC-SHA256，输出小写十六进制。</p>
 */
public final class SettlementSigner {

    public static final String SIGNATURE_VERSION = "v1";
    private static final char[] HEX = "0123456789abcdef".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    private SettlementSigner() {
    }

    public static String canonical(String appId, String timestamp, String nonce,
                                    String idempotencyKey, String body) {
        return safe(appId) + "\n" + safe(timestamp) + "\n" + safe(nonce) + "\n"
                + safe(idempotencyKey) + "\n" + sha256Hex(body);
    }

    public static String sign(String secret, String canonical) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return hex(mac.doFinal(canonical.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("HMAC-SHA256 签名失败", e);
        }
    }

    public static String responseCanonical(String requestId, String timestamp, String code, String batchNo) {
        return safe(requestId) + "\n" + safe(timestamp) + "\n" + safe(code) + "\n" + safe(batchNo);
    }

    public static String sha256Hex(String body) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return hex(digest.digest(safe(body).getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 计算失败", e);
        }
    }

    public static String newNonce() {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        return hex(bytes);
    }

    public static boolean constantTimeEquals(String left, String right) {
        if (left == null || right == null) {
            return false;
        }
        return MessageDigest.isEqual(left.getBytes(StandardCharsets.UTF_8),
                right.getBytes(StandardCharsets.UTF_8));
    }

    public static String hex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            chars[i * 2] = HEX[value >>> 4];
            chars[i * 2 + 1] = HEX[value & 0x0F];
        }
        return new String(chars);
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
