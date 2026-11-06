/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.ibatis.annotations.Mapper
 */
package com.mdd.common.mapper;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.Instructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface InstructorMapper
extends IBaseMapper<Instructor> {

    /** 原子累加教练余额，避免课后总结/人工扣课并发时读改写丢账。 */
    @Update("UPDATE la_instructor SET balance = COALESCE(balance, 0) + #{amount}, "
            + "update_time = NOW() WHERE id = #{instructorId} AND delete_time IS NULL")
    int addBalance(@Param("instructorId") Integer instructorId,
                   @Param("amount") BigDecimal amount);

    /** 人工扣课结算：余额、带课数一次原子累加，避免并发覆盖。 */
    @Update("UPDATE la_instructor SET balance = COALESCE(balance, 0) + #{amount}, "
            + "class_count = COALESCE(class_count, 0) + 1, update_time = NOW() "
            + "WHERE id = #{instructorId} AND delete_time IS NULL")
    int addBalanceAndClassCount(@Param("instructorId") Integer instructorId,
                                @Param("amount") BigDecimal amount);

    @Update("UPDATE la_instructor SET balance = COALESCE(balance, 0) + #{amount}, "
            + "class_count = COALESCE(class_count, 0) + 1, update_time = NOW() "
            + "WHERE id = #{id} AND delete_time IS NULL")
    int addManualLessonSettlement(@Param("id") Integer id, @Param("amount") BigDecimal amount);
}
