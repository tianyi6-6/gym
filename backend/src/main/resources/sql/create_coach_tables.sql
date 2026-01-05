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
