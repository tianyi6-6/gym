-- 健身俱乐部管理系统数据库脚本
CREATE DATABASE IF NOT EXISTS gym CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gym;
-- 1. 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '管理员表';
-- 2. 教练表
CREATE TABLE IF NOT EXISTS `coach` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '教练ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `specialty` VARCHAR(200) DEFAULT NULL COMMENT '专长',
  `experience` TEXT COMMENT '经验介绍',
  `professional_score` INT(11) DEFAULT 80 COMMENT '专业能力评分',
  `teaching_score` INT(11) DEFAULT 80 COMMENT '教学经验评分',
  `communication_score` INT(11) DEFAULT 80 COMMENT '沟通能力评分',
  `service_score` INT(11) DEFAULT 80 COMMENT '服务态度评分',
  `performance_score` INT(11) DEFAULT 80 COMMENT '业绩表现评分',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '教练表';
-- 3. 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `balance` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '余额',
  `member_level` VARCHAR(20) DEFAULT '普通会员' COMMENT '会员等级：普通会员、银卡会员、金卡会员、钻石会员',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';
-- 4. 健身课程表
CREATE TABLE IF NOT EXISTS `course` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `name` VARCHAR(100) NOT NULL COMMENT '课程名称',
  `coach_id` INT(11) NOT NULL COMMENT '教练ID',
  `description` TEXT COMMENT '课程描述',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '价格',
  `duration` INT(11) DEFAULT NULL COMMENT '时长（分钟）',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '课程图片',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-上架，0-下架',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_coach_id` (`coach_id`),
  CONSTRAINT `fk_course_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '健身课程表';
-- 5. 课程订单表
CREATE TABLE IF NOT EXISTS `course_order` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `course_id` INT(11) NOT NULL COMMENT '课程ID',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '订单金额',
  `status` TINYINT(1) DEFAULT 0 COMMENT '状态：0-待支付，1-已支付，2-已取消',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_id` (`course_id`),
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
    '管理员',
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