package com.mdd.common.util;

/**
 * 请求标识持有器。
 *
 * <p>由 {@code RequestIdFilter} 在请求进入时写入，异常处理器读取后
 * 返回给客户端并写入日志，便于把用户看到的“请求ID”与日志中的堆栈对应起来。</p>
 */
public final class RequestIdUtils {

    private static final String UNKNOWN = "unknown";
    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private RequestIdUtils() {
    }

    public static void set(String requestId) {
        CURRENT.set(requestId);
    }

    public static void clear() {
        CURRENT.remove();
    }

    /**
     * 当前请求标识；不在请求线程内时返回 unknown，保证响应文案始终可读。
     */
    public static String current() {
        String requestId = CURRENT.get();
        return requestId == null || requestId.trim().isEmpty() ? UNKNOWN : requestId;
    }
}
