package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.IIndexService;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.entity.Message;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.MessageMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.ListUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.user.User;

@Service
public class IndexServiceImpl implements IIndexService {
   @Resource
   UserMapper userMapper;
   @Resource
   InstructorMapper instructorMapper;
   @Resource
   CourseClassMapper courseClassMapper;
   @Resource
   RechargeOrderMapper rechargeOrderMapper;
   @Resource
   MessageMapper messageMapper;

   public IndexServiceImpl() {
   }

   @Override
   public Map<String, Object> topStats() {
      Map<String, Object> response = new LinkedHashMap<>();
      long totalUsers = this.userMapper.selectCount(null);
      List<Long> yesterday = TimeUtils.yesterday();
      long usersAtYesterdayEnd = this.userMapper.selectCount(new QueryWrapper<User>().lt("create_time", yesterday.get(1) + 1L));
      response.put("totalUsers", totalUsers);
      response.put("userGrowth", this.calculateGrowth(totalUsers, usersAtYesterdayEnd));
      long totalInstructors = this.instructorMapper.selectCount(null);
      long instructorsAtYesterdayEnd = this.instructorMapper
         .selectCount(new QueryWrapper<Instructor>().lt("create_time", new Date((yesterday.get(1) + 1L) * 1000L)));
      response.put("totalInstructors", totalInstructors);
      response.put("instructorGrowth", this.calculateGrowth(totalInstructors, instructorsAtYesterdayEnd));
      long ongoingClasses = this.courseClassMapper.selectCount(new QueryWrapper<CourseClass>().eq("status", 1));
      long ongoingAtYesterdayEnd = this.courseClassMapper
         .selectCount(new QueryWrapper<CourseClass>().eq("status", 1).lt("create_time", yesterday.get(1) + 1L));
      response.put("ongoingClasses", ongoingClasses);
      response.put("classGrowth", this.calculateGrowth(ongoingClasses, ongoingAtYesterdayEnd));
      List<Long> month = TimeUtils.month();
      List<Long> lastMonth = TimeUtils.lastMonth();
      BigDecimal thisMonthIncome = this.getIncomeBetween(month.get(0), month.get(1));
      BigDecimal lastMonthIncome = this.getIncomeBetween(lastMonth.get(0), lastMonth.get(1));
      response.put("monthlyIncome", thisMonthIncome.stripTrailingZeros().toPlainString());
      response.put("incomeGrowth", this.calculateGrowth(thisMonthIncome, lastMonthIncome));
      return response;
   }

   private String calculateGrowth(long current, long previous) {
      if (previous <= 0L) {
         return current > 0L ? "+100%" : "+0%";
      } else {
         double growth = (double)(current - previous) / (double)previous * 100.0;
         return (growth >= 0.0 ? "+" : "") + String.format("%.1f", growth) + "%";
      }
   }

   private String calculateGrowth(BigDecimal current, BigDecimal previous) {
      if (previous != null && previous.compareTo(BigDecimal.ZERO) > 0) {
         if (current == null) {
            current = BigDecimal.ZERO;
         }

         BigDecimal growth = current.subtract(previous).divide(previous, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
         return (growth.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "") + String.format("%.1f", growth) + "%";
      } else {
         return current != null && current.compareTo(BigDecimal.ZERO) > 0 ? "+100%" : "+0%";
      }
   }

   private BigDecimal getIncomeBetween(Long start, Long end) {
      QueryWrapper<RechargeOrder> wrapper = new QueryWrapper<RechargeOrder>();
      wrapper.eq("pay_status", 1).ge("pay_time", start).le("pay_time", end)
         .select(new String[]{"SUM(order_amount) as amount_sum"});
      List<Map<String, Object>> list = this.rechargeOrderMapper.selectMaps(wrapper);
      return list != null && !list.isEmpty() && list.get(0) != null && list.get(0).get("amount_sum") != null
         ? new BigDecimal(list.get(0).get("amount_sum").toString())
         : BigDecimal.ZERO;
   }

   @Override
   public Map<String, Object> index() {
      Map<String, Object> console = new LinkedHashMap<>();
      Map<String, Object> version = new LinkedHashMap<>();
      version.put("name", ConfigUtils.get("website", "name", "LikeAdmin-Java"));
      version.put("version", GlobalConfig.version);
      version.put("website", "www.likeadmin.cn");
      version.put("based", "Vue3.x、ElementUI、MySQL");
      Map<String, String> channel = new LinkedHashMap<>();
      channel.put("gitee", "https://gitee.com/likeadmin/likeadmin_java");
      channel.put("website", "https://www.likeadmin.cn");
      version.put("channel", channel);
      console.put("version", version);
      Map<String, Object> today = new LinkedHashMap<>();
      today.put("time", TimeUtils.timestampToDate(TimeUtils.timestamp()));
      List<Long> todayTime = TimeUtils.today();
      today.put("today_new_user", this.userMapper.selectCount(new QueryWrapper<User>().ge("create_time", todayTime.get(0))));
      today.put("total_new_user", this.userMapper.selectCount(null));
      today.put("today_sales", this.getIncomeBetween(todayTime.get(0), todayTime.get(1)));
      today.put("total_sales", this.getIncomeBetween(0L, TimeUtils.timestamp()));
      today.put(
         "order_num", this.rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>().eq("pay_status", 1).ge("pay_time", todayTime.get(0)))
      );
      today.put("order_sum", this.rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>().eq("pay_status", 1)));
      console.put("today", today);
      Map<String, Object> visitor = new LinkedHashMap<>();
      List<String> dates = TimeUtils.daysAgoDate(15);
      visitor.put("date", dates);
      final List<BigDecimal> amounts = new ArrayList<>();
      Calendar calendar = Calendar.getInstance();
      calendar.set(11, 0);
      calendar.set(12, 0);
      calendar.set(13, 0);
      calendar.set(14, 0);
      long todayStart = calendar.getTimeInMillis() / 1000L;

      for (int i = 14; i >= 0; i--) {
         long start = todayStart - (long)i * 86400L;
         long end = start + 86399L;
         amounts.add(this.getIncomeBetween(start, end));
      }

      visitor.put("list", new JSONArray() {
         {
            this.add(new JSONObject() {
               {
                  this.put("name", "收入");
                  this.put("data", amounts);
               }
            });
         }
      });
      console.put("visitor", visitor);
      List<Message> messages = this.messageMapper.selectList(new QueryWrapper<Message>().orderByDesc("create_time").last("limit 20"));
      JSONArray recentActivities = new JSONArray();

      for (Message msg : messages) {
         JSONObject act = new JSONObject();
         act.put("title", msg.getTitle());
         act.put("description", msg.getContent());
         act.put("time", TimeUtils.timestampToDate(msg.getCreateTime()));
         String icon = "el-icon-Message";
         String color = "#3b82f6";
         if (msg.getType() != null) {
            if (msg.getType() == 3) {
               icon = "el-icon-Money";
               color = "#22c55e";
            } else if (msg.getType() == 4) {
               icon = "el-icon-CirclePlus";
               color = "#f59e0b";
            } else if (msg.getType() == 1) {
               icon = "el-icon-Reading";
               color = "#a855f7";
            }
         }

         act.put("icon", icon);
         act.put("color", color);
         recentActivities.add(act);
      }

      console.put("recentActivities", recentActivities);
      console.put("menu", new JSONArray() {
         {
            this.add(new JSONObject() {
               {
                  this.put("name", "管理员");
                  this.put("image", UrlUtils.toAdminAbsoluteUrl("/api/static/menu_admin.png"));
                  this.put("url", "/permission/admin");
               }
            });
            this.add(new JSONObject() {
               {
                  this.put("name", "角色管理");
                  this.put("image", UrlUtils.toAdminAbsoluteUrl("/api/static/menu_role.png"));
                  this.put("url", "/permission/role");
               }
            });
            this.add(new JSONObject() {
               {
                  this.put("name", "部门管理");
                  this.put("image", UrlUtils.toAdminAbsoluteUrl("/api/static/menu_dept.png"));
                  this.put("url", "/organization/department");
               }
            });
            this.add(new JSONObject() {
               {
                  this.put("name", "字典管理");
                  this.put("image", UrlUtils.toAdminAbsoluteUrl("/api/static/menu_dict.png"));
                  this.put("url", "/dev_tools/dict");
               }
            });
            this.add(new JSONObject() {
               {
                  this.put("name", "代码生成器");
                  this.put("image", UrlUtils.toAdminAbsoluteUrl("/api/static/menu_generator.png"));
                  this.put("url", "/dev_tools/code");
               }
            });
            this.add(new JSONObject() {
               {
                  this.put("name", "素材中心");
                  this.put("image", UrlUtils.toAdminAbsoluteUrl("/api/static/menu_file.png"));
                  this.put("url", "/material/index");
               }
            });
            this.add(new JSONObject() {
               {
                  this.put("name", "菜单权限");
                  this.put("image", UrlUtils.toAdminAbsoluteUrl("/api/static/menu_auth.png"));
                  this.put("url", "/permission/menu");
               }
            });
            this.add(new JSONObject() {
               {
                  this.put("name", "网站信息");
                  this.put("image", UrlUtils.toAdminAbsoluteUrl("/api/static/menu_web.png"));
                  this.put("url", "/setting/website/information");
               }
            });
         }
      });
      return console;
   }

   @Override
   public Map<String, Object> config() {
      Map<String, String> website = ConfigUtils.get("website");
      String copyright = ConfigUtils.get("copyright", "config", "");
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("oss_domain", UrlUtils.domain());
      map.put("web_name", website.getOrDefault("name", ""));
      map.put("web_favicon", UrlUtils.toAdminAbsoluteUrl(website.getOrDefault("web_favicon", "")));
      map.put("web_logo", UrlUtils.toAdminAbsoluteUrl(website.getOrDefault("web_logo", "")));
      map.put("login_image", UrlUtils.toAdminAbsoluteUrl(website.getOrDefault("login_image", "")));
      map.put("copyright_config", ListUtils.stringToListAsMapStr(copyright));
      return map;
   }
}
