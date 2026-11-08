package com.mdd.admin.service.impl.region;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.region.IAreaService;
import com.mdd.admin.service.region.IRegionService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.region.AreaCreateValidate;
import com.mdd.admin.validate.region.AreaSearchValidate;
import com.mdd.admin.validate.region.AreaUpdateValidate;
import com.mdd.admin.vo.region.AreaListedVo;
import com.mdd.admin.vo.region.AreaOptionVo;
import com.mdd.admin.vo.region.RegionStreetVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.region.Area;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AreaServiceImpl implements IAreaService {
    @Resource
    AreaMapper areaMapper;
    @Resource
    CommunityMapper communityMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    IRegionService regionService;

    @Override
    public PageResult<AreaListedVo> list(PageValidate pageValidate, AreaSearchValidate searchValidate) {
        QueryWrapper<Area> qw = new QueryWrapper<>();
        qw.isNull("delete_time");
        if (StringUtils.isNotEmpty(searchValidate.getKeyword())) {
            String keyword = searchValidate.getKeyword().trim();
            qw.and(w -> w.like("name", keyword).or().like("address", keyword));
        }
        if (searchValidate.getCommunityId() != null && searchValidate.getCommunityId() > 0) {
            qw.eq("community_id", searchValidate.getCommunityId());
        }
        if (searchValidate.getRegionId() != null && searchValidate.getRegionId() > 0) {
            List<Integer> communityIds = communityIdsByRegion(searchValidate.getRegionId());
            if (communityIds.isEmpty()) {
                qw.eq("id", -1);
            } else {
                qw.in("community_id", communityIds);
            }
        }
        if (searchValidate.getStatus() != null) {
            qw.eq("status", searchValidate.getStatus());
        }
        qw.orderByAsc("sort").orderByAsc("id");

        int pageNo = pageValidate.getPage_no();
        int pageSize = pageValidate.getPage_size();
        IPage<Area> iPage = areaMapper.selectPage(new Page<>(pageNo, pageSize), qw);

        Map<Integer, Community> communityMap = communityMap();
        Map<Integer, RegionStreetVo> streetMap = regionService.streetMap();
        Map<Integer, Integer> classCountMap = classCountMap();

        List<AreaListedVo> list = new ArrayList<>();
        for (Area area : iPage.getRecords()) {
            list.add(buildVo(area, communityMap, streetMap, classCountMap));
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), (long) pageSize, list);
    }

    @Override
    public AreaListedVo detail(Integer id) {
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleteTime() != null) {
            throw new OperateException("小区不存在");
        }
        Map<Integer, Integer> classCountMap = new HashMap<>();
        Long count = courseClassMapper.selectCount(
                new QueryWrapper<CourseClass>().eq("area_id", id).eq("is_delete", 0)
        );
        classCountMap.put(id, count == null ? 0 : count.intValue());
        return buildVo(area, communityMap(), regionService.streetMap(), classCountMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer add(AreaCreateValidate createValidate) {
        Community community = checkCommunity(createValidate.getCommunityId());
        checkLocation(createValidate.getLat(), createValidate.getLng());
        String name = createValidate.getName().trim();
        checkDuplicateName(createValidate.getCommunityId(), name, null);

        long now = System.currentTimeMillis() / 1000L;
        Area area = new Area();
        area.setCommunityId(community == null ? 0 : community.getId());
        area.setName(name);
        area.setAddress(StringUtils.trim(createValidate.getAddress()));
        area.setLat(createValidate.getLat());
        area.setLng(createValidate.getLng());
        area.setCover(StringUtils.trim(createValidate.getCover()));
        area.setTags(StringUtils.trim(createValidate.getTags()));
        area.setDistanceText(StringUtils.trim(createValidate.getDistance()));
        area.setSort(createValidate.getSort() == null ? 0 : createValidate.getSort());
        area.setStatus(createValidate.getStatus() == null ? 1 : createValidate.getStatus());
        area.setRemark(StringUtils.trim(createValidate.getRemark()));
        area.setCreateTime(now);
        area.setUpdateTime(now);
        areaMapper.insert(area);
        return area.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(AreaUpdateValidate updateValidate) {
        Area area = areaMapper.selectById(updateValidate.getId());
        if (area == null || area.getDeleteTime() != null) {
            throw new OperateException("小区不存在");
        }
        Community community = checkCommunity(updateValidate.getCommunityId());
        checkLocation(updateValidate.getLat(), updateValidate.getLng());
        String name = updateValidate.getName().trim();
        checkDuplicateName(updateValidate.getCommunityId(), name, area.getId());

        area.setCommunityId(community == null ? 0 : community.getId());
        area.setName(name);
        area.setAddress(StringUtils.trim(updateValidate.getAddress()));
        area.setLat(updateValidate.getLat());
        area.setLng(updateValidate.getLng());
        area.setCover(StringUtils.trim(updateValidate.getCover()));
        area.setTags(StringUtils.trim(updateValidate.getTags()));
        area.setDistanceText(StringUtils.trim(updateValidate.getDistance()));
        area.setSort(updateValidate.getSort() == null ? 0 : updateValidate.getSort());
        area.setStatus(updateValidate.getStatus() == null ? 1 : updateValidate.getStatus());
        area.setRemark(StringUtils.trim(updateValidate.getRemark()));
        area.setUpdateTime(System.currentTimeMillis() / 1000L);
        areaMapper.updateById(area);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Integer id) {
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleteTime() != null) {
            throw new OperateException("小区不存在");
        }
        Long bound = courseClassMapper.selectCount(
                new QueryWrapper<CourseClass>().eq("area_id", id).eq("is_delete", 0)
        );
        if (bound != null && bound > 0) {
            throw new OperateException("该小区下还有 " + bound + " 个班级，请先调整班级绑定");
        }
        area.setDeleteTime(System.currentTimeMillis() / 1000L);
        areaMapper.updateById(area);
    }

    @Override
    public List<AreaOptionVo> all(Integer communityId, Integer status) {
        QueryWrapper<Area> qw = new QueryWrapper<>();
        qw.isNull("delete_time");
        if (communityId != null && communityId > 0) {
            qw.eq("community_id", communityId);
        }
        if (status != null) {
            qw.eq("status", status);
        }
        qw.orderByAsc("sort").orderByAsc("id");
        List<Area> areas = areaMapper.selectList(qw);

        Map<Integer, Community> communityMap = communityMap();
        Map<Integer, RegionStreetVo> streetMap = regionService.streetMap();
        List<AreaOptionVo> list = new ArrayList<>();
        for (Area area : areas) {
            AreaOptionVo vo = new AreaOptionVo();
            vo.setId(area.getId());
            vo.setName(area.getName());
            vo.setCommunityId(area.getCommunityId());
            vo.setAddress(area.getAddress());
            vo.setLat(area.getLat());
            vo.setLng(area.getLng());
            vo.setStatus(area.getStatus());
            Community community = communityMap.get(area.getCommunityId());
            if (community != null) {
                vo.setCommunityName(community.getName());
                RegionStreetVo street = streetMap.get(community.getRegionId());
                if (street != null) {
                    vo.setStreetName(street.getName());
                }
            }
            list.add(vo);
        }
        return list;
    }

    private AreaListedVo buildVo(Area area, Map<Integer, Community> communityMap,
                                 Map<Integer, RegionStreetVo> streetMap, Map<Integer, Integer> classCountMap) {
        AreaListedVo vo = new AreaListedVo();
        vo.setId(area.getId());
        vo.setCommunityId(area.getCommunityId());
        vo.setName(area.getName());
        vo.setAddress(area.getAddress());
        vo.setLat(area.getLat());
        vo.setLng(area.getLng());
        vo.setCover(UrlUtils.toAdminAbsoluteUrl(area.getCover()));
        vo.setTagsText(area.getTags());
        vo.setTags(splitTags(area.getTags()));
        vo.setDistance(area.getDistanceText());
        vo.setSort(area.getSort());
        vo.setStatus(area.getStatus());
        vo.setRemark(area.getRemark());
        vo.setClassCount(classCountMap.getOrDefault(area.getId(), 0));
        if (area.getCreateTime() != null) {
            vo.setCreateTime(TimeUtils.timestampToDate(area.getCreateTime()));
        }
        Community community = communityMap.get(area.getCommunityId());
        if (community != null) {
            vo.setCommunityName(community.getName());
            vo.setRegionId(community.getRegionId());
            RegionStreetVo street = streetMap.get(community.getRegionId());
            if (street != null) {
                vo.setStreetName(street.getName());
                vo.setDistrictId(street.getDistrictId());
                vo.setDistrictName(street.getDistrictName());
            }
        }
        return vo;
    }

    private Map<Integer, Community> communityMap() {
        List<Community> communities = communityMapper.selectList(
                new QueryWrapper<Community>().isNull("delete_time")
        );
        Map<Integer, Community> map = new HashMap<>();
        for (Community community : communities) {
            map.put(community.getId(), community);
        }
        return map;
    }

    private Map<Integer, Integer> classCountMap() {
        QueryWrapper<CourseClass> qw = new QueryWrapper<>();
        qw.select("area_id", "COUNT(*) AS cnt")
                .eq("is_delete", 0)
                .gt("area_id", 0)
                .groupBy("area_id");
        List<Map<String, Object>> rows = courseClassMapper.selectMaps(qw);
        Map<Integer, Integer> map = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Object areaId = row.get("area_id");
            Object cnt = row.get("cnt");
            if (areaId instanceof Number && cnt instanceof Number) {
                map.put(((Number) areaId).intValue(), ((Number) cnt).intValue());
            }
        }
        return map;
    }

    private List<Integer> communityIdsByRegion(Integer regionId) {
        List<Community> communities = communityMapper.selectList(
                new QueryWrapper<Community>().eq("region_id", regionId).isNull("delete_time")
        );
        List<Integer> ids = new ArrayList<>();
        for (Community community : communities) {
            ids.add(community.getId());
        }
        return ids;
    }

    private Community checkCommunity(Integer communityId) {
        if (communityId == null || communityId <= 0) {
            return null;
        }
        Community community = communityMapper.selectById(communityId);
        if (community == null || community.getDeleteTime() != null) {
            throw new OperateException("所属社区不存在");
        }
        return community;
    }

    private void checkLocation(BigDecimal lat, BigDecimal lng) {
        if ((lat == null) != (lng == null)) {
            throw new OperateException("经纬度需要同时填写或同时留空");
        }
    }

    private void checkDuplicateName(Integer communityId, String name, Integer excludeId) {
        QueryWrapper<Area> qw = new QueryWrapper<>();
        qw.eq("community_id", communityId == null ? 0 : communityId)
                .eq("name", name)
                .isNull("delete_time");
        if (excludeId != null) {
            qw.ne("id", excludeId);
        }
        Long count = areaMapper.selectCount(qw);
        if (count != null && count > 0) {
            throw new OperateException("该社区下已存在同名小区");
        }
    }

    private List<String> splitTags(String tags) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(tags)) {
            return list;
        }
        String normalized = tags.replace('，', ',').replace('；', ',').replace(';', ',');
        for (String tag : Arrays.asList(normalized.split(","))) {
            String value = tag.trim();
            if (!value.isEmpty() && !list.contains(value)) {
                list.add(value);
            }
        }
        return list;
    }
}
