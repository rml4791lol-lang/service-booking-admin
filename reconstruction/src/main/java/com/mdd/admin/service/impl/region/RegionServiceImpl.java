package com.mdd.admin.service.impl.region;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.region.IRegionService;
import com.mdd.admin.vo.region.RegionStreetVo;
import com.mdd.common.entity.region.Region;
import com.mdd.common.mapper.region.RegionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegionServiceImpl implements IRegionService {
    @Resource
    RegionMapper regionMapper;

    @Override
    public List<RegionStreetVo> streets() {
        List<Region> regions = regionMapper.selectList(
                new QueryWrapper<Region>().isNull("delete_time").orderByAsc("sort").orderByAsc("id")
        );
        Map<Integer, Region> byId = new LinkedHashMap<>();
        for (Region region : regions) {
            byId.put(region.getId(), region);
        }

        List<RegionStreetVo> streets = new ArrayList<>();
        for (Region region : regions) {
            if (!Integer.valueOf(3).equals(region.getLevel())) {
                continue;
            }
            RegionStreetVo vo = new RegionStreetVo();
            vo.setId(region.getId());
            vo.setName(region.getName());
            vo.setSort(region.getSort());
            Region district = byId.get(region.getParentId());
            if (district != null) {
                vo.setDistrictId(district.getId());
                vo.setDistrictName(district.getName());
                Region city = byId.get(district.getParentId());
                if (city != null) {
                    vo.setCityId(city.getId());
                    vo.setCityName(city.getName());
                    vo.setFullName(city.getName() + district.getName() + region.getName());
                } else {
                    vo.setFullName(district.getName() + region.getName());
                }
            } else {
                vo.setFullName(region.getName());
            }
            streets.add(vo);
        }
        streets.sort(Comparator
                .comparing((RegionStreetVo s) -> s.getCityId() == null ? 0 : s.getCityId())
                .thenComparing(s -> s.getDistrictId() == null ? 0 : s.getDistrictId())
                .thenComparing(s -> s.getSort() == null ? 0 : s.getSort())
                .thenComparing(s -> s.getId() == null ? 0 : s.getId()));
        return streets;
    }

    @Override
    public Map<Integer, RegionStreetVo> streetMap() {
        Map<Integer, RegionStreetVo> map = new HashMap<>();
        for (RegionStreetVo street : streets()) {
            map.put(street.getId(), street);
        }
        return map;
    }
}
