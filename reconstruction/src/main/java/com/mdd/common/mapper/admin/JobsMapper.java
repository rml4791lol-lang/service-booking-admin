/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.ibatis.annotations.Mapper
 *  org.apache.ibatis.annotations.Param
 *  org.apache.ibatis.annotations.Select
 */
package com.mdd.common.mapper.admin;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.admin.Jobs;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface JobsMapper
extends IBaseMapper<Jobs> {
    @Select(value={"SELECT * FROM la_jobs jobs INNER JOIN la_admin_jobs laj ON laj.jobs_id = jobs.id WHERE laj.admin_id = #{adminId} AND jobs.delete_time IS NULL"})
    public List<Jobs> getByAdminId(@Param(value="adminId") Integer var1);
}

