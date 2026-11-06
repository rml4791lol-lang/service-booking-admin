/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Coupon
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.CouponMapper
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.student.StudentMapper
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.front.service.ICourseOrderService
 *  com.mdd.front.service.impl.CourseOrderServiceImpl
 *  com.mdd.front.validate.course.CourseOrderPageValidate
 *  com.mdd.front.validate.course.CourseOrderValidate
 *  com.mdd.front.vo.course.CourseOrderListVo
 *  com.mdd.front.vo.course.CourseOrderStatsVo
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Coupon;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.student.Student;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.CouponMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.util.TimeUtils;
import com.mdd.front.service.ICourseOrderService;
import com.mdd.common.util.ParamUtils;
import com.mdd.front.validate.course.CourseOrderPageValidate;
import com.mdd.front.validate.course.CourseOrderValidate;
import com.mdd.front.vo.course.CourseOrderListVo;
import com.mdd.front.vo.course.CourseOrderStatsVo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseOrderServiceImpl
implements ICourseOrderService {
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CouponMapper couponMapper;
    @Resource
    StudentMapper studentMapper;

    @Override
    @Transactional
    public Map<String, Object> placeOrder(Integer userId, Integer terminal, CourseOrderValidate courseOrderValidate) {
        BigDecimal payAmount;
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)courseOrderValidate.getCourseId());
        if (courseClass == null || courseClass.getIsDelete() == 1) {
            throw new OperateException("\u8be5\u8bfe\u7a0b\u4e0d\u5b58\u5728\u6216\u5df2\u4e0b\u67b6");
        }
        Integer lessonCount = courseOrderValidate.getLessonCount();
        if (lessonCount == null || lessonCount < 1) {
            throw new OperateException("\u8d2d\u4e70\u8bfe\u65f6\u6570\u4e0d\u80fd\u5c0f\u4e8e1");
        }
        Integer studentId = courseOrderValidate.getStudentId();
        if (studentId == null || studentId <= 0) {
            throw new OperateException("\u8bf7\u9009\u62e9\u62a5\u540d\u5b66\u5458");
        }
        Student student = (Student)this.studentMapper.selectOne(new QueryWrapper<Student>().eq("id", (Object)studentId).eq("user_id", (Object)userId).isNull("delete_time").last("limit 1"));
        if (student == null) {
            throw new OperateException("\u6240\u9009\u5b66\u5458\u4e0d\u5b58\u5728\u6216\u4e0d\u5c5e\u4e8e\u5f53\u524d\u7528\u6237");
        }
        // 单价口径与课程详情一致：优先单节课时费 fee_per_lesson，未维护时回退整包总费用 total_fee。
        BigDecimal unitPrice = courseClass.getFeePerLesson() != null
                && courseClass.getFeePerLesson().signum() > 0
                ? courseClass.getFeePerLesson()
                : (courseClass.getTotalFee() != null ? courseClass.getTotalFee() : BigDecimal.ZERO);
        BigDecimal orderAmount = unitPrice.multiply(new BigDecimal(lessonCount));
        BigDecimal couponAmount = BigDecimal.ZERO;
        Coupon coupon = null;
        if (courseOrderValidate.getCouponId() != null) {
            coupon = (Coupon)this.couponMapper.selectById((Serializable)courseOrderValidate.getCouponId());
            if (coupon == null || coupon.getDeleteTime() != null) {
                throw new OperateException("\u62b5\u6263\u5238\u4e0d\u5b58\u5728");
            }
            if (!coupon.getUserId().equals(userId)) {
                throw new OperateException("\u62b5\u6263\u5238\u4e0d\u5c5e\u4e8e\u5f53\u524d\u7528\u6237");
            }
            if (coupon.getStatus() != 0) {
                throw new OperateException("\u62b5\u6263\u5238\u5df2\u4f7f\u7528\u6216\u5df2\u8fc7\u671f");
            }
            long now = System.currentTimeMillis() / 1000L;
            if (coupon.getExpireTime() != null && coupon.getExpireTime() < now) {
                throw new OperateException("\u62b5\u6263\u5238\u5df2\u8fc7\u671f");
            }
            if (coupon.getMinAmount() != null && orderAmount.compareTo(coupon.getMinAmount()) < 0) {
                throw new OperateException("\u8ba2\u5355\u91d1\u989d\u4e0d\u6ee1\u8db3\u62b5\u6263\u5238\u4f7f\u7528\u6761\u4ef6");
            }
            couponAmount = coupon.getAmount();
        }
        if ((payAmount = orderAmount.subtract(couponAmount)).compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }
        RechargeOrder order = new RechargeOrder();
        order.setUserId(userId);
        order.setStudentId(studentId);
        order.setCourseClassId(courseOrderValidate.getCourseId());
        order.setLessonCount(lessonCount);
        order.setSn(this.rechargeOrderMapper.randMakeOrderSn("sn"));
        order.setOrderAmount(payAmount);
        // 记录抵扣券信息：取消未支付订单时据此把券退回（否则券会永久停留在已使用状态）。
        if (coupon != null) {
            order.setCouponId(coupon.getId());
            order.setCouponAmount(couponAmount);
        }
        order.setPayWay(courseOrderValidate.getPayWay());
        order.setPayStatus(Integer.valueOf(0));
        order.setRefundStatus(Integer.valueOf(0));
        order.setOrderTerminal(terminal);
        order.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        order.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.rechargeOrderMapper.insert(order);
        if (coupon != null) {
            // #151：券占用改条件更新（WHERE status=0 AND delete_time IS NULL），不再 select 后整行 updateById。
            // 并发同券两单时由数据库行锁保证只有一单影响行数为 1；抢空的一方抛异常，
            // 连同订单插入一起回滚，不会出现「双立减」或「订单按优惠价落库而券仍可用」。
            long usedTime = System.currentTimeMillis() / 1000L;
            int claimed = this.couponMapper.update(null, new UpdateWrapper<Coupon>()
                    .set("status", 1)
                    .set("used_order_id", order.getId())
                    .set("used_time", usedTime)
                    .set("update_time", usedTime)
                    .eq("id", coupon.getId())
                    .eq("status", 0)
                    .isNull("delete_time"));
            if (claimed <= 0) {
                throw new OperateException("\u62b5\u6263\u5238\u5df2\u4f7f\u7528\u6216\u5df2\u8fc7\u671f");
            }
        }
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("order_id", order.getId());
        response.put("from", "course");
        return response;
    }

    public PageResult<CourseOrderListVo> orderList(CourseOrderPageValidate pageValidate, Integer userId) {
        Page page = new Page((long)pageValidate.getPage_no().intValue(), (long)pageValidate.getPage_size().intValue());
        QueryWrapper<RechargeOrder> queryWrapper = new QueryWrapper<RechargeOrder>();
        queryWrapper.eq("user_id", (Object)userId);
        queryWrapper.isNotNull("course_class_id").ne("course_class_id", (Object)0);
        if (pageValidate.getStatus() != null && !pageValidate.getStatus().isEmpty()) {
            switch (pageValidate.getStatus()) {
                case "pending": {
                    queryWrapper.eq("pay_status", (Object)0).eq("refund_status", (Object)0);
                    break;
                }
                case "paid": {
                    queryWrapper.eq("pay_status", (Object)1).eq("refund_status", (Object)0);
                    break;
                }
                case "completed": {
                    queryWrapper.eq("pay_status", (Object)1).eq("refund_status", (Object)0);
                    break;
                }
                case "cancelled": {
                    queryWrapper.eq("pay_status", (Object)4);
                }
            }
        }
        if (pageValidate.getKeyword() != null && !pageValidate.getKeyword().isEmpty()) {
            String kw = pageValidate.getKeyword().trim();
            // 关键字用参数占位绑定，禁止拼接用户输入（原实现 inSql 直接拼 kw，存在 SQL 注入）。
            queryWrapper.and(wrapper -> wrapper.like("sn", (Object)kw)
                    .or()
                    .apply("course_class_id IN (SELECT id FROM la_course_class WHERE name LIKE {0})",
                            "%" + kw + "%"));
        }
        queryWrapper.orderByDesc("id");
        IPage<RechargeOrder> iPage = this.rechargeOrderMapper.selectPage((IPage)page, (Wrapper)queryWrapper);
        LinkedList<CourseOrderListVo> list = new LinkedList<CourseOrderListVo>();
        for (RechargeOrder order : iPage.getRecords()) {
            CourseClass courseClass;
            Student stu;
            CourseOrderListVo vo = new CourseOrderListVo();
            vo.setId(order.getId());
            vo.setCourseId(order.getCourseClassId());
            vo.setOrderNo(order.getSn());
            vo.setCreateTime(TimeUtils.timestampToDate((String)(order.getCreateTime() != null ? String.valueOf(order.getCreateTime()) : "")));
            vo.setAmount(order.getOrderAmount());
            if (order.getStudentId() != null && order.getStudentId() > 0 && (stu = (Student)this.studentMapper.selectById((Serializable)order.getStudentId())) != null) {
                vo.setStudentName(stu.getName());
            }
            if ((courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)order.getCourseClassId())) != null) {
                vo.setCourseName(courseClass.getName());
                vo.setCoachAndVenue(courseClass.getInstructorName() + "\u00b7" + courseClass.getVenue());
                String weekDaysStr = courseClass.getWeekDays();
                if (weekDaysStr != null && !weekDaysStr.isEmpty()) {
                    String[] dayMap = new String[]{"", "\u4e00", "\u4e8c", "\u4e09", "\u56db", "\u4e94", "\u516d", "\u65e5"};
                    String[] days = weekDaysStr.split(",");
                    ArrayList<String> labels = new ArrayList<String>();
                    for (String day : days) {
                        String token = day.trim();
                        if (token.isEmpty()) {
                            continue;
                        }
                        // \u5386\u53f2\u73ed\u7ea7 week_days \u53ef\u80fd\u6df7\u5165\u810f\u503c\uff08\u5982 1,x\uff09\uff1a\u5355\u6761\u810f\u6570\u636e\u4e0d\u80fd\u8ba9\u6574\u4e2a\u8ba2\u5355\u5217\u8868 500\uff0c
                        // \u4fdd\u7559\u539f\u59cb\u6587\u672c\u964d\u7ea7\u5c55\u793a\u3002
                        Integer parsed = ParamUtils.parseIntOrNullQuietly(token);
                        labels.add(parsed != null && parsed >= 1 && parsed <= 7 ? dayMap[parsed] : token);
                    }
                    StringBuilder sb = new StringBuilder("\u6bcf\u5468");
                    sb.append(String.join("\u3001", labels));
                    sb.append(" ").append(courseClass.getTimeSlot() != null ? courseClass.getTimeSlot() : "");
                    vo.setClassTime(sb.toString());
                } else {
                    vo.setClassTime(courseClass.getTimeSlot() != null ? courseClass.getTimeSlot() : "-");
                }
                String sd = courseClass.getStartDate() != null ? TimeUtils.timestampToDate((String)String.valueOf(courseClass.getStartDate())).substring(0, 10).replace("-", ".") : "";
                String ed = courseClass.getEndDate() != null ? TimeUtils.timestampToDate((String)String.valueOf(courseClass.getEndDate())).substring(0, 10).replace("-", ".") : "";
                vo.setValidPeriod(sd + "-" + ed);
            } else {
                vo.setCourseName("\u672a\u77e5\u8bfe\u7a0b");
                vo.setCoachAndVenue("-");
                vo.setClassTime("-");
                vo.setValidPeriod("-");
            }
            if (order.getRefundStatus() != null && order.getRefundStatus() == 1) {
                vo.setStatus("cancelled");
                vo.setStatusText("\u5df2\u53d6\u6d88");
                vo.setCancelText("\u8ba2\u5355\u5df2\u88ab\u9000\u6b3e/\u53d6\u6d88");
                vo.setPrimaryAction("\u91cd\u65b0\u8d2d\u4e70");
            } else if (order.getPayStatus() != null && order.getPayStatus() == 4) {
                vo.setStatus("cancelled");
                vo.setStatusText("\u5df2\u53d6\u6d88");
                vo.setCancelText("\u8ba2\u5355\u5df2\u53d6\u6d88");
                vo.setPrimaryAction("\u91cd\u65b0\u8d2d\u4e70");
            } else if (order.getPayStatus() != null && order.getPayStatus() == 1) {
                vo.setStatus("paid");
                vo.setStatusText("\u5df2\u652f\u4ed8");
                vo.setPrimaryAction("\u518d\u6b21\u8d2d\u4e70");
            } else {
                vo.setStatus("pending");
                vo.setStatusText("\u5f85\u652f\u4ed8");
                vo.setSecondaryAction("\u53d6\u6d88");
                vo.setPrimaryAction("\u7acb\u5373\u652f\u4ed8");
            }
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public CourseOrderStatsVo orderStats(Integer userId) {
        CourseOrderStatsVo vo = new CourseOrderStatsVo();
        QueryWrapper<RechargeOrder> totalQw = new QueryWrapper<RechargeOrder>();
        totalQw.eq("user_id", (Object)userId).isNotNull("course_class_id").ne("course_class_id", (Object)0);
        Long total = this.rechargeOrderMapper.selectCount((Wrapper)totalQw);
        vo.setTotal(Long.valueOf(total != null ? total : 0L));
        QueryWrapper<RechargeOrder> pendingQw = new QueryWrapper<RechargeOrder>();
        pendingQw.eq("user_id", (Object)userId).isNotNull("course_class_id").ne("course_class_id", (Object)0).eq("pay_status", (Object)0).eq("refund_status", (Object)0);
        Long pending = this.rechargeOrderMapper.selectCount((Wrapper)pendingQw);
        vo.setPending(Long.valueOf(pending != null ? pending : 0L));
        QueryWrapper<RechargeOrder> spentQw = new QueryWrapper<RechargeOrder>();
        spentQw.eq("user_id", (Object)userId).isNotNull("course_class_id").ne("course_class_id", (Object)0).eq("pay_status", (Object)1).eq("refund_status", (Object)0);
        // #154：order_amount 落库时已是券后实付（placeOrder 里 order.setOrderAmount(payAmount)），
        // 统计再减 coupon_amount 属二次扣减，用券订单的累计已付会偏小；直接按 order_amount 求和即实付口径。
        spentQw.select(new String[]{"IFNULL(SUM(order_amount), 0) AS order_amount"});
        RechargeOrder spentResult = (RechargeOrder)this.rechargeOrderMapper.selectOne((Wrapper)spentQw);
        if (spentResult != null && spentResult.getOrderAmount() != null) {
            vo.setTotalSpent(spentResult.getOrderAmount());
        } else {
            vo.setTotalSpent(BigDecimal.ZERO);
        }
        return vo;
    }

    // #154：旧实现无条件置 refund_status=1 且整行 updateById，与支付回调（courseCallback 的
    // pay_status 0→1 条件抢占）并发时可产生「已支付且已取消」并误还券；且取消与券恢复两笔写
    // 无事务，极小窗口可能订单已取消而券未恢复。现改为：
    //   1) 取消只允许作用于未支付单：条件更新（pay_status=0 且 refund_status=0）+ 影响行数判定，
    //      抢占失败重新读取给出准确业务错误，不再出现「已支付且已取消」；
    //   2) @Transactional：订单取消与券恢复同事务，失败整体回滚；
    //   3) 券恢复也走条件更新（仅当券仍被本单占用时退回），避免覆盖新订单对同券的占用。
    @Transactional
    public void cancelOrder(Integer orderId, Integer userId) {
        RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new OperateException("\u8ba2\u5355\u4e0d\u5b58\u5728");
        }
        if (order.getPayStatus() != 0) {
            throw new OperateException("\u8be5\u8ba2\u5355\u5df2\u4ed8\u6b3e\uff0c\u82e5\u9700\u9000\u6b3e\u8bf7\u8054\u7cfb\u5ba2\u670d");
        }
        if (order.getRefundStatus() == 1) {
            throw new OperateException("\u8ba2\u5355\u5df2\u88ab\u53d6\u6d88");
        }
        long now = System.currentTimeMillis() / 1000L;
        int affected = this.rechargeOrderMapper.update(null, new UpdateWrapper<RechargeOrder>()
                .set("refund_status", 1)
                .set("update_time", Long.valueOf(now))
                .eq("id", (Object)orderId)
                .eq("user_id", (Object)userId)
                .eq("pay_status", (Object)0)
                .eq("refund_status", (Object)0));
        if (affected <= 0) {
            // 并发支付回调抢先把订单置为已支付（或另一请求已取消）：重新读取，给出准确业务错误。
            RechargeOrder latest = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)orderId);
            if (latest != null && latest.getPayStatus() != null && latest.getPayStatus() == 1) {
                throw new OperateException("\u8be5\u8ba2\u5355\u5df2\u4ed8\u6b3e\uff0c\u82e5\u9700\u9000\u6b3e\u8bf7\u8054\u7cfb\u5ba2\u670d");
            }
            throw new OperateException("\u8ba2\u5355\u5df2\u88ab\u53d6\u6d88");
        }
        Integer couponId = order.getCouponId();
        if (couponId != null && couponId > 0) {
            int restored = this.couponMapper.update(null, new UpdateWrapper<Coupon>()
                    .set("status", 0)
                    .set("used_order_id", 0)
                    .set("used_time", null)
                    .set("update_time", Long.valueOf(now))
                    .eq("id", (Object)couponId)
                    .eq("status", 1)
                    .eq("used_order_id", (Object)orderId)
                    .isNull("delete_time"));
            // restored=0：券已删除/已恢复/被取消后的新订单重新占用——订单取消仍生效，券按现状保留。
        }
    }

    public boolean checkPurchased(Integer userId, Integer courseId) {
        QueryWrapper<RechargeOrder> qw = new QueryWrapper<RechargeOrder>();
        qw.eq("user_id", (Object)userId).eq("course_class_id", (Object)courseId).eq("pay_status", (Object)1).eq("refund_status", (Object)0);
        Long count = this.rechargeOrderMapper.selectCount((Wrapper)qw);
        return count != null && count > 0L;
    }
}

