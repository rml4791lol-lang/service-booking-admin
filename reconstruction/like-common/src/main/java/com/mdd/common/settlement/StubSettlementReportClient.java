package com.mdd.common.settlement;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本地 stub：不发起任何网络请求，只记录最近一次批次，便于本地切换验证与单元自测。
 */
public class StubSettlementReportClient implements SettlementReportClient {

    private static final Logger log = LoggerFactory.getLogger(StubSettlementReportClient.class);
    private static final int MAX_KEYS = 100;

    private final Map<String, String> receipts = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > MAX_KEYS;
        }
    };
    private SettlementBatch lastBatch;
    private String lastKey = "";
    private int pushCount;

    @Override
    public synchronized SettlementPushResult push(SettlementBatch batch, String idempotencyKey) {
        pushCount++;
        lastBatch = batch;
        lastKey = idempotencyKey;
        SettlementPushResult result = new SettlementPushResult();
        result.setSuccess(true);
        result.setBatchNo(batch.getBatchNo());
        result.setIdempotencyKey(idempotencyKey);
        result.setAttempts(1);
        result.setHttpStatus(200);
        result.setResponseCode("S0000");
        result.setResponseSignatureVerified(true);
        result.setReceiptBatchNo(batch.getBatchNo());
        result.setItemCountExpected(batch.getItemCount());
        result.setReceivedItemCount(batch.getItemCount());
        String existing = receipts.get(idempotencyKey);
        if (existing != null) {
            result.setDuplicate(true);
            result.setCode("S1002");
            result.setMessage("stub: 幂等键已处理，返回原回执");
            result.setReceiptNo(existing);
            result.setRequestId(existing);
        } else {
            String receipt = "STUB-" + batch.getBatchNo();
            receipts.put(idempotencyKey, receipt);
            result.setCode("S0000");
            result.setMessage("stub: 已接收");
            result.setReceiptNo(receipt);
            result.setRequestId(receipt);
        }
        log.info("分账stub接收 batchNo={} key={} items={} duplicate={}",
                batch.getBatchNo(), idempotencyKey, batch.getItemCount(), result.isDuplicate());
        return result;
    }

    @Override
    public String mode() {
        return SettlementProperties.MODE_STUB;
    }

    public synchronized Map<String, Object> statusView() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("pushCount", pushCount);
        map.put("lastKey", lastKey);
        map.put("lastBatchNo", lastBatch == null ? "" : lastBatch.getBatchNo());
        map.put("lastItemCount", lastBatch == null ? 0 : lastBatch.getItemCount());
        map.put("receiptCount", receipts.size());
        return map;
    }
}
