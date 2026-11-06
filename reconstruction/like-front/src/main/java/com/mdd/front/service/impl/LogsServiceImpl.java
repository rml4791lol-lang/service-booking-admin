/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.log.UserAccountLog
 *  com.mdd.common.enums.LogMoneyEnum
 *  com.mdd.common.mapper.log.UserAccountLogMapper
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.front.service.ILogsService
 *  com.mdd.front.service.impl.LogsServiceImpl
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.vo.RechargeRecordVo
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.log.UserAccountLog;
import com.mdd.common.enums.LogMoneyEnum;
import com.mdd.common.mapper.log.UserAccountLogMapper;
import com.mdd.common.util.TimeUtils;
import com.mdd.front.service.ILogsService;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.vo.RechargeRecordVo;
import java.util.LinkedList;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LogsServiceImpl
implements ILogsService {
    @Resource
    UserAccountLogMapper userAccountLogMapper;

    public PageResult<RechargeRecordVo> userMoney(PageValidate pageValidate, Integer userId, Integer type) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        QueryWrapper<UserAccountLog> queryWrapper = new QueryWrapper<UserAccountLog>();
        queryWrapper.eq("user_id", (Object)userId);
        queryWrapper.orderByDesc("id");
        if (type > 0) {
            queryWrapper.eq("action", (Object)type);
        }
        IPage<UserAccountLog> iPage = this.userAccountLogMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), (Wrapper)queryWrapper);
        LinkedList<RechargeRecordVo> list = new LinkedList<RechargeRecordVo>();
        for (UserAccountLog logMoney : iPage.getRecords()) {
            RechargeRecordVo vo = new RechargeRecordVo();
            vo.setId(logMoney.getId());
            vo.setAction(logMoney.getAction());
            vo.setOrderAmount(logMoney.getChangeAmount());
            vo.setTips(LogMoneyEnum.getMsgByCode((Integer)logMoney.getChangeType()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)logMoney.getCreateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }
}

