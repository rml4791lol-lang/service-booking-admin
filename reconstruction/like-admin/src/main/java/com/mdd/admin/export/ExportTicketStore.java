package com.mdd.admin.export;

/**
 * #214 P1：导出票据存储。
 *
 * <p>生产实现为 {@link RedisExportTicketStore}（多实例共享、TTL 自动过期、
 * getAndDelete 原子消费）。测试用内存实现，不引入新依赖。</p>
 */
public interface ExportTicketStore {

    /**
     * 写入票据并设置 TTL。
     *
     * @param ticket     票据记录
     * @param ttlSeconds 有效秒数
     */
    void put(ExportTicket ticket, long ttlSeconds);

    /**
     * 原子取出并删除票据（单次使用）。
     *
     * @param ticket 票据字符串
     * @return 票据记录；不存在/已过期/已使用返回 null
     */
    ExportTicket take(String ticket);
}
