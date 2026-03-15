package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class HealthRiskWarning {
    private Integer id;
    private Integer userId;
    private String riskType;
    private Integer riskLevel;
    private String riskDescription;
    private Date warningDate;
    private Integer isHandled;
    private Date handleDate;
    private String handleResult;
    private Date createTime;
    private Date updateTime;
}
