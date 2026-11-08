/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.service.agent.IAgentFinanceService
 *  com.mdd.admin.service.impl.agent.AgentFinanceServiceImpl
 *  com.mdd.admin.validate.agent.AgentWithdrawalApplyValidate
 *  com.mdd.admin.validate.agent.AgentWithdrawalAuditValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.agent.AdminAgentWithdrawalStatsVo
 *  com.mdd.admin.vo.agent.AgentEarningLogVo
 *  com.mdd.admin.vo.agent.AgentFinanceStatsVo
 *  com.mdd.admin.vo.agent.AgentWithdrawalVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Message
 *  com.mdd.common.entity.agent.Agent
 *  com.mdd.common.entity.agent.AgentEarningLog
 *  com.mdd.common.entity.agent.AgentWithdrawal
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.MessageMapper
 *  com.mdd.common.mapper.agent.AgentEarningLogMapper
 *  com.mdd.common.mapper.agent.AgentMapper
 *  com.mdd.common.mapper.agent.AgentWithdrawalMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl.agent;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.agent.IAgentFinanceService;
import com.mdd.admin.aop.aspect.SensitiveDataMasker;
import com.mdd.admin.validate.agent.AgentWithdrawalApplyValidate;
import com.mdd.admin.validate.agent.AgentWithdrawalAuditValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.agent.AdminAgentWithdrawalStatsVo;
import com.mdd.admin.vo.agent.AgentEarningLogVo;
import com.mdd.admin.vo.agent.AgentFinanceStatsVo;
import com.mdd.admin.vo.agent.AgentWithdrawalVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Message;
import com.mdd.common.entity.agent.Agent;
import com.mdd.common.entity.agent.AgentEarningLog;
import com.mdd.common.entity.agent.AgentWithdrawal;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.MessageMapper;
import com.mdd.common.mapper.agent.AgentEarningLogMapper;
import com.mdd.common.mapper.agent.AgentMapper;
import com.mdd.common.mapper.agent.AgentWithdrawalMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.util.TimeUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentFinanceServiceImpl
implements IAgentFinanceService {
    @Resource
    AgentMapper agentMapper;
    @Resource
    AgentEarningLogMapper agentEarningLogMapper;
    @Resource
    AgentWithdrawalMapper agentWithdrawalMapper;
    @Resource
    MessageMapper messageMapper;
    @Resource
    CourseClassMapper courseClassMapper;

    private Agent getCurrentAgent() {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId == null) {
            return null;
        }
        return (Agent)this.agentMapper.selectOne(new QueryWrapper<Agent>().eq("admin_id", (Object)adminId).isNull("delete_time").last("limit 1"));
    }

    public AgentFinanceStatsVo stats() {
        Agent agent = this.getCurrentAgent();
        if (agent == null) {
            throw new OperateException("\u60a8\u4e0d\u662f\u4ee3\u7406\uff0c\u65e0\u6cd5\u67e5\u770b\u7edf\u8ba1");
        }
        AgentFinanceStatsVo vo = new AgentFinanceStatsVo();
        vo.setAgentName(agent.getName());
        vo.setBankName(agent.getBankName());
        vo.setBankCard(agent.getBankCard());
        vo.setWithdrawableAmount(agent.getBalance());
        QueryWrapper<AgentWithdrawal> qwSuccess = new QueryWrapper<AgentWithdrawal>();
        qwSuccess.eq("agent_id", (Object)agent.getId()).eq("status", (Object)1);
        vo.setSuccessCount(Integer.valueOf(Math.toIntExact(this.agentWithdrawalMapper.selectCount((Wrapper)qwSuccess))));
        QueryWrapper<AgentWithdrawal> qwPending = new QueryWrapper<AgentWithdrawal>();
        qwPending.eq("agent_id", (Object)agent.getId()).eq("status", (Object)0);
        vo.setPendingCount(Integer.valueOf(Math.toIntExact(this.agentWithdrawalMapper.selectCount((Wrapper)qwPending))));
        QueryWrapper<AgentWithdrawal> qwRefund = new QueryWrapper<AgentWithdrawal>();
        qwRefund.eq("agent_id", (Object)agent.getId()).eq("status", (Object)2);
        vo.setRefundCount(Integer.valueOf(Math.toIntExact(this.agentWithdrawalMapper.selectCount((Wrapper)qwRefund))));
        QueryWrapper<AgentWithdrawal> qwEarning = new QueryWrapper<AgentWithdrawal>();
        qwEarning.select(new String[]{"IFNULL(sum(amount), 0) as amount"}).eq("agent_id", (Object)agent.getId()).eq("status", (Object)1);
        try {
            AgentWithdrawal sumLog = (AgentWithdrawal)this.agentWithdrawalMapper.selectOne((Wrapper)qwEarning);
            if (sumLog != null && sumLog.getAmount() != null) {
                vo.setTotalRevenue(sumLog.getAmount());
            } else {
                vo.setTotalRevenue(BigDecimal.ZERO);
            }
        }
        catch (Exception e) {
            vo.setTotalRevenue(BigDecimal.ZERO);
        }
        return vo;
    }

    public AdminAgentWithdrawalStatsVo adminStats() {
        AdminAgentWithdrawalStatsVo vo = new AdminAgentWithdrawalStatsVo();
        List<Long> month = TimeUtils.month();
        long start = (Long)month.get(0);
        long end = (Long)month.get(1);
        QueryWrapper<AgentWithdrawal> qwMonthCount = new QueryWrapper<AgentWithdrawal>();
        qwMonthCount.ge("create_time", (Object)start).le("create_time", (Object)end);
        vo.setMonthWithdrawalCount(Integer.valueOf(Math.toIntExact(this.agentWithdrawalMapper.selectCount((Wrapper)qwMonthCount))));
        QueryWrapper<AgentWithdrawal> qwMonthAmount = new QueryWrapper<AgentWithdrawal>();
        qwMonthAmount.select(new String[]{"IFNULL(sum(amount), 0) as amount"}).eq("status", (Object)1).ge("create_time", (Object)start).le("create_time", (Object)end);
        try {
            AgentWithdrawal sumLog1 = (AgentWithdrawal)this.agentWithdrawalMapper.selectOne((Wrapper)qwMonthAmount);
            vo.setMonthWithdrawalAmount(sumLog1 != null && sumLog1.getAmount() != null ? sumLog1.getAmount() : BigDecimal.ZERO);
        }
        catch (Exception e) {
            vo.setMonthWithdrawalAmount(BigDecimal.ZERO);
        }
        QueryWrapper<AgentWithdrawal> qwPending = new QueryWrapper<AgentWithdrawal>();
        qwPending.eq("status", (Object)0);
        vo.setPendingCount(Integer.valueOf(Math.toIntExact(this.agentWithdrawalMapper.selectCount((Wrapper)qwPending))));
        QueryWrapper<AgentWithdrawal> qwTotalAmount = new QueryWrapper<AgentWithdrawal>();
        qwTotalAmount.select(new String[]{"IFNULL(sum(amount), 0) as amount"}).eq("status", (Object)1);
        try {
            AgentWithdrawal sumLog2 = (AgentWithdrawal)this.agentWithdrawalMapper.selectOne((Wrapper)qwTotalAmount);
            vo.setTotalWithdrawalAmount(sumLog2 != null && sumLog2.getAmount() != null ? sumLog2.getAmount() : BigDecimal.ZERO);
        }
        catch (Exception e) {
            vo.setTotalWithdrawalAmount(BigDecimal.ZERO);
        }
        return vo;
    }

    public PageResult<AgentEarningLogVo> earningList(PageValidate pageValidate, Integer agentId) {
        if (agentId == null || agentId <= 0) {
            Agent agent = this.getCurrentAgent();
            if (agent == null) {
                throw new OperateException("\u60a8\u4e0d\u662f\u4ee3\u7406\uff0c\u65e0\u6cd5\u67e5\u770b\u6536\u76ca\u8bb0\u5f55");
            }
            agentId = agent.getId();
        }
        IPage<AgentEarningLog> iPage = this.agentEarningLogMapper.selectPage((IPage)new Page((long)pageValidate.getPage_no().intValue(), (long)pageValidate.getPage_size().intValue()), new QueryWrapper<AgentEarningLog>().eq("agent_id", (Object)agentId).orderByDesc("id"));
        ArrayList<AgentEarningLogVo> list = new ArrayList<AgentEarningLogVo>();
        for (AgentEarningLog log : iPage.getRecords()) {
            AgentEarningLogVo vo = new AgentEarningLogVo();
            vo.setId(log.getId());
            vo.setCourseClassId(log.getCourseClassId());
            vo.setCommissionRate(log.getCommissionRate());
            vo.setEarningAmount(log.getEarningAmount());
            vo.setOrderAmount(log.getOrderAmount());
            vo.setCreateTime(TimeUtils.timestampToDate((Long)log.getCreateTime()));
            CourseClass cls = (CourseClass)this.courseClassMapper.selectById((Serializable)log.getCourseClassId());
            if (cls != null) {
                vo.setCourseName(cls.getName());
            }
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)pageValidate.getPage_size().longValue(), list);
    }

    public PageResult<AgentWithdrawalVo> withdrawalList(PageValidate pageValidate, Integer status, String agentName, String startTime, String endTime) {
        QueryWrapper<AgentWithdrawal> qw = new QueryWrapper<AgentWithdrawal>();
        Agent agent = this.getCurrentAgent();
        if (agent != null) {
            qw.eq("agent_id", (Object)agent.getId());
        }
        if (status != null) {
            qw.eq("status", (Object)status);
        }
        if (agentName != null && !agentName.trim().isEmpty()) {
            List<Agent> matchedAgents = this.agentMapper.selectList(new QueryWrapper<Agent>().like("name", (Object)agentName.trim()).isNull("delete_time"));
            if (matchedAgents.isEmpty()) {
                return PageResult.iPageHandle((Long)0L, (Long)((Object)pageValidate.getPage_no()), (Long)pageValidate.getPage_size().longValue(), new ArrayList());
            }
            ArrayList<Integer> agentIds = new ArrayList<Integer>();
            for (Agent a : matchedAgents) {
                agentIds.add(a.getId());
            }
            qw.in("agent_id", agentIds);
        }
        if (startTime != null && !startTime.trim().isEmpty()) {
            long startTs = TimeUtils.dateToTimestamp((String)startTime.trim());
            qw.ge("create_time", (Object)startTs);
        }
        if (endTime != null && !endTime.trim().isEmpty()) {
            long endTs = TimeUtils.dateToTimestamp((String)endTime.trim()) + 86399L;
            qw.le("create_time", (Object)endTs);
        }
        qw.orderByDesc("id");
        IPage<AgentWithdrawal> iPage = this.agentWithdrawalMapper.selectPage((IPage)new Page((long)pageValidate.getPage_no().intValue(), (long)pageValidate.getPage_size().intValue()), (Wrapper)qw);
        ArrayList<AgentWithdrawalVo> list = new ArrayList<AgentWithdrawalVo>();
        for (AgentWithdrawal w : iPage.getRecords()) {
            Agent a;
            AgentWithdrawalVo vo = new AgentWithdrawalVo();
            vo.setId(w.getId());
            vo.setWithdrawAmount(w.getAmount());
            vo.setBankName(w.getBankName());
            vo.setBankCard(SensitiveDataMasker.maskBankCard(w.getBankCard()));
            vo.setStatus(w.getStatus());
            vo.setAuditRemark(w.getAuditRemark());
            vo.setSubmitTime(TimeUtils.timestampToDate((Long)w.getCreateTime()));
            if (w.getAuditTime() != null && w.getAuditTime() > 0L) {
                vo.setAuditTime(TimeUtils.timestampToDate((Long)w.getAuditTime()));
            }
            if ((a = (Agent)this.agentMapper.selectById((Serializable)w.getAgentId())) != null) {
                vo.setAgentName(a.getName());
            }
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)pageValidate.getPage_size().longValue(), list);
    }

    @Transactional(rollbackFor={Exception.class})
    public void applyWithdrawal(AgentWithdrawalApplyValidate applyValidate) {
        Agent agent = this.getCurrentAgent();
        if (agent == null) {
            throw new OperateException("\u60a8\u4e0d\u662f\u4ee3\u7406\uff0c\u65e0\u6cd5\u7533\u8bf7\u63d0\u73b0");
        }
        if (applyValidate.getAmount().compareTo(agent.getBalance()) > 0) {
            throw new OperateException("\u63d0\u73b0\u91d1\u989d\u5927\u4e8e\u53ef\u63d0\u73b0\u4f59\u989d");
        }
        if (agent.getBankName() == null || agent.getBankName().isEmpty()) {
            throw new OperateException("\u8bf7\u5148\u5728\u8d26\u53f7\u8bbe\u7f6e\u4e2d\u7ed1\u5b9a\u63d0\u73b0\u94f6\u884c");
        }
        if (agent.getBankCard() == null || agent.getBankCard().isEmpty()) {
            throw new OperateException("\u8bf7\u5148\u5728\u8d26\u53f7\u8bbe\u7f6e\u4e2d\u7ed1\u5b9a\u63d0\u73b0\u94f6\u884c\u5361\u53f7");
        }
        agent.setBalance(agent.getBalance().subtract(applyValidate.getAmount()));
        this.agentMapper.updateById(agent);
        AgentWithdrawal w = new AgentWithdrawal();
        w.setAgentId(agent.getId());
        w.setAmount(applyValidate.getAmount());
        w.setBankName(agent.getBankName());
        w.setBankCard(agent.getBankCard());
        w.setStatus(Integer.valueOf(0));
        w.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        w.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.agentWithdrawalMapper.insert(w);
        Message msg = new Message();
        msg.setType(Integer.valueOf(2));
        msg.setTitle("\u4ee3\u7406\u63d0\u73b0\u7533\u8bf7");
        msg.setContent(agent.getName() + "\u7533\u8bf7\u63d0\u73b0" + applyValidate.getAmount() + "\u5143");
        msg.setSenderId(agent.getAdminId());
        msg.setSenderType("agent");
        msg.setSenderName(agent.getName());
        msg.setReceiverId(Integer.valueOf(0));
        msg.setReceiverType("admin");
        msg.setIsRead(Integer.valueOf(0));
        msg.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        msg.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        msg.setDeleteTime(Long.valueOf(0L));
        this.messageMapper.insert(msg);
    }

    @Transactional(rollbackFor={Exception.class})
    public void auditWithdrawal(AgentWithdrawalAuditValidate auditValidate) {
        Agent agent;
        AgentWithdrawal w = (AgentWithdrawal)this.agentWithdrawalMapper.selectById((Serializable)auditValidate.getId());
        if (w == null) {
            throw new OperateException("\u63d0\u73b0\u8bb0\u5f55\u4e0d\u5b58\u5728");
        }
        if (w.getStatus() != 0) {
            throw new OperateException("\u63d0\u73b0\u8bb0\u5f55\u5df2\u5904\u7406");
        }
        w.setStatus(auditValidate.getStatus());
        w.setAuditRemark(auditValidate.getAuditRemark() != null ? auditValidate.getAuditRemark() : "");
        w.setAuditTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        w.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.agentWithdrawalMapper.updateById(w);
        if (auditValidate.getStatus() == 2 && (agent = (Agent)this.agentMapper.selectById((Serializable)w.getAgentId())) != null) {
            agent.setBalance(agent.getBalance().add(w.getAmount()));
            this.agentMapper.updateById(agent);
        }
    }
}
