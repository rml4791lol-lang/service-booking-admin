/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  org.apache.ibatis.annotations.Mapper
 */
package com.mdd.common.mapper.log;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.log.UserAccountLog;
import com.mdd.common.entity.user.User;
import com.mdd.common.enums.AccountLogEnum;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.SpringUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAccountLogMapper
extends IBaseMapper<UserAccountLog> {
    default public void add(Integer userId, Integer changeType, BigDecimal changeAmount, Integer sourceId, String sourceSn, String remark, String extra) {
        UserMapper userMapper;
        User user;
        for (int i = 0; i <= 0 && !StringUtils.isNull(user = (User)(userMapper = SpringUtils.getBean(UserMapper.class)).selectById(userId)); ++i) {
            Integer changeObject = AccountLogEnum.getChangeObject(changeType);
            BigDecimal leftAmount = user.getUserMoney().add(changeAmount);
            UserAccountLog logMoney = new UserAccountLog();
            logMoney.setSn(this.randMakeOrderSn());
            logMoney.setUserId(userId);
            logMoney.setSourceSn(sourceSn);
            logMoney.setChangeObject(changeObject);
            logMoney.setChangeType(changeType);
            logMoney.setChangeAmount(changeAmount);
            logMoney.setLeftAmount(leftAmount);
            logMoney.setAction(1);
            logMoney.setRemark(remark);
            logMoney.setExtra(extra);
            logMoney.setCreateTime(System.currentTimeMillis() / 1000L);
            logMoney.setUpdateTime(System.currentTimeMillis() / 1000L);
            this.insert(logMoney);
        }
    }

    default public void dec(Integer userId, Integer changeType, BigDecimal changeAmount, Integer sourceId, String sourceSn, String remark, String extra) {
        UserMapper userMapper;
        User user;
        for (int i = 0; i <= 0 && !StringUtils.isNull(user = (User)(userMapper = SpringUtils.getBean(UserMapper.class)).selectById(userId)); ++i) {
            BigDecimal leftAmount = user.getUserMoney().subtract(changeAmount);
            UserAccountLog logMoney = new UserAccountLog();
            logMoney.setSn(this.randMakeOrderSn());
            logMoney.setUserId(userId);
            logMoney.setSourceSn(sourceSn);
            logMoney.setChangeType(changeType);
            logMoney.setChangeAmount(changeAmount);
            logMoney.setLeftAmount(leftAmount);
            logMoney.setAction(2);
            logMoney.setRemark(remark);
            logMoney.setExtra(extra);
            logMoney.setCreateTime(System.currentTimeMillis() / 1000L);
            logMoney.setUpdateTime(System.currentTimeMillis() / 1000L);
            this.insert(logMoney);
        }
    }

    default public String randMakeOrderSn() {
        String sn;
        UserAccountLog snModel;
        String date = TimeUtils.timestampToDate(System.currentTimeMillis() / 1000L, "yyyyMMddHHmmss");
        do {
            sn = date + ToolUtils.randomInt(12);
        } while ((snModel = (UserAccountLog)this.selectOne(new QueryWrapper<UserAccountLog>().select(new String[]{"id"}).eq("sn", (Object)sn).last("limit 1"))) != null);
        return sn;
    }
}

