package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.SportsCategory;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.region.Region;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.SportsCategoryMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.region.RegionMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IRegionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 小程序地区五级只读实现（#21）。
 *
 * <p>查询策略：一次请求内按表各查一次，再在内存里按 id 组装，避免“每条小区再查社区/街道/课程”的 N+1。</p>
 *
 * <p>小区可见规则：小区 status=1 且未软删，至少有一个已上架班级
 * （la_course_class.status IN (1,2) AND is_delete=0），并且该班级至少有一个可约场次：
 * ys_course_lesson.status=0、lesson_date >= 今天、该场次有效预约人数（la_course_booking.status != 3）
 * 小于班级 max_students。进入五级导航/地图还要求小区已绑定社区，且社区已绑定街道/分区。</p>
 */
@Service
public class RegionServiceImpl implements IRegionService {

    /** 已上架班级状态：1=进行中，2=预报名。 */
    private static final Integer[] LIVE_CLASS_STATUS = new Integer[]{1, 2};

    @Resource
    RegionMapper regionMapper;
    @Resource
    CommunityMapper communityMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    SportsCategoryMapper sportsCategoryMapper;
    @Resource
    InstructorMapper instructorMapper;

    @Override
    public List<Map<String, Object>> cities() {
        List<Region> regions = enabledRegions();
        Map<Integer, Integer> districtCount = new HashMap<>();
        for (Region region : regions) {
            if (isLevel(region, 2)) {
                districtCount.merge(region.getParentId(), 1, Integer::sum);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Region region : regions) {
            if (!isLevel(region, 1)) {
                continue;
            }
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", idText(region.getId()));
            item.put("name", region.getName());
            item.put("center", center(region));
            item.put("districtCount", districtCount.getOrDefault(region.getId(), 0));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> districts(Integer cityId) {
        List<Region> regions = enabledRegions();
        Map<Integer, Integer> streetCount = new HashMap<>();
        for (Region region : regions) {
            if (isLevel(region, 3)) {
                streetCount.merge(region.getParentId(), 1, Integer::sum);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Region region : regions) {
            if (!isLevel(region, 2) || (cityId != null && cityId > 0 && !cityId.equals(region.getParentId()))) {
                continue;
            }
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", idText(region.getId()));
            item.put("cityId", idText(region.getParentId()));
            item.put("name", region.getName());
            item.put("streetCount", streetCount.getOrDefault(region.getId(), 0));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> streets(Integer districtId) {
        List<Region> regions = enabledRegions();
        Map<Integer, Region> regionMap = regionMap(regions);
        Map<Integer, Community> communityMap = communityMap();
        Map<Integer, Integer> liveAreaCount = liveAreaCountByStreet(regionMap, communityMap);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Region region : regions) {
            if (!isLevel(region, 3) || (districtId != null && districtId > 0 && !districtId.equals(region.getParentId()))) {
                continue;
            }
            Region district = regionMap.get(region.getParentId());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", idText(region.getId()));
            item.put("districtId", idText(region.getParentId()));
            item.put("name", region.getName());
            item.put("districtName", district == null ? "" : district.getName());
            item.put("areaCount", liveAreaCount.getOrDefault(region.getId(), 0));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> communities(Integer streetId) {
        List<Region> regions = enabledRegions();
        Map<Integer, Region> regionMap = regionMap(regions);
        Map<Integer, Community> communityMap = communityMap();
        Map<Integer, Integer> liveCountByCommunity = liveAreaCountByCommunity(regionMap, communityMap);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Community community : communityMap.values()) {
            if (streetId != null && streetId > 0 && !streetId.equals(community.getRegionId())) {
                continue;
            }
            Region street = regionMap.get(community.getRegionId());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", idText(community.getId()));
            item.put("name", community.getName());
            item.put("streetId", idText(community.getRegionId()));
            item.put("streetName", street == null ? "" : street.getName());
            item.put("areaCount", liveCountByCommunity.getOrDefault(community.getId(), 0));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> areas(Boolean onlyLive, Integer districtId, Integer streetId,
                                           Integer communityId, String keyword) {
        List<Region> regions = enabledRegions();
        Map<Integer, Region> regionMap = regionMap(regions);
        Map<Integer, Community> communityMap = communityMap();
        LiveSnapshot live = liveSnapshot();
        String kw = keyword == null ? "" : keyword.trim();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Area area : enabledAreas()) {
            RegionPath path = pathOf(area.getCommunityId(), communityMap, regionMap);
            if (Boolean.TRUE.equals(onlyLive) && (!live.liveAreaIds.contains(area.getId()) || !path.navigable())) {
                continue;
            }
            if (districtId != null && districtId > 0 && !districtId.equals(path.districtId)) {
                continue;
            }
            if (streetId != null && streetId > 0 && !streetId.equals(path.streetId)) {
                continue;
            }
            if (communityId != null && communityId > 0 && !communityId.equals(area.getCommunityId())) {
                continue;
            }
            if (!kw.isEmpty() && (area.getName() == null || !area.getName().contains(kw))) {
                continue;
            }
            result.add(areaItem(area, path, live.classesByArea.get(area.getId()),
                    live.liveAreaIds.contains(area.getId())));
        }
        return result;
    }

    @Override
    public Map<String, Object> areaDetail(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        Area area = areaMapper.selectOne(new QueryWrapper<Area>()
                .eq("id", id)
                .eq("status", 1)
                .isNull("delete_time")
                .last("limit 1"));
        if (area == null) {
            return null;
        }

        Map<Integer, Region> regionMap = regionMap(enabledRegions());
        Map<Integer, Community> communityMap = communityMap();
        RegionPath path = pathOf(area.getCommunityId(), communityMap, regionMap);

        List<CourseClass> classes = liveClassesOfArea(area.getId());
        Map<String, Object> item = areaItem(area, path, classes, hasBookableLesson(classes));
        item.put("district", path.districtName);
        item.put("courses", courseItems(classes));
        item.put("coaches", coachItems(classes));
        return item;
    }

    // ------------------------------------------------------------------
    // 数据装载
    // ------------------------------------------------------------------

    private List<Region> enabledRegions() {
        return regionMapper.selectList(new QueryWrapper<Region>()
                .isNull("delete_time")
                .eq("status", 1)
                .orderByAsc("sort")
                .orderByAsc("id"));
    }

    private List<Area> enabledAreas() {
        return areaMapper.selectList(new QueryWrapper<Area>()
                .isNull("delete_time")
                .eq("status", 1)
                .orderByAsc("sort")
                .orderByAsc("id"));
    }

    private Map<Integer, Region> regionMap(List<Region> regions) {
        Map<Integer, Region> map = new LinkedHashMap<>();
        for (Region region : regions) {
            map.put(region.getId(), region);
        }
        return map;
    }

    private Map<Integer, Community> communityMap() {
        List<Community> communities = communityMapper.selectList(new QueryWrapper<Community>()
                .isNull("delete_time")
                .orderByAsc("id"));
        Map<Integer, Community> map = new LinkedHashMap<>();
        for (Community community : communities) {
            map.put(community.getId(), community);
        }
        return map;
    }

    private List<CourseClass> liveClassesOfArea(Integer areaId) {
        return courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .eq("area_id", areaId)
                .eq("is_delete", 0)
                .eq("is_show", 1)
                .in("status", (Object[]) LIVE_CLASS_STATUS)
                .orderByAsc("id"));
    }

    /**
     * 一次请求只查两次：已上架班级 + 可约场次聚合。
     */
    private LiveSnapshot liveSnapshot() {
        List<CourseClass> classes = courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .gt("area_id", 0)
                .eq("is_delete", 0)
                .eq("is_show", 1)
                .in("status", (Object[]) LIVE_CLASS_STATUS)
                .orderByAsc("id"));

        List<Integer> classIds = new ArrayList<>();
        for (CourseClass courseClass : classes) {
            classIds.add(courseClass.getId());
        }
        Map<Integer, Integer> bookableCounts = bookableCounts(classIds);
        LiveSnapshot snapshot = new LiveSnapshot();
        for (CourseClass courseClass : classes) {
            snapshot.classesByArea
                    .computeIfAbsent(courseClass.getAreaId(), key -> new ArrayList<>())
                    .add(courseClass);
            if (bookableCounts.getOrDefault(courseClass.getId(), 0) > 0) {
                snapshot.liveAreaIds.add(courseClass.getAreaId());
            }
        }
        return snapshot;
    }

    /**
     * 可约场次聚合：按班级统计满足以下条件的场次数。
     *
     * <ul>
     *   <li>场次 status=0（未开始，与 iUserCourseService.bookLesson 的可约判断一致）</li>
     *   <li>lesson_date >= 今天（避免历史未结课场次）</li>
     *   <li>有效预约数（la_course_booking.status != 3，3=已取消）小于班级 max_students</li>
     * </ul>
     *
     * 用一条 GROUP BY 查询完成，避免逐小区/逐班级查询。
     */
    private Map<Integer, Integer> bookableCounts(List<Integer> classIds) {
        Map<Integer, Integer> counts = new HashMap<>();
        if (classIds.isEmpty()) {
            return counts;
        }
        QueryWrapper<CourseLesson> query = new QueryWrapper<>();
        query.select("class_id", "COUNT(*) AS bookable_count")
                .eq("status", 0)
                .ge("lesson_date", LocalDate.now().toString())
                .in("class_id", classIds)
                .apply("(SELECT COUNT(*) FROM la_course_booking b WHERE b.lesson_id = ys_course_lesson.id AND b.status <> 3)"
                        + " < (SELECT c.max_students FROM la_course_class c WHERE c.id = ys_course_lesson.class_id)")
                .groupBy("class_id");

        List<Map<String, Object>> rows = courseLessonMapper.selectMaps(query);
        if (rows == null) {
            return counts;
        }
        for (Map<String, Object> row : rows) {
            Integer classId = intValue(row, "class_id", "classId");
            Integer count = intValue(row, "bookable_count", "bookableCount");
            if (classId != null) {
                counts.put(classId, count == null ? 0 : count);
            }
        }
        return counts;
    }

    private Map<Integer, Integer> liveAreaCountByStreet(Map<Integer, Region> regionMap,
                                                        Map<Integer, Community> communityMap) {
        LiveSnapshot live = liveSnapshot();
        Map<Integer, Integer> counts = new HashMap<>();
        for (Area area : enabledAreas()) {
            if (!live.liveAreaIds.contains(area.getId())) {
                continue;
            }
            RegionPath path = pathOf(area.getCommunityId(), communityMap, regionMap);
            if (path.streetId != null) {
                counts.merge(path.streetId, 1, Integer::sum);
            }
        }
        return counts;
    }

    private Map<Integer, Integer> liveAreaCountByCommunity(Map<Integer, Region> regionMap,
                                                           Map<Integer, Community> communityMap) {
        LiveSnapshot live = liveSnapshot();
        Map<Integer, Integer> counts = new HashMap<>();
        for (Area area : enabledAreas()) {
            if (!live.liveAreaIds.contains(area.getId())) {
                continue;
            }
            RegionPath path = pathOf(area.getCommunityId(), communityMap, regionMap);
            if (path.communityId != null && path.streetId != null) {
                counts.merge(path.communityId, 1, Integer::sum);
            }
        }
        return counts;
    }

    // ------------------------------------------------------------------
    // 组装
    // ------------------------------------------------------------------

    private Map<String, Object> areaItem(Area area, RegionPath path, List<CourseClass> classes, boolean hasCourse) {
        List<CourseClass> courseList = classes == null ? Collections.<CourseClass>emptyList() : classes;
        BigDecimal minPrice = null;
        List<String> titles = new ArrayList<>();
        for (CourseClass courseClass : courseList) {
            if (courseClass.getFeePerLesson() != null
                    && (minPrice == null || courseClass.getFeePerLesson().compareTo(minPrice) < 0)) {
                minPrice = courseClass.getFeePerLesson();
            }
            if (titles.size() < 2 && StringUtils.isNotEmpty(courseClass.getName())) {
                titles.add(courseClass.getName());
            }
        }

        Map<String, Object> item = new LinkedHashMap<>();
        // 小区 ID 保持 Number：微信 map marker.id 必须是 Number（页面直接用 a.id 生成 marker）。
        item.put("id", area.getId());
        item.put("communityId", idText(area.getCommunityId()));
        item.put("name", area.getName());
        item.put("address", area.getAddress());
        item.put("distance", area.getDistanceText() == null ? "" : area.getDistanceText());
        item.put("lat", area.getLat());
        item.put("lng", area.getLng());
        item.put("cover", UrlUtils.toAbsoluteUrl(area.getCover()));
        item.put("tags", splitTags(area.getTags()));
        item.put("status", area.getStatus());
        item.put("communityName", path.communityName);
        item.put("streetId", idText(path.streetId));
        item.put("streetName", path.streetName);
        item.put("districtId", idText(path.districtId));
        item.put("districtName", path.districtName);
        item.put("courseCount", courseList.size());
        item.put("minPrice", minPrice == null ? BigDecimal.ZERO : minPrice);
        item.put("hasCourse", hasCourse);
        item.put("courseSummary", String.join(" / ", titles));
        item.put("courseTitles", titles);
        return item;
    }

    private List<Map<String, Object>> courseItems(List<CourseClass> classes) {
        Map<Integer, String> categoryNames = categoryNames(classes);
        List<Map<String, Object>> items = new ArrayList<>();
        for (CourseClass courseClass : classes) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", idText(courseClass.getId()));
            item.put("areaId", idText(courseClass.getAreaId()));
            item.put("title", courseClass.getName());
            item.put("subtitle", courseClass.getVenue() == null ? "" : courseClass.getVenue());
            item.put("cover", UrlUtils.toAbsoluteUrl(courseClass.getImage()));
            item.put("coachIds", courseClass.getInstructorId() == null || courseClass.getInstructorId() <= 0
                    ? Collections.emptyList()
                    : Collections.singletonList(idText(courseClass.getInstructorId())));
            item.put("coachNames", courseClass.getInstructorName() == null ? "" : courseClass.getInstructorName());
            // 班级模型暂无年龄段字段（T29.2 课程契约补齐）；先返回空串，避免页面显示 0 岁。
            item.put("ageMin", "");
            item.put("ageMax", "");
            item.put("level", categoryNames.getOrDefault(courseClass.getCategoryId(), ""));
            item.put("unit", "次");
            item.put("price", courseClass.getFeePerLesson() == null ? BigDecimal.ZERO : courseClass.getFeePerLesson());
            item.put("capacity", courseClass.getMaxStudents());
            items.add(item);
        }
        return items;
    }

    private List<Map<String, Object>> coachItems(List<CourseClass> classes) {
        Set<Integer> coachIds = new LinkedHashSet<>();
        for (CourseClass courseClass : classes) {
            if (courseClass.getInstructorId() != null && courseClass.getInstructorId() > 0) {
                coachIds.add(courseClass.getInstructorId());
            }
        }
        if (coachIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Instructor> instructors = instructorMapper.selectList(new QueryWrapper<Instructor>()
                .in("id", coachIds)
                .isNull("delete_time")
                .in("status", 1, 2));
        Map<Integer, Instructor> instructorMap = new HashMap<>();
        for (Instructor instructor : instructors) {
            instructorMap.put(instructor.getId(), instructor);
        }

        List<Map<String, Object>> items = new ArrayList<>();
        for (Integer coachId : coachIds) {
            Instructor instructor = instructorMap.get(coachId);
            if (instructor == null) {
                continue;
            }
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", idText(instructor.getId()));
            item.put("name", instructor.getName());
            item.put("avatar", UrlUtils.toAbsoluteUrl(instructor.getAvatar()));
            item.put("ageRange", "");
            item.put("years", instructor.getExperienceYears() == null ? 0 : instructor.getExperienceYears());
            item.put("tags", splitTags(instructor.getSpecialty()));
            item.put("intro", instructor.getIntroduction() == null ? "" : instructor.getIntroduction());
            item.put("rating", instructor.getRatingScore());
            item.put("verified", instructor.getStatus() != null && instructor.getStatus() == 1
                    && StringUtils.isNotBlank(instructor.getCertification()));
            items.add(item);
        }
        return items;
    }

    private Map<Integer, String> categoryNames(List<CourseClass> classes) {
        Set<Integer> ids = new HashSet<>();
        for (CourseClass courseClass : classes) {
            if (courseClass.getCategoryId() != null && courseClass.getCategoryId() > 0) {
                ids.add(courseClass.getCategoryId());
            }
        }
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

    // ------------------------------------------------------------------
    // 工具
    // ------------------------------------------------------------------

    private RegionPath pathOf(Integer communityId, Map<Integer, Community> communityMap,
                              Map<Integer, Region> regionMap) {
        RegionPath path = new RegionPath();
        if (communityId == null || communityId <= 0) {
            return path;
        }
        Community community = communityMap.get(communityId);
        if (community == null) {
            return path;
        }
        path.communityId = community.getId();
        path.communityName = community.getName();

        Region street = regionMap.get(community.getRegionId());
        if (!isLevel(street, 3)) {
            return path;
        }
        path.streetId = street.getId();
        path.streetName = street.getName();

        Region district = regionMap.get(street.getParentId());
        if (isLevel(district, 2)) {
            path.districtId = district.getId();
            path.districtName = district.getName();
        }
        return path;
    }

    private boolean hasBookableLesson(List<CourseClass> classes) {
        if (classes == null || classes.isEmpty()) {
            return false;
        }
        List<Integer> classIds = new ArrayList<>();
        for (CourseClass courseClass : classes) {
            classIds.add(courseClass.getId());
        }
        Map<Integer, Integer> counts = bookableCounts(classIds);
        for (Integer classId : classIds) {
            if (counts.getOrDefault(classId, 0) > 0) {
                return true;
            }
        }
        return false;
    }

    private String idText(Integer value) {
        return value == null ? "" : String.valueOf(value);
    }

    private boolean isLevel(Region region, int level) {
        return region != null && region.getLevel() != null && region.getLevel() == level;
    }

    private Integer intValue(Map<String, Object> row, String snakeKey, String camelKey) {
        Object value = row.get(snakeKey);
        if (value == null) {
            value = row.get(camelKey);
        }
        return value instanceof Number ? ((Number) value).intValue() : null;
    }

    private Map<String, Object> center(Region region) {
        Map<String, Object> center = new LinkedHashMap<>();
        center.put("latitude", region.getCenterLat());
        center.put("longitude", region.getCenterLng());
        return center;
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

    private static final class RegionPath {
        private Integer communityId;
        private String communityName = "";
        private Integer streetId;
        private String streetName = "";
        private Integer districtId;
        private String districtName = "";

        private boolean navigable() {
            return streetId != null && districtId != null;
        }
    }

    private static final class LiveSnapshot {
        private final Map<Integer, List<CourseClass>> classesByArea = new HashMap<>();
        private final Set<Integer> liveAreaIds = new HashSet<>();
    }
}
