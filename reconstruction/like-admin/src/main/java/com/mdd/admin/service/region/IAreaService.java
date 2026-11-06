package com.mdd.admin.service.region;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.region.AreaCreateValidate;
import com.mdd.admin.validate.region.AreaSearchValidate;
import com.mdd.admin.validate.region.AreaUpdateValidate;
import com.mdd.admin.vo.region.AreaListedVo;
import com.mdd.admin.vo.region.AreaOptionVo;
import com.mdd.common.core.PageResult;

import java.util.List;

public interface IAreaService {
    PageResult<AreaListedVo> list(PageValidate pageValidate, AreaSearchValidate searchValidate);

    AreaListedVo detail(Integer id);

    Integer add(AreaCreateValidate createValidate);

    void edit(AreaUpdateValidate updateValidate);

    void del(Integer id);

    List<AreaOptionVo> all(Integer communityId, Integer status);
}
