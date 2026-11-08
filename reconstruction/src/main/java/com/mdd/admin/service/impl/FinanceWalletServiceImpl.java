/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.github.yulichang.interfaces.MPJBaseJoin
 *  com.github.yulichang.query.MPJQueryWrapper
 *  com.mdd.admin.service.IFinanceWalletService
 *  com.mdd.admin.service.impl.FinanceWalletServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.finance.FinanceWalletSearchValidate
 *  com.mdd.admin.vo.finance.FinanceWalletListVo
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.log.UserAccountLog
 *  com.mdd.common.enums.LogMoneyEnum
 *  com.mdd.common.mapper.log.UserAccountLogMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.MPJQueryWrapper;
import com.mdd.admin.service.IFinanceWalletService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceWalletSearchValidate;
import com.mdd.admin.vo.finance.FinanceWalletListVo;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.log.UserAccountLog;
import com.mdd.common.enums.LogMoneyEnum;
import com.mdd.common.mapper.log.UserAccountLogMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FinanceWalletServiceImpl
implements IFinanceWalletService {
    @Resource
    UserAccountLogMapper logMoneyMapper;

    public PageResult<FinanceWalletListVo> list(PageValidate pageValidate, FinanceWalletSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        MPJQueryWrapper<UserAccountLog> mpjQueryWrapper = new MPJQueryWrapper<>();
        (mpjQueryWrapper.selectAll(UserAccountLog.class).select(new String[]{"U.id as user_id,U.sn as sn,U.nickname,U.avatar, U.account, U.mobile"}).leftJoin("?_user U ON U.id=t.user_id".replace("?_", GlobalConfig.tablePrefix))).orderByDesc("id");
        this.logMoneyMapper.setSearch(mpjQueryWrapper, (Object)searchValidate, new String[]{"=:change_type@t.change_type:int", "datetime:start_time-end_time@t.create_time:str"});
        if (StringUtils.isNotEmpty((String)searchValidate.getUser_info())) {
            String keyword = searchValidate.getUser_info();
            mpjQueryWrapper.nested(wq -> ((((((wq.like("U.nickname", (Object)keyword)).or()).like("U.account", (Object)keyword)).or()).like("U.sn", (Object)keyword)).or()).like("U.mobile", (Object)keyword));
        }
        IPage<FinanceWalletListVo> iPage = this.logMoneyMapper.selectJoinPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), FinanceWalletListVo.class, (MPJBaseJoin)mpjQueryWrapper);
        for (FinanceWalletListVo vo : iPage.getRecords()) {
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            vo.setChangeTypeDesc(LogMoneyEnum.getMsgByCode((Integer)Integer.parseInt(vo.getChangeType())));
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)vo.getAvatar()));
        }
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("changeType", LogMoneyEnum.getTypeList());
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), (List)iPage.getRecords(), extend);
    }

    public Map<Integer, String> getUmChangeType() {
        return LogMoneyEnum.getTypeList();
    }
}

