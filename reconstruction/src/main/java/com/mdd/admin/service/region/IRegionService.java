package com.mdd.admin.service.region;

import com.mdd.admin.vo.region.RegionStreetVo;

import java.util.List;
import java.util.Map;

/**
 * 地区只读服务（后台社区/小区表单使用）。
 */
public interface IRegionService {
    List<RegionStreetVo> streets();

    Map<Integer, RegionStreetVo> streetMap();
}
