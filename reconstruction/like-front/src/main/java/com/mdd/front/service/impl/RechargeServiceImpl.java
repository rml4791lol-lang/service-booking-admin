/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.enums.AccountLogEnum
 *  com.mdd.common.enums.PaymentEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.log.UserAccountLogMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.front.service.IRechargeService
 *  com.mdd.front.service.impl.RechargeServiceImpl
 *  com.mdd.front.validate.RechargeValidate
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.vo.RechargeConfigVo
 *  com.mdd.front.vo.RechargeRecordVo
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.activity.Activity;
import com.mdd.common.entity.activity.ActivitySignup;
import com.mdd.common.entity.coupon.CouponGoods;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.refund.RefundTask;
import com.mdd.common.entity.user.User;
import com.mdd.common.enums.AccountLogEnum;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.activity.ActivityMapper;
import com.mdd.common.mapper.activity.ActivitySignupMapper;
import com.mdd.common.mapper.coupon.CouponGoodsMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.log.UserAccountLogMapper;
import com.mdd.common.mapper.refund.RefundTaskMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.front.service.IRechargeService;
import com.mdd.front.util.CouponRefundRules;
import com.mdd.front.util.OrderStatusUtils;
import com.mdd.front.validate.RechargeValidate;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.vo.OrderListVo;
import com.mdd.front.vo.RechargeConfigVo;
import com.mdd.front.vo.RechargeRecordVo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RechargeServiceImpl
implements IRechargeService {
    @Resource
    UserAccountLogMapper userAccountLogMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ActivityMapper activityMapper;
    @Resource
    ActivitySignupMapper activitySignupMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponGoodsMapper couponGoodsMapper;
    @Resource
    RefundTaskMapper refundTaskMapper;

    /** 券退款开关（#110 T12）：关闭时券订单不展示任何退款入口。 */
    @Value("${like.coupon.refund-enabled:true}")
    private boolean couponRefundEnabled;

    public RechargeConfigVo config(Integer userId) {
        User user = (User)this.userMapper.selectById((Serializable)userId);
        Map<String, String> config = ConfigUtils.get((String)"recharge");
        RechargeConfigVo vo = new RechargeConfigVo();
        vo.setStatus(Integer.valueOf(Integer.parseInt(config.getOrDefault("status", "0"))));
        vo.setMinAmount(new BigDecimal(config.getOrDefault("min_amount", "0")));
        vo.setUserMoney(user.getUserMoney());
        return vo;
    }

    public PageResult<RechargeRecordVo> record(Integer userId, PageValidate pageValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        QueryWrapper<RechargeOrder> queryWrapper = new QueryWrapper<RechargeOrder>();
        queryWrapper.eq("user_id", (Object)userId);
        queryWrapper.eq("pay_status", (Object)PaymentEnum.OK_PAID.getCode());
        queryWrapper.orderByDesc("id");
        IPage<RechargeOrder> iPage = this.rechargeOrderMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), (Wrapper)queryWrapper);
        LinkedList<RechargeRecordVo> list = new LinkedList<RechargeRecordVo>();
        for (RechargeOrder rechargeOrder : iPage.getRecords()) {
            RechargeRecordVo vo = new RechargeRecordVo();
            vo.setId(rechargeOrder.getId());
            vo.setAction(Integer.valueOf(1));
            vo.setOrderAmount(rechargeOrder.getOrderAmount());
            vo.setCreateTime(TimeUtils.timestampToDate((Long)rechargeOrder.getPayTime()));
            vo.setTips("\u5145\u503c" + vo.getOrderAmount() + "\u5143");
            vo.setOrderSn(rechargeOrder.getSn());
            vo.setPayStatus(rechargeOrder.getPayStatus());
            vo.setRefundStatus(rechargeOrder.getRefundStatus() == null ? Integer.valueOf(0) : rechargeOrder.getRefundStatus());
            String status = OrderStatusUtils.resolveStatus(rechargeOrder.getPayStatus(), rechargeOrder.getRefundStatus(), null, false);
            vo.setStatus(status);
            vo.setStatusText(OrderStatusUtils.statusText(status));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    /**
     * 小程序「我的订单」统一读模型（#63）。
     *
     * <p>一张订单表承载券/活动/课程三类订单，这里一次性把类型、标题、支付/退款状态与
     * #60 退款任务状态批量补齐（每类一次 IN 查询，避免逐单查询）。只读：不修改订单、
     * 不触发退款/取消，历史订单原样展示。</p>
     */
    public PageResult<OrderListVo> orderList(Integer userId, PageValidate pageValidate) {
        int pageNo = pageValidate.getPage_no() == null ? 1 : Math.max(1, pageValidate.getPage_no().intValue());
        int pageSize = pageValidate.getPage_size() == null ? 20 : Math.max(1, Math.min(60, pageValidate.getPage_size().intValue()));
        QueryWrapper<RechargeOrder> queryWrapper = new QueryWrapper<RechargeOrder>();
        queryWrapper.eq("user_id", (Object)userId);
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc("id");
        IPage<RechargeOrder> iPage = this.rechargeOrderMapper.selectPage((IPage)new Page((long)pageNo, (long)pageSize), (Wrapper)queryWrapper);
        List<RechargeOrder> orders = iPage.getRecords();
        if (orders.isEmpty()) {
            return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), new ArrayList<OrderListVo>());
        }

        Set<Integer> orderIds = new HashSet<Integer>();
        Set<Integer> activityIds = new HashSet<Integer>();
        Set<Integer> courseIds = new HashSet<Integer>();
        Set<Integer> couponGoodsIds = new HashSet<Integer>();
        for (RechargeOrder order : orders) {
            orderIds.add(order.getId());
            String type = OrderStatusUtils.resolveType(order.getActivityId(), order.getCourseClassId(), order.getCouponGoodsId());
            if (OrderStatusUtils.TYPE_ACTIVITY.equals(type)) {
                activityIds.add(order.getActivityId());
            } else if (OrderStatusUtils.TYPE_COURSE.equals(type)) {
                courseIds.add(order.getCourseClassId());
            } else if (OrderStatusUtils.TYPE_COUPON.equals(type)) {
                couponGoodsIds.add(order.getCouponGoodsId());
            }
        }

        Map<Integer, String> activityTitles = new HashMap<Integer, String>();
        if (!activityIds.isEmpty()) {
            List<Activity> activities = this.activityMapper.selectBatchIds(activityIds);
            for (Activity activity : activities) {
                activityTitles.put(activity.getId(), activity.getTitle());
            }
        }
        Map<Integer, String> courseNames = new HashMap<Integer, String>();
        if (!courseIds.isEmpty()) {
            List<CourseClass> courseClasses = this.courseClassMapper.selectBatchIds(courseIds);
            for (CourseClass courseClass : courseClasses) {
                courseNames.put(courseClass.getId(), courseClass.getName());
            }
        }
        // 券标题优先用券实例快照（购买时点名称），回退券商品当前名称。
        Map<Integer, String> couponInstanceTitles = new HashMap<Integer, String>();
        Map<Integer, List<CouponInstance>> couponInstancesByOrder = new HashMap<Integer, List<CouponInstance>>();
        List<CouponInstance> couponInstances = this.couponInstanceMapper.selectList(
                new QueryWrapper<CouponInstance>().in("order_id", orderIds).orderByAsc("id"));
        for (CouponInstance couponInstance : couponInstances) {
            if (couponInstance.getOrderId() != null && couponInstance.getTitle() != null) {
                couponInstanceTitles.put(couponInstance.getOrderId(), couponInstance.getTitle());
            }
            if (couponInstance.getOrderId() != null) {
                List<CouponInstance> group = couponInstancesByOrder.get(couponInstance.getOrderId());
                if (group == null) {
                    group = new ArrayList<CouponInstance>();
                    couponInstancesByOrder.put(couponInstance.getOrderId(), group);
                }
                group.add(couponInstance);
            }
        }
        Map<Integer, String> couponGoodsTitles = new HashMap<Integer, String>();
        if (!couponGoodsIds.isEmpty()) {
            List<CouponGoods> couponGoods = this.couponGoodsMapper.selectBatchIds(couponGoodsIds);
            for (CouponGoods goods : couponGoods) {
                couponGoodsTitles.put(goods.getId(), goods.getTitle());
            }
        }

        // 活动历史数据可能只把报名明细置为已取消而订单仍是待支付，读模型兜底展示「已取消」。
        Map<Integer, Integer> signupTotals = new HashMap<Integer, Integer>();
        Map<Integer, Integer> signupCancelled = new HashMap<Integer, Integer>();
        List<ActivitySignup> signups = this.activitySignupMapper.selectList(
                new QueryWrapper<ActivitySignup>().in("order_id", orderIds));
        for (ActivitySignup signup : signups) {
            Integer orderId = signup.getOrderId();
            if (orderId == null) {
                continue;
            }
            Integer total = signupTotals.get(orderId);
            signupTotals.put(orderId, total == null ? 1 : total + 1);
            if (signup.getStatus() != null && signup.getStatus().intValue() == ActivitySignup.STATUS_CANCELLED) {
                Integer cancelled = signupCancelled.get(orderId);
                signupCancelled.put(orderId, cancelled == null ? 1 : cancelled + 1);
            }
        }

        // #60 退款任务：一单最多一条，按 id 升序后取最后一条（新任务覆盖旧任务）。
        Map<Integer, Integer> refundTaskStatus = new HashMap<Integer, Integer>();
        List<RefundTask> refundTasks = this.refundTaskMapper.selectList(
                new QueryWrapper<RefundTask>().in("order_id", orderIds).orderByAsc("id"));
        for (RefundTask refundTask : refundTasks) {
            if (refundTask.getOrderId() != null) {
                refundTaskStatus.put(refundTask.getOrderId(), refundTask.getStatus());
            }
        }

        List<OrderListVo> list = new ArrayList<OrderListVo>(orders.size());
        for (RechargeOrder order : orders) {
            String type = OrderStatusUtils.resolveType(order.getActivityId(), order.getCourseClassId(), order.getCouponGoodsId());
            Integer totalSignup = signupTotals.get(order.getId());
            Integer cancelledSignup = signupCancelled.get(order.getId());
            boolean activityCancelled = totalSignup != null && totalSignup.intValue() > 0
                    && cancelledSignup != null && cancelledSignup.intValue() >= totalSignup.intValue();
            Integer taskStatus = refundTaskStatus.get(order.getId());
            String status = OrderStatusUtils.resolveStatus(order.getPayStatus(), order.getRefundStatus(), taskStatus, activityCancelled);

            OrderListVo vo = new OrderListVo();
            vo.setId(order.getId());
            vo.setOrderSn(order.getSn());
            vo.setOrderAmount(order.getOrderAmount());
            vo.setCreateTime(TimeUtils.timestampToDate(order.getCreateTime() != null ? order.getCreateTime() : order.getPayTime()));
            vo.setPayStatus(order.getPayStatus());
            vo.setRefundStatus(order.getRefundStatus() == null ? Integer.valueOf(0) : order.getRefundStatus());
            vo.setRefundTaskStatus(taskStatus);
            vo.setRefundTaskStatusText(OrderStatusUtils.refundTaskStatusText(taskStatus));
            vo.setType(type);
            vo.setTypeText(OrderStatusUtils.typeText(type));
            vo.setCouponGoodsId(order.getCouponGoodsId());
            vo.setCourseClassId(order.getCourseClassId());
            vo.setActivityId(order.getActivityId());
            vo.setActivityCount(order.getActivityCount());
            vo.setStatus(status);
            vo.setStatusText(OrderStatusUtils.statusText(status));
            vo.setStatusHint(OrderStatusUtils.statusHint(status, type, taskStatus));
            if (OrderStatusUtils.TYPE_ACTIVITY.equals(type)) {
                String title = activityTitles.get(order.getActivityId());
                vo.setTitle(title == null || title.isEmpty() ? "活动报名" : title);
            } else if (OrderStatusUtils.TYPE_COURSE.equals(type)) {
                String title = courseNames.get(order.getCourseClassId());
                vo.setTitle(title == null || title.isEmpty() ? "课程订单" : title);
            } else if (OrderStatusUtils.TYPE_COUPON.equals(type)) {
                String title = couponInstanceTitles.get(order.getId());
                if (title == null || title.isEmpty()) {
                    title = couponGoodsTitles.get(order.getCouponGoodsId());
                }
                vo.setTitle(title == null || title.isEmpty() ? "课程券订单" : title);
                // #110 T12：券订单退款入口与券页同口径（未用单券可退，已用/券包转客服）。
                List<CouponInstance> orderCoupons = couponInstancesByOrder.get(order.getId());
                int couponCount = orderCoupons == null ? 0 : orderCoupons.size();
                boolean couponUsed = CouponRefundRules.used(orderCoupons, false);
                boolean couponPack = CouponRefundRules.pack(orderCoupons);
                String refundMode = !couponRefundEnabled ? CouponRefundRules.MODE_NONE
                        : CouponRefundRules.mode(couponCount, couponUsed, couponPack,
                                order.getPayStatus(), order.getRefundStatus());
                vo.setRefundMode(refundMode);
                vo.setRefundHint(CouponRefundRules.hint(refundMode, couponCount, couponUsed, couponPack));
            } else {
                vo.setTitle("订单 " + order.getId());
            }
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public Map<String, Object> placeOrder(Integer userId, Integer terminal, RechargeValidate rechargeValidate) {
        RechargeConfigVo config = this.config(userId);
        if (config.getStatus().equals(0)) {
            throw new OperateException("\u5145\u503c\u529f\u80fd\u5df2\u5173\u95ed");
        }
        if (rechargeValidate.getMoney().compareTo(config.getMinAmount()) < 0) {
            throw new OperateException("\u5145\u503c\u91d1\u989d\u4e0d\u80fd\u5c11\u4e8e" + config.getMinAmount());
        }
        RechargeOrder order = new RechargeOrder();
        order.setUserId(userId);
        order.setOrderTerminal(terminal);
        order.setSn(this.rechargeOrderMapper.randMakeOrderSn("sn"));
        order.setPayStatus(Integer.valueOf(0));
        order.setRefundStatus(Integer.valueOf(0));
        order.setOrderAmount(rechargeValidate.getMoney());
        order.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        order.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.rechargeOrderMapper.insert(order);
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("order_id", order.getId());
        response.put("from", "recharge");
        return response;
    }

    @Transactional
    public void updatePayOrderStatusToPaid(String outTradeNo, String tradeNo) {
        RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>().eq("sn", (Object)outTradeNo).isNull("delete_time"));
        if (StringUtils.isNull(order)) {
            return;
        }
        if (order.getPayStatus().equals(PaymentEnum.OK_PAID.getCode())) {
            return;
        }
        Integer userId = order.getUserId();
        String orderSn = outTradeNo;
        User user = (User)this.userMapper.selectById((Serializable)order.getUserId());
        user.setUserMoney(user.getUserMoney().add(order.getOrderAmount()));
        user.setTotalRechargeAmount(user.getTotalRechargeAmount().add(order.getOrderAmount()));
        this.userMapper.updateById(user);
        this.userAccountLogMapper.add(userId, Integer.valueOf(AccountLogEnum.UM_INC_RECHARGE.getCode()), order.getOrderAmount(), order.getId(), order.getSn(), "", "");
        order.setTransactionId(tradeNo);
        order.setPayStatus(Integer.valueOf(PaymentEnum.OK_PAID.getCode()));
        order.setPayTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.rechargeOrderMapper.updateById(order);
    }
}

