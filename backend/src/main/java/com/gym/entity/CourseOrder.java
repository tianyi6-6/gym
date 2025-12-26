package com.gym.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CourseOrder {
    private Integer id;
    private String orderNo;
    private Integer userId;
    private Integer courseId;
    private BigDecimal price;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    // 关联字段
    private String userName;
    private String courseName;
}

