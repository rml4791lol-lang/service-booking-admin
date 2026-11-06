/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.binarywang.wx.miniapp.api.WxMaService
 *  cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.InstructorLeave
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.mapper.InstructorLeaveMapper
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.common.util.MessageUtil
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.InstructorController
 *  com.mdd.front.service.ICourseReviewService
 *  com.mdd.front.service.IInstructorCancelFrontService
 *  com.mdd.front.service.IInstructorService
 *  com.mdd.front.service.ILeaveService
 *  com.mdd.front.service.IMessageService
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.validate.instructor.InstructorLeaveApplyValidate
 *  com.mdd.front.validate.instructor.InstructorProfileUpdateValidate
 *  com.mdd.front.validate.leave.LeaveAuditValidate
 *  com.mdd.front.vo.leave.LeaveListVo
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.InstructorLeave;
import com.mdd.common.entity.user.User;
import com.mdd.common.mapper.InstructorLeaveMapper;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.common.util.MessageUtil;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.ICourseCatalogService;
import com.mdd.front.service.ICourseReviewService;
import com.mdd.front.service.IInstructorCancelFrontService;
import com.mdd.front.service.IInstructorService;
import com.mdd.front.service.ILeaveService;
import com.mdd.front.service.IMessageService;
import com.mdd.common.util.ParamUtils;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.validate.instructor.InstructorLeaveApplyValidate;
import com.mdd.front.validate.instructor.InstructorProfileUpdateValidate;
import com.mdd.front.validate.leave.LeaveAuditValidate;
import com.mdd.front.vo.leave.LeaveListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/instructor"})
@Api(tags={"\u6559\u7ec3\u63a5\u53e3(\u524d\u53f0)"})
public class InstructorController {
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ILeaveService iLeaveService;
    @Resource
    IInstructorService instructorService;
    @Resource
    private InstructorLeaveMapper instructorLeaveMapper;
    @Resource
    ICourseReviewService iCourseReviewService;
    @Resource
    IInstructorCancelFrontService instructorCancelFrontService;
    @Resource
    IMessageService messageService;
    @Resource
    ICourseCatalogService iCourseCatalogService;

    @NotLogin
    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u6559\u7ec3\u8be6\u60c5")
    public AjaxResult<Object> detail(@RequestParam(value="id") Integer id) {
        Map<String, Object> map = this.iCourseCatalogService.coachDetail(id);
        if (map == null) {
            return AjaxResult.failed((String)"\u6559\u7ec3\u4e0d\u5b58\u5728");
        }
        return AjaxResult.success(map);
    }

    @NotLogin
    @GetMapping(value={"/list"})
    @ApiOperation(value="\u6559\u7ec3\u5217\u8868")
    public AjaxResult<List<Map<String, Object>>> list(
            @RequestParam(value="areaId", required=false) Integer areaId,
            @RequestParam(value="categoryId", required=false) Integer categoryId,
            @RequestParam(value="keyword", required=false) String keyword) {
        return AjaxResult.success(this.iCourseCatalogService.coaches(areaId, categoryId, keyword));
    }

    @PostMapping(value={"/apply"})
    @ApiOperation(value="\u63d0\u4ea4\u6559\u7ec3\u7533\u8bf7")
    public AjaxResult<Object> apply(@RequestBody Map<String, String> params) {
        String experience;
        Integer userId = LikeFrontThreadLocal.getUserId();
        String name = params.get("name") == null ? "" : params.get("name").trim();
        String phone = params.get("phone") == null ? "" : params.get("phone").trim();
        String idCard = params.get("idCard") == null ? "" : params.get("idCard").trim();
        String certImages = params.get("certImages") == null ? "" : params.get("certImages").trim();
        String string = experience = params.get("experience") == null ? "" : params.get("experience").trim();
        if (name.isEmpty()) {
            return AjaxResult.failed((String)"\u8bf7\u8f93\u5165\u6559\u7ec3\u6635\u79f0");
        }
        if (phone.isEmpty()) {
            return AjaxResult.failed((String)"\u8bf7\u8f93\u5165\u8054\u7cfb\u7535\u8bdd");
        }
        Instructor exist = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("phone", (Object)phone).last("LIMIT 1"));
        if (exist != null) {
            return AjaxResult.failed((String)"\u5df2\u7ecf\u7533\u8bf7\uff0c\u8bf7\u52ff\u91cd\u590d\u7533\u8bf7");
        }
        QueryWrapper<Instructor> checkQw = new QueryWrapper<Instructor>();
        checkQw.eq("phone", (Object)phone).eq("status", (Object)2);
        Long pendingCount = this.instructorMapper.selectCount((Wrapper)checkQw);
        if (pendingCount != null && pendingCount > 0L) {
            return AjaxResult.failed((String)"\u60a8\u5df2\u6709\u4e00\u6761\u5f85\u5ba1\u6838\u7684\u7533\u8bf7\uff0c\u8bf7\u8010\u5fc3\u7b49\u5f85");
        }
        if (idCard.isEmpty()) {
            return AjaxResult.failed((String)"\u8bf7\u8f93\u5165\u8eab\u4efd\u8bc1\u53f7");
        }
        if (!idCard.matches("^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$")) {
            return AjaxResult.failed((String)"\u8eab\u4efd\u8bc1\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e");
        }
        Instructor app = new Instructor();
        app.setName(name);
        app.setPhone(phone);
        app.setIdCard(idCard);
        app.setCertImages(certImages);
        app.setIntroduction(experience);
        app.setStatus(Integer.valueOf(2));
        app.setGender(Integer.valueOf(1));
        app.setSpecialty("");
        app.setExperienceYears(Integer.valueOf(0));
        app.setClassCount(Integer.valueOf(0));
        app.setHourlyRate(BigDecimal.ZERO);
        app.setRatingScore(BigDecimal.ZERO);
        app.setCreateTime(new Date());
        app.setUpdateTime(new Date());
        this.instructorMapper.insert(app);
        MessageUtil.send((int)4, (String)"\u6559\u7ec3\u7533\u8bf7\u63d0\u4ea4\u6210\u529f", (String)"\u60a8\u7684\u6559\u7ec3\u5165\u9a7b\u7533\u8bf7\u5df2\u6210\u529f\u63d0\u4ea4\uff0c\u8bf7\u8010\u5fc3\u7b49\u5f85\u5de5\u4f5c\u4eba\u5458\u5ba1\u6838\u3002", (Integer)0, (String)"system", (String)"\u7cfb\u7edf\u901a\u77e5", (Integer)userId, (String)"user");
        return AjaxResult.success();
    }

    @PostMapping(value={"/cancelApply"})
    @ApiOperation(value="\u64a4\u9500\u5f85\u5ba1\u6838\u7684\u6559\u7ec3\u7533\u8bf7")
    public AjaxResult<Object> cancelApply() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        User user = (User)this.userMapper.selectById((Serializable)userId);
        if (user != null && user.getMobile() != null && !user.getMobile().isEmpty()) {
            QueryWrapper<Instructor> qw = new QueryWrapper<Instructor>();
            qw.eq("phone", (Object)user.getMobile()).eq("status", (Object)2);
            this.instructorMapper.delete((Wrapper)qw);
        }
        return AjaxResult.success();
    }

    @GetMapping(value={"/applyStatus"})
    @ApiOperation(value="\u67e5\u8be2\u6559\u7ec3\u7533\u8bf7\u72b6\u6001")
    public AjaxResult<Object> applyStatus(String phone) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        User user = (User)this.userMapper.selectById((Serializable)userId);
        String queryPhone = phone;
        if (queryPhone == null || queryPhone.isEmpty()) {
            String string = queryPhone = user != null ? user.getMobile() : "";
        }
        if (queryPhone == null || queryPhone.isEmpty()) {
            LinkedHashMap<String, Boolean> empty = new LinkedHashMap<String, Boolean>();
            empty.put("hasApply", false);
            return AjaxResult.success(empty);
        }
        QueryWrapper<Instructor> qw = new QueryWrapper<Instructor>();
        qw.eq("phone", (Object)queryPhone).orderByDesc("id").last("LIMIT 1");
        Instructor app = (Instructor)this.instructorMapper.selectOne((Wrapper)qw);
        if (app == null) {
            LinkedHashMap<String, Boolean> empty = new LinkedHashMap<String, Boolean>();
            empty.put("hasApply", false);
            return AjaxResult.success(empty);
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("hasApply", true);
        map.put("id", app.getId());
        int applyStatus = 2;
        String statusText = "\u5df2\u62d2\u7edd";
        if (app.getStatus() != null) {
            if (app.getStatus() == 2) {
                applyStatus = 0;
                statusText = "\u5ba1\u6838\u4e2d";
            } else if (app.getStatus() == 1) {
                applyStatus = 1;
                statusText = "\u5df2\u901a\u8fc7";
            }
        }
        map.put("applyStatus", applyStatus);
        map.put("statusText", statusText);
        map.put("auditRemark", "");
        map.put("idCard", app.getIdCard());
        map.put("certImages", app.getCertImages() == null ? "" : app.getCertImages());
        map.put("experience", app.getIntroduction());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("createTime", app.getCreateTime() != null ? sdf.format(app.getCreateTime()) : "");
        map.put("updateTime", app.getUpdateTime() != null ? sdf.format(app.getUpdateTime()) : "");
        return AjaxResult.success(map);
    }

    @GetMapping(value={"/home"})
    @ApiOperation(value="\u6559\u7ec3\u5de5\u4f5c\u53f0\u9996\u9875\u805a\u5408\u63a5\u53e3")
    public AjaxResult<Object> home() {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> result = this.instructorService.getHomeData(instructorId);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/profile"})
    @ApiOperation(value="\u6559\u7ec3\u4e2a\u4eba\u4e2d\u5fc3")
    public AjaxResult<Object> profile() {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> result = this.instructorService.getProfile(instructorId);
        return AjaxResult.success(result);
    }

    @PostMapping(value={"/updateAvatar"})
    @ApiOperation(value="\u66f4\u65b0\u6559\u7ec3\u5934\u50cf")
    public AjaxResult<Object> updateAvatar(@RequestBody Map<String, String> params) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        String avatar = params.get("avatar");
        if (avatar == null || avatar.trim().isEmpty()) {
            return AjaxResult.failed((String)"\u5934\u50cf\u5730\u5740\u4e0d\u80fd\u4e3a\u7a7a");
        }
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        instructor.setAvatar(UrlUtils.toRelativeUrl((String)avatar));
        instructor.setUpdateTime(new Date());
        this.instructorMapper.updateById(instructor);
        return AjaxResult.success();
    }

    @PostMapping(value={"/updateProfile"})
    @ApiOperation(value="\u6559\u7ec3\u4fee\u6539\u5b8c\u5584\u8d44\u6599")
    public AjaxResult<Object> updateProfile(@Validated @RequestBody InstructorProfileUpdateValidate validate) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        this.instructorService.updateProfile(instructorId, validate);
        return AjaxResult.success();
    }

    @GetMapping(value={"/students"})
    @ApiOperation(value="\u6559\u7ec3\u7aef\u6211\u7684\u5b66\u5458\u5217\u8868")
    public AjaxResult<Object> students(@RequestParam(value="pageNo", defaultValue="1") Integer pageNo, @RequestParam(value="pageSize", defaultValue="10") Integer pageSize, @RequestParam(value="keyword", required=false) String keyword) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> result = this.instructorService.getStudents(instructorId, pageNo, pageSize, keyword);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/students/account"})
    @ApiOperation(value="\u6559\u7ec3\u7aef\u5b66\u5458\u5ba2\u6237\u5bf9\u8d26")
    public AjaxResult<Object> studentAccount(@RequestParam(value="userId") Integer userId) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> result = this.instructorService.getStudentAccount(instructorId, userId);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/students/lessonRecords"})
    @ApiOperation(value="\u6559\u7ec3\u7aef\u5b66\u5458\u4e0a\u8bfe\u660e\u7ec6")
    public AjaxResult<Object> studentLessonRecords(@RequestParam(value="userId") Integer userId, @RequestParam(value="courseClassId", required=false) Integer courseClassId, @RequestParam(value="pageNo", defaultValue="1") Integer pageNo, @RequestParam(value="pageSize", defaultValue="20") Integer pageSize) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> result = this.instructorService.getStudentLessonRecords(instructorId, userId, courseClassId, pageNo, pageSize);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/schedule"})
    @ApiOperation(value="\u6559\u7ec3\u8bfe\u7a0b\u65e5\u5386")
    public AjaxResult<Object> schedule(@RequestParam(value="date", required=false) String dateStr, @RequestParam(value="pageNo", defaultValue="1") Integer pageNo, @RequestParam(value="pageSize", defaultValue="10") Integer pageSize) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> result = this.instructorService.getSchedule(instructorId, dateStr, pageNo, pageSize);
        return AjaxResult.success(result);
    }

    @PostMapping(value={"/coachCheckIn"})
    @ApiOperation(value="\u6559\u7ec3\u7b7e\u5230")
    public AjaxResult<Object> coachCheckIn(@RequestBody Map<String, Object> params) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u8bf7\u5148\u767b\u5f55");
        }
        Integer lessonId = ParamUtils.optionalInt(params.get("lessonId"), "\u8bfe\u6b21ID");
        Integer classId = ParamUtils.optionalInt(params.get("classId"), "\u73ed\u7ea7ID");
        QueryWrapper<InstructorLeave> leaveQw = new QueryWrapper<InstructorLeave>();
        long now = System.currentTimeMillis() / 1000L;
        leaveQw.eq("instructor_id", (Object)instructorId).eq("status", (Object)1).isNull("delete_time").le("start_time", (Object)now).ge("end_time", (Object)now).last("limit 1");
        InstructorLeave leave = (InstructorLeave)this.instructorLeaveMapper.selectOne((Wrapper)leaveQw);
        if (leave != null) {
            return AjaxResult.failed((String)"\u60a8\u5f53\u524d\u5904\u4e8e\u8bf7\u5047\u72b6\u6001\uff0c\u65e0\u6cd5\u7b7e\u5230");
        }
        this.instructorService.coachCheckIn(instructorId, lessonId, classId);
        return AjaxResult.success();
    }

    @GetMapping(value={"/qrcode"})
    @ApiOperation(value="\u83b7\u53d6\u6559\u7ec3\u4e13\u5c5e\u5c0f\u7a0b\u5e8f\u7801")
    public AjaxResult<Object> qrcode() {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        try {
            Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)instructorId);
            String coachName = instructor != null ? instructor.getName() : "";
            WxMaService wxMaService = WxMnpDriver.mnp();
            WxMaCodeLineColor lineColor = new WxMaCodeLineColor("26", "70", "107");
            byte[] qrcodeBytes = wxMaService.getQrcodeService().createWxaCodeUnlimitBytes("cName=" + coachName, "pages/search/search", false, "release", 430, true, lineColor, false);
            String base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(qrcodeBytes);
            LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
            result.put("qrcode", base64);
            return AjaxResult.success(result);
        }
        catch (Exception e) {
            return AjaxResult.failed((String)("\u751f\u6210\u5c0f\u7a0b\u5e8f\u7801\u5931\u8d25: " + e.getMessage()));
        }
    }

    @GetMapping(value={"/report"})
    @ApiOperation(value="\u83b7\u53d6\u62a5\u8868\u6570\u636e")
    public AjaxResult<Object> report() {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> data = this.instructorService.getReportData(instructorId);
        return AjaxResult.success(data);
    }

    @PostMapping(value={"/leave/apply"})
    @ApiOperation(value="\u6559\u7ec3\u63d0\u4ea4\u8bf7\u5047\u7533\u8bf7")
    public AjaxResult<Object> applyLeave(@Validated @RequestBody InstructorLeaveApplyValidate validate) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u8bf7\u5148\u8ba4\u8bc1\u4e3a\u6559\u7ec3");
        }
        this.instructorService.applyLeave(instructorId, validate);
        return AjaxResult.success();
    }

    @GetMapping(value={"/leave/list"})
    @ApiOperation(value="\u6559\u7ec3\u8bf7\u5047\u8bb0\u5f55")
    public AjaxResult<Object> leaveList(@Validated PageValidate pageValidate,
                                                                 @RequestParam(value="status", required=false) Integer status) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed("\u8bf7\u5148\u767b\u5f55\u6559\u7ec3\u8d26\u53f7");
        }
        QueryWrapper<InstructorLeave> qw = new QueryWrapper<InstructorLeave>()
                .eq("instructor_id", (Object)instructorId)
                .isNull("delete_time")
                .orderByDesc("id");
        if (status != null && status >= 0) {
            qw.eq("status", (Object)status);
        }
        IPage<InstructorLeave> page = this.instructorLeaveMapper.selectPage(
                (IPage)new Page((long)pageValidate.getPage_no().intValue(), (long)pageValidate.getPage_size().intValue()),
                (Wrapper)qw);
        String[] statusLabels = new String[]{"\u5f85\u5ba1\u6279", "\u5df2\u901a\u8fc7", "\u5df2\u62d2\u7edd", "\u5df2\u9500\u5047", "\u5df2\u9500\u5047"};
        java.util.ArrayList<Map<String, Object>> lists = new java.util.ArrayList<Map<String, Object>>();
        for (InstructorLeave leave : page.getRecords()) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", leave.getId());
            item.put("startTime", leave.getStartTime());
            item.put("endTime", leave.getEndTime());
            item.put("reason", leave.getReason());
            item.put("detail", leave.getDetail());
            item.put("status", leave.getStatus());
            int statusValue = leave.getStatus() == null ? 0 : leave.getStatus().intValue();
            item.put("statusDesc", statusValue >= 0 && statusValue < statusLabels.length ? statusLabels[statusValue] : "\u672a\u77e5");
            item.put("auditName", leave.getAuditName() != null ? leave.getAuditName() : "");
            item.put("auditRemark", leave.getAuditRemark() != null ? leave.getAuditRemark() : "");
            item.put("auditTime", leave.getAuditTime() != null ? leave.getAuditTime() : 0L);
            item.put("createTime", leave.getCreateTime() != null ? leave.getCreateTime() : 0L);
            lists.add(item);
        }
        return AjaxResult.success(PageResult.iPageHandle((Long)page.getTotal(), (Long)page.getCurrent(), (Long)page.getSize(), lists));
    }

    @GetMapping(value={"/leave/check"})
    @ApiOperation(value="\u67e5\u8be2\u6559\u7ec3\u5f53\u524d(\u6216\u5f53\u5929)\u662f\u5426\u6709\u8bf7\u5047\u8bb0\u5f55")
    public AjaxResult<InstructorLeave> checkLeave() {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.success((InstructorLeave)null);
        }
        QueryWrapper<Instructor> qw = new QueryWrapper<Instructor>();
        long now = System.currentTimeMillis() / 1000L;
        qw.eq("instructor_id", (Object)instructorId).ne("status", (Object)2).ne("status", (Object)4).isNull("delete_time").le("start_time", (Object)now).ge("end_time", (Object)now).last("limit 1");
        InstructorLeave leave = (InstructorLeave)this.instructorLeaveMapper.selectOne((Wrapper)qw);
        return AjaxResult.success(leave);
    }

    @PostMapping(value={"/leave/cancel"})
    @ApiOperation(value="\u6559\u7ec3\u9500\u5047")
    public AjaxResult<Object> cancelLeave() {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u672a\u767b\u5f55");
        }
        this.instructorService.cancelLeave(instructorId);
        return AjaxResult.success();
    }

    @GetMapping(value={"/review/summary"})
    @ApiOperation(value="\u6559\u7ec3\u7aef\u6559\u5b66\u8bc4\u4ef7\u6c47\u603b")
    public AjaxResult<Object> reviewSummary(@RequestParam(value="pageNo", defaultValue="1") Integer pageNo, @RequestParam(value="pageSize", defaultValue="5") Integer pageSize, @RequestParam(value="filter", required=false, defaultValue="all") String filter) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> data = this.iCourseReviewService.instructorReviewSummary(instructorId, pageNo, pageSize, filter);
        return AjaxResult.success(data);
    }

    @PostMapping(value={"/cancel/apply"})
    @ApiOperation(value="\u6559\u7ec3\u63d0\u4ea4\u6ce8\u9500\u7533\u8bf7")
    public AjaxResult<Object> cancelApply(@RequestBody Map<String, Object> params) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u8bf7\u5148\u767b\u5f55\u6559\u7ec3\u8d26\u53f7");
        }
        this.instructorCancelFrontService.apply(instructorId, params);
        return AjaxResult.success();
    }

    @GetMapping(value={"/cancel/status"})
    @ApiOperation(value="\u67e5\u8be2\u6559\u7ec3\u6ce8\u9500\u7533\u8bf7\u72b6\u6001")
    public AjaxResult<Object> cancelStatus() {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        Map<String, Object> result = this.instructorCancelFrontService.latestStatus(instructorId);
        return AjaxResult.success(result);
    }

    @PostMapping(value={"/message/send"})
    @ApiOperation(value="\u6559\u7ec3\u53d1\u9001\u6d88\u606f(\u5982\u7533\u8bf7\u5f00\u73ed)")
    public AjaxResult<Object> sendMessage(@RequestBody Map<String, Object> params) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u8bf7\u5148\u767b\u5f55\u6559\u7ec3\u8d26\u53f7");
        }
        this.messageService.send(instructorId, "instructor", params);
        return AjaxResult.success();
    }

    @GetMapping(value={"/attendance"})
    @ApiOperation(value="\u83b7\u53d6\u7b7e\u5230\u9875\u9762\u6570\u636e")
    public AjaxResult<Object> getAttendance(@RequestParam(value="classId", required=false) String classId,
                                            @RequestParam(value="date", required=false) String date,
                                            @RequestParam(value="lessonId", required=false) String lessonId) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u8bf7\u5148\u767b\u5f55\u6559\u7ec3\u8d26\u53f7!");
        }
        Integer parsedClassId = ParamUtils.requiredInt(classId, "\u73ed\u7ea7ID");
        String parsedDate = ParamUtils.optionalDate(date, "\u4e0a\u8bfe\u65e5\u671f");
        Integer parsedLessonId = ParamUtils.optionalInt(lessonId, "\u8bfe\u6b21ID");
        Map<String, Object> result = this.instructorService.getAttendanceData(instructorId, parsedClassId, parsedDate, parsedLessonId);
        return AjaxResult.success(result);
    }

    @PostMapping(value={"/attendance/checkIn"})
    @ApiOperation(value="\u7b7e\u5230/\u6539\u72b6\u6001")
    public AjaxResult<Object> checkIn(@RequestBody Map<String, Object> params) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u8bf7\u5148\u767b\u5f55\u6559\u7ec3\u8d26\u53f7");
        }
        QueryWrapper<InstructorLeave> leaveQw1 = new QueryWrapper<InstructorLeave>();
        long now1 = System.currentTimeMillis() / 1000L;
        leaveQw1.eq("instructor_id", (Object)instructorId).eq("status", (Object)1).isNull("delete_time").le("start_time", (Object)now1).ge("end_time", (Object)now1).last("limit 1");
        if (this.instructorLeaveMapper.selectOne((Wrapper)leaveQw1) != null) {
            return AjaxResult.failed((String)"\u60a8\u5f53\u524d\u5904\u4e8e\u8bf7\u5047\u72b6\u6001\uff0c\u65e0\u6cd5\u70b9\u540d");
        }
        Integer classId = ParamUtils.requiredInt(params.get("classId"), "\u73ed\u7ea7ID");
        Integer userId = ParamUtils.requiredInt(params.get("userId"), "\u5b66\u5458\u5bb6\u957fID");
        Integer studentId = ParamUtils.optionalInt(params.get("studentId"), "\u5b66\u5458ID");
        Integer status = ParamUtils.requiredInt(params.get("status"), "\u70b9\u540d\u72b6\u6001");
        String date = ParamUtils.optionalDate(params.get("date"), "\u4e0a\u8bfe\u65e5\u671f");
        Integer lessonId = ParamUtils.optionalInt(params.get("lessonId"), "\u8bfe\u6b21ID");
        Integer targetLessonNo = ParamUtils.optionalInt(params.get("lessonNo"), "\u8bfe\u6b21\u53f7");
        // #170：返回点名归属明细；无预约现场补建时 onSite=true（现场单显式标记）。
        Map<String, Object> checkinResult = this.instructorService.changeAttendanceStatus(instructorId, classId, userId, studentId, status, date, lessonId, targetLessonNo);
        return AjaxResult.success(checkinResult);
    }

    @PostMapping(value={"/attendance/complete"})
    @ApiOperation(value="\u7ed9\u73ed\u7ea7\u6216\u8005\u79c1\u6559\u7ed3\u8bfe")
    public AjaxResult<Object> completeLesson(@RequestBody Map<String, Object> params) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u8bf7\u5148\u8ba4\u8bc1\u4e3a\u6559\u7ec3");
        }
        QueryWrapper<InstructorLeave> leaveQw2 = new QueryWrapper<InstructorLeave>();
        long now2 = System.currentTimeMillis() / 1000L;
        leaveQw2.eq("instructor_id", (Object)instructorId).eq("status", (Object)1).isNull("delete_time").le("start_time", (Object)now2).ge("end_time", (Object)now2).last("limit 1");
        if (this.instructorLeaveMapper.selectOne((Wrapper)leaveQw2) != null) {
            return AjaxResult.failed((String)"\u60a8\u5f53\u524d\u5904\u4e8e\u8bf7\u5047\u72b6\u6001\uff0c\u65e0\u6cd5\u63d0\u4ea4\u8bfe\u5802\u603b\u7ed3");
        }
        Integer classId = ParamUtils.optionalInt(params.get("classId"), "\u73ed\u7ea7ID");
        String date = ParamUtils.optionalDate(params.get("date"), "\u4e0a\u8bfe\u65e5\u671f");
        Integer lessonId = ParamUtils.optionalInt(params.get("lessonId"), "\u8bfe\u6b21ID");
        Integer lessonNo = ParamUtils.optionalInt(params.get("lessonNo"), "\u8bfe\u6b21\u53f7");
        String summary = params.get("summary") == null ? "" : String.valueOf(params.get("summary"));
        String images = params.get("images") == null ? "" : String.valueOf(params.get("images"));
        this.instructorService.completeLesson(instructorId, classId, date, lessonId, lessonNo, summary, images);
        return AjaxResult.success();
    }

    @GetMapping(value={"/leave/classLeaves"})
    @ApiOperation(value="\u73ed\u7ea7\u8bf7\u5047\u8bb0\u5f55(\u6559\u7ec3\u7aef)")
    public AjaxResult<PageResult<LeaveListVo>> leaveClassLeaves(@Validated PageValidate pageValidate, @RequestParam(value="classId", required=false) Integer classId, @RequestParam(value="status", required=false) Integer status) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        PageResult list = this.iLeaveService.classLeaves(pageValidate, instructorId, classId, status);
        return AjaxResult.success(list);
    }

    @PostMapping(value={"/leave/audit"})
    @ApiOperation(value="\u6559\u7ec3\u5ba1\u6279\u8bf7\u5047")
    public AjaxResult<Object> leaveAudit(@Validated @RequestBody LeaveAuditValidate auditValidate) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        this.iLeaveService.audit(auditValidate, instructorId);
        return AjaxResult.success();
    }

    @GetMapping(value={"/completedLessons"})
    @ApiOperation(value="\u6559\u7ec3\u5df2\u5b8c\u6210\u8bfe\u8282\u5217\u8868")
    public AjaxResult<Object> completedLessons(@RequestParam(required=false) String keyword, @RequestParam(defaultValue="1") Integer pageNo, @RequestParam(defaultValue="10") Integer pageSize) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        List<Map<String, Object>> result = this.instructorService.completedLessonList(instructorId, keyword, pageNo, pageSize);
        return AjaxResult.success(result);
    }
}

