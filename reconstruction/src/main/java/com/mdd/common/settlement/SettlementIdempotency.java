package com.mdd.common.settlement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 幂等键生成。
 *
 * <p>批次号 / 幂等键由“期间 + 范围 + 批次类型 + 明细内容”共同决定；
 * 相同内容重复生成得到相同键，内容修订后得到新键，避免把修订版误判成重复。
 * {@code generatedAt} 不参与计算。</p>
 */
public final class SettlementIdempotency {

    public static final String BATCH_PREFIX = "SB";
    public static final String KEY_PREFIX = "ST";
    public static final String REVERSAL_BATCH_PREFIX = "SBR";
    public static final String REVERSAL_KEY_PREFIX = "STR";

    private SettlementIdempotency() {
    }

    public static String batchNo(String periodStart, String periodEnd, String scopeType, Integer scopeId,
                                 String batchType, String originalBatchNo, List<SettlementItem> items) {
        String hash = contentHash(periodStart, periodEnd, scopeType, scopeId, batchType, originalBatchNo, items);
        String prefix = "REVERSAL".equals(batchType) ? REVERSAL_BATCH_PREFIX : BATCH_PREFIX;
        return prefix + "-" + compact(periodStart) + "-" + compact(periodEnd) + "-"
                + safeScope(scopeType) + safeId(scopeId) + "-" + hash.substring(0, 12);
    }

    public static String key(String periodStart, String periodEnd, String scopeType, Integer scopeId,
                             String batchType, String originalBatchNo, List<SettlementItem> items) {
        String hash = contentHash(periodStart, periodEnd, scopeType, scopeId, batchType, originalBatchNo, items);
        String prefix = "REVERSAL".equals(batchType) ? REVERSAL_KEY_PREFIX : KEY_PREFIX;
        return prefix + "-" + compact(periodStart) + "-" + compact(periodEnd) + "-"
                + safeScope(scopeType) + safeId(scopeId) + "-" + hash.substring(0, 12);
    }

    public static String itemId(Integer courseClassId, String periodStart, String periodEnd, boolean reversal) {
        String prefix = reversal ? "SIR" : "SI";
        return prefix + "-CLS-" + (courseClassId == null ? 0 : courseClassId)
                + "-" + compact(periodStart) + "-" + compact(periodEnd);
    }

    public static String contentHash(String periodStart, String periodEnd, String scopeType, Integer scopeId,
                                     String batchType, String originalBatchNo, List<SettlementItem> items) {
        List<SettlementItem> sorted = new ArrayList<SettlementItem>();
        if (items != null) {
            sorted.addAll(items);
        }
        Collections.sort(sorted, new Comparator<SettlementItem>() {
            @Override
            public int compare(SettlementItem left, SettlementItem right) {
                return safe(left.getItemId()).compareTo(safe(right.getItemId()));
            }
        });
        StringBuilder canonical = new StringBuilder();
        canonical.append(safe(periodStart)).append('|')
                .append(safe(periodEnd)).append('|')
                .append(safe(scopeType)).append('|')
                .append(scopeId == null ? 0 : scopeId).append('|')
                .append(safe(batchType)).append('|')
                .append(safe(originalBatchNo)).append('\n');
        for (SettlementItem item : sorted) {
            canonical.append(safe(item.getItemId())).append(',')
                    .append(safe(item.getStatus())).append(',')
                    .append(item.getCommunityId()).append(',')
                    .append(item.getAgentId()).append(',')
                    .append(item.getCourseClassId()).append(',')
                    .append(item.getInstructorId()).append(',')
                    .append(item.getOrderCount()).append(',')
                    .append(plain(item.getPaidOrderAmount())).append(',')
                    .append(item.getPurchasedLessons()).append(',')
                    .append(plain(item.getSettlementBaseAmount())).append(',')
                    .append(plain(item.getCommunityShareAmount())).append(',')
                    .append(plain(item.getAgentShareAmount())).append(',')
                    .append(plain(item.getInstructorShareAmount())).append(',')
                    .append(plain(item.getPlatformShareAmount())).append(',')
                    .append(plain(item.getUnsettledOrderAmount())).append(',');
            if (item.getCoachShares() != null) {
                for (SettlementCoachShare share : item.getCoachShares()) {
                    canonical.append(share.getInstructorId()).append(':')
                            .append(plain(share.getShareAmount())).append(';');
                }
            }
            canonical.append('\n');
        }
        return SettlementSigner.sha256Hex(canonical.toString());
    }

    private static String plain(BigDecimal value) {
        return value == null ? "0" : value.toPlainString();
    }

    private static String compact(String date) {
        return date == null ? "" : date.replace("-", "");
    }

    private static String safeScope(String scopeType) {
        return "COMMUNITY".equals(scopeType) ? "COMMUNITY" : "ALL";
    }

    private static String safeId(Integer scopeId) {
        return scopeId == null || scopeId <= 0 ? "0" : String.valueOf(scopeId);
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
