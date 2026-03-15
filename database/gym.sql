/*
 Navicat MySQL Data Transfer
 
 Source Server         : localhost
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Database       : gym
 
 Target Server Type    : MYSQL
 Target Server Version : 80028
 File Encoding         : 65001
 
 Date: 2026-03-13 17:37:28
 */
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 5 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表';
-- ----------------------------
-- Table structure for body_assessment
-- ----------------------------
DROP TABLE IF EXISTS `body_assessment`;
CREATE TABLE `body_assessment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '体测数据ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `assessment_date` datetime NOT NULL COMMENT '体测日期',
  `body_fat_rate` decimal(5, 2) DEFAULT NULL COMMENT '体脂率（%）',
  `muscle_mass` decimal(5, 2) DEFAULT NULL COMMENT '肌肉量（kg）',
  `bone_density` decimal(5, 2) DEFAULT NULL COMMENT '骨密度',
  `basal_metabolic_rate` int DEFAULT NULL COMMENT '基础代谢率（kcal）',
  `bmi` decimal(5, 2) DEFAULT NULL COMMENT 'BMI指数',
  `visceral_fat_level` int DEFAULT NULL COMMENT '内脏脂肪等级',
  `body_water_rate` decimal(5, 2) DEFAULT NULL COMMENT '体水分率（%）',
  `protein_rate` decimal(5, 2) DEFAULT NULL COMMENT '蛋白质率（%）',
  `skeletal_muscle_mass` decimal(5, 2) DEFAULT NULL COMMENT '骨骼肌量（kg）',
  `weight` decimal(5, 2) DEFAULT NULL COMMENT '体重（kg）',
  `height` decimal(5, 2) DEFAULT NULL COMMENT '身高（cm）',
  `chest_circumference` decimal(5, 2) DEFAULT NULL COMMENT '胸围（cm）',
  `waist_circumference` decimal(5, 2) DEFAULT NULL COMMENT '腰围（cm）',
  `hip_circumference` decimal(5, 2) DEFAULT NULL COMMENT '臀围（cm）',
  `assessment_result` text COMMENT '体测评估结果',
  `coach_id` int DEFAULT NULL COMMENT '录入教练ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_assessment_date` (`assessment_date`),
  KEY `fk_body_assessment_coach` (`coach_id`),
  CONSTRAINT `fk_body_assessment_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`) ON DELETE
  SET NULL,
    CONSTRAINT `fk_body_assessment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '体测数据表';
-- ----------------------------
-- Table structure for coach
-- ----------------------------
DROP TABLE IF EXISTS `coach`;
CREATE TABLE `coach` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '教练ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `specialty` varchar(200) DEFAULT NULL COMMENT '专长',
  `experience` text COMMENT '经验介绍',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `professional_score` int DEFAULT '80' COMMENT '专业能力评分',
  `teaching_score` int DEFAULT '80' COMMENT '教学经验评分',
  `communication_score` int DEFAULT '80' COMMENT '沟通能力评分',
  `service_score` int DEFAULT '80' COMMENT '服务态度评分',
  `performance_score` int DEFAULT '80' COMMENT '业绩表现评分',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 11 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教练表';
-- ----------------------------
-- Table structure for coach_appointment
-- ----------------------------
DROP TABLE IF EXISTS `coach_appointment`;
CREATE TABLE `coach_appointment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `coach_id` int NOT NULL COMMENT '教练ID',
  `appointment_time` datetime NOT NULL COMMENT '预约时间',
  `duration` int DEFAULT '60' COMMENT '时长（分钟）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态：0-待确认，1-已确认，2-已取消',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coach_id` (`coach_id`),
  CONSTRAINT `fk_appointment_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`),
  CONSTRAINT `fk_appointment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教练预约表';
-- ----------------------------
-- Table structure for coach_course_schedule
-- ----------------------------
DROP TABLE IF EXISTS `coach_course_schedule`;
CREATE TABLE `coach_course_schedule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `coach_id` int NOT NULL,
  `course_id` int DEFAULT NULL,
  `course_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `location` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `max_students` int DEFAULT '10',
  `current_students` int DEFAULT '0',
  `status` tinyint(1) DEFAULT '1' COMMENT '1-正常 0-取消',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_schedule_date` (`schedule_date`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `coach_course_schedule_ibfk_1` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`) ON DELETE CASCADE,
  CONSTRAINT `coach_course_schedule_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 24 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教练课程安排表';
-- ----------------------------
-- Table structure for coach_course_trend
-- ----------------------------
DROP TABLE IF EXISTS `coach_course_trend`;
CREATE TABLE `coach_course_trend` (
  `id` int NOT NULL AUTO_INCREMENT,
  `coach_id` int NOT NULL,
  `year` int NOT NULL,
  `month` int NOT NULL,
  `course_count` int DEFAULT '0',
  `student_count` int DEFAULT '0',
  `revenue` decimal(10, 2) DEFAULT '0.00',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coach_year_month` (`coach_id`, `year`, `month`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_year_month` (`year`, `month`)
) ENGINE = InnoDB AUTO_INCREMENT = 7 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- ----------------------------
-- Table structure for coach_health_fitness
-- ----------------------------
DROP TABLE IF EXISTS `coach_health_fitness`;
CREATE TABLE `coach_health_fitness` (
  `id` int NOT NULL AUTO_INCREMENT,
  `coach_id` int NOT NULL,
  `record_date` date NOT NULL,
  `weight` decimal(5, 2) DEFAULT '0.00',
  `body_fat_rate` decimal(5, 2) DEFAULT '0.00',
  `heart_rate` int DEFAULT '0',
  `training_hours` decimal(10, 2) DEFAULT '0.00',
  `calories_burned` int DEFAULT '0',
  `muscle_mass` decimal(5, 2) DEFAULT '0.00',
  `bmi` decimal(5, 2) DEFAULT '0.00',
  `strength_score` int DEFAULT '0',
  `endurance_score` int DEFAULT '0',
  `flexibility_score` int DEFAULT '0',
  `speed_score` int DEFAULT '0',
  `coordination_score` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coach_date` (`coach_id`, `record_date`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_record_date` (`record_date`)
) ENGINE = InnoDB AUTO_INCREMENT = 21 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- ----------------------------
-- Table structure for coach_monthly_stats
-- ----------------------------
DROP TABLE IF EXISTS `coach_monthly_stats`;
CREATE TABLE `coach_monthly_stats` (
  `id` int NOT NULL AUTO_INCREMENT,
  `coach_id` int NOT NULL,
  `year` int NOT NULL,
  `month` int NOT NULL,
  `total_courses` int DEFAULT '0',
  `total_students` int DEFAULT '0',
  `attendance_rate` decimal(4, 1) DEFAULT '0.0',
  `satisfaction_score` decimal(3, 2) DEFAULT '0.00',
  `total_hours` decimal(10, 2) DEFAULT '0.00',
  `total_calories` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coach_year_month` (`coach_id`, `year`, `month`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_year_month` (`year`, `month`)
) ENGINE = InnoDB AUTO_INCREMENT = 7 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- ----------------------------
-- Table structure for coach_my_courses
-- ----------------------------
DROP TABLE IF EXISTS `coach_my_courses`;
CREATE TABLE `coach_my_courses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `coach_id` int NOT NULL,
  `course_id` int DEFAULT NULL,
  `course_name` varchar(100) NOT NULL,
  `course_type` varchar(50) DEFAULT NULL COMMENT '课程类型：力量训练、有氧运动、瑜伽、游泳等',
  `description` text,
  `price` decimal(10, 2) DEFAULT NULL,
  `duration` int DEFAULT NULL COMMENT '课程时长（分钟）',
  `difficulty_level` varchar(20) DEFAULT NULL COMMENT '难度级别：初级、中级、高级',
  `student_count` int DEFAULT '0' COMMENT '学员数量',
  `total_sessions` int DEFAULT '0' COMMENT '总课时数',
  `rating` decimal(3, 2) DEFAULT '0.00' COMMENT '课程评分',
  `status` tinyint(1) DEFAULT '1' COMMENT '1-正常 0-下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_course_id` (`course_id`),
  CONSTRAINT `coach_my_courses_ibfk_1` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`) ON DELETE CASCADE,
  CONSTRAINT `coach_my_courses_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 15 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教练我的课程表';
-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `name` varchar(100) NOT NULL COMMENT '课程名称',
  `coach_id` int NOT NULL COMMENT '教练ID',
  `description` text COMMENT '课程描述',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `duration` int DEFAULT NULL COMMENT '时长（分钟）',
  `image` mediumtext,
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-上架，0-下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_coach_id` (`coach_id`),
  CONSTRAINT `fk_course_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 16 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健身课程表';
-- ----------------------------
-- Table structure for course_order
-- ----------------------------
DROP TABLE IF EXISTS `course_order`;
CREATE TABLE `course_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` int NOT NULL COMMENT '用户ID',
  `course_id` int NOT NULL COMMENT '课程ID',
  `price` decimal(10, 2) NOT NULL COMMENT '订单金额',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态：0-待支付，1-已支付，2-已取消',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_id` (`course_id`),
<<<<<<< HEAD
  CONSTRAINT `fk_order_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 16 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程订单表';
-- ----------------------------
-- Table structure for equipment
-- ----------------------------
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '器材ID',
  `name` varchar(100) NOT NULL COMMENT '器材名称',
  `type` varchar(50) DEFAULT NULL COMMENT '器材类型',
  `description` text COMMENT '器材描述',
  `image` varchar(255) DEFAULT NULL COMMENT '器材图片',
  `total_count` int DEFAULT '1' COMMENT '总数量',
  `available_count` int DEFAULT '1' COMMENT '可用数量',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 4 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健身器材表';
-- ----------------------------
-- Table structure for equipment_appointment
-- ----------------------------
DROP TABLE IF EXISTS `equipment_appointment`;
CREATE TABLE `equipment_appointment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `equipment_id` int NOT NULL COMMENT '器材ID',
  `appointment_time` datetime NOT NULL COMMENT '预约时间',
  `duration` int DEFAULT '60' COMMENT '时长（分钟）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态：0-待确认，1-已确认，2-已取消',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_equipment_id` (`equipment_id`),
  CONSTRAINT `fk_eq_appointment_equipment` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`),
  CONSTRAINT `fk_eq_appointment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '器材预约表';
-- ----------------------------
-- Table structure for experience
-- ----------------------------
DROP TABLE IF EXISTS `experience`;
CREATE TABLE `experience` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '经验ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID（可为空，管理员发布）',
  `coach_id` int DEFAULT NULL COMMENT '教练ID（可为空）',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `image` varchar(255) DEFAULT NULL COMMENT '图片',
  `view_count` int DEFAULT '0' COMMENT '浏览次数',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-发布，0-草稿',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coach_id` (`coach_id`),
  CONSTRAINT `fk_experience_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`) ON DELETE
  SET NULL,
    CONSTRAINT `fk_experience_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE
  SET NULL
) ENGINE = InnoDB AUTO_INCREMENT = 6 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健身经验表';
-- ----------------------------
-- Table structure for health_goal
-- ----------------------------
DROP TABLE IF EXISTS `health_goal`;
CREATE TABLE `health_goal` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '健康目标ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `goal_type` varchar(50) NOT NULL COMMENT '目标类型：weight-体重，body_fat-体脂率，muscle-肌肉量',
  `current_value` decimal(10, 2) DEFAULT NULL COMMENT '当前值',
  `target_value` decimal(10, 2) NOT NULL COMMENT '目标值',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '目标截止日期',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-进行中，2-已完成，3-已放弃',
  `progress` decimal(5, 2) DEFAULT '0.00' COMMENT '完成进度（%）',
  `achievement_date` datetime DEFAULT NULL COMMENT '达成日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_health_goal_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健康目标表';
-- ----------------------------
-- Table structure for health_profile
-- ----------------------------
DROP TABLE IF EXISTS `health_profile`;
CREATE TABLE `health_profile` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '健康档案ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `age` int DEFAULT NULL COMMENT '年龄',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别：1-男，2-女',
  `height` decimal(5, 2) DEFAULT NULL COMMENT '身高（cm）',
  `weight` decimal(5, 2) DEFAULT NULL COMMENT '体重（kg）',
  `occupation` varchar(100) DEFAULT NULL COMMENT '职业',
  `medical_history` text COMMENT '既往病史',
  `allergy_history` text COMMENT '过敏史',
  `surgery_history` text COMMENT '手术史',
  `exercise_history` text COMMENT '运动经历',
  `exercise_habits` text COMMENT '运动习惯',
  `exercise_preference` text COMMENT '运动偏好',
  `sleep_habits` varchar(200) DEFAULT NULL COMMENT '作息时间',
  `diet_habits` varchar(200) DEFAULT NULL COMMENT '饮食习惯',
  `smoking_status` tinyint(1) DEFAULT NULL COMMENT '吸烟状况：0-不吸烟，1-偶尔吸烟，2-经常吸烟',
  `drinking_status` tinyint(1) DEFAULT NULL COMMENT '饮酒状况：0-不饮酒，1-偶尔饮酒，2-经常饮酒',
  `risk_level` tinyint(1) DEFAULT '1' COMMENT '风险等级：1-低风险，2-中风险，3-高风险',
  `risk_factors` text COMMENT '风险因素',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  CONSTRAINT `fk_health_profile_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员健康档案表';
-- ----------------------------
-- Table structure for health_risk_warning
-- ----------------------------
DROP TABLE IF EXISTS `health_risk_warning`;
CREATE TABLE `health_risk_warning` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `risk_type` varchar(50) NOT NULL COMMENT '风险类型：obesity-肥胖，cardiovascular-心血管，injury-运动损伤',
  `risk_level` tinyint(1) NOT NULL COMMENT '风险等级：1-低风险，2-中风险，3-高风险',
  `risk_description` text COMMENT '风险描述',
  `warning_date` datetime NOT NULL COMMENT '预警日期',
  `is_handled` tinyint(1) DEFAULT '0' COMMENT '是否已处理：0-未处理，1-已处理',
  `handle_date` datetime DEFAULT NULL COMMENT '处理日期',
  `handle_result` text COMMENT '处理结果',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_warning_date` (`warning_date`),
  KEY `idx_is_handled` (`is_handled`),
  CONSTRAINT `fk_health_risk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健康风险预警表';
-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `type` tinyint(1) DEFAULT '1' COMMENT '类型：1-普通，2-重要',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-发布，0-草稿',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 5 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统公告表';
-- ----------------------------
-- Table structure for recharge
-- ----------------------------
DROP TABLE IF EXISTS `recharge`;
CREATE TABLE `recharge` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '充值ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '充值金额',
  `type` tinyint(1) DEFAULT '1' COMMENT '类型：1-充值，2-消费',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_recharge_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 7 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '充值记录表';
-- ----------------------------
-- Table structure for recipe
-- ----------------------------
DROP TABLE IF EXISTS `recipe`;
CREATE TABLE `recipe` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '菜谱ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `content` longtext COMMENT '内容（富文本）',
  `image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `category` varchar(50) DEFAULT NULL COMMENT '分类',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-发布，0-草稿',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 5 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健身菜谱表';
-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `balance` decimal(10, 2) DEFAULT '0.00' COMMENT '余额',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `member_level` varchar(20) DEFAULT '普通会员' COMMENT '会员等级：普通会员、银卡会员、金卡会员、钻石会员',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 22 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表';
=======
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_order_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '课程订单表';
-- 6. 系统公告表
CREATE TABLE IF NOT EXISTS `notice` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` TEXT COMMENT '内容',
  `type` TINYINT(1) DEFAULT 1 COMMENT '类型：1-普通，2-重要',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-发布，0-草稿',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '系统公告表';
-- 7. 教练预约表
CREATE TABLE IF NOT EXISTS `coach_appointment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `coach_id` INT(11) NOT NULL COMMENT '教练ID',
  `appointment_time` DATETIME NOT NULL COMMENT '预约时间',
  `duration` INT(11) DEFAULT 60 COMMENT '时长（分钟）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `status` TINYINT(1) DEFAULT 0 COMMENT '状态：0-待确认，1-已确认，2-已取消',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coach_id` (`coach_id`),
  CONSTRAINT `fk_appointment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_appointment_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '教练预约表';
-- 8. 健身器材表
CREATE TABLE IF NOT EXISTS `equipment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '器材ID',
  `name` VARCHAR(100) NOT NULL COMMENT '器材名称',
  `type` VARCHAR(50) DEFAULT NULL COMMENT '器材类型',
  `description` TEXT COMMENT '器材描述',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '器材图片',
  `total_count` INT(11) DEFAULT 1 COMMENT '总数量',
  `available_count` INT(11) DEFAULT 1 COMMENT '可用数量',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '健身器材表';
-- 9. 器材预约表
CREATE TABLE IF NOT EXISTS `equipment_appointment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `equipment_id` INT(11) NOT NULL COMMENT '器材ID',
  `appointment_time` DATETIME NOT NULL COMMENT '预约时间',
  `duration` INT(11) DEFAULT 60 COMMENT '时长（分钟）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `status` TINYINT(1) DEFAULT 0 COMMENT '状态：0-待确认，1-已确认，2-已取消',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_equipment_id` (`equipment_id`),
  CONSTRAINT `fk_eq_appointment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_eq_appointment_equipment` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '器材预约表';
-- 10. 健身菜谱表
CREATE TABLE IF NOT EXISTS `recipe` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '菜谱ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` LONGTEXT COMMENT '内容（富文本）',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片',
  `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-发布，0-草稿',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '健身菜谱表';
-- 11. 健身经验表
CREATE TABLE IF NOT EXISTS `experience` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '经验ID',
  `user_id` INT(11) DEFAULT NULL COMMENT '用户ID（可为空，管理员发布）',
  `coach_id` INT(11) DEFAULT NULL COMMENT '教练ID（可为空）',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` TEXT COMMENT '内容',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '图片',
  `view_count` INT(11) DEFAULT 0 COMMENT '浏览次数',
  `like_count` INT(11) DEFAULT 0 COMMENT '点赞数',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-发布，0-草稿',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coach_id` (`coach_id`),
  CONSTRAINT `fk_experience_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE
  SET NULL,
    CONSTRAINT `fk_experience_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`) ON DELETE
  SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '健身经验表';
-- 12. 充值记录表
CREATE TABLE IF NOT EXISTS `recharge` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '充值ID',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `amount` DECIMAL(10, 2) NOT NULL COMMENT '充值金额',
  `type` TINYINT(1) DEFAULT 1 COMMENT '类型：1-充值，2-消费',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_recharge_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '充值记录表';
-- 插入初始数据
-- 注意：密码使用明文存储，默认密码都是123456
-- 管理员
INSERT INTO `admin` (`username`, `password`, `name`, `phone`)
VALUES (
    'admin',
    '123456',
    'admin',
    '13800138000'
  );
-- 教练
INSERT INTO `coach` (
    `username`,
    `password`,
    `name`,
    `phone`,
    `specialty`,
    `experience`,
    `status`
  )
VALUES (
    'zhang',
    '123456',
    '张教练',
    '13800138001',
    '力量训练,有氧运动',
    '拥有10年健身指导经验，擅长力量训练和有氧运动。',
    1
  );
-- 用户
INSERT INTO `user` (
    `username`,
    `password`,
    `name`,
    `phone`,
    `balance`,
    `status`
  )
VALUES (
    'zhangsan',
    '123456',
    '张三',
    '13800138002',
    1000.00,
    1
  ),
  (
    'lisi',
    '123456',
    '李四',
    '13800138003',
    2000.00,
    1
  ),
  (
    'wangwu',
    '123456',
    '王五',
    '13800138004',
    1500.00,
    1
  ),
  (
    'zhaoliu',
    '123456',
    '赵六',
    '13800138005',
    800.00,
    1
  ),
  (
    'qianqi',
    '123456',
    '钱七',
    '13800138006',
    3000.00,
    1
  );
-- 系统公告
INSERT INTO `notice` (`title`, `content`, `type`, `status`)
VALUES ('欢迎使用健身俱乐部管理系统', '欢迎使用健身俱乐部管理系统，祝您健身愉快！', 1, 1);
-- 健身器材
INSERT INTO `equipment` (
    `name`,
    `type`,
    `description`,
    `total_count`,
    `available_count`
  )
VALUES ('跑步机', '有氧器械', '专业跑步机，适合有氧运动', 10, 10),
  ('哑铃', '力量器械', '可调节重量哑铃', 20, 20),
  ('杠铃', '力量器械', '标准杠铃，适合力量训练', 5, 5);
-- 健身课程
INSERT INTO `course` (
    `name`,
    `coach_id`,
    `description`,
    `price`,
    `duration`,
    `status`
  )
VALUES ('初级力量训练', 1, '适合初学者的力量训练课程', 200.00, 60, 1),
  ('有氧运动课程', 1, '高效有氧运动训练', 150.00, 45, 1),
  ('高级力量训练', 1, '适合进阶学员的力量训练课程', 300.00, 90, 1),
  ('瑜伽课程', 1, '身心放松的瑜伽训练', 180.00, 60, 1),
  ('HIIT高强度间歇训练', 1, '快速燃脂的高强度训练', 250.00, 45, 1);
-- 课程订单
INSERT INTO `course_order` (
    `order_no`,
    `user_id`,
    `course_id`,
    `price`,
    `status`,
    `create_time`
  )
VALUES (
    'ORD20250126001',
    1,
    1,
    200.00,
    1,
    '2025-01-26 10:00:00'
  ),
  (
    'ORD20250126002',
    1,
    2,
    150.00,
    0,
    '2025-01-26 11:00:00'
  ),
  (
    'ORD20250126003',
    2,
    1,
    200.00,
    1,
    '2025-01-26 12:00:00'
  ),
  (
    'ORD20250126004',
    2,
    3,
    300.00,
    2,
    '2025-01-26 13:00:00'
  ),
  (
    'ORD20250126005',
    3,
    2,
    150.00,
    1,
    '2025-01-26 14:00:00'
  ),
  (
    'ORD20250126006',
    3,
    4,
    180.00,
    0,
    '2025-01-26 15:00:00'
  ),
  (
    'ORD20250126007',
    4,
    1,
    200.00,
    1,
    '2025-01-26 16:00:00'
  ),
  (
    'ORD20250126008',
    5,
    5,
    250.00,
    1,
    '2025-01-26 17:00:00'
  ),
  (
    'ORD20250126009',
    5,
    3,
    300.00,
    0,
    '2025-01-26 18:00:00'
  ),
  (
    'ORD20250126010',
    1,
    5,
    250.00,
    2,
    '2025-01-26 19:00:00'
  );
-- 健身菜谱
INSERT INTO `recipe` (`title`, `content`, `category`, `status`)
VALUES ('健身餐-鸡胸肉沙拉', '<p>营养丰富的健身餐，低脂高蛋白</p>', '减脂餐', 1);
-- 健身经验
INSERT INTO `experience` (`coach_id`, `title`, `content`, `status`)
VALUES (1, '如何正确进行力量训练', '力量训练需要注意动作标准，循序渐进...', 1);
>>>>>>> 0985784786d1680d4c0546437e49eb5c0069fb7d
