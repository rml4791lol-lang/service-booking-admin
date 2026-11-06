/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.instructor.IInstructorWithdrawalService
 *  com.mdd.admin.service.instructor.impl.InstructorWithdrawalServiceImpl
 *  com.mdd.admin.validate.instructor.InstructorWithdrawalAuditValidate
 *  com.mdd.admin.validate.instructor.InstructorWithdrawalSearchValidate
 *  com.mdd.admin.vo.instructor.InstructorWithdrawalListedVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.Message
 *  com.mdd.common.entity.instructor.InstructorAccountLog
 *  com.mdd.common.entity.instructor.InstructorWithdrawal
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.MessageMapper
 *  com.mdd.common.mapper.instructor.InstructorAccountLogMapper
 *  com.mdd.common.mapper.instructor.InstructorWithdrawalMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.instructor.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.instructor.IInstructorWithdrawalService;
import com.mdd.admin.validate.instructor.InstructorWithdrawalAuditValidate;
import com.mdd.admin.validate.instructor.InstructorWithdrawalSearchValidate;
import com.mdd.admin.vo.instructor.InstructorWithdrawalListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.Message;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.instructor.InstructorAccountLog;
import com.mdd.common.entity.instructor.InstructorWithdrawal;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.MessageMapper;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.instructor.InstructorAccountLogMapper;
import com.mdd.common.mapper.instructor.InstructorWithdrawalMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstructorWithdrawalServiceImpl
implements IInstructorWithdrawalService {
    private static final Logger log = LoggerFactory.getLogger(InstructorWithdrawalServiceImpl.class);

    @Resource
    private InstructorWithdrawalMapper instructorWithdrawalMapper;
    @Resource
    private InstructorMapper instructorMapper;
    @Resource
    private InstructorAccountLogMapper instructorAccountLogMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private AdminMapper adminMapper;

    public PageResult<InstructorWithdrawalListedVo> list(InstructorWithdrawalSearchValidate searchValidate, Integer pageNo, Integer pageSize) {
        QueryWrapper<InstructorWithdrawal> queryWrapper = new QueryWrapper<InstructorWithdrawal>();
        if (StringUtils.isNotEmpty((String)searchValidate.getSn())) {
            queryWrapper.eq("sn", (Object)searchValidate.getSn());
        }
        if (searchValidate.getStatus() != null) {
            queryWrapper.eq("status", (Object)searchValidate.getStatus());
        }
        if (StringUtils.isNotEmpty((String)searchValidate.getStartTime()) && StringUtils.isNotEmpty((String)searchValidate.getEndTime())) {
            queryWrapper.between("create_time", (Object)TimeUtils.dateToTimestamp((String)searchValidate.getStartTime()), (Object)TimeUtils.dateToTimestamp((String)searchValidate.getEndTime()));
        }
        if (StringUtils.isNotEmpty((String)searchValidate.getInstructorName())) {
            QueryWrapper<Instructor> iqw = new QueryWrapper<Instructor>();
            iqw.like("name", (Object)searchValidate.getInstructorName());
            iqw.select(new String[]{"id"});
            List<Instructor> instructors = this.instructorMapper.selectList((Wrapper)iqw);
            if (instructors.isEmpty()) {
                return PageResult.iPageHandle((Long)0L, (Long)1L, (Long)20L, new LinkedList());
            }
            LinkedList<Integer> instructorIds = new LinkedList<Integer>();
            for (Instructor instructor : instructors) {
                instructorIds.add(instructor.getId());
            }
            queryWrapper.in("instructor_id", instructorIds);
        }
        queryWrapper.orderByDesc("id");
        // 分页参数来自 @RequestParam，未走 PageValidate（page_size 1~60）：
        // 负数/0 会拼出非法 LIMIT 报 500，超大 pageSize 会一次拉全表（#58）。这里统一收敛。
        int safePageNo = pageNo == null || pageNo < 1 ? 1 : pageNo;
        int safePageSize = pageSize == null ? 20 : pageSize;
        if (safePageSize < 1) {
            safePageSize = 20;
        }
        if (safePageSize > 60) {
            safePageSize = 60;
        }
        IPage<InstructorWithdrawal> iPage = this.instructorWithdrawalMapper.selectPage((IPage)new Page((long)safePageNo, (long)safePageSize), (Wrapper)queryWrapper);
        // #66：本页教练一次性批量查询，避免逐行 selectOne（N+1）。
        Set<Integer> instructorIds = new HashSet<Integer>();
        for (InstructorWithdrawal item : iPage.getRecords()) {
            if (item.getInstructorId() != null) {
                instructorIds.add(item.getInstructorId());
            }
        }
        Map<Integer, Instructor> instructorMap = new HashMap<Integer, Instructor>();
        if (!instructorIds.isEmpty()) {
            for (Instructor instructor : this.instructorMapper.selectBatchIds(instructorIds)) {
                instructorMap.put(instructor.getId().intValue(), instructor);
            }
        }
        LinkedList<InstructorWithdrawalListedVo> list = new LinkedList<InstructorWithdrawalListedVo>();
        for (InstructorWithdrawal item : iPage.getRecords()) {
            InstructorWithdrawalListedVo vo = new InstructorWithdrawalListedVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            Instructor instructor = instructorMap.get(item.getInstructorId());
            if (instructor != null) {
                vo.setInstructorName(instructor.getName());
                vo.setInstructorAvatar(UrlUtils.toAdminAbsoluteUrl((String)instructor.getAvatar()));
                vo.setInstructorPhone(instructor.getPhone());
            }
            vo.setAuditTime(TimeUtils.timestampToDate((Long)item.getAuditTime()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)item.getCreateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public Map<String, Object> stats() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        List<Long> month = TimeUtils.month();
        long monthStart = (Long)month.get(0);
        long monthEnd = (Long)month.get(1);
        long monthCount = this.instructorWithdrawalMapper.selectCount(new QueryWrapper<InstructorWithdrawal>().eq("status", (Object)1).ge("create_time", (Object)monthStart).le("create_time", (Object)monthEnd));
        result.put("monthCount", monthCount);
        QueryWrapper<InstructorWithdrawal> qwMonthAmt = new QueryWrapper<InstructorWithdrawal>();
        qwMonthAmt.select(new String[]{"IFNULL(sum(amount), 0) as amount"}).eq("status", (Object)1).ge("create_time", (Object)monthStart).le("create_time", (Object)monthEnd);
        try {
            InstructorWithdrawal sum1 = (InstructorWithdrawal)this.instructorWithdrawalMapper.selectOne((Wrapper)qwMonthAmt);
            result.put("monthAmount", sum1 != null && sum1.getAmount() != null ? sum1.getAmount() : BigDecimal.ZERO);
        }
        catch (Exception e) {
            result.put("monthAmount", BigDecimal.ZERO);
        }
        long pendingCount = this.instructorWithdrawalMapper.selectCount(new QueryWrapper<InstructorWithdrawal>().eq("status", (Object)0));
        result.put("pendingCount", pendingCount);
        QueryWrapper<InstructorWithdrawal> qwTotalAmt = new QueryWrapper<InstructorWithdrawal>();
        qwTotalAmt.select(new String[]{"IFNULL(sum(amount), 0) as amount"}).eq("status", (Object)1);
        try {
            InstructorWithdrawal sum2 = (InstructorWithdrawal)this.instructorWithdrawalMapper.selectOne((Wrapper)qwTotalAmt);
            result.put("totalAmount", sum2 != null && sum2.getAmount() != null ? sum2.getAmount() : BigDecimal.ZERO);
        }
        catch (Exception e) {
            result.put("totalAmount", BigDecimal.ZERO);
        }
        return result;
    }

    @Transactional(rollbackFor={Exception.class})
    public void audit(InstructorWithdrawalAuditValidate auditValidate) {
        InstructorWithdrawal withdrawal = (InstructorWithdrawal)this.instructorWithdrawalMapper.selectById((Serializable)auditValidate.getId());
        if (withdrawal == null) {
            throw new OperateException("\u63d0\u73b0\u8bb0\u5f55\u4e0d\u5b58\u5728!");
        }
        if (!Integer.valueOf(0).equals(withdrawal.getStatus())) {
            throw new OperateException("\u8be5\u8bb0\u5f55\u5df2\u88ab\u5904\u7406\uff0c\u8bf7\u5237\u65b0\u9875\u9762!");
        }
        Integer targetStatus = auditValidate.getStatus();
        if (targetStatus == null || targetStatus != 1 && targetStatus != 2) {
            throw new OperateException("\u5ba1\u6838\u72b6\u6001\u53d6\u503c\u4e0d\u5408\u6cd5(1=\u901a\u8fc7,2=\u62d2\u7edd)!");
        }
        String auditRemark = auditValidate.getAuditRemark() == null ? "" : auditValidate.getAuditRemark().trim();
        if (targetStatus == 2 && auditRemark.isEmpty()) {
            throw new OperateException("\u62d2\u7edd\u63d0\u73b0\u5fc5\u987b\u586b\u5199\u539f\u56e0!");
        }
        BigDecimal amount = withdrawal.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("提现审核异常：金额不合法 withdrawalId={} instructorId={} amount={}",
                    withdrawal.getId(), withdrawal.getInstructorId(), amount);
            throw new OperateException("\u63d0\u73b0\u91d1\u989d\u5f02\u5e38\uff0c\u65e0\u6cd5\u5ba1\u6838");
        }
        long now = System.currentTimeMillis() / 1000L;
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        Admin admin = adminId == null ? null : (Admin)this.adminMapper.selectById((Serializable)adminId);
        String auditName = admin == null ? "\u672a\u77e5" : admin.getName();
        String finalRemark = targetStatus == 1 && auditRemark.isEmpty() ? "\u5ba1\u6838\u901a\u8fc7" : auditRemark;

        // 状态机唯一闸门：只允许「待审核(0)」条件更新到「通过(1)/拒绝(2)」。
        // 并发双审批由 InnoDB 行锁串行化，后到者 affected=0，不再重复入账/回退余额。
        int updated = this.instructorWithdrawalMapper.update(null, new UpdateWrapper<InstructorWithdrawal>()
                .eq("id", (Object)withdrawal.getId())
                .eq("status", (Object)0)
                .set("status", (Object)targetStatus)
                .set("audit_id", (Object)adminId)
                .set("audit_name", (Object)auditName)
                .set("audit_remark", (Object)finalRemark)
                .set("audit_time", (Object)now)
                .set("update_time", (Object)now));
        if (updated <= 0) {
            log.warn("提现审核并发冲突：记录已被其它请求处理 withdrawalId={} targetStatus={} adminId={}",
                    withdrawal.getId(), targetStatus, adminId);
            throw new OperateException("\u8be5\u8bb0\u5f55\u5df2\u88ab\u5904\u7406\uff0c\u8bf7\u5237\u65b0\u9875\u9762!");
        }

        if (targetStatus == 1) {
            // 累计提现原子累加：并发审核即使都通过状态闸门也只可能有一笔。
            int added = this.instructorMapper.addTotalWithdrawn(withdrawal.getInstructorId(), amount);
            if (added <= 0) {
                log.error("提现审核通过失败：教练不存在或已删除 withdrawalId={} instructorId={}",
                        withdrawal.getId(), withdrawal.getInstructorId());
                throw new OperateException("\u63d0\u73b0\u8bb0\u5f55\u5173\u8054\u7684\u6559\u7ec3\u4e0d\u5b58\u5728\uff0c\u8bf7\u6838\u5bf9\u6570\u636e");
            }
            log.info("提现审核通过 withdrawalId={} instructorId={} amount={} adminId={}",
                    withdrawal.getId(), withdrawal.getInstructorId(), amount, adminId);
            this.sendMessage(withdrawal.getInstructorId(), "\u6559\u7ec3\u63d0\u73b0\u7533\u8bf7\u5df2\u901a\u8fc7,\u91d1\u989d\u4e3a:" + amount.toPlainString() + "\u5143,\u8bf7\u53ca\u65f6\u67e5\u6536!");
            return;
        }

        // 驳回：锁定教练行后按当前余额回退，流水前后余额与原子累加保持一致。
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("id", (Object)withdrawal.getInstructorId())
                .isNull("delete_time")
                .last("limit 1 for update"));
        if (instructor == null) {
            log.error("提现驳回失败：教练不存在 withdrawalId={} instructorId={}",
                    withdrawal.getId(), withdrawal.getInstructorId());
            throw new OperateException("\u63d0\u73b0\u8bb0\u5f55\u5173\u8054\u7684\u6559\u7ec3\u4e0d\u5b58\u5728\uff0c\u8bf7\u6838\u5bf9\u6570\u636e");
        }
        BigDecimal beforeBalance = instructor.getBalance() != null ? instructor.getBalance() : BigDecimal.ZERO;
        BigDecimal afterBalance = beforeBalance.add(amount);
        int added = this.instructorMapper.addBalance(instructor.getId(), amount);
        if (added <= 0) {
            log.error("提现驳回失败：教练账户状态异常 withdrawalId={} instructorId={}",
                    withdrawal.getId(), withdrawal.getInstructorId());
            throw new OperateException("\u63d0\u73b0\u9a73\u56de\u5931\u8d25\uff1a\u6559\u7ec3\u8d26\u6237\u72b6\u6001\u5f02\u5e38");
        }
        InstructorAccountLog accountLog = new InstructorAccountLog();
        accountLog.setInstructorId(instructor.getId());
        accountLog.setType(Integer.valueOf(7));
        accountLog.setAmount(amount);
        accountLog.setBeforeBalance(beforeBalance);
        accountLog.setAfterBalance(afterBalance);
        accountLog.setRelationId(withdrawal.getId());
        accountLog.setRemark("\u63d0\u73b0\u9a73\u56de\u9000\u6b3e\uff0c\u5355\u53f7: " + withdrawal.getSn());
        accountLog.setCreateTime(Long.valueOf(now));
        this.instructorAccountLogMapper.insert(accountLog);
        log.info("提现审核驳回，余额已回退 withdrawalId={} instructorId={} amount={} before={} after={} adminId={}",
                withdrawal.getId(), withdrawal.getInstructorId(), amount, beforeBalance, afterBalance, adminId);
        this.sendMessage(withdrawal.getInstructorId(), "\u6559\u7ec3\u63d0\u73b0\u7533\u8bf7\u88ab\u62d2\u7edd,\u91d1\u989d\u4e3a:" + amount.toPlainString() + "\u5143,\u62d2\u7edd\u539f\u56e0:" + auditRemark + "\uff0c\u5df2\u9000\u56de\u4f59\u989d\u3002");
    }

    private void sendMessage(Integer instructorId, String content) {
        Message message = new Message();
        message.setType(Integer.valueOf(2));
        message.setTitle("\u7cfb\u7edf\u901a\u77e5");
        message.setContent(content);
        message.setReceiverId(Integer.valueOf(instructorId != null ? instructorId : 0));
        message.setReceiverType("instructor");
        message.setIsRead(Integer.valueOf(0));
        message.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        message.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.messageMapper.insert(message);
    }
}

