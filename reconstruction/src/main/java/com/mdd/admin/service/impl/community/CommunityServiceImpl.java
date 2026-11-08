/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.admin.IAdminRoleService
 *  com.mdd.admin.service.community.ICommunityService
 *  com.mdd.admin.service.impl.community.CommunityServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.community.CommunityCreateValidate
 *  com.mdd.admin.validate.community.CommunitySearchValidate
 *  com.mdd.admin.validate.community.CommunityUpdateValidate
 *  com.mdd.admin.vo.community.CommunityDetailVo
 *  com.mdd.admin.vo.community.CommunityEarningLogVo
 *  com.mdd.admin.vo.community.CommunityListedVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.entity.community.Community
 *  com.mdd.common.entity.community.CommunityAgent
 *  com.mdd.common.entity.community.CommunityEarningLog
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.system.SystemRole
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.community.CommunityAgentMapper
 *  com.mdd.common.mapper.community.CommunityEarningLogMapper
 *  com.mdd.common.mapper.community.CommunityMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.system.SystemRoleMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.ToolUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl.community;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.admin.IAdminRoleService;
import com.mdd.admin.service.community.ICommunityService;
import com.mdd.admin.service.region.IRegionService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.community.CommunityCreateValidate;
import com.mdd.admin.validate.community.CommunitySearchValidate;
import com.mdd.admin.validate.community.CommunityUpdateValidate;
import com.mdd.admin.vo.community.CommunityDetailVo;
import com.mdd.admin.vo.community.CommunityEarningLogVo;
import com.mdd.admin.vo.community.CommunityListedVo;
import com.mdd.admin.vo.community.CommunityOptionVo;
import com.mdd.admin.vo.region.RegionStreetVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.community.CommunityAgent;
import com.mdd.common.entity.community.CommunityEarningLog;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.system.SystemRole;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.community.CommunityAgentMapper;
import com.mdd.common.mapper.community.CommunityEarningLogMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.system.SystemRoleMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommunityServiceImpl
implements ICommunityService {
    @Resource
    CommunityMapper communityMapper;
    @Resource
    CommunityEarningLogMapper communityEarningLogMapper;
    @Resource
    CommunityAgentMapper communityAgentMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    AdminMapper adminMapper;
    @Resource
    SystemRoleMapper systemRoleMapper;
    @Resource
    IAdminRoleService iAdminRoleService;
    @Resource
    AreaMapper areaMapper;
    @Resource
    IRegionService iRegionService;

    public PageResult<CommunityListedVo> list(PageValidate pageValidate, CommunitySearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<Community> qw = new QueryWrapper<Community>();
        qw.isNull("delete_time");
        if (StringUtils.isNotEmpty(searchValidate.getName())) {
            qw.like("name", searchValidate.getName());
        }
        if (StringUtils.isNotEmpty(searchValidate.getMobile())) {
            qw.like("mobile", searchValidate.getMobile());
        }
        if (searchValidate.getRegionId() != null && searchValidate.getRegionId() > 0) {
            qw.eq("region_id", searchValidate.getRegionId());
        }
        qw.orderByDesc("id");
        IPage<Community> iPage = this.communityMapper.selectPage(new Page<>(page, limit), qw);

        Map<Integer, RegionStreetVo> streetMap = this.iRegionService.streetMap();
        Map<Integer, Integer> areaCountMap = this.areaCountMap();
        ArrayList<CommunityListedVo> list = new ArrayList<>();
        for (Community community : iPage.getRecords()) {
            CommunityListedVo vo = new CommunityListedVo();
            BeanUtils.copyProperties(community, vo);
            vo.setCreateTime(TimeUtils.timestampToDate(community.getCreateTime()));
            vo.setAreaCount(areaCountMap.getOrDefault(community.getId(), 0));
            this.fillRegion(vo, community.getRegionId(), streetMap);
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), limit.longValue(), list);
    }

    public CommunityDetailVo detail(Integer id) {
        Community community = (Community)this.communityMapper.selectById((Serializable)id);
        if (community == null || community.getDeleteTime() != null) {
            throw new OperateException("\u793e\u533a\u4e0d\u5b58\u5728");
        }
        CommunityDetailVo vo = new CommunityDetailVo();
        BeanUtils.copyProperties(community, vo);
        vo.setCreateTime(TimeUtils.timestampToDate(community.getCreateTime()));
        Long areaCount = this.areaMapper.selectCount(
                new QueryWrapper<Area>().eq("community_id", id).isNull("delete_time")
        );
        vo.setAreaCount(areaCount == null ? 0 : areaCount.intValue());
        this.fillRegion(vo, community.getRegionId(), this.iRegionService.streetMap());
        return vo;
    }

    @Transactional(rollbackFor={Exception.class})
    public Integer add(CommunityCreateValidate createValidate) {
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
        SystemRole role = (SystemRole)this.systemRoleMapper.selectOne(new QueryWrapper<SystemRole>().eq("name", (Object)"\u793e\u533a").isNull("delete_time").last("limit 1"));
        if (role == null) {
            role = new SystemRole();
            role.setName("\u793e\u533a");
            role.setDesc("\u793e\u533a\u4e3b\u4f53\u8d26\u53f7");
            role.setSort(Integer.valueOf(0));
            role.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            role.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.systemRoleMapper.insert(role);
        }
        ArrayList<Integer> roleIds = new ArrayList<Integer>();
        roleIds.add(role.getId());
        this.iAdminRoleService.batchInsert(admin.getId(), roleIds);
        Community community = new Community();
        community.setAdminId(admin.getId());
        community.setName(createValidate.getName());
        community.setMobile(createValidate.getMobile());
        community.setCommissionRate(createValidate.getCommissionRate());
        community.setBankName(createValidate.getBankName() == null ? "" : createValidate.getBankName());
        community.setBankCard(createValidate.getBankCard() == null ? "" : createValidate.getBankCard());
        this.checkLocation(createValidate.getLatitude(), createValidate.getLongitude());
        community.setRegionId(createValidate.getRegionId() == null ? 0 : createValidate.getRegionId());
        community.setAddress(StringUtils.trim(createValidate.getAddress()));
        community.setLatitude(createValidate.getLatitude());
        community.setLongitude(createValidate.getLongitude());
        community.setBalance(BigDecimal.ZERO);
        community.setTotalEarnings(BigDecimal.ZERO);
        community.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        community.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.communityMapper.insert(community);
        return community.getId();
    }

    @Transactional(rollbackFor={Exception.class})
    public void edit(CommunityUpdateValidate updateValidate) {
        Community community = (Community)this.communityMapper.selectById((Serializable)updateValidate.getId());
        if (community == null || community.getDeleteTime() != null) {
            throw new OperateException("\u793e\u533a\u4e0d\u5b58\u5728");
        }
        this.checkLocation(updateValidate.getLatitude(), updateValidate.getLongitude());
        boolean clearLocation = Boolean.TRUE.equals(updateValidate.getClearLocation());
        community.setName(updateValidate.getName());
        community.setMobile(updateValidate.getMobile());
        community.setCommissionRate(updateValidate.getCommissionRate());
        if (updateValidate.getBankName() != null) {
            community.setBankName(updateValidate.getBankName());
        }
        if (updateValidate.getBankCard() != null) {
            community.setBankCard(updateValidate.getBankCard());
        }
        if (updateValidate.getRegionId() != null) {
            community.setRegionId(updateValidate.getRegionId());
        }
        if (updateValidate.getAddress() != null) {
            community.setAddress(updateValidate.getAddress().trim());
        }
        // 老后台不提交坐标：仅在显式传坐标或显式清空时改动，避免编辑社区时误清空已维护坐标
        if (clearLocation) {
            community.setLatitude(null);
            community.setLongitude(null);
        } else if (updateValidate.getLatitude() != null || updateValidate.getLongitude() != null) {
            community.setLatitude(updateValidate.getLatitude());
            community.setLongitude(updateValidate.getLongitude());
        }
        community.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.communityMapper.updateById(community);
        if (clearLocation) {
            // MyBatis-Plus updateById 默认忽略 null 字段，清空坐标必须显式 set NULL
            UpdateWrapper<Community> clearWrapper = new UpdateWrapper<>();
            clearWrapper.eq("id", community.getId()).set("latitude", null).set("longitude", null);
            this.communityMapper.update(null, clearWrapper);
        }
    }


    @Transactional(rollbackFor={Exception.class})
    public void del(Integer id) {
        Community community = (Community)this.communityMapper.selectById((Serializable)id);
        if (community == null || community.getDeleteTime() != null) {
            throw new OperateException("\u793e\u533a\u4e0d\u5b58\u5728");
        }
        Long areaCount = this.areaMapper.selectCount(
                new QueryWrapper<Area>().eq("community_id", id).isNull("delete_time")
        );
        if (areaCount != null && areaCount > 0) {
            throw new OperateException("\u8be5\u793e\u533a\u4e0b\u8fd8\u6709 " + areaCount + " \u4e2a\u5c0f\u533a\uff0c\u8bf7\u5148\u8fc1\u79fb\u6216\u5220\u9664");
        }
        community.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.communityMapper.updateById(community);
    }

    public PageResult<CommunityEarningLogVo> earningList(PageValidate pageValidate, Integer communityId) {
        QueryWrapper<Community> qw = new QueryWrapper<Community>();
        if (communityId != null && communityId > 0) {
            qw.eq("community_id", (Object)communityId);
        }
        qw.orderByDesc("id");
        IPage<CommunityEarningLog> iPage = this.communityEarningLogMapper.selectPage((IPage)new Page((long)pageValidate.getPage_no().intValue(), (long)pageValidate.getPage_size().intValue()), (Wrapper)qw);
        ArrayList<CommunityEarningLogVo> list = new ArrayList<CommunityEarningLogVo>();
        for (CommunityEarningLog log : iPage.getRecords()) {
            CommunityEarningLogVo vo = new CommunityEarningLogVo();
            vo.setId(log.getId());
            vo.setCourseClassId(log.getCourseClassId());
            vo.setOrderAmount(log.getOrderAmount());
            vo.setCommissionRate(log.getCommissionRate());
            vo.setEarningAmount(log.getEarningAmount());
            vo.setCreateTime(TimeUtils.timestampToDate((Long)log.getCreateTime()));
            CourseClass cls = (CourseClass)this.courseClassMapper.selectById((Serializable)log.getCourseClassId());
            if (cls != null) {
                vo.setCourseName(cls.getName());
            }
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)pageValidate.getPage_size().longValue(), list);
    }

    @Transactional(rollbackFor={Exception.class})
    public void bindAgents(Integer communityId, List<Integer> agentIds) {
        this.communityAgentMapper.delete(new QueryWrapper<CommunityAgent>().eq("community_id", (Object)communityId));
        if (agentIds != null && !agentIds.isEmpty()) {
            long now = System.currentTimeMillis() / 1000L;
            for (Integer agentId : agentIds) {
                CommunityAgent ca = new CommunityAgent();
                ca.setCommunityId(communityId);
                ca.setAgentId(agentId);
                ca.setCreateTime(Long.valueOf(now));
                this.communityAgentMapper.insert(ca);
            }
        }
    }

    public List<Integer> getAgentIds(Integer communityId) {
        List<CommunityAgent> relations = this.communityAgentMapper.selectList(new QueryWrapper<CommunityAgent>().eq("community_id", (Object)communityId));
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (CommunityAgent r : relations) {
            ids.add(r.getAgentId());
        }
        return ids;
    }

    public List<CommunityOptionVo> all(Integer regionId, String keyword) {
        QueryWrapper<Community> qw = new QueryWrapper<Community>();
        qw.isNull("delete_time");
        if (regionId != null && regionId > 0) {
            qw.eq("region_id", regionId);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            String value = keyword.trim();
            qw.and(w -> w.like("name", value).or().like("mobile", value));
        }
        qw.orderByAsc("id");
        List<Community> communities = this.communityMapper.selectList(qw);
        Map<Integer, RegionStreetVo> streetMap = this.iRegionService.streetMap();
        List<CommunityOptionVo> list = new ArrayList<>();
        for (Community community : communities) {
            CommunityOptionVo vo = new CommunityOptionVo();
            vo.setId(community.getId());
            vo.setName(community.getName());
            vo.setMobile(community.getMobile());
            vo.setRegionId(community.getRegionId());
            vo.setAddress(community.getAddress());
            vo.setLatitude(community.getLatitude());
            vo.setLongitude(community.getLongitude());
            RegionStreetVo street = streetMap.get(community.getRegionId());
            if (street != null) {
                vo.setStreetName(street.getName());
            }
            list.add(vo);
        }
        return list;
    }

    private void fillRegion(CommunityListedVo vo, Integer regionId, Map<Integer, RegionStreetVo> streetMap) {
        RegionStreetVo street = streetMap.get(regionId);
        if (street == null) {
            return;
        }
        vo.setCityName(street.getCityName());
        vo.setDistrictName(street.getDistrictName());
        vo.setStreetName(street.getName());
    }

    private void fillRegion(CommunityDetailVo vo, Integer regionId, Map<Integer, RegionStreetVo> streetMap) {
        RegionStreetVo street = streetMap.get(regionId);
        if (street == null) {
            return;
        }
        vo.setCityName(street.getCityName());
        vo.setDistrictName(street.getDistrictName());
        vo.setStreetName(street.getName());
    }

    private Map<Integer, Integer> areaCountMap() {
        QueryWrapper<Area> qw = new QueryWrapper<>();
        qw.select("community_id", "COUNT(*) AS cnt").isNull("delete_time").groupBy("community_id");
        List<Map<String, Object>> rows = this.areaMapper.selectMaps(qw);
        Map<Integer, Integer> map = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Object communityId = row.get("community_id");
            Object cnt = row.get("cnt");
            if (communityId instanceof Number && cnt instanceof Number) {
                map.put(((Number) communityId).intValue(), ((Number) cnt).intValue());
            }
        }
        return map;
    }

    private void checkLocation(BigDecimal latitude, BigDecimal longitude) {
        if ((latitude == null) != (longitude == null)) {
            throw new OperateException("\u7ecf\u7eac\u5ea6\u9700\u8981\u540c\u65f6\u586b\u5199\u6216\u540c\u65f6\u7559\u7a7a");
        }
    }

    public Map<String, Object> stats() {
        QueryWrapper<Community> qw = new QueryWrapper<Community>();
        qw.isNull("delete_time");
        List<Community> all = this.communityMapper.selectList((Wrapper)qw);
        BigDecimal totalEarnings = BigDecimal.ZERO;
        BigDecimal totalBalance = BigDecimal.ZERO;
        for (Community c : all) {
            totalEarnings = totalEarnings.add(c.getTotalEarnings());
            totalBalance = totalBalance.add(c.getBalance());
        }
        Long earningCount = this.communityEarningLogMapper.selectCount(new QueryWrapper<CommunityEarningLog>());
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("communityCount", all.size());
        map.put("totalEarnings", totalEarnings);
        map.put("totalBalance", totalBalance);
        map.put("earningCount", earningCount);
        return map;
    }
}
