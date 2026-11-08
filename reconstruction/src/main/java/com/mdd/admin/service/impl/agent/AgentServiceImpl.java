/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.admin.IAdminRoleService
 *  com.mdd.admin.service.agent.IAgentService
 *  com.mdd.admin.service.impl.agent.AgentServiceImpl
 *  com.mdd.admin.validate.agent.AgentCreateValidate
 *  com.mdd.admin.validate.agent.AgentSearchValidate
 *  com.mdd.admin.validate.agent.AgentUpdateValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.agent.AgentDetailVo
 *  com.mdd.admin.vo.agent.AgentListedVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.entity.agent.Agent
 *  com.mdd.common.entity.community.CommunityAgent
 *  com.mdd.common.entity.system.SystemRole
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.agent.AgentMapper
 *  com.mdd.common.mapper.community.CommunityAgentMapper
 *  com.mdd.common.mapper.system.SystemRoleMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.ToolUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl.agent;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.admin.IAdminRoleService;
import com.mdd.admin.aop.aspect.SensitiveDataMasker;
import com.mdd.admin.service.agent.IAgentService;
import com.mdd.admin.validate.agent.AgentCreateValidate;
import com.mdd.admin.validate.agent.AgentSearchValidate;
import com.mdd.admin.validate.agent.AgentUpdateValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.agent.AgentDetailVo;
import com.mdd.admin.vo.agent.AgentListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.agent.Agent;
import com.mdd.common.entity.community.CommunityAgent;
import com.mdd.common.entity.system.SystemRole;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.agent.AgentMapper;
import com.mdd.common.mapper.community.CommunityAgentMapper;
import com.mdd.common.mapper.system.SystemRoleMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentServiceImpl
implements IAgentService {
    @Resource
    AgentMapper agentMapper;
    @Resource
    AdminMapper adminMapper;
    @Resource
    SystemRoleMapper systemRoleMapper;
    @Resource
    IAdminRoleService iAdminRoleService;
    @Resource
    CommunityAgentMapper communityAgentMapper;

    public PageResult<AgentListedVo> list(PageValidate pageValidate, AgentSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<Agent> qw = new QueryWrapper<Agent>();
        qw.isNull("delete_time");
        if (StringUtils.isNotEmpty((String)searchValidate.getName())) {
            qw.like("name", (Object)searchValidate.getName());
        }
        if (StringUtils.isNotEmpty((String)searchValidate.getMobile())) {
            qw.like("mobile", (Object)searchValidate.getMobile());
        }
        if (searchValidate.getCommunityId() != null && searchValidate.getCommunityId() > 0) {
            List<CommunityAgent> relations = this.communityAgentMapper.selectList(new QueryWrapper<CommunityAgent>().eq("community_id", (Object)searchValidate.getCommunityId()));
            if (relations.isEmpty()) {
                return PageResult.iPageHandle((Long)0L, Long.valueOf(page), (Long)limit.longValue(), new ArrayList());
            }
            ArrayList<Integer> agentIds = new ArrayList<Integer>();
            for (CommunityAgent r : relations) {
                agentIds.add(r.getAgentId());
            }
            qw.in("id", agentIds);
        }
        qw.orderByDesc("id");
        IPage<Agent> iPage = this.agentMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)qw);
        ArrayList<AgentListedVo> list = new ArrayList<AgentListedVo>();
        for (Agent agent : iPage.getRecords()) {
            AgentListedVo vo = new AgentListedVo();
            BeanUtils.copyProperties((Object)agent, (Object)vo);
            vo.setMobile(SensitiveDataMasker.maskMobile(vo.getMobile()));
            vo.setBankCard(SensitiveDataMasker.maskBankCard(vo.getBankCard()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)agent.getCreateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)limit.longValue(), list);
    }

    public AgentDetailVo detail(Integer id) {
        Agent agent = (Agent)this.agentMapper.selectById((Serializable)id);
        if (agent == null || agent.getDeleteTime() != null) {
            throw new OperateException("\u4ee3\u7406\u4e0d\u5b58\u5728");
        }
        AgentDetailVo vo = new AgentDetailVo();
        BeanUtils.copyProperties((Object)agent, (Object)vo);
        vo.setMobile(SensitiveDataMasker.maskMobile(vo.getMobile()));
        vo.setBankCard(SensitiveDataMasker.maskBankCard(vo.getBankCard()));
        vo.setCreateTime(TimeUtils.timestampToDate((Long)agent.getCreateTime()));
        return vo;
    }

    @Transactional(rollbackFor={Exception.class})
    public void add(AgentCreateValidate createValidate) {
        Admin adminCheck = (Admin)this.adminMapper.selectOne(new QueryWrapper<Admin>().eq("account", (Object)createValidate.getMobile()).isNull("delete_time").last("limit 1"));
        if (adminCheck != null) {
            throw new OperateException("\u8be5\u624b\u673a\u53f7\u5df2\u6ce8\u518c\u4e3a\u7ba1\u7406\u5458\u8d26\u53f7\uff0c\u8bf7\u66f4\u6362\u624b\u673a\u53f7");
        }
        String pwd = ToolUtils.makePassword((String)createValidate.getPassword().trim());
        Admin admin = new Admin();
        admin.setAccount(createValidate.getMobile());
        admin.setName(createValidate.getName());
        admin.setAvatar("/api/static/backend_avatar.png");
        admin.setPassword(pwd);
        admin.setMultipointLogin(Integer.valueOf(1));
        admin.setDisable(Integer.valueOf(0));
        admin.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        admin.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.adminMapper.insert(admin);
        SystemRole role = (SystemRole)this.systemRoleMapper.selectOne(new QueryWrapper<SystemRole>().eq("name", (Object)"\u4ee3\u7406").last("limit 1"));
        if (role == null) {
            role = new SystemRole();
            role.setName("\u4ee3\u7406");
            role.setDesc("\u4ee3\u7406\u5546\u7528\u6237");
            role.setSort(Integer.valueOf(0));
            role.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            role.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.systemRoleMapper.insert(role);
        }
        ArrayList<Integer> roleIds = new ArrayList<Integer>();
        roleIds.add(role.getId());
        this.iAdminRoleService.batchInsert(admin.getId(), roleIds);
        Agent agent = new Agent();
        agent.setAdminId(admin.getId());
        agent.setName(createValidate.getName());
        agent.setMobile(createValidate.getMobile());
        agent.setAreaPolygon(createValidate.getAreaPolygon() == null ? "" : createValidate.getAreaPolygon());
        agent.setCommissionRate(createValidate.getCommissionRate());
        agent.setBankName(createValidate.getBankName() == null ? "" : createValidate.getBankName());
        agent.setBankCard(createValidate.getBankCard() == null ? "" : createValidate.getBankCard());
        agent.setBalance(BigDecimal.ZERO);
        agent.setTotalEarnings(BigDecimal.ZERO);
        agent.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        agent.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.agentMapper.insert(agent);
    }

    @Transactional(rollbackFor={Exception.class})
    public void edit(AgentUpdateValidate updateValidate) {
        Agent agent = (Agent)this.agentMapper.selectById((Serializable)updateValidate.getId());
        if (agent == null || agent.getDeleteTime() != null) {
            throw new OperateException("\u4ee3\u7406\u4e0d\u5b58\u5728");
        }
        Admin adminCheck = (Admin)this.adminMapper.selectOne(new QueryWrapper<Admin>().eq("account", (Object)updateValidate.getMobile()).ne("id", (Object)agent.getAdminId()).isNull("delete_time").last("limit 1"));
        if (adminCheck != null) {
            throw new OperateException("\u8be5\u624b\u673a\u53f7\u5df2\u6ce8\u518c\u5176\u4ed6\u8d26\u53f7");
        }
        agent.setName(updateValidate.getName());
        agent.setMobile(updateValidate.getMobile());
        agent.setAreaPolygon(updateValidate.getAreaPolygon() == null ? "" : updateValidate.getAreaPolygon());
        agent.setCommissionRate(updateValidate.getCommissionRate());
        if (updateValidate.getBankName() != null) {
            agent.setBankName(updateValidate.getBankName());
        }
        if (updateValidate.getBankCard() != null) {
            agent.setBankCard(updateValidate.getBankCard());
        }
        agent.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.agentMapper.updateById(agent);
        Admin admin = (Admin)this.adminMapper.selectById((Serializable)agent.getAdminId());
        if (admin != null) {
            admin.setAccount(updateValidate.getMobile());
            admin.setName(updateValidate.getName());
            if (StringUtils.isNotEmpty((String)updateValidate.getPassword())) {
                admin.setPassword(ToolUtils.makePassword((String)updateValidate.getPassword().trim()));
            }
            admin.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.adminMapper.updateById(admin);
        }
    }

    @Transactional(rollbackFor={Exception.class})
    public void del(Integer id) {
        Agent agent = (Agent)this.agentMapper.selectById((Serializable)id);
        if (agent == null || agent.getDeleteTime() != null) {
            throw new OperateException("\u4ee3\u7406\u4e0d\u5b58\u5728");
        }
        agent.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.agentMapper.updateById(agent);
        Admin admin = (Admin)this.adminMapper.selectById((Serializable)agent.getAdminId());
        if (admin != null) {
            admin.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.adminMapper.updateById(admin);
        }
    }
}
