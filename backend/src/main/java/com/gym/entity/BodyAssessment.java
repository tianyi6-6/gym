package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class BodyAssessment {
    private Integer id;
    private Integer userId;
    private Date assessmentDate;
    private Double bodyFatRate;
    private Double muscleMass;
    private Double boneDensity;
    private Integer basalMetabolicRate;
    private Double bmi;
    private Integer visceralFatLevel;
    private Double bodyWaterRate;
    private Double proteinRate;
    private Double skeletalMuscleMass;
    private Double weight;
    private Double height;
    private Double chestCircumference;
    private Double waistCircumference;
    private Double hipCircumference;
    private String assessmentResult;
    private Integer coachId;
    private Date createTime;
    private Date updateTime;
}
