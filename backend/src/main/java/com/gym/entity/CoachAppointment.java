package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CoachAppointment {
    private Integer id;
    private Integer userId;
    private Integer coachId;
    private Date appointmentTime;
    private Integer duration;
    private String remark;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    // 关联字段
    private String userName;
    private String coachName;
}

