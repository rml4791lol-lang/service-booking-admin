package com.mdd.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.ILessonProfitShareService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.LessonProfitShareSearchValidate;
import com.mdd.admin.vo.finance.LessonProfitShareExportVo;
import com.mdd.admin.vo.finance.LessonProfitShareListVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.agent.Agent;
import com.mdd.common.entity.agent.AgentEarningLog;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.community.CommunityAgent;
import com.mdd.common.entity.community.CommunityEarningLog;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseLessonLog;
import com.mdd.common.entity.course.CourseManualConsume;
import com.mdd.common.entity.course.CourseManualLesson;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.instructor.InstructorAccountLog;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.agent.AgentEarningLogMapper;
import com.mdd.common.mapper.agent.AgentMapper;
import com.mdd.common.mapper.community.CommunityAgentMapper;
import com.mdd.common.mapper.community.CommunityEarningLogMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseManualConsumeMapper;
import com.mdd.common.mapper.course.CourseManualLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.instructor.InstructorAccountLogMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LessonProfitShareServiceImpl implements ILessonProfitShareService {
   private static final Pattern LESSON_NO_IN_REMARK = Pattern.compile("第(\\d+)节");
   @Resource
   CourseLessonMapper courseLessonMapper;
   @Resource
   CourseClassMapper courseClassMapper;
   @Resource
   CourseLessonLogMapper courseLessonLogMapper;
   @Resource
   AgentMapper agentMapper;
   @Resource
   CommunityMapper communityMapper;
   @Resource
   InstructorMapper instructorMapper;
   @Resource
   AgentEarningLogMapper agentEarningLogMapper;
   @Resource
   CommunityEarningLogMapper communityEarningLogMapper;
   @Resource
   CommunityAgentMapper communityAgentMapper;
   @Resource
   InstructorAccountLogMapper instructorAccountLogMapper;
   @Resource
   RechargeOrderMapper rechargeOrderMapper;
   @Resource
   CourseConsumeLogMapper courseConsumeLogMapper;
   @Resource
   UserCourseMapper userCourseMapper;
   @Resource
   CourseManualLessonMapper courseManualLessonMapper;
   @Resource
   CourseManualConsumeMapper courseManualConsumeMapper;

   public LessonProfitShareServiceImpl() {
   }

   private Community getCurrentCommunity() {
      Integer adminId = LikeAdminThreadLocal.getAdminId();
      return adminId == null
         ? null
         : (Community)this.communityMapper
            .selectOne(new QueryWrapper<Community>().eq("admin_id", adminId).isNull("delete_time").last("limit 1"));
   }

   @Override
   public PageResult<LessonProfitShareListVo> lists(PageValidate pageValidate, LessonProfitShareSearchValidate searchValidate) {
      Community scopeCommunity = this.getCurrentCommunity();
      List<CourseClass> classes = this.loadScopedClasses(searchValidate, scopeCommunity);
      if (classes.isEmpty()) {
         return this.emptyPage(pageValidate, scopeCommunity != null);
      } else {
         Map<Integer, CourseClass> classMap = classes.stream().collect(Collectors.toMap(CourseClass::getId, c -> (CourseClass)c, (a, b) -> a));
         List<Integer> classIds = new ArrayList<>(classMap.keySet());
         Integer statusFilter = 1;
         QueryWrapper<CourseLesson> lessonQw = new QueryWrapper<CourseLesson>();
         lessonQw.select(new String[]{"id", "class_id", "lesson_no", "lesson_date", "time_slot", "venue", "coach_id", "status"});
         lessonQw.in("class_id", classIds);
         lessonQw.eq("status", statusFilter);
         if (StringUtils.isNotEmpty(searchValidate.getStartDate())) {
            lessonQw.ge("lesson_date", searchValidate.getStartDate().trim());
         }

         if (StringUtils.isNotEmpty(searchValidate.getEndDate())) {
            lessonQw.le("lesson_date", searchValidate.getEndDate().trim());
         }

         lessonQw.orderByDesc("lesson_date").orderByDesc("id");
         List<CourseLesson> lessons = this.courseLessonMapper.selectList(lessonQw);
         List<CourseManualLesson> manualLessons = this.loadManualLessons(classIds, searchValidate);
         if (lessons.isEmpty() && manualLessons.isEmpty()) {
            return this.emptyPage(pageValidate, scopeCommunity != null);
         } else {
            LessonProfitShareServiceImpl.ShareContext ctx = this.buildShareContext(classMap, classIds, lessons, manualLessons);
            List<LessonProfitShareListVo> allRows = new ArrayList<>();

            for (CourseLesson lesson : lessons) {
               CourseClass cls = classMap.get(lesson.getClassId());
               if (cls != null) {
                  LessonProfitShareListVo vo = this.buildRow(lesson, cls, ctx);
                  if (statusFilter.equals(vo.getStatus())) {
                     allRows.add(vo);
                  }
               }
            }

            for (CourseManualLesson manualLesson : manualLessons) {
               CourseClass cls = classMap.get(manualLesson.getClassId());
               if (cls != null) {
                  allRows.add(this.buildManualRow(manualLesson, cls, ctx));
               }
            }

            allRows.sort((a, b) -> {
               int dateCompare = this.rowDateValue(b).compareTo((ChronoLocalDate)this.rowDateValue(a));
               return dateCompare != 0 ? dateCompare : this.nullToString(b.getLessonTime()).compareTo(this.nullToString(a.getLessonTime()));
            });
            Map<String, Object> summary = this.buildSummary(allRows);
            Map<String, Object> extend = new LinkedHashMap<>();
            extend.put("summary", summary);
            extend.put("isCommunityAdmin", scopeCommunity != null);
            int pageNo = pageValidate.getPage_no();
            int pageSize = pageValidate.getPage_size();
            int from = Math.max(0, (pageNo - 1) * pageSize);
            int to = Math.min(allRows.size(), from + pageSize);
            List<LessonProfitShareListVo> pageRows = from >= allRows.size() ? Collections.emptyList() : allRows.subList(from, to);
            return PageResult.iPageHandle((long)allRows.size(), (long)pageNo, (long)pageSize, pageRows, extend);
         }
      }
   }

   @Override
   public List<Map<String, Object>> communityOptions() {
      if (this.getCurrentCommunity() != null) {
         return Collections.emptyList();
      } else {
         List<Community> communities = this.communityMapper.selectList(new QueryWrapper<Community>().isNull("delete_time").orderByDesc("id"));
         List<Map<String, Object>> list = new ArrayList<>();

         for (Community community : communities) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", community.getId());
            item.put("name", community.getName());
            list.add(item);
         }

         return list;
      }
   }

   @Override
   public JSONObject getExportData(PageValidate pageValidate, LessonProfitShareSearchValidate searchValidate) {
      Integer limit = pageValidate.getPage_size();
      PageResult<LessonProfitShareListVo> pageResult = this.lists(pageValidate, searchValidate);
      return ToolUtils.getExportData(pageResult.getCount(), limit, searchValidate.getPage_start(), searchValidate.getPage_end(), "分账统计导出");
   }

   @Override
   public String export(LessonProfitShareSearchValidate searchValidate) {
      PageValidate pageValidate = new PageValidate();
      pageValidate.setPage_no(1);
      pageValidate.setPage_size(Integer.MAX_VALUE);
      List<LessonProfitShareListVo> rows = this.lists(pageValidate, searchValidate).getLists();
      if (StringUtils.isNotNull(searchValidate.getPage_type()) && searchValidate.getPage_type().equals(1)) {
         rows = this.sliceExportRows(rows, searchValidate);
      }

      List<LessonProfitShareExportVo> excelList = rows.stream().map(this::toExportVo).collect(Collectors.toList());
      String fileName = ToolUtils.safeExportFileName(searchValidate.getFile_name());
      String folderPath = "/excel/export/" + TimeUtils.timestampToDay(System.currentTimeMillis() / 1000L) + "/";
      String path = folderPath + fileName + ".xlsx";
      String filePath = YmlUtils.get("like.upload-directory") + path;
      File folder = new File(YmlUtils.get("like.upload-directory") + folderPath);
      if (!folder.exists() && !folder.mkdirs()) {
         throw new OperateException("创建文件夹失败");
      } else {
         ((ExcelWriterBuilder)EasyExcel.write(filePath).head(LessonProfitShareExportVo.class)).excelType(ExcelTypeEnum.XLSX).sheet("分账记录").doWrite(excelList);
         return UrlUtils.toAdminAbsoluteUrl(path);
      }
   }

   private List<LessonProfitShareListVo> sliceExportRows(List<LessonProfitShareListVo> rows, LessonProfitShareSearchValidate searchValidate) {
      if (rows != null && !rows.isEmpty()) {
         int pageSize = searchValidate.getPage_size() != null && searchValidate.getPage_size() > 0 ? searchValidate.getPage_size() : 20;
         int startPage = searchValidate.getPage_start() != null && searchValidate.getPage_start() > 0 ? searchValidate.getPage_start() : 1;
         int endPage = searchValidate.getPage_end() != null && searchValidate.getPage_end() >= startPage ? searchValidate.getPage_end() : startPage;
         int from = Math.max(0, (startPage - 1) * pageSize);
         int to = Math.min(rows.size(), endPage * pageSize);
         return from < rows.size() && from < to ? rows.subList(from, to) : Collections.emptyList();
      } else {
         return Collections.emptyList();
      }
   }

   private LessonProfitShareExportVo toExportVo(LessonProfitShareListVo row) {
      LessonProfitShareExportVo vo = new LessonProfitShareExportVo();
      vo.setLessonDate(row.getLessonDate());
      vo.setLessonTime(row.getLessonTime());
      vo.setVenue(row.getVenue());
      vo.setAgentName(row.getAgentName());
      vo.setAgentAmount(row.getAgentAmount());
      vo.setCommunityName(row.getCommunityName());
      vo.setCommunityAmount(row.getCommunityAmount());
      vo.setClassName(row.getClassName());
      vo.setInstructorName(row.getInstructorName());
      vo.setInstructorAmount(row.getInstructorAmount());
      vo.setLessonAmount(row.getLessonAmount());
      vo.setPlatformAmount(row.getPlatformAmount());
      vo.setStatusLabel(row.getStatusLabel());
      return vo;
   }

   private PageResult<LessonProfitShareListVo> emptyPage(PageValidate pageValidate, boolean isCommunityAdmin) {
      Map<String, Object> extend = new LinkedHashMap<>();
      extend.put("summary", this.buildSummary(Collections.emptyList()));
      extend.put("isCommunityAdmin", isCommunityAdmin);
      return PageResult.iPageHandle(
         0L, (long)pageValidate.getPage_no().intValue(), (long)pageValidate.getPage_size().intValue(), Collections.emptyList(), extend
      );
   }

   private List<CourseClass> loadScopedClasses(LessonProfitShareSearchValidate searchValidate, Community scopeCommunity) {
      QueryWrapper<CourseClass> classQw = new QueryWrapper<CourseClass>();
      classQw.eq("is_delete", 0);
      Integer scopeCommunityId = null;
      if (scopeCommunity != null) {
         scopeCommunityId = scopeCommunity.getId();
      } else if (searchValidate.getCommunityId() != null && searchValidate.getCommunityId() > 0) {
         scopeCommunityId = searchValidate.getCommunityId();
      }

      if (scopeCommunityId != null && scopeCommunityId > 0) {
         Set<Integer> scopedClassIds = this.loadCommunityScopedClassIds(scopeCommunityId);
         if (scopedClassIds.isEmpty()) {
            return Collections.emptyList();
         }

         classQw.in("id", scopedClassIds);
      }

      if (StringUtils.isNotEmpty(searchValidate.getClassName())) {
         classQw.like("name", searchValidate.getClassName().trim());
      }

      if (StringUtils.isNotEmpty(searchValidate.getAgentName())) {
         Set<Integer> agentClassIds = this.loadAgentScopedClassIdsByName(searchValidate.getAgentName().trim());
         if (agentClassIds.isEmpty()) {
            return Collections.emptyList();
         }

         classQw.in("id", agentClassIds);
      }

      return this.courseClassMapper.selectList(classQw);
   }

   private List<CourseManualLesson> loadManualLessons(List<Integer> classIds, LessonProfitShareSearchValidate searchValidate) {
      if (classIds != null && !classIds.isEmpty()) {
         QueryWrapper<CourseManualLesson> manualQw = new QueryWrapper<CourseManualLesson>();
         manualQw.select(
            new String[]{
               "id",
               "class_id",
               "lesson_date",
               "time_slot",
               "instructor_id",
               "instructor_name",
               "default_instructor_id",
               "venue",
               "fee_amount",
               "student_count",
               "status"
            }
         );
         manualQw.in("class_id", classIds);
         manualQw.eq("status", 1);
         if (StringUtils.isNotEmpty(searchValidate.getStartDate())) {
            manualQw.ge("lesson_date", searchValidate.getStartDate().trim());
         }

         if (StringUtils.isNotEmpty(searchValidate.getEndDate())) {
            manualQw.le("lesson_date", searchValidate.getEndDate().trim());
         }

         manualQw.orderByDesc("lesson_date").orderByDesc("id");
         return this.courseManualLessonMapper.selectList(manualQw);
      } else {
         return Collections.emptyList();
      }
   }

   private Set<Integer> loadCommunityScopedClassIds(Integer communityId) {
      Set<Integer> classIds = new HashSet<>();
      if (communityId != null && communityId > 0) {
         for (CourseClass cls : this.courseClassMapper
            .selectList(new QueryWrapper<CourseClass>().select(new String[]{"id"}).eq("community_id", communityId).eq("is_delete", 0))) {
            classIds.add(cls.getId());
         }

         List<Integer> agentIds = this.loadCommunityAgentIds(communityId);
         classIds.addAll(this.loadAgentScopedClassIds(agentIds));

         for (CommunityEarningLog log : this.communityEarningLogMapper
            .selectList(
               new QueryWrapper<CommunityEarningLog>().select(new String[]{"course_class_id"}).eq("community_id", communityId).isNull("delete_time")
            )) {
            if (log.getCourseClassId() != null && log.getCourseClassId() > 0) {
               classIds.add(log.getCourseClassId());
            }
         }

         return classIds;
      } else {
         return classIds;
      }
   }

   private List<Integer> loadCommunityAgentIds(Integer communityId) {
      if (communityId != null && communityId > 0) {
         List<CommunityAgent> relations = this.communityAgentMapper
            .selectList(new QueryWrapper<CommunityAgent>().select(new String[]{"agent_id"}).eq("community_id", communityId));
         List<Integer> agentIds = new ArrayList<>();

         for (CommunityAgent relation : relations) {
            if (relation.getAgentId() != null && relation.getAgentId() > 0 && !agentIds.contains(relation.getAgentId())) {
               agentIds.add(relation.getAgentId());
            }
         }

         return agentIds;
      } else {
         return Collections.emptyList();
      }
   }

   private Set<Integer> loadAgentScopedClassIdsByName(String agentName) {
      if (StringUtils.isEmpty(agentName)) {
         return Collections.emptySet();
      } else {
         List<Agent> agents = this.agentMapper
            .selectList(new QueryWrapper<Agent>().select(new String[]{"id"}).like("name", agentName).isNull("delete_time"));
         List<Integer> agentIds = agents.stream().<Integer>map(Agent::getId).collect(Collectors.toList());
         return this.loadAgentScopedClassIds(agentIds);
      }
   }

   private Set<Integer> loadAgentScopedClassIds(List<Integer> agentIds) {
      Set<Integer> classIds = new HashSet<>();
      if (agentIds != null && !agentIds.isEmpty()) {
         for (CourseClass cls : this.courseClassMapper
            .selectList(new QueryWrapper<CourseClass>().select(new String[]{"id"}).in("agent_id", agentIds).eq("is_delete", 0))) {
            classIds.add(cls.getId());
         }

         for (AgentEarningLog log : this.agentEarningLogMapper
            .selectList(new QueryWrapper<AgentEarningLog>().select(new String[]{"course_class_id"}).in("agent_id", agentIds).isNull("delete_time"))) {
            if (log.getCourseClassId() != null && log.getCourseClassId() > 0) {
               classIds.add(log.getCourseClassId());
            }
         }

         return classIds;
      } else {
         return classIds;
      }
   }

   private LessonProfitShareServiceImpl.ShareContext buildShareContext(
      Map<Integer, CourseClass> classMap, List<Integer> classIds, List<CourseLesson> lessons, List<CourseManualLesson> manualLessons
   ) {
      LessonProfitShareServiceImpl.ShareContext ctx = new LessonProfitShareServiceImpl.ShareContext();
      Set<Integer> agentIds = new HashSet<>();
      Set<Integer> communityIds = new HashSet<>();
      Set<Integer> coachIds = new HashSet<>();

      for (CourseClass cls : classMap.values()) {
         if (cls.getAgentId() != null && cls.getAgentId() > 0) {
            agentIds.add(cls.getAgentId());
         }

         if (cls.getCommunityId() != null && cls.getCommunityId() > 0) {
            communityIds.add(cls.getCommunityId());
         }

         if (cls.getInstructorId() != null && cls.getInstructorId() > 0) {
            coachIds.add(cls.getInstructorId());
         }
      }

      for (CourseLesson lesson : lessons) {
         if (lesson.getCoachId() != null && lesson.getCoachId() > 0) {
            coachIds.add(lesson.getCoachId());
         }
      }

      for (CourseManualLesson manualLesson : manualLessons) {
         if (manualLesson.getInstructorId() != null && manualLesson.getInstructorId() > 0) {
            coachIds.add(manualLesson.getInstructorId());
         }
      }

      ctx.classCommunityFromEarningLog = this.loadClassCommunityFromEarningLog(classIds);
      ctx.classAgentFromEarningLog = this.loadClassAgentFromEarningLog(classIds);
      Set<Integer> allAgentIds = new HashSet<>(agentIds);
      allAgentIds.addAll(ctx.classAgentFromEarningLog.values());
      ctx.agentCommunityMap = this.loadAgentCommunityMap(allAgentIds);
      Set<Integer> allCommunityIds = new HashSet<>(communityIds);
      allCommunityIds.addAll(ctx.classCommunityFromEarningLog.values());

      for (List<Integer> ids : ctx.agentCommunityMap.values()) {
         allCommunityIds.addAll(ids);
      }

      this.loadCommunityNames(allCommunityIds, ctx);
      ctx.communityAgentMap = this.loadCommunityAgentMap(allCommunityIds);

      for (List<Integer> ids : ctx.communityAgentMap.values()) {
         allAgentIds.addAll(ids);
      }

      this.loadAgentNames(allAgentIds, ctx);
      if (!coachIds.isEmpty()) {
         for (Instructor instructor : this.instructorMapper.selectBatchIds(coachIds)) {
            if (instructor != null) {
               ctx.instructorNameMap.put(instructor.getId(), instructor.getName());
            }
         }
      }

      List<CourseLessonLog> lessonLogs = this.courseLessonLogMapper
         .selectList(new QueryWrapper<CourseLessonLog>().in("course_class_id", classIds).isNull("delete_time"));

      for (CourseLessonLog log : lessonLogs) {
         ctx.lessonLogMap.put(this.logKey(log.getCourseClassId(), log.getLessonNo()), log);
      }

      ctx.agentPerLessonMap = this.buildPerLessonShare(classIds, classMap, true);
      ctx.communityPerLessonMap = this.buildPerLessonShare(classIds, classMap, false);
      Set<Integer> lessonLogIds = lessonLogs.stream().<Integer>map(CourseLessonLog::getId).collect(Collectors.toSet());
      QueryWrapper<InstructorAccountLog> logQw = new QueryWrapper<InstructorAccountLog>();
      logQw.eq("type", 1);
      logQw.and(w -> {
         w.in("relation_id", classIds);
         if (!lessonLogIds.isEmpty()) {
            w.or().in("relation_id", lessonLogIds);
         }
      });
      ctx.instructorLogs = this.instructorAccountLogMapper.selectList(logQw);
      ctx.lessonAmountMap = this.buildLessonAmountMap(lessons, classMap);
      ctx.manualLessonAmountMap = this.buildManualLessonAmountMap(manualLessons, classMap);
      return ctx;
   }

   private Map<Integer, BigDecimal> buildPerLessonShare(List<Integer> classIds, Map<Integer, CourseClass> classMap, boolean agentShare) {
      Map<Integer, BigDecimal> result = new HashMap<>();

      for (Integer classId : classIds) {
         CourseClass cls = classMap.get(classId);
         if (cls != null) {
            BigDecimal total = BigDecimal.ZERO;
            int paidLessons = 0;
            List<Integer> orderIds = new ArrayList<>();
            if (agentShare) {
               for (AgentEarningLog log : this.agentEarningLogMapper
                  .selectList(new QueryWrapper<AgentEarningLog>().eq("course_class_id", classId).isNull("delete_time"))) {
                  if (log.getEarningAmount() != null) {
                     total = total.add(log.getEarningAmount());
                  }

                  if (log.getOrderId() != null && log.getOrderId() > 0) {
                     orderIds.add(log.getOrderId());
                  }
               }
            } else {
               for (CommunityEarningLog log : this.communityEarningLogMapper
                  .selectList(new QueryWrapper<CommunityEarningLog>().eq("course_class_id", classId).isNull("delete_time"))) {
                  if (log.getEarningAmount() != null) {
                     total = total.add(log.getEarningAmount());
                  }

                  if (log.getOrderId() != null && log.getOrderId() > 0) {
                     orderIds.add(log.getOrderId());
                  }
               }
            }

            if (total.compareTo(BigDecimal.ZERO) > 0) {
               Map<Integer, Integer> orderLessonCountMap = this.loadOrderLessonCountMap(orderIds);

               for (Integer orderId : orderIds) {
                  Integer lessonCount = orderLessonCountMap.get(orderId);
                  if (lessonCount != null && lessonCount > 0) {
                     paidLessons += lessonCount;
                  }
               }

               boolean legacySingleOrderLog = false;
               if (agentShare) {
                  List<AgentEarningLog> logs = this.agentEarningLogMapper
                     .selectList(new QueryWrapper<AgentEarningLog>().eq("course_class_id", classId).isNull("delete_time"));
                  if (logs.size() == 1 && logs.get(0).getOrderId() != null && logs.get(0).getOrderId() > 0) {
                     RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectById(logs.get(0).getOrderId());
                     if (order != null
                        && logs.get(0).getOrderAmount() != null
                        && order.getOrderAmount() != null
                        && logs.get(0).getOrderAmount().compareTo(order.getOrderAmount()) == 0) {
                        legacySingleOrderLog = true;
                     }
                  }
               } else {
                  List<CommunityEarningLog> logs = this.communityEarningLogMapper
                     .selectList(new QueryWrapper<CommunityEarningLog>().eq("course_class_id", classId).isNull("delete_time"));
                  if (logs.size() == 1 && logs.get(0).getOrderId() != null && logs.get(0).getOrderId() > 0) {
                     RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectById(logs.get(0).getOrderId());
                     if (order != null
                        && logs.get(0).getOrderAmount() != null
                        && order.getOrderAmount() != null
                        && logs.get(0).getOrderAmount().compareTo(order.getOrderAmount()) == 0) {
                        legacySingleOrderLog = true;
                     }
                  }
               }

               BigDecimal perLesson;
               if (legacySingleOrderLog && paidLessons > 0) {
                  perLesson = total.divide(BigDecimal.valueOf((long)paidLessons), 2, RoundingMode.HALF_UP);
               } else {
                  long logCount = agentShare
                     ? this.agentEarningLogMapper.selectCount(new QueryWrapper<AgentEarningLog>().eq("course_class_id", classId).isNull("delete_time"))
                     : this.communityEarningLogMapper
                        .selectCount(new QueryWrapper<CommunityEarningLog>().eq("course_class_id", classId).isNull("delete_time"));
                  if (logCount > 0L) {
                     perLesson = total.divide(BigDecimal.valueOf(logCount), 2, RoundingMode.HALF_UP);
                  } else {
                     int totalLessons = cls.getTotalLessons() != null && cls.getTotalLessons() > 0 ? cls.getTotalLessons() : 1;
                     perLesson = total.divide(BigDecimal.valueOf((long)totalLessons), 2, RoundingMode.HALF_UP);
                  }
               }

               result.put(classId, perLesson);
            }
         }
      }

      return result;
   }

   private Map<Integer, Integer> loadOrderLessonCountMap(List<Integer> orderIds) {
      if (orderIds != null && !orderIds.isEmpty()) {
         Set<Integer> uniqueOrderIds = new HashSet<>(orderIds);
         List<RechargeOrder> orders = this.rechargeOrderMapper
            .selectList(new QueryWrapper<RechargeOrder>().select(new String[]{"id", "lesson_count"}).in("id", uniqueOrderIds).isNull("delete_time"));
         Map<Integer, Integer> map = new HashMap<>();

         for (RechargeOrder order : orders) {
            if (order.getId() != null) {
               map.put(order.getId(), order.getLessonCount());
            }
         }

         return map;
      } else {
         return Collections.emptyMap();
      }
   }

   private Map<String, BigDecimal> buildLessonAmountMap(List<CourseLesson> lessons, Map<Integer, CourseClass> classMap) {
      Map<String, BigDecimal> result = new HashMap<>();
      if (lessons != null && !lessons.isEmpty()) {
         Set<Integer> classIds = new HashSet<>();
         Set<Integer> lessonNos = new HashSet<>();
         Set<String> lessonKeys = new HashSet<>();

         for (CourseLesson lesson : lessons) {
            if (lesson.getClassId() != null && lesson.getLessonNo() != null && lesson.getLessonNo() > 0) {
               classIds.add(lesson.getClassId());
               lessonNos.add(lesson.getLessonNo());
               lessonKeys.add(this.logKey(lesson.getClassId(), lesson.getLessonNo()));
            }
         }

         if (!classIds.isEmpty() && !lessonNos.isEmpty()) {
            List<CourseConsumeLog> logs = this.courseConsumeLogMapper
               .selectList(
                  new QueryWrapper<CourseConsumeLog>()
                              .select(new String[]{"id", "user_id", "student_id", "course_class_id", "lesson_no", "create_time"})
                              .in("course_class_id", classIds)
                           .in("lesson_no", lessonNos)
                        .orderByAsc("create_time")
                     .orderByAsc("id")
               );
            if (logs.isEmpty()) {
               return result;
            } else {
               List<CourseConsumeLog> lessonLogs = new ArrayList<>();

               for (CourseConsumeLog log : logs) {
                  if (log.getUserId() != null
                     && log.getCourseClassId() != null
                     && log.getLessonNo() != null
                     && lessonKeys.contains(this.logKey(log.getCourseClassId(), log.getLessonNo()))) {
                     lessonLogs.add(log);
                  }
               }

               if (lessonLogs.isEmpty()) {
                  return result;
               } else {
                  Map<String, List<LessonProfitShareServiceImpl.PurchaseBucket>> purchaseBuckets = this.buildPurchaseBuckets(lessonLogs);
                  Map<String, List<Integer>> enrolledStudentIdsMap = this.buildEnrolledStudentIdMap(lessonLogs);
                  Map<String, Set<String>> countedParticipantMap = new HashMap<>();

                  for (CourseConsumeLog logx : lessonLogs) {
                     String lessonKey = this.logKey(logx.getCourseClassId(), logx.getLessonNo());
                     List<Integer> studentIds = this.resolveProfitShareStudentIds(logx, enrolledStudentIdsMap);
                     Set<String> countedParticipants = countedParticipantMap.computeIfAbsent(lessonKey, k -> new HashSet<>());

                     for (Integer studentId : studentIds) {
                        String participantKey = this.userClassStudentKey(logx.getUserId(), logx.getCourseClassId(), studentId);
                        if (countedParticipants.add(participantKey)) {
                           String bucketKey = studentId != null && studentId > 0
                              ? this.userClassStudentKey(logx.getUserId(), logx.getCourseClassId(), studentId)
                              : this.userClassKey(logx.getUserId(), logx.getCourseClassId());
                           BigDecimal unitAmount = this.takeNextUnitAmount(purchaseBuckets.get(bucketKey));
                           if (unitAmount == null) {
                              unitAmount = this.fallbackLessonAmount(classMap.get(logx.getCourseClassId()));
                           }

                           if (unitAmount != null) {
                              result.merge(lessonKey, unitAmount, BigDecimal::add);
                           }
                        }
                     }
                  }

                  return result;
               }
            }
         } else {
            return result;
         }
      } else {
         return result;
      }
   }

   private Map<String, List<Integer>> buildEnrolledStudentIdMap(List<CourseConsumeLog> logs) {
      Set<Integer> userIds = new HashSet<>();
      Set<Integer> classIds = new HashSet<>();

      for (CourseConsumeLog log : logs) {
         if (log.getUserId() != null && log.getCourseClassId() != null) {
            userIds.add(log.getUserId());
            classIds.add(log.getCourseClassId());
         }
      }

      if (!userIds.isEmpty() && !classIds.isEmpty()) {
         List<UserCourse> enrollments = this.userCourseMapper
            .selectList(
               new QueryWrapper<UserCourse>()
                           .select(new String[]{"user_id", "student_id", "course_class_id", "order_id"})
                           .in("user_id", userIds)
                        .in("course_class_id", classIds)
                     .isNull("delete_time")
                  .orderByAsc("id")
            );
         Set<Integer> orderIds = new HashSet<>();

         for (UserCourse enrollment : enrollments) {
            if (enrollment.getOrderId() != null && enrollment.getOrderId() > 0) {
               orderIds.add(enrollment.getOrderId());
            }
         }

         if (orderIds.isEmpty()) {
            return Collections.emptyMap();
         } else {
            List<RechargeOrder> orders = this.rechargeOrderMapper
               .selectList(
                  new QueryWrapper<RechargeOrder>().select(new String[]{"id"}).in("id", orderIds).eq("pay_status", 1)
                        .ne("refund_status", 1)
                     .isNull("delete_time")
               );
            Set<Integer> validOrderIds = orders.stream().<Integer>map(RechargeOrder::getId).collect(Collectors.toSet());
            Map<String, List<Integer>> result = new HashMap<>();

            for (UserCourse enrollmentx : enrollments) {
               if (enrollmentx.getOrderId() != null && validOrderIds.contains(enrollmentx.getOrderId())) {
                  Integer studentId = this.normalizeStudentId(enrollmentx.getStudentId());
                  if (studentId > 0) {
                     List<Integer> studentIds = result.computeIfAbsent(
                        this.userClassKey(enrollmentx.getUserId(), enrollmentx.getCourseClassId()), k -> new ArrayList<>()
                     );
                     if (!studentIds.contains(studentId)) {
                        studentIds.add(studentId);
                     }
                  }
               }
            }

            return result;
         }
      } else {
         return Collections.emptyMap();
      }
   }

   private List<Integer> resolveProfitShareStudentIds(CourseConsumeLog log, Map<String, List<Integer>> enrolledStudentIdsMap) {
      Integer logStudentId = this.normalizeStudentId(log.getStudentId());
      List<Integer> enrolledStudentIds = enrolledStudentIdsMap.get(this.userClassKey(log.getUserId(), log.getCourseClassId()));
      if (enrolledStudentIds != null && !enrolledStudentIds.isEmpty()) {
         List<Integer> result = new ArrayList<>(enrolledStudentIds);
         if (logStudentId > 0 && !result.contains(logStudentId)) {
            result.add(logStudentId);
         }

         return result;
      } else {
         List<Integer> result = new ArrayList<>();
         result.add(logStudentId);
         return result;
      }
   }

   private Map<Integer, BigDecimal> buildManualLessonAmountMap(List<CourseManualLesson> manualLessons, Map<Integer, CourseClass> classMap) {
      Map<Integer, BigDecimal> result = new HashMap<>();
      if (manualLessons != null && !manualLessons.isEmpty()) {
         Set<Integer> manualIds = new HashSet<>();
         Map<Integer, CourseManualLesson> manualMap = new HashMap<>();

         for (CourseManualLesson manualLesson : manualLessons) {
            if (manualLesson.getId() != null && manualLesson.getId() > 0) {
               manualIds.add(manualLesson.getId());
               manualMap.put(manualLesson.getId(), manualLesson);
            }
         }

         if (manualIds.isEmpty()) {
            return result;
         } else {
            List<CourseManualConsume> details = this.courseManualConsumeMapper
               .selectList(new QueryWrapper<CourseManualConsume>().select(new String[]{"id", "manual_id", "user_course_id"}).in("manual_id", manualIds));
            if (details.isEmpty()) {
               return result;
            } else {
               Set<Integer> userCourseIds = new HashSet<>();

               for (CourseManualConsume detail : details) {
                  if (detail.getUserCourseId() != null && detail.getUserCourseId() > 0) {
                     userCourseIds.add(detail.getUserCourseId());
                  }
               }

               if (userCourseIds.isEmpty()) {
                  return result;
               } else {
                  List<UserCourse> enrollments = this.userCourseMapper
                     .selectList(
                        new QueryWrapper<UserCourse>()
                              .select(new String[]{"id", "user_id", "course_class_id", "order_id", "total_lessons"})
                              .in("id", userCourseIds)
                           .isNull("delete_time")
                     );
                  Map<Integer, UserCourse> enrollmentMap = enrollments.stream()
                     .collect(Collectors.toMap(UserCourse::getId, enrollmentx -> enrollmentx, (a, b) -> a));
                  Set<Integer> orderIds = new HashSet<>();

                  for (UserCourse enrollment : enrollments) {
                     if (enrollment.getOrderId() != null && enrollment.getOrderId() > 0) {
                        orderIds.add(enrollment.getOrderId());
                     }
                  }

                  Map<Integer, RechargeOrder> orderMap = new HashMap<>();
                  if (!orderIds.isEmpty()) {
                     List<RechargeOrder> orders = this.rechargeOrderMapper
                        .selectList(
                           new QueryWrapper<RechargeOrder>()
                                       .select(new String[]{"id", "order_amount", "lesson_count"})
                                       .in("id", orderIds)
                                    .eq("pay_status", 1)
                                 .ne("refund_status", 1)
                              .isNull("delete_time")
                        );
                     orderMap = orders.stream().collect(Collectors.toMap(RechargeOrder::getId, order -> (RechargeOrder)order, (a, b) -> a));
                  }

                  for (CourseManualConsume detailx : details) {
                     if (detailx.getManualId() != null && detailx.getUserCourseId() != null) {
                        CourseManualLesson manualLessonx = manualMap.get(detailx.getManualId());
                        UserCourse enrollmentx = enrollmentMap.get(detailx.getUserCourseId());
                        BigDecimal unitAmount = this.resolveUnitAmount(enrollmentx, orderMap);
                        if (unitAmount == null && manualLessonx != null) {
                           unitAmount = this.fallbackLessonAmount(classMap.get(manualLessonx.getClassId()));
                        }

                        if (unitAmount != null) {
                           result.merge(detailx.getManualId(), unitAmount, BigDecimal::add);
                        }
                     }
                  }

                  return result;
               }
            }
         }
      } else {
         return result;
      }
   }

   private Map<String, List<LessonProfitShareServiceImpl.PurchaseBucket>> buildPurchaseBuckets(List<CourseConsumeLog> logs) {
      Set<Integer> userIds = new HashSet<>();
      Set<Integer> classIds = new HashSet<>();

      for (CourseConsumeLog log : logs) {
         if (log.getUserId() != null && log.getCourseClassId() != null) {
            userIds.add(log.getUserId());
            classIds.add(log.getCourseClassId());
         }
      }

      if (!userIds.isEmpty() && !classIds.isEmpty()) {
         List<UserCourse> enrollments = this.userCourseMapper
            .selectList(
               new QueryWrapper<UserCourse>()
                              .select(new String[]{"id", "user_id", "student_id", "course_class_id", "order_id", "total_lessons"})
                              .in("user_id", userIds)
                           .in("course_class_id", classIds)
                        .isNull("delete_time")
                     .orderByAsc("order_id")
                  .orderByAsc("id")
            );
         if (enrollments.isEmpty()) {
            return Collections.emptyMap();
         } else {
            Set<Integer> orderIds = new HashSet<>();

            for (UserCourse enrollment : enrollments) {
               if (enrollment.getOrderId() != null && enrollment.getOrderId() > 0) {
                  orderIds.add(enrollment.getOrderId());
               }
            }

            if (orderIds.isEmpty()) {
               return Collections.emptyMap();
            } else {
               List<RechargeOrder> orders = this.rechargeOrderMapper
                  .selectList(
                     new QueryWrapper<RechargeOrder>()
                                 .select(new String[]{"id", "order_amount", "lesson_count"})
                                 .in("id", orderIds)
                              .eq("pay_status", 1)
                           .ne("refund_status", 1)
                        .isNull("delete_time")
                  );
               Map<Integer, RechargeOrder> orderMap = orders.stream()
                  .collect(Collectors.toMap(RechargeOrder::getId, order -> (RechargeOrder)order, (a, b) -> a));
               Map<String, List<LessonProfitShareServiceImpl.PurchaseBucket>> result = new HashMap<>();

               for (UserCourse enrollmentx : enrollments) {
                  BigDecimal unitAmount = this.resolveUnitAmount(enrollmentx, orderMap);
                  if (unitAmount != null) {
                     int totalLessons = this.resolveTotalLessons(enrollmentx, orderMap.get(enrollmentx.getOrderId()));
                     if (enrollmentx.getStudentId() != null && enrollmentx.getStudentId() > 0) {
                        result.computeIfAbsent(
                              this.userClassStudentKey(enrollmentx.getUserId(), enrollmentx.getCourseClassId(), enrollmentx.getStudentId()),
                              k -> new ArrayList<>()
                           )
                           .add(new LessonProfitShareServiceImpl.PurchaseBucket(enrollmentx.getOrderId(), unitAmount, totalLessons));
                     }

                     result.computeIfAbsent(this.userClassKey(enrollmentx.getUserId(), enrollmentx.getCourseClassId()), k -> new ArrayList<>())
                        .add(new LessonProfitShareServiceImpl.PurchaseBucket(enrollmentx.getOrderId(), unitAmount, totalLessons));
                  }
               }

               for (List<LessonProfitShareServiceImpl.PurchaseBucket> buckets : result.values()) {
                  buckets.sort((a, b) -> a.orderId.compareTo(b.orderId));
               }

               return result;
            }
         }
      } else {
         return Collections.emptyMap();
      }
   }

   private BigDecimal resolveUnitAmount(UserCourse enrollment, Map<Integer, RechargeOrder> orderMap) {
      if (enrollment != null && enrollment.getOrderId() != null && enrollment.getOrderId() > 0) {
         RechargeOrder order = orderMap.get(enrollment.getOrderId());
         if (order == null) {
            return null;
         } else {
            int totalLessons = this.resolveTotalLessons(enrollment, order);
            if (totalLessons <= 0) {
               return null;
            } else {
               BigDecimal orderAmount = order.getOrderAmount() != null ? order.getOrderAmount() : BigDecimal.ZERO;
               return orderAmount.divide(BigDecimal.valueOf((long)totalLessons), 2, RoundingMode.HALF_UP);
            }
         }
      } else {
         return null;
      }
   }

   private int resolveTotalLessons(UserCourse enrollment, RechargeOrder order) {
      if (enrollment != null && enrollment.getTotalLessons() != null && enrollment.getTotalLessons() > 0) {
         return enrollment.getTotalLessons();
      } else {
         return order != null && order.getLessonCount() != null && order.getLessonCount() > 0 ? order.getLessonCount() : 0;
      }
   }

   private BigDecimal takeNextUnitAmount(List<LessonProfitShareServiceImpl.PurchaseBucket> buckets) {
      if (buckets != null && !buckets.isEmpty()) {
         for (LessonProfitShareServiceImpl.PurchaseBucket bucket : buckets) {
            if (bucket.remainingLessons > 0) {
               bucket.remainingLessons -= 1;
               return bucket.unitAmount;
            }
         }

         return buckets.get(buckets.size() - 1).unitAmount;
      } else {
         return null;
      }
   }

   private BigDecimal fallbackLessonAmount(CourseClass cls) {
      if (cls == null) {
         return null;
      } else if (cls.getTotalFee() != null && cls.getTotalFee().compareTo(BigDecimal.ZERO) > 0) {
         return cls.getTotalFee();
      } else {
         return cls.getFeePerLesson() != null && cls.getFeePerLesson().compareTo(BigDecimal.ZERO) > 0 ? cls.getFeePerLesson() : null;
      }
   }

   private LessonProfitShareListVo buildRow(CourseLesson lesson, CourseClass cls, LessonProfitShareServiceImpl.ShareContext ctx) {
      LessonProfitShareListVo vo = new LessonProfitShareListVo();
      vo.setLessonId(lesson.getId());
      vo.setLessonDate(this.formatLessonDate(lesson.getLessonDate()));
      vo.setLessonTime(StringUtils.isNotEmpty(lesson.getTimeSlot()) ? lesson.getTimeSlot() : "");
      vo.setVenue(StringUtils.isNotEmpty(lesson.getVenue()) ? lesson.getVenue() : (StringUtils.isNotEmpty(cls.getVenue()) ? cls.getVenue() : ""));
      vo.setClassName(StringUtils.isNotEmpty(cls.getName()) ? cls.getName() : "");
      String agentName = this.resolveAgentName(cls, ctx);
      if (StringUtils.isNotEmpty(agentName)) {
         vo.setAgentName(agentName);
      }

      String communityName = this.resolveCommunityName(cls, ctx);
      if (StringUtils.isNotEmpty(communityName)) {
         vo.setCommunityName(communityName);
      }

      Integer coachId = cls.getInstructorId() != null && cls.getInstructorId() > 0 ? cls.getInstructorId() : lesson.getCoachId();
      if (coachId != null && coachId > 0) {
         String coachName = ctx.instructorNameMap.get(coachId);
         vo.setInstructorName(StringUtils.isNotEmpty(coachName) ? coachName : cls.getInstructorName());
      } else if (StringUtils.isNotEmpty(cls.getInstructorName())) {
         vo.setInstructorName(cls.getInstructorName());
      }

      boolean settled = this.isSettled(lesson, cls.getId(), ctx);
      vo.setStatus(settled ? 1 : 0);
      vo.setStatusLabel(settled ? "已结算" : "未结算");
      BigDecimal lessonAmount = ctx.lessonAmountMap.get(this.logKey(cls.getId(), lesson.getLessonNo()));
      if (lessonAmount != null && lessonAmount.compareTo(BigDecimal.ZERO) > 0) {
         vo.setLessonAmount(lessonAmount);
      } else if (cls.getTotalFee() != null && cls.getTotalFee().compareTo(BigDecimal.ZERO) > 0) {
         vo.setLessonAmount(cls.getTotalFee());
      }

      vo.setAgentAmount(ctx.agentPerLessonMap.get(cls.getId()));
      vo.setCommunityAmount(ctx.communityPerLessonMap.get(cls.getId()));
      vo.setInstructorAmount(this.resolveInstructorAmount(lesson, cls, ctx));
      if (settled) {
         vo.setPlatformAmount(this.calcPlatformAmount(vo));
      }

      return vo;
   }

   private LessonProfitShareListVo buildManualRow(CourseManualLesson manualLesson, CourseClass cls, LessonProfitShareServiceImpl.ShareContext ctx) {
      LessonProfitShareListVo vo = new LessonProfitShareListVo();
      vo.setLessonId(manualLesson.getId() != null ? -manualLesson.getId() : null);
      vo.setLessonDate(this.formatLessonDate(manualLesson.getLessonDate()));
      vo.setLessonTime(StringUtils.isNotEmpty(manualLesson.getTimeSlot()) ? manualLesson.getTimeSlot() : "");
      vo.setVenue(StringUtils.isNotEmpty(manualLesson.getVenue()) ? manualLesson.getVenue() : (StringUtils.isNotEmpty(cls.getVenue()) ? cls.getVenue() : ""));
      vo.setClassName(StringUtils.isNotEmpty(cls.getName()) ? cls.getName() : "");
      String agentName = this.resolveAgentName(cls, ctx);
      if (StringUtils.isNotEmpty(agentName)) {
         vo.setAgentName(agentName);
      }

      String communityName = this.resolveCommunityName(cls, ctx);
      if (StringUtils.isNotEmpty(communityName)) {
         vo.setCommunityName(communityName);
      }

      if (StringUtils.isNotEmpty(manualLesson.getInstructorName())) {
         vo.setInstructorName(manualLesson.getInstructorName());
      } else if (manualLesson.getInstructorId() != null && manualLesson.getInstructorId() > 0) {
         String coachName = ctx.instructorNameMap.get(manualLesson.getInstructorId());
         vo.setInstructorName(StringUtils.isNotEmpty(coachName) ? coachName : cls.getInstructorName());
      } else if (StringUtils.isNotEmpty(cls.getInstructorName())) {
         vo.setInstructorName(cls.getInstructorName());
      }

      vo.setStatus(1);
      vo.setStatusLabel("已结算");
      BigDecimal lessonAmount = ctx.manualLessonAmountMap.get(manualLesson.getId());
      if (lessonAmount == null || lessonAmount.compareTo(BigDecimal.ZERO) <= 0) {
         BigDecimal unitAmount = this.fallbackLessonAmount(cls);
         Integer studentCount = manualLesson.getStudentCount() != null && manualLesson.getStudentCount() > 0 ? manualLesson.getStudentCount() : 1;
         if (unitAmount != null) {
            lessonAmount = unitAmount.multiply(BigDecimal.valueOf((long)studentCount.intValue()));
         }
      }

      vo.setLessonAmount(lessonAmount);
      vo.setAgentAmount(ctx.agentPerLessonMap.get(cls.getId()));
      vo.setCommunityAmount(ctx.communityPerLessonMap.get(cls.getId()));
      vo.setInstructorAmount(manualLesson.getFeeAmount());
      vo.setPlatformAmount(this.calcPlatformAmount(vo));
      return vo;
   }

   private boolean isSettled(CourseLesson lesson, Integer classId, LessonProfitShareServiceImpl.ShareContext ctx) {
      return lesson.getStatus() != null && lesson.getStatus() == 1 ? true : ctx.lessonLogMap.containsKey(this.logKey(classId, lesson.getLessonNo()));
   }

   private BigDecimal resolveInstructorAmount(CourseLesson lesson, CourseClass cls, LessonProfitShareServiceImpl.ShareContext ctx) {
      BigDecimal fromLog = this.matchInstructorAmount(lesson, cls, ctx);
      if (fromLog != null) {
         return fromLog;
      } else {
         return this.isSettled(lesson, cls.getId(), ctx) && cls.getFeePerLesson() != null && cls.getFeePerLesson().compareTo(BigDecimal.ZERO) > 0
            ? cls.getFeePerLesson()
            : null;
      }
   }

   private BigDecimal matchInstructorAmount(CourseLesson lesson, CourseClass cls, LessonProfitShareServiceImpl.ShareContext ctx) {
      String key = this.logKey(cls.getId(), lesson.getLessonNo());
      CourseLessonLog lessonLog = ctx.lessonLogMap.get(key);
      Integer coachId = cls.getInstructorId() != null && cls.getInstructorId() > 0 ? cls.getInstructorId() : lesson.getCoachId();
      Integer lessonNo = lesson.getLessonNo();
      if (lessonLog != null) {
         for (InstructorAccountLog log : ctx.instructorLogs) {
            if (this.isLessonIncomeLog(log, coachId)
               && log.getRelationId() != null
               && log.getRelationId().equals(lessonLog.getId())
               && this.remarkMatchesLessonNo(log.getRemark(), lessonNo)
               && (coachId == null || log.getInstructorId() == null || log.getInstructorId().equals(coachId))) {
               return log.getAmount();
            }
         }
      }

      for (InstructorAccountLog logx : ctx.instructorLogs) {
         if (this.isLessonIncomeLog(logx, coachId)
            && logx.getRelationId() != null
            && logx.getRelationId().equals(cls.getId())
            && this.remarkMatchesLessonNo(logx.getRemark(), lessonNo)) {
            return logx.getAmount();
         }
      }

      return null;
   }

   private boolean isLessonIncomeLog(InstructorAccountLog log, Integer coachId) {
      return log.getType() != null && log.getType() == 1 ? coachId == null || log.getInstructorId() == null || log.getInstructorId().equals(coachId) : false;
   }

   private boolean remarkMatchesLessonNo(String remark, Integer lessonNo) {
      if (!StringUtils.isEmpty(remark) && lessonNo != null && lessonNo > 0) {
         Matcher matcher = LESSON_NO_IN_REMARK.matcher(remark);

         while (matcher.find()) {
            try {
               if (lessonNo.equals(Integer.parseInt(matcher.group(1)))) {
                  return true;
               }
            } catch (NumberFormatException var5) {
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private BigDecimal calcPlatformAmount(LessonProfitShareListVo vo) {
      return vo.getLessonAmount() == null
         ? null
         : vo.getLessonAmount()
            .subtract(this.nullToZero(vo.getAgentAmount()))
            .subtract(this.nullToZero(vo.getCommunityAmount()))
            .subtract(this.nullToZero(vo.getInstructorAmount()));
   }

   private BigDecimal nullToZero(BigDecimal amount) {
      return amount == null ? BigDecimal.ZERO : amount;
   }

   private Map<String, Object> buildSummary(List<LessonProfitShareListVo> rows) {
      BigDecimal agentTotal = BigDecimal.ZERO;
      BigDecimal communityTotal = BigDecimal.ZERO;
      BigDecimal instructorTotal = BigDecimal.ZERO;
      BigDecimal lessonTotal = BigDecimal.ZERO;
      BigDecimal platformTotal = BigDecimal.ZERO;

      for (LessonProfitShareListVo row : rows) {
         if (row.getStatus() != null && row.getStatus() == 1) {
            if (row.getAgentAmount() != null) {
               agentTotal = agentTotal.add(row.getAgentAmount());
            }

            if (row.getCommunityAmount() != null) {
               communityTotal = communityTotal.add(row.getCommunityAmount());
            }

            if (row.getInstructorAmount() != null) {
               instructorTotal = instructorTotal.add(row.getInstructorAmount());
            }

            if (row.getLessonAmount() != null) {
               lessonTotal = lessonTotal.add(row.getLessonAmount());
            }

            if (row.getPlatformAmount() != null) {
               platformTotal = platformTotal.add(row.getPlatformAmount());
            }
         }
      }

      Map<String, Object> summary = new LinkedHashMap<>();
      summary.put("agentAmountTotal", agentTotal);
      summary.put("communityAmountTotal", communityTotal);
      summary.put("instructorAmountTotal", instructorTotal);
      summary.put("lessonAmountTotal", lessonTotal);
      summary.put("platformAmountTotal", platformTotal);
      return summary;
   }

   private Map<Integer, Integer> loadClassCommunityFromEarningLog(List<Integer> classIds) {
      Map<Integer, Integer> map = new HashMap<>();
      if (classIds.isEmpty()) {
         return map;
      } else {
         for (CommunityEarningLog log : this.communityEarningLogMapper
            .selectList(new QueryWrapper<CommunityEarningLog>().in("course_class_id", classIds).isNull("delete_time").orderByDesc("id"))) {
            if (log.getCommunityId() != null && log.getCommunityId() > 0) {
               map.putIfAbsent(log.getCourseClassId(), log.getCommunityId());
            }
         }

         return map;
      }
   }

   private Map<Integer, List<Integer>> loadAgentCommunityMap(Set<Integer> agentIds) {
      Map<Integer, List<Integer>> map = new HashMap<>();
      if (agentIds.isEmpty()) {
         return map;
      } else {
         for (CommunityAgent relation : this.communityAgentMapper.selectList(new QueryWrapper<CommunityAgent>().in("agent_id", agentIds))) {
            if (relation.getAgentId() != null && relation.getCommunityId() != null) {
               map.computeIfAbsent(relation.getAgentId(), k -> new ArrayList<>()).add(relation.getCommunityId());
            }
         }

         return map;
      }
   }

   private Map<Integer, Integer> loadClassAgentFromEarningLog(List<Integer> classIds) {
      Map<Integer, Integer> map = new HashMap<>();
      if (classIds.isEmpty()) {
         return map;
      } else {
         for (AgentEarningLog log : this.agentEarningLogMapper
            .selectList(new QueryWrapper<AgentEarningLog>().in("course_class_id", classIds).isNull("delete_time").orderByDesc("id"))) {
            if (log.getAgentId() != null && log.getAgentId() > 0) {
               map.putIfAbsent(log.getCourseClassId(), log.getAgentId());
            }
         }

         return map;
      }
   }

   private Map<Integer, List<Integer>> loadCommunityAgentMap(Set<Integer> communityIds) {
      Map<Integer, List<Integer>> map = new HashMap<>();
      if (communityIds.isEmpty()) {
         return map;
      } else {
         for (CommunityAgent relation : this.communityAgentMapper.selectList(new QueryWrapper<CommunityAgent>().in("community_id", communityIds))) {
            if (relation.getCommunityId() != null && relation.getAgentId() != null) {
               map.computeIfAbsent(relation.getCommunityId(), k -> new ArrayList<>()).add(relation.getAgentId());
            }
         }

         return map;
      }
   }

   private void loadCommunityNames(Set<Integer> communityIds, LessonProfitShareServiceImpl.ShareContext ctx) {
      communityIds.removeIf(id -> id == null || id <= 0 || ctx.communityNameMap.containsKey(id));
      if (!communityIds.isEmpty()) {
         for (Community community : this.communityMapper.selectBatchIds(communityIds)) {
            if (community != null && community.getDeleteTime() == null) {
               ctx.communityNameMap.put(community.getId(), community.getName());
            }
         }
      }
   }

   private void loadAgentNames(Set<Integer> agentIds, LessonProfitShareServiceImpl.ShareContext ctx) {
      agentIds.removeIf(id -> id == null || id <= 0 || ctx.agentNameMap.containsKey(id));
      if (!agentIds.isEmpty()) {
         for (Agent agent : this.agentMapper.selectBatchIds(agentIds)) {
            if (agent != null && agent.getDeleteTime() == null) {
               ctx.agentNameMap.put(agent.getId(), agent.getName());
            }
         }
      }
   }

   private List<Integer> resolveCommunityIds(CourseClass cls, LessonProfitShareServiceImpl.ShareContext ctx) {
      List<Integer> ids = new ArrayList<>();
      if (cls.getCommunityId() != null && cls.getCommunityId() > 0) {
         ids.add(cls.getCommunityId());
         return ids;
      } else {
         Integer fromEarning = ctx.classCommunityFromEarningLog.get(cls.getId());
         if (fromEarning != null && fromEarning > 0) {
            ids.add(fromEarning);
            return ids;
         } else {
            if (cls.getAgentId() != null && cls.getAgentId() > 0) {
               List<Integer> communityIds = ctx.agentCommunityMap.get(cls.getAgentId());
               if (communityIds != null) {
                  for (Integer communityId : communityIds) {
                     if (communityId != null && communityId > 0 && !ids.contains(communityId)) {
                        ids.add(communityId);
                     }
                  }
               }
            }

            return ids;
         }
      }
   }

   private String resolveCommunityName(CourseClass cls, LessonProfitShareServiceImpl.ShareContext ctx) {
      List<Integer> communityIds = this.resolveCommunityIds(cls, ctx);
      if (communityIds.isEmpty()) {
         return "";
      } else {
         List<String> names = new ArrayList<>();

         for (Integer communityId : communityIds) {
            String name = ctx.communityNameMap.get(communityId);
            if (StringUtils.isNotEmpty(name) && !names.contains(name)) {
               names.add(name);
            }
         }

         return String.join("、", names);
      }
   }

   private List<Integer> resolveAgentIds(CourseClass cls, LessonProfitShareServiceImpl.ShareContext ctx) {
      List<Integer> ids = new ArrayList<>();
      if (cls.getAgentId() != null && cls.getAgentId() > 0) {
         ids.add(cls.getAgentId());
         return ids;
      } else {
         Integer fromEarning = ctx.classAgentFromEarningLog.get(cls.getId());
         if (fromEarning != null && fromEarning > 0) {
            ids.add(fromEarning);
            return ids;
         } else {
            for (Integer communityId : this.resolveCommunityIds(cls, ctx)) {
               List<Integer> agentIds = ctx.communityAgentMap.get(communityId);
               if (agentIds != null) {
                  for (Integer agentId : agentIds) {
                     if (agentId != null && agentId > 0 && !ids.contains(agentId)) {
                        ids.add(agentId);
                     }
                  }
               }
            }

            return ids;
         }
      }
   }

   private String resolveAgentName(CourseClass cls, LessonProfitShareServiceImpl.ShareContext ctx) {
      List<Integer> agentIds = this.resolveAgentIds(cls, ctx);
      if (agentIds.isEmpty()) {
         return "";
      } else {
         List<String> names = new ArrayList<>();

         for (Integer agentId : agentIds) {
            String name = ctx.agentNameMap.get(agentId);
            if (StringUtils.isNotEmpty(name) && !names.contains(name)) {
               names.add(name);
            }
         }

         return String.join("、", names);
      }
   }

   private String formatLessonDate(String date) {
      if (StringUtils.isEmpty(date)) {
         return "";
      } else {
         try {
            LocalDate localDate = LocalDate.parse(date.trim());
            return localDate.getYear() + "/" + localDate.getMonthValue() + "/" + localDate.getDayOfMonth();
         } catch (DateTimeParseException var3) {
            return date;
         }
      }
   }

   private LocalDate rowDateValue(LessonProfitShareListVo row) {
      if (row != null && !StringUtils.isEmpty(row.getLessonDate())) {
         try {
            String[] parts = row.getLessonDate().split("/");
            return parts.length == 3
               ? LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]))
               : LocalDate.parse(row.getLessonDate().trim());
         } catch (Exception var3) {
            return LocalDate.MIN;
         }
      } else {
         return LocalDate.MIN;
      }
   }

   private String nullToString(String value) {
      return value == null ? "" : value;
   }

   private String logKey(Integer classId, Integer lessonNo) {
      return classId + "_" + (lessonNo != null ? lessonNo : 0);
   }

   private String userClassKey(Integer userId, Integer classId) {
      return userId + "_" + classId;
   }

   private String userClassStudentKey(Integer userId, Integer classId, Integer studentId) {
      return userId + "_" + classId + "_" + (studentId != null && studentId > 0 ? studentId : 0);
   }

   private Integer normalizeStudentId(Integer studentId) {
      return studentId != null && studentId > 0 ? studentId : 0;
   }

   private static class PurchaseBucket {
      private final Integer orderId;
      private final BigDecimal unitAmount;
      private int remainingLessons;

      private PurchaseBucket(Integer orderId, BigDecimal unitAmount, int remainingLessons) {
         this.orderId = orderId;
         this.unitAmount = unitAmount;
         this.remainingLessons = remainingLessons;
      }
   }

   private static class ShareContext {
      private final Map<Integer, String> agentNameMap = new HashMap<>();
      private final Map<Integer, String> communityNameMap = new HashMap<>();
      private final Map<Integer, String> instructorNameMap = new HashMap<>();
      private final Map<String, CourseLessonLog> lessonLogMap = new HashMap<>();
      private Map<String, BigDecimal> lessonAmountMap = Collections.emptyMap();
      private Map<Integer, BigDecimal> manualLessonAmountMap = Collections.emptyMap();
      private Map<Integer, BigDecimal> agentPerLessonMap = Collections.emptyMap();
      private Map<Integer, BigDecimal> communityPerLessonMap = Collections.emptyMap();
      private Map<Integer, Integer> classCommunityFromEarningLog = Collections.emptyMap();
      private Map<Integer, Integer> classAgentFromEarningLog = Collections.emptyMap();
      private Map<Integer, List<Integer>> agentCommunityMap = Collections.emptyMap();
      private Map<Integer, List<Integer>> communityAgentMap = Collections.emptyMap();
      private List<InstructorAccountLog> instructorLogs = Collections.emptyList();

      private ShareContext() {
      }
   }
}
