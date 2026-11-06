/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.ICourseRefundService
 *  com.mdd.admin.service.impl.CourseRefundServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.refund.CourseRefundAuditValidate
 *  com.mdd.admin.validate.refund.CourseRefundSearchValidate
 *  com.mdd.admin.vo.refund.CourseRefundListedVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.entity.course.CourseRefund
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.course.CourseRefundMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.plugin.wechat.WxPayDriver
 *  com.mdd.common.plugin.wechat.request.RefundRequestV3
 *  com.mdd.common.util.AmountUtil
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UserCourseEligibilityUtil
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.ICourseRefundService;
import com.mdd.admin.service.IFinanceRechargerService;
import com.mdd.admin.service.IRefundTaskService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.refund.CourseRefundAuditValidate;
import com.mdd.admin.validate.refund.CourseRefundSearchValidate;
import com.mdd.admin.vo.refund.CourseRefundListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.refund.RefundTask;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseRefundMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.refund.RefundTaskMapper;
import com.mdd.common.service.ActivitySignupCancelService;
import com.mdd.common.service.CourseConsumeEffectiveService;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.CourseBookingUtil;
import com.mdd.common.util.CourseRefundAmountUtil;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UserCourseEligibilityUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseRefundServiceImpl
implements ICourseRefundService {
    private static final Logger log = LoggerFactory.getLogger(CourseRefundServiceImpl.class);
    @Resource
    CourseRefundMapper courseRefundMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    AdminMapper adminMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    RefundTaskMapper refundTaskMapper;
    @Resource
    IRefundTaskService refundTaskService;
    @Resource
    IFinanceRechargerService financeRechargerService;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    CourseConsumeEffectiveService courseConsumeEffectiveService;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    ActivitySignupCancelService activitySignupCancelService;

    public PageResult<CourseRefundListedVo> list(PageValidate pageValidate, CourseRefundSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<CourseRefund> queryWrapper = new QueryWrapper<CourseRefund>();
        queryWrapper.orderByDesc("id");
        if (StringUtils.isNotNull(searchValidate.getStatus()) && searchValidate.getStatus() != -1) {
            queryWrapper.eq("status", (Object)searchValidate.getStatus());
        }
        if (StringUtils.isNotEmpty((String)searchValidate.getUserName())) {
            queryWrapper.like("user_name", (Object)searchValidate.getUserName());
        }
        // #110 T12 来源筛选：券退款写 course_class_id=0，课程退款恒为 >0。
        if ("coupon".equals(searchValidate.getSource())) {
            queryWrapper.eq("course_class_id", (Object)0);
        } else if ("course".equals(searchValidate.getSource())) {
            queryWrapper.ne("course_class_id", (Object)0);
        }
        IPage<CourseRefund> iPage = this.courseRefundMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        Set<Integer> activityOrderIds = this.activityOrderIds(iPage.getRecords());
        LinkedList<CourseRefundListedVo> list = new LinkedList<CourseRefundListedVo>();
        for (CourseRefund item : iPage.getRecords()) {
            CourseRefundListedVo vo = new CourseRefundListedVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            vo.setStatusDesc(this.getStatusDesc(item.getStatus()));
            // #110 T12 / #213 C3：course_class_id=0 同时承载券退款与活动退款，
            // 活动退款按关联订单 activity_id 区分来源，避免后台把活动退款显示成「券」。
            boolean zeroClass = item.getCourseClassId() == null || item.getCourseClassId() == 0;
            boolean activity = zeroClass && item.getOrderId() != null
                    && activityOrderIds.contains(item.getOrderId());
            boolean coupon = zeroClass && !activity;
            vo.setSource(activity ? "activity" : (coupon ? "coupon" : "course"));
            vo.setSourceText(activity ? "活动" : (coupon ? "券" : "课程"));
            vo.setPayAmount(item.getPayAmount() != null ? item.getPayAmount().toString() : "0.00");
            vo.setRefundAmount(item.getRefundAmount() != null ? item.getRefundAmount().toString() : "0.00");
            vo.setAuditTime(TimeUtils.timestampToDate((Long)item.getAuditTime()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)item.getCreateTime()));
            list.add(vo);
        }
        this.attachRefundTasks(list);
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    @Transactional(rollbackFor={Exception.class})
    public void audit(CourseRefundAuditValidate auditValidate, Integer adminId) {
        String auditRemark = StringUtils.trim(auditValidate.getAuditRemark());
        if (auditValidate.getStatus() == null || auditValidate.getStatus() != 1 && auditValidate.getStatus() != 2) {
            throw new OperateException("\u5ba1\u6838\u72b6\u6001\u53d6\u503c\u4e0d\u5408\u6cd5(1=\u901a\u8fc7,2=\u62d2\u7edd)!");
        }
        if (auditValidate.getStatus() == 2 && StringUtils.isEmpty(auditRemark)) {
            throw new OperateException("\u62d2\u7edd\u9000\u8bfe\u5fc5\u987b\u586b\u5199\u539f\u56e0!");
        }
        // 行锁 + status 二次判断：并发审核同一申请时（双人同时点通过）只有一个事务能拿到锁，
        // 后到者看到 status!=0 直接报错，避免重复销课/重复调用微信退款（资金幂等，#58）。
        CourseRefund refund = (CourseRefund)this.courseRefundMapper.selectOne(
                new QueryWrapper<CourseRefund>().eq("id", (Object)auditValidate.getId()).last("limit 1 for update"));
        if (refund == null) {
            throw new OperateException("\u9000\u8bfe\u7533\u8bf7\u4e0d\u5b58\u5728!");
        }
        if (refund.getStatus() != 0) {
            throw new OperateException("\u53ea\u80fd\u5bf9\u3010\u5f85\u5ba1\u6279\u3011\u72b6\u6001\u7684\u8bb0\u5f55\u8fdb\u884c\u5ba1\u6279!");
        }
        Admin admin = (Admin)this.adminMapper.selectById((Serializable)adminId);
        RechargeOrder exactOrder = null;
        UserCourse userCourse = null;
        if (auditValidate.getStatus() == 1 && refund.getCourseClassId() != null && refund.getCourseClassId() > 0) {
            // 审核通过时必须按「当前实际消课流水」重算，不能沿用申请时的 used_lessons / refund_amount 快照：
            // 申请到审核之间可能又上了课，且老核销链路不回写 used_lessons（上完课仍可能全退）。
            exactOrder = this.resolveRefundOrder(refund);
            userCourse = (UserCourse)this.userCourseMapper.selectOne(new QueryWrapper<UserCourse>().eq("user_id", (Object)refund.getUserId()).eq("course_class_id", (Object)refund.getCourseClassId()).eq("order_id", (Object)exactOrder.getId()).isNull("delete_time").last("limit 1"));
            if (userCourse == null) {
                throw new OperateException("\u9000\u8bfe\u7533\u8bf7\u65e0\u6cd5\u5339\u914d\u539f\u62a5\u540d\u8bb0\u5f55\uff0c\u8bf7\u6838\u5bf9\u8ba2\u5355\u6570\u636e");
            }
            this.applyRecomputedRefundAmount(refund, exactOrder, userCourse);
        }
        refund.setStatus(auditValidate.getStatus());
        refund.setAuditId(adminId);
        refund.setAuditName(admin != null ? admin.getName() : "\u7ba1\u7406\u5458");
        refund.setAuditTime(TimeUtils.timestamp());
        refund.setAuditRemark(auditRemark);
        refund.setUpdateTime(TimeUtils.timestamp());
        this.courseRefundMapper.updateById(refund);
        if (auditValidate.getStatus() == 1) {
            if (refund.getCourseClassId() == null || refund.getCourseClassId() == 0) {
                RechargeOrder activityOrder = refund.getOrderId() == null || refund.getOrderId() <= 0
                        ? null : this.rechargeOrderMapper.selectById(refund.getOrderId());
                if (activityOrder != null && activityOrder.getActivityId() != null
                        && activityOrder.getActivityId() > 0) {
                    // #213 C3：活动退款复用 la_course_refund（course_class_id=0），
                    // 按订单 activity_id 路由到活动履约回滚 + 既有退款通道。
                    this.settleActivityRefund(refund, adminId, activityOrder);
                    return;
                }
                // #110 T12 券退款：券订单没有 la_user_course，走独立的作废 + 原路退分支。
                this.settleCouponRefund(refund, adminId);
                return;
            }

            // outbox（#60）：只有真正需要调用微信退款的订单才落任务；钱包订单同步退回余额，
            // 不可退/已退/零元订单不建任务，后台列表不会再把「无需线上退款」显示成「退款成功」。
            if (refund.getRefundAmount() != null && refund.getRefundAmount().compareTo(BigDecimal.ZERO) > 0) {
                RechargeOrder refundableOrder = (RechargeOrder)this.rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>().eq("id", (Object)exactOrder.getId()).eq("pay_status", (Object)1).eq("refund_status", (Object)0).isNull("delete_time").last("limit 1"));
                if (refundableOrder != null) {
                    boolean wechatPaid = refundableOrder.getPayWay() != null && refundableOrder.getPayWay() == 2
                            && refundableOrder.getTransactionId() != null && !refundableOrder.getTransactionId().isEmpty();
                    boolean walletPaid = refundableOrder.getPayWay() != null && refundableOrder.getPayWay() == 1;
                    if (wechatPaid) {
                        // 微信退款必须异步 outbox：审核事务内不直连微信，失败可重试。
                        // #152：同单唯一放行——先锁 order 行并校验同单无未完结退款任务再落任务；
                        // 并发审核在同一行锁上串行，后到者看到同单已有任务直接拒绝，杜绝双重退款。
                        this.claimOrderForWechatRefund(refundableOrder);
                        this.refundTaskService.createForRefund(refund, refundableOrder);
                    } else if (walletPaid) {
                        // #61 钱包支付课程退款：余额加回（部分退款按实际应退金额），
                        // 同步写退款记录/流水；不走退款任务，避免后台出现虚假的「退款成功」任务。
                        // 注意：FinanceRechargerService 用 REQUIRES_NEW 独立提交，内部也会更新
                        // la_user_course；因此本方法把 user_course 的最终更新放到退款之后，
                        // 避免外层先锁住该行、内层新事务等待造成死锁。
                        this.financeRechargerService.refund(refundableOrder.getId(), adminId,
                                refund.getRefundAmount());
                    } else {
                        // 非微信/钱包（如线下转账）：保持原行为，审核即视为退款完成，不建任务。
                        // #152：整行 updateById 改条件更新（refund_status 0→1），并发双审核只有一个置位成功。
                        this.rechargeOrderMapper.update(null, new UpdateWrapper<RechargeOrder>()
                                .eq("id", (Object)refundableOrder.getId())
                                .eq("refund_status", (Object)0)
                                .isNull("delete_time")
                                .set("refund_status", Integer.valueOf(1))
                                .set("update_time", Long.valueOf(System.currentTimeMillis() / 1000L)));
                    }
                }
            }
            userCourse.setStatus(Integer.valueOf(2));
            userCourse.setRemainLessons(Integer.valueOf(0));
            userCourse.setUpdateTime(TimeUtils.timestamp());
            this.userCourseMapper.updateById(userCourse);
            // 审计发现项：退款通过后，该课包未上课的有效预约必须一并取消（status=3），
            // 否则已退费学员仍能按原预约到课/被点名。
            this.cancelFutureBookings(refund, userCourse);
        }
    }

    /**
     * #213 C3 活动退款收口：先走既有缴费退款通道（微信 outbox / 钱包同步 / 其它渠道），
     * 再取消该订单全部报名行、批次置取消并按批次整批释放名额。
     *
     * <p>幂等：order.refund_status=1 时跳过外部退款，只做本地回滚（重试补偿）；
     * 批次状态是名额释放唯一闸门，重复执行不会二次扣减 joined。</p>
     */
    private void settleActivityRefund(CourseRefund refund, Integer adminId, RechargeOrder order) {
        if (order.getDeleteTime() != null
                || refund.getUserId() == null || !refund.getUserId().equals(order.getUserId())
                || order.getActivityId() == null || order.getActivityId() <= 0) {
            throw new OperateException("活动退款申请关联的订单不存在或状态异常");
        }
        if (order.getPayStatus() == null || order.getPayStatus() != 1) {
            throw new OperateException("活动订单未支付，不能退款");
        }
        BigDecimal orderAmount = order.getOrderAmount() == null ? BigDecimal.ZERO : order.getOrderAmount();
        BigDecimal refundAmount = refund.getRefundAmount() == null ? orderAmount : refund.getRefundAmount();
        if (refundAmount.compareTo(BigDecimal.ZERO) < 0 || refundAmount.compareTo(orderAmount) > 0) {
            throw new OperateException("活动退款金额不合法");
        }
        int refundStatus = order.getRefundStatus() == null ? 0 : order.getRefundStatus();
        if (refundStatus == 0 && refundAmount.compareTo(BigDecimal.ZERO) > 0) {
            boolean wechatPaid = order.getPayWay() != null && order.getPayWay() == 2
                    && StringUtils.isNotEmpty(order.getTransactionId());
            boolean walletPaid = order.getPayWay() != null && order.getPayWay() == 1;
            if (wechatPaid) {
                // 同一退款申请已有任务时直接复用（重试补偿），不再二次放行/二次退款。
                RefundTask existingTask = this.refundTaskMapper.selectOne(new QueryWrapper<RefundTask>()
                        .eq("refund_id", refund.getId()).last("limit 1"));
                if (existingTask == null) {
                    this.claimOrderForWechatRefund(order);
                    this.refundTaskService.createForRefund(refund, order);
                }
            } else if (walletPaid) {
                this.financeRechargerService.refund(order.getId(), adminId, refundAmount);
            } else {
                // 线下转账/无交易号：与课程/券退款一致，审核即视为退款完成。
                this.rechargeOrderMapper.update(null, new UpdateWrapper<RechargeOrder>()
                        .eq("id", order.getId())
                        .eq("refund_status", 0)
                        .isNull("delete_time")
                        .set("refund_status", 1)
                        .set("update_time", TimeUtils.timestamp()));
            }
        }
        int released = this.activitySignupCancelService.cancelOrderSignups(order.getId());
        log.info("活动退款审核通过: refundId={} orderId={} activityId={} released={} adminId={}",
                refund.getId(), order.getId(), order.getActivityId(), released, adminId);
    }

    /** 批量标记 course_class_id=0 的退款单中，关联订单属于活动订单的集合（用于来源展示）。 */
    private Set<Integer> activityOrderIds(List<CourseRefund> records) {
        Set<Integer> result = new HashSet<>();
        if (records == null || records.isEmpty()) {
            return result;
        }
        Set<Integer> orderIds = new HashSet<>();
        for (CourseRefund item : records) {
            if ((item.getCourseClassId() == null || item.getCourseClassId() == 0)
                    && item.getOrderId() != null && item.getOrderId() > 0) {
                orderIds.add(item.getOrderId());
            }
        }
        if (orderIds.isEmpty()) {
            return result;
        }
        List<RechargeOrder> orders = this.rechargeOrderMapper.selectList(new QueryWrapper<RechargeOrder>()
                .select("id", "activity_id")
                .in("id", orderIds));
        for (RechargeOrder order : orders) {
            if (order.getActivityId() != null && order.getActivityId() > 0) {
                result.add(order.getId());
            }
        }
        return result;
    }

    /**
     * #110 T12 券退款收口：作废该订单全部券实例 + 每张写一条 type=5 退回流水，
     * 再复用 #60 outbox 原路退（微信异步任务 / 钱包同步退回 / 其它渠道直接置已退）。
     *
     * <p>幂等：已作废的券跳过，不重复写退回流水；订单已退款时在进入本方法前就报错。</p>
     */
    private void settleCouponRefund(CourseRefund refund, Integer adminId) {
        RechargeOrder order = this.rechargeOrderMapper.selectById(refund.getOrderId());
        boolean valid = order != null
                && refund.getUserId() != null && refund.getUserId().equals(order.getUserId())
                && order.getCouponGoodsId() != null && order.getCouponGoodsId() > 0
                && Integer.valueOf(1).equals(order.getPayStatus())
                && Integer.valueOf(0).equals(order.getRefundStatus() == null ? Integer.valueOf(0) : order.getRefundStatus())
                && order.getDeleteTime() == null;
        if (!valid) {
            throw new OperateException("券退款申请关联的订单不存在或状态异常");
        }
        List<CouponInstance> instances = this.couponInstanceMapper.selectList(
                new QueryWrapper<CouponInstance>().eq("order_id", order.getId()).orderByAsc("id"));
        if (instances.isEmpty()) {
            throw new OperateException("券退款申请未匹配到券实例，请核对订单数据");
        }
        long now = TimeUtils.timestamp();
        for (CouponInstance instance : instances) {
            if (instance.getStatus() != null && instance.getStatus() == 4) {
                continue;
            }
            // #152：作废改条件更新（status!=4 才置 4）。affected=0 说明并发审核已作废该券，
            // 跳过流水写入；流水只追加，无条件整行写回会让同单双审核重复落 type=5 退回流水。
            int voided = this.couponInstanceMapper.update(null, new UpdateWrapper<CouponInstance>()
                    .eq("id", (Object)instance.getId())
                    .ne("status", (Object)4)
                    .set("status", Integer.valueOf(4))
                    .set("remaining", Integer.valueOf(0))
                    .set("update_time", Long.valueOf(now)));
            if (voided <= 0) {
                continue;
            }
            CouponFlow flow = new CouponFlow();
            flow.setCouponId(instance.getId());
            flow.setUserId(instance.getUserId());
            flow.setStudentId(instance.getStudentId());
            flow.setType(Integer.valueOf(5));
            flow.setTimes(Integer.valueOf(1));
            flow.setBalance(Integer.valueOf(0));
            flow.setOrderId(order.getId());
            flow.setOrderSn(order.getSn());
            flow.setRef("REFUND" + refund.getId());
            flow.setOperatorType("admin");
            flow.setOperatorId(adminId);
            flow.setRemark("券退款审核通过，券已作废");
            flow.setCreateTime(now);
            this.couponFlowMapper.insert(flow);
        }
        if (refund.getRefundAmount() == null || refund.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        Integer payWay = order.getPayWay();
        boolean wechatPaid = payWay != null && payWay == 2 && StringUtils.isNotEmpty(order.getTransactionId());
        boolean walletPaid = payWay != null && payWay == 1;
        if (wechatPaid) {
            // 微信退款必须异步 outbox，审核事务内不直连微信；失败可重试。
            // #152：同单唯一放行——先锁 order 行并校验同单无未完结退款任务再落任务；
            // 并发审核在同一行锁上串行，后到者看到同单已有任务直接拒绝，杜绝双重退款。
            this.claimOrderForWechatRefund(order);
            this.refundTaskService.createForRefund(refund, order);
        } else if (walletPaid) {
            this.financeRechargerService.refund(order.getId(), adminId, refund.getRefundAmount());
        } else {
            // 其它渠道（线下转账/历史无交易号）：与课程退款一致，审核即视为退款完成。
            // #152：整行 updateById 改条件更新（refund_status 0→1），并发双审核只有一个置位成功。
            this.rechargeOrderMapper.update(null, new UpdateWrapper<RechargeOrder>()
                    .eq("id", (Object)order.getId())
                    .eq("refund_status", (Object)0)
                    .isNull("delete_time")
                    .set("refund_status", Integer.valueOf(1))
                    .set("update_time", Long.valueOf(now)));
        }
    }

    /**
     * #152 结算侧同单唯一放行：微信退款任务落库前对 order 行加锁（SELECT ... FOR UPDATE，
     * 当前读，不受 REPEATABLE READ 快照影响），再以当前读检查同单（order_id）是否已存在退款任务。
     *
     * <p>任意状态的既有任务（待执行/执行中/成功/失败）都算：失败任务必须走 retry 复用原
     * out_refund_no（微信侧幂等），绝不允许第二笔申请另起 out_refund_no 再放一笔。
     * 并发审核在同一 order 行锁上串行：后到者要么看到 refund_status!=0（已退款），
     * 要么看到同单已有任务（已放行未到账），均抛业务错误，保证同单只放行一笔退款任务。</p>
     *
     * <p>只能在「微信支付」分支内调用。钱包分支禁止调用：FinanceRechargerService 用
     * REQUIRES_NEW 独立事务更新 order 行，外层先锁 order 行会让内层条件更新等待自身锁，
     * 锁超时；钱包路径由该条件更新（refund_status 0→1，affected!=1 即拒绝）保证同单只退一次。</p>
     */
    private void claimOrderForWechatRefund(RechargeOrder order) {
        RechargeOrder locked = (RechargeOrder)this.rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>()
                .eq("id", (Object)order.getId()).last("limit 1 for update"));
        if (locked == null || locked.getDeleteTime() != null
                || !Integer.valueOf(1).equals(locked.getPayStatus())
                || !Integer.valueOf(0).equals(locked.getRefundStatus() == null
                        ? Integer.valueOf(0) : locked.getRefundStatus())) {
            throw new OperateException("订单退款状态已变化，请刷新后重试");
        }
        RefundTask existingTask = (RefundTask)this.refundTaskMapper.selectOne(new QueryWrapper<RefundTask>()
                .eq("order_id", (Object)locked.getId()).orderByAsc("id").last("limit 1 for update"));
        if (existingTask != null) {
            throw new OperateException("该订单已存在退款任务（退款处理中或已完成），请勿重复审核退款");
        }
    }

    private void attachRefundTasks(List<CourseRefundListedVo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<Integer> refundIds = new LinkedList<>();
        for (CourseRefundListedVo vo : list) {
            if (vo.getId() != null) {
                refundIds.add(vo.getId());
            }
        }
        if (refundIds.isEmpty()) {
            return;
        }
        List<RefundTask> tasks = this.refundTaskMapper.selectList(new QueryWrapper<RefundTask>().in("refund_id", refundIds));
        Map<Integer, RefundTask> taskMap = new HashMap<>();
        for (RefundTask task : tasks) {
            taskMap.put(task.getRefundId(), task);
        }
        for (CourseRefundListedVo vo : list) {
            RefundTask task = taskMap.get(vo.getId());
            if (task == null) {
                continue;
            }
            vo.setRefundTaskId(task.getId());
            vo.setRefundTaskStatus(task.getStatus());
            vo.setRefundTaskStatusDesc(this.getRefundTaskStatusDesc(task.getStatus()));
            vo.setRefundRetryCount(task.getRetryCount());
            vo.setRefundLastError(task.getLastError());
            vo.setRefundTaskResultMsg(task.getResultMsg());
        }
    }

    private String getRefundTaskStatusDesc(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case RefundTask.STATUS_PENDING: {
                return "\u5f85\u6267\u884c";
            }
            case RefundTask.STATUS_RUNNING: {
                return "\u6267\u884c\u4e2d";
            }
            case RefundTask.STATUS_SUCCESS: {
                return "\u6210\u529f";
            }
            case RefundTask.STATUS_FAILED: {
                return "\u5931\u8d25";
            }
        }
        return "\u672a\u77e5";
    }

    /**
     * 审核通过时按实际消课流水重算已用课时与应退金额，覆盖申请时写下的快照。
     *
     * <p>已用课时口径由 {@link com.mdd.common.service.CourseConsumeEffectiveService} 统一提供
     * （过滤无效流水 + 同班多课包 FIFO 分配），金额公式与前端申请时一致：
     * 应退 = 实付 - 实际已用课时 × 单节课时费。</p>
     */
    private void applyRecomputedRefundAmount(CourseRefund refund, RechargeOrder order, UserCourse userCourse) {
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)refund.getCourseClassId());
        int totalLessons = userCourse.getTotalLessons() != null && userCourse.getTotalLessons() > 0
                ? userCourse.getTotalLessons()
                : (courseClass != null && courseClass.getTotalLessons() != null && courseClass.getTotalLessons() > 0
                        ? courseClass.getTotalLessons()
                        : (order.getLessonCount() != null ? order.getLessonCount() : 0));
        BigDecimal payAmount = order.getOrderAmount() != null ? order.getOrderAmount() : refund.getPayAmount();
        int effectiveUsedLessons = this.courseConsumeEffectiveService.countEffectiveUsedLessonsForEnrollment(userCourse);
        CourseRefundAmountUtil.Result calc = CourseRefundAmountUtil.resolve(
                payAmount,
                CourseRefundAmountUtil.resolveLessonFee(
                        courseClass != null ? courseClass.getFeePerLesson() : null,
                        courseClass != null ? courseClass.getTotalFee() : null,
                        totalLessons),
                totalLessons,
                effectiveUsedLessons);
        refund.setPayAmount(payAmount == null ? BigDecimal.ZERO : payAmount);
        refund.setTotalLessons(Integer.valueOf(calc.getTotalLessons()));
        refund.setUsedLessons(Integer.valueOf(calc.getUsedLessons()));
        refund.setRefundAmount(calc.getRefundAmount());
    }

    /**
     * 退款通过后取消该课包「未上课」的有效预约（status=1 → 3）。已结束的课次保留原始预约，
     * 供对账/审计；未结束判定与注销账号共用 {@link CourseBookingUtil} 口径。
     */
    private void cancelFutureBookings(CourseRefund refund, UserCourse userCourse) {
        QueryWrapper<CourseBooking> bookingQuery = new QueryWrapper<CourseBooking>()
                .eq("user_id", (Object)refund.getUserId())
                .eq("course_class_id", (Object)refund.getCourseClassId())
                .eq("status", (Object)CourseBookingUtil.STATUS_BOOKED);
        // 同一班级可能有多名学员（兄弟姐妹），只取消本次退款课包对应学员的预约。
        if (userCourse.getStudentId() != null && userCourse.getStudentId() > 0) {
            bookingQuery.eq("student_id", (Object)userCourse.getStudentId());
        }
        List<CourseBooking> bookings = this.courseBookingMapper.selectList(bookingQuery);
        if (bookings.isEmpty()) {
            return;
        }
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)refund.getCourseClassId());
        long now = TimeUtils.timestamp();
        for (CourseBooking booking : bookings) {
            CourseLesson lesson = this.resolveBookingLesson(booking);
            String lessonDate = booking.getLessonDate() != null && !booking.getLessonDate().isEmpty()
                    ? booking.getLessonDate()
                    : (lesson != null ? lesson.getLessonDate() : "");
            String timeSlot = lesson != null ? ClassScheduleUtil.resolveLessonTimeSlot(lesson, courseClass) : "";
            if (CourseBookingUtil.isLessonEnded(lessonDate, timeSlot)) {
                continue;
            }
            CourseBooking update = new CourseBooking();
            update.setId(booking.getId());
            update.setStatus(Integer.valueOf(CourseBookingUtil.STATUS_CANCELLED));
            update.setUpdateTime(Long.valueOf(now));
            this.courseBookingMapper.updateById(update);
        }
    }

    private CourseLesson resolveBookingLesson(CourseBooking booking) {
        if (booking.getLessonId() != null && booking.getLessonId() > 0) {
            return (CourseLesson)this.courseLessonMapper.selectById((Serializable)booking.getLessonId());
        }
        if (booking.getCourseClassId() != null && booking.getLessonDate() != null) {
            return (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>()
                    .eq("class_id", (Object)booking.getCourseClassId())
                    .eq("lesson_date", (Object)booking.getLessonDate())
                    .last("limit 1"));
        }
        return null;
    }

    private RechargeOrder resolveRefundOrder(CourseRefund refund) {
        if (refund.getOrderId() != null && refund.getOrderId() > 0) {
            RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)refund.getOrderId());
            if (!(order != null && refund.getUserId().equals(order.getUserId()) && refund.getCourseClassId().equals(order.getCourseClassId()) && UserCourseEligibilityUtil.isExactOrder((Integer)refund.getOrderId(), (Integer)order.getId()) && UserCourseEligibilityUtil.isActiveOrder((Integer)order.getPayStatus(), (Integer)order.getRefundStatus(), (Long)order.getDeleteTime()))) {
                throw new OperateException("\u9000\u8bfe\u7533\u8bf7\u5173\u8054\u7684\u539f\u8ba2\u5355\u4e0d\u5b58\u5728\u6216\u72b6\u6001\u5f02\u5e38");
            }
            return order;
        }
        List<UserCourse> enrollments = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)refund.getUserId()).eq("course_class_id", (Object)refund.getCourseClassId()).ne("status", (Object)2).isNull("delete_time").orderByAsc("id"));
        LinkedHashMap<Integer, RechargeOrder> activeOrders = new LinkedHashMap<Integer, RechargeOrder>();
        for (UserCourse enrollment : enrollments) {
            RechargeOrder order;
            if (enrollment.getOrderId() == null || enrollment.getOrderId() <= 0 || (order = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)enrollment.getOrderId())) == null || !UserCourseEligibilityUtil.isActiveOrder((Integer)order.getPayStatus(), (Integer)order.getRefundStatus(), (Long)order.getDeleteTime())) continue;
            activeOrders.putIfAbsent(order.getId(), order);
        }
        if (activeOrders.size() != 1) {
            throw new OperateException("\u5386\u53f2\u9000\u8bfe\u7533\u8bf7\u65e0\u6cd5\u552f\u4e00\u5339\u914d\u539f\u8ba2\u5355\uff0c\u8bf7\u5148\u6838\u5bf9\u5177\u4f53\u5b66\u5458\u8ba2\u5355");
        }
        return (RechargeOrder)activeOrders.values().iterator().next();
    }

    private String getStatusDesc(Integer status) {
        switch (status) {
            case 0: {
                return "\u5f85\u5ba1\u6279";
            }
            case 1: {
                return "\u5df2\u901a\u8fc7";
            }
            case 2: {
                return "\u5df2\u62d2\u7edd";
            }
        }
        return "\u672a\u77e5";
    }
}

