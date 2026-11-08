/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.ibatis.annotations.Mapper
 *  org.apache.ibatis.annotations.Param
 *  org.apache.ibatis.annotations.Select
 */
package com.mdd.common.mapper.system;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.system.SystemRole;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SystemRoleMapper
extends IBaseMapper<SystemRole> {
    @Select(value={"SELECT * FROM la_system_role role INNER JOIN la_admin_role lar ON lar.role_id = role.id WHERE lar.admin_id = #{adminId} AND role.delete_time IS NULL"})
    public List<SystemRole> getByAdminId(@Param(value="adminId") Integer var1);
}

