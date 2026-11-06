/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.controller.CourseReviewController
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseReview
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseReviewMapper
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseReview;
import com.mdd.common.entity.user.User;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseReviewMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.ParamUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/course_review"})
@Api(tags={"\u6559\u7ec3\u8bc4\u4ef7\u7ba1\u7406"})
public class CourseReviewController {
    @Resource
    CourseReviewMapper courseReviewMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    UserMapper userMapper;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u8bc4\u4ef7\u5217\u8868")
    public AjaxResult<Object> list(@RequestParam(defaultValue="1") Integer pageNo,
                                   @RequestParam(defaultValue="20") Integer pageSize,
                                   @RequestParam(required=false) Integer instructorId,
                                   @RequestParam(required=false) Integer courseClassId,
                                   @RequestParam(required=false) String keyword) {
        QueryWrapper<CourseReview> wrapper = new QueryWrapper<CourseReview>().isNull("delete_time").orderByDesc("id");
        if (instructorId != null && instructorId > 0) {
            wrapper.eq("instructor_id", (Object)instructorId);
        }
        if (courseClassId != null && courseClassId > 0) {
            wrapper.eq("course_class_id", (Object)courseClassId);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like("content", (Object)keyword.trim());
        }
        IPage<CourseReview> page = this.courseReviewMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), (Wrapper)wrapper);
        LinkedList<Map<String, Object>> lists = new LinkedList<Map<String, Object>>();
        for (CourseReview r : page.getRecords()) {
            lists.add(this.buildItem(r));
        }
        return AjaxResult.success(PageResult.iPageHandle((Long)page.getTotal(), (Long)page.getCurrent(), (Long)page.getSize(), lists));
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u8bc4\u4ef7\u8be6\u60c5")
    public AjaxResult<Object> detail(@RequestParam(value="id") Integer id) {
        CourseReview review = (CourseReview)this.courseReviewMapper.selectById((Serializable)id);
        if (review == null || review.getDeleteTime() != null) {
            return AjaxResult.failed("\u8bc4\u4ef7\u4e0d\u5b58\u5728");
        }
        return AjaxResult.success(this.buildItem(review));
    }

    private Map<String, Object> buildItem(CourseReview r) {
        LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
        item.put("id", r.getId());
        item.put("userId", r.getUserId());
        item.put("courseClassId", r.getCourseClassId());
        item.put("instructorId", r.getInstructorId());
        item.put("ratingTeaching", r.getRatingTeaching());
        item.put("ratingAtmosphere", r.getRatingAtmosphere());
        item.put("ratingAttitude", r.getRatingAttitude());
        item.put("content", r.getContent() != null ? r.getContent() : "");
        item.put("images", this.normalizeImages(r.getImages()));
        item.put("lessonNo", r.getLessonNo());
        item.put("createTime", r.getCreateTime() == null ? "" : TimeUtils.timestampToDate((Long)r.getCreateTime().longValue()));
        item.put("replyContent", r.getReplyContent() != null ? r.getReplyContent() : "");
        item.put("replyTime", r.getReplyTime() == null ? "" : TimeUtils.timestampToDate((Long)r.getReplyTime().longValue()));
        CourseClass cc = r.getCourseClassId() == null ? null : (CourseClass)this.courseClassMapper.selectById((Serializable)r.getCourseClassId());
        item.put("courseName", cc != null ? cc.getName() : "");
        Instructor instructor = r.getInstructorId() == null ? null : (Instructor)this.instructorMapper.selectById((Serializable)r.getInstructorId());
        item.put("instructorName", instructor != null ? instructor.getName() : "");
        User user = r.getUserId() == null ? null : (User)this.userMapper.selectById((Serializable)r.getUserId());
        item.put("userName", user != null ? (user.getNickname() != null ? user.getNickname() : user.getRealName()) : "");
        item.put("userMobile", user != null && user.getMobile() != null ? user.getMobile() : "");
        item.put("userAvatar", user != null && user.getAvatar() != null ? UrlUtils.toAdminAbsoluteUrl((String)user.getAvatar()) : "");
        return item;
    }

    private List<String> normalizeImages(String images) {
        if (images == null || images.trim().isEmpty()) {
            return new ArrayList<String>();
        }
        return Arrays.stream(images.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> UrlUtils.toAdminAbsoluteUrl((String)s))
                .collect(Collectors.toList());
    }

    @PostMapping(value={"/del"})
    @ApiOperation(value="\u5220\u9664\u8bc4\u4ef7")
    public AjaxResult<Object> del(@RequestBody Map<String, Object> params) {
        Integer id = ParamUtils.requiredInt(params.get("id"), "\u8bc4\u4ef7ID");
        CourseReview review = (CourseReview)this.courseReviewMapper.selectById((Serializable)id);
        if (review != null) {
            review.setDeleteTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
            this.courseReviewMapper.updateById(review);
        }
        return AjaxResult.success();
    }
}

