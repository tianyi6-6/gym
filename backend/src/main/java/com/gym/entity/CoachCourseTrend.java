package com.gym.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CoachCourseTrend {
    private Integer id;
    private Integer coachId;
    private Integer year;
    private Integer month;
    private Integer courseCount;
    private Integer studentCount;
    private BigDecimal revenue;
    private Date createTime;
    private Date updateTime;
}
