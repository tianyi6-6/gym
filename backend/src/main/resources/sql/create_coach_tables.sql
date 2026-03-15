-- 教练课程趋势表
CREATE TABLE IF NOT EXISTS coach_course_trend (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    coach_id INT NOT NULL COMMENT '教练ID',
    year INT NOT NULL COMMENT '年份',
    month INT NOT NULL COMMENT '月份',
    course_count INT DEFAULT 0 COMMENT '课程数量',
    student_count INT DEFAULT 0 COMMENT '学员数量',
    revenue DECIMAL(10,2) DEFAULT 0.00 COMMENT '收入',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_coach_id (coach_id),
    INDEX idx_year_month (year, month),
    UNIQUE KEY uk_coach_year_month (coach_id, year, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练课程趋势表';

-- 教练月度统计表
CREATE TABLE IF NOT EXISTS coach_monthly_stats (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    coach_id INT NOT NULL COMMENT '教练ID',
    year INT NOT NULL COMMENT '年份',
    month INT NOT NULL COMMENT '月份',
    total_courses INT DEFAULT 0 COMMENT '课程总数',
    total_students INT DEFAULT 0 COMMENT '学员总数',
    attendance_rate DECIMAL(4,1) DEFAULT 0.0 COMMENT '学员平均出勤率(%)',
    satisfaction_score DECIMAL(3,2) DEFAULT 0.00 COMMENT '课程满意度评分(1-5分)',
    total_hours DECIMAL(10,2) DEFAULT 0.00 COMMENT '总训练时长(小时)',
    total_calories INT DEFAULT 0 COMMENT '总卡路里消耗',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_coach_id (coach_id),
    INDEX idx_year_month (year, month),
    UNIQUE KEY uk_coach_year_month (coach_id, year, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练月度统计表';

-- 教练健康体能表
CREATE TABLE IF NOT EXISTS coach_health_fitness (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    coach_id INT NOT NULL COMMENT '教练ID',
    record_date DATE NOT NULL COMMENT '记录日期',
    weight DECIMAL(5,2) DEFAULT 0.00 COMMENT '体重(kg)',
    body_fat_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '体脂率(%)',
    heart_rate INT DEFAULT 0 COMMENT '静息心率(bpm)',
    training_hours DECIMAL(10,2) DEFAULT 0.00 COMMENT '训练时长(小时)',
    calories_burned INT DEFAULT 0 COMMENT '卡路里消耗(kcal)',
    muscle_mass DECIMAL(5,2) DEFAULT 0.00 COMMENT '肌肉量(kg)',
    bmi DECIMAL(5,2) DEFAULT 0.00 COMMENT 'BMI指数',
    strength_score INT DEFAULT 0 COMMENT '力量评分(0-100)',
    endurance_score INT DEFAULT 0 COMMENT '耐力评分(0-100)',
    flexibility_score INT DEFAULT 0 COMMENT '柔韧性评分(0-100)',
    speed_score INT DEFAULT 0 COMMENT '速度评分(0-100)',
    coordination_score INT DEFAULT 0 COMMENT '协调性评分(0-100)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_coach_id (coach_id),
    INDEX idx_record_date (record_date),
    UNIQUE KEY uk_coach_date (coach_id, record_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练健康体能表';

-- 教练预约表
CREATE TABLE IF NOT EXISTS coach_appointment (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id INT NOT NULL COMMENT '用户ID',
    coach_id INT NOT NULL COMMENT '教练ID',
    appointment_time DATETIME NOT NULL COMMENT '预约时间',
    duration INT DEFAULT 60 COMMENT '预约时长(分钟)',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    status INT DEFAULT 0 COMMENT '状态：0-待处理，1-已确认，2-已拒绝，3-已完成，4-已取消',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_coach_id (coach_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_appointment_time (appointment_time),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (coach_id) REFERENCES coach(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练预约表';

-- 健身内容表
CREATE TABLE IF NOT EXISTS fitness_content (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    summary VARCHAR(500) DEFAULT '' COMMENT '摘要',
    cover_image VARCHAR(255) DEFAULT '' COMMENT '封面图片',
    author_id INT NOT NULL COMMENT '作者ID',
    author_name VARCHAR(50) NOT NULL COMMENT '作者姓名',
    status INT DEFAULT 0 COMMENT '状态：0-草稿，1-已发布，2-已下架',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    collect_count INT DEFAULT 0 COMMENT '收藏数',
    share_count INT DEFAULT 0 COMMENT '分享数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    publish_time DATETIME COMMENT '发布时间',
    INDEX idx_author_id (author_id),
    INDEX idx_status (status),
    INDEX idx_view_count (view_count),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健身内容表';

-- 内容标签表
CREATE TABLE IF NOT EXISTS content_tag (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '标签名称',
    description VARCHAR(200) DEFAULT '' COMMENT '标签描述',
    use_count INT DEFAULT 0 COMMENT '使用次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_name (name),
    INDEX idx_use_count (use_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容标签表';

-- 内容标签关联表
CREATE TABLE IF NOT EXISTS content_tag_relation (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    content_id INT NOT NULL COMMENT '内容ID',
    tag_id INT NOT NULL COMMENT '标签ID',
    INDEX idx_content_id (content_id),
    INDEX idx_tag_id (tag_id),
    UNIQUE KEY uk_content_tag (content_id, tag_id),
    FOREIGN KEY (content_id) REFERENCES fitness_content(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES content_tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容标签关联表';
