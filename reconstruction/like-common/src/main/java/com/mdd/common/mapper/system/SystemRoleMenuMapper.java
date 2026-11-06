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
import com.mdd.common.entity.system.SystemRoleMenu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SystemRoleMenuMapper
extends IBaseMapper<SystemRoleMenu> {
    @Select(value={"SELECT DISTINCT menuR.menu_id FROM la_system_role_menu menuR INNER JOIN la_system_role role ON menuR.role_id = role.id INNER JOIN la_admin_role lar ON lar.role_id = role.id WHERE lar.admin_id = #{adminId} AND role.delete_time IS NULL"})
    public List<Integer> getMenuIds(@Param(value="adminId") Integer var1);
}

