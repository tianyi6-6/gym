package com.gym.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CoachHealthFitness {
    private Integer id;
    private Integer coachId;
    private Date recordDate;
    private BigDecimal weight;
    private BigDecimal bodyFatRate;
    private Integer heartRate;
    private BigDecimal trainingHours;
    private Integer caloriesBurned;
    private BigDecimal muscleMass;
    private BigDecimal bmi;
    private Integer strengthScore;
    private Integer enduranceScore;
    private Integer flexibilityScore;
    private Integer speedScore;
    private Integer coordinationScore;
    private Date createTime;
    private Date updateTime;
}
