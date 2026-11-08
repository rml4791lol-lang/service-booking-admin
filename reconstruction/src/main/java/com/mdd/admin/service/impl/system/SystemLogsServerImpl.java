/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.github.yulichang.interfaces.MPJBaseJoin
 *  com.github.yulichang.query.MPJQueryWrapper
 *  com.mdd.admin.service.impl.system.SystemLogsServerImpl
 *  com.mdd.admin.service.system.ISystemLogsServer
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.SystemSearchLoginsValidate
 *  com.mdd.admin.validate.system.SystemSearchOperateValidate
 *  com.mdd.admin.vo.system.SystemLogLoginVo
 *  com.mdd.admin.vo.system.SystemLogOperateVo
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.system.OperationLog
 *  com.mdd.common.entity.system.SystemLogLogin
 *  com.mdd.common.mapper.system.SystemLogLoginMapper
 *  com.mdd.common.mapper.system.SystemLogOperateMapper
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl.system;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.MPJQueryWrapper;
import com.mdd.admin.service.system.ISystemLogsServer;
import com.mdd.admin.aop.aspect.SensitiveDataMasker;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.SystemSearchLoginsValidate;
import com.mdd.admin.validate.system.SystemSearchOperateValidate;
import com.mdd.admin.vo.system.SystemLogLoginVo;
import com.mdd.admin.vo.system.SystemLogOperateVo;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.system.OperationLog;
import com.mdd.common.entity.system.SystemLogLogin;
import com.mdd.common.mapper.system.SystemLogLoginMapper;
import com.mdd.common.mapper.system.SystemLogOperateMapper;
import com.mdd.common.util.TimeUtils;
import java.util.LinkedList;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SystemLogsServerImpl
implements ISystemLogsServer {
    @Resource
    SystemLogOperateMapper logOperateMapper;
    @Resource
    SystemLogLoginMapper logLoginMapper;

    public PageResult<SystemLogOperateVo> operate(PageValidate pageValidate, SystemSearchOperateValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        MPJQueryWrapper<OperationLog> mpjQueryWrapper = (new MPJQueryWrapper<OperationLog>().selectAll(OperationLog.class).select(new String[]{"sa.account as username,sa.name nickname"}).leftJoin("?_admin sa ON sa.id=t.admin_id".replace("?_", GlobalConfig.tablePrefix))).orderByDesc("id");
        this.logOperateMapper.setSearch(mpjQueryWrapper, (Object)searchValidate, new String[]{"like:action:str", "like:username@sa.name:str", "like:ip:str", "=:type:str", "=:url:str", "datetime:start_time-end_time@t.create_time:str"});
        IPage<SystemLogOperateVo> iPage = this.logOperateMapper.selectJoinPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), SystemLogOperateVo.class, (MPJBaseJoin)mpjQueryWrapper);
        for (SystemLogOperateVo vo : iPage.getRecords()) {
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            // 历史数据迁移前的兜底：即使库中仍有旧日志，接口也不返回原始敏感请求体。
            vo.setParams(SensitiveDataMasker.maskJson(vo.getParams()));
            vo.setResult(SensitiveDataMasker.maskJson(vo.getResult()));
        }
        return PageResult.iPageHandle((IPage)iPage);
    }

    public PageResult<SystemLogLoginVo> login(PageValidate pageValidate, SystemSearchLoginsValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        QueryWrapper<SystemLogLogin> queryWrapper = new QueryWrapper<SystemLogLogin>();
        queryWrapper.orderByDesc("id");
        this.logLoginMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"like:username:str", "=:status:int", "datetime:startTime-endTime@create_time:str"});
        IPage<SystemLogLogin> iPage = this.logLoginMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), (Wrapper)queryWrapper);
        LinkedList<SystemLogLoginVo> list = new LinkedList<SystemLogLoginVo>();
        for (SystemLogLogin item : iPage.getRecords()) {
            SystemLogLoginVo vo = new SystemLogLoginVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((Long)item.getCreateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }
}
