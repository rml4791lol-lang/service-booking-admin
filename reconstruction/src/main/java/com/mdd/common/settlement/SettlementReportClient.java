package com.mdd.common.settlement;

/**
 * 分账数据上报客户端。实现必须无副作用（除出站 HTTP 外），失败不得影响主业务流程。
 */
public interface SettlementReportClient {

    /**
     * 上报一个批次。
     *
     * @param batch          批次（已设置 batchNo / idempotencyKey）
     * @param idempotencyKey 请求幂等键，重试时必须保持不变
     * @return 单次调用结果；是否可重试由 {@link SettlementPushResult#isRetryable()} 表达
     */
    SettlementPushResult push(SettlementBatch batch, String idempotencyKey);

    /**
     * 客户端模式：stub / http。
     */
    String mode();
}
