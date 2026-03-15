-- 会员健康档案与体测数据管理模块数据库脚本
USE gym;

-- 13. 会员健康档案表
CREATE TABLE IF NOT EXISTS `health_profile` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '健康档案ID',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `age` INT(11) DEFAULT NULL COMMENT '年龄',
  `gender` TINYINT(1) DEFAULT NULL COMMENT '性别：1-男，2-女',
  `height` DECIMAL(5, 2) DEFAULT NULL COMMENT '身高（cm）',
  `weight` DECIMAL(5, 2) DEFAULT NULL COMMENT '体重（kg）',
  `occupation` VARCHAR(100) DEFAULT NULL COMMENT '职业',
  `medical_history` TEXT DEFAULT NULL COMMENT '既往病史',
  `allergy_history` TEXT DEFAULT NULL COMMENT '过敏史',
  `surgery_history` TEXT DEFAULT NULL COMMENT '手术史',
  `exercise_history` TEXT DEFAULT NULL COMMENT '运动经历',
  `exercise_habits` TEXT DEFAULT NULL COMMENT '运动习惯',
  `exercise_preference` TEXT DEFAULT NULL COMMENT '运动偏好',
  `sleep_habits` VARCHAR(200) DEFAULT NULL COMMENT '作息时间',
  `diet_habits` VARCHAR(200) DEFAULT NULL COMMENT '饮食习惯',
  `smoking_status` TINYINT(1) DEFAULT NULL COMMENT '吸烟状况：0-不吸烟，1-偶尔吸烟，2-经常吸烟',
  `drinking_status` TINYINT(1) DEFAULT NULL COMMENT '饮酒状况：0-不饮酒，1-偶尔饮酒，2-经常饮酒',
  `risk_level` TINYINT(1) DEFAULT 1 COMMENT '风险等级：1-低风险，2-中风险，3-高风险',
  `risk_factors` TEXT DEFAULT NULL COMMENT '风险因素',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  CONSTRAINT `fk_health_profile_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '会员健康档案表';

-- 14. 体测数据表
CREATE TABLE IF NOT EXISTS `body_assessment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '体测数据ID',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `assessment_date` DATETIME NOT NULL COMMENT '体测日期',
  `body_fat_rate` DECIMAL(5, 2) DEFAULT NULL COMMENT '体脂率（%）',
  `muscle_mass` DECIMAL(5, 2) DEFAULT NULL COMMENT '肌肉量（kg）',
  `bone_density` DECIMAL(5, 2) DEFAULT NULL COMMENT '骨密度',
  `basal_metabolic_rate` INT(11) DEFAULT NULL COMMENT '基础代谢率（kcal）',
  `bmi` DECIMAL(5, 2) DEFAULT NULL COMMENT 'BMI指数',
  `visceral_fat_level` INT(11) DEFAULT NULL COMMENT '内脏脂肪等级',
  `body_water_rate` DECIMAL(5, 2) DEFAULT NULL COMMENT '体水分率（%）',
  `protein_rate` DECIMAL(5, 2) DEFAULT NULL COMMENT '蛋白质率（%）',
  `skeletal_muscle_mass` DECIMAL(5, 2) DEFAULT NULL COMMENT '骨骼肌量（kg）',
  `weight` DECIMAL(5, 2) DEFAULT NULL COMMENT '体重（kg）',
  `height` DECIMAL(5, 2) DEFAULT NULL COMMENT '身高（cm）',
  `chest_circumference` DECIMAL(5, 2) DEFAULT NULL COMMENT '胸围（cm）',
  `waist_circumference` DECIMAL(5, 2) DEFAULT NULL COMMENT '腰围（cm）',
  `hip_circumference` DECIMAL(5, 2) DEFAULT NULL COMMENT '臀围（cm）',
  `assessment_result` TEXT DEFAULT NULL COMMENT '体测评估结果',
  `coach_id` INT(11) DEFAULT NULL COMMENT '录入教练ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_assessment_date` (`assessment_date`),
  CONSTRAINT `fk_body_assessment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_body_assessment_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '体测数据表';

-- 15. 健康目标表
CREATE TABLE IF NOT EXISTS `health_goal` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '健康目标ID',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `goal_type` VARCHAR(50) NOT NULL COMMENT '目标类型：weight-体重，body_fat-体脂率，muscle-肌肉量',
  `current_value` DECIMAL(10, 2) DEFAULT NULL COMMENT '当前值',
  `target_value` DECIMAL(10, 2) NOT NULL COMMENT '目标值',
  `unit` VARCHAR(20) DEFAULT NULL COMMENT '单位',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE NOT NULL COMMENT '目标截止日期',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-进行中，2-已完成，3-已放弃',
  `progress` DECIMAL(5, 2) DEFAULT 0.00 COMMENT '完成进度（%）',
  `achievement_date` DATETIME DEFAULT NULL COMMENT '达成日期',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_health_goal_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '健康目标表';

-- 16. 健康风险预警表
CREATE TABLE IF NOT EXISTS `health_risk_warning` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `risk_type` VARCHAR(50) NOT NULL COMMENT '风险类型：obesity-肥胖，cardiovascular-心血管，injury-运动损伤',
  `risk_level` TINYINT(1) NOT NULL COMMENT '风险等级：1-低风险，2-中风险，3-高风险',
  `risk_description` TEXT DEFAULT NULL COMMENT '风险描述',
  `warning_date` DATETIME NOT NULL COMMENT '预警日期',
  `is_handled` TINYINT(1) DEFAULT 0 COMMENT '是否已处理：0-未处理，1-已处理',
  `handle_date` DATETIME DEFAULT NULL COMMENT '处理日期',
  `handle_result` TEXT DEFAULT NULL COMMENT '处理结果',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_warning_date` (`warning_date`),
  KEY `idx_is_handled` (`is_handled`),
  CONSTRAINT `fk_health_risk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '健康风险预警表';

-- 插入测试数据
-- 为用户zhangsan创建健康档案
INSERT INTO `health_profile` (
  `user_id`, `age`, `gender`, `height`, `weight`, `occupation`, 
  `medical_history`, `exercise_history`, `exercise_habits`, 
  `sleep_habits`, `diet_habits`, `smoking_status`, `drinking_status`
) VALUES (
  1, 28, 1, 175.0, 75.0, '程序员',
  '无', '大学期间曾坚持跑步2年', '每周运动2-3次',
  '23:00-07:00', '饮食规律，偶尔外卖', 0, 1
);

-- 为用户zhangsan创建体测数据
INSERT INTO `body_assessment` (
  `user_id`, `assessment_date`, `body_fat_rate`, `muscle_mass`, 
  `basal_metabolic_rate`, `bmi`, `visceral_fat_level`, 
  `body_water_rate`, `protein_rate`, `skeletal_muscle_mass`,
  `weight`, `height`, `coach_id`
) VALUES (
  1, '2024-01-15 10:00:00', 18.5, 35.2, 1750, 24.5, 8, 
  55.2, 16.8, 32.5, 75.0, 175.0, 1
);

INSERT INTO `body_assessment` (
  `user_id`, `assessment_date`, `body_fat_rate`, `muscle_mass`, 
  `basal_metabolic_rate`, `bmi`, `visceral_fat_level`, 
  `body_water_rate`, `protein_rate`, `skeletal_muscle_mass`,
  `weight`, `height`, `coach_id`
) VALUES (
  1, '2024-02-15 10:00:00', 17.8, 36.0, 1780, 24.2, 7, 
  56.0, 17.2, 33.2, 74.2, 175.0, 1
);

INSERT INTO `body_assessment` (
  `user_id`, `assessment_date`, `body_fat_rate`, `muscle_mass`, 
  `basal_metabolic_rate`, `bmi`, `visceral_fat_level`, 
  `body_water_rate`, `protein_rate`, `skeletal_muscle_mass`,
  `weight`, `height`, `coach_id`
) VALUES (
  1, '2024-03-15 10:00:00', 17.2, 36.8, 1800, 23.8, 6, 
  56.8, 17.5, 33.8, 73.5, 175.0, 1
);

-- 为用户zhangsan创建健康目标
INSERT INTO `health_goal` (
  `user_id`, `goal_type`, `current_value`, `target_value`, 
  `unit`, `start_date`, `end_date`, `status`, `progress`
) VALUES (
  1, 'weight', 75.0, 70.0, 'kg', '2024-01-01', '2024-06-30', 1, 66.67
);

INSERT INTO `health_goal` (
  `user_id`, `goal_type`, `current_value`, `target_value`, 
  `unit`, `start_date`, `end_date`, `status`, `progress`
) VALUES (
  1, 'body_fat', 18.5, 15.0, '%', '2024-01-01', '2024-06-30', 1, 43.33
);

-- 为用户zhangsan创建健康风险预警
INSERT INTO `health_risk_warning` (
  `user_id`, `risk_type`, `risk_level`, `risk_description`, 
  `warning_date`, `is_handled`
) VALUES (
  1, 'obesity', 1, 'BMI略高，建议控制饮食并增加有氧运动', 
  '2024-01-15 10:00:00', 1
);
