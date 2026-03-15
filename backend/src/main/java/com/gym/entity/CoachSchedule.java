package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CoachSchedule {
    private Integer id;
    private Integer coachId;
    private Date scheduleDate;
    private String startTime;
    private String endTime;
    private Integer status; // 0: 不可预约, 1: 可预约, 2: 已预约, 3: 已完成, 4: 已取消
    private String type; // 课程, 咨询, 休息等
    private String description;
    private Date createTime;
    private Date updateTime;
    
    // 关联字段
    private String coachName;
}