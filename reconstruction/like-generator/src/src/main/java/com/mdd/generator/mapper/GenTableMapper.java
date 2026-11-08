/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.basics.IBaseMapper
 *  org.apache.ibatis.annotations.Mapper
 *  org.apache.ibatis.annotations.Select
 */
package com.mdd.generator.mapper;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.generator.entity.GenTable;
import com.mdd.generator.entity.GenTableColumn;
import com.mdd.generator.vo.DbTableVo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GenTableMapper
extends IBaseMapper<GenTable> {
    @Select(value={"<script>", "SELECT table_name, table_comment, create_time, update_time FROM information_schema.tables WHERE table_schema = (SELECT database()) AND table_name NOT LIKE 'qrtz_%' AND table_name NOT LIKE 'gen_%' AND table_name NOT IN (select table_name from la_gen_table)", "<if test=\"tableName != null and tableName != ''\">AND lower(table_name) like lower(concat('%', #{tableName}, '%'))</if>", "<if test=\"tableComment != null and tableComment != ''\">AND lower(table_comment) like lower(concat('%', #{tableComment}, '%'))</if>", "</script>"})
    public List<DbTableVo> selectDbTableList(Map<String, String> var1);

    @Select(value={"<script>", "SELECT table_name, table_comment, create_time, update_time FROM information_schema.tables WHERE table_schema = (SELECT database()) AND table_name NOT LIKE 'qrtz_%' AND table_name NOT LIKE 'gen_%' <if test=\"tableName != null and tableName != ''\">AND lower(table_name) like lower(concat('%', #{tableName}, '%'))</if>", "<if test=\"tableComment != null and tableComment != ''\">AND lower(table_comment) like lower(concat('%', #{tableComment}, '%'))</if>", "</script>"})
    public List<DbTableVo> selectDbTableAllList(Map<String, String> var1);

    @Select(value={"<script>", "SELECT table_name, table_comment, create_time, update_time FROM information_schema.tables WHERE table_name NOT LIKE 'qrtz_%' AND table_name NOT LIKE 'gen_%' AND table_schema = (select database()) AND table_name IN <foreach collection='array' item='name' open='(' separator=',' close=')'>#{name}</foreach>", "</script>"})
    public List<DbTableVo> selectDbTableListByNames(String[] var1);

    @Select(value={"<script>", "SELECT column_name, (CASE WHEN (is_nullable = 'no' <![CDATA[ && ]]> column_key != 'PRI') THEN '1' ELSE NULL END) AS is_required, (CASE WHEN column_key = 'PRI' THEN '1' ELSE '0' END) AS is_pk, ordinal_position AS sort, column_comment, (CASE WHEN extra = 'auto_increment' THEN '1' ELSE '0' END) AS is_increment, column_type FROM information_schema.columns WHERE table_schema = (SELECT database()) AND table_name = (#{tableName}) ORDER BY ordinal_position", "</script>"})
    public List<GenTableColumn> selectDbTableColumnsByName(String var1);
}

