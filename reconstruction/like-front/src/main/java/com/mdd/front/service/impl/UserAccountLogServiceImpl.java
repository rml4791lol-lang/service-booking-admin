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
 *  com.mdd.common.enums.AccountLogEnum
 *  com.mdd.common.mapper.log.UserAccountLogMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.front.service.IUserAccountLogService
 *  com.mdd.front.service.impl.UserAccountLogServiceImpl
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.validate.users.UserAccountLogSearchValidate
 *  com.mdd.front.vo.user.UserAccountListVo
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.log.UserAccountLog;
import com.mdd.common.enums.AccountLogEnum;
import com.mdd.common.mapper.log.UserAccountLogMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.front.service.IUserAccountLogService;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.validate.users.UserAccountLogSearchValidate;
import com.mdd.front.vo.user.UserAccountListVo;
import java.util.Collection;
import java.util.LinkedList;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserAccountLogServiceImpl
implements IUserAccountLogService {
    @Resource
    UserAccountLogMapper userAccountLogMapper;

    public PageResult<UserAccountListVo> lists(PageValidate pageValidate, UserAccountLogSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        QueryWrapper<UserAccountLog> queryWrapper = new QueryWrapper<UserAccountLog>();
        queryWrapper.isNull("delete_time");
        queryWrapper.eq("user_id", (Object)searchValidate.getUserId());
        if (StringUtils.isNotEmpty((String)searchValidate.getType()) && searchValidate.getType().equals("um")) {
            queryWrapper.in("change_type", (Collection)AccountLogEnum.getUserMoneyChangeType());
        }
        if (StringUtils.isNotNull(searchValidate.getAction())) {
            queryWrapper.eq("action", (Object)searchValidate.getAction());
        }
        queryWrapper.orderByDesc("id");
        IPage<UserAccountLog> iPage = this.userAccountLogMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), (Wrapper)queryWrapper);
        LinkedList<UserAccountListVo> list = new LinkedList<UserAccountListVo>();
        for (UserAccountLog item : iPage.getRecords()) {
            UserAccountListVo vo = new UserAccountListVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            vo.setTypeDesc(AccountLogEnum.getChangeTypeDesc((Integer)item.getChangeType()));
            String symbol = item.getAction().equals(AccountLogEnum.DEC.getCode()) ? "-" : "+";
            vo.setChangeAmountDesc(symbol + item.getChangeAmount());
            vo.setCreateTime(TimeUtils.timestampToDate((Long)item.getCreateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }
}

