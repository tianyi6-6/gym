package com.gym.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CoachMyCourses {
    private Integer id;
    private Integer coachId;
    private Integer courseId;
    private String courseName;
    private String courseType;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private String difficultyLevel;
    private Integer studentCount;
    private Integer totalSessions;
    private BigDecimal rating;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    private String coachName;
}
