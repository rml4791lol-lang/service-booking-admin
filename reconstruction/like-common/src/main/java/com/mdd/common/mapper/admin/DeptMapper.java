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
import com.mdd.common.entity.admin.Dept;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DeptMapper
extends IBaseMapper<Dept> {
    @Select(value={"SELECT * FROM la_dept dept INNER JOIN la_admin_dept lad ON lad.dept_id = dept.id WHERE lad.admin_id = #{adminId} AND dept.delete_time IS NULL"})
    public List<Dept> getByAdminId(@Param(value="adminId") Integer var1);
}

