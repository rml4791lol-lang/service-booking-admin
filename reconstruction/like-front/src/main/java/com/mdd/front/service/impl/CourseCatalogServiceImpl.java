package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseReview;
import com.mdd.common.entity.course.SportsCategory;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.instructor.InstructorCert;
import com.mdd.common.entity.region.Region;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseReviewMapper;
import com.mdd.common.mapper.course.SportsCategoryMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.instructor.InstructorCertMapper;
import com.mdd.common.mapper.region.RegionMapper;
import com.mdd.common.service.CouponCheckinService;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.HtmlSanitizer;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.ICourseCatalogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 课程与教练公开只读目录实现（#22）。
 *
 * <p>口径：</p>
 * <ul>
 *   <li>“上架课程”= {@code la_course_class.status IN (1,2) AND is_delete=0}，与地区页一致。</li>
 *   <li>“可约场次”= {@code ys_course_lesson.status=0}（与 bookLesson 一致）且
 *       {@code lesson_date >= 今天}；余位 = 班级 max_students - 有效预约数
 *       （{@code la_course_booking.status <> 3}）。</li>
 *   <li>“报名截止”= 场次当天 23:59。现有 bookLesson 只校验场次 status=0，不校验日期/截止，
 *       因此不额外收紧，避免接口展示“已截止”但下单仍能成功。</li>
 * </ul>
 *
 * <p>查询策略：列表按表各查一次后内存组装（班级表当前仅几十行）；
 * 场次列表按 lesson_id 一次 GROUP BY 统计预约数，不逐场次查询。</p>
 */
@Service
public class CourseCatalogServiceImpl implements ICourseCatalogService {

    /** 已上架班级状态：1=进行中，2=预报名。 */
    private static final Integer[] LIVE_CLASS_STATUS = new Integer[]{1, 2};

    private static final int DEFAULT_DURATION = 60;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 60;
    private static final int DEFAULT_QUICK_LIMIT = 6;
    private static final int MAX_QUICK_LIMIT = 30;
    private static final int MAX_DETAIL_SESSIONS = 12;

    private static final String[] WEEK_CN = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    CourseReviewMapper courseReviewMapper;
    @Resource
    SportsCategoryMapper sportsCategoryMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    RegionMapper regionMapper;
    @Resource
    CommunityMapper communityMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    InstructorCertMapper instructorCertMapper;
    @Resource
    CouponCheckinService couponCheckinService;

    // ------------------------------------------------------------------
    // 课程
    // ------------------------------------------------------------------

    @Override
    public List<Map<String, Object>> courses(Integer areaId, Integer communityId, Integer streetId, Integer districtId,
                                             Integer categoryId, Integer age, String keyword,
                                             Integer pageNo, Integer pageSize, String lat, String lng) {
        List<CourseClass> all = liveClasses();
        CatalogContext ctx = buildContext(all);
        String kw = keyword == null ? "" : keyword.trim();

        List<Map<String, Object>> items = new ArrayList<>();
        for (CourseClass courseClass : all) {
            RegionPath path = pathOf(courseClass, ctx);
            if (areaId != null && areaId > 0 && !areaId.equals(courseClass.getAreaId())) {
                continue;
            }
            // 课程归属社区：后台班级编辑里显式设置的 community_id 优先，未设置才回退到
            // 课程绑定小区(area)所属社区。甲方在后台把课程绑定到新社区后，若仍按 area
            // 口径过滤会出现「绑了社区却不生效」（2026-11-05 现场反馈）。
            Integer courseCommunity = courseClass.getCommunityId() != null && courseClass.getCommunityId() > 0
                    ? courseClass.getCommunityId() : path.communityId;
            if (communityId != null && communityId > 0 && !communityId.equals(courseCommunity)) {
                continue;
            }
            if (streetId != null && streetId > 0 && !streetId.equals(path.streetId)) {
                continue;
            }
            if (districtId != null && districtId > 0 && !districtId.equals(path.districtId)) {
                continue;
            }
            if (categoryId != null && categoryId > 0 && !categoryId.equals(courseClass.getCategoryId())) {
                continue;
            }
            if (age != null && age > 0 && !ageMatches(courseClass, age)) {
                continue;
            }
            if (!kw.isEmpty() && !keywordMatches(courseClass, ctx, kw)) {
                continue;
            }
            Map<String, Object> item = courseItem(courseClass, ctx, lat, lng);
            item.put("_distMeters", distanceMeters(lat, lng, courseClass.getLatitude(), courseClass.getLongitude()));
            items.add(item);
        }

        sortByDistance(items);
        return paginate(items, pageNo, pageSize);
    }

    @Override
    public Map<String, Object> courseDetail(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        CourseClass courseClass = courseClassMapper.selectOne(new QueryWrapper<CourseClass>()
                .eq("id", id)
                .eq("is_delete", 0)
                .last("limit 1"));
        // 下架课程不在详情露出（#44），与后台「下架后小程序列表/详情均不可见」口径一致。
        if (courseClass == null || courseClass.getIsShow() == null || courseClass.getIsShow() != 1) {
            return null;
        }

        List<CourseClass> all = liveClasses();
        if (!containsClass(all, id)) {
            all.add(courseClass);
        }
        CatalogContext ctx = buildContext(all);
        Map<String, Object> item = courseItem(courseClass, ctx, null, null);

        List<CourseLesson> lessons = futureLessons(Collections.singletonList(courseClass));
        Map<Integer, CourseClass> detailClassMap = new HashMap<>();
        detailClassMap.put(courseClass.getId(), courseClass);
        List<Map<String, Object>> sessionItems = sessionItems(lessons, detailClassMap, ctx, false);
        item.put("sessionCount", sessionItems.size());
        if (sessionItems.size() > MAX_DETAIL_SESSIONS) {
            sessionItems = new ArrayList<>(sessionItems.subList(0, MAX_DETAIL_SESSIONS));
        }
        item.put("sessions", sessionItems);

        // 教练：班级授课教练 + 场次教练（代课/临时加课）去重。
        Set<Integer> coachIds = new LinkedHashSet<>();
        if (courseClass.getInstructorId() != null && courseClass.getInstructorId() > 0) {
            coachIds.add(courseClass.getInstructorId());
        }
        for (CourseLesson lesson : lessons) {
            if (lesson.getCoachId() != null && lesson.getCoachId() > 0) {
                coachIds.add(lesson.getCoachId());
            }
        }
        ensureInstructors(ctx, coachIds);
        List<Map<String, Object>> coachList = new ArrayList<>();
        List<String> coachNames = new ArrayList<>();
        for (Integer coachId : coachIds) {
            Instructor instructor = ctx.instructors.get(coachId);
            if (instructor == null) {
                continue;
            }
            coachList.add(coachItem(instructor, ctx));
            coachNames.add(instructor.getName());
        }
        List<String> coachIdTexts = new ArrayList<>();
        for (Integer coachId : coachIds) {
            coachIdTexts.add(String.valueOf(coachId));
        }
        item.put("coachIds", coachIdTexts);
        item.put("coachNames", String.join("、", coachNames));
        item.put("coachList", coachList);
        if (!coachList.isEmpty()) {
            item.put("coach", coachList.get(0));
        }

        // 同小区相关课程（与 mock 的 getCourse.related 一致）。
        List<Map<String, Object>> related = new ArrayList<>();
        if (courseClass.getAreaId() != null && courseClass.getAreaId() > 0) {
            for (CourseClass other : all) {
                if (other.getId().equals(courseClass.getId())) {
                    continue;
                }
                if (!courseClass.getAreaId().equals(other.getAreaId())) {
                    continue;
                }
                related.add(courseItem(other, ctx, null, null));
            }
        }
        item.put("related", related);

        // 课程说明类字段：介绍由后台「课程介绍」页签维护（#44），其余保持 mock 契约的空数组/空串。
        item.put("summary", nvlText(courseClass.getSummary()));
        item.put("introHtml", HtmlSanitizer.sanitizeCourseIntro(courseClass.getIntroHtml()));
        item.put("introImages", absoluteImageList(courseClass.getIntroImages()));
        item.put("highlight", Collections.emptyList());
        item.put("syllabus", Collections.emptyList());
        item.put("notice", "");
        item.put("totalLessons", courseClass.getTotalLessons());
        item.put("startDate", timestampToDate(courseClass.getStartDate()));
        item.put("endDate", timestampToDate(courseClass.getEndDate()));
        item.put("status", courseClass.getStatus());
        return item;
    }

    @Override
    public List<Map<String, Object>> sessions(Integer courseId) {
        if (courseId == null || courseId <= 0) {
            return Collections.emptyList();
        }
        CourseClass courseClass = courseClassMapper.selectById(courseId);
        if (courseClass == null || (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1)
                || courseClass.getIsShow() == null || courseClass.getIsShow() != 1) {
            return Collections.emptyList();
        }
        List<CourseLesson> lessons = courseLessonMapper.selectList(new QueryWrapper<CourseLesson>()
                .eq("class_id", courseId)
                .orderByAsc("lesson_date")
                .orderByAsc("time_slot")
                .orderByAsc("id"));
        CatalogContext ctx = buildContext(Collections.singletonList(courseClass));
        Map<Integer, CourseClass> classMap = new HashMap<>();
        classMap.put(courseClass.getId(), courseClass);
        return sessionItems(lessons, classMap, ctx, false);
    }

    @Override
    public List<Map<String, Object>> quickSessions(Integer limit) {
        int size = limit == null || limit <= 0 ? DEFAULT_QUICK_LIMIT : Math.min(limit, MAX_QUICK_LIMIT);
        String today = LocalDate.now().toString();
        // 先取一批候选场次（可约场次占比高，200 条足够覆盖 limit），再过滤满员场次。
        List<CourseLesson> candidates = courseLessonMapper.selectList(new QueryWrapper<CourseLesson>()
                .eq("status", 0)
                .ge("lesson_date", today)
                .orderByAsc("lesson_date")
                .orderByAsc("time_slot")
                .orderByAsc("id")
                .last("limit 200"));
        if (candidates.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Integer> classIds = new LinkedHashSet<>();
        for (CourseLesson lesson : candidates) {
            classIds.add(lesson.getClassId());
        }
        List<CourseClass> classes = liveClassesByIds(classIds);
        Map<Integer, CourseClass> classMap = classMap(classes);
        CatalogContext ctx = buildContext(classes);

        List<Map<String, Object>> items = new ArrayList<>();
        for (Map<String, Object> item : sessionItems(candidates, classMap, ctx, false)) {
            Object remaining = item.get("remaining");
            if (remaining instanceof Number && ((Number) remaining).intValue() > 0) {
                items.add(item);
            }
            if (items.size() >= size) {
                break;
            }
        }
        return items;
    }

    @Override
    public Map<String, Object> session(Integer lessonId) {
        if (lessonId == null || lessonId <= 0) {
            return null;
        }
        CourseLesson lesson = courseLessonMapper.selectById(lessonId);
        if (lesson == null) {
            return null;
        }
        CourseClass courseClass = courseClassMapper.selectById(lesson.getClassId());
        // \u4e0e\u8bfe\u7a0b\u5217\u8868/\u8be6\u60c5\u53e3\u5f84\u4e00\u81f4\uff1a\u5df2\u5220\u9664\u6216\u5df2\u4e0b\u67b6\u7684\u8bfe\u7a0b\u4e0d\u5728\u5c0f\u7a0b\u5e8f\u9732\u51fa\u573a\u6b21\u660e\u7ec6\u3002
        if (courseClass == null || (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1)
                || courseClass.getIsShow() == null || courseClass.getIsShow() != 1) {
            return null;
        }
        CatalogContext ctx = buildContext(Collections.singletonList(courseClass));
        Map<Integer, CourseClass> classMap = new HashMap<>();
        classMap.put(courseClass.getId(), courseClass);
        List<Map<String, Object>> items = sessionItems(Collections.singletonList(lesson), classMap, ctx, false);
        return items.isEmpty() ? null : items.get(0);
    }

    // ------------------------------------------------------------------
    // 教练
    // ------------------------------------------------------------------

    @Override
    public List<Map<String, Object>> coaches(Integer areaId, Integer categoryId, String keyword) {
        List<CourseClass> all = liveClasses();
        CatalogContext ctx = buildContext(all);

        // \u6559\u7ec3 status\uff1a0=\u5df2\u505c\u7528\uff0c1=\u6b63\u5e38\uff0c2=\u5f85\u5ba1\u6838\uff1b\u516c\u5f00\u5217\u8868\u53ea\u5c55\u793a\u5ba1\u6838\u901a\u8fc7\u7684\u6559\u7ec3\u3002
        List<Instructor> instructors = instructorMapper.selectList(new QueryWrapper<Instructor>()
                .isNull("delete_time")
                .eq("status", (Object) 1)
                .orderByAsc("sort")
                .orderByAsc("id"));
        String kw = keyword == null ? "" : keyword.trim();

        List<Map<String, Object>> items = new ArrayList<>();
        for (Instructor instructor : instructors) {
            if (areaId != null && areaId > 0 && !instructorAreas(instructor.getId(), ctx).contains(areaId)) {
                continue;
            }
            if (categoryId != null && categoryId > 0 && !instructorCategoryIds(instructor.getId(), ctx).contains(categoryId)) {
                continue;
            }
            if (!kw.isEmpty() && !coachKeywordMatches(instructor, ctx, kw)) {
                continue;
            }
            items.add(coachItem(instructor, ctx));
        }
        return items;
    }

    @Override
    public Map<String, Object> coachDetail(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        Instructor instructor = instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("id", id)
                .isNull("delete_time")
                .eq("status", (Object) 1)
                .last("limit 1"));
        if (instructor == null) {
            return null;
        }

        List<CourseClass> all = liveClasses();
        List<CourseClass> own = new ArrayList<>();
        for (CourseClass courseClass : all) {
            if (id.equals(courseClass.getInstructorId())) {
                own.add(courseClass);
            }
        }
        // 场次教练（代课）也计入教练可授课程。
        Set<Integer> taughtClassIds = new HashSet<>();
        List<Map<String, Object>> rows = courseLessonMapper.selectMaps(new QueryWrapper<CourseLesson>()
                .select("DISTINCT class_id")
                .eq("coach_id", id)
                .eq("status", 0));
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                Integer classId = intValue(row, "class_id", "classId");
                if (classId != null && classId > 0) {
                    taughtClassIds.add(classId);
                }
            }
        }
        for (CourseClass courseClass : all) {
            if (taughtClassIds.contains(courseClass.getId()) && !containsClass(own, courseClass.getId())) {
                own.add(courseClass);
            }
        }

        CatalogContext ctx = buildContext(own.isEmpty() ? all : own);
        ensureInstructors(ctx, Collections.singleton(id));
        ensureCertificates(ctx, Collections.singleton(id));
        Map<String, Object> item = coachItem(instructor, ctx);
        List<Map<String, Object>> courses = new ArrayList<>();
        for (CourseClass courseClass : own) {
            courses.add(courseItem(courseClass, ctx, null, null));
        }
        item.put("courses", courses);
        return item;
    }

    // ------------------------------------------------------------------
    // 组装：课程
    // ------------------------------------------------------------------

    private Map<String, Object> courseItem(CourseClass courseClass, CatalogContext ctx, String lat, String lng) {
        RegionPath path = pathOf(courseClass, ctx);
        Instructor instructor = ctx.instructors.get(courseClass.getInstructorId());
        String categoryName = ctx.categoryNames.getOrDefault(courseClass.getCategoryId(), "");

        BigDecimal feePerLesson = nvl(courseClass.getFeePerLesson());
        BigDecimal totalFee = nvl(courseClass.getTotalFee());
        BigDecimal discount = nvl(courseClass.getDiscountPrice());
        // 列表主价格展示「整期总费用」（甲方 2026-11-05 明确口径）：total_fee 优先，
        // 未填时才回退单节课时费。此前优先 feePerLesson，小程序卡片显示「¥60/次」
        // 与后台「总费用 49」不一致。
        BigDecimal price = totalFee.signum() > 0 ? totalFee : feePerLesson;
        BigDecimal originalPrice = feePerLesson.compareTo(price) > 0 ? feePerLesson : price;
        BigDecimal rating = ratingOf(courseClass.getId(), instructor, ctx);
        long sold = ctx.soldByClass.getOrDefault(courseClass.getId(), 0);
        int duration = durationOf(courseClass);
        String level = StringUtils.isNotBlank(courseClass.getLevel())
                ? courseClass.getLevel()
                : categoryName;

        Map<String, Object> item = new LinkedHashMap<>();
        // 课程 ID 跟随 mock 返回字符串；classId 保留数字供旧调用方使用。
        item.put("id", String.valueOf(courseClass.getId()));
        item.put("classId", courseClass.getId());
        // 与券适用/核销共用同一班型判定，避免私教课在目录里继续被标成 group。
        // 兼容说明：既有 trial/group 调用方不变；仅原先误标为 group 的私教课新增 private。
        item.put("courseType", couponCheckinService.courseType(courseClass));
        item.put("title", courseClass.getName());
        // 副标题优先用后台维护的课程简介（#44），未维护时回退场地名，保持原 mock 观感。
        item.put("summary", nvlText(courseClass.getSummary()));
        item.put("subtitle", StringUtils.isNotBlank(courseClass.getSummary())
                ? courseClass.getSummary().trim()
                : nvlText(courseClass.getVenue()));
        item.put("cover", UrlUtils.toAbsoluteUrl(courseClass.getImage()));
        item.put("coachIds", instructor == null ? Collections.emptyList()
                : Collections.singletonList(String.valueOf(instructor.getId())));
        item.put("coachNames", instructor != null ? nvlText(instructor.getName())
                : nvlText(courseClass.getInstructorName()));
        item.put("coach", instructor == null ? null : coachItem(instructor, ctx));
        item.put("ageMin", courseClass.getAgeMin());
        item.put("ageMax", courseClass.getAgeMax());
        item.put("ageText", ageText(courseClass));
        item.put("level", level);
        item.put("categoryId", courseClass.getCategoryId());
        item.put("categoryName", categoryName);
        item.put("tags", tagsOf(courseClass, categoryName));
        item.put("unit", "次");
        item.put("price", price);
        item.put("originalPrice", originalPrice);
        item.put("priceMin", price);
        item.put("priceMax", originalPrice);
        item.put("priceRange", price.compareTo(originalPrice) == 0
                ? "¥" + moneyText(price)
                : "¥" + moneyText(price) + "-¥" + moneyText(originalPrice));
        item.put("feePerLesson", feePerLesson);
        item.put("totalFee", totalFee);
        item.put("discountPrice", discount);
        item.put("totalLessons", courseClass.getTotalLessons());
        item.put("capacity", courseClass.getMaxStudents());
        item.put("duration", duration);
        item.put("rating", rating);
        item.put("sold", sold);
        item.put("highlight", Collections.emptyList());
        item.put("syllabus", Collections.emptyList());
        item.put("notice", "");
        item.put("areaId", idText(courseClass.getAreaId()));
        item.put("areaName", path.areaName);
        // 展示口径与过滤口径一致：课程显式 community_id 优先（绑定新社区后立即生效）
        Integer itemCommunity = courseClass.getCommunityId() != null && courseClass.getCommunityId() > 0
                ? courseClass.getCommunityId() : path.communityId;
        com.mdd.common.entity.community.Community itemCommunityObj = ctx.communities.get(itemCommunity);
        item.put("communityId", idText(itemCommunity));
        item.put("communityName", itemCommunityObj != null ? itemCommunityObj.getName() : path.communityName);
        item.put("streetId", idText(path.streetId));
        item.put("streetName", path.streetName);
        item.put("districtId", idText(path.districtId));
        item.put("district", path.districtName);
        item.put("districtName", path.districtName);
        item.put("venue", courseClass.getVenue());
        item.put("lat", courseClass.getLatitude());
        item.put("lng", courseClass.getLongitude());
        item.put("classType", courseClass.getClassType());
        item.put("weekDays", courseClass.getWeekDays());
        item.put("timeSlot", courseClass.getTimeSlot());
        item.put("scheduleMode", courseClass.getScheduleMode() == null ? 0 : courseClass.getScheduleMode());
        item.put("scheduleRules", courseClass.getScheduleRules());
        item.put("status", courseClass.getStatus());
        item.put("startDate", timestampToDate(courseClass.getStartDate()));
        // 旧接口字段（兼容老调用方）
        item.put("name", courseClass.getName());
        item.put("instructor", courseClass.getInstructorName());
        item.put("time", courseClass.getTimeSlot());
        item.put("image", UrlUtils.toAbsoluteUrl(courseClass.getImage()));
        double distanceMeters = distanceMeters(lat, lng, courseClass.getLatitude(), courseClass.getLongitude());
        item.put("distance", distanceText(distanceMeters));
        return item;
    }

    private List<String> tagsOf(CourseClass courseClass, String categoryName) {
        List<String> tags = new ArrayList<>();
        if (StringUtils.isNotBlank(categoryName)) {
            tags.add(categoryName);
        }
        if (courseClass.getClassType() != null && courseClass.getClassType() == 1) {
            tags.add("体验班");
        } else {
            tags.add("常规班");
        }
        Integer capacity = courseClass.getMaxStudents();
        if (capacity != null && capacity > 0) {
            tags.add(capacity <= 10 ? capacity + "人小班" : capacity + "人班");
        }
        return tags;
    }

    private String ageText(CourseClass courseClass) {
        if (courseClass.getAgeMin() == null || courseClass.getAgeMax() == null) {
            return "";
        }
        return courseClass.getAgeMin() + "-" + courseClass.getAgeMax() + "岁";
    }

    private boolean ageMatches(CourseClass courseClass, int age) {
        // 未维护年龄段的课程不做年龄过滤，避免存量数据在筛选时全部消失；
        // 只维护了单边时按已维护的一边过滤。
        if (courseClass.getAgeMin() == null && courseClass.getAgeMax() == null) {
            return true;
        }
        if (courseClass.getAgeMin() != null && age < courseClass.getAgeMin()) {
            return false;
        }
        return courseClass.getAgeMax() == null || age <= courseClass.getAgeMax();
    }

    private boolean keywordMatches(CourseClass courseClass, CatalogContext ctx, String keyword) {
        if (contains(courseClass.getName(), keyword) || contains(courseClass.getVenue(), keyword)
                || contains(courseClass.getInstructorName(), keyword)) {
            return true;
        }
        String categoryName = ctx.categoryNames.getOrDefault(courseClass.getCategoryId(), "");
        return contains(categoryName, keyword);
    }

    // ------------------------------------------------------------------
    // 组装：场次
    // ------------------------------------------------------------------

    /**
     * @param onlyBookable true 时只返回今天及以后、status=0 的场次（课程详情用）
     */
    private List<Map<String, Object>> sessionItems(List<CourseLesson> lessons, Map<Integer, CourseClass> classMap,
                                                   CatalogContext ctx, boolean onlyBookable) {
        if (lessons == null || lessons.isEmpty()) {
            return Collections.emptyList();
        }
        String today = LocalDate.now().toString();

        List<Integer> lessonIds = new ArrayList<>();
        for (CourseLesson lesson : lessons) {
            lessonIds.add(lesson.getId());
        }
        Map<Integer, Integer> bookedCounts = bookingCounts(lessonIds);

        Set<Integer> coachIds = new LinkedHashSet<>();
        for (CourseLesson lesson : lessons) {
            CourseClass courseClass = classMap.get(lesson.getClassId());
            if (lesson.getCoachId() != null && lesson.getCoachId() > 0) {
                coachIds.add(lesson.getCoachId());
            } else if (courseClass != null && courseClass.getInstructorId() != null && courseClass.getInstructorId() > 0) {
                coachIds.add(courseClass.getInstructorId());
            }
        }
        ensureInstructors(ctx, coachIds);

        List<Map<String, Object>> items = new ArrayList<>();
        for (CourseLesson lesson : lessons) {
            CourseClass courseClass = classMap.get(lesson.getClassId());
            if (courseClass == null) {
                continue;
            }
            boolean open = lesson.getStatus() != null && lesson.getStatus() == 0;
            if (onlyBookable && (!open || lesson.getLessonDate() == null || lesson.getLessonDate().compareTo(today) < 0)) {
                continue;
            }

            int capacity = courseClass.getMaxStudents() == null ? 0 : courseClass.getMaxStudents();
            int booked = bookedCounts.getOrDefault(lesson.getId(), 0);
            int remaining = Math.max(capacity - booked, 0);
            Integer coachId = lesson.getCoachId() != null && lesson.getCoachId() > 0
                    ? lesson.getCoachId()
                    : courseClass.getInstructorId();
            Instructor instructor = coachId == null ? null : ctx.instructors.get(coachId);
            RegionPath path = pathOf(courseClass, ctx);
            String time = ClassScheduleUtil.resolveLessonTimeSlot(lesson, courseClass);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", String.valueOf(lesson.getId()));
            item.put("courseId", String.valueOf(courseClass.getId()));
            item.put("areaId", idText(courseClass.getAreaId()));
            item.put("coachId", idText(coachId));
            item.put("date", lesson.getLessonDate());
            item.put("time", time);
            item.put("capacity", capacity);
            item.put("booked", booked);
            // 报名截止与 bookLesson 一致：场次当天 23:59 前均可报名。
            item.put("deadline", lesson.getLessonDate());
            item.put("deadlineAt", nvlText(lesson.getLessonDate()) + " 23:59");
            item.put("status", lessonStatusText(lesson.getStatus()));
            item.put("bookable", open && lesson.getLessonDate() != null && lesson.getLessonDate().compareTo(today) >= 0
                    && remaining > 0);
            item.put("courseTitle", nvlText(courseClass.getName()));
            item.put("cover", UrlUtils.toAbsoluteUrl(courseClass.getImage()));
            item.put("price", priceOf(courseClass));
            item.put("duration", durationOf(courseClass));
            item.put("ageMin", courseClass.getAgeMin());
            item.put("ageMax", courseClass.getAgeMax());
            item.put("areaName", path.areaName);
            item.put("venue", ClassScheduleUtil.resolveLessonVenue(lesson, courseClass));
            item.put("coachName", instructor == null ? nvlText(courseClass.getInstructorName()) : nvlText(instructor.getName()));
            item.put("coachAvatar", instructor == null ? "" : UrlUtils.toAbsoluteUrl(instructor.getAvatar()));
            item.put("remaining", remaining);
            item.put("full", remaining <= 0);
            item.put("dayLabel", dayLabel(lesson.getLessonDate()));
            item.put("shortDate", shortDate(lesson.getLessonDate()));
            item.put("weekday", weekday(lesson.getLessonDate()));
            item.put("lessonNo", lesson.getLessonNo());
            item.put("lessonType", lesson.getLessonType());
            item.put("weekDay", lesson.getWeekDay());
            items.add(item);
        }
        items.sort(Comparator
                .comparing((Map<String, Object> m) -> String.valueOf(m.getOrDefault("date", "")))
                .thenComparing(m -> String.valueOf(m.getOrDefault("time", "")))
                .thenComparing(m -> String.valueOf(m.getOrDefault("id", ""))));
        return items;
    }

    // ------------------------------------------------------------------
    // 组装：教练
    // ------------------------------------------------------------------

    private Map<String, Object> coachItem(Instructor instructor, CatalogContext ctx) {
        List<Integer> areaIds = instructorAreas(instructor.getId(), ctx);
        List<String> areas = new ArrayList<>();
        List<String> areaNames = new ArrayList<>();
        for (Integer areaId : areaIds) {
            Area area = ctx.areas.get(areaId);
            if (area == null) {
                continue;
            }
            areas.add(String.valueOf(areaId));
            areaNames.add(nvlText(area.getName()));
        }

        List<String> tags = splitTags(instructor.getSpecialty());
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", String.valueOf(instructor.getId()));
        item.put("name", nvlText(instructor.getName()));
        item.put("avatar", UrlUtils.toAbsoluteUrl(instructor.getAvatar()));
        item.put("gender", genderText(instructor.getGender()));
        // 公开接口不返回教练完整手机号，与 mock 的 138****2210 口径一致。
        // #182 收编公共打码（展示脱敏口径）；isBlank 先归空串，保持既有空白输入输出不变。
        item.put("phone", StringUtils.isBlank(instructor.getPhone()) ? ""
                : ToolUtils.maskPhone(instructor.getPhone(), false));
        item.put("ageRange", ageRangeOf(instructor.getId(), ctx));
        item.put("years", instructor.getExperienceYears() == null ? 0 : instructor.getExperienceYears());
        item.put("tags", tags);
        item.put("tagText", String.join("、", tags));
        item.put("intro", nvlText(instructor.getIntroduction()));
        item.put("areas", areas);
        item.put("areaNames", areaNames);
        item.put("areaText", String.join("、", areaNames));
        item.put("rating", nvl(instructor.getRatingScore()));
        item.put("students", ctx.studentsByInstructor.getOrDefault(instructor.getId(), 0));
        item.put("verified", isVerified(instructor));
        // 资质：优先取已审核通过的结构化资质（#49），无结构化数据时回退旧 certification 文本。
        List<InstructorCert> approvedCerts = ctx.certificatesByInstructor
                .getOrDefault(instructor.getId(), Collections.emptyList());
        InstructorCert primaryCert = approvedCerts.isEmpty() ? null : approvedCerts.get(0);
        item.put("certName", primaryCert != null
                ? nvlText(primaryCert.getName())
                : nvlText(instructor.getCertification()));
        item.put("certNo", primaryCert != null ? nvlText(primaryCert.getNo()) : "");
        item.put("certExpire", primaryCert != null && primaryCert.getExpire() != null
                ? new SimpleDateFormat("yyyy-MM-dd").format(primaryCert.getExpire())
                : "");
        // 旧接口字段（兼容老调用方）
        item.put("desc", nvlText(instructor.getIntroduction()));
        item.put("specialty", nvlText(instructor.getSpecialty()));
        item.put("experienceYears", instructor.getExperienceYears() == null ? 0 : instructor.getExperienceYears());
        item.put("introduction", nvlText(instructor.getIntroduction()));
        item.put("classCount", instructor.getClassCount() == null ? 0 : instructor.getClassCount());
        item.put("ratingScore", nvl(instructor.getRatingScore()));
        item.put("certification", nvlText(instructor.getCertification()));
        return item;
    }

    private String ageRangeOf(Integer instructorId, CatalogContext ctx) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (CourseClass courseClass : ctx.classes) {
            if (!instructorId.equals(courseClass.getInstructorId())) {
                continue;
            }
            if (courseClass.getAgeMin() != null) {
                min = Math.min(min, courseClass.getAgeMin());
            }
            if (courseClass.getAgeMax() != null) {
                max = Math.max(max, courseClass.getAgeMax());
            }
        }
        if (min == Integer.MAX_VALUE || max == Integer.MIN_VALUE) {
            return "";
        }
        return "可执教 " + min + "-" + max + " 岁";
    }

    private List<Integer> instructorAreas(Integer instructorId, CatalogContext ctx) {
        List<Integer> areas = new ArrayList<>();
        for (CourseClass courseClass : ctx.classes) {
            if (!instructorId.equals(courseClass.getInstructorId())) {
                continue;
            }
            Integer areaId = courseClass.getAreaId();
            if (areaId != null && areaId > 0 && !areas.contains(areaId)) {
                areas.add(areaId);
            }
        }
        return areas;
    }

    private List<Integer> instructorCategoryIds(Integer instructorId, CatalogContext ctx) {
        List<Integer> ids = new ArrayList<>();
        for (CourseClass courseClass : ctx.classes) {
            if (!instructorId.equals(courseClass.getInstructorId())) {
                continue;
            }
            Integer categoryId = courseClass.getCategoryId();
            if (categoryId != null && categoryId > 0 && !ids.contains(categoryId)) {
                ids.add(categoryId);
            }
        }
        return ids;
    }

    private boolean coachKeywordMatches(Instructor instructor, CatalogContext ctx, String keyword) {
        if (contains(instructor.getName(), keyword) || contains(instructor.getSpecialty(), keyword)
                || contains(instructor.getIntroduction(), keyword)) {
            return true;
        }
        for (Integer areaId : instructorAreas(instructor.getId(), ctx)) {
            Area area = ctx.areas.get(areaId);
            if (area != null && contains(area.getName(), keyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVerified(Instructor instructor) {
        if (instructor.getStatus() == null || instructor.getStatus() != 1) {
            return false;
        }
        return StringUtils.isNotBlank(instructor.getCertification())
                || StringUtils.isNotBlank(instructor.getCertImages());
    }

    // ------------------------------------------------------------------
    // 数据装载
    // ------------------------------------------------------------------

    private List<CourseClass> liveClasses() {
        return courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .eq("is_delete", 0)
                .eq("is_show", 1)
                .in("status", (Object[]) LIVE_CLASS_STATUS)
                .orderByDesc("id"));
    }

    private List<CourseClass> liveClassesByIds(Set<Integer> classIds) {
        if (classIds == null || classIds.isEmpty()) {
            return Collections.emptyList();
        }
        return courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .in("id", classIds)
                .eq("is_delete", 0)
                .eq("is_show", 1)
                .in("status", (Object[]) LIVE_CLASS_STATUS));
    }

    private List<CourseLesson> futureLessons(List<CourseClass> classes) {
        Set<Integer> classIds = new LinkedHashSet<>();
        for (CourseClass courseClass : classes) {
            classIds.add(courseClass.getId());
        }
        if (classIds.isEmpty()) {
            return Collections.emptyList();
        }
        return courseLessonMapper.selectList(new QueryWrapper<CourseLesson>()
                .in("class_id", classIds)
                .eq("status", 0)
                .ge("lesson_date", LocalDate.now().toString())
                .orderByAsc("lesson_date")
                .orderByAsc("time_slot")
                .orderByAsc("id"));
    }

    private CatalogContext buildContext(List<CourseClass> classes) {
        CatalogContext ctx = new CatalogContext();
        ctx.classes = classes == null ? Collections.<CourseClass>emptyList() : classes;

        Set<Integer> categoryIds = new HashSet<>();
        Set<Integer> instructorIds = new HashSet<>();
        Set<Integer> classIds = new HashSet<>();
        for (CourseClass courseClass : ctx.classes) {
            if (courseClass.getCategoryId() != null && courseClass.getCategoryId() > 0) {
                categoryIds.add(courseClass.getCategoryId());
            }
            if (courseClass.getInstructorId() != null && courseClass.getInstructorId() > 0) {
                instructorIds.add(courseClass.getInstructorId());
            }
            classIds.add(courseClass.getId());
        }

        ctx.categoryNames = categoryNames(categoryIds);
        ctx.instructors = instructorMap(instructorIds);
        ctx.regions = regionMap();
        ctx.communities = communityMap();
        ctx.areas = areaMap();
        ctx.soldByClass = soldCounts(classIds);
        ctx.ratingByClass = ratingAverages(classIds);
        ctx.studentsByInstructor = studentCounts(ctx.classes);
        ctx.certificatesByInstructor = approvedCertificates(ctx.instructors.keySet());
        return ctx;
    }

    /**
     * 已通过审核的结构化资质（#49）：每个教练取有效期最晚的一条作为课程详情主展示，
     * 证书名/编号/有效期均来自 la_instructor_cert，不再固定为空。
     */
    private Map<Integer, List<InstructorCert>> approvedCertificates(Set<Integer> instructorIds) {
        Map<Integer, List<InstructorCert>> map = new HashMap<>();
        if (instructorIds == null || instructorIds.isEmpty()) {
            return map;
        }
        List<InstructorCert> certs = instructorCertMapper.selectList(new QueryWrapper<InstructorCert>()
                .in("instructor_id", instructorIds)
                .eq("status", InstructorCert.STATUS_APPROVED)
                .isNull("delete_time")
                .orderByDesc("expire")
                .orderByDesc("id"));
        for (InstructorCert cert : certs) {
            map.computeIfAbsent(cert.getInstructorId(), key -> new ArrayList<>()).add(cert);
        }
        return map;
    }

    private void ensureCertificates(CatalogContext ctx, Set<Integer> instructorIds) {
        Set<Integer> missing = new HashSet<>();
        for (Integer id : instructorIds) {
            if (id != null && id > 0 && !ctx.certificatesByInstructor.containsKey(id)) {
                missing.add(id);
            }
        }
        if (!missing.isEmpty()) {
            ctx.certificatesByInstructor.putAll(approvedCertificates(missing));
        }
    }

    private void ensureInstructors(CatalogContext ctx, Set<Integer> instructorIds) {
        Set<Integer> missing = new HashSet<>();
        for (Integer id : instructorIds) {
            if (id != null && id > 0 && !ctx.instructors.containsKey(id)) {
                missing.add(id);
            }
        }
        if (!missing.isEmpty()) {
            ctx.instructors.putAll(instructorMap(missing));
        }
    }

    private Map<Integer, String> categoryNames(Set<Integer> ids) {
        Map<Integer, String> names = new HashMap<>();
        if (ids.isEmpty()) {
            return names;
        }
        List<SportsCategory> categories = sportsCategoryMapper.selectList(
                new QueryWrapper<SportsCategory>().in("id", ids));
        for (SportsCategory category : categories) {
            names.put(category.getId(), category.getName());
        }
        return names;
    }

    private Map<Integer, Instructor> instructorMap(Set<Integer> ids) {
        Map<Integer, Instructor> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return map;
        }
        List<Instructor> instructors = instructorMapper.selectList(new QueryWrapper<Instructor>()
                .in("id", ids)
                .isNull("delete_time")
                .in("status", (Object[]) LIVE_CLASS_STATUS));
        for (Instructor instructor : instructors) {
            map.put(instructor.getId(), instructor);
        }
        return map;
    }

    private Map<Integer, Region> regionMap() {
        List<Region> regions = regionMapper.selectList(new QueryWrapper<Region>()
                .isNull("delete_time")
                .eq("status", 1)
                .orderByAsc("sort")
                .orderByAsc("id"));
        Map<Integer, Region> map = new HashMap<>();
        for (Region region : regions) {
            map.put(region.getId(), region);
        }
        return map;
    }

    private Map<Integer, Community> communityMap() {
        List<Community> communities = communityMapper.selectList(new QueryWrapper<Community>()
                .isNull("delete_time")
                .orderByAsc("id"));
        Map<Integer, Community> map = new HashMap<>();
        for (Community community : communities) {
            map.put(community.getId(), community);
        }
        return map;
    }

    private Map<Integer, Area> areaMap() {
        List<Area> areas = areaMapper.selectList(new QueryWrapper<Area>()
                .isNull("delete_time")
                .orderByAsc("sort")
                .orderByAsc("id"));
        Map<Integer, Area> map = new HashMap<>();
        for (Area area : areas) {
            map.put(area.getId(), area);
        }
        return map;
    }

    private Map<Integer, Integer> soldCounts(Set<Integer> classIds) {
        Map<Integer, Integer> counts = new HashMap<>();
        if (classIds == null || classIds.isEmpty()) {
            return counts;
        }
        List<Map<String, Object>> rows = userCourseMapper.selectMaps(new QueryWrapper<UserCourse>()
                .select("course_class_id", "COUNT(*) AS sold_count")
                .isNull("delete_time")
                .in("course_class_id", classIds)
                .groupBy("course_class_id"));
        if (rows == null) {
            return counts;
        }
        for (Map<String, Object> row : rows) {
            Integer classId = intValue(row, "course_class_id", "courseClassId");
            Integer count = intValue(row, "sold_count", "soldCount");
            if (classId != null) {
                counts.put(classId, count == null ? 0 : count);
            }
        }
        return counts;
    }

    private Map<Integer, BigDecimal> ratingAverages(Set<Integer> classIds) {
        Map<Integer, BigDecimal> ratings = new HashMap<>();
        if (classIds == null || classIds.isEmpty()) {
            return ratings;
        }
        List<Map<String, Object>> rows = courseReviewMapper.selectMaps(new QueryWrapper<CourseReview>()
                .select("course_class_id",
                        "AVG((rating_teaching + rating_atmosphere + rating_attitude) / 3.0) AS rating_avg")
                .isNull("delete_time")
                .in("course_class_id", classIds)
                .groupBy("course_class_id"));
        if (rows == null) {
            return ratings;
        }
        for (Map<String, Object> row : rows) {
            Integer classId = intValue(row, "course_class_id", "courseClassId");
            BigDecimal avg = decimalValue(row, "rating_avg", "ratingAvg");
            if (classId != null && avg != null) {
                ratings.put(classId, avg.setScale(1, RoundingMode.HALF_UP));
            }
        }
        return ratings;
    }

    private Map<Integer, Integer> studentCounts(List<CourseClass> classes) {
        Map<Integer, Integer> counts = new HashMap<>();
        if (classes == null || classes.isEmpty()) {
            return counts;
        }
        Map<Integer, Integer> classToInstructor = new HashMap<>();
        Set<Integer> classIds = new HashSet<>();
        for (CourseClass courseClass : classes) {
            classIds.add(courseClass.getId());
            if (courseClass.getInstructorId() != null && courseClass.getInstructorId() > 0) {
                classToInstructor.put(courseClass.getId(), courseClass.getInstructorId());
            }
        }
        List<Map<String, Object>> rows = courseBookingMapper.selectMaps(new QueryWrapper<CourseBooking>()
                .select("course_class_id", "COUNT(DISTINCT student_id) AS student_count")
                .ne("status", 3)
                .gt("student_id", 0)
                .in("course_class_id", classIds)
                .groupBy("course_class_id"));
        if (rows == null) {
            return counts;
        }
        for (Map<String, Object> row : rows) {
            Integer classId = intValue(row, "course_class_id", "courseClassId");
            Integer count = intValue(row, "student_count", "studentCount");
            Integer instructorId = classToInstructor.get(classId);
            if (instructorId != null && count != null) {
                counts.merge(instructorId, count, Integer::sum);
            }
        }
        return counts;
    }

    private Map<Integer, Integer> bookingCounts(List<Integer> lessonIds) {
        Map<Integer, Integer> counts = new HashMap<>();
        if (lessonIds == null || lessonIds.isEmpty()) {
            return counts;
        }
        List<Map<String, Object>> rows = courseBookingMapper.selectMaps(new QueryWrapper<CourseBooking>()
                .select("lesson_id", "COUNT(*) AS booked_count")
                .ne("status", 3)
                .in("lesson_id", lessonIds)
                .groupBy("lesson_id"));
        if (rows == null) {
            return counts;
        }
        for (Map<String, Object> row : rows) {
            Integer lessonId = intValue(row, "lesson_id", "lessonId");
            Integer count = intValue(row, "booked_count", "bookedCount");
            if (lessonId != null) {
                counts.put(lessonId, count == null ? 0 : count);
            }
        }
        return counts;
    }

    // ------------------------------------------------------------------
    // 工具
    // ------------------------------------------------------------------

    private RegionPath pathOf(CourseClass courseClass, CatalogContext ctx) {
        RegionPath path = new RegionPath();
        Area area = ctx.areas.get(courseClass.getAreaId());
        if (area != null) {
            path.areaName = nvlText(area.getName());
        }
        Integer communityId = area != null && area.getCommunityId() != null && area.getCommunityId() > 0
                ? area.getCommunityId()
                : courseClass.getCommunityId();
        if (communityId == null || communityId <= 0) {
            return path;
        }
        Community community = ctx.communities.get(communityId);
        if (community == null) {
            return path;
        }
        path.communityId = community.getId();
        path.communityName = nvlText(community.getName());

        Region street = ctx.regions.get(community.getRegionId());
        if (!isLevel(street, 3)) {
            return path;
        }
        path.streetId = street.getId();
        path.streetName = nvlText(street.getName());

        Region district = ctx.regions.get(street.getParentId());
        if (isLevel(district, 2)) {
            path.districtId = district.getId();
            path.districtName = nvlText(district.getName());
        }
        return path;
    }

    private boolean isLevel(Region region, int level) {
        return region != null && region.getLevel() != null && region.getLevel() == level;
    }

    private Map<Integer, CourseClass> classMap(List<CourseClass> classes) {
        Map<Integer, CourseClass> map = new HashMap<>();
        for (CourseClass courseClass : classes) {
            map.put(courseClass.getId(), courseClass);
        }
        return map;
    }

    private boolean containsClass(List<CourseClass> classes, Integer id) {
        for (CourseClass courseClass : classes) {
            if (courseClass.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private List<Map<String, Object>> paginate(List<Map<String, Object>> items, Integer pageNo, Integer pageSize) {
        int page = pageNo == null || pageNo < 1 ? 1 : pageNo;
        int size = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);
        int from = (page - 1) * size;
        if (from >= items.size()) {
            return Collections.emptyList();
        }
        int to = Math.min(from + size, items.size());
        return new ArrayList<>(items.subList(from, to));
    }

    private void sortByDistance(List<Map<String, Object>> items) {
        if (items.isEmpty()) {
            return;
        }
        boolean hasDistance = false;
        for (Map<String, Object> item : items) {
            Object value = item.get("_distMeters");
            if (value instanceof Double && ((Double) value).doubleValue() < Double.MAX_VALUE) {
                hasDistance = true;
                break;
            }
        }
        if (hasDistance) {
            items.sort(Comparator.comparingDouble(m -> {
                Object value = m.get("_distMeters");
                return value instanceof Number ? ((Number) value).doubleValue() : Double.MAX_VALUE;
            }));
        }
        for (Map<String, Object> item : items) {
            item.remove("_distMeters");
        }
    }

    private double distanceMeters(String lat, String lng, String classLat, String classLng) {
        if (StringUtils.isBlank(lat) || StringUtils.isBlank(lng)
                || StringUtils.isBlank(classLat) || StringUtils.isBlank(classLng)) {
            return Double.MAX_VALUE;
        }
        try {
            return distance(Double.parseDouble(lat), Double.parseDouble(lng),
                    Double.parseDouble(classLat), Double.parseDouble(classLng));
        } catch (Exception e) {
            return Double.MAX_VALUE;
        }
    }

    private String distanceText(double meters) {
        if (meters == Double.MAX_VALUE) {
            return "";
        }
        return meters < 1000 ? Math.round(meters) + "m" : String.format("%.1fkm", meters / 1000.0);
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {
        double r = 6378137.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2.0) * Math.sin(dLng / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return r * c;
    }

    private int durationOf(CourseClass courseClass) {
        String slot = courseClass.getTimeSlot();
        if (courseClass.getScheduleMode() != null && courseClass.getScheduleMode() == 1) {
            try {
                List<ClassScheduleUtil.ScheduleRule> rules = ClassScheduleUtil.parseRules(courseClass.getScheduleRules());
                if (rules != null && !rules.isEmpty()) {
                    slot = rules.get(0).getTimeSlot();
                }
            } catch (Exception ignored) {
                // schedule_rules 异常时回退 time_slot / 默认 60 分钟
            }
        }
        int minutes = minutesBetween(slot);
        return minutes > 0 ? minutes : DEFAULT_DURATION;
    }

    private int minutesBetween(String slot) {
        if (StringUtils.isBlank(slot) || !slot.contains("-")) {
            return 0;
        }
        String[] parts = slot.split("-");
        if (parts.length != 2) {
            return 0;
        }
        try {
            String[] start = parts[0].trim().split(":");
            String[] end = parts[1].trim().split(":");
            int startMinutes = Integer.parseInt(start[0]) * 60 + Integer.parseInt(start[1]);
            int endMinutes = Integer.parseInt(end[0]) * 60 + Integer.parseInt(end[1]);
            int duration = endMinutes - startMinutes;
            return duration <= 0 ? 0 : duration;
        } catch (Exception e) {
            return 0;
        }
    }

    private BigDecimal priceOf(CourseClass courseClass) {
        BigDecimal fee = nvl(courseClass.getFeePerLesson());
        return fee.signum() > 0 ? fee : nvl(courseClass.getTotalFee());
    }

    private BigDecimal ratingOf(Integer classId, Instructor instructor, CatalogContext ctx) {
        BigDecimal rating = ctx.ratingByClass.get(classId);
        if (rating != null && rating.signum() > 0) {
            return rating;
        }
        if (instructor != null && instructor.getRatingScore() != null && instructor.getRatingScore().signum() > 0) {
            return instructor.getRatingScore().setScale(1, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private String lessonStatusText(Integer status) {
        if (status == null) {
            return "open";
        }
        if (status == 1) {
            return "finished";
        }
        if (status == 2) {
            return "cancelled";
        }
        return "open";
    }

    private String dayLabel(String date) {
        LocalDate value = parseDate(date);
        if (value == null) {
            return "";
        }
        long diff = ChronoUnit.DAYS.between(LocalDate.now(), value);
        if (diff == 0) {
            return "今天";
        }
        if (diff == 1) {
            return "明天";
        }
        if (diff == 2) {
            return "后天";
        }
        return weekday(date);
    }

    private String shortDate(String date) {
        LocalDate value = parseDate(date);
        if (value == null) {
            return "";
        }
        return String.format("%02d.%02d", value.getMonthValue(), value.getDayOfMonth());
    }

    private String weekday(String date) {
        LocalDate value = parseDate(date);
        if (value == null) {
            return "";
        }
        return WEEK_CN[value.getDayOfWeek().getValue() % 7];
    }

    private LocalDate parseDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    private String timestampToDate(Integer timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp.longValue() * 1000L));
    }

    private String genderText(Integer gender) {
        if (gender == null) {
            return "";
        }
        if (gender == 1) {
            return "男";
        }
        if (gender == 2) {
            return "女";
        }
        return "";
    }

    private List<String> splitTags(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        List<String> tags = new ArrayList<>();
        for (String part : text.split("[,，、]")) {
            String tag = part.trim();
            if (!tag.isEmpty()) {
                tags.add(tag);
            }
        }
        return tags;
    }

    private boolean contains(String text, String keyword) {
        return text != null && text.contains(keyword);
    }

    private String idText(Integer value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String nvlText(String value) {
        return value == null ? "" : value;
    }

    /**
     * 后台课程介绍多图（相对路径，逗号分隔）转小程序可直接渲染的绝对地址（#44）。
     */
    private List<String> absoluteImageList(String introImages) {
        if (StringUtils.isBlank(introImages)) {
            return Collections.emptyList();
        }
        List<String> urls = new ArrayList<>();
        for (String path : introImages.split(",")) {
            String item = path.trim();
            if (!item.isEmpty()) {
                urls.add(UrlUtils.toAbsoluteUrl(item));
            }
        }
        return urls;
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String moneyText(BigDecimal value) {
        return nvl(value).stripTrailingZeros().toPlainString();
    }

    private Integer intValue(Map<String, Object> row, String snakeKey, String camelKey) {
        Object value = row.get(snakeKey);
        if (value == null) {
            value = row.get(camelKey);
        }
        return value instanceof Number ? ((Number) value).intValue() : null;
    }

    private BigDecimal decimalValue(Map<String, Object> row, String snakeKey, String camelKey) {
        Object value = row.get(snakeKey);
        if (value == null) {
            value = row.get(camelKey);
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return null;
    }

    private static final class RegionPath {
        private String areaName = "";
        private Integer communityId;
        private String communityName = "";
        private Integer streetId;
        private String streetName = "";
        private Integer districtId;
        private String districtName = "";
    }

    private static final class CatalogContext {
        private List<CourseClass> classes = Collections.emptyList();
        private Map<Integer, String> categoryNames = Collections.emptyMap();
        private Map<Integer, Instructor> instructors = new HashMap<>();
        private Map<Integer, Region> regions = Collections.emptyMap();
        private Map<Integer, Community> communities = Collections.emptyMap();
        private Map<Integer, Area> areas = Collections.emptyMap();
        private Map<Integer, Integer> soldByClass = Collections.emptyMap();
        private Map<Integer, BigDecimal> ratingByClass = Collections.emptyMap();
        private Map<Integer, Integer> studentsByInstructor = Collections.emptyMap();
        private Map<Integer, List<InstructorCert>> certificatesByInstructor = Collections.emptyMap();
    }
}
