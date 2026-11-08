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
import com.mdd.common.entity.admin.Admin;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper
extends IBaseMapper<Admin> {
    @Select(value={"SELECT * FROM la_admin admin INNER JOIN la_admin_dept lad ON admin.id = lad.admin_id WHERE lad.dept_id = #{deptId} AND admin.delete_time IS NULL"})
    public List<Admin> getByDept(@Param(value="deptId") Integer var1);

    @Select(value={"SELECT * FROM la_admin admin INNER JOIN la_admin_jobs lad ON admin.id = lad.admin_id WHERE lad.jobs_id = #{jobId} AND admin.delete_time IS NULL"})
    public List<Admin> getByJobs(@Param(value="jobId") Integer var1);

    @Select(value={"SELECT count(*) FROM la_admin admin INNER JOIN la_admin_role lad ON admin.id = lad.admin_id WHERE lad.role_id = #{roleId} AND admin.delete_time IS NULL"})
    public Integer getCountByRoleId(@Param(value="roleId") Integer var1);
}

