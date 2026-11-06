/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.excel.EasyExcel
 *  com.alibaba.excel.support.ExcelTypeEnum
 *  com.alibaba.excel.write.builder.ExcelWriterBuilder
 *  com.alibaba.fastjson2.JSONObject
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.alipay.api.AlipayObject
 *  com.alipay.api.AlipayRequest
 *  com.alipay.api.DefaultAlipayClient
 *  com.alipay.api.domain.AlipayTradeRefundModel
 *  com.alipay.api.request.AlipayTradeRefundRequest
 *  com.alipay.api.response.AlipayTradeRefundResponse
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result
 *  com.github.binarywang.wxpay.exception.WxPayException
 *  com.github.yulichang.interfaces.MPJBaseJoin
 *  com.github.yulichang.query.MPJQueryWrapper
 *  com.mdd.admin.service.IFinanceRechargerService
 *  com.mdd.admin.service.impl.FinanceRechargerServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.finance.FinanceRechargeSearchValidate
 *  com.mdd.admin.vo.finance.FinanceRechargeListExportVo
 *  com.mdd.admin.vo.finance.FinanceRechargeListVo
 *  com.mdd.common.config.AlipayConfig
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Coupon
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.RefundLog
 *  com.mdd.common.entity.RefundRecord
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.enums.LogMoneyEnum
 *  com.mdd.common.enums.PaymentEnum
 *  com.mdd.common.enums.RefundEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.CouponMapper
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.RefundLogMapper
 *  com.mdd.common.mapper.RefundRecordMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.log.UserAccountLogMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.plugin.wechat.WxPayDriver
 *  com.mdd.common.plugin.wechat.request.RefundRequestV3
 *  com.mdd.common.util.AmountUtil
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.ToolUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.YmlUtils
 *  javax.annotation.Resource
 *  org.springframework.jdbc.datasource.DataSourceTransactionManager
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.TransactionDefinition
 *  org.springframework.transaction.TransactionStatus
 *  org.springframework.util.Assert
 */
package com.mdd.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alipay.api.AlipayObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.MPJQueryWrapper;
import com.mdd.admin.service.IFinanceRechargerService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceRechargeSearchValidate;
import com.mdd.admin.vo.finance.FinanceRechargeListExportVo;
import com.mdd.admin.vo.finance.FinanceRechargeListVo;
import com.mdd.common.config.AlipayConfig;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Coupon;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.RefundLog;
import com.mdd.common.entity.RefundRecord;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.user.User;
import com.mdd.common.enums.AccountLogEnum;
import com.mdd.common.enums.LogMoneyEnum;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.enums.RefundEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.CouponMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.RefundLogMapper;
import com.mdd.common.mapper.RefundRecordMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.log.UserAccountLogMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.plugin.wechat.WxPayDriver;
import com.mdd.common.plugin.wechat.request.RefundRequestV3;
import com.mdd.common.util.AmountUtil;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

@Service
public class FinanceRechargerServiceImpl
implements IFinanceRechargerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinanceRechargerServiceImpl.class);
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    UserAccountLogMapper userAccountLogMapper;
    @Resource
    RefundRecordMapper refundRecordMapper;
    @Resource
    RefundLogMapper refundLogMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CouponMapper couponMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    DataSourceTransactionManager transactionManager;
    @Resource
    TransactionDefinition transactionDefinition;

    public PageResult<FinanceRechargeListVo> list(PageValidate pageValidate, FinanceRechargeSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        MPJQueryWrapper<RechargeOrder> mpjQueryWrapper = new MPJQueryWrapper<>();
        ((((mpjQueryWrapper.selectAll(RechargeOrder.class).select(new String[]{"U.id as user_id, U.account, U.nickname, U.avatar, U.mobile, t.sn AS sn, t.is_audit, t.voucher AS voucher"}).select(new String[]{"C.name as course_name, C.instructor_name"}).select(new String[]{"CP.name as coupon_name"}).select(new String[]{"S.name as student_name, S.avatar as student_avatar"}).leftJoin("?_user U ON U.id=t.user_id".replace("?_", GlobalConfig.tablePrefix))).leftJoin("?_course_class C ON C.id=t.course_class_id".replace("?_", GlobalConfig.tablePrefix))).leftJoin("?_coupon CP ON CP.id=t.coupon_id".replace("?_", GlobalConfig.tablePrefix))).leftJoin("?_student S ON S.id=t.student_id".replace("?_", GlobalConfig.tablePrefix))).orderByDesc("id");
        this.rechargeOrderMapper.setSearch(mpjQueryWrapper, (Object)searchValidate, new String[]{"like:sn@t.sn:str", "=:pay_way@t.pay_way:int", "=:pay_status@t.pay_status:int", "datetime:start_time-end_time@t.create_time:str"});
        if (StringUtils.isNotEmpty((String)searchValidate.getUser_info())) {
            String keyword = searchValidate.getUser_info();
            mpjQueryWrapper.nested(wq -> ((((((wq.like("U.nickname", (Object)keyword)).or()).like("U.account", (Object)keyword)).or()).like("U.sn", (Object)keyword)).or()).like("U.mobile", (Object)keyword));
        }
        IPage<FinanceRechargeListVo> iPage = this.rechargeOrderMapper.selectJoinPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), FinanceRechargeListVo.class, (MPJBaseJoin)mpjQueryWrapper);
        for (FinanceRechargeListVo vo : iPage.getRecords()) {
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            vo.setPayTime(StringUtils.isNull(vo.getPayTime()) ? "-" : TimeUtils.timestampToDate((String)vo.getPayTime()));
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)vo.getAvatar()));
            vo.setPayWay(vo.getPayWay());
            vo.setPayWayText(PaymentEnum.getPayWayMsg((Integer)Integer.parseInt(vo.getPayWay())));
            vo.setPayStatusText(PaymentEnum.getPayStatusMsg((Integer)vo.getPayStatus()));
            vo.setStudentAvatar(UrlUtils.toAdminAbsoluteUrl((String)vo.getStudentAvatar()));
        }
        return PageResult.iPageHandle((IPage)iPage);
    }

    public void refund(Integer orderId, Integer adminId) {
        refund(orderId, adminId, null);
    }

    /**
     * 统一退款入口（#61）：按订单类型区分钱包资金方向。
     *
     * <p>钱包支付的**消费订单**（课程/活动/券）退款是把钱加回用户余额；
     * 只有纯充值订单退款才是从余额里扣回已入账金额。两类语义相反，不能共用 dec 分支。</p>
     *
     * <p>事务使用 REQUIRES_NEW：退款记录/订单状态必须先于调用方（活动告警处理）
     * 的本地写库独立提交。这样即使后续取消报名/释放名额失败，退款记录仍在，
     * 重试时可凭 order.refund_status=1 跳过外部退款，只补本地状态，避免重复退款。</p>
     */
    public void refund(Integer orderId, Integer adminId, BigDecimal refundAmount) {
        boolean isWxPay;
        RechargeOrder rechargeOrder = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)orderId);
        Assert.notNull((Object)rechargeOrder, (String)"\u5145\u503c\u8ba2\u5355\u4e0d\u5b58\u5728!");
        if (!rechargeOrder.getPayStatus().equals(PaymentEnum.OK_PAID.getCode())) {
            throw new OperateException("\u5f53\u524d\u8ba2\u5355\u4e0d\u53ef\u9000\u6b3e!");
        }
        if (Integer.valueOf(1).equals(rechargeOrder.getRefundStatus())) {
            throw new OperateException("\u8ba2\u5355\u5df2\u53d1\u8d77\u9000\u6b3e,\u9000\u6b3e\u5931\u8d25\u8bf7\u5230\u9000\u6b3e\u8bb0\u5f55\u91cd\u65b0\u9000\u6b3e!");
        }
        User user = (User)this.userMapper.selectById((Serializable)rechargeOrder.getUserId());
        Integer payWay = rechargeOrder.getPayWay();
        boolean isWalletPay = payWay != null && payWay.equals(PaymentEnum.WALLET_PAY.getCode());
        boolean isAliPay = payWay != null && payWay.equals(PaymentEnum.ALI_PAY.getCode());
        boolean bl = isWxPay = payWay != null && payWay.equals(PaymentEnum.WX_PAY.getCode());
        if (!(isWalletPay || isAliPay || isWxPay)) {
            throw new OperateException("\u5f53\u524d\u8ba2\u5355\u652f\u4ed8\u65b9\u5f0f\u4e0d\u652f\u6301\u9000\u6b3e!");
        }
        BigDecimal orderAmount = rechargeOrder.getOrderAmount() == null
                ? BigDecimal.ZERO : rechargeOrder.getOrderAmount();
        BigDecimal actualRefund = refundAmount == null ? orderAmount : refundAmount;
        if (actualRefund.compareTo(BigDecimal.ZERO) < 0
                || actualRefund.compareTo(orderAmount) > 0) {
            throw new OperateException("\u9000\u6b3e\u91d1\u989d\u4e0d\u5408\u6cd5!");
        }
        // 充值订单退款是扣回已入账余额，必须校验余额够扣；消费订单退款是加回余额，不校验。
        boolean consumptionOrder = isConsumptionOrder(rechargeOrder);
        if (isWalletPay && !consumptionOrder
                && (user.getUserMoney() == null || user.getUserMoney().compareTo(actualRefund) < 0)) {
            throw new OperateException("\u9000\u6b3e\u5931\u8d25:\u7528\u6237\u4f59\u989d\u5df2\u4e0d\u8db3\u9000\u6b3e\u91d1\u989d!");
        }
        // REQUIRES_NEW：退款记录与订单退款状态独立提交，供调用方失败重试时判重。
        TransactionStatus transactionStatus = this.transactionManager.getTransaction(
                new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        RefundRecord refundRecord = null;
        RefundLog log = null;
        try {
            long claimNow = System.currentTimeMillis() / 1000L;
            // 订单退款状态是唯一并发闸门：只有把一个未退款订单从 0 原子切到 1 的请求
            // 才能继续入账/出站退款。并发请求会在 InnoDB 行锁上串行，后到者更新 0 行。
            int claimed = this.rechargeOrderMapper.update(null,
                    new UpdateWrapper<RechargeOrder>()
                            .set("refund_status", Integer.valueOf(1))
                            .set("update_time", Long.valueOf(claimNow))
                            .eq("id", rechargeOrder.getId())
                            .eq("pay_status", Integer.valueOf(PaymentEnum.OK_PAID.getCode()))
                            .eq("refund_status", Integer.valueOf(0))
                            .isNull("delete_time"));
            if (claimed != 1) {
                throw new OperateException("\u8ba2\u5355\u5df2\u53d1\u8d77\u9000\u6b3e,\u8bf7\u52ff\u91cd\u590d\u64cd\u4f5c");
            }
            rechargeOrder.setRefundStatus(Integer.valueOf(1));
            rechargeOrder.setUpdateTime(Long.valueOf(claimNow));
            UserCourse uc = (UserCourse)this.userCourseMapper.selectOne(new QueryWrapper<UserCourse>().eq("order_id", (Object)rechargeOrder.getId()).last("limit 1"));
            if (uc != null) {
                uc.setStatus(Integer.valueOf(2));
                uc.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.userCourseMapper.updateById(uc);
            }
            if (isWalletPay) {
                if (consumptionOrder) {
                    // 课程/活动/券订单是余额扣款，退款应把余额原子加回。
                    int logType = consumptionRefundLogType(rechargeOrder);
                    long now = System.currentTimeMillis() / 1000L;
                    this.userAccountLogMapper.add(user.getId(), Integer.valueOf(logType), actualRefund,
                            rechargeOrder.getId(), rechargeOrder.getSn(), consumptionRefundMessage(logType), null);
                    int added = this.userMapper.addMoney(user.getId(), actualRefund, Long.valueOf(now));
                    if (added <= 0) {
                        throw new OperateException("\u9000\u6b3e\u5931\u8d25:\u7528\u6237\u4e0d\u5b58\u5728");
                    }
                } else {
                    this.userAccountLogMapper.dec(user.getId(), Integer.valueOf(LogMoneyEnum.UM_DEC_RECHARGE.getCode()), actualRefund, rechargeOrder.getId(), rechargeOrder.getSn(), "\u5145\u503c\u8ba2\u5355\u9000\u6b3e", null);
                    user.setUserMoney(user.getUserMoney().subtract(actualRefund));
                    this.userMapper.updateById(user);
                }
            }
            String refundSn = this.refundRecordMapper.randMakeOrderSn("sn");
            refundRecord = new RefundRecord();
            refundRecord.setSn(refundSn);
            // 同一订单只允许一条后台退款记录；条件更新与唯一键双层防重。
            refundRecord.setRefundKey("admin-order:" + rechargeOrder.getId());
            refundRecord.setUserId(rechargeOrder.getUserId());
            refundRecord.setOrderId(rechargeOrder.getId());
            refundRecord.setOrderSn(rechargeOrder.getSn());
            refundRecord.setOrderType(RefundEnum.getOrderType((Integer)(consumptionOrder
                    ? RefundEnum.ORDER_TYPE_ORDER.getCode() : RefundEnum.ORDER_TYPE_RECHARGE.getCode())));
            refundRecord.setOrderAmount(orderAmount);
            refundRecord.setRefundAmount(actualRefund);
            refundRecord.setRefundType(Integer.valueOf(RefundEnum.TYPE_ADMIN.getCode()));
            refundRecord.setTransactionId(rechargeOrder.getTransactionId());
            refundRecord.setRefundWay(rechargeOrder.getPayWay());
            this.refundRecordMapper.insert(refundRecord);
            log = new RefundLog();
            log.setSn(this.refundLogMapper.randMakeOrderSn("sn"));
            log.setRecordId(refundRecord.getId());
            log.setUserId(rechargeOrder.getUserId());
            log.setHandleId(adminId);
            log.setOrderAmount(orderAmount);
            log.setRefundAmount(refundRecord.getRefundAmount());
            log.setRefundStatus(Integer.valueOf(RefundEnum.REFUND_ING.getCode()));
            log.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            log.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.refundLogMapper.insert(log);
            String refundResponse = null;
            refundResponse = isWalletPay
                    ? "{\"refund_type\":\"wallet\",\"message\":\"\u4f59\u989d\u652f\u4ed8\u9000\u6b3e\u5df2\u5b8c\u6210\"}"
                    : (isAliPay ? this.aliPayRefund(rechargeOrder, refundSn) : this.wxPayRefund(rechargeOrder, refundSn));
            refundRecord.setRefundStatus(Integer.valueOf(RefundEnum.REFUND_SUCCESS.getCode()));
            refundRecord.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            refundRecord.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            refundRecord.setTransactionId(rechargeOrder.getTransactionId());
            this.refundRecordMapper.updateById(refundRecord);
            log.setRefundStatus(Integer.valueOf(RefundEnum.REFUND_SUCCESS.getCode()));
            log.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            if (StringUtils.isNotNull(refundResponse)) {
                log.setRefundMsg(refundResponse);
            }
            this.refundLogMapper.updateById(log);
            this.transactionManager.commit(transactionStatus);
        }
        catch (Exception e) {
            LOGGER.error("退款失败 orderId={} adminId={} amount={}", orderId, adminId, actualRefund, e);
            this.transactionManager.rollback(transactionStatus);
            if (StringUtils.isNotNull(refundRecord)) {
                refundRecord.setRefundStatus(Integer.valueOf(RefundEnum.REFUND_ERROR.getCode()));
                this.refundRecordMapper.updateById(refundRecord);
            }
            if (StringUtils.isNotNull(log)) {
                log.setRefundStatus(Integer.valueOf(RefundEnum.REFUND_ERROR.getCode()));
                this.refundLogMapper.updateById(log);
            }
            throw new OperateException(e.getMessage());
        }
    }

    /** 消费订单：课程 / 活动 / 券；纯充值订单三者均为空。 */
    private boolean isConsumptionOrder(RechargeOrder order) {
        return (order.getCourseClassId() != null && order.getCourseClassId() > 0)
                || (order.getActivityId() != null && order.getActivityId() > 0)
                || (order.getCouponGoodsId() != null && order.getCouponGoodsId() > 0);
    }

    /** 消费订单退款入账类型：与扣款时的流水类型对应，便于财务按类型汇总。 */
    private int consumptionRefundLogType(RechargeOrder order) {
        if (order.getActivityId() != null && order.getActivityId() > 0) {
            return AccountLogEnum.UM_INC_ACTIVITY_REFUND.getCode();
        }
        if (order.getCourseClassId() != null && order.getCourseClassId() > 0) {
            return AccountLogEnum.UM_INC_COURSE_REFUND.getCode();
        }
        return AccountLogEnum.UM_INC_COUPON_REFUND.getCode();
    }

    private String consumptionRefundMessage(int logType) {
        if (logType == AccountLogEnum.UM_INC_ACTIVITY_REFUND.getCode()) {
            return "\u6d3b\u52a8\u62a5\u540d\u9000\u6b3e\u589e\u52a0\u4f59\u989d";
        }
        if (logType == AccountLogEnum.UM_INC_COURSE_REFUND.getCode()) {
            return "\u8bfe\u7a0b\u9000\u6b3e\u589e\u52a0\u4f59\u989d";
        }
        return "\u5238\u8ba2\u5355\u9000\u6b3e\u589e\u52a0\u4f59\u989d";
    }

    private String aliPayRefund(RechargeOrder rechargeOrder, String refundSn) throws Exception {
        String gateWay = StringUtils.isNotNull(YmlUtils.get((String)"like.alidebug")) && YmlUtils.get((String)"like.alidebug").equals("true") ? AlipayConfig.GATEWAY_URL_DEBUG : AlipayConfig.GATEWAY_URL;
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(gateWay, ConfigUtils.getAliDevPay((String)"app_id"), ConfigUtils.getAliDevPay((String)"private_key"), "json", AlipayConfig.CHARSET, ConfigUtils.getAliDevPay((String)"ali_public_key"), AlipayConfig.SIGN_TYPE);
        AlipayTradeRefundRequest aliRequest = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel alipayTradeRefundModel = new AlipayTradeRefundModel();
        alipayTradeRefundModel.setTradeNo(rechargeOrder.getTransactionId());
        alipayTradeRefundModel.setRefundAmount(rechargeOrder.getOrderAmount().toString());
        alipayTradeRefundModel.setOutRequestNo(rechargeOrder.getSn());
        aliRequest.setBizModel((AlipayObject)alipayTradeRefundModel);
        AlipayTradeRefundResponse response = (AlipayTradeRefundResponse)alipayClient.execute((AlipayRequest)aliRequest);
        if (response.isSuccess()) {
            String ret = JSONObject.toJSONString((Object)response, (JSONWriter.Feature[])new JSONWriter.Feature[0]);
            if (StringUtils.isNotNull(ret)) {
                JSONObject refundResponseJSON = JSONObject.parseObject((String)ret);
                rechargeOrder.setRefundTransactionId(refundResponseJSON.getString("tradeNo"));
                this.rechargeOrderMapper.updateById(rechargeOrder);
            }
            return ret;
        }
        throw new Exception(response.getBody());
    }

    private String wxPayRefund(RechargeOrder rechargeOrder, String refundSn) throws WxPayException {
        String refundTransactionId = null;
        RefundRequestV3 requestV3 = new RefundRequestV3();
        requestV3.setTransactionId(rechargeOrder.getTransactionId());
        requestV3.setOutTradeNo(rechargeOrder.getSn());
        requestV3.setOutRefundNo(refundSn);
        requestV3.setTotalAmount(Integer.valueOf(AmountUtil.yuan2Fen((String)rechargeOrder.getOrderAmount().toString())));
        requestV3.setRefundAmount(Integer.valueOf(AmountUtil.yuan2Fen((String)rechargeOrder.getOrderAmount().toString())));
        WxPayRefundV3Result result = WxPayDriver.refund((RefundRequestV3)requestV3);
        refundTransactionId = result.getTransactionId();
        rechargeOrder.setRefundTransactionId(String.valueOf(result.getRefundId()));
        this.rechargeOrderMapper.updateById(rechargeOrder);
        return JSONObject.toJSONString((Object)result, (JSONWriter.Feature[])new JSONWriter.Feature[0]);
    }

    public void refundAgain(Integer recordId, Integer adminId) {
        TransactionStatus transactionStatus = this.transactionManager.getTransaction(this.transactionDefinition);
        RefundLog log = null;
        try {
            RefundRecord refundRecord = (RefundRecord)this.refundRecordMapper.selectById((Serializable)recordId);
            // 记录不存在时不能直接 getOrderId()：原先会抛 NPE 并把原始英文异常文案返回前端（task-7）
            Assert.notNull((Object)refundRecord, (String)"\u9000\u6b3e\u8bb0\u5f55\u4e0d\u5b58\u5728!");
            RechargeOrder rechargeOrder = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)refundRecord.getOrderId());
            Assert.notNull((Object)rechargeOrder, (String)"\u5145\u503c\u8ba2\u5355\u4e22\u5931!");
            log = (RefundLog)this.refundLogMapper.selectOne(new QueryWrapper<RefundLog>().eq("record_id", (Object)recordId).last("limit 1"));
            Assert.notNull((Object)log, (String)"\u9000\u6b3e\u6d41\u6c34\u4e0d\u5b58\u5728!");
            log.setRefundStatus(Integer.valueOf(RefundEnum.REFUND_ING.getCode()));
            this.refundLogMapper.updateById(log);
            RefundRequestV3 requestV3 = new RefundRequestV3();
            requestV3.setTransactionId(refundRecord.getTransactionId());
            requestV3.setOutTradeNo(refundRecord.getOrderSn());
            requestV3.setOutRefundNo(refundRecord.getSn());
            requestV3.setTotalAmount(Integer.valueOf(AmountUtil.yuan2Fen((String)rechargeOrder.getOrderAmount().toString())));
            requestV3.setRefundAmount(Integer.valueOf(AmountUtil.yuan2Fen((String)refundRecord.getOrderAmount().toString())));
            WxPayDriver.refund((RefundRequestV3)requestV3);
            log.setRefundStatus(Integer.valueOf(RefundEnum.REFUND_SUCCESS.getCode()));
            this.refundLogMapper.updateById(log);
            this.transactionManager.commit(transactionStatus);
        }
        catch (Exception e) {
            LOGGER.error("退款重试失败 recordId={} adminId={}", recordId, adminId, e);
            this.transactionManager.rollback(transactionStatus);
            if (StringUtils.isNotNull(log)) {
                log.setRefundStatus(Integer.valueOf(RefundEnum.REFUND_ERROR.getCode()));
                this.refundLogMapper.updateById(log);
            }
            throw new OperateException(e.getMessage());
        }
    }

    public JSONObject getExportData(PageValidate pageValidate, FinanceRechargeSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        PageResult userVoPageResult = this.list(pageValidate, searchValidate);
        JSONObject ret = ToolUtils.getExportData((Long)userVoPageResult.getCount(), (Integer)limit, (Integer)searchValidate.getPage_start(), (Integer)searchValidate.getPage_end(), (String)"\u5145\u503c\u5217\u8868");
        return ret;
    }

    public String export(FinanceRechargeSearchValidate searchValidate) {
        PageValidate pageValidate = new PageValidate();
        if (StringUtils.isNotNull(searchValidate.getPage_start())) {
            pageValidate.setPage_no(searchValidate.getPage_start());
        } else {
            pageValidate.setPage_no(Integer.valueOf(1));
        }
        if (StringUtils.isNotNull(searchValidate.getPage_end()) && StringUtils.isNotNull(searchValidate.getPage_size())) {
            pageValidate.setPage_size(Integer.valueOf(searchValidate.getPage_end() * searchValidate.getPage_size()));
        } else {
            pageValidate.setPage_size(Integer.valueOf(20));
        }
        Boolean isAll = StringUtils.isNull(searchValidate.getPage_type()) || searchValidate.getPage_type().equals(0);
        List excellist = this.getExcellist(isAll.booleanValue(), pageValidate, searchValidate);
        String fileName = ToolUtils.safeExportFileName(searchValidate.getFile_name());
        String folderPath = "/excel/export/" + TimeUtils.timestampToDay((Long)(System.currentTimeMillis() / 1000L)) + "/";
        String path = folderPath + fileName + ".xlsx";
        String filePath = YmlUtils.get((String)"like.upload-directory") + path;
        File folder = new File(YmlUtils.get((String)"like.upload-directory") + folderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new OperateException("\u521b\u5efa\u6587\u4ef6\u5939\u5931\u8d25");
        }
        ((ExcelWriterBuilder)EasyExcel.write((String)filePath).head(FinanceRechargeListExportVo.class)).excelType(ExcelTypeEnum.XLSX).sheet("\u7f34\u8d39\u8bb0\u5f55").doWrite((Collection)excellist);
        return UrlUtils.toAdminAbsoluteUrl((String)path);
    }

    private List<FinanceRechargeListExportVo> getExcellist(boolean isAll, PageValidate pageValidate, FinanceRechargeSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        MPJQueryWrapper<RechargeOrder> mpjQueryWrapper = new MPJQueryWrapper<RechargeOrder>();
        ((mpjQueryWrapper.selectAll(RechargeOrder.class).select(new String[]{"U.id as user_id, U.account, U.nickname, U.avatar, U.mobile, t.sn AS sn"}).select(new String[]{"C.name as course_name, C.instructor_name"}).leftJoin("?_user U ON U.id=t.user_id".replace("?_", GlobalConfig.tablePrefix))).leftJoin("?_course_class C ON C.id=t.course_class_id".replace("?_", GlobalConfig.tablePrefix))).orderByDesc("id");
        this.rechargeOrderMapper.setSearch(mpjQueryWrapper, (Object)searchValidate, new String[]{"like:sn@t.sn:str", "=:pay_way@t.pay_way:int", "=:pay_status@t.pay_status:int", "datetime:start_time-end_time@t.create_time:str"});
        if (StringUtils.isNotEmpty((String)searchValidate.getUser_info())) {
            String keyword = searchValidate.getUser_info();
            mpjQueryWrapper.nested(wq -> ((((wq.like("U.nickname", (Object)keyword)).or()).like("U.sn", (Object)keyword)).or()).like("U.mobile", (Object)keyword));
        }
        List<FinanceRechargeListExportVo> retList = new ArrayList();
        if (isAll) {
            retList = this.rechargeOrderMapper.selectJoinList(FinanceRechargeListExportVo.class, (MPJBaseJoin)mpjQueryWrapper);
        } else {
            IPage<FinanceRechargeListExportVo> iPage = this.rechargeOrderMapper.selectJoinPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), FinanceRechargeListExportVo.class, (MPJBaseJoin)mpjQueryWrapper);
            retList = iPage.getRecords();
        }
        for (FinanceRechargeListExportVo vo : retList) {
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            vo.setPayTime(TimeUtils.timestampToDate((String)vo.getPayTime()));
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)vo.getAvatar()));
            vo.setPayWay(vo.getPayWay());
            vo.setPayWayText(PaymentEnum.getPayWayMsg((Integer)Integer.parseInt(vo.getPayWay())));
            vo.setPayStatusText(PaymentEnum.getPayStatusMsg((Integer)vo.getPayStatus()));
        }
        return retList;
    }

    public Map<String, Object> stats() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        long now = System.currentTimeMillis() / 1000L;
        String monthBeginDate = TimeUtils.timestampToDate((Long)now).substring(0, 7) + "-01 00:00:00";
        long monthBegin = TimeUtils.dateToTimestamp((String)monthBeginDate);
        List<Map<String, Object>> sumList = this.rechargeOrderMapper.selectMaps(new QueryWrapper<RechargeOrder>().select(new String[]{"sum(order_amount) as total_amount"}).eq("pay_status", (Object)1).ge("create_time", (Object)monthBegin));
        BigDecimal totalRevenue = BigDecimal.ZERO;
        if (sumList != null && sumList.size() > 0 && sumList.get(0) != null && ((Map)sumList.get(0)).get("total_amount") != null) {
            totalRevenue = new BigDecimal(((Map)sumList.get(0)).get("total_amount").toString());
        }
        Long successCount = this.rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>().eq("pay_status", (Object)1));
        Long pendingCount = this.rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>().eq("pay_status", (Object)0));
        Long refundCount = this.rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>().eq("refund_status", (Object)1));
        map.put("totalRevenue", totalRevenue);
        map.put("successCount", successCount);
        map.put("pendingCount", pendingCount);
        map.put("refundCount", refundCount);
        return map;
    }

    /**
     * 后台编辑缴费订单（#77）。
     *
     * <p>只允许改支付方式/支付状态/对账状态/支付凭证/抵扣券；订单号与金额一律只读，
     * 不读取也不落库调用方传入的 sn/orderAmount。旧后台提交的 snake_case 字段由
     * {@code FinanceRechargeEditValidate} 的 @JsonAlias 兼容。</p>
     *
     * <p>校验口径：支付方式 1-4（1=余额 2=微信 3=支付宝 4=线下）；支付状态 0/1/4；
     * 对账状态 0/1；线下支付必须带凭证；抵扣券必须存在且属于订单用户，换成新券时
     * 该券必须仍可用；已关联的原券即使已变成“已使用”也允许原样保存。</p>
     */
    public void edit(Integer id, Integer payWay, Integer payStatus, Integer isAudit, String voucher, Integer couponId) {
        if (id == null) {
            throw new OperateException("订单ID不能为空");
        }
        RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectOne(
                new QueryWrapper<RechargeOrder>().eq("id", (Object)id));
        if (order == null) {
            throw new OperateException("订单不存在");
        }
        if (payWay == null || payWay < 1 || payWay > 4) {
            throw new OperateException("支付方式不合法");
        }
        if (payStatus == null || (payStatus != 0 && payStatus != 1 && payStatus != 4)) {
            throw new OperateException("支付状态不合法");
        }
        if (isAudit == null || (isAudit != 0 && isAudit != 1)) {
            throw new OperateException("审核状态不合法");
        }
        Integer currentPayStatus = order.getPayStatus() == null ? 0 : order.getPayStatus();
        Integer currentPayWay = order.getPayWay();
        Integer currentCouponId = order.getCouponId() == null ? 0 : order.getCouponId();
        Integer requestedCouponId = couponId == null ? 0 : couponId;
        if (!currentPayStatus.equals(payStatus)) {
            throw new OperateException("支付状态必须由支付/退款流程变更，编辑接口不得直接修改");
        }
        if (Integer.valueOf(1).equals(currentPayStatus) && !java.util.Objects.equals(currentPayWay, payWay)) {
            throw new OperateException("已支付订单的支付通道不可修改，退款只能走原支付通道");
        }
        if (!currentCouponId.equals(requestedCouponId)) {
            throw new OperateException("优惠券变更必须走券状态机，编辑接口不得直接更换");
        }
        if (isAudit == 1 && !Integer.valueOf(1).equals(currentPayStatus)) {
            throw new OperateException("未支付订单不能标记为已对账");
        }
        String normalizedVoucher = voucher == null ? "" : voucher.trim();
        if (payWay == 4 && normalizedVoucher.isEmpty()) {
            throw new OperateException("请上传支付凭证");
        }
        if (normalizedVoucher.length() > 100) {
            throw new OperateException("支付凭证地址过长");
        }

        if (couponId != null && couponId > 0) {
            Coupon coupon = (Coupon)this.couponMapper.selectOne(
                    new QueryWrapper<Coupon>().eq("id", (Object)couponId).last("limit 1"));
            if (coupon == null) {
                throw new OperateException("优惠券不存在");
            }
            if (coupon.getUserId() != null && coupon.getUserId() > 0) {
                if (!coupon.getUserId().equals(order.getUserId())) {
                    throw new OperateException("优惠券不属于该订单用户");
                }
            } else if (StringUtils.isNotEmpty(coupon.getMobile())) {
                User user = (User)this.userMapper.selectById((Serializable)order.getUserId());
                if (user == null || !coupon.getMobile().equals(user.getMobile())) {
                    throw new OperateException("优惠券不属于该订单用户");
                }
            }
            boolean unchangedCoupon = couponId.equals(order.getCouponId());
            if (!unchangedCoupon && coupon.getStatus() != null && coupon.getStatus() != 0) {
                throw new OperateException("优惠券已使用或已过期");
            }
        }

        UpdateWrapper<RechargeOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", (Object)id)
                .set("pay_way", (Object)payWay)
                .set("pay_status", (Object)payStatus)
                .set("is_audit", (Object)isAudit)
                .set("voucher", (Object)normalizedVoucher)
                .set("update_time", (Object)(System.currentTimeMillis() / 1000L));
        if (payWay == 4 && payStatus == 1 && order.getPayTime() == null) {
            updateWrapper.set("pay_time", (Object)(System.currentTimeMillis() / 1000L));
        }
        this.rechargeOrderMapper.update(null, (Wrapper)updateWrapper);
    }
}
