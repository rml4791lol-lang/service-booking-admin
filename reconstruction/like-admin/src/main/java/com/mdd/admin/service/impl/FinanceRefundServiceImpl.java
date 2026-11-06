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
 *  com.mdd.admin.service.IFinanceRefundService
 *  com.mdd.admin.service.impl.FinanceRefundServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.finance.FinanceRefundSearchValidate
 *  com.mdd.admin.vo.finance.FinanceRefundListVo
 *  com.mdd.admin.vo.finance.FinanceRefundLogVo
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.RefundLog
 *  com.mdd.common.entity.RefundRecord
 *  com.mdd.common.enums.PaymentEnum
 *  com.mdd.common.enums.RefundEnum
 *  com.mdd.common.mapper.RefundLogMapper
 *  com.mdd.common.mapper.RefundRecordMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.MPJQueryWrapper;
import com.mdd.admin.service.IFinanceRefundService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceRefundSearchValidate;
import com.mdd.admin.vo.finance.FinanceRefundListVo;
import com.mdd.admin.vo.finance.FinanceRefundLogVo;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.RefundLog;
import com.mdd.common.entity.RefundRecord;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.enums.RefundEnum;
import com.mdd.common.mapper.RefundLogMapper;
import com.mdd.common.mapper.RefundRecordMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FinanceRefundServiceImpl
implements IFinanceRefundService {
    @Resource
    RefundRecordMapper refundRecordMapper;
    @Resource
    RefundLogMapper refundLogMapper;

    public PageResult<FinanceRefundListVo> list(PageValidate pageValidate, FinanceRefundSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        MPJQueryWrapper<RefundRecord> mpjQueryWrapper = new MPJQueryWrapper<>();
        (mpjQueryWrapper.selectAll(RefundRecord.class).select(new String[]{"U.id as user_id,U.nickname,U.avatar, U.account"}).leftJoin("?_user U ON U.id=t.user_id".replace("?_", GlobalConfig.tablePrefix))).orderByDesc("id");
        this.refundRecordMapper.setSearch(mpjQueryWrapper, (Object)searchValidate, new String[]{"like:sn@t.sn:str", "like:order_sn@t.order_sn:str", "=:refund_type@t.refund_type:int", "=:refund_status@t.refund_status:int", "datetime:start_time-end_time@t.create_time:str"});
        if (StringUtils.isNotEmpty((String)searchValidate.getUser_info())) {
            String keyword = searchValidate.getUser_info();
            mpjQueryWrapper.nested(wq -> ((((((wq.like("U.nickname", (Object)keyword)).or()).like("U.account", (Object)keyword)).or()).like("U.sn", (Object)keyword)).or()).like("U.mobile", (Object)keyword));
        }
        IPage<FinanceRefundListVo> iPage = this.refundRecordMapper.selectJoinPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), FinanceRefundListVo.class, (MPJBaseJoin)mpjQueryWrapper);
        for (FinanceRefundListVo vo : iPage.getRecords()) {
            vo.setRefundTypeText(RefundEnum.getRefundTypeMsg((Integer)vo.getRefundType()));
            vo.setRefundStatusText(RefundEnum.getRefundStatusMsg((Integer)vo.getRefundStatus()));
            vo.setRefundWayText(PaymentEnum.getPayWayMsg((Integer)vo.getRefundWay()));
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)vo.getAvatar()));
        }
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("total", this.refundRecordMapper.selectCount(null));
        extend.put("ing", this.refundRecordMapper.selectCount(new QueryWrapper<RefundRecord>().eq("refund_status", (Object)0)));
        extend.put("success", this.refundRecordMapper.selectCount(new QueryWrapper<RefundRecord>().eq("refund_status", (Object)1)));
        extend.put("error", this.refundRecordMapper.selectCount(new QueryWrapper<RefundRecord>().eq("refund_status", (Object)2)));
        LinkedHashMap<String, BigDecimal> stat = new LinkedHashMap<String, BigDecimal>();
        stat.put("totalRefundAmount", this.refundRecordMapper.sum("order_amount", new QueryWrapper<RefundRecord>()));
        stat.put("ingRefundAmount", this.refundRecordMapper.sum("order_amount", new QueryWrapper<RefundRecord>().eq("refund_status", (Object)0)));
        stat.put("successRefundAmount", this.refundRecordMapper.sum("order_amount", new QueryWrapper<RefundRecord>().eq("refund_status", (Object)1)));
        stat.put("errorRefundAmount", this.refundRecordMapper.sum("order_amount", new QueryWrapper<RefundRecord>().eq("refund_status", (Object)2)));
        extend.put("stat", stat);
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), (List)iPage.getRecords(), extend);
    }

    public List<FinanceRefundLogVo> log(Integer recordId) {
        MPJQueryWrapper<RefundLog> mpjQueryWrapper = new MPJQueryWrapper<RefundLog>();
        ((mpjQueryWrapper.selectAll(RefundLog.class).select(new String[]{"sa.name as handler"}).eq("t.record_id", (Object)recordId)).innerJoin("?_admin sa ON sa.id=t.handle_id".replace("?_", GlobalConfig.tablePrefix))).orderByDesc("t.id");
        List<FinanceRefundLogVo> list = this.refundLogMapper.selectJoinList(FinanceRefundLogVo.class, (MPJBaseJoin)mpjQueryWrapper);
        for (FinanceRefundLogVo vo : list) {
            vo.setRefundStatusText(RefundEnum.getRefundStatusMsg((Integer)vo.getRefundStatus()));
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
        }
        return list;
    }

    public Map<String, Object> stat() {
        LinkedHashMap<String, Object> stat = new LinkedHashMap<String, Object>();
        stat.put("total", this.refundRecordMapper.sum("order_amount", new QueryWrapper<RefundRecord>()));
        stat.put("ing", this.refundRecordMapper.sum("order_amount", new QueryWrapper<RefundRecord>().eq("refund_status", (Object)0)));
        stat.put("success", this.refundRecordMapper.sum("order_amount", new QueryWrapper<RefundRecord>().eq("refund_status", (Object)1)));
        stat.put("error", this.refundRecordMapper.sum("order_amount", new QueryWrapper<RefundRecord>().eq("refund_status", (Object)2)));
        return stat;
    }
}

