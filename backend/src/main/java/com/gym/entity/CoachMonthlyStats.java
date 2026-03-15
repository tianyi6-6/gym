package com.gym.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CoachMonthlyStats {
    private Integer id;
    private Integer coachId;
    private Integer year;
    private Integer month;
    private Integer totalCourses;
    private Integer totalStudents;
    private BigDecimal attendanceRate;
    private BigDecimal satisfactionScore;
    private BigDecimal totalHours;
    private Integer totalCalories;
    private Date createTime;
    private Date updateTime;
}
