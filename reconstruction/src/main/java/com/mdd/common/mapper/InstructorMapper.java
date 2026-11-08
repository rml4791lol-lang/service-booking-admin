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
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface InstructorMapper
extends IBaseMapper<Instructor> {

    /**
     * 提现申请余额条件扣减：余额判断与扣减在同一条 UPDATE 内完成，并发申请由数据库行锁
     * 串行化，返回 0 表示余额不足（或教练不存在/已删除）。
     */
    @Update("UPDATE la_instructor SET balance = COALESCE(balance, 0) - #{amount}, "
            + "update_time = NOW() WHERE id = #{instructorId} AND delete_time IS NULL "
            + "AND COALESCE(balance, 0) >= #{amount}")
    int deductBalance(@Param("instructorId") Integer instructorId,
                      @Param("amount") BigDecimal amount);

    /** 读取当前余额（独立语句，避免同事务一级缓存返回扣减前快照）。 */
    @Select("SELECT balance FROM la_instructor WHERE id = #{instructorId}")
    BigDecimal selectBalance(@Param("instructorId") Integer instructorId);

    /** 提现审核通过：累计提现金额原子累加，避免并发审核读改写丢账。 */
    @Update("UPDATE la_instructor SET total_withdrawn = COALESCE(total_withdrawn, 0) + #{amount}, "
            + "update_time = NOW() WHERE id = #{instructorId} AND delete_time IS NULL")
    int addTotalWithdrawn(@Param("instructorId") Integer instructorId,
                          @Param("amount") BigDecimal amount);

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
