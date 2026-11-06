
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `la_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_activity` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '活动标题',
  `cover` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '封面图（相对或绝对地址）',
  `activity_date` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '活动日期 yyyy-MM-dd',
  `time_slot` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '活动时段 HH:mm-HH:mm',
  `place` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '活动地点（展示文案）',
  `area_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '小区ID（la_area.id，0=不关联）',
  `fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '每人费用（按参与学员人数计费）',
  `capacity` int(11) NOT NULL DEFAULT '0' COMMENT '报名名额上限（人）',
  `companion_limit` int(11) NOT NULL DEFAULT '0' COMMENT '陪同人员数上限（0=不开放）',
  `joined` int(11) NOT NULL DEFAULT '0' COMMENT '已占名额（含待支付预占，人）',
  `deadline` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '报名截止日期 yyyy-MM-dd',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '活动介绍（对应前端 desc）',
  `tags` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签，逗号分隔',
  `notice` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '报名须知',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '上下架：0=下架，1=上架',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序，小在前',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) unsigned DEFAULT NULL COMMENT '软删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_status_date` (`status`,`activity_date`),
  KEY `idx_area` (`area_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动主档（按学员人数计费）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_activity_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_activity_booking` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '报名批次ID',
  `activity_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '活动ID',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '报名家长账号ID',
  `order_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '支付订单ID（0=免费报名）',
  `student_count` int(11) NOT NULL DEFAULT '0' COMMENT '学员数',
  `companion_count` int(11) NOT NULL DEFAULT '0' COMMENT '陪同人员数',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0=待支付,1=已支付,3=已取消',
  `create_time` int(10) unsigned DEFAULT NULL,
  `update_time` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_activity_status` (`activity_id`,`status`),
  KEY `idx_order` (`order_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动报名批次（学员+陪同，一次报名一行）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_activity_enroll_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_activity_enroll_alert` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '告警ID',
  `activity_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '活动ID（la_activity.id）',
  `order_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '支付订单ID（la_recharge_order.id，唯一）',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '报名家长账号ID',
  `order_sn` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '订单号（冗余，便于对账）',
  `student_ids` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '报名学员ID，逗号分隔',
  `student_count` int(11) NOT NULL DEFAULT '0' COMMENT '报名学员人数',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单金额（元）',
  `pay_way` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付方式：1=余额 2=微信 3=支付宝',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '异常类型：1=名额释放后支付已满员（超员生效）',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '处理状态：0=待处理 1=已处理 2=已退款',
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '异常原因（自动生成）',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '运营处理备注',
  `handler_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '处理人后台管理员ID（0=未处理）',
  `handler_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '处理人名称（冗余）',
  `handle_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '处理时间戳（0=未处理）',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order` (`order_id`),
  KEY `idx_status_create` (`status`,`create_time`),
  KEY `idx_activity` (`activity_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动报名超员待处理告警（#61）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_activity_signup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_activity_signup` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '报名ID',
  `activity_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '活动ID',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '报名家长账号ID',
  `student_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '参与学员ID',
  `order_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '支付订单ID（la_recharge_order.id）',
  `booking_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '报名批次ID（la_activity_booking.id，0=历史数据）',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0=待支付，1=已支付，3=已取消',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_student` (`activity_id`,`student_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_order` (`order_id`),
  KEY `idx_status_create` (`status`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动报名明细（占名额）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_admin` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `root` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否超级管理员 0-否 1-是',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '名称',
  `avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '用户头像',
  `account` varchar(32) NOT NULL DEFAULT '' COMMENT '账号',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `login_time` int(11) DEFAULT NULL COMMENT '最后登录时间',
  `login_ip` varchar(39) DEFAULT '' COMMENT '最后登录ip',
  `multipoint_login` tinyint(3) unsigned DEFAULT '1' COMMENT '是否支持多处登录：1-是；0-否；',
  `disable` tinyint(3) unsigned DEFAULT '0' COMMENT '是否禁用：0-否；1-是；',
  `create_time` int(11) NOT NULL COMMENT '创建时间',
  `update_time` int(11) DEFAULT NULL COMMENT '修改时间',
  `delete_time` int(11) DEFAULT NULL COMMENT '删除时间',
  `first_login` int(11) DEFAULT '1' COMMENT '是否首次登录',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_admin_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_admin_dept` (
  `admin_id` int(10) NOT NULL DEFAULT '0' COMMENT '管理员id',
  `dept_id` int(10) NOT NULL DEFAULT '0' COMMENT '部门id',
  PRIMARY KEY (`admin_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_admin_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_admin_jobs` (
  `admin_id` int(10) NOT NULL COMMENT '管理员id',
  `jobs_id` int(10) NOT NULL COMMENT '岗位id',
  PRIMARY KEY (`admin_id`,`jobs_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_admin_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_admin_role` (
  `admin_id` int(10) NOT NULL COMMENT '管理员id',
  `role_id` int(10) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`admin_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_admin_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_admin_session` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) unsigned NOT NULL COMMENT '用户id',
  `terminal` tinyint(1) NOT NULL DEFAULT '1' COMMENT '客户端类型：1-pc管理后台 2-mobile手机管理后台',
  `token` varchar(32) NOT NULL COMMENT '令牌',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `expire_time` int(10) NOT NULL COMMENT '到期时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `admin_id_client` (`admin_id`,`terminal`) USING BTREE COMMENT '一个用户在一个终端只有一个token',
  UNIQUE KEY `token` (`token`) USING BTREE COMMENT 'token是唯一的'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员会话表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_agent` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '关联后台管理员ID',
  `community_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属社区ID',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '代理名称',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '联系电话',
  `area_polygon` text COMMENT '代理区域(高德地图多边形JSON)',
  `commission_rate` decimal(5,4) NOT NULL DEFAULT '0.0000' COMMENT '分成比例',
  `bank_name` varchar(64) DEFAULT '' COMMENT '提现银行名称',
  `bank_card` varchar(64) DEFAULT '' COMMENT '提现银行卡号',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '当前可提现余额',
  `total_earnings` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '累计总收益',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(20) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='代理商表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_agent_earning_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_agent_earning_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agent_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '代理ID',
  `course_class_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '购买的课程班级ID',
  `order_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '对应订单ID',
  `order_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `commission_rate` decimal(5,4) NOT NULL DEFAULT '0.0000' COMMENT '当时分成比例',
  `earning_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '分成金额',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(20) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COMMENT='代理分成记录表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_agent_withdrawal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_agent_withdrawal` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agent_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '代理ID',
  `bank_name` varchar(64) NOT NULL DEFAULT '' COMMENT '提现银行名称',
  `bank_card` varchar(64) NOT NULL DEFAULT '' COMMENT '提现银行卡号',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '提现金额',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态:0-待审核;1-审核通过(已打款);2-审核拒绝',
  `audit_remark` varchar(255) DEFAULT '' COMMENT '审核备注',
  `audit_time` bigint(20) DEFAULT '0' COMMENT '审核时间',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(20) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='代理提现申请表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_area` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `community_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属社区ID（la_community.id）',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '小区名称',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细地址',
  `lat` decimal(10,6) DEFAULT NULL COMMENT '纬度（后台维护，前端只读）',
  `lng` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `cover` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '封面图',
  `tags` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签（逗号分隔，如：篮球场,塑胶跑道）',
  `distance_text` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '展示用距离串（如 480m，可后台填）',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1=启用 0=停用',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) unsigned DEFAULT NULL COMMENT '删除时间（软删）',
  PRIMARY KEY (`id`),
  KEY `idx_community` (`community_id`),
  KEY `idx_status` (`status`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小区库（绑定社区）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `cid` int(11) NOT NULL COMMENT '文章分类',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `desc` varchar(255) DEFAULT '' COMMENT '简介',
  `abstract` text COMMENT '文章摘要',
  `image` varchar(128) DEFAULT NULL COMMENT '文章图片',
  `author` varchar(255) DEFAULT '' COMMENT '作者',
  `content` text COMMENT '文章内容',
  `click_virtual` int(10) DEFAULT '0' COMMENT '虚拟浏览量',
  `click_actual` int(11) DEFAULT '0' COMMENT '实际浏览量',
  `is_show` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否显示:1-是.0-否',
  `sort` int(5) DEFAULT '0' COMMENT '排序',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(11) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='文章表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_article_cate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_article_cate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章分类id',
  `name` varchar(90) DEFAULT NULL COMMENT '分类名称',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `is_show` tinyint(1) DEFAULT '1' COMMENT '是否显示:1-是;0-否',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_article_collect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_article_collect` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `article_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '文章ID',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '收藏状态 0-未收藏 1-已收藏',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_asset_url_migration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_asset_url_migration` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `batch_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '迁移批次（一次 apply 一个批次）',
  `table_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '业务表名',
  `column_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字段名',
  `row_pk` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '业务表主键值',
  `old_value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '迁移前字段原值（回滚用）',
  `new_value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '迁移后字段值（回滚前置校验用）',
  `status` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'applied' COMMENT '状态：applied=已改 rolled_back=已回滚 skipped=冲突未改',
  `created_at` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间戳（秒）',
  `rolled_back_at` int(11) unsigned DEFAULT NULL COMMENT '回滚时间戳（秒）',
  PRIMARY KEY (`id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_status` (`status`),
  KEY `idx_row` (`table_name`,`column_name`,`row_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='#122 P3 对外资源地址归一化台账（旧域名绝对地址→相对key）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_class_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_class_apply` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `group_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '聚合组ID',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '家长账号ID',
  `student_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '学员ID',
  `area_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '意向小区ID（可为0）',
  `area_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '意向小区名称（自由填写）',
  `expect_time` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '期望时间',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '联系手机号',
  `note` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '明细状态，跟随聚合组',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '提交时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) unsigned DEFAULT NULL COMMENT '软删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_group` (`group_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_student` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='开班申请明细';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_class_apply_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_class_apply_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '聚合组ID',
  `street_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '街道ID（la_region.id，level=3）',
  `course_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '期望课程类型',
  `joined` int(11) NOT NULL DEFAULT '0' COMMENT '已申请人数（合并计数）',
  `target` int(11) NOT NULL DEFAULT '5' COMMENT '成班目标人数',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '状态：pending=招募中，following=客服跟进中，closed=已开班',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_street_course` (`street_id`,`course_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='开班申请聚合组（街道+课程类型）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_community` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '关联后台管理员ID',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '社区名称',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '联系电话',
  `region_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属街道ID（la_region.id，level=3）',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '详细地址',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度（后台维护，可为空）',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度（后台维护，可为空）',
  `commission_rate` decimal(5,4) NOT NULL DEFAULT '0.0000' COMMENT '分成比例',
  `bank_name` varchar(64) DEFAULT '' COMMENT '提现银行名称',
  `bank_card` varchar(64) DEFAULT '' COMMENT '提现银行卡号',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '当前可提现余额',
  `total_earnings` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '累计总收益',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(20) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_region` (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COMMENT='社区表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_community_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_community_agent` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `community_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '社区ID',
  `agent_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '代理ID',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_community_agent` (`community_id`,`agent_id`),
  KEY `idx_agent_id` (`agent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='社区-代理关联表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_community_earning_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_community_earning_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `community_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '社区ID',
  `course_class_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '课程班级ID',
  `order_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '订单ID',
  `order_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `commission_rate` decimal(5,4) NOT NULL DEFAULT '0.0000' COMMENT '当时分成比例',
  `earning_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '分成金额',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(20) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_community_id` (`community_id`)
) ENGINE=InnoDB AUTO_INCREMENT=454 DEFAULT CHARSET=utf8mb4 COMMENT='社区分成记录表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(30) DEFAULT NULL COMMENT '类型',
  `name` varchar(60) NOT NULL DEFAULT '' COMMENT '名称',
  `value` text COMMENT '值',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COMMENT='配置表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联模板ID(0=手动创建)',
  `sn` varchar(32) NOT NULL DEFAULT '' COMMENT '券编号',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '券名称',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '抵扣金额',
  `min_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '最低消费金额(0=无门槛)',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '绑定用户ID',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '绑定手机号',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态:0=未使用,1=已使用,2=已过期',
  `used_order_id` int(11) DEFAULT '0' COMMENT '使用时的订单ID',
  `used_time` int(10) DEFAULT NULL COMMENT '使用时间',
  `start_time` int(10) DEFAULT NULL COMMENT '生效时间',
  `expire_time` int(10) DEFAULT NULL COMMENT '过期时间',
  `admin_id` int(11) NOT NULL DEFAULT '0' COMMENT '发券管理员ID',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_mobile` (`mobile`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8mb4 COMMENT='优惠券(抵扣券)表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_coupon_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_coupon_flow` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `coupon_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '券实例ID（la_coupon_instance.id）',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `student_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学员ID',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '类型：1=发放，2=核销扣减，3=补发，4=作废，5=退回',
  `times` int(11) NOT NULL DEFAULT '0' COMMENT '变动次数（正数增加，负数扣减）',
  `balance` int(11) NOT NULL DEFAULT '0' COMMENT '变动后余额',
  `order_id` int(10) unsigned DEFAULT NULL COMMENT '关联订单ID',
  `order_sn` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '关联订单号',
  `ref` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '关联业务（课程/场次/原因）',
  `operator_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'system' COMMENT '操作人类型：system/admin/user/coach',
  `operator_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '操作人ID',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_user_student` (`user_id`,`student_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='次数券流水（只追加，不可删除）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_coupon_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_coupon_goods` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '商品编码（数据种子幂等键，如 group_trial）',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '券名称',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '商品简介（前端 desc 字段）',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '售价（元）',
  `times` int(11) NOT NULL DEFAULT '1' COMMENT '含次数',
  `valid_days` int(11) NOT NULL DEFAULT '0' COMMENT '有效天数（0=长期有效，待甲方最终口径）',
  `scope` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '适用范围文案（如 团课通用）',
  `scope_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'all' COMMENT '适用范围类型：all/group/private/trial',
  `course_ids` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '限定课程ID，逗号分隔；空=不限',
  `area_ids` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '限定小区ID，逗号分隔；空=不限',
  `per_user` int(11) NOT NULL DEFAULT '0' COMMENT '单账号限购份数（0=不限）',
  `per_student` int(11) NOT NULL DEFAULT '0' COMMENT '单学员限购份数（0=不限）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '上下架：0=下架，1=上架',
  `tag` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '前端角标（新人专享/灵活/超值等）',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序，小在前',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) unsigned DEFAULT NULL COMMENT '软删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`),
  KEY `idx_status_sort` (`status`,`sort`),
  KEY `idx_delete_time` (`delete_time`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='次数券商品';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_coupon_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_coupon_instance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '券实例ID',
  `sn` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '券编号',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '持券用户ID（la_user.id）',
  `student_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '持券学员ID（la_student.id）',
  `goods_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '券商品ID（la_coupon_goods.id）',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '券名称快照',
  `total` int(11) NOT NULL DEFAULT '0' COMMENT '总次数',
  `remaining` int(11) NOT NULL DEFAULT '0' COMMENT '剩余次数',
  `scope` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '适用范围文案快照',
  `scope_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'all' COMMENT '适用范围类型快照',
  `course_ids` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '限定课程ID，逗号分隔；空=不限',
  `area_ids` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '限定小区ID，逗号分隔；空=不限',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1=有效，2=已用完，3=已过期，4=已作废',
  `start_time` int(10) unsigned DEFAULT NULL COMMENT '生效时间',
  `expire_time` int(10) unsigned DEFAULT NULL COMMENT '到期时间（当天 23:59:59；NULL=长期有效）',
  `order_id` int(10) unsigned DEFAULT NULL COMMENT '发放来源订单ID（NULL=后台发放/补发，不参与唯一）',
  `order_seq` int(11) NOT NULL DEFAULT '0' COMMENT '同一订单内第几张券（0=旧数据/单张）',
  `source` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'purchase' COMMENT '来源：purchase=购买，grant=后台发放，compensate=补发',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sn` (`sn`),
  UNIQUE KEY `uk_order_seq` (`order_id`,`order_seq`),
  KEY `idx_user_student` (`user_id`,`student_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_status_expire` (`status`,`expire_time`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='次数券实例';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_coupon_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_coupon_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '券名称',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '抵扣金额',
  `min_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '满减门槛(0=无门槛)',
  `valid_days` int(11) NOT NULL DEFAULT '0' COMMENT '有效天数(从发放日起算,0=按固定时间)',
  `start_time` bigint(20) DEFAULT NULL COMMENT '固定生效时间(valid_days=0时生效)',
  `expire_time` bigint(20) DEFAULT NULL COMMENT '固定过期时间(valid_days=0时生效)',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '模板状态:0=启用,1=停用',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `admin_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `delete_time` bigint(20) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='优惠券模板表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_booking` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `student_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学员档案ID',
  `course_class_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '班级ID',
  `lesson_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课节ID(ys_course_lesson.id)',
  `lesson_no` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课节号',
  `lesson_date` varchar(10) NOT NULL DEFAULT '' COMMENT '上课日期',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=已预约，10=已点名',
  `create_time` int(10) unsigned DEFAULT NULL,
  `update_time` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_lesson_student` (`user_id`,`lesson_id`,`student_id`),
  KEY `idx_lesson_id` (`lesson_id`),
  KEY `idx_class_date` (`course_class_id`,`lesson_date`)
) ENGINE=InnoDB AUTO_INCREMENT=472 DEFAULT CHARSET=utf8mb4 COMMENT='课时预约表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_booking_bak_20260629`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_booking_bak_20260629` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `student_id` int(10) unsigned DEFAULT NULL COMMENT '学员档案ID',
  `course_class_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '班级ID',
  `lesson_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课节ID(ys_course_lesson.id)',
  `lesson_no` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课节号',
  `lesson_date` varchar(10) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '上课日期',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1=已预约，10=已点名',
  `create_time` int(10) unsigned DEFAULT NULL,
  `update_time` int(10) unsigned DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_checkin_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_checkin_audit` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '审计ID',
  `session_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '场次ID（ys_course_lesson.id）',
  `class_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课程班级ID',
  `lesson_no` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课节序号',
  `booking_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '预约ID',
  `student_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学员ID',
  `student_name` varchar(64) NOT NULL DEFAULT '' COMMENT '学员姓名快照',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '家长用户ID',
  `course_name` varchar(100) NOT NULL DEFAULT '' COMMENT '课程名称快照',
  `session_date` varchar(10) NOT NULL DEFAULT '' COMMENT '场次日期快照',
  `time_slot` varchar(30) NOT NULL DEFAULT '' COMMENT '场次时段快照',
  `action_type` varchar(32) NOT NULL DEFAULT 'manual_checkin' COMMENT '动作：manual_checkin=人工补核销',
  `reason` varchar(500) NOT NULL DEFAULT '' COMMENT '补核销原因（必填）',
  `operator_admin_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '操作管理员ID',
  `operator_name` varchar(64) NOT NULL DEFAULT '' COMMENT '操作管理员姓名快照',
  `before_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '变更前预约状态',
  `after_status` tinyint(4) NOT NULL DEFAULT '10' COMMENT '变更后预约状态',
  `deducted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '本次是否扣券：1=扣 0=未扣',
  `coupon_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '扣减的券实例ID',
  `coupon_title` varchar(100) NOT NULL DEFAULT '' COMMENT '券名称快照',
  `coupon_remaining` int(11) NOT NULL DEFAULT '0' COMMENT '扣减后券余额',
  `detail` text COMMENT '扩展信息（未扣券原因/请求摘要等）',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_session_id` (`session_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_operator_admin_id` (`operator_admin_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人工补核销审计表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_class` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '班级名称',
  `summary` varchar(255) NOT NULL DEFAULT '' COMMENT '课程简介/副标题',
  `intro_html` mediumtext COMMENT '课程介绍富文本(HTML，后台维护)',
  `intro_images` varchar(2000) NOT NULL DEFAULT '' COMMENT '课程介绍多图(相对路径，逗号分隔)',
  `category_id` int(11) NOT NULL DEFAULT '0' COMMENT '项目ID(关联la_sports_category)',
  `instructor_id` int(11) NOT NULL DEFAULT '0' COMMENT '教练ID',
  `instructor_name` varchar(50) DEFAULT '' COMMENT '教练姓名(冗余显示)',
  `agent_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '绑定的代理ID',
  `community_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属社区ID',
  `area_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属小区ID（la_area.id，0=未绑定）',
  `age_min` tinyint(3) unsigned DEFAULT NULL COMMENT '适龄下限（岁，NULL=未维护）',
  `age_max` tinyint(3) unsigned DEFAULT NULL COMMENT '适龄上限（岁，NULL=未维护）',
  `level` varchar(20) NOT NULL DEFAULT '' COMMENT '课程难度/等级（空=回退运动类目名）',
  `venue` varchar(255) DEFAULT '' COMMENT '训练场馆',
  `latitude` varchar(20) DEFAULT NULL COMMENT '坐标：latitude',
  `longitude` varchar(20) DEFAULT NULL COMMENT '坐标：longitude',
  `image` varchar(500) DEFAULT '' COMMENT '课程图片',
  `class_type` tinyint(1) DEFAULT '0' COMMENT '班级类型: 0=正常班, 1=体验班，2：预售班级',
  `start_date` int(11) DEFAULT NULL COMMENT '开课日期',
  `end_date` int(11) DEFAULT NULL COMMENT '结束日期',
  `week_days` varchar(50) DEFAULT '' COMMENT '上课周期(如: 每周三、五)',
  `time_slot` varchar(500) DEFAULT '' COMMENT '上课时间段(统一时段或多时段摘要)',
  `schedule_mode` tinyint(1) NOT NULL DEFAULT '0' COMMENT '排课模式: 0=统一时段, 1=多时段',
  `schedule_rules` text COMMENT '多时段规则 JSON',
  `total_lessons` int(11) DEFAULT '0' COMMENT '总课节数',
  `fee_per_lesson` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '教练课时费(单节) - 教练提成',
  `total_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总费用(报名费)',
  `discount_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '折扣价',
  `student_count` int(11) DEFAULT '0' COMMENT '当前学员人数',
  `max_students` int(11) DEFAULT '2' COMMENT '最少开课人数',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态: 0=已结束, 1=进行中, 2=预报名',
  `is_show` tinyint(1) NOT NULL DEFAULT '1' COMMENT '上架/显示状态: 1=上架(小程序可见), 0=下架',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除: 0=未删除, 1=已删除',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_instructor` (`instructor_id`),
  KEY `idx_community` (`community_id`),
  KEY `idx_area` (`area_id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COMMENT='班级管理表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_consume_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_consume_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `student_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学员ID',
  `user_course_id` int(11) NOT NULL DEFAULT '0' COMMENT '报名记录ID',
  `course_class_id` int(11) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `lesson_no` int(11) NOT NULL DEFAULT '0' COMMENT '核销的课节',
  `source_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0=正常 1=人工扣课',
  `manual_id` int(11) NOT NULL DEFAULT '0' COMMENT '人工扣课记录ID',
  `consume_key` varchar(96) DEFAULT NULL COMMENT '扣课流水幂等键（manual:<manual_id>:<user_course_id>）',
  `create_time` int(10) DEFAULT NULL COMMENT '核销时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_consume_key` (`consume_key`),
  KEY `idx_user_class` (`user_id`,`course_class_id`),
  KEY `idx_user_class_student` (`user_id`,`course_class_id`,`student_id`),
  KEY `idx_user_course_id` (`user_course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1267 DEFAULT CHARSET=utf8mb4 COMMENT='课程核销流水表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_consume_log_bak_20260629`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_consume_log_bak_20260629` (
  `id` int(11) NOT NULL DEFAULT '0',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `course_class_id` int(11) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `lesson_no` int(11) NOT NULL DEFAULT '0' COMMENT '核销的课节',
  `source_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0=正常 1=人工扣课',
  `manual_id` int(11) NOT NULL DEFAULT '0' COMMENT '人工扣课记录ID',
  `create_time` int(10) DEFAULT NULL COMMENT '核销时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_lesson_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_lesson_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_class_id` int(11) NOT NULL COMMENT '班级ID',
  `instructor_id` int(11) NOT NULL COMMENT '教练ID',
  `lesson_no` int(11) NOT NULL DEFAULT '1' COMMENT '第几节课',
  `summary` varchar(500) DEFAULT '' COMMENT '课后总结',
  `images` varchar(2000) DEFAULT '' COMMENT '上课照片(多图逗号分隔)',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_course_class` (`course_class_id`),
  KEY `idx_instructor` (`instructor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8mb4 COMMENT='课程课后总结表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_makeup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_makeup` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `lesson_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '关联课时ID(ys_course_lesson.id)',
  `class_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '班级ID',
  `leave_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '关联请假记录ID',
  `instructor_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '教练ID',
  `lesson_no` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '原课时号',
  `original_date` varchar(10) NOT NULL DEFAULT '' COMMENT '原上课日期',
  `makeup_date` varchar(10) NOT NULL DEFAULT '' COMMENT '补课日期 yyyy-MM-dd',
  `makeup_time_slot` varchar(20) NOT NULL DEFAULT '' COMMENT '补课时段(如 14:00-16:00)',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0=待补课, 1=已补课',
  `settle_amount` decimal(10,2) DEFAULT '0.00' COMMENT '结算金额',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_lesson_id` (`lesson_id`),
  KEY `idx_leave_id` (`leave_id`),
  KEY `idx_class_id` (`class_id`),
  KEY `idx_instructor_id` (`instructor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='补课记录表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_manual_consume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_manual_consume` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `manual_id` int(11) NOT NULL DEFAULT '0' COMMENT '人工扣课记录ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `student_id` int(11) DEFAULT NULL COMMENT '学员ID',
  `user_course_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户课程报名ID',
  `consume_log_id` int(11) NOT NULL DEFAULT '0' COMMENT '课消流水ID',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_manual_user_course` (`manual_id`,`user_course_id`),
  UNIQUE KEY `uk_manual_student` (`manual_id`,`user_id`,`student_id`),
  KEY `idx_manual_id` (`manual_id`)
) ENGINE=InnoDB AUTO_INCREMENT=891 DEFAULT CHARSET=utf8mb4 COMMENT='人工扣课明细';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_manual_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_manual_lesson` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `request_key` varchar(64) DEFAULT NULL COMMENT '人工扣课请求幂等键',
  `class_id` int(11) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `lesson_date` varchar(10) NOT NULL DEFAULT '' COMMENT '实际上课日期 yyyy-MM-dd',
  `time_slot` varchar(50) NOT NULL DEFAULT '' COMMENT '上课时段',
  `instructor_id` int(11) NOT NULL DEFAULT '0' COMMENT '实际上课教练ID',
  `instructor_name` varchar(50) NOT NULL DEFAULT '' COMMENT '实际上课教练姓名(冗余)',
  `default_instructor_id` int(11) NOT NULL DEFAULT '0' COMMENT '班级默认教练ID',
  `venue` varchar(200) NOT NULL DEFAULT '' COMMENT '上课地点',
  `fee_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '教练结费金额',
  `student_count` int(11) NOT NULL DEFAULT '0' COMMENT '扣课学员数',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `operator_id` int(11) NOT NULL DEFAULT '0' COMMENT '操作管理员ID',
  `operator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '操作管理员姓名',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=已完成',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_request_key` (`request_key`),
  KEY `idx_class_date` (`class_id`,`lesson_date`),
  KEY `idx_instructor_id` (`instructor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=463 DEFAULT CHARSET=utf8mb4 COMMENT='人工加课扣课主记录';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_refund` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `course_class_id` int(11) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `order_id` int(11) NOT NULL DEFAULT '0' COMMENT '原课程订单ID',
  `class_name` varchar(100) NOT NULL DEFAULT '' COMMENT '班级名称(冗余)',
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户姓名(冗余)',
  `reason` varchar(50) NOT NULL DEFAULT '' COMMENT '退课原因标签',
  `detail` varchar(500) NOT NULL DEFAULT '' COMMENT '详细说明',
  `pay_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '已付金额',
  `refund_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '应退金额',
  `total_lessons` int(11) NOT NULL DEFAULT '0' COMMENT '总课时',
  `used_lessons` int(11) NOT NULL DEFAULT '0' COMMENT '已消课时',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态:0=待审批,1=已通过,2=已拒绝',
  `audit_id` int(11) DEFAULT NULL COMMENT '审批人ID',
  `audit_name` varchar(50) NOT NULL DEFAULT '' COMMENT '审批人姓名',
  `audit_time` int(11) DEFAULT NULL COMMENT '审批时间',
  `audit_remark` varchar(500) NOT NULL DEFAULT '' COMMENT '审批备注',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(11) DEFAULT NULL COMMENT '删除时间',
  `pending_order_guard` int(11) GENERATED ALWAYS AS ((case when ((`status` = 0) and isnull(`delete_time`) and (`order_id` > 0)) then `order_id` else NULL end)) STORED COMMENT '待审批单守卫(#152): status=0且未软删且order_id>0时=order_id否则NULL, uk_pending_order据此保证同单至多一笔待审批',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pending_order` (`pending_order_guard`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_class_id` (`course_class_id`),
  KEY `idx_status` (`status`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COMMENT='退课申请表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `course_class_id` int(11) NOT NULL COMMENT '班级ID',
  `lesson_no` int(11) NOT NULL DEFAULT '0' COMMENT '课节序号',
  `instructor_id` int(11) NOT NULL COMMENT '教练ID',
  `rating_teaching` tinyint(4) NOT NULL DEFAULT '0' COMMENT '教学质量评分(1-5)',
  `rating_atmosphere` tinyint(4) NOT NULL DEFAULT '0' COMMENT '课堂氛围评分(1-5)',
  `rating_attitude` tinyint(4) NOT NULL DEFAULT '0' COMMENT '教练态度评分(1-5)',
  `content` varchar(500) DEFAULT '' COMMENT '评价内容',
  `images` varchar(2000) DEFAULT '' COMMENT '评价图片(多张逗号分隔)',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  `recommend_count` int(11) NOT NULL DEFAULT '0' COMMENT '推荐此课程数',
  `like_count` int(11) NOT NULL DEFAULT '0' COMMENT '爱心点赞数',
  `reply_content` varchar(1000) DEFAULT '' COMMENT '教练回复内容',
  `reply_time` int(10) DEFAULT NULL COMMENT '教练回复时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_class` (`user_id`,`course_class_id`),
  KEY `idx_instructor` (`instructor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COMMENT='课程评价表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_course_temp_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_course_temp_lesson` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `class_id` int(11) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `lesson_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联 ys_course_lesson.id',
  `lesson_no` int(11) NOT NULL DEFAULT '0' COMMENT '课节号',
  `lesson_date` varchar(10) NOT NULL DEFAULT '' COMMENT '上课日期 yyyy-MM-dd',
  `time_slot` varchar(20) NOT NULL DEFAULT '' COMMENT '上课时段',
  `venue` varchar(200) NOT NULL DEFAULT '' COMMENT '上课地点',
  `latitude` varchar(32) NOT NULL DEFAULT '' COMMENT '纬度',
  `longitude` varchar(32) NOT NULL DEFAULT '' COMMENT '经度',
  `instructor_id` int(11) NOT NULL DEFAULT '0' COMMENT '授课教练ID',
  `instructor_name` varchar(50) NOT NULL DEFAULT '' COMMENT '授课教练姓名',
  `default_instructor_id` int(11) NOT NULL DEFAULT '0' COMMENT '班级默认教练ID',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `source_id` int(11) NOT NULL DEFAULT '0' COMMENT '复制来源 la_course_temp_lesson.id',
  `operator_id` int(11) NOT NULL DEFAULT '0' COMMENT '操作管理员ID',
  `operator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '操作管理员姓名',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0=待上课 1=已结课 2=已取消',
  `active_key` varchar(128) DEFAULT NULL COMMENT '待上课临时课唯一键（班级:日期:时段）；取消后置 NULL',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_lesson_id` (`lesson_id`),
  UNIQUE KEY `uk_temp_active` (`active_key`),
  KEY `idx_class_id` (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COMMENT='临时加课记录';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_decorate_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_decorate_page` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '10' COMMENT '页面类型 1=商城首页, 2=个人中心, 3=客服设置 4-PC首页',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '页面名称',
  `data` text COMMENT '页面数据',
  `meta` text COMMENT '页面设置',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='装修页面配置表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_decorate_tabbar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_decorate_tabbar` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '导航名称',
  `selected` varchar(200) NOT NULL DEFAULT '' COMMENT '未选图标',
  `unselected` varchar(200) NOT NULL DEFAULT '' COMMENT '已选图标',
  `link` varchar(200) DEFAULT NULL COMMENT '链接地址',
  `is_show` tinyint(255) unsigned NOT NULL DEFAULT '1' COMMENT '显示状态',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='装修底部导航表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '部门名称',
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级部门id',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `leader` varchar(64) DEFAULT NULL COMMENT '负责人',
  `mobile` varchar(16) DEFAULT NULL COMMENT '联系电话',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '部门状态（0停用 1正常）',
  `create_time` int(10) NOT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '修改时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_dev_crontab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_dev_crontab` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '定时任务名称',
  `type` tinyint(1) NOT NULL COMMENT '类型 1-定时任务',
  `system` tinyint(4) DEFAULT '0' COMMENT '是否系统任务 0-否 1-是',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `command` varchar(64) NOT NULL COMMENT '命令内容',
  `params` varchar(64) DEFAULT '' COMMENT '参数',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1-运行 2-停止 3-错误',
  `expression` varchar(64) NOT NULL COMMENT '运行规则',
  `error` varchar(256) DEFAULT NULL COMMENT '运行失败原因',
  `last_time` int(11) DEFAULT NULL COMMENT '最后执行时间',
  `time` varchar(64) DEFAULT '0' COMMENT '实时执行时长',
  `max_time` varchar(64) DEFAULT '0' COMMENT '最大执行时长',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='计划任务表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_dev_pay_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_dev_pay_config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '模版名称',
  `pay_way` tinyint(1) NOT NULL COMMENT '支付方式:1-余额支付;2-微信支付;3-支付宝支付;',
  `config` text COMMENT '对应支付配置(json字符串)',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `sort` int(5) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_dev_pay_way`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_dev_pay_way` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pay_config_id` int(11) NOT NULL COMMENT '支付配置ID',
  `scene` tinyint(1) NOT NULL COMMENT '场景:1-微信小程序;2-微信公众号;3-H5;4-PC;5-APP;',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认支付:0-否;1-是;',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态:0-关闭;1-开启;',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_dict_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) NOT NULL COMMENT '数据名称',
  `value` varchar(255) NOT NULL COMMENT '数据值',
  `type_id` int(11) NOT NULL COMMENT '字典类型id',
  `type_value` varchar(255) NOT NULL COMMENT '字典类型',
  `sort` int(10) DEFAULT '0' COMMENT '排序值',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0-停用 1-正常',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `create_time` int(10) NOT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '修改时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_dict_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '字典名称',
  `type` varchar(255) NOT NULL DEFAULT '' COMMENT '字典类型名称',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0-停用 1-正常',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `create_time` int(10) NOT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '修改时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_enroll_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_enroll_link` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '链接ID',
  `token` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '链接令牌（小程序 path 使用）',
  `session_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '课程场次ID（ys_course_lesson.id）',
  `course_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '课程班级ID（冗余，便于查询）',
  `creator_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '生成人后台管理员ID',
  `source` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'admin' COMMENT '来源：admin=客服后台',
  `expire_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '过期时间戳（0=长期有效）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0=停用，1=有效',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`),
  KEY `idx_session` (`session_id`),
  KEY `idx_status_expire` (`status`,`expire_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客服报名链接';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_enrollment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '提交ID',
  `link_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '报名链接ID（0=未带令牌）',
  `token` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '提交时携带的链接令牌',
  `session_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '课程场次ID',
  `course_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '课程班级ID',
  `parent_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '家长姓名',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '联系手机号',
  `student_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '学员姓名',
  `gender` varchar(4) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '学员性别：男/女',
  `age` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '学员年龄（3-16）',
  `note` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0=待客服确认，1=已确认，2=已拒绝',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '提交人账号ID（未登录=0）',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '提交时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_session_phone_student` (`session_id`,`phone`,`student_name`),
  KEY `idx_link` (`link_id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_session_create` (`session_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客服报名链接表单提交';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `cid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '类目ID',
  `source_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '上传者id',
  `source` tinyint(1) NOT NULL DEFAULT '0' COMMENT '来源类型[0-后台,1-用户]',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '10' COMMENT '类型[10=图片, 20=视频]',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名称',
  `uri` varchar(200) NOT NULL COMMENT '文件路径',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=431 DEFAULT CHARSET=utf8mb4 COMMENT='文件表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_file_cate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_file_cate` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `pid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '10' COMMENT '类型[10=图片，20=视频，30=文件]',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '分类名称',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) unsigned DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='文件分类表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_gen_table` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_name` varchar(200) NOT NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(200) NOT NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(200) NOT NULL DEFAULT '' COMMENT '关联表名称',
  `sub_table_fk` varchar(200) NOT NULL DEFAULT '' COMMENT '关联表外键',
  `sub_table_fr` varchar(200) NOT NULL DEFAULT '' COMMENT '关联表主键',
  `author_name` varchar(100) NOT NULL DEFAULT '' COMMENT '作者的名称',
  `entity_name` varchar(100) NOT NULL DEFAULT '' COMMENT '实体的名称',
  `module_name` varchar(60) NOT NULL DEFAULT '' COMMENT '生成模块名',
  `function_name` varchar(60) NOT NULL DEFAULT '' COMMENT '生成功能名',
  `tree_primary` varchar(60) NOT NULL DEFAULT '' COMMENT '树主键字段',
  `tree_parent` varchar(60) NOT NULL DEFAULT '' COMMENT '树父级字段',
  `tree_name` varchar(60) NOT NULL DEFAULT '' COMMENT '树显示字段',
  `gen_tpl` varchar(20) NOT NULL DEFAULT 'crud' COMMENT '生成模板方式: [crud=单表, tree=树表]',
  `gen_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '生成代码方式: [0=zip压缩包, 1=自定义路径]',
  `gen_path` varchar(200) NOT NULL DEFAULT '/' COMMENT '生成代码路径: [不填默认项目路径]',
  `menu_status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '菜单状态: [1=自动构建, 2=手动添加]',
  `menu_pid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '菜单父级',
  `menu_name` varchar(100) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `remarks` varchar(200) NOT NULL DEFAULT '' COMMENT '备注信息',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_gen_table_column` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '列主键',
  `table_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '表外键',
  `column_name` varchar(200) NOT NULL DEFAULT '' COMMENT '列名称',
  `column_comment` varchar(200) NOT NULL DEFAULT '' COMMENT '列描述',
  `column_length` varchar(5) DEFAULT '0' COMMENT '列长度',
  `column_type` varchar(100) NOT NULL DEFAULT '' COMMENT '列类型 ',
  `java_type` varchar(100) NOT NULL DEFAULT '' COMMENT 'JAVA类型',
  `java_field` varchar(100) NOT NULL DEFAULT '' COMMENT 'JAVA字段',
  `is_pk` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否主键: [1=是, 0=否]',
  `is_increment` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否自增: [1=是, 0=否]',
  `is_required` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否必填: [1=是, 0=否]',
  `is_insert` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否插入字段: [1=是, 0=否]',
  `is_edit` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否编辑字段: [1=是, 0=否]',
  `is_list` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否列表字段: [1=是, 0=否]',
  `is_query` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否查询字段: [1=是, 0=否]',
  `query_type` varchar(30) NOT NULL DEFAULT 'EQ' COMMENT '查询方式: [等于、不等于、大于、小于、范围]',
  `html_type` varchar(30) NOT NULL DEFAULT '' COMMENT '显示类型: [文本框、文本域、下拉框、复选框、单选框、日期控件]',
  `dict_type` varchar(200) NOT NULL DEFAULT '' COMMENT '字典类型',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序编号',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='代码生成字段表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_hot_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_hot_search` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '关键词',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序号',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='热门搜索表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(20) NOT NULL DEFAULT '0' COMMENT '申请教练的时候绑定的玩家ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '教练姓名',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '头像',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '联系电话',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `gender` tinyint(4) NOT NULL DEFAULT '1' COMMENT '性别:1=男,2=女',
  `specialty` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '专业领域',
  `experience_years` int(11) NOT NULL DEFAULT '0' COMMENT '从业年限',
  `introduction` text COLLATE utf8mb4_unicode_ci COMMENT '个人简介',
  `cert_images` text COLLATE utf8mb4_unicode_ci COMMENT '资质证书图片',
  `certification` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '教练资质(逗号分隔,如:国家一级教练员,FIBA认证教练)',
  `teachable_courses` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '可授课程（运动项目分类ID，逗号分隔）',
  `service_areas` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '服务区域（社区ID，逗号分隔）',
  `class_count` int(11) NOT NULL DEFAULT '0' COMMENT '负责班级数',
  `hourly_rate` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '课酬（元/课时）',
  `rating_score` decimal(3,2) NOT NULL DEFAULT '0.00' COMMENT '评分（0-5）',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态:0=停用,1=正常,2=待验证',
  `id_card` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证号码',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '当前可提现余额',
  `total_withdrawn` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '累计已提现总额',
  `must_change_password` tinyint(1) NOT NULL DEFAULT '0' COMMENT '首次登录是否必须修改初始密码:0=否,1=是',
  `account_opened_time` datetime DEFAULT NULL COMMENT '教练账号开通时间',
  `password_updated_time` datetime DEFAULT NULL COMMENT '登录密码最后更新时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '教练端最后登录时间',
  PRIMARY KEY (`id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_instructor_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教练表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_account_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_account_audit` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `instructor_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '教练ID(la_instructor.id)',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '账号ID(la_user.id)',
  `action` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '动作:OPEN/RESET/DISABLE/ENABLE/CHANGE_PASSWORD',
  `source` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '来源:ADMIN/COACH/SYSTEM',
  `operator_id` int(11) NOT NULL DEFAULT '0' COMMENT '操作人ID(后台管理员ID或登录账号ID)',
  `operator_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '操作人名称',
  `ip` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '操作IP',
  `detail` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '审计说明(不记录任何明文口令)',
  `result` tinyint(1) NOT NULL DEFAULT '1' COMMENT '结果:1=成功,0=失败',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_audit_instructor` (`instructor_id`,`id`),
  KEY `idx_audit_action` (`action`),
  KEY `idx_audit_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教练账号操作审计表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_account_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_account_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `instructor_id` bigint(20) NOT NULL COMMENT '教练ID',
  `type` tinyint(2) NOT NULL COMMENT '流水类型: 1=课时收入, 2=绩效奖励, 3=奖金补贴, 4=其他收入, 5=扣款项, 6=申请提现, 7=提现退回',
  `amount` decimal(10,2) NOT NULL COMMENT '变动金额(可正可负)',
  `before_balance` decimal(10,2) NOT NULL COMMENT '变动前余额',
  `after_balance` decimal(10,2) NOT NULL COMMENT '变动后余额',
  `relation_id` int(11) DEFAULT '0' COMMENT '关联ID(如课时日志ID / 提现单ID)',
  `settlement_key` varchar(96) DEFAULT NULL COMMENT '教练结费幂等键（manual:<manual_lesson_id>）',
  `remark` varchar(255) DEFAULT '' COMMENT '变动说明/账单名称',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间(账变时间)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_settlement_key` (`settlement_key`),
  KEY `idx_instructor_id` (`instructor_id`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=635 DEFAULT CHARSET=utf8mb4 COMMENT='教练资金账变流水记录表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_application` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '联系电话',
  `gender` tinyint(4) NOT NULL DEFAULT '1' COMMENT '性别:1=男,2=女',
  `specialty` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '专业领域',
  `experience_years` int(11) NOT NULL DEFAULT '0' COMMENT '从业年限',
  `introduction` text COLLATE utf8mb4_unicode_ci COMMENT '个人简介',
  `certificate_images` text COLLATE utf8mb4_unicode_ci COMMENT '证书图片（JSON）',
  `expected_hourly_rate` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '期望课酬',
  `apply_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态:0=待审核,1=通过,2=拒绝',
  `audit_admin_id` int(10) unsigned DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '审核意见',
  `approved_instructor_id` bigint(20) unsigned DEFAULT NULL COMMENT '转正后教练ID',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_apply_status` (`apply_status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教练申请表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `instructor_id` int(11) NOT NULL COMMENT '教练id',
  `openid` varchar(128) NOT NULL COMMENT '微信openid',
  `unionid` varchar(128) DEFAULT '' COMMENT '微信unionid',
  `terminal` tinyint(1) NOT NULL DEFAULT '1' COMMENT '客户端类型：1-微信小程序；2-微信公众号',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `openid` (`openid`),
  KEY `idx_instructor_id` (`instructor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='教练微信授权表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_bank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_bank` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `instructor_id` int(11) NOT NULL COMMENT '教练ID',
  `bank_name` varchar(50) NOT NULL DEFAULT '' COMMENT '银行名称',
  `bank_branch` varchar(100) DEFAULT '' COMMENT '开户支行名称',
  `card_no` varchar(30) NOT NULL DEFAULT '' COMMENT '银行卡号',
  `card_holder` varchar(30) NOT NULL DEFAULT '' COMMENT '持卡人姓名',
  `is_default` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否默认:0=否,1=是',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(11) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_instructor_id` (`instructor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='教练提现银行卡表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_cancel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_cancel` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `instructor_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '教练ID',
  `reason` varchar(50) NOT NULL DEFAULT '' COMMENT '注销原因标签',
  `detail` text COMMENT '详细说明',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0=待审核,1=已通过,2=已驳回',
  `audit_id` int(11) DEFAULT NULL COMMENT '审核管理员ID',
  `audit_name` varchar(50) DEFAULT '' COMMENT '审核人姓名',
  `audit_time` bigint(20) DEFAULT NULL COMMENT '审核时间戳',
  `audit_remark` varchar(255) DEFAULT '' COMMENT '审核备注',
  `create_time` bigint(20) NOT NULL DEFAULT '0',
  `update_time` bigint(20) NOT NULL DEFAULT '0',
  `delete_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='教练注销申请表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_cert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_cert` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `instructor_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '教练ID(la_instructor.id)',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '证书类型',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '证书名称',
  `no` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '证书编号',
  `org` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '发证机构',
  `expire_start` date DEFAULT NULL COMMENT '有效期起',
  `expire` date DEFAULT NULL COMMENT '有效期止',
  `front` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '证书照片(相对路径)',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态:0=草稿,1=待审核,2=已通过,3=已驳回',
  `audit_admin_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '审核管理员ID',
  `audit_admin_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '审核管理员名称快照',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '审核意见(驳回必填)',
  `submit_time` datetime DEFAULT NULL COMMENT '最后提交审核时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间(软删)',
  PRIMARY KEY (`id`),
  KEY `idx_cert_instructor` (`instructor_id`,`id`),
  KEY `idx_cert_status` (`status`,`id`),
  KEY `idx_cert_type` (`type`),
  KEY `idx_cert_no` (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教练资质证件表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_cert_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_cert_audit` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cert_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '资质ID(la_instructor_cert.id)',
  `instructor_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '教练ID(la_instructor.id)',
  `cert_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '证书名称快照',
  `action` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '动作:SUBMIT/APPROVE/REJECT',
  `source` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '来源:COACH/ADMIN/SYSTEM',
  `operator_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '操作人ID(教练ID或管理员ID)',
  `operator_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '操作人名称快照',
  `ip` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '操作IP',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '审核意见/提交说明',
  `status_before` tinyint(4) NOT NULL DEFAULT '0' COMMENT '变更前状态',
  `status_after` tinyint(4) NOT NULL DEFAULT '0' COMMENT '变更后状态',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_cert_audit_cert` (`cert_id`,`id`),
  KEY `idx_cert_audit_instructor` (`instructor_id`,`id`),
  KEY `idx_cert_audit_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教练资质审核留痕表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_leave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_leave` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instructor_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '教练ID',
  `start_time` int(10) NOT NULL DEFAULT '0' COMMENT '请假开始时间(时间戳)',
  `end_time` int(10) NOT NULL DEFAULT '0' COMMENT '请假结束时间(时间戳)',
  `reason` varchar(50) NOT NULL DEFAULT '' COMMENT '请假原因标签',
  `detail` varchar(500) NOT NULL DEFAULT '' COMMENT '详细说明',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态:0=待审批,1=已批准,2=已拒绝',
  `audit_id` int(11) DEFAULT NULL COMMENT '审批管理员ID',
  `audit_name` varchar(50) NOT NULL DEFAULT '' COMMENT '审批管理员姓名',
  `audit_time` int(10) DEFAULT NULL COMMENT '审批时间(时间戳)',
  `audit_remark` varchar(500) NOT NULL DEFAULT '' COMMENT '审批备注',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_instructor_id` (`instructor_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='教练请假表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_instructor_withdrawal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_instructor_withdrawal` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `instructor_id` int(11) NOT NULL COMMENT '教练ID',
  `sn` varchar(60) NOT NULL DEFAULT '' COMMENT '提现单号',
  `amount` decimal(10,2) NOT NULL COMMENT '提现金额',
  `bank_name` varchar(50) NOT NULL DEFAULT '' COMMENT '收款银行名称',
  `bank_card` varchar(30) NOT NULL DEFAULT '' COMMENT '银行卡号',
  `card_holder` varchar(30) NOT NULL DEFAULT '' COMMENT '持卡人',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态:0=待审核,1=审核通过(已打款),2=审核拒绝',
  `audit_id` int(11) DEFAULT NULL COMMENT '审核管理员ID',
  `audit_name` varchar(50) DEFAULT '' COMMENT '审核人姓名',
  `audit_remark` varchar(500) DEFAULT '' COMMENT '审核备注/拒绝事由',
  `audit_time` int(11) DEFAULT NULL COMMENT '处理打款时间',
  `create_time` int(11) DEFAULT NULL COMMENT '申请时间',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_instructor_id` (`instructor_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='教练提现工作台申请单表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '岗位名称',
  `code` varchar(64) NOT NULL COMMENT '岗位编码',
  `sort` int(11) DEFAULT '0' COMMENT '显示顺序',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0停用 1正常）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` int(10) NOT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '修改时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_leave_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_leave_application` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '申请人用户ID(学生)',
  `student_id` int(11) DEFAULT NULL COMMENT '关联学员ID',
  `instructor_id` int(11) NOT NULL DEFAULT '0' COMMENT '申请人教练ID(教练请假时填)',
  `applicant_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '申请人类型:1=学生,2=教练',
  `class_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联班级ID',
  `class_name` varchar(100) DEFAULT '' COMMENT '班级名称(冗余)',
  `applicant_name` varchar(50) DEFAULT '' COMMENT '申请人姓名(冗余)',
  `start_time` int(11) NOT NULL COMMENT '请假开始时间(时间戳)',
  `end_time` int(11) NOT NULL COMMENT '请假结束时间(时间戳)',
  `reason` varchar(50) NOT NULL DEFAULT '' COMMENT '请假原因标签(生病/事假等)',
  `detail` varchar(500) DEFAULT '' COMMENT '详细说明',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态:0=待审批,1=已批准,2=已拒绝',
  `audit_id` int(11) DEFAULT NULL COMMENT '审批人ID(教练或管理员)',
  `audit_name` varchar(50) DEFAULT '' COMMENT '审批人姓名',
  `audit_time` int(11) DEFAULT NULL COMMENT '审批时间',
  `audit_remark` varchar(500) DEFAULT '' COMMENT '审批备注',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(11) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_instructor_id` (`instructor_id`),
  KEY `idx_class_id` (`class_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_legacy_import_ledger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_legacy_import_ledger` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `batch_id` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '导入批次（一次 import 一个批次）',
  `source_db` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '源库（备份演练库）',
  `source_table` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '源表',
  `source_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '源表主键',
  `target_table` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '目标表',
  `target_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '目标表主键（老数据保留原 ID）',
  `natural_key` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '自然键快照（sn/openid/order_id 等）',
  `imported_at` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '导入时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_batch_target` (`batch_id`,`target_table`,`target_id`),
  KEY `idx_target` (`target_table`,`target_id`),
  KEY `idx_source` (`source_table`,`source_id`),
  KEY `idx_batch` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='T30.3 老数据导入台账（只追加，回滚按批次删除）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_message` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '消息类型:1=上课提醒,2=请假审批,3=付款成功,4=教练申请开办',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '消息标题',
  `content` text COMMENT '消息内容',
  `sender_id` int(10) unsigned DEFAULT NULL COMMENT '发送人ID(教练/学员)',
  `sender_type` varchar(20) DEFAULT '' COMMENT '发送人类型:instructor/student',
  `sender_name` varchar(50) DEFAULT '' COMMENT '发送人姓名',
  `receiver_id` int(10) unsigned DEFAULT NULL COMMENT '接收人ID(0=管理员)',
  `receiver_type` varchar(20) DEFAULT '' COMMENT '接收人类型:admin/instructor/student',
  `is_read` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0=未读,1=已读',
  `extra` text COMMENT '扩展数据(JSON)',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(20) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_receiver` (`receiver_type`,`receiver_id`),
  KEY `idx_sender` (`sender_type`,`sender_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1250 DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_notice_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_notice_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `scene_id` int(10) unsigned DEFAULT '0' COMMENT '场景',
  `read` tinyint(1) DEFAULT '0' COMMENT '已读状态;0-未读,1-已读',
  `recipient` tinyint(1) DEFAULT '0' COMMENT '通知接收对象类型;1-会员;2-商家;3-平台;4-游客(未注册用户)',
  `send_type` tinyint(1) DEFAULT '0' COMMENT '通知发送类型 1-系统通知 2-短信通知 3-微信模板 4-微信小程序',
  `notice_type` tinyint(1) DEFAULT NULL COMMENT '通知类型 1-业务通知 2-验证码',
  `extra` varchar(255) DEFAULT '' COMMENT '其他',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='通知记录表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_notice_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_notice_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scene_id` int(10) NOT NULL COMMENT '场景id',
  `scene_name` varchar(255) NOT NULL DEFAULT '' COMMENT '场景名称',
  `scene_desc` varchar(255) NOT NULL DEFAULT '' COMMENT '场景描述',
  `recipient` tinyint(1) NOT NULL DEFAULT '1' COMMENT '接收者 1-用户 2-平台',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '通知类型: 1-业务通知 2-验证码',
  `system_notice` text COMMENT '系统通知设置',
  `sms_notice` text COMMENT '短信通知设置',
  `oa_notice` text COMMENT '公众号通知设置',
  `mnp_notice` text COMMENT '小程序通知设置',
  `support` char(10) NOT NULL DEFAULT '' COMMENT '支持的发送类型 1-系统通知 2-短信通知 3-微信模板消息 4-小程序提醒',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='通知设置表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_official_account_reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_official_account_reply` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '规则名称',
  `keyword` varchar(64) NOT NULL DEFAULT '' COMMENT '关键词',
  `reply_type` tinyint(1) NOT NULL COMMENT '回复类型 1-关注回复 2-关键字回复 3-默认回复',
  `matching_type` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '匹配方式：1-全匹配；2-模糊匹配',
  `content_type` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '内容类型：1-文本',
  `content` text NOT NULL COMMENT '回复内容',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '启动状态：1-启动；0-关闭',
  `sort` int(11) unsigned NOT NULL DEFAULT '50' COMMENT '排序',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公众号消息回调表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_operation_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) NOT NULL COMMENT '管理员ID',
  `admin_name` varchar(16) NOT NULL DEFAULT '' COMMENT '管理员名称',
  `account` varchar(16) NOT NULL DEFAULT '' COMMENT '管理员账号',
  `action` varchar(64) DEFAULT '' COMMENT '操作名称',
  `type` varchar(8) NOT NULL COMMENT '请求方式',
  `url` varchar(600) NOT NULL COMMENT '访问链接',
  `params` text COMMENT '请求数据',
  `result` text COMMENT '请求结果',
  `ip` varchar(39) NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=577 DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_operation_log_advfix_backup_20261025`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_operation_log_advfix_backup_20261025` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) NOT NULL COMMENT '管理员ID',
  `admin_name` varchar(16) NOT NULL DEFAULT '' COMMENT '管理员名称',
  `account` varchar(16) NOT NULL DEFAULT '' COMMENT '管理员账号',
  `action` varchar(64) DEFAULT '' COMMENT '操作名称',
  `type` varchar(8) NOT NULL COMMENT '请求方式',
  `url` varchar(600) NOT NULL COMMENT '访问链接',
  `params` text COMMENT '请求数据',
  `result` text COMMENT '请求结果',
  `ip` varchar(39) NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=476 DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_recharge_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_recharge_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sn` varchar(64) NOT NULL COMMENT '订单编号',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `student_id` int(10) unsigned DEFAULT NULL COMMENT '学员ID',
  `course_class_id` int(11) NOT NULL DEFAULT '0' COMMENT '课程ID',
  `lesson_count` int(11) NOT NULL DEFAULT '0' COMMENT '购买课时数',
  `pay_sn` varchar(255) DEFAULT '' COMMENT '支付编号-冗余字段，针对微信同一主体不同客户端支付需用不同订单号预留。',
  `pay_way` tinyint(2) NOT NULL DEFAULT '2' COMMENT '支付方式 2-微信支付 3-支付宝支付',
  `pay_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付状态：0-待支付；1-已支付；4-已取消',
  `pay_time` int(10) DEFAULT NULL COMMENT '支付时间',
  `voucher` varchar(100) DEFAULT NULL COMMENT '凭证图片地址',
  `is_audit` int(11) NOT NULL DEFAULT '0' COMMENT '0：未对账，1：已对账',
  `order_amount` decimal(10,2) NOT NULL COMMENT '充值金额',
  `coupon_id` int(11) NOT NULL DEFAULT '0' COMMENT '使用的抵扣券ID(0=未使用)',
  `coupon_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '抵扣金额',
  `order_terminal` tinyint(1) DEFAULT '1' COMMENT '终端',
  `transaction_id` varchar(128) DEFAULT NULL COMMENT '第三方平台交易流水号',
  `refund_status` tinyint(1) DEFAULT '0' COMMENT '退款状态 0-未退款 1-已退款',
  `refund_transaction_id` varchar(255) DEFAULT NULL COMMENT '退款交易流水号',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  `coupon_goods_id` int(11) NOT NULL DEFAULT '0' COMMENT '券商品ID（0=非券订单）',
  `activity_id` int(11) NOT NULL DEFAULT '0' COMMENT '活动ID（0=非活动订单）',
  `activity_student_ids` varchar(512) NOT NULL DEFAULT '' COMMENT '活动报名学员ID，逗号分隔',
  `activity_count` int(11) NOT NULL DEFAULT '0' COMMENT '活动报名学员人数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=364 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_refund_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_refund_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sn` varchar(32) DEFAULT NULL COMMENT '编号',
  `record_id` int(11) NOT NULL COMMENT '退款记录id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联用户',
  `handle_id` int(11) NOT NULL DEFAULT '0' COMMENT '处理人id（管理员id）',
  `order_amount` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '订单总的应付款金额，冗余字段',
  `refund_amount` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '本次退款金额',
  `refund_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '退款状态，0退款中，1退款成功，2退款失败',
  `refund_msg` text COMMENT '退款信息',
  `create_time` int(10) unsigned DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_refund_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_refund_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sn` varchar(32) NOT NULL DEFAULT '' COMMENT '退款编号',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联用户',
  `order_id` int(11) NOT NULL DEFAULT '0' COMMENT '来源订单id',
  `refund_key` varchar(64) DEFAULT NULL COMMENT '后台退款幂等键（admin-order:<order_id>）',
  `order_sn` varchar(32) NOT NULL COMMENT '来源单号',
  `order_type` varchar(255) DEFAULT 'order' COMMENT '订单来源 order-商品订单 recharge-充值订单',
  `order_amount` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '订单总的应付款金额，冗余字段',
  `refund_amount` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '本次退款金额',
  `transaction_id` varchar(255) DEFAULT NULL COMMENT '第三方平台交易流水号',
  `refund_way` tinyint(1) NOT NULL DEFAULT '1' COMMENT '退款方式 1-线上退款 2-线下退款',
  `refund_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '退款类型 1-后台退款',
  `refund_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '退款状态，0退款中，1退款成功，2退款失败',
  `create_time` int(10) unsigned DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_refund_record_sn` (`sn`),
  UNIQUE KEY `uk_refund_key` (`refund_key`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_refund_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_refund_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '本地任务编号（RT+退课申请ID，幂等）',
  `refund_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '退课申请ID（la_course_refund.id）',
  `order_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '原支付订单ID（la_recharge_order.id）',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `course_class_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '班级ID',
  `pay_way` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '支付方式（2=微信，其它=无需线上退款）',
  `transaction_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '微信支付交易号（原支付）',
  `out_trade_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '原商户订单号',
  `out_refund_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '微信退款单号（重试必须复用，微信侧幂等）',
  `total_fee` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '订单总额（分）',
  `refund_fee` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '本次退款金额（分）',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态：0=待执行，1=执行中，2=成功，3=失败',
  `retry_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '已失败重试次数',
  `max_retry` int(11) unsigned NOT NULL DEFAULT '8' COMMENT '自动重试上限（达到后仅允许人工重试）',
  `next_retry_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '下次自动重试时间戳（0=不自动重试）',
  `last_error` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '最后一次失败原因',
  `result_msg` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '执行结果说明（成功信息/无需退款说明）',
  `execute_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '本次执行开始时间戳',
  `finish_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '任务结束时间戳（成功/最终失败）',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间戳',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refund_id` (`refund_id`),
  UNIQUE KEY `uk_task_no` (`task_no`),
  UNIQUE KEY `uk_out_refund_no` (`out_refund_no`),
  KEY `idx_status_retry` (`status`,`next_retry_time`),
  KEY `idx_order` (`order_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款任务 outbox（审核先落任务，异步执行微信退款）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_refund_task_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_refund_task_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `task_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '退款任务ID（la_refund_task.id）',
  `attempt_no` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '第几次执行（从 1 开始）',
  `action` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '动作：create=任务创建，execute=执行，retry=人工重试，requeue=超时清扫',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '流水状态：0=处理中，1=成功，2=失败',
  `out_refund_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '本次使用的微信退款单号',
  `message` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '结果/错误说明',
  `cost_ms` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '执行耗时（毫秒）',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_task` (`task_id`,`id`),
  KEY `idx_action_status` (`action`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款任务执行流水（只追加）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_region` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '上级ID（城市为0）',
  `level` tinyint(1) NOT NULL DEFAULT '1' COMMENT '层级：1=城市 2=分区 3=街道',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '名称',
  `center_lat` decimal(10,6) DEFAULT NULL COMMENT '中心纬度（城市用）',
  `center_lng` decimal(10,6) DEFAULT NULL COMMENT '中心经度（城市用）',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1=启用 0=停用',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) unsigned DEFAULT NULL COMMENT '删除时间（软删）',
  PRIMARY KEY (`id`),
  KEY `idx_parent` (`parent_id`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='地区层级表（城市/分区/街道）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_settlement_push_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_settlement_push_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `batch_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '批次号（未生成时为空）',
  `idempotency_key` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '幂等键',
  `dedup_key` varchar(96) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '重复上报去重键：幂等键+DRY/LIVE；SKIP: 前缀表示无 READY 数据',
  `period_start` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分账期间开始 yyyy-MM-dd',
  `period_end` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分账期间结束 yyyy-MM-dd',
  `batch_type` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL' COMMENT '批次类型：NORMAL/REVERSAL',
  `original_batch_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '冲正时原批次号',
  `scope_type` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ALL' COMMENT '范围类型：ALL/COMMUNITY',
  `scope_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '范围ID（社区ID，全量为0）',
  `item_count` int(11) NOT NULL DEFAULT '0' COMMENT '请求明细条数',
  `dry_run` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否演练：1=演练未出站 0=真实上报',
  `client_mode` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '适配器模式：disabled/stub/http',
  `status` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '上报状态：DRY_RUN/SKIPPED/DISABLED/CONFIG_ERROR/ACCEPTED/DUPLICATE/REJECTED/FAILED',
  `success` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否成功：1=成功',
  `duplicate` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否幂等命中：1=重复请求返回原回执',
  `code` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '业务码（S0000/S1002/L0001...）',
  `message` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '结果说明',
  `http_status` int(11) DEFAULT NULL COMMENT 'HTTP 状态码；未出站为 NULL',
  `response_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '回执业务码（响应体 code）',
  `request_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '社区系统请求ID',
  `receipt_no` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '社区系统回执号',
  `receipt_batch_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '回执回显批次号',
  `received_item_count` int(11) NOT NULL DEFAULT '-1' COMMENT '回执接收条数 receivedItemCount；-1=回执未提供',
  `item_count_mismatch` tinyint(4) NOT NULL DEFAULT '0' COMMENT '回执条数与请求条数是否不一致：1=不一致',
  `attempts` int(11) NOT NULL DEFAULT '0' COMMENT '尝试次数（含首次）',
  `elapsed_ms` bigint(20) NOT NULL DEFAULT '0' COMMENT '总耗时（毫秒）',
  `request_summary` text COLLATE utf8mb4_unicode_ci COMMENT '请求摘要 JSON（批次/范围/汇总，不含明细）',
  `response_summary` text COLLATE utf8mb4_unicode_ci COMMENT '回执摘要 JSON（脱敏，不含完整 body/secret）',
  `operator_admin_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '操作管理员ID',
  `operator_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '操作管理员姓名快照',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '写入时间（秒）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dedup_key` (`dedup_key`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_idempotency_key` (`idempotency_key`),
  KEY `idx_period` (`period_start`,`period_end`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分账上报结果日志';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_sms_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_sms_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `scene_id` int(11) NOT NULL COMMENT '场景id',
  `mobile` varchar(11) NOT NULL COMMENT '手机号码',
  `content` varchar(255) NOT NULL COMMENT '发送内容',
  `code` varchar(32) DEFAULT NULL COMMENT '发送关键字（注册、找回密码）',
  `is_verify` tinyint(1) DEFAULT '0' COMMENT '是否已验证；0-否；1-是',
  `check_num` int(5) DEFAULT '0' COMMENT '验证次数',
  `send_status` tinyint(1) NOT NULL COMMENT '发送状态：0-发送中；1-发送成功；2-发送失败',
  `send_time` int(10) NOT NULL COMMENT '发送时间',
  `results` text COMMENT '短信结果',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='短信记录表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_sports_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_sports_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` int(11) unsigned DEFAULT NULL COMMENT '父级ID(NULL=根)',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '项目名称(如：篮球、足球)',
  `icon` varchar(255) DEFAULT '' COMMENT '图标(设计稿中的球类图标)',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `is_show` tinyint(1) DEFAULT '1' COMMENT '是否显示: 0=隐藏, 1=显示',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='运动项目分类表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_student` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) unsigned NOT NULL COMMENT '所属家长(la_user.id)',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '学员姓名',
  `id_card` varchar(18) NOT NULL DEFAULT '' COMMENT '学员身份证号',
  `gender` tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别:0=未知,1=男,2=女',
  `birthday` varchar(20) DEFAULT NULL COMMENT '出生日期(yyyy-MM-dd)',
  `age` tinyint(3) unsigned DEFAULT NULL COMMENT '年龄（生日缺失时的兜底值）',
  `avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '学员头像',
  `note` varchar(100) NOT NULL DEFAULT '' COMMENT '学员备注',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认学员:0=否,1=是',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) unsigned DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8mb4 COMMENT='学员档案表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_system_log_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_system_log_login` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '管理员ID',
  `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '登录账号',
  `ip` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '登录IP',
  `os` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '操作系统',
  `browser` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '浏览器',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '操作状态: [1=成功, 0=失败]',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_system_log_operate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_system_log_operate` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '操作人ID',
  `type` varchar(30) NOT NULL DEFAULT '' COMMENT '请求类型: GET/POST/PUT',
  `title` varchar(30) DEFAULT '' COMMENT '操作标题',
  `ip` varchar(39) NOT NULL DEFAULT '' COMMENT '请求IP',
  `url` varchar(200) NOT NULL DEFAULT '' COMMENT '请求接口',
  `method` varchar(200) NOT NULL DEFAULT '' COMMENT '请求方法',
  `args` text COMMENT '请求参数',
  `error` text COMMENT '错误信息',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '执行状态: 1=成功, 2=失败',
  `start_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '开始时间',
  `end_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '结束时间',
  `task_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '执行耗时',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_system_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_system_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '上级菜单',
  `type` char(2) NOT NULL DEFAULT '' COMMENT '权限类型: M=目录，C=菜单，A=按钮',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `icon` varchar(100) NOT NULL DEFAULT '' COMMENT '菜单图标',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '菜单排序',
  `perms` varchar(100) NOT NULL DEFAULT '' COMMENT '权限标识',
  `paths` varchar(100) NOT NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(200) NOT NULL DEFAULT '' COMMENT '前端组件',
  `selected` varchar(200) NOT NULL DEFAULT '' COMMENT '选中路径',
  `params` varchar(200) NOT NULL DEFAULT '' COMMENT '路由参数',
  `is_cache` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否缓存: 0=否, 1=是',
  `is_show` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否显示: 0=否, 1=是',
  `is_disable` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否禁用: 0=否, 1=是',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=444 DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_system_menu_advfix_backup_20261025`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_system_menu_advfix_backup_20261025` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_system_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_system_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT '名称',
  `desc` varchar(128) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '描述',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_system_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_system_role_menu` (
  `role_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关系表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_system_role_menu_t36_backup_20261028`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_system_role_menu_t36_backup_20261028` (
  `role_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关系表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sn` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '编号',
  `avatar` varchar(200) NOT NULL DEFAULT '' COMMENT '头像',
  `real_name` varchar(32) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `nickname` varchar(32) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `account` varchar(32) NOT NULL DEFAULT '' COMMENT '用户账号',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '用户密码',
  `mobile` varchar(32) NOT NULL DEFAULT '' COMMENT '用户电话',
  `area_id` int(10) unsigned DEFAULT NULL COMMENT '常去小区ID（la_area.id）',
  `note` varchar(255) NOT NULL DEFAULT '' COMMENT '家长备注',
  `sex` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '用户性别: [1=男, 2=女]',
  `birthday` varchar(20) NOT NULL DEFAULT '' COMMENT '出生日期(格式:YYYY-MM-DD)',
  `channel` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '注册渠道: [1-微信小程序 2-微信公众号 3-手机H5 4-电脑PC 5-苹果APP 6-安卓APP]',
  `is_disable` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否禁用: [0=否, 1=是]',
  `login_ip` varchar(200) NOT NULL DEFAULT '' COMMENT '最后登录IP',
  `login_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最后登录时间',
  `is_new_user` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是新注册用户: [1-是, 0-否]',
  `user_money` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '用户余额',
  `total_recharge_amount` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '累计充值',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` int(10) unsigned DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `sn` (`sn`) USING BTREE COMMENT '编号唯一',
  UNIQUE KEY `account` (`account`) USING BTREE COMMENT '账号唯一'
) ENGINE=InnoDB AUTO_INCREMENT=20209 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_user_account_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_user_account_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sn` varchar(32) NOT NULL DEFAULT '' COMMENT '流水号',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `change_object` tinyint(1) NOT NULL DEFAULT '0' COMMENT '变动对象',
  `change_type` smallint(5) NOT NULL COMMENT '变动类型',
  `action` tinyint(1) NOT NULL DEFAULT '0' COMMENT '动作 1-增加 2-减少',
  `change_amount` decimal(10,2) NOT NULL COMMENT '变动数量',
  `left_amount` decimal(10,2) NOT NULL DEFAULT '100.00' COMMENT '变动后数量',
  `source_sn` varchar(255) DEFAULT NULL COMMENT '关联单号',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `extra` text COMMENT '预留扩展字段',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_user_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_user_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `openid` varchar(128) NOT NULL COMMENT '微信openid',
  `unionid` varchar(128) DEFAULT '' COMMENT '微信unionid',
  `terminal` tinyint(1) NOT NULL DEFAULT '1' COMMENT '客户端类型：1-微信小程序；2-微信公众号；3-手机H5；4-电脑PC；5-苹果APP；6-安卓APP',
  `create_time` int(10) DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `openid` (`openid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8mb4 COMMENT='用户授权表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_user_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_user_course` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `student_id` int(10) unsigned DEFAULT NULL COMMENT '学员ID',
  `course_class_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '课程班级ID',
  `order_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '关联订单ID',
  `total_lessons` int(11) NOT NULL DEFAULT '0' COMMENT '购买的总课时',
  `used_lessons` int(11) NOT NULL DEFAULT '0' COMMENT '已消课时',
  `remain_lessons` int(11) NOT NULL DEFAULT '0' COMMENT '剩余课时',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态: 0=学习中, 1=已完成，2=已退课',
  `create_time` int(10) unsigned DEFAULT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned DEFAULT NULL COMMENT '更新时间',
  `delete_time` int(10) unsigned DEFAULT NULL COMMENT '删除时间',
  `order_course_guard` int(11) GENERATED ALWAYS AS ((case when ((`order_id` > 0) and isnull(`delete_time`)) then `order_id` else NULL end)) STORED COMMENT '有效报名守卫(#153): order_id>0且未软删时=order_id否则NULL, uk_order_course据此保证同单至多一条有效报名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_course` (`order_course_guard`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_class_id` (`course_class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8mb4 COMMENT='用户课程报名记录';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `la_user_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `la_user_session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `terminal` tinyint(1) NOT NULL DEFAULT '1' COMMENT '客户端类型：1-微信小程序；2-微信公众号；3-手机H5；4-电脑PC；5-苹果APP；6-安卓APP',
  `token` varchar(191) NOT NULL COMMENT '令牌',
  `update_time` int(10) DEFAULT NULL COMMENT '更新时间',
  `expire_time` int(10) NOT NULL COMMENT '到期时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `admin_id_client` (`user_id`,`terminal`) USING BTREE COMMENT '一个用户在一个终端只有一个token',
  UNIQUE KEY `token` (`token`) USING BTREE COMMENT 'token是唯一的'
) ENGINE=InnoDB AUTO_INCREMENT=1701 DEFAULT CHARSET=utf8mb4 COMMENT='用户会话表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `ys_course_attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ys_course_attendance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `class_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '班级ID',
  `lesson_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课节ID',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学员用户ID',
  `lesson_date` varchar(10) NOT NULL DEFAULT '' COMMENT '上课日期 yyyy-MM-dd',
  `lesson_no` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '第几课时',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '签到状态: 0=未签到, 1=已到课, 2=请假, 3=缺勤',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_class_date` (`class_id`,`lesson_date`),
  KEY `idx_user` (`user_id`),
  KEY `idx_lesson_id` (`lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员签到记录';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `ys_course_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ys_course_lesson` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `class_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '班级ID',
  `lesson_no` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '第几节课',
  `lesson_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0=正课 1=临时加课',
  `lesson_date` varchar(10) NOT NULL DEFAULT '' COMMENT '上课日期 yyyy-MM-dd',
  `week_day` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '周几(1=周一...7=周日)',
  `time_slot` varchar(20) NOT NULL DEFAULT '' COMMENT '上课时段',
  `venue` varchar(200) NOT NULL DEFAULT '' COMMENT '上课地点(临时课可覆盖班级默认)',
  `latitude` varchar(32) NOT NULL DEFAULT '' COMMENT '纬度',
  `longitude` varchar(32) NOT NULL DEFAULT '' COMMENT '经度',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `operator_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建管理员ID',
  `operator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `source_lesson_id` int(11) NOT NULL DEFAULT '0' COMMENT '复制来源课节ID',
  `coach_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '授课教练ID',
  `coach_check_in` tinyint(3) NOT NULL DEFAULT '0' COMMENT '教练签到: 0=未签到, 1=已签到',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0=待上课, 1=已结课, 2=已取消',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_class_id` (`class_id`),
  KEY `idx_class_date` (`class_id`,`lesson_date`),
  KEY `idx_coach_date` (`coach_id`,`lesson_date`)
) ENGINE=InnoDB AUTO_INCREMENT=25714 DEFAULT CHARSET=utf8mb4 COMMENT='课节预生成表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

