/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.ibatis.annotations.Mapper
 */
package com.mdd.common.mapper.user;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.user.User;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper
extends IBaseMapper<User> {

    /**
     * 钱包余额条件扣减（#59）：余额判断与扣减在同一条 UPDATE 内完成，
     * 并发下由数据库行锁保证只有余额足够的请求扣成功，返回受影响行数。
     *
     * <p>返回 0 表示余额不足（或用户不存在/已删除），调用方据此给出中文业务提示，
     * 不再使用“先查余额、内存扣减、再整行写回”的读-改-写实现。</p>
     */
    @Update("UPDATE la_user SET user_money = user_money - #{amount}, update_time = #{updateTime} "
            + "WHERE id = #{userId} AND delete_time IS NULL AND user_money >= #{amount}")
    int deductMoney(@Param("userId") Integer userId,
                    @Param("amount") BigDecimal amount,
                    @Param("updateTime") Long updateTime);

    /**
     * 钱包余额增加（活动报名退款，#61）：原子累加，避免读-改-写覆盖并发扣减。
     */
    @Update("UPDATE la_user SET user_money = user_money + #{amount}, update_time = #{updateTime} "
            + "WHERE id = #{userId} AND delete_time IS NULL")
    int addMoney(@Param("userId") Integer userId,
                 @Param("amount") BigDecimal amount,
                 @Param("updateTime") Long updateTime);
}

