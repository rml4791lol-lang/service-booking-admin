/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.agent.Agent;
import com.mdd.common.entity.agent.AgentEarningLog;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.community.CommunityEarningLog;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.agent.AgentEarningLogMapper;
import com.mdd.common.mapper.agent.AgentMapper;
import com.mdd.common.mapper.community.CommunityEarningLogMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LessonCommissionService {
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CommunityMapper communityMapper;
    @Resource
    CommunityEarningLogMapper communityEarningLogMapper;
    @Resource
    AgentMapper agentMapper;
    @Resource
    AgentEarningLogMapper agentEarningLogMapper;

    public void recordForLessonConsume(UserCourse userCourse, CourseClass courseClass) {
        Agent agent;
        BigDecimal totalEarnings;
        BigDecimal balance;
        Serializable log;
        BigDecimal earning;
        BigDecimal rate;
        Community community;
        if (userCourse == null || courseClass == null) {
            return;
        }
        BigDecimal unitAmount = this.resolveUnitAmount(userCourse, courseClass);
        if (unitAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        RechargeOrder order = this.resolveRechargeOrder(userCourse);
        Integer orderId = order != null && order.getId() != null ? order.getId() : 0;
        long now = System.currentTimeMillis() / 1000L;
        if (courseClass.getCommunityId() != null && courseClass.getCommunityId() > 0 && (community = (Community)this.communityMapper.selectById(courseClass.getCommunityId())) != null && community.getDeleteTime() == null) {
            BigDecimal bigDecimal = rate = community.getCommissionRate() != null ? community.getCommissionRate() : BigDecimal.ZERO;
            if (rate.compareTo(BigDecimal.ZERO) > 0 && (earning = unitAmount.multiply(rate).setScale(2, RoundingMode.HALF_UP)).compareTo(BigDecimal.ZERO) > 0) {
                log = new CommunityEarningLog();
                ((CommunityEarningLog)log).setCommunityId(community.getId());
                ((CommunityEarningLog)log).setCourseClassId(courseClass.getId());
                ((CommunityEarningLog)log).setOrderId(orderId);
                ((CommunityEarningLog)log).setOrderAmount(unitAmount);
                ((CommunityEarningLog)log).setCommissionRate(rate);
                ((CommunityEarningLog)log).setEarningAmount(earning);
                ((CommunityEarningLog)log).setCreateTime(now);
                ((CommunityEarningLog)log).setUpdateTime(now);
                this.communityEarningLogMapper.insert((CommunityEarningLog)log);
                balance = community.getBalance() != null ? community.getBalance() : BigDecimal.ZERO;
                totalEarnings = community.getTotalEarnings() != null ? community.getTotalEarnings() : BigDecimal.ZERO;
                community.setBalance(balance.add(earning));
                community.setTotalEarnings(totalEarnings.add(earning));
                community.setUpdateTime(now);
                this.communityMapper.updateById(community);
            }
        }
        if (courseClass.getAgentId() != null && courseClass.getAgentId() > 0 && (agent = (Agent)this.agentMapper.selectById(courseClass.getAgentId())) != null && agent.getDeleteTime() == null) {
            BigDecimal bigDecimal = rate = agent.getCommissionRate() != null ? agent.getCommissionRate() : BigDecimal.ZERO;
            if (rate.compareTo(BigDecimal.ZERO) > 0 && (earning = unitAmount.multiply(rate).setScale(2, RoundingMode.HALF_UP)).compareTo(BigDecimal.ZERO) > 0) {
                log = new AgentEarningLog();
                ((AgentEarningLog)log).setAgentId(agent.getId());
                ((AgentEarningLog)log).setCourseClassId(courseClass.getId());
                ((AgentEarningLog)log).setOrderId(orderId);
                ((AgentEarningLog)log).setOrderAmount(unitAmount);
                ((AgentEarningLog)log).setCommissionRate(rate);
                ((AgentEarningLog)log).setEarningAmount(earning);
                ((AgentEarningLog)log).setCreateTime(now);
                ((AgentEarningLog)log).setUpdateTime(now);
                this.agentEarningLogMapper.insert((AgentEarningLog)log);
                balance = agent.getBalance() != null ? agent.getBalance() : BigDecimal.ZERO;
                totalEarnings = agent.getTotalEarnings() != null ? agent.getTotalEarnings() : BigDecimal.ZERO;
                agent.setBalance(balance.add(earning));
                agent.setTotalEarnings(totalEarnings.add(earning));
                agent.setUpdateTime(now);
                this.agentMapper.updateById(agent);
            }
        }
    }

    private RechargeOrder resolveRechargeOrder(UserCourse userCourse) {
        RechargeOrder order;
        if (userCourse.getOrderId() != null && userCourse.getOrderId() > 0 && (order = (RechargeOrder)this.rechargeOrderMapper.selectById(userCourse.getOrderId())) != null && order.getDeleteTime() == null) {
            return order;
        }
        if (userCourse.getUserId() == null || userCourse.getCourseClassId() == null) {
            return null;
        }
        return (RechargeOrder)this.rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>().eq("user_id", (Object)userCourse.getUserId()).eq("course_class_id", (Object)userCourse.getCourseClassId()).eq("pay_status", (Object)1).isNull("delete_time").orderByAsc("id").last("limit 1"));
    }

    private BigDecimal resolveUnitAmount(UserCourse userCourse, CourseClass courseClass) {
        int totalLessons;
        RechargeOrder order = this.resolveRechargeOrder(userCourse);
        if (order != null && (totalLessons = this.resolveTotalLessons(userCourse, order)) > 0 && order.getOrderAmount() != null && order.getOrderAmount().compareTo(BigDecimal.ZERO) > 0) {
            return order.getOrderAmount().divide(BigDecimal.valueOf(totalLessons), 2, RoundingMode.HALF_UP);
        }
        if (courseClass.getTotalFee() != null && courseClass.getTotalFee().compareTo(BigDecimal.ZERO) > 0) {
            return courseClass.getTotalFee();
        }
        if (courseClass.getFeePerLesson() != null && courseClass.getFeePerLesson().compareTo(BigDecimal.ZERO) > 0) {
            return courseClass.getFeePerLesson();
        }
        return BigDecimal.ZERO;
    }

    private int resolveTotalLessons(UserCourse userCourse, RechargeOrder order) {
        if (userCourse.getTotalLessons() != null && userCourse.getTotalLessons() > 0) {
            return userCourse.getTotalLessons();
        }
        if (order.getLessonCount() != null && order.getLessonCount() > 0) {
            return order.getLessonCount();
        }
        return 0;
    }
}

