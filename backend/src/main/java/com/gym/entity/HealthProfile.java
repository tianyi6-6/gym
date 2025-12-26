package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class HealthProfile {
    private Integer id;
    private Integer userId;
    private Integer age;
    private Integer gender;
    private Double height;
    private Double weight;
    private String occupation;
    private String medicalHistory;
    private String allergyHistory;
    private String surgeryHistory;
    private String exerciseHistory;
    private String exerciseHabits;
    private String exercisePreference;
    private String sleepHabits;
    private String dietHabits;
    private Integer smokingStatus;
    private Integer drinkingStatus;
    private Integer riskLevel;
    private String riskFactors;
    private Date createTime;
    private Date updateTime;
}
