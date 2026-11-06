package com.mdd.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.mapper.ReportQuery;
import com.mdd.admin.mapper.ReportQueryMapper;
import com.mdd.admin.service.IReportService;
import com.mdd.admin.export.ExportFileService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.report.ReportSearchValidate;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.activity.Activity;
import com.mdd.common.entity.activity.ActivitySignup;
import com.mdd.common.entity.activity.ClassApply;
import com.mdd.common.entity.activity.ClassApplyGroup;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponGoods;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.region.Region;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.activity.ActivityMapper;
import com.mdd.common.mapper.activity.ActivitySignupMapper;
import com.mdd.common.mapper.activity.ClassApplyGroupMapper;
import com.mdd.common.mapper.activity.ClassApplyMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponGoodsMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.region.RegionMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 后台数据报表（T30.5 / #40、#65、#66）。
 *
 * <p>五类报表共用同一套筛选/统计/导出实现；#66 起列表分页、计数与汇总全部下推到
 * SQL（{@link ReportQueryMapper}），页面不再加载全量数据；导出按批次从 SQL 取页写入
 * Excel，并对行数与文件大小设置上限，超限返回明确业务错误。</p>
 *
 * <p>#102 起课程报表改为真实收益口径（订单/课时/核销/营业额），不做二次分配计算；
 * 教练月营业额视图（本月/上月 + 下钻）为本类新增，只统计工作量与营业额。
 * 其余四类报表与 #65 文档/断言一致。</p>
 */
@Service
public class ReportServiceImpl implements IReportService {

    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final String DEFAULT_START = "1970-01-01";
    /** 教练月营业额下钻明细单次返回上限（超出截断并标记）。 */
    private static final int COACH_DETAIL_LIMIT = 500;

    /** 导出单次最多行数；可用 like.report.export-max-rows 覆盖。 */
    private static final int DEFAULT_EXPORT_MAX_ROWS = 50000;
    /** 导出文件最大 MB；可用 like.report.export-max-file-size-mb 覆盖。 */
    private static final BigDecimal DEFAULT_EXPORT_MAX_FILE_MB = new BigDecimal("20");
    /** 导出时每次从数据库取的行数，避免一次性加载全量。 */
    private static final int EXPORT_BATCH_SIZE = 2000;

    private static final String RANGE_CALIBER =
            "时间范围：startDate/endDate 均含首尾两天，缺省 1970-01-01 至当天（本机时区）。";

    private static final String UNASSIGNED_COACH_NAME = "未分配教练";

    private static final String COACH_MONTH_CALIBER =
            "自然月口径：month=yyyy-MM，缺省本月；含首月首日 00:00:00 至月末 23:59:59（本机时区）。"
            + "总营业额：la_recharge_order 中 pay_status=1、delete_time IS NULL、refund_status<>1 的实付金额合计，按 pay_time 归期。"
            + "教练归属：la_course_class.instructor_id；班级已删除或无教练归属的订单进入「未分配教练」，不丢单、可对账。"
            + "券包订单（course_class_id=0 且 coupon_goods_id>0）无课程班级、无法归属教练，排除在教练营业额之外并在汇总单独说明。"
            + "上课数：区间内不同（班级, 课节）数量；服务人次：la_course_consume_log 核销流水行数（含历史人工核销）。"
            + "本视图只展示营业额与工作量，不做任何二次分配计算。";

    private static final String COURSE_CALIBER =
            RANGE_CALIBER
            + "课程订单：la_recharge_order 中 course_class_id>0、pay_status=1、delete_time IS NULL、refund_status<>1，按 pay_time 归期。"
            + "已消课时：非删除班级在区间内 la_course_consume_log.create_time 的流水行数，不按预约状态过滤。"
            + "到场人次/场次取 la_course_booking.status=10（已上课）按 lesson_date 归期的行数与不同 lesson_id 数。"
            + "班级纳入范围：区间内存在已支付订单、或存在消课流水、或存在已到场预约。"
            + "#102 起本报表只展示真实营业额与工作量（订单/课时/核销），不做任何二次分配计算。";

    private static final String CHECKIN_CALIBER =
            RANGE_CALIBER
            + "核销口径：la_course_consume_log 一条消课流水一行，按 create_time 归期；班级不存在或已删除的流水不计入。"
            + "来源 source_type：0=正常核销、1=历史人工核销、2=后台人工补核销。"
            + "预约状态按 la_course_booking（user_id+student_id+course_class_id+lesson_no）匹配，status=10 为已上课；"
            + "未匹配到预约的是历史消课流水，单独展示为「无预约记录」，不影响核销数量。";

    private static final String COUPON_CALIBER =
            RANGE_CALIBER
            + "券流水口径：la_coupon_flow 只追加、不删除，按 create_time 归期；"
            + "类型 1=发放、2=核销扣减、3=补发、4=作废、5=退回；times 为本次变动次数（发放/补发/退回为正，扣减/作废为负），balance 为变动后余额。"
            + "券名称/编号取自 la_coupon_instance 快照，券商品取自 la_coupon_instance.goods_id。"
            + "张数口径（#103）：10 节课券包按张落库（一单 N 行、每行 1 次），"
            + "每张券写一条发放流水，因此「发放张数 = 券实例数 = 发放流水条数」，汇总里的发放/核销计数单位是「张」。";

    private static final String ACTIVITY_CALIBER =
            RANGE_CALIBER
            + "活动报名口径：la_activity_signup 一条报名明细一行，按 create_time 归期；"
            + "状态 0=待支付、1=已支付、3=已取消（其余状态计入待支付）。"
            + "已支付金额=报名状态为已支付且关联订单 pay_status=1 的 la_recharge_order.order_amount 合计；订单未支付或不存在按 0 计。"
            + "活动按参与学员人数计费（fee×人数），社区通过活动地点 la_area.community_id 关联。";

    private static final String CLASS_APPLY_CALIBER =
            RANGE_CALIBER
            + "开班申请口径：la_class_apply 一条申请明细一行，按 create_time 归期（delete_time IS NULL）；"
            + "同一「街道+课程类型」合并到 la_class_apply_group，joined/target 为聚合组进度，status：pending=招募中、following=客服跟进中、closed=已开班；"
            + "本报表不生成订单、不扣课时。";

    @Resource
    ReportQueryMapper reportQueryMapper;
    @Resource
    CommunityMapper communityMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponGoodsMapper couponGoodsMapper;
    @Resource
    ActivityMapper activityMapper;
    @Resource
    ActivitySignupMapper activitySignupMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    RegionMapper regionMapper;
    @Resource
    ClassApplyMapper classApplyMapper;
    @Resource
    ClassApplyGroupMapper classApplyGroupMapper;
    @Resource
    ExportFileService exportFileService;

    // ------------------------------------------------------------------
    // 课程报表
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> courseList(PageValidate pageValidate,
                                                      ReportSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        ReportQuery query = courseQuery(searchValidate);
        long total = reportQueryMapper.countCourse(query);
        List<Map<String, Object>> rows = pageRows(total, pageNo, pageSize, paged -> courseRows(paged), query);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("summary", courseSummary(query));
        extend.put("caliber", COURSE_CALIBER);
        extend.put("periodStart", periodStart(searchValidate));
        extend.put("periodEnd", periodEnd(searchValidate));
        extend.put("options", options());
        extend.put("columns", columnsToMap(courseColumns()));
        return PageResult.iPageHandle(total, (long) pageNo, (long) pageSize, rows, extend);
    }

    @Override
    public Map<String, Object> courseExport(ReportSearchValidate searchValidate) {
        final ReportQuery query = courseQuery(searchValidate);
        long total = reportQueryMapper.countCourse(query);
        return export("course", "课程报表", courseColumns(), query, total,
                paged -> courseRows(paged),
                courseSummary(query), COURSE_CALIBER, searchValidate);
    }

    private ReportQuery courseQuery(ReportSearchValidate search) {
        ReportQuery query = baseQuery(search);
        query.setCommunityId(positive(resolveCommunityScope(positive(search.getCommunityId()))));
        query.setCourseId(positive(search.getCourseId()));
        query.setInstructorId(positive(search.getInstructorId()));
        return query;
    }

    private List<Map<String, Object>> courseRows(ReportQuery query) {
        List<Map<String, Object>> rawRows = reportQueryMapper.pageCourse(query);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> raw : rawRows) {
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("courseClassId", asInt(raw.get("courseClassId")));
            row.put("courseName", safe((String) raw.get("courseName")));
            row.put("communityId", asInt(raw.get("communityId")));
            row.put("communityName", safe((String) raw.get("communityName")));
            row.put("instructorId", asInt(raw.get("instructorId")));
            row.put("instructorName", safe((String) raw.get("instructorName")));
            row.put("orderCount", asInt(raw.get("orderCount")));
            row.put("paidAmount", round2(asDecimal(raw.get("paidAmount"))));
            row.put("purchasedLessons", asInt(raw.get("purchasedLessons")));
            row.put("consumedLessons", asInt(raw.get("consumedLessons")));
            row.put("attendedStudents", asInt(raw.get("attendedStudents")));
            row.put("attendedLessons", asInt(raw.get("attendedLessons")));
            rows.add(row);
        }
        return rows;
    }

    private Map<String, Object> courseSummary(ReportQuery query) {
        Map<String, Object> raw = reportQueryMapper.summaryCourse(query);
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("classCount", asInt(raw.get("classCount")));
        summary.put("orderCount", asInt(raw.get("orderCount")));
        summary.put("paidAmount", round2(asDecimal(raw.get("paidAmount"))));
        summary.put("purchasedLessons", asInt(raw.get("purchasedLessons")));
        summary.put("consumedLessons", asInt(raw.get("consumedLessons")));
        summary.put("attendedStudents", asInt(raw.get("attendedStudents")));
        return summary;
    }

    // ------------------------------------------------------------------
    // 教练月营业额（#102）
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> coachMonthList(PageValidate pageValidate,
                                                          ReportSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        MonthWindow window = monthWindow(searchValidate);
        ReportQuery query = window.query();
        long total = reportQueryMapper.countCoachMonth(query);
        List<Map<String, Object>> rows = pageRows(total, pageNo, pageSize, paged -> coachMonthRows(paged), query);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("month", window.month);
        extend.put("periodStart", window.startDate);
        extend.put("periodEnd", window.endDate);
        extend.put("summary", coachMonthSummary(query));
        extend.put("couponNote", coachMonthCouponNote(query));
        extend.put("caliber", COACH_MONTH_CALIBER);
        return PageResult.iPageHandle(total, (long) pageNo, (long) pageSize, rows, extend);
    }

    @Override
    public Map<String, Object> coachMonthExport(ReportSearchValidate searchValidate) {
        MonthWindow window = monthWindow(searchValidate);
        ReportQuery query = window.query();
        // 复用通用导出：文件名/期间取自 startDate-endDate，这里回填当月首尾。
        searchValidate.setStartDate(window.startDate);
        searchValidate.setEndDate(window.endDate);
        long total = reportQueryMapper.countCoachMonth(query);
        return export("coach_month", "教练月营业额", coachMonthColumns(), query, total,
                paged -> coachMonthRows(paged),
                coachMonthSummary(query), COACH_MONTH_CALIBER, searchValidate);
    }

    @Override
    public Map<String, Object> coachMonthDetail(ReportSearchValidate searchValidate) {
        MonthWindow window = monthWindow(searchValidate);
        Integer coachId = searchValidate.getCoachId();
        if (coachId == null || coachId < 0) {
            throw new OperateException("请选择要下钻的教练（coachId）");
        }
        ReportQuery headQuery = window.query();
        headQuery.setInstructorId(coachId);
        headQuery.setOffset(0);
        headQuery.setLimit(1);
        List<Map<String, Object>> head = reportQueryMapper.pageCoachMonth(headQuery);
        String coachName = head.isEmpty() ? "" : safe((String) head.get(0).get("coachName"));
        if (coachId == 0) {
            coachName = UNASSIGNED_COACH_NAME;
        } else if (coachName.isEmpty()) {
            coachName = coachName(coachId);
        }

        ReportQuery lessonQuery = window.query();
        lessonQuery.setInstructorId(coachId);
        lessonQuery.setOffset(0);
        // 多取一行判断是否截断，返回前裁掉。
        lessonQuery.setLimit(COACH_DETAIL_LIMIT + 1);
        List<Map<String, Object>> lessonRows = reportQueryMapper.pageCoachMonthLessons(lessonQuery);
        boolean lessonsTruncated = lessonRows.size() > COACH_DETAIL_LIMIT;
        if (lessonsTruncated) {
            lessonRows = new ArrayList<Map<String, Object>>(lessonRows.subList(0, COACH_DETAIL_LIMIT));
        }

        ReportQuery orderQuery = window.query();
        orderQuery.setInstructorId(coachId);
        orderQuery.setOffset(0);
        orderQuery.setLimit(COACH_DETAIL_LIMIT + 1);
        List<Map<String, Object>> orderRawRows = reportQueryMapper.pageCoachMonthOrders(orderQuery);
        boolean ordersTruncated = orderRawRows.size() > COACH_DETAIL_LIMIT;
        if (ordersTruncated) {
            orderRawRows = new ArrayList<Map<String, Object>>(orderRawRows.subList(0, COACH_DETAIL_LIMIT));
        }

        List<Map<String, Object>> lessons = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> raw : lessonRows) {
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("courseClassId", asInt(raw.get("courseClassId")));
            row.put("courseName", safe((String) raw.get("courseName")));
            row.put("lessonNo", asInt(raw.get("lessonNo")));
            row.put("servicePersons", asInt(raw.get("servicePersons")));
            row.put("lastConsumeTime", formatTime(asLong(raw.get("lastConsumeTime"))));
            lessons.add(row);
        }
        List<Map<String, Object>> orders = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> raw : orderRawRows) {
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("orderId", asInt(raw.get("orderId")));
            row.put("sn", safe((String) raw.get("sn")));
            row.put("courseClassId", asInt(raw.get("courseClassId")));
            row.put("courseName", safe((String) raw.get("courseName")));
            row.put("studentId", asInt(raw.get("studentId")));
            row.put("studentName", safe((String) raw.get("studentName")));
            row.put("lessonCount", asInt(raw.get("lessonCount")));
            row.put("orderAmount", round2(asDecimal(raw.get("orderAmount"))));
            row.put("payTime", formatTime(asLong(raw.get("payTime"))));
            orders.add(row);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("coachId", coachId);
        data.put("coachName", coachName);
        data.put("month", window.month);
        data.put("periodStart", window.startDate);
        data.put("periodEnd", window.endDate);
        data.put("summary", coachMonthSummary(headQuery));
        data.put("lessons", lessons);
        data.put("orders", orders);
        data.put("lessonsTruncated", lessonsTruncated);
        data.put("ordersTruncated", ordersTruncated);
        data.put("caliber", COACH_MONTH_CALIBER);
        return data;
    }

    private List<Map<String, Object>> coachMonthRows(ReportQuery query) {
        List<Map<String, Object>> rawRows = reportQueryMapper.pageCoachMonth(query);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> raw : rawRows) {
            Integer coachId = asInt(raw.get("coachId"));
            String coachName = safe((String) raw.get("coachName"));
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("coachId", coachId);
            // 无教练归属的桶固定叫「未分配教练」：班级上的 instructor_name 只是历史冗余字段，
            // 不能拿来充当教练名（未分配就是未分配）。
            row.put("coachName", coachId == 0
                    ? UNASSIGNED_COACH_NAME
                    : (coachName.isEmpty() ? "" : coachName));
            row.put("orderCount", asInt(raw.get("orderCount")));
            row.put("paidAmount", round2(asDecimal(raw.get("paidAmount"))));
            row.put("lessonCount", asInt(raw.get("lessonCount")));
            row.put("servicePersons", asInt(raw.get("servicePersons")));
            rows.add(row);
        }
        return rows;
    }

    private Map<String, Object> coachMonthSummary(ReportQuery query) {
        Map<String, Object> raw = reportQueryMapper.summaryCoachMonth(query);
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("coachCount", asInt(raw.get("coachCount")));
        summary.put("orderCount", asInt(raw.get("orderCount")));
        summary.put("paidAmount", round2(asDecimal(raw.get("paidAmount"))));
        summary.put("lessonCount", asInt(raw.get("lessonCount")));
        summary.put("servicePersons", asInt(raw.get("servicePersons")));
        Map<String, Object> coupon = reportQueryMapper.summaryCoachMonthCoupon(query);
        summary.put("couponOrderCount", asInt(coupon.get("couponOrderCount")));
        summary.put("couponOrderAmount", round2(asDecimal(coupon.get("couponOrderAmount"))));
        return summary;
    }

    /** 券包收入无法归属教练：汇总口径显式说明（页面/导出都带上）。 */
    private String coachMonthCouponNote(ReportQuery query) {
        Map<String, Object> coupon = reportQueryMapper.summaryCoachMonthCoupon(query);
        int count = asInt(coupon.get("couponOrderCount"));
        BigDecimal amount = round2(asDecimal(coupon.get("couponOrderAmount")));
        return "券包收入不计入教练营业额：本月券包订单 " + count + " 笔、金额 ￥" + amount.toPlainString()
                + "（券包订单无课程班级，无法归属教练）。";
    }

    private String coachName(Integer coachId) {
        if (coachId == null || coachId <= 0) {
            return UNASSIGNED_COACH_NAME;
        }
        Instructor instructor = instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("id", coachId).isNull("delete_time").last("limit 1"));
        return instructor == null ? "" : safe(instructor.getName());
    }

    private List<Column> coachMonthColumns() {
        return Arrays.asList(
                new Column("coachName", "教练", 20),
                new Column("orderCount", "订单数", 10),
                new Column("paidAmount", "总营业额(元)", 16),
                new Column("lessonCount", "上课数(场次)", 14),
                new Column("servicePersons", "服务人次", 12)
        );
    }

    /**
     * 解析 month=yyyy-MM（缺省本月）并换算成区间 epoch / 日期。
     *
     * <p>#106 统计页兼容参数：未传 month 且同时传了 startDate/endDate 时按任意区间统计，
     * month 语义保持优先（传了 month 就按自然月），不改变「本月/上月」既有行为。</p>
     */
    private MonthWindow monthWindow(ReportSearchValidate search) {
        String text = trim(search.getMonth());
        MonthWindow window = new MonthWindow();
        LocalDate start;
        LocalDate end;
        if (text.isEmpty() && !trim(search.getStartDate()).isEmpty() && !trim(search.getEndDate()).isEmpty()) {
            start = parseDate(trim(search.getStartDate()), "开始日期");
            end = parseDate(trim(search.getEndDate()), "结束日期");
            if (start.isAfter(end)) {
                throw new OperateException("开始日期不能晚于结束日期");
            }
            window.month = "";
        } else {
            YearMonth month;
            if (text.isEmpty()) {
                month = YearMonth.now();
            } else {
                try {
                    month = YearMonth.parse(text);
                } catch (DateTimeParseException error) {
                    throw new OperateException("月份格式应为 yyyy-MM");
                }
            }
            start = month.atDay(1);
            end = month.atEndOfMonth();
            window.month = month.toString();
        }
        window.startDate = start.toString();
        window.endDate = end.toString();
        window.startEpoch = start.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        window.endEpoch = end.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toEpochSecond();
        return window;
    }

    // ------------------------------------------------------------------
    // 核销报表
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> checkinList(PageValidate pageValidate,
                                                       ReportSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        ReportQuery query = checkinQuery(searchValidate);
        long total = reportQueryMapper.countCheckin(query);
        List<Map<String, Object>> rows = pageRows(total, pageNo, pageSize, paged -> checkinRows(reportQueryMapper.pageCheckinIds(paged)), query);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("summary", checkinSummary(query));
        extend.put("caliber", CHECKIN_CALIBER);
        extend.put("periodStart", periodStart(searchValidate));
        extend.put("periodEnd", periodEnd(searchValidate));
        extend.put("options", options());
        extend.put("columns", columnsToMap(checkinColumns()));
        return PageResult.iPageHandle(total, (long) pageNo, (long) pageSize, rows, extend);
    }

    @Override
    public Map<String, Object> checkinExport(ReportSearchValidate searchValidate) {
        final ReportQuery query = checkinQuery(searchValidate);
        long total = reportQueryMapper.countCheckin(query);
        return export("checkin", "核销报表", checkinColumns(), query, total,
                paged -> checkinRows(reportQueryMapper.pageCheckinIds(paged)),
                checkinSummary(query), CHECKIN_CALIBER, searchValidate);
    }

    private ReportQuery checkinQuery(ReportSearchValidate search) {
        ReportQuery query = baseQuery(search);
        query.setCommunityId(positive(search.getCommunityId()));
        query.setCourseId(positive(search.getCourseId()));
        query.setInstructorId(positive(search.getInstructorId()));
        query.setSourceType(search.getSourceType());
        String status = trim(search.getStatus());
        if (status.isEmpty() || "ALL".equalsIgnoreCase(status)) {
            query.setBookingStatus(null);
        } else if ("attended".equals(status) || "booked".equals(status) || "none".equals(status)) {
            query.setBookingStatus(status);
        } else {
            query.setMatchNone(1);
        }
        return query;
    }

    private List<Map<String, Object>> checkinRows(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<CourseConsumeLog> logs = new ArrayList<CourseConsumeLog>(courseConsumeLogMapper.selectBatchIds(ids));
        Collections.sort(logs, new Comparator<CourseConsumeLog>() {
            @Override
            public int compare(CourseConsumeLog left, CourseConsumeLog right) {
                int byTime = Long.compare(nvlLong(right.getCreateTime()), nvlLong(left.getCreateTime()));
                return byTime != 0 ? byTime : Integer.compare(nvl(right.getId()), nvl(left.getId()));
            }
        });
        return buildCheckinRows(logs);
    }

    private List<Map<String, Object>> buildCheckinRows(List<CourseConsumeLog> logs) {
        if (logs.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Integer> classIds = new LinkedHashSet<Integer>();
        Set<Integer> userIds = new LinkedHashSet<Integer>();
        Set<Integer> studentIds = new LinkedHashSet<Integer>();
        for (CourseConsumeLog log : logs) {
            if (log.getCourseClassId() != null) {
                classIds.add(log.getCourseClassId());
            }
            if (log.getUserId() != null) {
                userIds.add(log.getUserId());
            }
            if (log.getStudentId() != null) {
                studentIds.add(log.getStudentId());
            }
        }
        Map<Integer, CourseClass> classMap = mapByIds(classIds, CourseClass.class);
        Map<Integer, User> userMap = mapByIds(userIds, User.class);
        Map<Integer, Student> studentMap = mapByIds(studentIds, Student.class);
        Map<Integer, Community> communityMap = communityMap();
        Map<Integer, Instructor> instructorMap = instructorMap();

        Map<String, CourseBooking> bookingMap = new HashMap<String, CourseBooking>();
        Set<Integer> lessonIds = new LinkedHashSet<Integer>();
        if (!classIds.isEmpty()) {
            List<CourseBooking> bookings = courseBookingMapper.selectList(new QueryWrapper<CourseBooking>()
                    .in("course_class_id", classIds));
            for (CourseBooking booking : bookings) {
                String key = bookingKey(booking.getUserId(), booking.getStudentId(),
                        booking.getCourseClassId(), booking.getLessonNo());
                CourseBooking previous = bookingMap.get(key);
                if (previous == null || (nvl(booking.getStatus()) == 10 && nvl(previous.getStatus()) != 10)) {
                    bookingMap.put(key, booking);
                }
                if (booking.getLessonId() != null && booking.getLessonId() > 0) {
                    lessonIds.add(booking.getLessonId());
                }
            }
        }
        Map<Integer, CourseLesson> lessonMap = mapByIds(lessonIds, CourseLesson.class);

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (CourseConsumeLog log : logs) {
            CourseClass courseClass = classMap.get(log.getCourseClassId());
            if (courseClass == null || nvl(courseClass.getIsDelete()) == 1) {
                continue;
            }
            CourseBooking booking = bookingMap.get(bookingKey(log.getUserId(), log.getStudentId(),
                    log.getCourseClassId(), log.getLessonNo()));
            int bookingStatus = booking == null ? -1 : nvl(booking.getStatus());
            String statusKey = bookingStatus == 10 ? "attended" : (booking == null ? "none" : "booked");
            CourseLesson lesson = booking == null ? null : lessonMap.get(booking.getLessonId());
            User user = userMap.get(log.getUserId());
            Student student = studentMap.get(log.getStudentId());
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("id", log.getId());
            row.put("consumeTime", formatTime(log.getCreateTime()));
            row.put("courseClassId", log.getCourseClassId());
            row.put("courseName", safe(courseClass.getName()));
            row.put("communityId", nvl(courseClass.getCommunityId()));
            row.put("communityName", nameOf(communityMap, courseClass.getCommunityId()));
            row.put("instructorId", nvl(courseClass.getInstructorId()));
            row.put("instructorName", instructorName(courseClass, instructorMap));
            row.put("studentId", nvl(log.getStudentId()));
            row.put("studentName", student == null ? "" : safe(student.getName()));
            row.put("userId", nvl(log.getUserId()));
            row.put("parentName", userName(user));
            row.put("parentMobile", user == null ? "" : safe(user.getMobile()));
            row.put("lessonNo", nvl(log.getLessonNo()));
            row.put("lessonDate", booking == null ? "" : safe(booking.getLessonDate()));
            row.put("timeSlot", lesson == null ? "" : safe(lesson.getTimeSlot()));
            row.put("sourceType", nvl(log.getSourceType()));
            row.put("sourceTypeText", sourceTypeText(log.getSourceType()));
            row.put("bookingStatus", bookingStatus);
            row.put("bookingStatusText", bookingStatus == 10 ? "已上课" : (booking == null ? "无预约记录" : "已预约未上课"));
            row.put("statusKey", statusKey);
            rows.add(row);
        }
        return rows;
    }

    private Map<String, Object> checkinSummary(ReportQuery query) {
        Map<String, Object> raw = reportQueryMapper.summaryCheckin(query);
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("total", asInt(raw.get("total")));
        summary.put("normalCount", asInt(raw.get("normalCount")));
        summary.put("manualCount", asInt(raw.get("manualCount")));
        summary.put("attendedCount", asInt(raw.get("attendedCount")));
        summary.put("noBookingCount", asInt(raw.get("noBookingCount")));
        summary.put("studentCount", asInt(raw.get("studentCount")));
        return summary;
    }

    // ------------------------------------------------------------------
    // 券流水报表
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> couponList(PageValidate pageValidate,
                                                      ReportSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        ReportQuery query = couponQuery(searchValidate);
        long total = reportQueryMapper.countCoupon(query);
        List<Map<String, Object>> rows = pageRows(total, pageNo, pageSize, paged -> couponRows(reportQueryMapper.pageCouponIds(paged)), query);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("summary", couponSummary(query));
        extend.put("caliber", COUPON_CALIBER);
        extend.put("periodStart", periodStart(searchValidate));
        extend.put("periodEnd", periodEnd(searchValidate));
        extend.put("options", options());
        extend.put("columns", columnsToMap(couponColumns()));
        return PageResult.iPageHandle(total, (long) pageNo, (long) pageSize, rows, extend);
    }

    @Override
    public Map<String, Object> couponExport(ReportSearchValidate searchValidate) {
        final ReportQuery query = couponQuery(searchValidate);
        long total = reportQueryMapper.countCoupon(query);
        return export("coupon", "券流水报表", couponColumns(), query, total,
                paged -> couponRows(reportQueryMapper.pageCouponIds(paged)),
                couponSummary(query), COUPON_CALIBER, searchValidate);
    }

    /**
     * 券流水筛选口径（#185 复审）。
     *
     * <p>业务页「券列表」按学员/手机号筛选后导出流水，{@code userId} 精确匹配持券用户，
     * {@code mobile} 模糊匹配 {@code la_user.mobile}，与 {@code CouponServiceImpl} 券列表
     * 的 like 口径一致；不传时保持原有全量行为。</p>
     *
     * <p>「已过期」是券实例状态而非流水事件：{@code la_coupon_flow.type} 只有
     * 1=发放、2=核销扣减、3=补发、4=作废、5=退回，没有过期类型，因此报表不提供按
     * 「已过期」导出；业务页导出入口（coupon/index.vue）已阻止该状态并给出提示。</p>
     */
    private ReportQuery couponQuery(ReportSearchValidate search) {
        ReportQuery query = baseQuery(search);
        query.setFlowType(search.getFlowType());
        query.setCouponGoodsId(positive(search.getCouponGoodsId()));
        query.setUserId(positive(search.getUserId()));
        query.setMobile(likePattern(search.getMobile()));
        return query;
    }

    private List<Map<String, Object>> couponRows(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<CouponFlow> flows = new ArrayList<CouponFlow>(couponFlowMapper.selectBatchIds(ids));
        Collections.sort(flows, new Comparator<CouponFlow>() {
            @Override
            public int compare(CouponFlow left, CouponFlow right) {
                int byTime = Long.compare(nvlLong(right.getCreateTime()), nvlLong(left.getCreateTime()));
                return byTime != 0 ? byTime : Integer.compare(nvl(right.getId()), nvl(left.getId()));
            }
        });
        return buildCouponRows(flows);
    }

    private List<Map<String, Object>> buildCouponRows(List<CouponFlow> flows) {
        if (flows.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Integer> couponIds = new LinkedHashSet<Integer>();
        Set<Integer> userIds = new LinkedHashSet<Integer>();
        Set<Integer> studentIds = new LinkedHashSet<Integer>();
        for (CouponFlow flow : flows) {
            if (flow.getCouponId() != null) {
                couponIds.add(flow.getCouponId());
            }
            if (flow.getUserId() != null) {
                userIds.add(flow.getUserId());
            }
            if (flow.getStudentId() != null) {
                studentIds.add(flow.getStudentId());
            }
        }
        Map<Integer, CouponInstance> instanceMap = mapByIds(couponIds, CouponInstance.class);
        Set<Integer> goodsIds = new LinkedHashSet<Integer>();
        for (CouponInstance instance : instanceMap.values()) {
            if (instance.getGoodsId() != null && instance.getGoodsId() > 0) {
                goodsIds.add(instance.getGoodsId());
            }
        }
        Map<Integer, CouponGoods> goodsMap = mapByIds(goodsIds, CouponGoods.class);
        Map<Integer, User> userMap = mapByIds(userIds, User.class);
        Map<Integer, Student> studentMap = mapByIds(studentIds, Student.class);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (CouponFlow flow : flows) {
            CouponInstance instance = instanceMap.get(flow.getCouponId());
            CouponGoods goods = instance == null ? null : goodsMap.get(instance.getGoodsId());
            User user = userMap.get(flow.getUserId());
            Student student = studentMap.get(flow.getStudentId());
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("id", flow.getId());
            row.put("createTime", formatTime(flow.getCreateTime()));
            row.put("type", nvl(flow.getType()));
            row.put("typeText", flowTypeText(flow.getType()));
            row.put("couponId", nvl(flow.getCouponId()));
            row.put("couponSn", instance == null ? "" : safe(instance.getSn()));
            row.put("couponTitle", instance == null ? "" : safe(instance.getTitle()));
            row.put("goodsId", instance == null ? 0 : nvl(instance.getGoodsId()));
            row.put("goodsTitle", goods == null ? (instance == null ? "" : safe(instance.getTitle())) : safe(goods.getTitle()));
            row.put("userId", nvl(flow.getUserId()));
            row.put("parentName", userName(user));
            row.put("studentId", nvl(flow.getStudentId()));
            row.put("studentName", student == null ? "" : safe(student.getName()));
            row.put("times", nvl(flow.getTimes()));
            row.put("balance", nvl(flow.getBalance()));
            row.put("orderId", nvl(flow.getOrderId()));
            row.put("orderSn", safe(flow.getOrderSn()));
            row.put("operatorType", safe(flow.getOperatorType()));
            row.put("operatorTypeText", operatorTypeText(flow.getOperatorType()));
            row.put("operatorId", nvl(flow.getOperatorId()));
            row.put("remark", safe(flow.getRemark()));
            rows.add(row);
        }
        return rows;
    }

    private Map<String, Object> couponSummary(ReportQuery query) {
        Map<String, Object> raw = reportQueryMapper.summaryCoupon(query);
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("total", asInt(raw.get("total")));
        summary.put("grantCount", asInt(raw.get("grantCount")));
        summary.put("consumeCount", asInt(raw.get("consumeCount")));
        summary.put("reissueCount", asInt(raw.get("reissueCount")));
        summary.put("cancelCount", asInt(raw.get("cancelCount")));
        summary.put("refundCount", asInt(raw.get("refundCount")));
        summary.put("timesIn", asInt(raw.get("timesIn")));
        summary.put("timesOut", asInt(raw.get("timesOut")));
        return summary;
    }

    // ------------------------------------------------------------------
    // 活动报名报表
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> activityList(PageValidate pageValidate,
                                                        ReportSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        ReportQuery query = activityQuery(searchValidate);
        long total = reportQueryMapper.countActivity(query);
        List<Map<String, Object>> rows = pageRows(total, pageNo, pageSize, paged -> activityRows(reportQueryMapper.pageActivityIds(paged)), query);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("summary", activitySummary(query));
        extend.put("caliber", ACTIVITY_CALIBER);
        extend.put("periodStart", periodStart(searchValidate));
        extend.put("periodEnd", periodEnd(searchValidate));
        extend.put("options", options());
        extend.put("columns", columnsToMap(activityColumns()));
        return PageResult.iPageHandle(total, (long) pageNo, (long) pageSize, rows, extend);
    }

    @Override
    public Map<String, Object> activityExport(ReportSearchValidate searchValidate) {
        final ReportQuery query = activityQuery(searchValidate);
        long total = reportQueryMapper.countActivity(query);
        return export("activity", "活动报名报表", activityColumns(), query, total,
                paged -> activityRows(reportQueryMapper.pageActivityIds(paged)),
                activitySummary(query), ACTIVITY_CALIBER, searchValidate);
    }

    private ReportQuery activityQuery(ReportSearchValidate search) {
        ReportQuery query = baseQuery(search);
        query.setCommunityId(positive(search.getCommunityId()));
        query.setActivityId(positive(search.getActivityId()));
        String status = trim(search.getStatus());
        if (status.isEmpty() || "ALL".equalsIgnoreCase(status)) {
            query.setSignupStatus(null);
        } else {
            try {
                query.setSignupStatus(Integer.parseInt(status));
            } catch (NumberFormatException error) {
                query.setMatchNone(1);
            }
        }
        return query;
    }

    private List<Map<String, Object>> activityRows(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<ActivitySignup> signups = new ArrayList<ActivitySignup>(activitySignupMapper.selectBatchIds(ids));
        Collections.sort(signups, new Comparator<ActivitySignup>() {
            @Override
            public int compare(ActivitySignup left, ActivitySignup right) {
                int byTime = Long.compare(nvlLong(right.getCreateTime()), nvlLong(left.getCreateTime()));
                return byTime != 0 ? byTime : Integer.compare(nvl(right.getId()), nvl(left.getId()));
            }
        });
        return buildActivityRows(signups);
    }

    private List<Map<String, Object>> buildActivityRows(List<ActivitySignup> signups) {
        if (signups.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Integer> activityIds = new LinkedHashSet<Integer>();
        Set<Integer> orderIds = new LinkedHashSet<Integer>();
        Set<Integer> userIds = new LinkedHashSet<Integer>();
        Set<Integer> studentIds = new LinkedHashSet<Integer>();
        for (ActivitySignup signup : signups) {
            if (signup.getActivityId() != null) {
                activityIds.add(signup.getActivityId());
            }
            if (signup.getOrderId() != null && signup.getOrderId() > 0) {
                orderIds.add(signup.getOrderId());
            }
            if (signup.getUserId() != null) {
                userIds.add(signup.getUserId());
            }
            if (signup.getStudentId() != null) {
                studentIds.add(signup.getStudentId());
            }
        }
        Map<Integer, Activity> activityMap = mapByIds(activityIds, Activity.class);
        Map<Integer, RechargeOrder> orderMap = mapByIds(orderIds, RechargeOrder.class);
        Map<Integer, User> userMap = mapByIds(userIds, User.class);
        Map<Integer, Student> studentMap = mapByIds(studentIds, Student.class);
        Set<Integer> areaIds = new LinkedHashSet<Integer>();
        for (Activity activity : activityMap.values()) {
            if (activity.getAreaId() != null && activity.getAreaId() > 0) {
                areaIds.add(activity.getAreaId());
            }
        }
        Map<Integer, Area> areaMap = mapByIds(areaIds, Area.class);
        Map<Integer, Community> communityMap = communityMap();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (ActivitySignup signup : signups) {
            Activity activity = activityMap.get(signup.getActivityId());
            Area area = activity == null ? null : areaMap.get(activity.getAreaId());
            Integer communityId = area == null ? 0 : nvl(area.getCommunityId());
            RechargeOrder order = orderMap.get(signup.getOrderId());
            User user = userMap.get(signup.getUserId());
            Student student = studentMap.get(signup.getStudentId());
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("id", signup.getId());
            row.put("signupTime", formatTime(signup.getCreateTime()));
            row.put("activityId", nvl(signup.getActivityId()));
            row.put("activityTitle", activity == null ? "" : safe(activity.getTitle()));
            row.put("activityDate", activity == null ? "" : safe(activity.getActivityDate()));
            row.put("timeSlot", activity == null ? "" : safe(activity.getTimeSlot()));
            row.put("place", activity == null ? "" : safe(activity.getPlace()));
            row.put("areaId", activity == null ? 0 : nvl(activity.getAreaId()));
            row.put("areaName", area == null ? "" : safe(area.getName()));
            row.put("communityId", communityId);
            row.put("communityName", nameOf(communityMap, communityId));
            row.put("userId", nvl(signup.getUserId()));
            row.put("parentName", userName(user));
            row.put("parentMobile", user == null ? "" : safe(user.getMobile()));
            row.put("studentId", nvl(signup.getStudentId()));
            row.put("studentName", student == null ? "" : safe(student.getName()));
            row.put("status", nvl(signup.getStatus()));
            row.put("statusText", signupStatusText(signup.getStatus()));
            row.put("orderId", nvl(signup.getOrderId()));
            row.put("orderSn", order == null ? "" : safe(order.getSn()));
            row.put("orderAmount", order == null ? ZERO : round2(nvl(order.getOrderAmount())));
            row.put("payStatus", order == null ? null : order.getPayStatus());
            row.put("payStatusText", order == null ? "" : payStatusText(order.getPayStatus()));
            row.put("payTime", order == null ? "" : formatTime(order.getPayTime()));
            rows.add(row);
        }
        return rows;
    }

    private Map<String, Object> activitySummary(ReportQuery query) {
        Map<String, Object> raw = reportQueryMapper.summaryActivity(query);
        int total = asInt(raw.get("total"));
        int paid = asInt(raw.get("paidCount"));
        int cancelled = asInt(raw.get("cancelledCount"));
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("total", total);
        summary.put("paidCount", paid);
        summary.put("pendingCount", total - paid - cancelled);
        summary.put("cancelledCount", cancelled);
        summary.put("paidAmount", round2(asDecimal(raw.get("paidAmount"))));
        summary.put("studentCount", asInt(raw.get("studentCount")));
        return summary;
    }

    // ------------------------------------------------------------------
    // 开班申请报表
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> classApplyList(PageValidate pageValidate,
                                                          ReportSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        ReportQuery query = classApplyQuery(searchValidate);
        long total = reportQueryMapper.countClassApply(query);
        List<Map<String, Object>> rows = pageRows(total, pageNo, pageSize, paged -> classApplyRows(reportQueryMapper.pageClassApplyIds(paged)), query);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("summary", classApplySummary(query));
        extend.put("caliber", CLASS_APPLY_CALIBER);
        extend.put("periodStart", periodStart(searchValidate));
        extend.put("periodEnd", periodEnd(searchValidate));
        extend.put("options", options());
        extend.put("columns", columnsToMap(classApplyColumns()));
        return PageResult.iPageHandle(total, (long) pageNo, (long) pageSize, rows, extend);
    }

    @Override
    public Map<String, Object> classApplyExport(ReportSearchValidate searchValidate) {
        final ReportQuery query = classApplyQuery(searchValidate);
        long total = reportQueryMapper.countClassApply(query);
        return export("class_apply", "开班申请报表", classApplyColumns(), query, total,
                paged -> classApplyRows(reportQueryMapper.pageClassApplyIds(paged)),
                classApplySummary(query), CLASS_APPLY_CALIBER, searchValidate);
    }

    private ReportQuery classApplyQuery(ReportSearchValidate search) {
        ReportQuery query = baseQuery(search);
        query.setStreetId(positive(search.getStreetId()));
        String courseType = trim(search.getCourseType());
        query.setCourseType(courseType.isEmpty() ? null : courseType);
        String status = trim(search.getStatus());
        query.setGroupStatus(status.isEmpty() || "ALL".equalsIgnoreCase(status) ? null : status);
        return query;
    }

    private List<Map<String, Object>> classApplyRows(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<ClassApply> applies = new ArrayList<ClassApply>(classApplyMapper.selectBatchIds(ids));
        Collections.sort(applies, new Comparator<ClassApply>() {
            @Override
            public int compare(ClassApply left, ClassApply right) {
                int byTime = Long.compare(nvlLong(right.getCreateTime()), nvlLong(left.getCreateTime()));
                return byTime != 0 ? byTime : Integer.compare(nvl(right.getId()), nvl(left.getId()));
            }
        });
        return buildClassApplyRows(applies);
    }

    private List<Map<String, Object>> buildClassApplyRows(List<ClassApply> applies) {
        if (applies.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Integer> groupIds = new LinkedHashSet<Integer>();
        Set<Integer> userIds = new LinkedHashSet<Integer>();
        Set<Integer> studentIds = new LinkedHashSet<Integer>();
        for (ClassApply apply : applies) {
            if (apply.getGroupId() != null) {
                groupIds.add(apply.getGroupId());
            }
            if (apply.getUserId() != null) {
                userIds.add(apply.getUserId());
            }
            if (apply.getStudentId() != null) {
                studentIds.add(apply.getStudentId());
            }
        }
        Map<Integer, ClassApplyGroup> groupMap = mapByIds(groupIds, ClassApplyGroup.class);
        Map<Integer, User> userMap = mapByIds(userIds, User.class);
        Map<Integer, Student> studentMap = mapByIds(studentIds, Student.class);
        Map<Integer, Region> regionMap = new HashMap<Integer, Region>();
        for (Region region : regionMapper.selectList(new QueryWrapper<Region>().eq("level", 3))) {
            regionMap.put(region.getId(), region);
        }
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (ClassApply apply : applies) {
            ClassApplyGroup group = groupMap.get(apply.getGroupId());
            String courseType = group == null ? "" : safe(group.getCourseType());
            Integer streetId = group == null ? 0 : nvl(group.getStreetId());
            String groupStatus = group == null ? safe(apply.getStatus()) : safe(group.getStatus());
            Region street = regionMap.get(streetId);
            User user = userMap.get(apply.getUserId());
            Student student = studentMap.get(apply.getStudentId());
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("id", apply.getId());
            row.put("createTime", formatTime(apply.getCreateTime()));
            row.put("groupId", nvl(apply.getGroupId()));
            row.put("streetId", streetId);
            row.put("streetName", street == null ? "" : safe(street.getName()));
            row.put("courseType", courseType);
            row.put("joined", group == null ? 0 : nvl(group.getJoined()));
            row.put("target", group == null ? 0 : nvl(group.getTarget()));
            row.put("progress", group == null ? "0/0" : nvl(group.getJoined()) + "/" + nvl(group.getTarget()));
            row.put("groupStatus", groupStatus);
            row.put("groupStatusText", classApplyStatusText(groupStatus));
            row.put("areaId", nvl(apply.getAreaId()));
            row.put("areaName", safe(apply.getAreaName()));
            row.put("userId", nvl(apply.getUserId()));
            row.put("parentName", userName(user));
            row.put("parentMobile", user == null ? "" : safe(user.getMobile()));
            row.put("studentId", nvl(apply.getStudentId()));
            row.put("studentName", student == null ? "" : safe(student.getName()));
            row.put("phone", safe(apply.getPhone()));
            row.put("expectTime", safe(apply.getExpectTime()));
            row.put("note", safe(apply.getNote()));
            row.put("status", safe(apply.getStatus()));
            row.put("statusText", classApplyStatusText(safe(apply.getStatus())));
            rows.add(row);
        }
        return rows;
    }

    private Map<String, Object> classApplySummary(ReportQuery query) {
        Map<String, Object> raw = reportQueryMapper.summaryClassApply(query);
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("total", asInt(raw.get("total")));
        summary.put("groupCount", asInt(raw.get("groupCount")));
        summary.put("studentCount", asInt(raw.get("studentCount")));
        summary.put("pendingCount", asInt(raw.get("pendingCount")));
        summary.put("followingCount", asInt(raw.get("followingCount")));
        summary.put("closedCount", asInt(raw.get("closedCount")));
        return summary;
    }

    // ------------------------------------------------------------------
    // 导出
    // ------------------------------------------------------------------

    private Map<String, Object> export(String type, String sheetName, List<Column> columns,
                                       ReportQuery query, long total, ReportPage fetch,
                                       Map<String, Object> summary, String caliber,
                                       ReportSearchValidate search) {
        int maxRows = exportMaxRows();
        if (total > maxRows) {
            throw new OperateException("导出数据 " + total + " 行，超过单次导出上限 " + maxRows
                    + " 行，请缩小筛选范围后重试");
        }
        String requested = sanitizeFileName(trim(search.getFile_name()));
        String defaultName = "report-" + type + "-" + periodStart(search).replace("-", "")
                + "-" + periodEnd(search).replace("-", "");
        String fileName = requested.isEmpty() ? defaultName : requested;
        File target = exportFileService.allocate(fileName);
        long written = 0;
        List<List<String>> head = new ArrayList<List<String>>();
        for (Column column : columns) {
            head.add(Collections.singletonList(column.title));
        }
        try {
            ExcelWriter writer = EasyExcel.write(target).head(head)
                    .excelType(ExcelTypeEnum.XLSX).build();
            WriteSheet sheet = EasyExcel.writerSheet(sheetName).build();
            if (total == 0) {
                writer.write(Collections.emptyList(), sheet);
            }
            while (written < total) {
                if (written > Integer.MAX_VALUE) {
                    break;
                }
                query.setOffset((int) written);
                query.setLimit(EXPORT_BATCH_SIZE);
                List<Map<String, Object>> rows = fetch.fetch(query);
                if (rows.isEmpty()) {
                    break;
                }
                writer.write(toData(rows, columns), sheet);
                written += rows.size();
            }
            writer.finish();
            long maxBytes = exportMaxFileBytes();
            if (target.length() > maxBytes) {
                String actual = formatMb(target.length());
                String limit = formatMb(maxBytes);
                target.delete();
                throw new OperateException("导出文件大小 " + actual + " MB，超过上限 " + limit
                        + " MB，请缩小筛选范围后重试");
            }
        } catch (RuntimeException error) {
            target.delete();
            throw error;
        }
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.putAll(exportFileService.issue(target, fileName + ".xlsx"));
        result.put("fileName", fileName + ".xlsx");
        result.put("rowCount", written);
        result.put("summary", summary);
        result.put("caliber", caliber);
        result.put("periodStart", periodStart(search));
        result.put("periodEnd", periodEnd(search));
        return result;
    }

    private List<List<Object>> toData(List<Map<String, Object>> rows, List<Column> columns) {
        List<List<Object>> data = new ArrayList<List<Object>>();
        for (Map<String, Object> row : rows) {
            List<Object> line = new ArrayList<Object>();
            for (Column column : columns) {
                Object value = row.get(column.key);
                line.add(value == null ? "" : value);
            }
            data.add(line);
        }
        return data;
    }

    private int exportMaxRows() {
        String value = YmlUtils.get("like.report.export-max-rows");
        if (StringUtils.isEmpty(value)) {
            return DEFAULT_EXPORT_MAX_ROWS;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : DEFAULT_EXPORT_MAX_ROWS;
        } catch (NumberFormatException error) {
            return DEFAULT_EXPORT_MAX_ROWS;
        }
    }

    private long exportMaxFileBytes() {
        BigDecimal mb = DEFAULT_EXPORT_MAX_FILE_MB;
        String value = YmlUtils.get("like.report.export-max-file-size-mb");
        if (StringUtils.isNotEmpty(value)) {
            try {
                BigDecimal parsed = new BigDecimal(value.trim());
                if (parsed.compareTo(ZERO) > 0) {
                    mb = parsed;
                }
            } catch (NumberFormatException ignored) {
                // 配置非法时回退默认上限，避免导出被静默关闭。
            }
        }
        return mb.multiply(new BigDecimal(1024L * 1024L)).longValue();
    }

    private String formatMb(long bytes) {
        return BigDecimal.valueOf(bytes)
                .divide(new BigDecimal(1024L * 1024L), 2, RoundingMode.HALF_UP)
                .stripTrailingZeros().toPlainString();
    }

    // ------------------------------------------------------------------
    // 分页 / 查询辅助
    // ------------------------------------------------------------------

    private interface ReportPage {
        List<Map<String, Object>> fetch(ReportQuery query);
    }

    private List<Map<String, Object>> pageRows(long total, int pageNo, int pageSize,
                                               ReportPage fetch, ReportQuery query) {
        if (total <= 0) {
            return Collections.emptyList();
        }
        long from = (long) (pageNo - 1) * pageSize;
        if (from >= total || from > Integer.MAX_VALUE) {
            return Collections.emptyList();
        }
        query.setOffset((int) from);
        query.setLimit(pageSize);
        return fetch.fetch(query);
    }

    private int pageNo(PageValidate pageValidate) {
        int pageNo = pageValidate == null || pageValidate.getPage_no() == null ? 1 : pageValidate.getPage_no();
        return Math.max(1, pageNo);
    }

    private int pageSize(PageValidate pageValidate) {
        int pageSize = pageValidate == null || pageValidate.getPage_size() == null ? 20 : pageValidate.getPage_size();
        return Math.min(200, Math.max(1, pageSize));
    }

    private ReportQuery baseQuery(ReportSearchValidate search) {
        DateRange range = dateRange(search);
        ReportQuery query = new ReportQuery();
        query.setStartEpoch(range.startEpoch);
        query.setEndEpoch(range.endEpoch);
        query.setStartDate(range.startDate);
        query.setEndDate(range.endDate);
        query.setKeyword(likePattern(search.getKeyword()));
        return query;
    }

    /** 关键词/手机号统一转小写并转义通配符；空值返回 null（对应 SQL 不加条件）。 */
    private String likePattern(String value) {
        String text = trim(value).toLowerCase();
        if (text.isEmpty()) {
            return null;
        }
        String escaped = text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return "%" + escaped + "%";
    }

    private DateRange dateRange(ReportSearchValidate search) {
        String startText = trim(search.getStartDate());
        String endText = trim(search.getEndDate());
        LocalDate end = endText.isEmpty() ? LocalDate.now() : parseDate(endText, "结束日期");
        LocalDate start = startText.isEmpty() ? LocalDate.parse(DEFAULT_START) : parseDate(startText, "开始日期");
        if (start.isAfter(end)) {
            throw new OperateException("开始日期不能晚于结束日期");
        }
        DateRange range = new DateRange();
        range.startDate = start.toString();
        range.endDate = end.toString();
        range.startEpoch = start.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        range.endEpoch = end.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toEpochSecond();
        return range;
    }

    private LocalDate parseDate(String value, String field) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException error) {
            throw new OperateException(field + "格式应为 yyyy-MM-dd");
        }
    }

    private String periodStart(ReportSearchValidate search) {
        return dateRange(search).startDate;
    }

    private String periodEnd(ReportSearchValidate search) {
        return dateRange(search).endDate;
    }

    private Integer resolveCommunityScope(Integer requestedCommunityId) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId == null) {
            return requestedCommunityId;
        }
        Community community = communityMapper.selectOne(new QueryWrapper<Community>()
                .eq("admin_id", adminId).isNull("delete_time").last("limit 1"));
        return community != null ? community.getId() : requestedCommunityId;
    }

    private Map<String, Object> options() {
        Map<String, Object> options = new LinkedHashMap<String, Object>();
        options.put("communities", communityOptions());
        options.put("courses", courseOptions());
        options.put("instructors", instructorOptions());
        options.put("activities", activityOptions());
        options.put("couponGoods", couponGoodsOptions());
        options.put("streets", streetOptions());
        options.put("courseTypes", courseTypeOptions());
        return options;
    }

    private List<Map<String, Object>> communityOptions() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Community community : communityMapper.selectList(new QueryWrapper<Community>()
                .isNull("delete_time").orderByAsc("id"))) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", community.getId());
            item.put("name", safe(community.getName()));
            result.add(item);
        }
        return result;
    }

    private List<Map<String, Object>> courseOptions() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (CourseClass courseClass : courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .eq("is_delete", 0).orderByAsc("id"))) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", courseClass.getId());
            item.put("name", safe(courseClass.getName()));
            item.put("communityId", nvl(courseClass.getCommunityId()));
            item.put("instructorId", nvl(courseClass.getInstructorId()));
            result.add(item);
        }
        return result;
    }

    private List<Map<String, Object>> instructorOptions() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Instructor instructor : instructorMapper.selectList(new QueryWrapper<Instructor>()
                .isNull("delete_time").orderByAsc("id"))) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", instructor.getId());
            item.put("name", safe(instructor.getName()));
            result.add(item);
        }
        return result;
    }

    private List<Map<String, Object>> activityOptions() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Activity activity : activityMapper.selectList(new QueryWrapper<Activity>()
                .isNull("delete_time").orderByDesc("id"))) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", activity.getId());
            item.put("name", safe(activity.getTitle()));
            item.put("activityDate", safe(activity.getActivityDate()));
            result.add(item);
        }
        return result;
    }

    private List<Map<String, Object>> couponGoodsOptions() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (CouponGoods goods : couponGoodsMapper.selectList(new QueryWrapper<CouponGoods>()
                .isNull("delete_time").orderByAsc("sort", "id"))) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", goods.getId());
            item.put("name", safe(goods.getTitle()));
            item.put("times", nvl(goods.getTimes()));
            result.add(item);
        }
        return result;
    }

    private List<Map<String, Object>> streetOptions() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Region region : regionMapper.selectList(new QueryWrapper<Region>()
                .eq("level", 3).isNull("delete_time").orderByAsc("id"))) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", region.getId());
            item.put("name", safe(region.getName()));
            result.add(item);
        }
        return result;
    }

    private List<String> courseTypeOptions() {
        Set<String> types = new LinkedHashSet<String>();
        for (ClassApplyGroup group : classApplyGroupMapper.selectList(new QueryWrapper<ClassApplyGroup>()
                .select("course_type").groupBy("course_type").orderByAsc("course_type"))) {
            if (StringUtils.isNotEmpty(group.getCourseType())) {
                types.add(group.getCourseType());
            }
        }
        return new ArrayList<String>(types);
    }

    private Map<Integer, Community> communityMap() {
        Map<Integer, Community> result = new HashMap<Integer, Community>();
        for (Community community : communityMapper.selectList(new QueryWrapper<Community>().isNull("delete_time"))) {
            result.put(community.getId(), community);
        }
        return result;
    }

    private Map<Integer, Instructor> instructorMap() {
        Map<Integer, Instructor> result = new HashMap<Integer, Instructor>();
        for (Instructor instructor : instructorMapper.selectList(new QueryWrapper<Instructor>().isNull("delete_time"))) {
            result.put(instructor.getId(), instructor);
        }
        return result;
    }

    private <T> Map<Integer, T> mapByIds(Set<Integer> ids, Class<T> clazz) {
        Map<Integer, T> result = new HashMap<Integer, T>();
        if (ids == null || ids.isEmpty()) {
            return result;
        }
        List<Integer> list = new ArrayList<Integer>();
        for (Integer id : ids) {
            if (id != null && id > 0) {
                list.add(id);
            }
        }
        if (list.isEmpty()) {
            return result;
        }
        if (clazz == CourseClass.class) {
            for (CourseClass item : courseClassMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        } else if (clazz == User.class) {
            for (User item : userMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        } else if (clazz == Student.class) {
            for (Student item : studentMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        } else if (clazz == CourseLesson.class) {
            for (CourseLesson item : courseLessonMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        } else if (clazz == CouponInstance.class) {
            for (CouponInstance item : couponInstanceMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        } else if (clazz == CouponGoods.class) {
            for (CouponGoods item : couponGoodsMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        } else if (clazz == Activity.class) {
            for (Activity item : activityMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        } else if (clazz == RechargeOrder.class) {
            for (RechargeOrder item : rechargeOrderMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        } else if (clazz == Area.class) {
            for (Area item : areaMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        } else if (clazz == ClassApplyGroup.class) {
            for (ClassApplyGroup item : classApplyGroupMapper.selectBatchIds(list)) {
                result.put(item.getId(), clazz.cast(item));
            }
        }
        return result;
    }

    private long nvlLong(Long value) {
        return value == null ? 0L : value;
    }

    // ------------------------------------------------------------------
    // 文本/金额辅助
    // ------------------------------------------------------------------

    private List<Column> courseColumns() {
        return Arrays.asList(
                new Column("courseName", "课程班级", 28),
                new Column("communityName", "社区", 18),
                new Column("instructorName", "教练", 14),
                new Column("orderCount", "订单数", 10),
                new Column("paidAmount", "实收金额(元)", 14),
                new Column("purchasedLessons", "购买课时", 10),
                new Column("consumedLessons", "已消课时", 10),
                new Column("attendedStudents", "到场人次", 10),
                new Column("attendedLessons", "上课场次", 10)
        );
    }

    private List<Column> checkinColumns() {
        return Arrays.asList(
                new Column("consumeTime", "核销时间", 20),
                new Column("courseName", "课程班级", 26),
                new Column("communityName", "社区", 18),
                new Column("instructorName", "教练", 14),
                new Column("studentName", "学员", 12),
                new Column("parentName", "家长", 12),
                new Column("parentMobile", "手机号", 15),
                new Column("lessonNo", "课节", 8),
                new Column("lessonDate", "上课日期", 13),
                new Column("timeSlot", "时段", 15),
                new Column("sourceTypeText", "核销来源", 18),
                new Column("bookingStatusText", "预约状态", 16),
                new Column("id", "消课流水ID", 12)
        );
    }

    private List<Column> couponColumns() {
        return Arrays.asList(
                new Column("id", "流水ID", 10),
                new Column("createTime", "时间", 20),
                new Column("typeText", "类型", 12),
                new Column("couponSn", "券编号", 24),
                new Column("couponTitle", "券名称", 20),
                new Column("goodsTitle", "券商品", 20),
                new Column("studentName", "学员", 12),
                new Column("parentName", "家长", 12),
                new Column("times", "变动次数", 10),
                new Column("balance", "变动后余额", 12),
                new Column("orderSn", "关联订单号", 28),
                new Column("operatorTypeText", "操作人类型", 12),
                new Column("remark", "备注", 36)
        );
    }

    private List<Column> activityColumns() {
        return Arrays.asList(
                new Column("id", "报名ID", 10),
                new Column("signupTime", "报名时间", 20),
                new Column("activityTitle", "活动", 24),
                new Column("activityDate", "活动日期", 13),
                new Column("timeSlot", "时段", 14),
                new Column("place", "地点", 22),
                new Column("areaName", "小区", 16),
                new Column("communityName", "社区", 18),
                new Column("studentName", "学员", 12),
                new Column("parentName", "家长", 12),
                new Column("parentMobile", "手机号", 15),
                new Column("statusText", "报名状态", 12),
                new Column("orderSn", "订单号", 28),
                new Column("orderAmount", "订单金额(元)", 14),
                new Column("payStatusText", "支付状态", 12)
        );
    }

    private List<Column> classApplyColumns() {
        return Arrays.asList(
                new Column("id", "申请ID", 10),
                new Column("createTime", "申请时间", 20),
                new Column("streetName", "街道", 16),
                new Column("courseType", "课程类型", 16),
                new Column("progress", "聚合进度", 12),
                new Column("groupStatusText", "组状态", 14),
                new Column("areaName", "小区", 16),
                new Column("studentName", "学员", 12),
                new Column("parentName", "家长", 12),
                new Column("phone", "手机号", 15),
                new Column("expectTime", "期望时间", 16),
                new Column("statusText", "申请状态", 14),
                new Column("note", "备注", 30)
        );
    }

    private List<Map<String, Object>> columnsToMap(List<Column> columns) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Column column : columns) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("key", column.key);
            item.put("title", column.title);
            item.put("width", column.width);
            result.add(item);
        }
        return result;
    }

    private String sourceTypeText(Integer sourceType) {
        if (sourceType == null) {
            return "未知";
        }
        if (sourceType == 0) {
            return "正常核销";
        }
        if (sourceType == 2) {
            return "后台人工补核销";
        }
        if (sourceType == 1) {
            return "历史人工核销";
        }
        return "未知(" + sourceType + ")";
    }

    private String flowTypeText(Integer type) {
        if (type == null) {
            return "未知";
        }
        switch (type) {
            case 1:
                return "发放";
            case 2:
                return "核销扣减";
            case 3:
                return "补发";
            case 4:
                return "作废";
            case 5:
                return "退回";
            default:
                return "未知(" + type + ")";
        }
    }

    private String operatorTypeText(String operatorType) {
        String value = safe(operatorType);
        if ("system".equalsIgnoreCase(value)) {
            return "系统";
        }
        if ("admin".equalsIgnoreCase(value)) {
            return "管理员";
        }
        if ("coach".equalsIgnoreCase(value)) {
            return "教练";
        }
        if ("user".equalsIgnoreCase(value)) {
            return "用户";
        }
        return value;
    }

    private String signupStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        if (status == 0) {
            return "待支付";
        }
        if (status == 1) {
            return "已支付";
        }
        if (status == 3) {
            return "已取消";
        }
        return "未知(" + status + ")";
    }

    private String payStatusText(Integer payStatus) {
        if (payStatus == null) {
            return "";
        }
        if (payStatus == 1) {
            return "已支付";
        }
        if (payStatus == 4) {
            return "已关闭";
        }
        return "未支付";
    }

    private String classApplyStatusText(String status) {
        if ("following".equals(status)) {
            return "客服跟进中";
        }
        if ("closed".equals(status)) {
            return "已开班";
        }
        return "招募中";
    }

    private String bookingKey(Integer userId, Integer studentId, Integer classId, Integer lessonNo) {
        return String.valueOf(nvl(userId)) + "-" + String.valueOf(nvl(studentId)) + "-"
                + String.valueOf(nvl(classId)) + "-" + String.valueOf(nvl(lessonNo));
    }

    private String userName(User user) {
        if (user == null) {
            return "";
        }
        if (StringUtils.isNotEmpty(user.getRealName())) {
            return safe(user.getRealName());
        }
        return safe(user.getNickname());
    }

    private String instructorName(CourseClass courseClass, Map<Integer, Instructor> instructorMap) {
        Instructor instructor = instructorMap.get(courseClass.getInstructorId());
        if (instructor != null && StringUtils.isNotEmpty(instructor.getName())) {
            return safe(instructor.getName());
        }
        return safe(courseClass.getInstructorName());
    }

    private String nameOf(Map<Integer, Community> communityMap, Integer communityId) {
        Community community = communityMap.get(communityId);
        return community == null ? "" : safe(community.getName());
    }

    private String formatTime(Long timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return TimeUtils.timestampToDate(timestamp);
    }

    private BigDecimal round2(BigDecimal value) {
        return (value == null ? ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? ZERO : value;
    }

    private int nvl(Integer value) {
        return value == null ? 0 : value;
    }

    private Integer positive(Integer value) {
        return value == null || value <= 0 ? null : value;
    }

    private Long positive(Long value) {
        return value == null || value <= 0L ? null : value;
    }

    private BigDecimal asDecimal(Object value) {
        if (value == null) {
            return ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(String.valueOf(value));
        } catch (NumberFormatException error) {
            return ZERO;
        }
    }

    private int asInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException error) {
            return 0;
        }
    }

    private Long asLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException error) {
            return 0L;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private String sanitizeFileName(String value) {
        if (value == null) {
            return "";
        }
        String name = value.replaceAll("[\\\\/:*?\"<>|\\s]+", "-");
        name = name.replaceAll("^-+|-+$", "");
        return name.length() > 80 ? name.substring(0, 80) : name;
    }

    /** 自然月窗口（#102 教练月营业额）：供列表/导出/下钻共用同一区间。 */
    private static class MonthWindow {
        private String month;
        private String startDate;
        private String endDate;
        private long startEpoch;
        private long endEpoch;

        private ReportQuery query() {
            ReportQuery query = new ReportQuery();
            query.setStartDate(startDate);
            query.setEndDate(endDate);
            query.setStartEpoch(startEpoch);
            query.setEndEpoch(endEpoch);
            return query;
        }
    }

    private static class Column {
        private final String key;
        private final String title;
        private final int width;

        private Column(String key, String title, int width) {
            this.key = key;
            this.title = title;
            this.width = width;
        }
    }

    private static class DateRange {
        private String startDate;
        private String endDate;
        private long startEpoch;
        private long endEpoch;
    }
}
