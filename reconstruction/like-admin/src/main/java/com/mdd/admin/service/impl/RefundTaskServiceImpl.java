package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IRefundTaskService;
import com.mdd.admin.service.refund.RefundExecutionResult;
import com.mdd.admin.service.refund.RefundTaskCreatedEvent;
import com.mdd.admin.service.refund.RefundTaskExecutor;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.refund.RefundTaskSearchValidate;
import com.mdd.admin.vo.refund.RefundTaskListVo;
import com.mdd.admin.vo.refund.RefundTaskLogVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.refund.RefundTask;
import com.mdd.common.entity.refund.RefundTaskLog;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.refund.RefundTaskLogMapper;
import com.mdd.common.mapper.refund.RefundTaskMapper;
import com.mdd.common.util.AmountUtil;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TextUtils;
import com.mdd.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefundTaskServiceImpl implements IRefundTaskService {
    private static final Logger log = LoggerFactory.getLogger(RefundTaskServiceImpl.class);

    @Resource
    RefundTaskMapper refundTaskMapper;
    @Resource
    RefundTaskLogMapper refundTaskLogMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    RefundTaskExecutor refundTaskExecutor;
    @Resource
    ApplicationEventPublisher eventPublisher;
    @Resource
    PlatformTransactionManager transactionManager;

    @Value("${like.refund.max-retry:8}")
    private int maxRetry;

    @Value("${like.refund.auto-retry-delay-seconds:60}")
    private int autoRetryDelaySeconds;

    @Value("${like.refund.stale-processing-seconds:300}")
    private int staleProcessingSeconds;

    private TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager);
    }

    @Override
    public RefundTask createForRefund(CourseRefund refund, RechargeOrder order) {
        if (order == null || StringUtils.isEmpty(order.getTransactionId())) {
            // 调用方只会在微信支付且交易号存在时创建任务；这里防御性兜底，绝不落“假成功”任务。
            return null;
        }
        RefundTask exists = refundTaskMapper.selectOne(new QueryWrapper<RefundTask>()
                .eq("refund_id", refund.getId()).last("limit 1"));
        if (exists != null) {
            return exists;
        }
        long now = TimeUtils.timestamp();
        RefundTask task = new RefundTask();
        task.setTaskNo("RT" + refund.getId());
        task.setRefundId(refund.getId());
        task.setOrderId(order.getId());
        task.setUserId(refund.getUserId());
        task.setCourseClassId(refund.getCourseClassId());
        task.setPayWay(order.getPayWay() != null ? order.getPayWay() : 0);
        task.setTransactionId(order.getTransactionId());
        task.setOutTradeNo(order.getSn() != null ? order.getSn() : "");
        task.setOutRefundNo("RF" + refund.getId());
        task.setTotalFee(order.getOrderAmount() != null
                ? AmountUtil.yuan2Fen(order.getOrderAmount().toPlainString()) : 0);
        task.setRefundFee(refund.getRefundAmount() != null
                ? AmountUtil.yuan2Fen(refund.getRefundAmount().toPlainString()) : 0);
        task.setStatus(RefundTask.STATUS_PENDING);
        task.setRetryCount(0);
        task.setMaxRetry(maxRetry);
        task.setNextRetryTime(0L);
        task.setLastError("");
        task.setResultMsg("");
        task.setExecuteTime(0L);
        task.setFinishTime(0L);
        task.setCreateTime(now);
        task.setUpdateTime(now);
        try {
            refundTaskMapper.insert(task);
        } catch (DuplicateKeyException e) {
            // 唯一键兜底：并发/重复审核时复用已有任务，绝不重复退款。
            return refundTaskMapper.selectOne(new QueryWrapper<RefundTask>()
                    .eq("refund_id", refund.getId()).last("limit 1"));
        }
        insertLog(task.getId(), 0, "create", 1, task.getOutRefundNo(),
                "微信退款任务已创建，等待异步执行", 0, now);
        eventPublisher.publishEvent(new RefundTaskCreatedEvent(task.getId()));
        return task;
    }

    @Override
    public PageResult<RefundTaskListVo> list(PageValidate pageValidate, RefundTaskSearchValidate searchValidate) {
        int pageNo = pageValidate.getPage_no() == null ? 1 : pageValidate.getPage_no();
        int pageSize = pageValidate.getPage_size() == null ? 20 : pageValidate.getPage_size();
        QueryWrapper<RefundTask> queryWrapper = new QueryWrapper<>();
        if (searchValidate != null) {
            if (searchValidate.getRefundId() != null && searchValidate.getRefundId() > 0) {
                queryWrapper.eq("refund_id", searchValidate.getRefundId());
            }
            if (searchValidate.getStatus() != null && searchValidate.getStatus() >= 0) {
                queryWrapper.eq("status", searchValidate.getStatus());
            }
            if (StringUtils.isNotEmpty(searchValidate.getTaskNo())) {
                queryWrapper.like("task_no", searchValidate.getTaskNo());
            }
        }
        queryWrapper.orderByDesc("id");
        IPage<RefundTask> iPage = refundTaskMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        List<RefundTaskListVo> list = new ArrayList<>();
        for (RefundTask item : iPage.getRecords()) {
            list.add(toListVo(item));
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list, buildStat());
    }

    @Override
    public List<RefundTaskLogVo> logs(Integer taskId) {
        List<RefundTaskLog> logs = refundTaskLogMapper.selectList(new QueryWrapper<RefundTaskLog>()
                .eq("task_id", taskId).orderByAsc("id"));
        List<RefundTaskLogVo> list = new ArrayList<>();
        for (RefundTaskLog item : logs) {
            RefundTaskLogVo vo = new RefundTaskLogVo();
            vo.setId(item.getId());
            vo.setTaskId(item.getTaskId());
            vo.setAttemptNo(item.getAttemptNo());
            vo.setAction(item.getAction());
            vo.setActionDesc(actionDesc(item.getAction()));
            vo.setStatus(item.getStatus());
            vo.setStatusDesc(logStatusDesc(item.getStatus()));
            vo.setOutRefundNo(item.getOutRefundNo());
            vo.setMessage(item.getMessage());
            vo.setCostMs(item.getCostMs());
            vo.setCreateTime(TimeUtils.timestampToDate(item.getCreateTime()));
            list.add(vo);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void retry(Integer refundId, Integer adminId) {
        RefundTask task = refundTaskMapper.selectOne(new QueryWrapper<RefundTask>()
                .eq("refund_id", refundId).orderByDesc("id").last("limit 1"));
        if (task == null) {
            throw new OperateException("退款任务不存在，请先审核通过后再试");
        }
        if (RefundTask.STATUS_SUCCESS == nullSafe(task.getStatus())) {
            throw new OperateException("退款任务已成功，无需重试");
        }
        if (RefundTask.STATUS_PENDING == nullSafe(task.getStatus())) {
            throw new OperateException("退款任务排队中，请稍后刷新查看");
        }
        long now = TimeUtils.timestamp();
        if (RefundTask.STATUS_RUNNING == nullSafe(task.getStatus())) {
            long executeTime = task.getExecuteTime() == null ? 0L : task.getExecuteTime();
            if (executeTime <= 0 || executeTime > now - staleProcessingSeconds) {
                throw new OperateException("退款任务执行中，请稍后刷新查看");
            }
        }
        UpdateWrapper<RefundTask> updateWrapper = new UpdateWrapper<RefundTask>()
                .eq("id", task.getId())
                .in("status", RefundTask.STATUS_FAILED, RefundTask.STATUS_RUNNING)
                .set("status", RefundTask.STATUS_PENDING)
                .set("next_retry_time", 0)
                .set("update_time", now);
        if (refundTaskMapper.update(null, updateWrapper) <= 0) {
            throw new OperateException("退款任务状态已变化，请刷新后重试");
        }
        int attemptNo = nullSafe(task.getRetryCount()) + 1;
        insertLog(task.getId(), attemptNo, "retry", 1, task.getOutRefundNo(),
                "管理员 " + (adminId == null ? "0" : adminId) + " 手动重试", 0, now);
        eventPublisher.publishEvent(new RefundTaskCreatedEvent(task.getId()));
    }

    @Override
    public void executeTask(Integer taskId) {
        long start = System.currentTimeMillis();
        ClaimResult claim = claimTask(taskId);
        if (claim == null) {
            return;
        }
        RefundTask task = claim.task;
        try {
            RefundExecutionResult result = refundTaskExecutor.execute(task);
            transactionTemplate().executeWithoutResult(status ->
                    finishSuccess(task, claim.logId, result, System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.warn("退款任务执行失败 taskId={} outRefundNo={}", task.getId(), task.getOutRefundNo(), e);
            transactionTemplate().executeWithoutResult(status ->
                    finishFailure(task, claim.logId, e, System.currentTimeMillis() - start));
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int requeueStale(int batchSize) {
        long now = TimeUtils.timestamp();
        long before = now - staleProcessingSeconds;
        List<RefundTask> staleTasks = refundTaskMapper.selectList(new QueryWrapper<RefundTask>()
                .eq("status", RefundTask.STATUS_RUNNING)
                .gt("execute_time", 0)
                .le("execute_time", before)
                .orderByAsc("id")
                .last("limit " + safeBatch(batchSize)));
        int count = 0;
        for (RefundTask task : staleTasks) {
            UpdateWrapper<RefundTask> updateWrapper = new UpdateWrapper<RefundTask>()
                    .eq("id", task.getId())
                    .eq("status", RefundTask.STATUS_RUNNING)
                    .eq("execute_time", task.getExecuteTime())
                    .set("status", RefundTask.STATUS_PENDING)
                    .set("next_retry_time", 0)
                    .set("update_time", now);
            if (refundTaskMapper.update(null, updateWrapper) > 0) {
                int attemptNo = nullSafe(task.getRetryCount()) + 1;
                insertLog(task.getId(), attemptNo, "requeue", 1, task.getOutRefundNo(),
                        "执行超时或进程中断，重新排队", 0, now);
                count++;
            }
        }
        return count;
    }

    @Override
    public List<Integer> listDueTaskIds(int batchSize) {
        long now = TimeUtils.timestamp();
        QueryWrapper<RefundTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        queryWrapper.and(wrapper -> wrapper
                .eq("status", RefundTask.STATUS_PENDING)
                .or(failed -> failed.eq("status", RefundTask.STATUS_FAILED)
                        .gt("next_retry_time", 0)
                        .le("next_retry_time", now)));
        queryWrapper.orderByAsc("id").last("limit " + safeBatch(batchSize));
        List<Integer> ids = new ArrayList<>();
        for (RefundTask task : refundTaskMapper.selectList(queryWrapper)) {
            ids.add(task.getId());
        }
        return ids;
    }

    private ClaimResult claimTask(Integer taskId) {
        return transactionTemplate().execute(status -> {
            long now = TimeUtils.timestamp();
            UpdateWrapper<RefundTask> updateWrapper = new UpdateWrapper<RefundTask>()
                    .eq("id", taskId)
                    .in("status", RefundTask.STATUS_PENDING, RefundTask.STATUS_FAILED)
                    .set("status", RefundTask.STATUS_RUNNING)
                    .set("execute_time", now)
                    .set("update_time", now);
            if (refundTaskMapper.update(null, updateWrapper) <= 0) {
                return null;
            }
            RefundTask task = refundTaskMapper.selectById(taskId);
            if (task == null) {
                return null;
            }
            int attemptNo = nullSafe(task.getRetryCount()) + 1;
            RefundTaskLog taskLog = insertLog(task.getId(), attemptNo, "execute", 0,
                    task.getOutRefundNo(), "开始执行微信退款", 0, now);
            return new ClaimResult(task, taskLog.getId());
        });
    }

    private void finishSuccess(RefundTask task, Integer logId, RefundExecutionResult result, long costMs) {
        long now = TimeUtils.timestamp();
        String message = result == null || StringUtils.isEmpty(result.getMessage())
                ? "微信退款受理成功" : result.getMessage();
        UpdateWrapper<RefundTask> taskUpdate = new UpdateWrapper<RefundTask>()
                .eq("id", task.getId())
                .eq("status", RefundTask.STATUS_RUNNING)
                .set("status", RefundTask.STATUS_SUCCESS)
                .set("result_msg", TextUtils.truncate(message, 255))
                .set("last_error", "")
                .set("next_retry_time", 0)
                .set("finish_time", now)
                .set("update_time", now);
        refundTaskMapper.update(null, taskUpdate);
        if (task.getOrderId() != null && task.getOrderId() > 0) {
            UpdateWrapper<RechargeOrder> orderUpdate = new UpdateWrapper<RechargeOrder>()
                    .eq("id", task.getOrderId())
                    .set("refund_status", 1)
                    .set("update_time", now);
            if (result != null && StringUtils.isNotEmpty(result.getRefundId())) {
                orderUpdate.set("refund_transaction_id", TextUtils.truncate(result.getRefundId(), 255));
            }
            rechargeOrderMapper.update(null, orderUpdate);
        }
        updateLog(logId, 1, message, costMs, now);
    }

    private void finishFailure(RefundTask task, Integer logId, Exception e, long costMs) {
        long now = TimeUtils.timestamp();
        int retryCount = nullSafe(task.getRetryCount()) + 1;
        int taskMaxRetry = task.getMaxRetry() == null || task.getMaxRetry() <= 0 ? maxRetry : task.getMaxRetry();
        boolean exhausted = retryCount >= taskMaxRetry;
        long nextRetryTime = exhausted ? 0L : now + (long) autoRetryDelaySeconds * retryCount;
        String message = TextUtils.truncate(errorMessage(e), 500);
        UpdateWrapper<RefundTask> taskUpdate = new UpdateWrapper<RefundTask>()
                .eq("id", task.getId())
                .eq("status", RefundTask.STATUS_RUNNING)
                .set("status", RefundTask.STATUS_FAILED)
                .set("retry_count", retryCount)
                .set("next_retry_time", nextRetryTime)
                .set("last_error", message)
                .set("finish_time", exhausted ? now : 0L)
                .set("update_time", now);
        refundTaskMapper.update(null, taskUpdate);
        updateLog(logId, 2, message, costMs, now);
    }

    private RefundTaskLog insertLog(Integer taskId, Integer attemptNo, String action, Integer status,
                                    String outRefundNo, String message, Integer costMs, long now) {
        RefundTaskLog taskLog = new RefundTaskLog();
        taskLog.setTaskId(taskId);
        taskLog.setAttemptNo(attemptNo == null ? 0 : attemptNo);
        taskLog.setAction(action);
        taskLog.setStatus(status == null ? 0 : status);
        taskLog.setOutRefundNo(outRefundNo == null ? "" : outRefundNo);
        taskLog.setMessage(TextUtils.truncate(message, 1000));
        taskLog.setCostMs(costMs == null ? 0 : costMs);
        taskLog.setCreateTime(now);
        refundTaskLogMapper.insert(taskLog);
        return taskLog;
    }

    private void updateLog(Integer logId, Integer status, String message, long costMs, long now) {
        if (logId == null) {
            return;
        }
        UpdateWrapper<RefundTaskLog> updateWrapper = new UpdateWrapper<RefundTaskLog>()
                .eq("id", logId)
                .set("status", status)
                .set("message", TextUtils.truncate(message, 1000))
                .set("cost_ms", costMs);
        refundTaskLogMapper.update(null, updateWrapper);
    }

    private RefundTaskListVo toListVo(RefundTask item) {
        RefundTaskListVo vo = new RefundTaskListVo();
        vo.setId(item.getId());
        vo.setTaskNo(item.getTaskNo());
        vo.setRefundId(item.getRefundId());
        vo.setOrderId(item.getOrderId());
        vo.setUserId(item.getUserId());
        vo.setPayWay(item.getPayWay());
        vo.setOutTradeNo(item.getOutTradeNo());
        vo.setOutRefundNo(item.getOutRefundNo());
        vo.setTransactionId(item.getTransactionId());
        vo.setTotalFee(fen2YuanText(item.getTotalFee()));
        vo.setRefundFee(fen2YuanText(item.getRefundFee()));
        vo.setStatus(item.getStatus());
        vo.setStatusDesc(taskStatusDesc(item.getStatus()));
        vo.setRetryCount(item.getRetryCount());
        vo.setMaxRetry(item.getMaxRetry());
        vo.setNextRetryTime(item.getNextRetryTime() != null && item.getNextRetryTime() > 0
                ? TimeUtils.timestampToDate(item.getNextRetryTime()) : "");
        vo.setLastError(item.getLastError());
        vo.setResultMsg(item.getResultMsg());
        vo.setExecuteTime(item.getExecuteTime() != null && item.getExecuteTime() > 0
                ? TimeUtils.timestampToDate(item.getExecuteTime()) : "");
        vo.setFinishTime(item.getFinishTime() != null && item.getFinishTime() > 0
                ? TimeUtils.timestampToDate(item.getFinishTime()) : "");
        vo.setCreateTime(TimeUtils.timestampToDate(item.getCreateTime()));
        return vo;
    }

    private Map<String, Object> buildStat() {
        Map<String, Object> extend = new LinkedHashMap<>();
        extend.put("total", refundTaskMapper.selectCount(null));
        extend.put("pending", refundTaskMapper.selectCount(new QueryWrapper<RefundTask>().eq("status", RefundTask.STATUS_PENDING)));
        extend.put("processing", refundTaskMapper.selectCount(new QueryWrapper<RefundTask>().eq("status", RefundTask.STATUS_RUNNING)));
        extend.put("success", refundTaskMapper.selectCount(new QueryWrapper<RefundTask>().eq("status", RefundTask.STATUS_SUCCESS)));
        extend.put("failed", refundTaskMapper.selectCount(new QueryWrapper<RefundTask>().eq("status", RefundTask.STATUS_FAILED)));
        extend.put("successRefundFee", fen2YuanText(sumFee(RefundTask.STATUS_SUCCESS)));
        extend.put("failedRefundFee", fen2YuanText(sumFee(RefundTask.STATUS_FAILED)));
        return extend;
    }

    private Integer sumFee(int status) {
        BigDecimal total = refundTaskMapper.sum("refund_fee", new QueryWrapper<RefundTask>().eq("status", status));
        return total == null ? 0 : total.intValue();
    }

    private String fen2YuanText(Integer fen) {
        if (fen == null) {
            return "0.00";
        }
        return BigDecimal.valueOf(fen).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).toPlainString();
    }

    private String taskStatusDesc(Integer status) {
        switch (nullSafe(status)) {
            case RefundTask.STATUS_PENDING:
                return "待执行";
            case RefundTask.STATUS_RUNNING:
                return "执行中";
            case RefundTask.STATUS_SUCCESS:
                return "成功";
            case RefundTask.STATUS_FAILED:
                return "失败";
            default:
                return "未知";
        }
    }

    private String logStatusDesc(Integer status) {
        switch (nullSafe(status)) {
            case RefundTaskLog.STATUS_PROCESSING:
                return "处理中";
            case RefundTaskLog.STATUS_SUCCESS:
                return "成功";
            case RefundTaskLog.STATUS_FAILED:
                return "失败";
            default:
                return "未知";
        }
    }

    private String actionDesc(String action) {
        if ("create".equals(action)) {
            return "创建任务";
        }
        if ("execute".equals(action)) {
            return "执行退款";
        }
        if ("retry".equals(action)) {
            return "手动重试";
        }
        if ("requeue".equals(action)) {
            return "超时重排队";
        }
        return action;
    }

    private int nullSafe(Integer value) {
        return value == null ? -1 : value;
    }

    private int safeBatch(int batchSize) {
        if (batchSize <= 0) {
            return 20;
        }
        return Math.min(batchSize, 200);
    }

    private String errorMessage(Exception e) {
        String message = e == null ? null : e.getMessage();
        if (StringUtils.isEmpty(message) && e != null) {
            message = e.getClass().getSimpleName();
        }
        return StringUtils.isEmpty(message) ? "退款执行失败" : message;
    }

    private static class ClaimResult {
        private final RefundTask task;
        private final Integer logId;

        private ClaimResult(RefundTask task, Integer logId) {
            this.task = task;
            this.logId = logId;
        }
    }
}
