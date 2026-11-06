/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseReview
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseReviewMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.service.ICourseReviewService
 *  com.mdd.front.service.impl.CourseReviewServiceImpl
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseReview;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseReviewMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.ICourseReviewService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CourseReviewServiceImpl
implements ICourseReviewService {
    @Resource
    CourseReviewMapper courseReviewMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;

    public void submit(Integer userId, Map<String, Object> params) {
        Integer lessonNo;
        Integer courseClassId = this.parseRequiredInt(params.get("courseClassId"), "\u8bfe\u7a0b\u53c2\u6570\u5f02\u5e38");
        Integer n = lessonNo = this.parseOptionalInt(params.get("lessonNo"));
        if (lessonNo == null || lessonNo <= 0) {
            throw new RuntimeException("\u8bfe\u65f6\u5df2\u7ed3\u675f\uff0c\u65e0\u6cd5\u8bc4\u4ef7");
        }
        // \u53ea\u6709\u771f\u6b63\u4e0a\u8fc7\u8be5\u8bfe\u8282\u7684\u5b66\u5458\u624d\u80fd\u8bc4\u4ef7\uff1a\u5426\u5219\u4efb\u610f\u767b\u5f55\u8d26\u53f7\u53ef\u4ee5\u5237\u4efb\u610f\u8bfe\u7a0b/\u6559\u7ec3\u7684\u8bc4\u5206\u3002
        boolean attended = this.hasAttendedLesson(userId, courseClassId, lessonNo);
        if (!attended) {
            throw new RuntimeException("\u8be5\u8bfe\u8282\u5c1a\u672a\u5b8c\u6210\uff0c\u6682\u4e0d\u80fd\u8bc4\u4ef7");
        }
        CourseReview existing = (CourseReview)this.courseReviewMapper.selectOne(new QueryWrapper<CourseReview>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).eq("lesson_no", (Object)lessonNo).isNull("delete_time").last("limit 1"));
        if (existing != null) {
            throw new RuntimeException("\u60a8\u5df2\u8bc4\u4ef7\u8fc7\u8be5\u8bfe\u8282");
        }
        CourseClass cc = (CourseClass)this.courseClassMapper.selectById((Serializable)courseClassId);
        if (cc == null) {
            throw new RuntimeException("\u8bfe\u7a0b\u73ed\u7ea7\u4e0d\u5b58\u5728");
        }
        if (cc.getStatus() != null && cc.getStatus() == 2) {
            throw new RuntimeException("\u9884\u552e\u8bfe\u7a0b\u6682\u4e0d\u652f\u6301\u8bc4\u4ef7");
        }
        if (cc.getStatus() != null && cc.getStatus() == 0) {
            throw new RuntimeException("\u8be5\u8bfe\u7a0b\u5df2\u7ed3\u675f\uff0c\u65e0\u6cd5\u8bc4\u4ef7");
        }
        CourseReview review = new CourseReview();
        review.setUserId(userId);
        review.setCourseClassId(courseClassId);
        review.setLessonNo(lessonNo);
        review.setInstructorId(cc.getInstructorId());
        review.setRatingTeaching(this.parseRating(params.get("ratingTeaching")));
        review.setRatingAtmosphere(this.parseRating(params.get("ratingAtmosphere")));
        review.setRatingAttitude(this.parseRating(params.get("ratingAttitude")));
        String content = params.getOrDefault("content", "").toString().trim();
        if (content.length() > 500) {
            content = content.substring(0, 500);
        }
        review.setContent(content);
        review.setImages(params.getOrDefault("images", "").toString());
        review.setCreateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        review.setUpdateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        this.courseReviewMapper.insert(review);
        this.updateInstructorRating(cc.getInstructorId());
    }

    public List<Map<String, Object>> list(Integer courseClassId) {
        List<CourseReview> reviews = this.courseReviewMapper.selectList(new QueryWrapper<CourseReview>().eq("course_class_id", (Object)courseClassId).isNull("delete_time").orderByDesc("id"));
        LinkedList<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        for (CourseReview r : reviews) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", r.getId());
            item.put("lessonNo", r.getLessonNo());
            item.put("userId", r.getUserId());
            item.put("ratingTeaching", r.getRatingTeaching());
            item.put("ratingAtmosphere", r.getRatingAtmosphere());
            item.put("ratingAttitude", r.getRatingAttitude());
            item.put("content", r.getContent());
            item.put("images", r.getImages() != null && !r.getImages().isEmpty() ? Arrays.stream(r.getImages().split(",")).map(s -> UrlUtils.toAbsoluteUrl((String)s.trim())).collect(Collectors.toList()) : new ArrayList());
            item.put("createTime", TimeUtils.timestampToDate((Long)r.getCreateTime().longValue()));
            item.put("recommendCount", r.getRecommendCount() != null ? r.getRecommendCount() : 0);
            item.put("likeCount", r.getLikeCount() != null ? r.getLikeCount() : 0);
            item.put("replyContent", r.getReplyContent());
            item.put("replyTime", r.getReplyTime() != null ? TimeUtils.timestampToDate((Long)r.getReplyTime().longValue()) : "");
            User reviewUser = this.userMapper.selectById((Serializable)r.getUserId());
            item.put("userName", reviewUser != null
                    ? (reviewUser.getNickname() != null && !reviewUser.getNickname().isEmpty()
                        ? reviewUser.getNickname() : reviewUser.getRealName())
                    : "");
            item.put("userAvatar", reviewUser != null && reviewUser.getAvatar() != null
                    ? UrlUtils.toAbsoluteUrl((String)reviewUser.getAvatar()) : "");
            result.add(item);
        }
        return result;
    }

    public Map<String, Object> instructorReviewSummary(Integer instructorId, Integer pageNo, Integer pageSize, String filter) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        QueryWrapper<CourseReview> statQw = new QueryWrapper<CourseReview>();
        statQw.eq("instructor_id", (Object)instructorId).isNull("delete_time");
        List<CourseReview> allReviews = this.courseReviewMapper.selectList((Wrapper)statQw);
        int totalCount = allReviews.size();
        int goodCount = 0;
        BigDecimal sumScore = BigDecimal.ZERO;
        int lowTeaching = 0;
        int lowAtmosphere = 0;
        int lowAttitude = 0;
        for (CourseReview r : allReviews) {
            int t = r.getRatingTeaching() != null ? r.getRatingTeaching() : 0;
            int a = r.getRatingAtmosphere() != null ? r.getRatingAtmosphere() : 0;
            int att = r.getRatingAttitude() != null ? r.getRatingAttitude() : 0;
            BigDecimal avg = BigDecimal.valueOf(t + a + att).divide(BigDecimal.valueOf(3L), 2, RoundingMode.HALF_UP);
            sumScore = sumScore.add(avg);
            if (avg.compareTo(BigDecimal.valueOf(4L)) >= 0) {
                ++goodCount;
            }
            if (t < 4) {
                ++lowTeaching;
            }
            if (a < 4) {
                ++lowAtmosphere;
            }
            if (att >= 4) continue;
            ++lowAttitude;
        }
        BigDecimal avgScore = totalCount > 0 ? sumScore.divide(BigDecimal.valueOf(totalCount), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        int goodRate = totalCount > 0 ? (int)Math.round((double)goodCount * 100.0 / (double)totalCount) : 0;
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)instructorId);
        BigDecimal instructorRating = instructor != null && instructor.getRatingScore() != null ? instructor.getRatingScore() : avgScore;
        result.put("totalCount", totalCount);
        result.put("avgScore", instructorRating);
        result.put("goodRate", goodRate);
        ArrayList<String> suggestions = new ArrayList<String>();
        if (lowTeaching > 0) {
            suggestions.add(lowTeaching + "\u4f4d\u5b66\u5458\u8ba4\u4e3a\u6559\u5b66\u8d28\u91cf\u6709\u5f85\u63d0\u5347");
        }
        if (lowAtmosphere > 0) {
            suggestions.add(lowAtmosphere + "\u4f4d\u5b66\u5458\u8ba4\u4e3a\u8bfe\u5802\u6c1b\u56f4\u6709\u5f85\u6539\u5584");
        }
        if (lowAttitude > 0) {
            suggestions.add(lowAttitude + "\u4f4d\u5b66\u5458\u5efa\u8bae\u6559\u7ec3\u6001\u5ea6\u66f4\u52a0\u8010\u5fc3");
        }
        if (suggestions.isEmpty()) {
            suggestions.add("\u6240\u6709\u5b66\u5458\u5bf9\u60a8\u7684\u8bc4\u4ef7\u90fd\u5f88\u68d2\uff0c\u7ee7\u7eed\u4fdd\u6301\uff01");
        }
        result.put("suggestions", suggestions);
        QueryWrapper<CourseReview> listQw = new QueryWrapper<CourseReview>();
        listQw.eq("instructor_id", (Object)instructorId).isNull("delete_time");
        if ("5star".equals(filter)) {
            listQw.apply("(rating_teaching + rating_atmosphere + rating_attitude) / 3 >= 5", new Object[0]);
        } else if ("4star".equals(filter)) {
            listQw.apply("(rating_teaching + rating_atmosphere + rating_attitude) / 3 >= 4", new Object[0]);
        } else if ("hasImage".equals(filter)) {
            listQw.isNotNull("images").ne("images", (Object)"");
        }
        listQw.orderByDesc("create_time");
        long total = this.courseReviewMapper.selectCount((Wrapper)listQw);
        listQw.last("LIMIT " + (pageNo - 1) * pageSize + "," + pageSize);
        List<CourseReview> pageList = this.courseReviewMapper.selectList((Wrapper)listQw);
        ArrayList reviewList = new ArrayList();
        for (CourseReview r : pageList) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", r.getId());
            User user = (User)this.userMapper.selectById((Serializable)r.getUserId());
            item.put("name", user != null ? (user.getNickname() != null ? user.getNickname() : "\u5b66\u5458") : "\u5b66\u5458");
            item.put("avatar", user != null && user.getAvatar() != null ? UrlUtils.toAbsoluteUrl((String)user.getAvatar()) : "");
            CourseClass cc = (CourseClass)this.courseClassMapper.selectById((Serializable)r.getCourseClassId());
            item.put("className", cc != null ? cc.getName() : "");
            item.put("date", TimeUtils.timestampToDate((Long)r.getCreateTime().longValue()));
            item.put("dimensions", dimensions(r));
            item.put("content", r.getContent() != null ? r.getContent() : "");
            item.put("images", r.getImages() != null && !r.getImages().isEmpty() ? Arrays.stream(r.getImages().split(",")).map(s -> UrlUtils.toAbsoluteUrl((String)s.trim())).collect(Collectors.toList()) : new ArrayList());
            item.put("recommendCount", r.getRecommendCount() != null ? r.getRecommendCount() : 0);
            item.put("likes", r.getLikeCount() != null ? r.getLikeCount() : 0);
            item.put("replyContent", r.getReplyContent() != null ? r.getReplyContent() : "");
            item.put("replyTime", r.getReplyTime() != null ? TimeUtils.timestampToDate((Long)r.getReplyTime().longValue()) : "");
            reviewList.add(item);
        }
        result.put("reviews", reviewList);
        result.put("total", total);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        result.put("noMore", (long)pageNo.intValue() * (long)pageSize.intValue() >= total);
        return result;
    }

    private void updateInstructorRating(Integer instructorId) {
        Instructor instructor;
        if (instructorId == null) {
            return;
        }
        List<CourseReview> allReviews = this.courseReviewMapper.selectList(new QueryWrapper<CourseReview>().eq("instructor_id", (Object)instructorId).isNull("delete_time"));
        if (allReviews.isEmpty()) {
            return;
        }
        BigDecimal totalScore = BigDecimal.ZERO;
        for (CourseReview r : allReviews) {
            int t = r.getRatingTeaching() != null ? r.getRatingTeaching() : 0;
            int a = r.getRatingAtmosphere() != null ? r.getRatingAtmosphere() : 0;
            int att = r.getRatingAttitude() != null ? r.getRatingAttitude() : 0;
            BigDecimal singleScore = BigDecimal.valueOf(t + a + att).divide(BigDecimal.valueOf(3L), 4, RoundingMode.HALF_UP);
            totalScore = totalScore.add(singleScore);
        }
        BigDecimal avgScore = totalScore.divide(BigDecimal.valueOf(allReviews.size()), 2, RoundingMode.HALF_UP);
        if (avgScore.compareTo(BigDecimal.valueOf(5L)) > 0) {
            avgScore = BigDecimal.valueOf(5L);
        }
        if ((instructor = (Instructor)this.instructorMapper.selectById((Serializable)instructorId)) != null) {
            instructor.setRatingScore(avgScore);
            this.instructorMapper.updateById(instructor);
        }
    }

    public void interact(Integer id, Integer type) {
        CourseReview review = (CourseReview)this.courseReviewMapper.selectById((Serializable)id);
        if (review == null) {
            return;
        }
        if (type == 1) {
            int count = review.getRecommendCount() != null ? review.getRecommendCount() : 0;
            review.setRecommendCount(Integer.valueOf(count + 1));
        } else if (type == 2) {
            int count = review.getLikeCount() != null ? review.getLikeCount() : 0;
            review.setLikeCount(Integer.valueOf(count + 1));
        }
        review.setUpdateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        this.courseReviewMapper.updateById(review);
    }

    public void reply(Integer instructorId, Integer id, String content) {
        CourseReview review = (CourseReview)this.courseReviewMapper.selectById((Serializable)id);
        if (review == null) {
            throw new RuntimeException("\u8bc4\u4ef7\u4e0d\u5b58\u5728");
        }
        // \u53ea\u5141\u8bb8\u88ab\u8bc4\u4ef7\u7684\u6559\u7ec3\u672c\u4eba\u56de\u590d\uff0c\u907f\u514d\u4efb\u610f\u6559\u7ec3 token \u4ee3\u56de\u590d\u522b\u4eba\u8bc4\u4ef7\u3002
        if (instructorId != null && review.getInstructorId() != null
                && !instructorId.equals(review.getInstructorId())) {
            throw new OperateException("\u65e0\u6743\u56de\u590d\u8be5\u8bc4\u4ef7");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("\u56de\u590d\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (content.length() > 500) {
            content = content.substring(0, 500);
        }
        review.setReplyContent(content);
        review.setReplyTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        review.setUpdateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        this.courseReviewMapper.updateById(review);
    }

    /** \u8bc4\u4ef7\u524d\u6821\u9a8c\u771f\u5b9e\u4e0a\u8bfe\uff1a\u9884\u7ea6\u72b6\u6001=10(\u5df2\u4e0a\u8bfe) \u6216\u5df2\u6709\u6d88\u8bfe\u6d41\u6c34\u3002 */
    private boolean hasAttendedLesson(Integer userId, Integer courseClassId, Integer lessonNo) {
        Long arrived = this.courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>()
                .eq("user_id", (Object)userId)
                .eq("course_class_id", (Object)courseClassId)
                .eq("lesson_no", (Object)lessonNo)
                .eq("status", (Object)10));
        if (arrived != null && arrived > 0L) {
            return true;
        }
        Long consumed = this.courseConsumeLogMapper.selectCount(new QueryWrapper<CourseConsumeLog>()
                .eq("user_id", (Object)userId)
                .eq("course_class_id", (Object)courseClassId)
                .eq("lesson_no", (Object)lessonNo));
        return consumed != null && consumed > 0L;
    }

    private Integer parseRating(Object value) {
        Integer rating = this.parseOptionalInt(value);
        if (rating == null || rating < 1 || rating > 5) {
            throw new RuntimeException("\u8bc4\u5206\u5fc5\u987b\u4e3a 1-5 \u661f");
        }
        return rating;
    }

    private Integer parseRequiredInt(Object value, String message) {
        Integer parsed = this.parseOptionalInt(value);
        if (parsed == null) {
            throw new RuntimeException(message);
        }
        return parsed;
    }

    private Integer parseOptionalInt(Object value) {
        if (value == null) {
            return null;
        }
        try {
            String text = String.valueOf(value).trim();
            return text.isEmpty() ? null : Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** 评价维度。用 Java 8 兼容写法（生产运行时为 temurin:8-jre，不能用 Map.of）。 */
    private List<Map<String, Object>> dimensions(CourseReview r) {
        List<Map<String, Object>> dims = new ArrayList<Map<String, Object>>();
        dims.add(dimension("\u6559\u5b66\u8d28\u91cf", r.getRatingTeaching()));
        dims.add(dimension("\u8bfe\u5802\u6c1b\u56f4", r.getRatingAtmosphere()));
        dims.add(dimension("\u6559\u7ec3\u6001\u5ea6", r.getRatingAttitude()));
        return dims;
    }

    private Map<String, Object> dimension(String label, Integer score) {
        Map<String, Object> dim = new LinkedHashMap<String, Object>();
        dim.put("label", label);
        dim.put("score", score != null ? score : 0);
        return dim;
    }
}
