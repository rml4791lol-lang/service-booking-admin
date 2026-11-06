package com.mdd.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.mdd.admin.mapper.StatsQuery;
import com.mdd.admin.mapper.StatsQueryMapper;
import com.mdd.admin.service.IReportService;
import com.mdd.admin.service.IStatsService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.report.ReportSearchValidate;
import com.mdd.admin.validate.stats.StatsSearchValidate;
import com.mdd.common.core.PageResult;
import com.mdd.common.exception.OperateException;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计（T36 #106，统计链公共骨架）。
 *
 * <p>教练维度不新写 SQL：直接复用 #102 教练月营业额（订单聚合 + 消课聚合各按班级先聚合再 JOIN），
 * 避免出现第二份教练口径。本类只负责：期间换算、总览/趋势、以及订单/课节明细的
 * 维度下钻（#107/#108/#109 复用同一 Mapper）。</p>
 */
@Service
public class StatsServiceImpl implements IStatsService {

    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final String UNASSIGNED = "未归属";
    /** 趋势默认窗口：近 30 天（含今天）。 */
    private static final int TREND_DEFAULT_DAYS = 30;

    /** 口径说明：导出与接口共用；只描述营业额与工作量的统计方式。 */
    private static final String CALIBER = "营业额=已支付、未退款、未删除的课程订单实付金额"
            + "（SUM(order_amount)，券抵扣不计入）；上课数=区间内不同课节数；服务人次=区间内核销流水行数"
            + "（含历史人工核销）。对象数量为当前有效存量（课程包 la_course_class.is_delete=0，"
            + "社区/小区未删除）。数据只做真实营业额与工作量展示，不做二次分配计算。";

    /** 社区维度口径：#107；客流按 A2 决策=到场人次（区间内核销流水行数，与旧核销报表一致）。 */
    private static final String COMMUNITY_CALIBER = "社区归属=班级所属社区，班级未填社区时用所属小区反查"
            + "（COALESCE(NULLIF(class.community_id,0), area.community_id, 0)），两者都缺归「未归属」；"
            + "订单数=已支付、未退款、未删除的课程订单笔数；营业额=这些订单的实付金额合计；"
            + "客流=到场人次（区间内核销流水行数，含历史人工核销）。只做真实营业额与工作量展示，不做二次分配计算。";

    /** 社区维度导出列（列表列同序，保证导出与页面一致）。 */
    private static final List<String> COMMUNITY_COLUMNS = Arrays.asList(
            "社区", "订单数", "客流(人次)", "营业额(元)");

    /** 小区维度口径：#108；与社区维度同构，只把分组键换成 class.area_id（客流同样按 A2=到场人次）。 */
    private static final String AREA_CALIBER = "小区归属=班级所属小区（COALESCE(class.area_id, 0)），"
            + "未填小区归「未归属」；订单数=已支付、未退款、未删除的课程订单笔数；"
            + "营业额=这些订单的实付金额合计；客流=到场人次（区间内核销流水行数，含历史人工核销）。"
            + "只做真实营业额与工作量展示，不做二次分配计算。";

    /** 小区维度导出列（列表列同序，保证导出与页面一致）。 */
    private static final List<String> AREA_COLUMNS = Arrays.asList(
            "小区", "订单数", "客流(人次)", "营业额(元)");

    /** 老板视角口径（#109）：与 #106/#107/#108 同一套订单/客流/归属口径 + 未归属显式成行。 */
    private static final String OWNER_CALIBER = "老板视角：营业额=已支付、未退款、未删除的课程订单实付金额"
            + "（SUM(order_amount)，按 pay_time 归期）；订单数=同口径订单笔数；客流=到场人次"
            + "（区间内核销流水行数）；社区归属=COALESCE(NULLIF(class.community_id,0), area.community_id, 0)，"
            + "片区=社区所属街道的上级分区（la_region.level=2），教练归属=class.instructor_id；"
            + "「未归属/未分配」始终显式成行，各维度行合计与总览一致（差异不丢弃）。"
            + "只做真实营业额与工作量展示，不做二次分配计算。";

    /** 课程包销量口径（#109）：购买数/报名数来自报名记录，金额来自已支付未退款订单。 */
    private static final String COURSE_PACK_CALIBER = "课程包销量：购买数=COUNT(DISTINCT la_user_course.id)，"
            + "报名数=COUNT(DISTINCT la_user_course.user_id)（有多少人报名了这个包），"
            + "均按 la_user_course.create_time 归期；金额=已支付、未退款、未删除订单的实付金额合计，按 pay_time 归期；"
            + "购买数与金额先各自聚合再合并，不按明细行放大。列出全部有购买记录或有期间订单的课程包"
            + "（不硬编码固定包数量）；班级已删除/缺失的购买与订单归「未归属」行。";

    /** 老板视角排序白名单（前端只传键名，服务端只命中枚举，绝不拼 SQL）。 */
    private static final List<String> OWNER_ORDER_KEYS = Collections.unmodifiableList(
            Arrays.asList("revenue", "orderCount", "attends"));
    /** 老板视角默认返回行数（社区最好 / 教练最好）。 */
    private static final int OWNER_DEFAULT_LIMIT = 20;
    /** 老板视角单次返回上限（0=全部，导出默认全量）。 */
    private static final int OWNER_MAX_LIMIT = 200;
    /** 无教练归属的桶名（与 #102 教练月营业额保持一致）。 */
    private static final String UNASSIGNED_COACH = "未分配教练";

    private static final List<String> REGION_COLUMNS = Arrays.asList(
            "片区", "覆盖社区数", "订单数", "营业额(元)");
    private static final List<String> COMMUNITY_TOP_COLUMNS = Arrays.asList(
            "社区", "订单数", "客流(人次)", "营业额(元)");
    private static final List<String> INSTRUCTOR_TOP_COLUMNS = Arrays.asList(
            "教练", "订单数", "客流(人次)", "营业额(元)");
    private static final List<String> COURSE_MIX_COLUMNS = Arrays.asList(
            "课程班级", "类目", "订单数", "营业额(元)");
    private static final List<String> COURSE_PACK_COLUMNS = Arrays.asList(
            "课程包", "购买数", "金额(元)", "报名数");

    /** 导出单次行数上限，与旧报表共用同一配置键。 */
    private static final int DEFAULT_EXPORT_MAX_ROWS = 50000;

    @Resource
    StatsQueryMapper statsQueryMapper;

    /** 教练维度复用 #102 的教练月营业额实现（同一份 SQL / 导出 / 下钻）。 */
    @Resource
    IReportService iReportService;

    // ------------------------------------------------------------------
    // 一级：对象数量 + 期间总览 + 环比
    // ------------------------------------------------------------------

    @Override
    public Map<String, Object> overview(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        StatsQuery query = buildQuery(searchValidate, period, true);

        Map<String, Object> counts = statsQueryMapper.objectCounts();
        Map<String, Object> summary = statsQueryMapper.periodSummary(query);

        Map<String, Object> objects = new LinkedHashMap<String, Object>();
        objects.put("instructorCount", asInt(counts.get("instructorCount")));
        objects.put("coursePackCount", asInt(counts.get("coursePackCount")));
        objects.put("coursePackOnSaleCount", asInt(counts.get("coursePackOnSaleCount")));
        objects.put("communityCount", asInt(counts.get("communityCount")));
        objects.put("areaCount", asInt(counts.get("areaCount")));
        objects.put("orderCount", asInt(summary.get("orderCount")));

        Map<String, Object> amounts = new LinkedHashMap<String, Object>();
        amounts.put("revenue", round2(asDecimal(summary.get("revenue"))));
        amounts.put("lessonCount", asInt(summary.get("lessonCount")));
        amounts.put("servicePersons", asInt(summary.get("servicePersons")));

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("period", period.toMap());
        data.put("objects", objects);
        data.put("amounts", amounts);
        data.put("previous", shouldCompare(searchValidate) ? previous(period, query, summary) : null);
        data.put("caliber", CALIBER);
        return data;
    }

    /** 环比：上一自然月 / 上等长区间；营业额外加金额差与百分比（上期为 0 时百分比为 null）。 */
    private Map<String, Object> previous(Period period, StatsQuery baseQuery,
                                         Map<String, Object> currentSummary) {
        StatsQuery query = new StatsQuery();
        query.setStartEpoch(period.previousStartEpoch);
        query.setEndEpoch(period.previousEndEpoch);
        query.setInstructorId(baseQuery.getInstructorId());
        query.setCommunityId(baseQuery.getCommunityId());
        query.setAreaId(baseQuery.getAreaId());
        query.setCourseClassId(baseQuery.getCourseClassId());
        query.setKeyword(baseQuery.getKeyword());
        Map<String, Object> summary = statsQueryMapper.periodSummary(query);
        int orderCount = asInt(summary.get("orderCount"));
        BigDecimal revenue = round2(asDecimal(summary.get("revenue")));
        int currentOrderCount = asInt(currentSummary.get("orderCount"));
        BigDecimal currentRevenue = round2(asDecimal(currentSummary.get("revenue")));

        Map<String, Object> previous = new LinkedHashMap<String, Object>();
        previous.put("startDate", period.previousStartDate);
        previous.put("endDate", period.previousEndDate);
        previous.put("orderCount", orderCount);
        previous.put("revenue", revenue);
        previous.put("orderDelta", currentOrderCount - orderCount);
        previous.put("revenueDelta", currentRevenue.subtract(revenue));
        previous.put("revenueRatio", revenue.compareTo(ZERO) == 0
                ? null
                : currentRevenue.subtract(revenue).divide(revenue, 4, RoundingMode.HALF_UP));
        return previous;
    }

    // ------------------------------------------------------------------
    // 二级：营业额按日趋势
    // ------------------------------------------------------------------

    @Override
    public Map<String, Object> trend(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, true);
        StatsQuery query = buildQuery(searchValidate, period, false);
        List<Map<String, Object>> rawRows = statsQueryMapper.trendRows(query);

        Map<String, Map<String, Object>> byDate = new LinkedHashMap<String, Map<String, Object>>();
        for (Map<String, Object> raw : rawRows) {
            byDate.put(safe((String) raw.get("date")), raw);
        }

        LocalDate start = LocalDate.parse(period.startDate);
        LocalDate end = LocalDate.parse(period.endDate);
        List<Map<String, Object>> points = new ArrayList<Map<String, Object>>();
        int totalOrders = 0;
        BigDecimal totalRevenue = ZERO;
        BigDecimal maxRevenue = ZERO;
        for (LocalDate cursor = start; !cursor.isAfter(end); cursor = cursor.plusDays(1)) {
            String date = cursor.toString();
            Map<String, Object> raw = byDate.get(date);
            int orderCount = raw == null ? 0 : asInt(raw.get("orderCount"));
            BigDecimal revenue = raw == null ? ZERO : round2(asDecimal(raw.get("revenue")));
            Map<String, Object> point = new LinkedHashMap<String, Object>();
            point.put("date", date);
            point.put("orderCount", orderCount);
            point.put("revenue", revenue);
            points.add(point);
            totalOrders += orderCount;
            totalRevenue = totalRevenue.add(revenue);
            if (revenue.compareTo(maxRevenue) > 0) {
                maxRevenue = revenue;
            }
        }
        long days = ChronoUnit.DAYS.between(start, end) + 1;

        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("days", days);
        summary.put("orderCount", totalOrders);
        summary.put("revenue", round2(totalRevenue));
        summary.put("maxRevenue", round2(maxRevenue));
        summary.put("avgRevenue", days <= 0 ? ZERO : round2(totalRevenue.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP)));

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        Map<String, Object> periodMap = period.toMap();
        periodMap.put("granularity", "day");
        data.put("period", periodMap);
        data.put("points", points);
        data.put("summary", summary);
        data.put("caliber", CALIBER);
        return data;
    }

    // ------------------------------------------------------------------
    // 教练维度：复用 #102 教练月营业额（列表 / 导出 / 下钻）
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> instructorList(PageValidate pageValidate,
                                                          StatsSearchValidate searchValidate) {
        return iReportService.coachMonthList(pageValidate, toReportSearch(searchValidate, false));
    }

    @Override
    public Map<String, Object> instructorExport(StatsSearchValidate searchValidate) {
        ReportSearchValidate search = toReportSearch(searchValidate, false);
        if (trim(search.getFile_name()).isEmpty()) {
            search.setFile_name("stats-instructor");
        }
        return iReportService.coachMonthExport(search);
    }

    @Override
    public Map<String, Object> instructorDetail(StatsSearchValidate searchValidate) {
        return iReportService.coachMonthDetail(toReportSearch(searchValidate, true));
    }

    // ------------------------------------------------------------------
    // 社区维度（#107）：订单数 / 客流（到场人次）/ 营业额
    // ------------------------------------------------------------------

    /**
     * 社区列表：全量与下钻复用同一份聚合（{@code communityStats}）。
     *
     * <p>聚合结果最多几百行（社区数量级），PageResult 分页在内存切分，
     * 好处是列表与导出共用同一份 SQL 结果，两端口径与「未归属」行天然一致。</p>
     */
    @Override
    public PageResult<Map<String, Object>> communityList(PageValidate pageValidate,
                                                         StatsSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        Period period = resolvePeriod(searchValidate, false);
        List<Map<String, Object>> rows = communityRows(buildQuery(searchValidate, period, false));
        int total = rows.size();
        int from = Math.min((pageNo - 1) * pageSize, total);
        int to = Math.min(from + pageSize, total);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("periodStart", period.startDate);
        extend.put("periodEnd", period.endDate);
        extend.put("caliber", COMMUNITY_CALIBER);
        return PageResult.iPageHandle((long) total, (long) pageNo, (long) pageSize,
                new ArrayList<Map<String, Object>>(rows.subList(from, to)), extend);
    }

    @Override
    public Map<String, Object> communityExport(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        List<Map<String, Object>> rows = communityRows(buildQuery(searchValidate, period, false));
        int maxRows = exportMaxRows();
        if (rows.size() > maxRows) {
            throw new OperateException("导出数据 " + rows.size() + " 行，超过单次导出上限 " + maxRows
                    + " 行，请缩小筛选范围后重试");
        }
        String requested = sanitizeFileName(trim(searchValidate.getFile_name()));
        String fileName = requested.isEmpty() ? "stats-community-"
                + period.startDate.replace("-", "") + "-" + period.endDate.replace("-", "") : requested;
        String folderPath = "/excel/export/" + TimeUtils.timestampToDay(System.currentTimeMillis() / 1000L) + "/";
        String path = folderPath + fileName + "-" + System.currentTimeMillis() + ".xlsx";
        String uploadDirectory = YmlUtils.get("like.upload-directory");
        File folder = new File(uploadDirectory + folderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new OperateException("创建导出目录失败");
        }
        File target = new File(uploadDirectory + path);
        List<List<String>> head = new ArrayList<List<String>>();
        for (String title : COMMUNITY_COLUMNS) {
            head.add(Collections.singletonList(title));
        }
        try {
            ExcelWriter writer = EasyExcel.write(target).head(head).excelType(ExcelTypeEnum.XLSX).build();
            WriteSheet sheet = EasyExcel.writerSheet("社区维度").build();
            List<List<Object>> data = new ArrayList<List<Object>>();
            for (Map<String, Object> row : rows) {
                List<Object> line = new ArrayList<Object>();
                line.add(row.get("communityName"));
                line.add(row.get("orderCount"));
                line.add(row.get("attends"));
                line.add(row.get("revenue"));
                data.add(line);
            }
            writer.write(data, sheet);
            writer.finish();
        } catch (RuntimeException error) {
            target.delete();
            throw error;
        }
        int orderCount = 0;
        int attends = 0;
        BigDecimal revenue = ZERO;
        for (Map<String, Object> row : rows) {
            orderCount += asInt(row.get("orderCount"));
            attends += asInt(row.get("attends"));
            revenue = revenue.add(asDecimal(row.get("revenue")));
        }
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("communityCount", rows.size());
        summary.put("orderCount", orderCount);
        summary.put("attends", attends);
        summary.put("revenue", round2(revenue));

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("url", UrlUtils.toAdminAbsoluteUrl(path));
        result.put("fileName", fileName + ".xlsx");
        result.put("rowCount", rows.size());
        result.put("summary", summary);
        result.put("caliber", COMMUNITY_CALIBER);
        result.put("periodStart", period.startDate);
        result.put("periodEnd", period.endDate);
        return result;
    }

    /** 社区聚合行：归属 0 显示「未归属」，社区已删除时回落为「社区#id」。 */
    private List<Map<String, Object>> communityRows(StatsQuery query) {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> raw : statsQueryMapper.communityStats(query)) {
            int communityId = asInt(raw.get("communityId"));
            String name = trim(safe((String) raw.get("communityName")));
            if (communityId != 0 && name.isEmpty()) {
                name = "社区#" + communityId;
            }
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("communityId", communityId);
            row.put("communityName", communityId == 0 ? UNASSIGNED : name);
            row.put("orderCount", asInt(raw.get("orderCount")));
            row.put("attends", asInt(raw.get("attends")));
            row.put("revenue", round2(asDecimal(raw.get("revenue"))));
            rows.add(row);
        }
        return rows;
    }

    // ------------------------------------------------------------------
    // 小区维度（#108）：订单数 / 客流（到场人次）/ 营业额（与 #107 同构，只换分组键）
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> areaList(PageValidate pageValidate,
                                                    StatsSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        Period period = resolvePeriod(searchValidate, false);
        List<Map<String, Object>> rows = areaRows(buildQuery(searchValidate, period, false));
        int total = rows.size();
        int from = Math.min((pageNo - 1) * pageSize, total);
        int to = Math.min(from + pageSize, total);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("periodStart", period.startDate);
        extend.put("periodEnd", period.endDate);
        extend.put("caliber", AREA_CALIBER);
        return PageResult.iPageHandle((long) total, (long) pageNo, (long) pageSize,
                new ArrayList<Map<String, Object>>(rows.subList(from, to)), extend);
    }

    @Override
    public Map<String, Object> areaExport(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        List<Map<String, Object>> rows = areaRows(buildQuery(searchValidate, period, false));
        int maxRows = exportMaxRows();
        if (rows.size() > maxRows) {
            throw new OperateException("导出数据 " + rows.size() + " 行，超过单次导出上限 " + maxRows
                    + " 行，请缩小筛选范围后重试");
        }
        String requested = sanitizeFileName(trim(searchValidate.getFile_name()));
        String fileName = requested.isEmpty() ? "stats-area-"
                + period.startDate.replace("-", "") + "-" + period.endDate.replace("-", "") : requested;
        String folderPath = "/excel/export/" + TimeUtils.timestampToDay(System.currentTimeMillis() / 1000L) + "/";
        String path = folderPath + fileName + "-" + System.currentTimeMillis() + ".xlsx";
        String uploadDirectory = YmlUtils.get("like.upload-directory");
        File folder = new File(uploadDirectory + folderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new OperateException("创建导出目录失败");
        }
        File target = new File(uploadDirectory + path);
        List<List<String>> head = new ArrayList<List<String>>();
        for (String title : AREA_COLUMNS) {
            head.add(Collections.singletonList(title));
        }
        try {
            ExcelWriter writer = EasyExcel.write(target).head(head).excelType(ExcelTypeEnum.XLSX).build();
            WriteSheet sheet = EasyExcel.writerSheet("小区维度").build();
            List<List<Object>> data = new ArrayList<List<Object>>();
            for (Map<String, Object> row : rows) {
                List<Object> line = new ArrayList<Object>();
                line.add(row.get("areaName"));
                line.add(row.get("orderCount"));
                line.add(row.get("attends"));
                line.add(row.get("revenue"));
                data.add(line);
            }
            writer.write(data, sheet);
            writer.finish();
        } catch (RuntimeException error) {
            target.delete();
            throw error;
        }
        int orderCount = 0;
        int attends = 0;
        BigDecimal revenue = ZERO;
        for (Map<String, Object> row : rows) {
            orderCount += asInt(row.get("orderCount"));
            attends += asInt(row.get("attends"));
            revenue = revenue.add(asDecimal(row.get("revenue")));
        }
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("areaCount", rows.size());
        summary.put("orderCount", orderCount);
        summary.put("attends", attends);
        summary.put("revenue", round2(revenue));

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("url", UrlUtils.toAdminAbsoluteUrl(path));
        result.put("fileName", fileName + ".xlsx");
        result.put("rowCount", rows.size());
        result.put("summary", summary);
        result.put("caliber", AREA_CALIBER);
        result.put("periodStart", period.startDate);
        result.put("periodEnd", period.endDate);
        return result;
    }

    /** 小区聚合行：归属 0 显示「未归属」，小区已删除时回落为「小区#id」。 */
    private List<Map<String, Object>> areaRows(StatsQuery query) {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> raw : statsQueryMapper.areaStats(query)) {
            int areaId = asInt(raw.get("areaId"));
            String name = trim(safe((String) raw.get("areaName")));
            if (areaId != 0 && name.isEmpty()) {
                name = "小区#" + areaId;
            }
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("areaId", areaId);
            row.put("areaName", areaId == 0 ? UNASSIGNED : name);
            row.put("orderCount", asInt(raw.get("orderCount")));
            row.put("attends", asInt(raw.get("attends")));
            row.put("revenue", round2(asDecimal(raw.get("revenue"))));
            rows.add(row);
        }
        return rows;
    }

    // ------------------------------------------------------------------
    // 老板视角（#109）：片区覆盖 / 社区最好 / 教练最好 / 上了什么课
    // ------------------------------------------------------------------

    @Override
    public Map<String, Object> ownerRegion(StatsSearchValidate searchValidate) {
        return ownerRegionData(resolvePeriod(searchValidate, false));
    }

    @Override
    public Map<String, Object> ownerRegionExport(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        Map<String, Object> data = ownerRegionData(period);
        return writeOwnerExcel("stats-owner-region", "片区覆盖", REGION_COLUMNS,
                Arrays.asList("regionName", "communityCount", "orderCount", "revenue"),
                asRowList(data.get("rows")), OWNER_CALIBER, period, searchValidate);
    }

    /**
     * 片区覆盖：不新写社区口径 —— 直接复用 #107 的 {@code communityStats}
     * （含全部有效社区与「未归属」行），再用 社区 → 街道(level=3) → 分区(level=2) 映射上卷到片区。
     * 归 0 的社区/订单进「未归属」片区行，保证行合计与总览一致。
     */
    private Map<String, Object> ownerRegionData(Period period) {
        StatsQuery query = buildOwnerQuery(period);
        List<Map<String, Object>> communityRows = communityRows(query);
        Map<Integer, RegionBucket> buckets = new LinkedHashMap<Integer, RegionBucket>();
        for (Map<String, Object> node : statsQueryMapper.regionNodes()) {
            int regionId = asInt(node.get("regionId"));
            buckets.put(regionId, new RegionBucket(regionId, safe((String) node.get("regionName")),
                    safe((String) node.get("cityName"))));
        }
        RegionBucket unassigned = new RegionBucket(0, UNASSIGNED, "");
        Map<Integer, Integer> regionOfCommunity = new LinkedHashMap<Integer, Integer>();
        for (Map<String, Object> raw : statsQueryMapper.communityRegionRows()) {
            int communityId = asInt(raw.get("communityId"));
            int regionId = asInt(raw.get("regionId"));
            int level = asInt(raw.get("regionLevel"));
            int parentId = asInt(raw.get("parentRegionId"));
            int target = level == 2 ? regionId : (level == 3 ? parentId : 0);
            if (target != 0 && !buckets.containsKey(target)) {
                target = 0;
            }
            regionOfCommunity.put(communityId, target);
            bucketOf(target, buckets, unassigned).communityCount += 1;
        }
        for (Map<String, Object> row : communityRows) {
            int communityId = asInt(row.get("communityId"));
            Integer mapped = communityId == 0 ? Integer.valueOf(0) : regionOfCommunity.get(communityId);
            RegionBucket bucket = bucketOf(mapped == null ? 0 : mapped.intValue(), buckets, unassigned);
            bucket.orderCount += asInt(row.get("orderCount"));
            bucket.revenue = bucket.revenue.add(asDecimal(row.get("revenue")));
        }
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        int assignedCommunities = 0;
        for (RegionBucket bucket : buckets.values()) {
            assignedCommunities += bucket.communityCount;
            rows.add(bucket.toRow());
        }
        if (unassigned.communityCount > 0 || unassigned.orderCount > 0
                || unassigned.revenue.compareTo(ZERO) != 0) {
            rows.add(unassigned.toRow());
        }
        Map<String, Object> totals = ownerTotals(query);
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("regionCount", buckets.size());
        summary.put("communityCount", assignedCommunities + unassigned.communityCount);
        summary.put("assignedCommunityCount", assignedCommunities);
        summary.put("unassignedCommunityCount", unassigned.communityCount);
        summary.put("orderCount", sumInt(rows, "orderCount"));
        summary.put("revenue", sumMoney(rows, "revenue"));
        summary.put("totalOrderCount", totals.get("orderCount"));
        summary.put("totalRevenue", totals.get("revenue"));
        summary.put("unassigned", unassigned.toRow());

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("period", period.toMap());
        data.put("rows", rows);
        data.put("summary", summary);
        data.put("caliber", OWNER_CALIBER);
        return data;
    }

    @Override
    public Map<String, Object> ownerCommunityTop(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        return ownerCommunityTopData(period,
                resolveOwnerOrderBy(searchValidate.getOrderBy()),
                resolveOwnerLimit(searchValidate.getLimit()));
    }

    @Override
    public Map<String, Object> ownerCommunityTopExport(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        String orderBy = resolveOwnerOrderBy(searchValidate.getOrderBy());
        Map<String, Object> data = ownerCommunityTopData(period, orderBy, 0);
        return writeOwnerExcel("stats-owner-community-top", "社区效果排行", COMMUNITY_TOP_COLUMNS,
                Arrays.asList("communityName", "orderCount", "attends", "revenue"),
                asRowList(data.get("rows")), OWNER_CALIBER, period, searchValidate);
    }

    /**
     * 社区效果排行：复用 #107 的社区聚合，再按后端白名单字段排序（营业额 DESC、并列看订单数）。
     * 排序在内存完成 —— 前端传的 orderBy 只用于命中白名单枚举，绝不拼进 SQL。
     */
    private Map<String, Object> ownerCommunityTopData(Period period, String orderBy, int limit) {
        StatsQuery query = buildOwnerQuery(period);
        List<Map<String, Object>> allRows = communityRows(query);
        sortOwnerRows(allRows, orderBy, "communityId");
        List<Map<String, Object>> rows = truncateRows(allRows, limit);
        for (int index = 0; index < rows.size(); index++) {
            rows.get(index).put("rank", index + 1);
        }
        Map<String, Object> unassigned = ownerBucket(allRows, "communityId", 0);
        if (unassigned == null) {
            unassigned = new LinkedHashMap<String, Object>();
            unassigned.put("communityId", 0);
            unassigned.put("communityName", UNASSIGNED);
            unassigned.put("orderCount", 0);
            unassigned.put("attends", 0);
            unassigned.put("revenue", ZERO);
        }
        Map<String, Object> totals = ownerTotals(query);
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("rowCount", allRows.size());
        summary.put("listedRowCount", rows.size());
        summary.put("orderBy", orderBy);
        summary.put("limit", limit);
        summary.put("orderCount", sumInt(allRows, "orderCount"));
        summary.put("revenue", sumMoney(allRows, "revenue"));
        summary.put("attends", sumInt(allRows, "attends"));
        summary.put("totalOrderCount", totals.get("orderCount"));
        summary.put("totalRevenue", totals.get("revenue"));
        summary.put("unassigned", unassigned);
        summary.put("truncated", limit > 0 && allRows.size() > limit);

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("period", period.toMap());
        data.put("rows", rows);
        data.put("summary", summary);
        data.put("caliber", OWNER_CALIBER);
        return data;
    }

    @Override
    public Map<String, Object> ownerInstructorTop(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        return ownerInstructorTopData(period,
                resolveOwnerOrderBy(searchValidate.getOrderBy()),
                resolveOwnerLimit(searchValidate.getLimit()));
    }

    @Override
    public Map<String, Object> ownerInstructorTopExport(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        String orderBy = resolveOwnerOrderBy(searchValidate.getOrderBy());
        Map<String, Object> data = ownerInstructorTopData(period, orderBy, 0);
        return writeOwnerExcel("stats-owner-instructor-top", "教练效果排行", INSTRUCTOR_TOP_COLUMNS,
                Arrays.asList("instructorName", "orderCount", "attends", "revenue"),
                asRowList(data.get("rows")), OWNER_CALIBER, period, searchValidate);
    }

    /**
     * 教练效果排行：复用 #102 教练月营业额（同一份 SQL，不复制第二份教练口径），
     * 再补「未分配教练」残差行 —— 订单无有效课程班级（course_class_id=0 / 班级已删除）时
     * #102 从班级侧聚合覆盖不到，这些单显式并入未分配教练行，保证行合计 == 总览。
     *
     * <p>排序在内存完成，orderBy 只命中白名单枚举；limit=0 表示全部（导出默认全量）。</p>
     */
    private Map<String, Object> ownerInstructorTopData(Period period, String orderBy, int limit) {
        StatsQuery query = buildOwnerQuery(period);
        PageValidate pageValidate = new PageValidate();
        pageValidate.setPage_no(1);
        pageValidate.setPage_size(OWNER_MAX_LIMIT);
        PageResult<Map<String, Object>> page = iReportService.coachMonthList(pageValidate,
                ownerReportSearch(period));
        int coachRowCount = page.getLists() == null ? 0 : page.getLists().size();
        List<Map<String, Object>> allRows = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> raw : page.getLists()) {
            int instructorId = asInt(raw.get("coachId"));
            String name = trim(safe((String) raw.get("coachName")));
            if (instructorId != 0 && name.isEmpty()) {
                name = "教练#" + instructorId;
            }
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("instructorId", instructorId);
            row.put("instructorName", instructorId == 0 ? UNASSIGNED_COACH : name);
            row.put("orderCount", asInt(raw.get("orderCount")));
            row.put("revenue", round2(asDecimal(raw.get("paidAmount"))));
            row.put("attends", asInt(raw.get("servicePersons")));
            row.put("lessonCount", asInt(raw.get("lessonCount")));
            allRows.add(row);
        }
        Map<String, Object> orphan = statsQueryMapper.orphanOrders(query);
        int orphanOrders = asInt(orphan.get("orderCount"));
        BigDecimal orphanRevenue = round2(asDecimal(orphan.get("revenue")));
        if (orphanOrders > 0 || orphanRevenue.compareTo(ZERO) != 0) {
            Map<String, Object> target = ownerBucket(allRows, "instructorId", 0);
            if (target == null) {
                target = new LinkedHashMap<String, Object>();
                target.put("instructorId", 0);
                target.put("instructorName", UNASSIGNED_COACH);
                target.put("orderCount", 0);
                target.put("revenue", ZERO);
                target.put("attends", 0);
                target.put("lessonCount", 0);
                allRows.add(target);
            }
            target.put("orderCount", asInt(target.get("orderCount")) + orphanOrders);
            target.put("revenue", round2(asDecimal(target.get("revenue")).add(orphanRevenue)));
        }
        sortOwnerRows(allRows, orderBy, "instructorId");
        List<Map<String, Object>> rows = truncateRows(allRows, limit);
        for (int index = 0; index < rows.size(); index++) {
            rows.get(index).put("rank", index + 1);
        }
        Map<String, Object> unassigned = ownerBucket(allRows, "instructorId", 0);
        if (unassigned == null) {
            unassigned = new LinkedHashMap<String, Object>();
            unassigned.put("instructorId", 0);
            unassigned.put("instructorName", UNASSIGNED_COACH);
            unassigned.put("orderCount", 0);
            unassigned.put("attends", 0);
            unassigned.put("revenue", ZERO);
        }
        Map<String, Object> totals = ownerTotals(query);
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("rowCount", allRows.size());
        summary.put("listedRowCount", rows.size());
        summary.put("orderBy", orderBy);
        summary.put("limit", limit);
        summary.put("orderCount", sumInt(allRows, "orderCount"));
        summary.put("revenue", sumMoney(allRows, "revenue"));
        summary.put("attends", sumInt(allRows, "attends"));
        summary.put("totalOrderCount", totals.get("orderCount"));
        summary.put("totalRevenue", totals.get("revenue"));
        summary.put("unassigned", unassigned);
        summary.put("truncated", limit > 0 && allRows.size() > limit);
        summary.put("sourceTruncated", page.getCount() != null && page.getCount().intValue() > coachRowCount);

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("period", period.toMap());
        data.put("rows", rows);
        data.put("summary", summary);
        data.put("caliber", OWNER_CALIBER);
        return data;
    }

    @Override
    public Map<String, Object> ownerCourseMix(StatsSearchValidate searchValidate) {
        return ownerCourseMixData(resolvePeriod(searchValidate, false));
    }

    @Override
    public Map<String, Object> ownerCourseMixExport(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        Map<String, Object> data = ownerCourseMixData(period);
        return writeOwnerExcel("stats-owner-course-mix", "课程构成", COURSE_MIX_COLUMNS,
                Arrays.asList("courseName", "categoryName", "orderCount", "revenue"),
                asRowList(data.get("rows")), OWNER_CALIBER, period, searchValidate);
    }

    /** 课程构成（上了什么课）：按 课程班级 + 类目 聚合期间订单；无有效班级的订单归「未归属」行。 */
    private Map<String, Object> ownerCourseMixData(Period period) {
        StatsQuery query = buildOwnerQuery(period);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> raw : statsQueryMapper.courseMixRows(query)) {
            int courseClassId = asInt(raw.get("courseClassId"));
            int categoryId = asInt(raw.get("categoryId"));
            String courseName = trim(safe((String) raw.get("courseName")));
            String categoryName = trim(safe((String) raw.get("categoryName")));
            if (courseClassId != 0 && courseName.isEmpty()) {
                courseName = "课程班级#" + courseClassId;
            }
            if (categoryId != 0 && categoryName.isEmpty()) {
                categoryName = "类目#" + categoryId;
            }
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("courseClassId", courseClassId);
            row.put("courseName", courseClassId == 0 ? UNASSIGNED : courseName);
            row.put("categoryId", categoryId);
            row.put("categoryName", categoryId == 0 ? UNASSIGNED : categoryName);
            row.put("orderCount", asInt(raw.get("orderCount")));
            row.put("revenue", round2(asDecimal(raw.get("revenue"))));
            rows.add(row);
        }
        Map<String, Object> unassigned = ownerBucket(rows, "courseClassId", 0);
        if (unassigned == null) {
            unassigned = new LinkedHashMap<String, Object>();
            unassigned.put("courseClassId", 0);
            unassigned.put("courseName", UNASSIGNED);
            unassigned.put("orderCount", 0);
            unassigned.put("revenue", ZERO);
        }
        Map<String, Object> totals = ownerTotals(query);
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("rowCount", rows.size());
        summary.put("orderCount", sumInt(rows, "orderCount"));
        summary.put("revenue", sumMoney(rows, "revenue"));
        summary.put("totalOrderCount", totals.get("orderCount"));
        summary.put("totalRevenue", totals.get("revenue"));
        summary.put("unassigned", unassigned);

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("period", period.toMap());
        data.put("rows", rows);
        data.put("summary", summary);
        data.put("caliber", OWNER_CALIBER);
        return data;
    }

    // ------------------------------------------------------------------
    // 课程包销量（#109）：购买数 / 金额 / 报名数
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> coursePackList(PageValidate pageValidate,
                                                          StatsSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        Period period = resolvePeriod(searchValidate, false);
        StatsQuery query = buildOwnerQuery(period);
        List<Map<String, Object>> rows = coursePackRows(query);
        int total = rows.size();
        int from = Math.min((pageNo - 1) * pageSize, total);
        int to = Math.min(from + pageSize, total);
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("periodStart", period.startDate);
        extend.put("periodEnd", period.endDate);
        extend.put("caliber", COURSE_PACK_CALIBER);
        extend.put("summary", coursePackSummary(rows, query));
        return PageResult.iPageHandle((long) total, (long) pageNo, (long) pageSize,
                new ArrayList<Map<String, Object>>(rows.subList(from, to)), extend);
    }

    @Override
    public Map<String, Object> coursePackExport(StatsSearchValidate searchValidate) {
        Period period = resolvePeriod(searchValidate, false);
        StatsQuery query = buildOwnerQuery(period);
        List<Map<String, Object>> rows = coursePackRows(query);
        Map<String, Object> result = writeOwnerExcel("stats-course-pack", "课程包销量", COURSE_PACK_COLUMNS,
                Arrays.asList("coursePackName", "purchases", "revenue", "enrollCount"),
                rows, COURSE_PACK_CALIBER, period, searchValidate);
        result.put("summary", coursePackSummary(rows, query));
        return result;
    }

    /**
     * 课程包销量行：购买数/报名数来自 la_user_course，金额来自期间已支付未退款订单，
     * 两者各自先聚合再 JOIN（禁止同一条 GROUP BY 里 JOIN 明细表，避免金额 ×N 放大）。
     * 不硬编码「12 个课程包」：列出全部有购买记录或有期间订单的课程包（库/代码里没有 12 这个对象）。
     */
    private List<Map<String, Object>> coursePackRows(StatsQuery query) {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> raw : statsQueryMapper.coursePackRows(query)) {
            int coursePackId = asInt(raw.get("coursePackId"));
            String name = trim(safe((String) raw.get("coursePackName")));
            if (coursePackId != 0 && name.isEmpty()) {
                name = "课程包#" + coursePackId;
            }
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("coursePackId", coursePackId);
            row.put("coursePackName", coursePackId == 0 ? UNASSIGNED : name);
            row.put("categoryName", safe((String) raw.get("categoryName")));
            row.put("purchases", asInt(raw.get("purchases")));
            row.put("revenue", round2(asDecimal(raw.get("revenue"))));
            row.put("enrollCount", asInt(raw.get("enrollCount")));
            row.put("orderCount", asInt(raw.get("orderCount")));
            rows.add(row);
        }
        return rows;
    }

    private Map<String, Object> coursePackSummary(List<Map<String, Object>> rows, StatsQuery query) {
        Map<String, Object> totals = ownerTotals(query);
        int purchases = 0;
        int enrollCount = 0;
        for (Map<String, Object> row : rows) {
            purchases += asInt(row.get("purchases"));
            enrollCount += asInt(row.get("enrollCount"));
        }
        Map<String, Object> unassigned = ownerBucket(rows, "coursePackId", 0);
        if (unassigned == null) {
            unassigned = new LinkedHashMap<String, Object>();
            unassigned.put("coursePackId", 0);
            unassigned.put("coursePackName", UNASSIGNED);
            unassigned.put("purchases", 0);
            unassigned.put("revenue", ZERO);
            unassigned.put("enrollCount", 0);
        }
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("coursePackCount", rows.size());
        summary.put("purchases", purchases);
        summary.put("revenue", sumMoney(rows, "revenue"));
        summary.put("enrollCount", enrollCount);
        summary.put("orderCount", sumInt(rows, "orderCount"));
        summary.put("totalOrderCount", totals.get("orderCount"));
        summary.put("totalRevenue", totals.get("revenue"));
        summary.put("unassigned", unassigned);
        return summary;
    }

    // ------------------------------------------------------------------
    // 老板视角 / 课程包销量：导出与参数小工具
    // ------------------------------------------------------------------

    /** 老板视角只吃期间（month 或 startDate/endDate），不继承教练/社区/小区筛选，保证合计与总览同源。 */
    private StatsQuery buildOwnerQuery(Period period) {
        StatsQuery query = new StatsQuery();
        query.setStartEpoch(period.startEpoch);
        query.setEndEpoch(period.endEpoch);
        return query;
    }

    /** 总览口径的期间合计（与 stats/overview 同一条 SQL），用于「行合计 == 总览」对账。 */
    private Map<String, Object> ownerTotals(StatsQuery query) {
        Map<String, Object> raw = statsQueryMapper.periodSummary(query);
        Map<String, Object> totals = new LinkedHashMap<String, Object>();
        totals.put("orderCount", asInt(raw.get("orderCount")));
        totals.put("revenue", round2(asDecimal(raw.get("revenue"))));
        return totals;
    }

    /** 教练维度期间与 #106 对齐：month 优先，其余用已换算的首尾日期，避免 #102 的默认月口径分叉。 */
    private ReportSearchValidate ownerReportSearch(Period period) {
        ReportSearchValidate report = new ReportSearchValidate();
        if (!trim(period.month).isEmpty()) {
            report.setMonth(period.month);
        }
        report.setStartDate(period.startDate);
        report.setEndDate(period.endDate);
        return report;
    }

    /** limit：默认 20，0=全部，其余钳制到 [1, 200]。 */
    private int resolveOwnerLimit(Integer value) {
        if (value == null || value.intValue() < 0) {
            return OWNER_DEFAULT_LIMIT;
        }
        if (value.intValue() == 0) {
            return 0;
        }
        return Math.min(value.intValue(), OWNER_MAX_LIMIT);
    }

    /** 排序字段白名单：非白名单值回落默认 revenue（只命中枚举，绝不拼 SQL）。 */
    private String resolveOwnerOrderBy(String value) {
        String key = trim(value);
        for (String item : OWNER_ORDER_KEYS) {
            if (item.equals(key)) {
                return item;
            }
        }
        return OWNER_ORDER_KEYS.get(0);
    }

    /** 排序：主键降序，并列看订单数降序，再并列看营业额降序，最后 id 升序（稳定且可断言）。 */
    private void sortOwnerRows(List<Map<String, Object>> rows, final String orderBy, final String idKey) {
        Collections.sort(rows, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> left, Map<String, Object> right) {
                int primary = compareOwnerKey(left, right, orderBy);
                if (primary != 0) {
                    return primary;
                }
                int orders = compareLong(asLong(right.get("orderCount")), asLong(left.get("orderCount")));
                if (orders != 0) {
                    return orders;
                }
                int revenue = asDecimal(right.get("revenue")).compareTo(asDecimal(left.get("revenue")));
                if (revenue != 0) {
                    return revenue;
                }
                return compareLong(asLong(left.get(idKey)), asLong(right.get(idKey)));
            }
        });
    }

    private int compareOwnerKey(Map<String, Object> left, Map<String, Object> right, String orderBy) {
        if ("orderCount".equals(orderBy)) {
            return compareLong(asLong(right.get("orderCount")), asLong(left.get("orderCount")));
        }
        if ("attends".equals(orderBy)) {
            return compareLong(asLong(right.get("attends")), asLong(left.get("attends")));
        }
        return asDecimal(right.get("revenue")).compareTo(asDecimal(left.get("revenue")));
    }

    private int compareLong(long left, long right) {
        return left < right ? -1 : (left == right ? 0 : 1);
    }

    private List<Map<String, Object>> truncateRows(List<Map<String, Object>> rows, int limit) {
        if (limit <= 0 || rows.size() <= limit) {
            return rows;
        }
        return new ArrayList<Map<String, Object>>(rows.subList(0, limit));
    }

    private Map<String, Object> ownerBucket(List<Map<String, Object>> rows, String idKey, int id) {
        for (Map<String, Object> row : rows) {
            if (asInt(row.get(idKey)) == id) {
                return row;
            }
        }
        return null;
    }

    private RegionBucket bucketOf(int regionId, Map<Integer, RegionBucket> buckets, RegionBucket unassigned) {
        RegionBucket bucket = buckets.get(regionId);
        return bucket == null ? unassigned : bucket;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> asRowList(Object value) {
        return value == null ? new ArrayList<Map<String, Object>>() : (List<Map<String, Object>>) value;
    }

    private int sumInt(List<Map<String, Object>> rows, String key) {
        int sum = 0;
        for (Map<String, Object> row : rows) {
            sum += asInt(row.get(key));
        }
        return sum;
    }

    private BigDecimal sumMoney(List<Map<String, Object>> rows, String key) {
        BigDecimal sum = ZERO;
        for (Map<String, Object> row : rows) {
            sum = sum.add(asDecimal(row.get(key)));
        }
        return round2(sum);
    }

    /** 老板视角/课程包统一导出：列表与导出行同源，导出默认全量（不受 limit 截断）。 */
    private Map<String, Object> writeOwnerExcel(String filePrefix, String sheetName, List<String> columns,
                                                List<String> keys, List<Map<String, Object>> rows,
                                                String caliber, Period period,
                                                StatsSearchValidate searchValidate) {
        int maxRows = exportMaxRows();
        if (rows.size() > maxRows) {
            throw new OperateException("导出数据 " + rows.size() + " 行，超过单次导出上限 " + maxRows
                    + " 行，请缩小筛选范围后重试");
        }
        String requested = sanitizeFileName(trim(searchValidate.getFile_name()));
        String fileName = requested.isEmpty() ? filePrefix + "-"
                + period.startDate.replace("-", "") + "-" + period.endDate.replace("-", "") : requested;
        String folderPath = "/excel/export/" + TimeUtils.timestampToDay(System.currentTimeMillis() / 1000L) + "/";
        String path = folderPath + fileName + "-" + System.currentTimeMillis() + ".xlsx";
        String uploadDirectory = YmlUtils.get("like.upload-directory");
        File folder = new File(uploadDirectory + folderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new OperateException("创建导出目录失败");
        }
        File target = new File(uploadDirectory + path);
        List<List<String>> head = new ArrayList<List<String>>();
        for (String title : columns) {
            head.add(Collections.singletonList(title));
        }
        try {
            ExcelWriter writer = EasyExcel.write(target).head(head).excelType(ExcelTypeEnum.XLSX).build();
            WriteSheet sheet = EasyExcel.writerSheet(sheetName).build();
            List<List<Object>> data = new ArrayList<List<Object>>();
            for (Map<String, Object> row : rows) {
                List<Object> line = new ArrayList<Object>();
                for (String key : keys) {
                    line.add(row.get(key));
                }
                data.add(line);
            }
            writer.write(data, sheet);
            writer.finish();
        } catch (RuntimeException error) {
            target.delete();
            throw error;
        }
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("url", UrlUtils.toAdminAbsoluteUrl(path));
        result.put("fileName", fileName + ".xlsx");
        result.put("rowCount", rows.size());
        result.put("periodStart", period.startDate);
        result.put("periodEnd", period.endDate);
        result.put("caliber", caliber);
        return result;
    }

    /** 片区聚合桶（片区行 / 未归属片区行）。 */
    private static class RegionBucket {
        private final int regionId;
        private final String regionName;
        private final String cityName;
        private int communityCount;
        private int orderCount;
        private BigDecimal revenue = ZERO;

        private RegionBucket(int regionId, String regionName, String cityName) {
            this.regionId = regionId;
            this.regionName = regionName;
            this.cityName = cityName;
        }

        private Map<String, Object> toRow() {
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("regionId", regionId);
            row.put("regionName", regionId == 0 ? UNASSIGNED : regionName);
            row.put("cityName", cityName);
            row.put("communityCount", communityCount);
            row.put("orderCount", orderCount);
            row.put("revenue", revenue.setScale(2, RoundingMode.HALF_UP));
            return row;
        }
    }

    private int exportMaxRows() {
        String value = YmlUtils.get("like.report.export-max-rows");
        if (value == null || value.trim().isEmpty()) {
            return DEFAULT_EXPORT_MAX_ROWS;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : DEFAULT_EXPORT_MAX_ROWS;
        } catch (NumberFormatException error) {
            return DEFAULT_EXPORT_MAX_ROWS;
        }
    }

    private String sanitizeFileName(String value) {
        if (value == null) {
            return "";
        }
        String name = value.replaceAll("[\\\\/:*?\"<>|\\s]+", "-");
        name = name.replaceAll("^-+|-+$", "");
        return name.length() > 80 ? name.substring(0, 80) : name;
    }

    // ------------------------------------------------------------------
    // 三级：订单 / 课节明细（维度下钻公共层）
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> orderList(PageValidate pageValidate,
                                                     StatsSearchValidate searchValidate) {
        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        Period period = resolvePeriod(searchValidate, false);
        StatsQuery query = buildQuery(searchValidate, period, false);
        long total = statsQueryMapper.countOrders(query);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (total > 0) {
            query.setOffset((int) ((long) (pageNo - 1) * pageSize));
            query.setLimit(pageSize);
            for (Map<String, Object> raw : statsQueryMapper.pageOrders(query)) {
                int communityId = asInt(raw.get("communityId"));
                int areaId = asInt(raw.get("areaId"));
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                row.put("orderId", asInt(raw.get("orderId")));
                row.put("sn", safe((String) raw.get("sn")));
                row.put("courseClassId", asInt(raw.get("courseClassId")));
                row.put("courseName", safe((String) raw.get("courseName")));
                row.put("instructorId", asInt(raw.get("instructorId")));
                row.put("instructorName", safe((String) raw.get("instructorName")));
                row.put("communityId", communityId);
                row.put("communityName", communityId == 0
                        ? UNASSIGNED : safe((String) raw.get("communityName")));
                row.put("areaId", areaId);
                row.put("areaName", areaId == 0 ? UNASSIGNED : safe((String) raw.get("areaName")));
                row.put("studentId", asInt(raw.get("studentId")));
                row.put("studentName", safe((String) raw.get("studentName")));
                row.put("lessonCount", asInt(raw.get("lessonCount")));
                row.put("orderAmount", round2(asDecimal(raw.get("orderAmount"))));
                row.put("payTime", formatTime(asLong(raw.get("payTime"))));
                rows.add(row);
            }
        }
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("periodStart", period.startDate);
        extend.put("periodEnd", period.endDate);
        extend.put("caliber", CALIBER);
        return PageResult.iPageHandle(total, (long) pageNo, (long) pageSize, rows, extend);
    }

    @Override
    public PageResult<Map<String, Object>> lessonList(PageValidate pageValidate,
                                                      StatsSearchValidate searchValidate) {        int pageNo = pageNo(pageValidate);
        int pageSize = pageSize(pageValidate);
        Period period = resolvePeriod(searchValidate, false);
        StatsQuery query = buildQuery(searchValidate, period, false);
        long total = statsQueryMapper.countLessons(query);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (total > 0) {
            query.setOffset((int) ((long) (pageNo - 1) * pageSize));
            query.setLimit(pageSize);
            for (Map<String, Object> raw : statsQueryMapper.pageLessons(query)) {
                int communityId = asInt(raw.get("communityId"));
                int areaId = asInt(raw.get("areaId"));
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                row.put("courseClassId", asInt(raw.get("courseClassId")));
                row.put("courseName", safe((String) raw.get("courseName")));
                row.put("instructorId", asInt(raw.get("instructorId")));
                row.put("instructorName", safe((String) raw.get("instructorName")));
                row.put("communityId", communityId);
                row.put("communityName", communityId == 0
                        ? UNASSIGNED : safe((String) raw.get("communityName")));
                row.put("areaId", areaId);
                row.put("areaName", areaId == 0 ? UNASSIGNED : safe((String) raw.get("areaName")));
                row.put("lessonNo", asInt(raw.get("lessonNo")));
                row.put("servicePersons", asInt(raw.get("servicePersons")));
                row.put("lastConsumeTime", formatTime(asLong(raw.get("lastConsumeTime"))));
                rows.add(row);
            }
        }
        Map<String, Object> extend = new LinkedHashMap<String, Object>();
        extend.put("periodStart", period.startDate);
        extend.put("periodEnd", period.endDate);
        extend.put("caliber", CALIBER);
        return PageResult.iPageHandle(total, (long) pageNo, (long) pageSize, rows, extend);
    }

    // ------------------------------------------------------------------
    // 参数换算
    // ------------------------------------------------------------------

    /** 期间：month &gt; startDate/endDate &gt; 默认（趋势=近 30 天，其余=本月）。 */
    private Period resolvePeriod(StatsSearchValidate search, boolean trendDefault) {
        Period period = new Period();
        String monthText = trim(search == null ? null : search.getMonth());
        String startText = trim(search == null ? null : search.getStartDate());
        String endText = trim(search == null ? null : search.getEndDate());
        LocalDate start;
        LocalDate end;
        if (!monthText.isEmpty()) {
            YearMonth month;
            try {
                month = YearMonth.parse(monthText);
            } catch (DateTimeParseException error) {
                throw new OperateException("月份格式应为 yyyy-MM");
            }
            start = month.atDay(1);
            end = month.atEndOfMonth();
            period.month = month.toString();
            period.previousStart = start.minusMonths(1);
            period.previousEnd = end.minusMonths(1);
        } else if (!startText.isEmpty() || !endText.isEmpty()) {
            start = startText.isEmpty() ? LocalDate.now().withDayOfMonth(1) : parseDate(startText, "开始日期");
            end = endText.isEmpty() ? LocalDate.now() : parseDate(endText, "结束日期");
            if (start.isAfter(end)) {
                throw new OperateException("开始日期不能晚于结束日期");
            }
            long days = ChronoUnit.DAYS.between(start, end) + 1;
            period.previousEnd = start.minusDays(1);
            period.previousStart = period.previousEnd.minusDays(days - 1);
        } else if (trendDefault) {
            end = LocalDate.now();
            start = end.minusDays(TREND_DEFAULT_DAYS - 1L);
            long days = TREND_DEFAULT_DAYS;
            period.previousEnd = start.minusDays(1);
            period.previousStart = period.previousEnd.minusDays(days - 1);
        } else {
            start = LocalDate.now().withDayOfMonth(1);
            end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            period.month = YearMonth.from(start).toString();
            period.previousStart = start.minusMonths(1);
            period.previousEnd = end.minusMonths(1);
        }
        period.startDate = start.toString();
        period.endDate = end.toString();
        period.startEpoch = start.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        period.endEpoch = end.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toEpochSecond();
        period.previousStartDate = period.previousStart.toString();
        period.previousEndDate = period.previousEnd.toString();
        period.previousStartEpoch = period.previousStart.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        period.previousEndEpoch = period.previousEnd.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toEpochSecond();
        return period;
    }

    private StatsQuery buildQuery(StatsSearchValidate search, Period period, boolean withPrevious) {
        StatsQuery query = new StatsQuery();
        query.setStartEpoch(period.startEpoch);
        query.setEndEpoch(period.endEpoch);
        if (withPrevious) {
            query.setPreviousStartEpoch(period.previousStartEpoch);
            query.setPreviousEndEpoch(period.previousEndEpoch);
        }
        query.setInstructorId(positive(search.getInstructorId()));
        query.setCommunityId(nonNegative(search.getCommunityId()));
        query.setAreaId(nonNegative(search.getAreaId()));
        query.setCourseClassId(positive(search.getCourseClassId()));
        String keyword = trim(search.getKeyword()).toLowerCase();
        if (!keyword.isEmpty()) {
            String escaped = keyword.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            query.setKeyword("%" + escaped + "%");
        }
        return query;
    }

    /** 教练维度参数转 #102 的报表参数（字段名沿用 coachId，避免第二份查询对象）。 */
    private ReportSearchValidate toReportSearch(StatsSearchValidate search, boolean forDetail) {
        ReportSearchValidate report = new ReportSearchValidate();
        report.setMonth(trim(search.getMonth()).isEmpty() ? null : trim(search.getMonth()));
        report.setStartDate(search.getStartDate());
        report.setEndDate(search.getEndDate());
        report.setKeyword(search.getKeyword());
        report.setFile_name(search.getFile_name());
        Integer coachId = search.getCoachId() != null ? search.getCoachId() : search.getInstructorId();
        if (forDetail || coachId != null) {
            report.setCoachId(coachId);
        }
        return report;
    }

    private boolean shouldCompare(StatsSearchValidate search) {
        return search.getCompare() == null || search.getCompare() != 0;
    }

    private LocalDate parseDate(String value, String field) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException error) {
            throw new OperateException(field + "格式应为 yyyy-MM-dd");
        }
    }

    // ------------------------------------------------------------------
    // 小工具
    // ------------------------------------------------------------------

    private int pageNo(PageValidate pageValidate) {
        int pageNo = pageValidate == null || pageValidate.getPage_no() == null ? 1 : pageValidate.getPage_no();
        return Math.max(1, pageNo);
    }

    private int pageSize(PageValidate pageValidate) {
        int pageSize = pageValidate == null || pageValidate.getPage_size() == null ? 20 : pageValidate.getPage_size();
        return Math.min(200, Math.max(1, pageSize));
    }

    private Integer positive(Integer value) {
        return value == null || value <= 0 ? null : value;
    }

    private Integer nonNegative(Integer value) {
        return value == null || value < 0 ? null : value;
    }

    private BigDecimal round2(BigDecimal value) {
        return (value == null ? ZERO : value).setScale(2, RoundingMode.HALF_UP);
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

    private long asLong(Object value) {
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

    private String formatTime(Long timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return TimeUtils.timestampToDate(timestamp);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    /** 统计期间（当前 + 上期），对外输出为 period map。 */
    private static class Period {
        private String month = "";
        private String startDate;
        private String endDate;
        private long startEpoch;
        private long endEpoch;
        private LocalDate previousStart;
        private LocalDate previousEnd;
        private String previousStartDate;
        private String previousEndDate;
        private long previousStartEpoch;
        private long previousEndEpoch;

        private Map<String, Object> toMap() {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("month", month);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("previousStartDate", previousStartDate);
            map.put("previousEndDate", previousEndDate);
            return map;
        }
    }
}
