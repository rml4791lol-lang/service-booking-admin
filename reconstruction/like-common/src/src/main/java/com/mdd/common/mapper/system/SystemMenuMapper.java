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
import com.mdd.common.entity.system.SystemMenu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SystemMenuMapper
extends IBaseMapper<SystemMenu> {
    @Select(value={"<script>", " SELECT ", " perms ", " FROM la_system_menu WHERE perms != '' AND id in ", "<foreach item='item' index='index' collection='items' open='(' separator=',' close=')'>", "#{item}", "</foreach>", "</script>"})
    public List<String> getPerms(@Param(value="items") List<Integer> var1);
}

