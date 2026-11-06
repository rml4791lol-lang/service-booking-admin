package com.mdd.admin.export;

import com.alibaba.fastjson2.JSON;
import com.mdd.common.util.RedisUtils;
import org.springframework.stereotype.Component;

/**
 * #214 P1：Redis 票据存储（多实例共享）。
 *
 * <p>key 形如 {@code like:export:ticket:<ticket>}，TTL 与票据有效期一致；
 * 消费使用 Redis 的 GETDEL 语义（Spring Data Redis 2.7 {@code getAndDelete}），
 * 多实例并发下同一票据也只会成功一次。</p>
 */
@Component
public class RedisExportTicketStore implements ExportTicketStore {

    private static final String KEY_PREFIX = "export:ticket:";

    @Override
    public void put(ExportTicket ticket, long ttlSeconds) {
        RedisUtils.set(KEY_PREFIX + ticket.getTicket(), JSON.toJSONString(ticket), ttlSeconds);
    }

    @Override
    public ExportTicket take(String ticket) {
        Object raw = RedisUtils.getAndDelete(KEY_PREFIX + ticket);
        if (raw == null) {
            return null;
        }
        return JSON.parseObject(String.valueOf(raw), ExportTicket.class);
    }
}
