package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CoachCourseSchedule {
    private Integer id;
    private Integer coachId;
    private Integer courseId;
    private String courseName;
    private Date scheduleDate;
    private String startTime;
    private String endTime;
    private String location;
    private Integer maxStudents;
    private Integer currentStudents;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    private String coachName;
}
