package com.gym.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Course {
    private Integer id;
    private String name;
    private Integer coachId;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private String image;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    // 关联字段
    private String coachName;
}

