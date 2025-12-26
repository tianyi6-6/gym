package com.gym.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class HealthGoal {
    private Integer id;
    private Integer userId;
    private String goalType;
    private BigDecimal currentValue;
    private BigDecimal targetValue;
    private String unit;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private BigDecimal progress;
    private Date achievementDate;
    private Date createTime;
    private Date updateTime;
}
