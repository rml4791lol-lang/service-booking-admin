package com.mdd.front.cache;

import com.mdd.common.exception.OperateException;
import com.mdd.common.util.IpUtils;
import com.mdd.common.util.RedisUtils;

/**
 * 教练密码登录防爆破限频（#157）：按「账号+客户端IP」维度对登录失败计数，
 * 达到阈值后窗口内拒绝该组合的后续尝试（不再查库，同时挡住手机号枚举），
 * 成功登录即清零。范式照搬 SmsController 的 Redis incr + 过期窗口，
 * 键前缀/阈值/窗口/文案集中在此定义，禁止散落字符串。
 */
public class LoginFailLimitCache {

    /** Redis 失败计数键前缀 */
    public static final String KEY_PREFIX = "login:pwd:fail:";
    /** 计数窗口内允许的最大登录失败次数 */
    public static final long MAX_ATTEMPTS = 5L;
    /** 失败计数窗口（秒），自首次失败起算 */
    public static final long WINDOW_SECONDS = 600L;
    /** 账号不存在与密码错误统一后的文案（错误码一致），避免可枚举教练手机号 */
    public static final String MSG_LOGIN_FAILED = "账号或密码错误!";
    /** 触发限频后的提示文案 */
    public static final String MSG_RATE_LIMITED = "登录尝试次数过多，请稍后再试";

    /**
     * 登录尝试前检查：账号+IP 在窗口内失败次数达到阈值则拒绝。
     */
    public static void assertNotBlocked(String account) {
        if (isBlocked(toLong(RedisUtils.get(failKey(account))))) {
            throw new OperateException(MSG_RATE_LIMITED);
        }
    }

    /**
     * 记录一次登录失败；首次失败时设置过期窗口（与 SmsController 的 incr+expire 同款）。
     */
    public static void recordFailure(String account) {
        String key = failKey(account);
        Long count = RedisUtils.incr(key, 1L);
        if (count != null && count == 1L) {
            RedisUtils.expire(key, WINDOW_SECONDS);
        }
    }

    /**
     * 登录成功后清零该账号+IP 的失败计数。
     */
    public static void clear(String account) {
        RedisUtils.del(new String[]{failKey(account)});
    }

    /** 当前请求上下文的失败计数键（客户端 IP 取自请求线程本地） */
    public static String failKey(String account) {
        return failKey(account, IpUtils.getIpAddress());
    }

    /** 纯函数：账号+IP 哈希成计数键（供断言器离线校验，非请求线程也可用） */
    static String failKey(String account, String ip) {
        String safeAccount = account == null ? "" : account;
        String safeIp = ip == null ? "" : ip;
        return KEY_PREFIX + Integer.toHexString(safeAccount.hashCode())
                + ":" + Integer.toHexString(safeIp.hashCode());
    }

    /** 纯函数：失败次数是否达到限频阈值 */
    static boolean isBlocked(Long failCount) {
        return failCount != null && failCount >= MAX_ATTEMPTS;
    }

    private static Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
