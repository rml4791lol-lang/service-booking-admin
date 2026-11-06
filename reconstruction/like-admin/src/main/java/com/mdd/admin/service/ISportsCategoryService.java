/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISportsCategoryService
 *  com.mdd.admin.validate.course.SportsCategoryCreateValidate
 *  com.mdd.common.entity.course.SportsCategory
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.course.SportsCategoryCreateValidate;
import com.mdd.admin.validate.course.SportsCategoryUpdateValidate;
import com.mdd.common.entity.course.SportsCategory;
import java.util.List;

public interface ISportsCategoryService {
    /**
     * 类目列表。
     *
     * @param includeHidden 是否包含隐藏项
     * @param asTree        是否返回嵌套树；false 时保持原有扁平结构
     */
    public List<SportsCategory> listAll(boolean includeHidden, boolean asTree);

    public void add(SportsCategoryCreateValidate var1);

    public void edit(SportsCategoryUpdateValidate var1);
}
