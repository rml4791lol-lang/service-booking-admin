package com.mdd.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.IFinanceSettlementService;
import com.mdd.admin.export.ExportFileService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceSettlementPushValidate;
import com.mdd.admin.validate.finance.FinanceSettlementSearchValidate;
import com.mdd.admin.vo.finance.SettlementAggregateVo;
import com.mdd.admin.vo.finance.SettlementExportVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.agent.Agent;
import com.mdd.common.entity.agent.AgentEarningLog;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.community.CommunityEarningLog;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLessonLog;
import com.mdd.common.entity.instructor.InstructorAccountLog;
import com.mdd.common.entity.settlement.SettlementPushLog;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.agent.AgentEarningLogMapper;
import com.mdd.common.mapper.agent.AgentMapper;
import com.mdd.common.mapper.community.CommunityEarningLogMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.instructor.InstructorAccountLogMapper;
import com.mdd.common.mapper.settlement.SettlementPushLogMapper;
import com.mdd.common.settlement.SettlementBatch;
import com.mdd.common.settlement.SettlementCoachShare;
import com.mdd.common.settlement.SettlementIdempotency;
import com.mdd.common.settlement.SettlementItem;
import com.mdd.common.settlement.SettlementProperties;
import com.mdd.common.settlement.SettlementPushResult;
import com.mdd.common.settlement.SettlementReporter;
import com.mdd.common.settlement.SettlementScope;
import com.mdd.common.settlement.SettlementSummary;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TextUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class FinanceSettlementServiceImpl implements IFinanceSettlementService {

    private static final Logger log = LoggerFactory.getLogger(FinanceSettlementServiceImpl.class);
    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final BigDecimal CENT = new BigDecimal("0.01");
    private static final String STATUS_READY = "READY";
    private static final String STATUS_HOLD = "HOLD";
    /** #71 上报日志的幂等占位与终态（与 SettlementPushLog.status 注释保持一致）。 */
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_ACCEPTED = "ACCEPTED";
    private static final String STATUS_DUPLICATE = "DUPLICATE";
    private static final String STATUS_DRY_RUN = "DRY_RUN";
    private static final String STATUS_SKIPPED = "SKIPPED";
    private static final String STATUS_DISABLED = "DISABLED";
    /** PENDING 超过该秒数视为进程中断留下的陈旧占位，允许复用同一行重试。 */
    private static final long PENDING_STALE_SECONDS = 300L;
    private static final String DEFAULT_ALL_START = "1970-01-01";

    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CommunityMapper communityMapper;
    @Resource
    AgentMapper agentMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CommunityEarningLogMapper communityEarningLogMapper;
    @Resource
    AgentEarningLogMapper agentEarningLogMapper;
    @Resource
    InstructorAccountLogMapper instructorAccountLogMapper;
    @Resource
    CourseLessonLogMapper courseLessonLogMapper;
    @Resource
    SettlementReporter settlementReporter;
    @Resource
    SettlementPushLogMapper settlementPushLogMapper;
    @Resource
    ExportFileService exportFileService;

    @Override
    public PageResult<SettlementItem> lists(PageValidate pageValidate, FinanceSettlementSearchValidate searchValidate) {
        SettlementQuery query = this.query(searchValidate);
        List<SettlementItem> rows = this.filterStatus(query.items, searchValidate.getStatus());
        int pageNo = pageValidate.getPage_no() == null ? 1 : pageValidate.getPage_no();
        int pageSize = pageValidate.getPage_size() == null ? 20 : pageValidate.getPage_size();
        int from = Math.max(0, (pageNo - 1) * pageSize);
        int to = Math.min(rows.size(), from + pageSize);
        List<SettlementItem> pageRows = from >= rows.size() ? Collections.<SettlementItem>emptyList() : rows.subList(from, to);
        SettlementSummary summary = this.summarize(rows);
        // #102：页面/接口只出真实营业额与明细，二次分配金额不再计算返回（字段名保留、值置空）。
        for (SettlementItem row : pageRows) {
            this.hideAllocation(row);
        }
        this.hideAllocation(summary);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("periodStart", query.periodStart);
        extend.put("periodEnd", query.periodEnd);
        extend.put("summary", summary);
        extend.put("isCommunityAdmin", query.communityScoped);
        extend.put("caliber", CALIBER_NOTE);
        extend.put("reconciliation", this.hiddenReconciliation());
        return PageResult.iPageHandle((long) rows.size(), (long) pageNo, (long) pageSize, pageRows, extend);
    }

    @Override
    public Map<String, Object> summary(FinanceSettlementSearchValidate searchValidate) {
        SettlementQuery query = this.query(searchValidate);
        List<SettlementItem> rows = this.filterStatus(query.items, searchValidate.getStatus());
        String dimension = normalizeDimension(searchValidate.getDimension());
        if ("coach".equals(dimension)) {
            // #102：教练「分成」维度下线；教练维度改由「教练月营业额」视图提供（只出工作量与营业额）。
            SettlementSummary empty = this.summarize(Collections.<SettlementItem>emptyList());
            this.hideAllocation(empty);
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("dimension", dimension);
            data.put("periodStart", query.periodStart);
            data.put("periodEnd", query.periodEnd);
            data.put("items", new ArrayList<SettlementAggregateVo>());
            data.put("totals", empty);
            data.put("caliber", CALIBER_NOTE);
            data.put("reconciliation", this.hiddenReconciliation());
            data.put("note", COACH_DIMENSION_NOTE);
            return data;
        }
        Map<String, SettlementAggregateVo> grouped = new LinkedHashMap<String, SettlementAggregateVo>();
        for (SettlementItem row : rows) {
            String key;
            Integer id;
            String name;
            if ("community".equals(dimension)) {
                id = row.getCommunityId() == null ? 0 : row.getCommunityId();
                key = "community:" + id;
                name = StringUtils.isNotEmpty(row.getCommunityName()) ? row.getCommunityName() : "未绑定社区";
            } else {
                id = row.getCourseClassId() == null ? 0 : row.getCourseClassId();
                key = "course:" + id;
                name = StringUtils.isNotEmpty(row.getCourseClassName()) ? row.getCourseClassName() : "未知课程";
            }
            SettlementAggregateVo vo = grouped.get(key);
            if (vo == null) {
                vo = newAggregate(dimension, key, id, name);
                grouped.put(key, vo);
            }
            vo.setItemCount(vo.getItemCount() + 1);
            vo.setOrderCount(vo.getOrderCount() + nvl(row.getOrderCount()));
            vo.setPaidOrderAmount(vo.getPaidOrderAmount().add(nvl(row.getPaidOrderAmount())));
            vo.setSettlementBaseAmount(vo.getSettlementBaseAmount().add(nvl(row.getSettlementBaseAmount())));
            vo.setCommunityShareAmount(vo.getCommunityShareAmount().add(nvl(row.getCommunityShareAmount())));
            vo.setAgentShareAmount(vo.getAgentShareAmount().add(nvl(row.getAgentShareAmount())));
            vo.setInstructorShareAmount(vo.getInstructorShareAmount().add(nvl(row.getInstructorShareAmount())));
            vo.setPlatformShareAmount(vo.getPlatformShareAmount().add(nvl(row.getPlatformShareAmount())));
            vo.setShareTotalAmount(vo.getShareTotalAmount().add(nvl(row.getShareTotalAmount())));
            if (STATUS_READY.equals(row.getStatus())) {
                vo.setReadyItemCount(vo.getReadyItemCount() + 1);
            } else {
                vo.setHoldItemCount(vo.getHoldItemCount() + 1);
            }
        }
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("dimension", dimension);
        data.put("periodStart", query.periodStart);
        data.put("periodEnd", query.periodEnd);
        // #102：维度聚合只保留订单数/实收/购买课时，二次分配金额一律置空。
        List<SettlementAggregateVo> aggregates = new ArrayList<SettlementAggregateVo>(grouped.values());
        for (SettlementAggregateVo aggregate : aggregates) {
            this.hideAllocation(aggregate);
        }
        SettlementSummary totals = this.summarize(rows);
        this.hideAllocation(totals);
        data.put("items", aggregates);
        data.put("totals", totals);
        data.put("caliber", CALIBER_NOTE);
        data.put("reconciliation", this.hiddenReconciliation());
        return data;
    }

    @Override
    public Map<String, Object> export(FinanceSettlementSearchValidate searchValidate) {
        SettlementQuery query = this.query(searchValidate);
        List<SettlementItem> rows = this.filterStatus(query.items, searchValidate.getStatus());
        List<SettlementExportVo> exportRows = new ArrayList<SettlementExportVo>();
        String period = query.periodStart + " ~ " + query.periodEnd;
        for (SettlementItem row : rows) {
            exportRows.add(toExportVo(row, period));
        }
        String fileName = sanitizeFileName(searchValidate.getFile_name());
        if (fileName.isEmpty()) {
            fileName = "settlement-" + query.periodStart.replace("-", "") + "-" + query.periodEnd.replace("-", "");
        }
        File target = exportFileService.allocate(fileName);
        // #102：后台「分账数据」页改为只读「收益明细」，导出只保留真实营业额与明细列。
        // 说明：SettlementExportVo 的字段名不能删（社区系统分账契约 v1.0 与 freeze-check 冻结），
        // 这里用 EasyExcel 的排除列能力隐藏二次分配/对账列，避免契约字段与页面口径互相绑架。
        EasyExcel.write(target.getAbsolutePath()).head(SettlementExportVo.class)
                .excludeColumnFieldNames(REVENUE_DETAIL_HIDDEN_COLUMNS)
                .excelType(ExcelTypeEnum.XLSX).sheet("收益明细").doWrite(exportRows);
        SettlementSummary exportSummary = this.summarize(rows);
        this.hideAllocation(exportSummary);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.putAll(exportFileService.issue(target, fileName + ".xlsx"));
        data.put("fileName", fileName + ".xlsx");
        data.put("rowCount", exportRows.size());
        data.put("periodStart", query.periodStart);
        data.put("periodEnd", query.periodEnd);
        data.put("summary", exportSummary);
        data.put("caliber", CALIBER_NOTE);
        data.put("reconciliation", this.hiddenReconciliation());
        return data;
    }

    @Override
    public Map<String, Object> config() {
        return settlementReporter.status();
    }

    @Override
    public Map<String, Object> push(FinanceSettlementPushValidate validate) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (!Integer.valueOf(1).equals(adminId)) {
            throw new OperateException("仅超级管理员可触发分账上报");
        }
        if (validate == null) {
            validate = new FinanceSettlementPushValidate();
        }
        String periodStart = requireDate(validate.getPeriodStart(), "periodStart");
        String periodEnd = requireDate(validate.getPeriodEnd(), "periodEnd");
        if (periodStart.compareTo(periodEnd) > 0) {
            throw new OperateException("periodStart 不能晚于 periodEnd");
        }
        String batchType = StringUtils.isEmpty(validate.getBatchType())
                ? "NORMAL" : validate.getBatchType().trim().toUpperCase();
        if (!"NORMAL".equals(batchType) && !"REVERSAL".equals(batchType)) {
            throw new OperateException("batchType 仅支持 NORMAL / REVERSAL");
        }
        if ("REVERSAL".equals(batchType) && StringUtils.isEmpty(validate.getOriginalBatchNo())) {
            throw new OperateException("冲正批次必须填写 originalBatchNo");
        }
        boolean dryRun = !Boolean.FALSE.equals(validate.getDryRun());
        String originalBatchNo = StringUtils.isEmpty(validate.getOriginalBatchNo())
                ? "" : validate.getOriginalBatchNo().trim();
        boolean communityScoped = validate.getCommunityId() != null && validate.getCommunityId() > 0;

        SettlementPushLog pushLog = new SettlementPushLog();
        pushLog.setPeriodStart(periodStart);
        pushLog.setPeriodEnd(periodEnd);
        pushLog.setBatchType(batchType);
        pushLog.setOriginalBatchNo(originalBatchNo);
        pushLog.setScopeType(communityScoped ? "COMMUNITY" : "ALL");
        pushLog.setScopeId(communityScoped ? validate.getCommunityId() : 0);
        pushLog.setDryRun(dryRun ? 1 : 0);
        pushLog.setClientMode(currentClientMode());
        pushLog.setOperatorAdminId(adminId);
        pushLog.setOperatorName(TextUtils.truncate(String.valueOf(LikeAdminThreadLocal.get("username")), 64));
        pushLog.setCreateTime((int) (System.currentTimeMillis() / 1000L));

        FinanceSettlementSearchValidate search = new FinanceSettlementSearchValidate();
        search.setStartDate(periodStart);
        search.setEndDate(periodEnd);
        search.setCommunityId(validate.getCommunityId());
        search.setStatus(STATUS_READY);
        SettlementQuery query = this.query(search);
        List<SettlementItem> ready = this.filterStatus(query.items, STATUS_READY);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("periodStart", periodStart);
        data.put("periodEnd", periodEnd);
        data.put("dryRun", dryRun);
        if (ready.isEmpty()) {
            return this.pushSkipped(pushLog, query.items, data);
        }
        boolean reversal = "REVERSAL".equals(batchType);
        List<SettlementItem> items = new ArrayList<SettlementItem>();
        for (SettlementItem row : ready) {
            items.add(reversal ? reverseItem(row) : row);
        }
        SettlementScope scope = communityScoped
                ? SettlementScope.community(validate.getCommunityId(), communityName(validate.getCommunityId()))
                : SettlementScope.all();
        SettlementBatch batch = new SettlementBatch();
        batch.setBatchType(batchType);
        batch.setOriginalBatchNo(originalBatchNo);
        batch.setPeriodStart(periodStart);
        batch.setPeriodEnd(periodEnd);
        batch.setScope(scope);
        batch.setItems(items);
        batch.setItemCount(items.size());
        batch.setGeneratedAt(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new java.util.Date()));
        batch.setSummary(this.summarize(items));
        batch.setBatchNo(SettlementIdempotency.batchNo(periodStart, periodEnd, scope.getType(), scope.getId(),
                batchType, batch.getOriginalBatchNo(), items));
        batch.setIdempotencyKey(SettlementIdempotency.key(periodStart, periodEnd, scope.getType(), scope.getId(),
                batchType, batch.getOriginalBatchNo(), items));
        pushLog.setBatchNo(batch.getBatchNo());
        pushLog.setIdempotencyKey(batch.getIdempotencyKey());
        pushLog.setScopeType(scope.getType());
        pushLog.setScopeId(scope.getId());
        pushLog.setItemCount(items.size());
        pushLog.setRequestSummary(buildRequestSummary(batch));
        data.put("batchNo", batch.getBatchNo());
        data.put("idempotencyKey", batch.getIdempotencyKey());
        data.put("itemCount", items.size());
        data.put("summary", batch.getSummary());
        data.put("batch", batch);
        // #71 幂等：同一周期/范围/批次类型 + 相同明细内容 + 相同演练标记 = 同一逻辑批次。
        // 唯一索引 uk_dedup_key 保证只落一行；已成功的直接返回缓存，失败/未出站/陈旧占位复用该行重试。
        Map<String, Object> idempotent = this.claimPushRecord(pushLog,
                batch.getIdempotencyKey() + (dryRun ? ":DRY" : ":LIVE"), data);
        if (idempotent != null) {
            return idempotent;
        }
        if (dryRun) {
            data.put("sent", false);
            data.put("message", "演练模式：未出站。确认后传 dryRun=false 再执行");
            savePushLog(pushLog, "DRY_RUN", null, 0L);
            return data;
        }
        long startedAt = System.currentTimeMillis();
        SettlementPushResult result;
        try {
            result = settlementReporter.push(batch);
        } catch (RuntimeException e) {
            log.error("分账上报执行异常 batchNo={} key={}", batch.getBatchNo(), batch.getIdempotencyKey(), e);
            SettlementPushResult failure = SettlementPushResult.failure("L0005",
                    "分账上报执行异常：" + safe(e.getMessage()), false);
            failure.setBatchNo(batch.getBatchNo());
            failure.setIdempotencyKey(batch.getIdempotencyKey());
            failure.setElapsedMs(System.currentTimeMillis() - startedAt);
            savePushLog(pushLog, "FAILED", failure, failure.getElapsedMs());
            throw e;
        }
        long elapsedMs = result.getElapsedMs() > 0 ? result.getElapsedMs() : System.currentTimeMillis() - startedAt;
        savePushLog(pushLog, pushLogStatus(result), result, elapsedMs);
        data.put("sent", true);
        data.put("result", result);
        return data;
    }

    /**
     * 无 READY 数据的幂等分支（#71）。
     *
     * <p>去重键由「期间 + 范围 + 批次类型 + 全部明细内容摘要 + 演练标记」组成：
     * 同一周期、同一数据状态重复点击只保留一条 SKIPPED 审计；HOLD 数据修订后摘要变化，
     * 才允许新的上报尝试，不会把「修好数据再推」误判成重复。</p>
     */
    private Map<String, Object> pushSkipped(SettlementPushLog pushLog, List<SettlementItem> items,
                                            Map<String, Object> data) {
        SettlementPushResult skipped = SettlementPushResult.failure("L0008", "没有可上报的 READY 分账数据", false);
        pushLog.setItemCount(0);
        pushLog.setBatchNo("");
        pushLog.setIdempotencyKey("");
        pushLog.setRequestSummary("{\"periodStart\":\"" + pushLog.getPeriodStart() + "\",\"periodEnd\":\""
                + pushLog.getPeriodEnd() + "\",\"reason\":\"no-ready-items\"}");
        String digest = SettlementIdempotency.key(pushLog.getPeriodStart(), pushLog.getPeriodEnd(),
                pushLog.getScopeType(), pushLog.getScopeId() == null ? 0 : pushLog.getScopeId(),
                pushLog.getBatchType(), pushLog.getOriginalBatchNo(), items);
        boolean dryRun = Integer.valueOf(1).equals(pushLog.getDryRun());
        Map<String, Object> idempotent = this.claimPushRecord(pushLog,
                "SKIP:" + digest + (dryRun ? ":DRY" : ":LIVE"), data);
        if (idempotent != null) {
            return idempotent;
        }
        savePushLog(pushLog, STATUS_SKIPPED, skipped, 0L);
        data.put("sent", false);
        data.put("itemCount", 0);
        data.put("message", "没有 READY 分账数据；HOLD 项请先按 dataQuality 修正，不会上报");
        data.put("result", skipped);
        return data;
    }

    /**
     * 幂等占位（#71）。
     *
     * <p>返回 {@code null} 表示本次请求已取得该逻辑批次的写入权，调用方继续演练/出站，
     * 最终把结果写回同一行；返回非 {@code null} 表示命中已有记录，直接返回幂等响应
     * （不重复下发、不新增行）。</p>
     *
     * <p>并发安全由 {@code la_settlement_push_log.uk_dedup_key} 保证：先插入 PENDING 占位，
     * 唯一键冲突说明另一请求已占位/已完成，按既有状态返回缓存或「处理中」。</p>
     */
    private Map<String, Object> claimPushRecord(SettlementPushLog pushLog, String dedupKey,
                                                Map<String, Object> data) {
        pushLog.setDedupKey(dedupKey);
        SettlementPushLog existing = settlementPushLogMapper.selectOne(new QueryWrapper<SettlementPushLog>()
                .eq("dedup_key", dedupKey).last("limit 1"));
        if (existing != null) {
            Map<String, Object> response = this.existingPushResponse(existing, data);
            if (response != null) {
                return response;
            }
            // 失败 / 未启用 / 配置错误 / 业务拒绝 / 陈旧占位：复用同一行重试，不新增记录。
            pushLog.setId(existing.getId());
        }
        pushLog.setStatus(STATUS_PENDING);
        pushLog.setSuccess(0);
        pushLog.setDuplicate(0);
        pushLog.setCode("");
        pushLog.setMessage("");
        pushLog.setHttpStatus(null);
        pushLog.setResponseCode("");
        pushLog.setRequestId("");
        pushLog.setReceiptNo("");
        pushLog.setReceiptBatchNo("");
        pushLog.setReceivedItemCount(-1);
        pushLog.setItemCountMismatch(0);
        pushLog.setAttempts(0);
        pushLog.setElapsedMs(0L);
        pushLog.setCreateTime((int) (System.currentTimeMillis() / 1000L));
        if (pushLog.getId() == null) {
            try {
                settlementPushLogMapper.insert(pushLog);
            } catch (DuplicateKeyException e) {
                SettlementPushLog concurrent = settlementPushLogMapper.selectOne(new QueryWrapper<SettlementPushLog>()
                        .eq("dedup_key", dedupKey).last("limit 1"));
                Map<String, Object> response = this.existingPushResponse(concurrent, data);
                if (response != null) {
                    return response;
                }
                return this.inFlightPushResponse(concurrent, data);
            }
        } else {
            settlementPushLogMapper.updateById(pushLog);
        }
        data.put("logId", pushLog.getId());
        return null;
    }

    /**
     * 已存在记录时：成功/演练/无数据结果直接返回缓存；处理中返回「正在上报」；
     * 其余失败类状态返回 {@code null} 由调用方复用同一行重试。
     */
    private Map<String, Object> existingPushResponse(SettlementPushLog row, Map<String, Object> data) {
        if (row == null) {
            return null;
        }
        String status = row.getStatus();
        boolean finished = Integer.valueOf(1).equals(row.getSuccess())
                || STATUS_ACCEPTED.equals(status) || STATUS_DUPLICATE.equals(status)
                || STATUS_DRY_RUN.equals(status) || STATUS_SKIPPED.equals(status);
        if (finished) {
            data.put("sent", false);
            data.put("duplicate", true);
            data.put("idempotent", true);
            data.put("cached", true);
            data.put("itemCount", row.getItemCount());
            data.put("batchNo", safe(row.getBatchNo()));
            data.put("idempotencyKey", safe(row.getIdempotencyKey()));
            data.put("status", status);
            data.put("logId", row.getId());
            data.put("message", "该周期同一批次已有上报结果（" + safe(status) + "），命中幂等，未重复下发");
            data.put("result", this.toPushResult(row));
            return data;
        }
        if (STATUS_PENDING.equals(status) && !isPendingStale(row)) {
            return this.inFlightPushResponse(row, data);
        }
        return null;
    }

    private Map<String, Object> inFlightPushResponse(SettlementPushLog row, Map<String, Object> data) {
        data.put("sent", false);
        data.put("duplicate", true);
        data.put("idempotent", true);
        data.put("cached", false);
        data.put("inFlight", true);
        data.put("status", STATUS_PENDING);
        data.put("logId", row == null ? null : row.getId());
        data.put("batchNo", row == null ? "" : safe(row.getBatchNo()));
        data.put("idempotencyKey", row == null ? "" : safe(row.getIdempotencyKey()));
        data.put("itemCount", row == null || row.getItemCount() == null ? 0 : row.getItemCount());
        data.put("message", "同一批次的另一上报请求正在处理中，本次未重复下发，请稍后查看上报日志");
        data.put("result", null);
        return data;
    }

    private boolean isPendingStale(SettlementPushLog row) {
        if (row == null || row.getCreateTime() == null) {
            return true;
        }
        long now = System.currentTimeMillis() / 1000L;
        return now - row.getCreateTime() > PENDING_STALE_SECONDS;
    }

    /**
     * 由推送日志还原上报结果（幂等命中的响应体与首次结果保持同构）。
     */
    private SettlementPushResult toPushResult(SettlementPushLog row) {
        SettlementPushResult result = new SettlementPushResult();
        result.setSuccess(Integer.valueOf(1).equals(row.getSuccess()));
        result.setDuplicate(Integer.valueOf(1).equals(row.getDuplicate()));
        result.setDisabled(STATUS_DISABLED.equals(row.getStatus()));
        result.setDryRun(STATUS_DRY_RUN.equals(row.getStatus()));
        result.setCode(safe(row.getCode()));
        result.setMessage(safe(row.getMessage()));
        result.setAttempts(row.getAttempts() == null ? 0 : row.getAttempts());
        result.setHttpStatus(row.getHttpStatus());
        result.setResponseCode(safe(row.getResponseCode()));
        result.setRequestId(safe(row.getRequestId()));
        result.setReceiptNo(safe(row.getReceiptNo()));
        result.setReceiptBatchNo(safe(row.getReceiptBatchNo()));
        result.setBatchNo(safe(row.getBatchNo()));
        result.setIdempotencyKey(safe(row.getIdempotencyKey()));
        result.setReceivedItemCount(row.getReceivedItemCount());
        result.setItemCountMismatch(Integer.valueOf(1).equals(row.getItemCountMismatch()));
        result.setElapsedMs(row.getElapsedMs() == null ? 0L : row.getElapsedMs());
        return result;
    }

    /**
     * 契约 §6.2 的上报状态；演练/无数据是后台特有的未出站分支。
     */
    private String pushLogStatus(SettlementPushResult result) {
        if (result == null) {
            return "FAILED";
        }
        if (result.isDisabled()) {
            return STATUS_DISABLED;
        }
        if (result.isSuccess()) {
            return result.isDuplicate() ? STATUS_DUPLICATE : STATUS_ACCEPTED;
        }
        if ("L0002".equals(result.getCode())) {
            return "CONFIG_ERROR";
        }
        return result.isRetryable() ? "FAILED" : "REJECTED";
    }

    private String currentClientMode() {
        SettlementProperties properties = settlementReporter.getProperties();
        if (!properties.isEnabled()) {
            return "disabled";
        }
        return properties.isHttpMode() ? SettlementProperties.MODE_HTTP : SettlementProperties.MODE_STUB;
    }

    /**
     * 上报结果落库（la_settlement_push_log）。写日志失败只告警，不影响上报返回。
     *
     * <p>#71 起幂等占位已插入同一行（{@code pushLog.id} 非空），这里改为按 id 更新，
     * 保证同一逻辑批次只有一行记录；占位被并发请求抢先时 id 为空也不会重复插入
     * （正常流程在 claimPushRecord 已返回）。</p>
     */
    private void savePushLog(SettlementPushLog pushLog, String status, SettlementPushResult result, long elapsedMs) {
        try {
            pushLog.setStatus(status);
            if (result != null) {
                pushLog.setSuccess(result.isSuccess() ? 1 : 0);
                pushLog.setDuplicate(result.isDuplicate() ? 1 : 0);
                pushLog.setCode(TextUtils.truncate(result.getCode(), 32));
                pushLog.setMessage(TextUtils.truncate(result.getMessage(), 1000));
                pushLog.setHttpStatus(result.getHttpStatus());
                pushLog.setResponseCode(TextUtils.truncate(result.getResponseCode(), 64));
                pushLog.setRequestId(TextUtils.truncate(result.getRequestId(), 128));
                pushLog.setReceiptNo(TextUtils.truncate(result.getReceiptNo(), 128));
                pushLog.setReceiptBatchNo(TextUtils.truncate(result.getReceiptBatchNo(), 64));
                pushLog.setReceivedItemCount(result.getReceivedItemCount() == null
                        ? -1 : result.getReceivedItemCount());
                pushLog.setItemCountMismatch(result.isItemCountMismatch() ? 1 : 0);
                pushLog.setAttempts(result.getAttempts());
                pushLog.setResponseSummary(buildResponseSummary(result));
            } else {
                pushLog.setSuccess(0);
                pushLog.setDuplicate(0);
                pushLog.setReceivedItemCount(-1);
                pushLog.setItemCountMismatch(0);
                pushLog.setAttempts(0);
                pushLog.setResponseSummary("{\"dryRun\":true,\"sent\":false}");
            }
            pushLog.setElapsedMs(Math.max(0L, elapsedMs));
            if (pushLog.getId() == null) {
                settlementPushLogMapper.insert(pushLog);
            } else {
                settlementPushLogMapper.updateById(pushLog);
            }
        } catch (Exception e) {
            log.error("分账上报日志落库失败 status={} batchNo={}", status, pushLog.getBatchNo(), e);
        }
    }

    private String buildRequestSummary(SettlementBatch batch) {
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("schemaVersion", batch.getSchemaVersion());
        summary.put("batchNo", batch.getBatchNo());
        summary.put("idempotencyKey", batch.getIdempotencyKey());
        summary.put("batchType", batch.getBatchType());
        summary.put("originalBatchNo", batch.getOriginalBatchNo());
        summary.put("periodStart", batch.getPeriodStart());
        summary.put("periodEnd", batch.getPeriodEnd());
        summary.put("scopeType", batch.getScope() == null ? "" : batch.getScope().getType());
        summary.put("scopeId", batch.getScope() == null ? 0 : batch.getScope().getId());
        summary.put("itemCount", batch.getItemCount());
        summary.put("generatedAt", batch.getGeneratedAt());
        summary.put("summary", batch.getSummary());
        return TextUtils.truncate(JSON.toJSONString(summary), 4000);
    }

    private String buildResponseSummary(SettlementPushResult result) {
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("success", result.isSuccess());
        summary.put("duplicate", result.isDuplicate());
        summary.put("retryable", result.isRetryable());
        summary.put("disabled", result.isDisabled());
        summary.put("code", result.getCode());
        summary.put("responseCode", result.getResponseCode());
        summary.put("message", TextUtils.truncate(result.getMessage(), 500));
        summary.put("httpStatus", result.getHttpStatus());
        summary.put("requestId", result.getRequestId());
        summary.put("receiptNo", result.getReceiptNo());
        summary.put("receiptBatchNo", result.getReceiptBatchNo());
        summary.put("receivedItemCount", result.getReceivedItemCount());
        summary.put("itemCountExpected", result.getItemCountExpected());
        summary.put("itemCountMismatch", result.isItemCountMismatch());
        summary.put("attempts", result.getAttempts());
        summary.put("elapsedMs", result.getElapsedMs());
        summary.put("responseSignatureVerified", result.isResponseSignatureVerified());
        return TextUtils.truncate(JSON.toJSONString(summary), 4000);
    }

    private SettlementQuery query(FinanceSettlementSearchValidate search) {
        SettlementQuery query = new SettlementQuery();
        Integer communityScope = this.resolveCommunityScope(search.getCommunityId());
        query.communityScoped = communityScope != null;
        LocalDate requestedStart = parseOptionalDate(search.getStartDate(), "startDate");
        LocalDate requestedEnd = parseOptionalDate(search.getEndDate(), "endDate");
        if (requestedStart != null && requestedEnd != null && requestedStart.isAfter(requestedEnd)) {
            throw new OperateException("开始日期不能晚于结束日期");
        }
        LocalDate end = requestedEnd == null ? LocalDate.now() : requestedEnd;
        Long startEpoch = requestedStart == null ? null : startOfDay(requestedStart);
        Long endEpoch = endOfDay(end);

        List<CourseClass> classes = this.loadClasses(search, communityScope);
        query.periodEnd = end.toString();
        if (classes.isEmpty()) {
            query.periodStart = requestedStart == null ? end.minusDays(29).toString() : requestedStart.toString();
            query.items = Collections.emptyList();
            return query;
        }
        Map<Integer, CourseClass> classMap = new LinkedHashMap<Integer, CourseClass>();
        List<Integer> classIds = new ArrayList<Integer>();
        for (CourseClass courseClass : classes) {
            classMap.put(courseClass.getId(), courseClass);
            classIds.add(courseClass.getId());
        }

        List<RechargeOrder> orders = this.loadOrders(classIds, startEpoch, endEpoch);
        List<CommunityEarningLog> communityLogs = this.loadCommunityLogs(classIds, startEpoch, endEpoch);
        List<AgentEarningLog> agentLogs = this.loadAgentLogs(classIds, startEpoch, endEpoch);
        List<CourseLessonLog> lessonLogs = this.loadLessonLogs(classIds);
        Map<Integer, CourseLessonLog> lessonLogMap = new HashMap<Integer, CourseLessonLog>();
        for (CourseLessonLog lessonLog : lessonLogs) {
            lessonLogMap.put(lessonLog.getId(), lessonLog);
        }
        List<InstructorAccountLog> instructorLogs = this.loadInstructorLogs(classIds, lessonLogMap, startEpoch, endEpoch,
                search.getInstructorId());
        Long minTimestamp = null;
        for (RechargeOrder order : orders) {
            minTimestamp = min(minTimestamp, order.getPayTime());
        }
        for (CommunityEarningLog log : communityLogs) {
            minTimestamp = min(minTimestamp, log.getCreateTime());
        }
        for (AgentEarningLog log : agentLogs) {
            minTimestamp = min(minTimestamp, log.getCreateTime());
        }
        for (InstructorAccountLog log : instructorLogs) {
            minTimestamp = min(minTimestamp, log.getCreateTime());
        }
        if (requestedStart != null) {
            query.periodStart = requestedStart.toString();
        } else if (minTimestamp != null) {
            query.periodStart = timestampToDate(minTimestamp).toString();
        } else {
            query.periodStart = end.minusDays(29).toString();
        }

        Map<Integer, ClassAgg> aggregates = new HashMap<Integer, ClassAgg>();
        for (Integer classId : classIds) {
            aggregates.put(classId, new ClassAgg());
        }
        Set<Integer> referencedOrderIds = new HashSet<Integer>();
        for (RechargeOrder order : orders) {
            ClassAgg agg = aggregates.get(order.getCourseClassId());
            if (agg == null) {
                continue;
            }
            agg.orderCount++;
            agg.paidOrderAmount = agg.paidOrderAmount.add(nvl(order.getOrderAmount()));
            agg.purchasedLessons += order.getLessonCount() == null ? 0 : order.getLessonCount();
        }
        for (CommunityEarningLog log : communityLogs) {
            ClassAgg agg = aggregates.get(log.getCourseClassId());
            if (agg == null) {
                continue;
            }
            agg.communityLogs++;
            agg.communityBase = agg.communityBase.add(nvl(log.getOrderAmount()));
            agg.communityShare = agg.communityShare.add(nvl(log.getEarningAmount()));
            if (log.getOrderId() != null && log.getOrderId() > 0) {
                referencedOrderIds.add(log.getOrderId());
                agg.orderIds.add(log.getOrderId());
            }
            if (rateMismatch(log.getOrderAmount(), log.getCommissionRate(), log.getEarningAmount())) {
                agg.communityRateMismatch++;
            }
        }
        for (AgentEarningLog log : agentLogs) {
            ClassAgg agg = aggregates.get(log.getCourseClassId());
            if (agg == null) {
                continue;
            }
            agg.agentLogs++;
            agg.agentBase = agg.agentBase.add(nvl(log.getOrderAmount()));
            agg.agentShare = agg.agentShare.add(nvl(log.getEarningAmount()));
            if (log.getOrderId() != null && log.getOrderId() > 0) {
                referencedOrderIds.add(log.getOrderId());
                agg.orderIds.add(log.getOrderId());
            }
            if (rateMismatch(log.getOrderAmount(), log.getCommissionRate(), log.getEarningAmount())) {
                agg.agentRateMismatch++;
            }
        }
        for (InstructorAccountLog log : instructorLogs) {
            Integer classId = resolveClassId(log.getRelationId(), lessonLogMap, classMap);
            if (classId == null) {
                continue;
            }
            ClassAgg agg = aggregates.get(classId);
            if (agg == null) {
                continue;
            }
            agg.coachLogs++;
            agg.coachShare = agg.coachShare.add(nvl(log.getAmount()));
            agg.coachIds.add(log.getInstructorId());
            if (log.getInstructorId() != null) {
                BigDecimal previous = agg.coachShareByCoach.get(log.getInstructorId());
                agg.coachShareByCoach.put(log.getInstructorId(),
                        (previous == null ? ZERO : previous).add(nvl(log.getAmount())));
            }
        }
        Set<Integer> existingOrderIds = new HashSet<Integer>();
        if (!referencedOrderIds.isEmpty()) {
            for (RechargeOrder order : rechargeOrderMapper.selectBatchIds(referencedOrderIds)) {
                existingOrderIds.add(order.getId());
            }
        }

        Map<Integer, String> communityNames = this.loadCommunityNames(classMap);
        Map<Integer, String> agentNames = this.loadAgentNames(classMap);
        Map<Integer, String> instructorNames = this.loadInstructorNames(classMap, aggregates);

        List<SettlementItem> items = new ArrayList<SettlementItem>();
        for (Integer classId : classIds) {
            CourseClass courseClass = classMap.get(classId);
            ClassAgg agg = aggregates.get(classId);
            if (courseClass == null || agg == null || !agg.hasActivity()) {
                continue;
            }
            if (search.getInstructorId() != null && search.getInstructorId() > 0
                    && !search.getInstructorId().equals(courseClass.getInstructorId())
                    && !agg.coachIds.contains(search.getInstructorId())) {
                continue;
            }
            SettlementItem item = this.buildItem(courseClass, agg, query.periodStart, query.periodEnd,
                    communityNames, agentNames, instructorNames, existingOrderIds);
            items.add(item);
        }
        Collections.sort(items, new Comparator<SettlementItem>() {
            @Override
            public int compare(SettlementItem left, SettlementItem right) {
                return safeInt(left.getCourseClassId()).compareTo(safeInt(right.getCourseClassId()));
            }
        });
        query.items = items;
        boolean itemScoped = (search.getCourseClassId() != null && search.getCourseClassId() > 0)
                || (search.getAgentId() != null && search.getAgentId() > 0)
                || (search.getInstructorId() != null && search.getInstructorId() > 0);
        Set<Integer> itemClassIds = new HashSet<Integer>();
        for (SettlementItem item : items) {
            itemClassIds.add(item.getCourseClassId());
        }
        query.reconciliation = this.buildReconciliation(communityScope, startEpoch, endEpoch,
                itemScoped, itemClassIds);
        return query;
    }

    private Map<String, Object> buildReconciliation(Integer communityScope, Long requestedStart, Long requestedEnd,
                                                     boolean itemScoped, Set<Integer> itemClassIds) {
        long start = requestedStart == null ? 0L : requestedStart;
        long end = requestedEnd == null ? endOfDay(LocalDate.now()) : requestedEnd;
        Map<String, Object> reconciliation = new LinkedHashMap<String, Object>();
        Map<String, Object> ledger = new LinkedHashMap<String, Object>();
        Map<String, Object> included = new LinkedHashMap<String, Object>();
        Map<String, Object> excluded = new LinkedHashMap<String, Object>();

        Set<Integer> ledgerClassIds = null;
        if (itemScoped) {
            ledgerClassIds = new HashSet<Integer>(itemClassIds);
        } else if (communityScope != null) {
            ledgerClassIds = new HashSet<Integer>();
            for (CourseClass courseClass : courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                    .select("id").eq("is_delete", 0).eq("community_id", communityScope))) {
                ledgerClassIds.add(courseClass.getId());
            }
        }

        QueryWrapper<CommunityEarningLog> communityQuery = new QueryWrapper<CommunityEarningLog>();
        communityQuery.isNull("delete_time").ge("create_time", start).le("create_time", end);
        if (communityScope != null) {
            communityQuery.eq("community_id", communityScope);
        }
        if (ledgerClassIds != null) {
            if (ledgerClassIds.isEmpty()) {
                communityQuery.eq("course_class_id", -1);
            } else {
                communityQuery.in("course_class_id", ledgerClassIds);
            }
        }
        List<CommunityEarningLog> allCommunityLogs = communityEarningLogMapper.selectList(communityQuery);
        BigDecimal communityBase = ZERO;
        BigDecimal communityShare = ZERO;
        for (CommunityEarningLog log : allCommunityLogs) {
            communityBase = communityBase.add(nvl(log.getOrderAmount()));
            communityShare = communityShare.add(nvl(log.getEarningAmount()));
        }
        ledger.put("community", ledgerItem(allCommunityLogs.size(), communityBase, communityShare));

        QueryWrapper<AgentEarningLog> agentQuery = new QueryWrapper<AgentEarningLog>();
        agentQuery.isNull("delete_time").ge("create_time", start).le("create_time", end);
        if (ledgerClassIds != null) {
            if (ledgerClassIds.isEmpty()) {
                agentQuery.eq("course_class_id", -1);
            } else {
                agentQuery.in("course_class_id", ledgerClassIds);
            }
        }
        List<AgentEarningLog> allAgentLogs = agentEarningLogMapper.selectList(agentQuery);
        BigDecimal agentBase = ZERO;
        BigDecimal agentShare = ZERO;
        for (AgentEarningLog log : allAgentLogs) {
            agentBase = agentBase.add(nvl(log.getOrderAmount()));
            agentShare = agentShare.add(nvl(log.getEarningAmount()));
        }
        ledger.put("agent", ledgerItem(allAgentLogs.size(), agentBase, agentShare));

        QueryWrapper<InstructorAccountLog> instructorQuery = new QueryWrapper<InstructorAccountLog>();
        instructorQuery.eq("type", 1).ge("create_time", start).le("create_time", end);
        if (ledgerClassIds != null) {
            Set<Integer> relationIds = new HashSet<Integer>(ledgerClassIds);
            if (!ledgerClassIds.isEmpty()) {
                for (CourseLessonLog lessonLog : courseLessonLogMapper.selectList(new QueryWrapper<CourseLessonLog>()
                        .select("id").in("course_class_id", ledgerClassIds).isNull("delete_time"))) {
                    relationIds.add(lessonLog.getId());
                }
            }
            if (relationIds.isEmpty()) {
                instructorQuery.eq("relation_id", -1);
            } else {
                instructorQuery.in("relation_id", relationIds);
            }
        }
        List<InstructorAccountLog> allInstructorLogs = instructorAccountLogMapper.selectList(instructorQuery);
        BigDecimal instructorAmount = ZERO;
        for (InstructorAccountLog log : allInstructorLogs) {
            instructorAmount = instructorAmount.add(nvl(log.getAmount()));
        }
        ledger.put("instructor", ledgerItem(allInstructorLogs.size(), instructorAmount, instructorAmount));

        int includedCommunityLogs = 0;
        BigDecimal includedCommunityBase = ZERO;
        BigDecimal includedCommunityShare = ZERO;
        for (CommunityEarningLog log : allCommunityLogs) {
            if (itemClassIds.contains(log.getCourseClassId())) {
                includedCommunityLogs++;
                includedCommunityBase = includedCommunityBase.add(nvl(log.getOrderAmount()));
                includedCommunityShare = includedCommunityShare.add(nvl(log.getEarningAmount()));
            }
        }
        int includedAgentLogs = 0;
        BigDecimal includedAgentBase = ZERO;
        BigDecimal includedAgentShare = ZERO;
        for (AgentEarningLog log : allAgentLogs) {
            if (itemClassIds.contains(log.getCourseClassId())) {
                includedAgentLogs++;
                includedAgentBase = includedAgentBase.add(nvl(log.getOrderAmount()));
                includedAgentShare = includedAgentShare.add(nvl(log.getEarningAmount()));
            }
        }
        Set<Integer> instructorRelationIds = new HashSet<Integer>(itemClassIds);
        if (!itemClassIds.isEmpty()) {
            for (CourseLessonLog lessonLog : courseLessonLogMapper.selectList(new QueryWrapper<CourseLessonLog>()
                    .select("id").in("course_class_id", itemClassIds).isNull("delete_time"))) {
                instructorRelationIds.add(lessonLog.getId());
            }
        }
        int includedInstructorLogs = 0;
        BigDecimal includedInstructorAmount = ZERO;
        for (InstructorAccountLog log : allInstructorLogs) {
            if (instructorRelationIds.contains(log.getRelationId())) {
                includedInstructorLogs++;
                includedInstructorAmount = includedInstructorAmount.add(nvl(log.getAmount()));
            }
        }
        included.put("community", ledgerItem(includedCommunityLogs, includedCommunityBase, includedCommunityShare));
        included.put("agent", ledgerItem(includedAgentLogs, includedAgentBase, includedAgentShare));
        included.put("instructor", ledgerItem(includedInstructorLogs, includedInstructorAmount, includedInstructorAmount));

        excluded.put("community", ledgerItem(Math.max(0, allCommunityLogs.size() - includedCommunityLogs),
                max(ZERO, communityBase.subtract(includedCommunityBase)),
                max(ZERO, communityShare.subtract(includedCommunityShare))));
        excluded.put("agent", ledgerItem(Math.max(0, allAgentLogs.size() - includedAgentLogs),
                max(ZERO, agentBase.subtract(includedAgentBase)),
                max(ZERO, agentShare.subtract(includedAgentShare))));
        excluded.put("instructor", ledgerItem(Math.max(0, allInstructorLogs.size() - includedInstructorLogs),
                max(ZERO, instructorAmount.subtract(includedInstructorAmount)),
                max(ZERO, instructorAmount.subtract(includedInstructorAmount))));

        reconciliation.put("ledger", ledger);
        reconciliation.put("included", included);
        reconciliation.put("excluded", excluded);
        reconciliation.put("note", itemScoped
                ? "当前按班级/教练/代理筛选，ledger 仅统计筛选范围内班级的日志，excluded 为范围内无法归属的部分。"
                : "ledger 为期间内全部日志；included 为可按现存班级归属并进入明细的部分；"
                + "excluded 为引用的班级已被硬删除/无法归属的部分，未进入明细，需甲方确认是否仍需结算。"
                + (communityScope == null ? "" : "当前为社区范围，代理/教练日志按该社区现存班级限定，可能低估 excluded。"));
        return reconciliation;
    }

    private Map<String, Object> ledgerItem(int count, BigDecimal base, BigDecimal share) {
        Map<String, Object> item = new LinkedHashMap<String, Object>();
        item.put("logCount", count);
        item.put("baseAmount", round2(base == null ? ZERO : base));
        item.put("shareAmount", round2(share));
        return item;
    }

    private SettlementItem buildItem(CourseClass courseClass, ClassAgg agg, String periodStart, String periodEnd,
                                     Map<Integer, String> communityNames, Map<Integer, String> agentNames,
                                     Map<Integer, String> instructorNames, Set<Integer> existingOrderIds) {
        SettlementItem item = new SettlementItem();
        item.setItemId(SettlementIdempotency.itemId(courseClass.getId(), periodStart, periodEnd, false));
        item.setCourseClassId(courseClass.getId());
        item.setCourseClassName(safe(courseClass.getName()));
        item.setCommunityId(courseClass.getCommunityId() == null ? 0 : courseClass.getCommunityId());
        item.setCommunityName(nameOf(communityNames, item.getCommunityId()));
        item.setAgentId(courseClass.getAgentId() == null ? 0 : courseClass.getAgentId());
        item.setAgentName(nameOf(agentNames, item.getAgentId()));
        Integer instructorId = courseClass.getInstructorId() == null ? 0 : courseClass.getInstructorId();
        if (instructorId <= 0 && !agg.coachIds.isEmpty()) {
            instructorId = firstPositive(agg.coachIds);
        }
        item.setInstructorId(instructorId);
        String instructorName = nameOf(instructorNames, instructorId);
        if (StringUtils.isEmpty(instructorName) && StringUtils.isNotEmpty(courseClass.getInstructorName())) {
            instructorName = courseClass.getInstructorName();
        }
        item.setInstructorName(instructorName);
        item.setOrderCount(agg.orderCount);
        item.setPaidOrderAmount(round2(agg.paidOrderAmount));
        item.setPurchasedLessons(agg.purchasedLessons);
        BigDecimal base;
        if (agg.communityBase.compareTo(ZERO) > 0) {
            base = agg.communityBase;
            item.getSourceTypes().add("COMMUNITY_EARNING_LOG");
        } else if (agg.agentBase.compareTo(ZERO) > 0) {
            base = agg.agentBase;
            item.getSourceTypes().add("AGENT_EARNING_LOG");
        } else {
            base = ZERO;
        }
        if (agg.communityLogs > 0) {
            item.getSourceTypes().add("COMMUNITY_EARNING_LOG");
        }
        if (agg.agentLogs > 0) {
            item.getSourceTypes().add("AGENT_EARNING_LOG");
        }
        if (agg.coachLogs > 0) {
            item.getSourceTypes().add("INSTRUCTOR_ACCOUNT_LOG");
        }
        item.setSourceTypes(new ArrayList<String>(new LinkedHashSet<String>(item.getSourceTypes())));
        BigDecimal communityShare = round2(agg.communityShare);
        BigDecimal agentShare = round2(agg.agentShare);
        BigDecimal coachShare = round2(agg.coachShare);
        BigDecimal shareTotal = round2(communityShare.add(agentShare).add(coachShare));
        BigDecimal platform = round2(base.subtract(shareTotal));
        item.setSettlementBaseAmount(round2(base));
        item.setCommunityShareAmount(communityShare);
        item.setAgentShareAmount(agentShare);
        item.setInstructorShareAmount(coachShare);
        List<SettlementCoachShare> coachShares = new ArrayList<SettlementCoachShare>();
        for (Map.Entry<Integer, BigDecimal> entry : agg.coachShareByCoach.entrySet()) {
            coachShares.add(new SettlementCoachShare(entry.getKey(),
                    nameOf(instructorNames, entry.getKey()), round2(entry.getValue())));
        }
        Collections.sort(coachShares, new Comparator<SettlementCoachShare>() {
            @Override
            public int compare(SettlementCoachShare left, SettlementCoachShare right) {
                int byAmount = right.getShareAmount().compareTo(left.getShareAmount());
                return byAmount != 0 ? byAmount
                        : safeInt(left.getInstructorId()).compareTo(safeInt(right.getInstructorId()));
            }
        });
        item.setCoachShares(coachShares);
        item.setShareTotalAmount(shareTotal);
        item.setPlatformShareAmount(platform);
        item.setUnsettledOrderAmount(round2(max(ZERO, item.getPaidOrderAmount().subtract(base))));

        List<String> quality = item.getDataQuality();
        if (agg.orderCount > 0 && agg.communityLogs == 0 && agg.agentLogs == 0 && agg.coachLogs == 0) {
            quality.add("NO_LEDGER");
        }
        if (base.compareTo(ZERO) <= 0 && (shareTotal.compareTo(ZERO) > 0 || agg.orderCount > 0
                || agg.communityLogs > 0 || agg.agentLogs > 0 || agg.coachLogs > 0)) {
            quality.add("MISSING_BASE");
        }
        if (platform.compareTo(ZERO) < 0) {
            quality.add("NEGATIVE_PLATFORM");
        }
        if (agg.communityRateMismatch > 0 || agg.agentRateMismatch > 0) {
            quality.add("RATE_MISMATCH");
        }
        if (agg.orderCount > 0 && base.compareTo(item.getPaidOrderAmount().add(CENT)) > 0) {
            quality.add("BASE_EXCEEDS_ORDER");
        }
        if (agg.orderRefMissing(existingOrderIds)) {
            quality.add("ORDER_NOT_FOUND");
        }
        item.setStatus(quality.isEmpty() ? STATUS_READY : STATUS_HOLD);
        return item;
    }

    private SettlementAggregateVo newAggregate(String dimension, String key, Integer id, String name) {
        SettlementAggregateVo vo = new SettlementAggregateVo();
        vo.setDimension(dimension);
        vo.setKey(key);
        vo.setId(id);
        vo.setName(name);
        vo.setItemCount(0);
        vo.setOrderCount(0);
        vo.setReadyItemCount(0);
        vo.setHoldItemCount(0);
        vo.setPaidOrderAmount(ZERO);
        vo.setSettlementBaseAmount(ZERO);
        vo.setCommunityShareAmount(ZERO);
        vo.setAgentShareAmount(ZERO);
        vo.setInstructorShareAmount(ZERO);
        vo.setPlatformShareAmount(ZERO);
        vo.setShareTotalAmount(ZERO);
        return vo;
    }

    private SettlementSummary summarize(List<SettlementItem> items) {
        SettlementSummary summary = new SettlementSummary();
        summary.setItemCount(items == null ? 0 : items.size());
        int ready = 0;
        int hold = 0;
        for (SettlementItem item : items) {
            summary.setOrderCount(summary.getOrderCount() + nvl(item.getOrderCount()));
            summary.setPaidOrderAmount(summary.getPaidOrderAmount().add(nvl(item.getPaidOrderAmount())));
            summary.setSettlementBaseAmount(summary.getSettlementBaseAmount().add(nvl(item.getSettlementBaseAmount())));
            summary.setCommunityShareAmount(summary.getCommunityShareAmount().add(nvl(item.getCommunityShareAmount())));
            summary.setAgentShareAmount(summary.getAgentShareAmount().add(nvl(item.getAgentShareAmount())));
            summary.setInstructorShareAmount(summary.getInstructorShareAmount().add(nvl(item.getInstructorShareAmount())));
            summary.setPlatformShareAmount(summary.getPlatformShareAmount().add(nvl(item.getPlatformShareAmount())));
            summary.setShareTotalAmount(summary.getShareTotalAmount().add(nvl(item.getShareTotalAmount())));
            if (STATUS_READY.equals(item.getStatus())) {
                ready++;
            } else {
                hold++;
            }
        }
        summary.setReadyItemCount(ready);
        summary.setHoldItemCount(hold);
        summary.setBalanced(hold == 0);
        summary.setPaidOrderAmount(round2(summary.getPaidOrderAmount()));
        summary.setSettlementBaseAmount(round2(summary.getSettlementBaseAmount()));
        summary.setCommunityShareAmount(round2(summary.getCommunityShareAmount()));
        summary.setAgentShareAmount(round2(summary.getAgentShareAmount()));
        summary.setInstructorShareAmount(round2(summary.getInstructorShareAmount()));
        summary.setPlatformShareAmount(round2(summary.getPlatformShareAmount()));
        summary.setShareTotalAmount(round2(summary.getShareTotalAmount()));
        return summary;
    }

    private List<SettlementItem> filterStatus(List<SettlementItem> items, String status) {
        if (StringUtils.isEmpty(status) || "ALL".equalsIgnoreCase(status.trim())) {
            return items;
        }
        String expected = status.trim().toUpperCase();
        if (!STATUS_READY.equals(expected) && !STATUS_HOLD.equals(expected)) {
            throw new OperateException("status 仅支持 READY / HOLD");
        }
        List<SettlementItem> filtered = new ArrayList<SettlementItem>();
        for (SettlementItem item : items) {
            if (expected.equals(item.getStatus())) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    private List<CourseClass> loadClasses(FinanceSettlementSearchValidate search, Integer communityScope) {
        QueryWrapper<CourseClass> query = new QueryWrapper<CourseClass>();
        query.eq("is_delete", 0);
        if (communityScope != null && communityScope > 0) {
            query.eq("community_id", communityScope);
        } else if (search.getCommunityId() != null && search.getCommunityId() > 0) {
            query.eq("community_id", search.getCommunityId());
        }
        if (search.getCourseClassId() != null && search.getCourseClassId() > 0) {
            query.eq("id", search.getCourseClassId());
        }
        if (search.getAgentId() != null && search.getAgentId() > 0) {
            query.eq("agent_id", search.getAgentId());
        }
        query.orderByAsc("id");
        return courseClassMapper.selectList(query);
    }

    private List<RechargeOrder> loadOrders(List<Integer> classIds, Long startEpoch, Long endEpoch) {
        QueryWrapper<RechargeOrder> query = new QueryWrapper<RechargeOrder>();
        query.select("id", "sn", "user_id", "student_id", "course_class_id", "lesson_count", "order_amount",
                "pay_status", "pay_time", "refund_status", "delete_time");
        query.in("course_class_id", classIds);
        query.eq("pay_status", 1);
        query.isNull("delete_time");
        query.and(w -> w.ne("refund_status", 1).or().isNull("refund_status"));
        if (startEpoch != null) {
            query.ge("pay_time", startEpoch);
        }
        query.le("pay_time", endEpoch);
        return rechargeOrderMapper.selectList(query);
    }

    private List<CommunityEarningLog> loadCommunityLogs(List<Integer> classIds, Long startEpoch, Long endEpoch) {
        QueryWrapper<CommunityEarningLog> query = new QueryWrapper<CommunityEarningLog>();
        query.in("course_class_id", classIds);
        query.isNull("delete_time");
        if (startEpoch != null) {
            query.ge("create_time", startEpoch);
        }
        query.le("create_time", endEpoch);
        return communityEarningLogMapper.selectList(query);
    }

    private List<AgentEarningLog> loadAgentLogs(List<Integer> classIds, Long startEpoch, Long endEpoch) {
        QueryWrapper<AgentEarningLog> query = new QueryWrapper<AgentEarningLog>();
        query.in("course_class_id", classIds);
        query.isNull("delete_time");
        if (startEpoch != null) {
            query.ge("create_time", startEpoch);
        }
        query.le("create_time", endEpoch);
        return agentEarningLogMapper.selectList(query);
    }

    private List<CourseLessonLog> loadLessonLogs(List<Integer> classIds) {
        QueryWrapper<CourseLessonLog> query = new QueryWrapper<CourseLessonLog>();
        query.in("course_class_id", classIds);
        query.isNull("delete_time");
        return courseLessonLogMapper.selectList(query);
    }

    private List<InstructorAccountLog> loadInstructorLogs(List<Integer> classIds,
                                                          Map<Integer, CourseLessonLog> lessonLogMap,
                                                          Long startEpoch, Long endEpoch,
                                                          Integer instructorId) {
        QueryWrapper<InstructorAccountLog> query = new QueryWrapper<InstructorAccountLog>();
        query.eq("type", 1);
        final List<Integer> relationIds = new ArrayList<Integer>(classIds);
        relationIds.addAll(lessonLogMap.keySet());
        query.in("relation_id", relationIds);
        if (instructorId != null && instructorId > 0) {
            query.eq("instructor_id", instructorId);
        }
        if (startEpoch != null) {
            query.ge("create_time", startEpoch);
        }
        query.le("create_time", endEpoch);
        return instructorAccountLogMapper.selectList(query);
    }

    private Map<Integer, String> loadCommunityNames(Map<Integer, CourseClass> classMap) {
        Set<Integer> ids = new HashSet<Integer>();
        for (CourseClass courseClass : classMap.values()) {
            if (courseClass.getCommunityId() != null && courseClass.getCommunityId() > 0) {
                ids.add(courseClass.getCommunityId());
            }
        }
        Map<Integer, String> names = new HashMap<Integer, String>();
        if (ids.isEmpty()) {
            return names;
        }
        for (Community community : communityMapper.selectBatchIds(ids)) {
            if (community != null && community.getDeleteTime() == null) {
                names.put(community.getId(), safe(community.getName()));
            }
        }
        return names;
    }

    private Map<Integer, String> loadAgentNames(Map<Integer, CourseClass> classMap) {
        Set<Integer> ids = new HashSet<Integer>();
        for (CourseClass courseClass : classMap.values()) {
            if (courseClass.getAgentId() != null && courseClass.getAgentId() > 0) {
                ids.add(courseClass.getAgentId());
            }
        }
        Map<Integer, String> names = new HashMap<Integer, String>();
        if (ids.isEmpty()) {
            return names;
        }
        for (Agent agent : agentMapper.selectBatchIds(ids)) {
            if (agent != null && agent.getDeleteTime() == null) {
                names.put(agent.getId(), safe(agent.getName()));
            }
        }
        return names;
    }

    private Map<Integer, String> loadInstructorNames(Map<Integer, CourseClass> classMap,
                                                     Map<Integer, ClassAgg> aggregates) {
        Set<Integer> ids = new HashSet<Integer>();
        for (CourseClass courseClass : classMap.values()) {
            if (courseClass.getInstructorId() != null && courseClass.getInstructorId() > 0) {
                ids.add(courseClass.getInstructorId());
            }
        }
        for (ClassAgg agg : aggregates.values()) {
            for (Integer id : agg.coachIds) {
                if (id != null && id > 0) {
                    ids.add(id);
                }
            }
        }
        Map<Integer, String> names = new HashMap<Integer, String>();
        if (ids.isEmpty()) {
            return names;
        }
        for (Instructor instructor : instructorMapper.selectBatchIds(ids)) {
            if (instructor != null) {
                names.put(instructor.getId(), safe(instructor.getName()));
            }
        }
        return names;
    }

    private Integer resolveCommunityScope(Integer requestedCommunityId) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId == null) {
            return requestedCommunityId;
        }
        Community community = communityMapper.selectOne(new QueryWrapper<Community>()
                .eq("admin_id", adminId).isNull("delete_time").last("limit 1"));
        if (community != null) {
            return community.getId();
        }
        return requestedCommunityId;
    }

    private String communityName(Integer communityId) {
        if (communityId == null || communityId <= 0) {
            return "";
        }
        Community community = communityMapper.selectById(communityId);
        return community == null ? "" : safe(community.getName());
    }

    private Integer resolveClassId(Integer relationId, Map<Integer, CourseLessonLog> lessonLogMap,
                                   Map<Integer, CourseClass> classMap) {
        if (relationId == null) {
            return null;
        }
        CourseLessonLog lessonLog = lessonLogMap.get(relationId);
        if (lessonLog != null) {
            return lessonLog.getCourseClassId();
        }
        return classMap.containsKey(relationId) ? relationId : null;
    }

    private SettlementItem reverseItem(SettlementItem source) {
        SettlementItem item = new SettlementItem();
        item.setItemId(source.getItemId().replaceFirst("^SI-", "SIR-"));
        item.setStatus(source.getStatus());
        item.setCommunityId(source.getCommunityId());
        item.setCommunityName(source.getCommunityName());
        item.setAgentId(source.getAgentId());
        item.setAgentName(source.getAgentName());
        item.setCourseClassId(source.getCourseClassId());
        item.setCourseClassName(source.getCourseClassName());
        item.setInstructorId(source.getInstructorId());
        item.setInstructorName(source.getInstructorName());
        List<SettlementCoachShare> reversedCoachShares = new ArrayList<SettlementCoachShare>();
        for (SettlementCoachShare share : source.getCoachShares()) {
            reversedCoachShares.add(new SettlementCoachShare(share.getInstructorId(),
                    share.getInstructorName(), negate(share.getShareAmount())));
        }
        item.setCoachShares(reversedCoachShares);
        item.setOrderCount(source.getOrderCount());
        item.setPurchasedLessons(source.getPurchasedLessons());
        item.setCurrency(source.getCurrency());
        item.setSourceTypes(source.getSourceTypes());
        item.setDataQuality(source.getDataQuality());
        item.setPaidOrderAmount(negate(source.getPaidOrderAmount()));
        item.setSettlementBaseAmount(negate(source.getSettlementBaseAmount()));
        item.setCommunityShareAmount(negate(source.getCommunityShareAmount()));
        item.setAgentShareAmount(negate(source.getAgentShareAmount()));
        item.setInstructorShareAmount(negate(source.getInstructorShareAmount()));
        item.setPlatformShareAmount(negate(source.getPlatformShareAmount()));
        item.setShareTotalAmount(negate(source.getShareTotalAmount()));
        item.setUnsettledOrderAmount(negate(source.getUnsettledOrderAmount()));
        return item;
    }

    private SettlementExportVo toExportVo(SettlementItem item, String period) {
        SettlementExportVo vo = new SettlementExportVo();
        vo.setPeriod(period);
        vo.setItemId(item.getItemId());
        vo.setStatus(item.getStatus());
        vo.setCommunityName(item.getCommunityName());
        vo.setAgentName(item.getAgentName());
        vo.setCourseClassName(item.getCourseClassName());
        vo.setInstructorName(item.getInstructorName());
        vo.setOrderCount(item.getOrderCount());
        vo.setPaidOrderAmount(item.getPaidOrderAmount());
        vo.setPurchasedLessons(item.getPurchasedLessons());
        vo.setSettlementBaseAmount(item.getSettlementBaseAmount());
        vo.setCommunityShareAmount(item.getCommunityShareAmount());
        vo.setAgentShareAmount(item.getAgentShareAmount());
        vo.setInstructorShareAmount(item.getInstructorShareAmount());
        vo.setPlatformShareAmount(item.getPlatformShareAmount());
        vo.setShareTotalAmount(item.getShareTotalAmount());
        vo.setUnsettledOrderAmount(item.getUnsettledOrderAmount());
        StringBuilder coachDetail = new StringBuilder();
        if (item.getCoachShares() != null) {
            for (SettlementCoachShare share : item.getCoachShares()) {
                if (coachDetail.length() > 0) {
                    coachDetail.append("; ");
                }
                coachDetail.append(share.getInstructorId()).append('-')
                        .append(share.getInstructorName()).append(':')
                        .append(share.getShareAmount());
            }
        }
        vo.setCoachShareDetail(coachDetail.toString());
        vo.setDataQuality(item.getDataQuality() == null ? "" : String.join(",", item.getDataQuality()));
        vo.setSourceTypes(item.getSourceTypes() == null ? "" : String.join(",", item.getSourceTypes()));
        return vo;
    }

    private String requireDate(String value, String field) {
        if (StringUtils.isEmpty(value)) {
            throw new OperateException("缺少 " + field + "（yyyy-MM-dd）");
        }
        try {
            return LocalDate.parse(value.trim()).toString();
        } catch (DateTimeParseException e) {
            throw new OperateException(field + " 格式错误，需为 yyyy-MM-dd");
        }
    }

    private LocalDate parseOptionalDate(String value, String field) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new OperateException(field + " 格式错误，需为 yyyy-MM-dd");
        }
    }

    private long startOfDay(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    private long endOfDay(LocalDate date) {
        return date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() - 1L;
    }

    private LocalDate timestampToDate(Long timestamp) {
        return java.time.Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Long min(Long current, Long value) {
        if (value == null || value <= 0) {
            return current;
        }
        return current == null || value < current ? value : current;
    }

    private boolean rateMismatch(BigDecimal base, BigDecimal rate, BigDecimal share) {
        if (base == null || rate == null || share == null) {
            return false;
        }
        BigDecimal expected = base.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        return expected.subtract(share).abs().compareTo(CENT) > 0;
    }

    private BigDecimal round2(BigDecimal value) {
        return (value == null ? ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal negate(BigDecimal value) {
        return round2(value == null ? ZERO : value.negate());
    }

    private BigDecimal max(BigDecimal left, BigDecimal right) {
        return left.compareTo(right) >= 0 ? left : right;
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? ZERO : value;
    }

    private int nvl(Integer value) {
        return value == null ? 0 : value;
    }

    private String nameOf(Map<Integer, String> names, Integer id) {
        if (id == null || id <= 0) {
            return "";
        }
        String name = names.get(id);
        return name == null ? "" : name;
    }

    private Integer firstPositive(Set<Integer> values) {
        for (Integer value : values) {
            if (value != null && value > 0) {
                return value;
            }
        }
        return 0;
    }

    private Integer safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String sanitizeFileName(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        return value.trim().replaceAll("[^A-Za-z0-9\\u4e00-\\u9fa5_-]", "_");
    }

    private String normalizeDimension(String dimension) {
        if (StringUtils.isEmpty(dimension)) {
            return "course";
        }
        String normalized = dimension.trim().toLowerCase();
        if ("community".equals(normalized) || "coach".equals(normalized) || "course".equals(normalized)) {
            return normalized;
        }
        throw new OperateException("dimension 仅支持 community / course / coach");
    }

    /** 收益明细则导出时隐藏的二次分配/对账列（字段定义保留，见 SettlementExportVo 契约说明）。 */
    private static final java.util.Set<String> REVENUE_DETAIL_HIDDEN_COLUMNS =
            new java.util.HashSet<String>(java.util.Arrays.asList(
                    "itemId", "status", "agentName", "settlementBaseAmount", "communityShareAmount",
                    "agentShareAmount", "instructorShareAmount", "platformShareAmount", "shareTotalAmount",
                    "unsettledOrderAmount", "coachShareDetail", "dataQuality", "sourceTypes"));

    /** #102：教练「分成」维度下线后的提示（教练月营业额视图接管教练维度）。 */
    private static final String COACH_DIMENSION_NOTE =
            "教练分成维度已下线（#102）：不再计算「分到多少钱」。教练维度请用「教练月营业额」视图看上课数/服务人次/总营业额。";

    private static final String CALIBER_NOTE =
            "收益明细口径：订单取 la_recharge_order 中 course_class_id>0、pay_status=1、未删除、未退款，按 pay_time 归期；"
            + "金额只展示实收营业额（order_amount 合计）与购买课时，供线下核算。"
            + "#102 起本页不再计算社区/代理/教练分成、平台留存与任何比例测算，"
            + "分账上报契约 v1.0 由 /finance.settlement/push 按合同单独执行，不受本页展示口径影响。";

    /** #102：页面/接口不再返回二次分配与台账对账数据（字段名保留、值置空）。 */
    private static final String RECONCILIATION_HIDDEN_NOTE =
            "「分账数据」页已改为只读「收益明细」（#102）：社区/代理/教练台账对账数据不再计算返回；"
            + "上报批次契约见 /finance.settlement/push。";

    private void hideAllocation(SettlementItem item) {
        if (item == null) {
            return;
        }
        item.setSettlementBaseAmount(null);
        item.setCommunityShareAmount(null);
        item.setAgentShareAmount(null);
        item.setInstructorShareAmount(null);
        item.setPlatformShareAmount(null);
        item.setShareTotalAmount(null);
        item.setUnsettledOrderAmount(null);
        item.setCoachShares(null);
        item.setDataQuality(null);
        item.setSourceTypes(null);
    }

    private void hideAllocation(SettlementSummary summary) {
        if (summary == null) {
            return;
        }
        summary.setSettlementBaseAmount(null);
        summary.setCommunityShareAmount(null);
        summary.setAgentShareAmount(null);
        summary.setInstructorShareAmount(null);
        summary.setPlatformShareAmount(null);
        summary.setShareTotalAmount(null);
    }

    private void hideAllocation(SettlementAggregateVo aggregate) {
        if (aggregate == null) {
            return;
        }
        aggregate.setSettlementBaseAmount(null);
        aggregate.setCommunityShareAmount(null);
        aggregate.setAgentShareAmount(null);
        aggregate.setInstructorShareAmount(null);
        aggregate.setPlatformShareAmount(null);
        aggregate.setShareTotalAmount(null);
    }

    private Map<String, Object> hiddenReconciliation() {
        Map<String, Object> reconciliation = new LinkedHashMap<String, Object>();
        reconciliation.put("hidden", Boolean.TRUE);
        reconciliation.put("note", RECONCILIATION_HIDDEN_NOTE);
        return reconciliation;
    }

    private static class ClassAgg {
        int orderCount;
        int purchasedLessons;
        BigDecimal paidOrderAmount = ZERO;
        int communityLogs;
        BigDecimal communityBase = ZERO;
        BigDecimal communityShare = ZERO;
        int communityRateMismatch;
        int agentLogs;
        BigDecimal agentBase = ZERO;
        BigDecimal agentShare = ZERO;
        int agentRateMismatch;
        int coachLogs;
        BigDecimal coachShare = ZERO;
        Map<Integer, BigDecimal> coachShareByCoach = new LinkedHashMap<Integer, BigDecimal>();
        Set<Integer> coachIds = new TreeSet<Integer>();
        Set<Integer> orderIds = new HashSet<Integer>();

        boolean hasActivity() {
            return orderCount > 0 || communityLogs > 0 || agentLogs > 0 || coachLogs > 0;
        }

        boolean orderRefMissing(Set<Integer> existingOrderIds) {
            for (Integer orderId : orderIds) {
                if (!existingOrderIds.contains(orderId)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class SettlementQuery {
        String periodStart = DEFAULT_ALL_START;
        String periodEnd = "";
        boolean communityScoped;
        List<SettlementItem> items = Collections.emptyList();
        Map<String, Object> reconciliation = Collections.emptyMap();
    }
}
