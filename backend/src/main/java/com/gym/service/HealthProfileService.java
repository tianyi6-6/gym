package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.HealthProfile;
import com.gym.mapper.HealthProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthProfileService {
    @Autowired
    private HealthProfileMapper healthProfileMapper;

    public Result<HealthProfile> findByUserId(Integer userId) {
        HealthProfile profile = healthProfileMapper.findByUserId(userId);
        if (profile == null) {
            return Result.error("健康档案不存在");
        }
        return Result.success(profile);
    }

    public Result<HealthProfile> findById(Integer id) {
        HealthProfile profile = healthProfileMapper.findById(id);
        if (profile == null) {
            return Result.error("健康档案不存在");
        }
        return Result.success(profile);
    }

    public Result<List<HealthProfile>> findAll() {
        return Result.success(healthProfileMapper.findAll());
    }

    public Result<List<HealthProfile>> findByRiskLevel(Integer riskLevel) {
        return Result.success(healthProfileMapper.findByRiskLevel(riskLevel));
    }

    public Result<Void> save(HealthProfile healthProfile) {
        if (healthProfile.getId() == null) {
            HealthProfile existing = healthProfileMapper.findByUserId(healthProfile.getUserId());
            if (existing != null) {
                return Result.error("该用户已存在健康档案");
            }
            calculateRiskLevel(healthProfile);
            healthProfileMapper.insert(healthProfile);
        } else {
            calculateRiskLevel(healthProfile);
            healthProfileMapper.update(healthProfile);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        healthProfileMapper.delete(id);
        return Result.success(null);
    }

    public Result<Void> deleteByUserId(Integer userId) {
        healthProfileMapper.deleteByUserId(userId);
        return Result.success(null);
    }

    private void calculateRiskLevel(HealthProfile profile) {
        int riskScore = 0;
        StringBuilder riskFactors = new StringBuilder();

        if (profile.getAge() != null && profile.getAge() > 60) {
            riskScore += 2;
            riskFactors.append("高龄;");
        }

        if (profile.getSmokingStatus() != null && profile.getSmokingStatus() == 2) {
            riskScore += 2;
            riskFactors.append("经常吸烟;");
        }

        if (profile.getDrinkingStatus() != null && profile.getDrinkingStatus() == 2) {
            riskScore += 2;
            riskFactors.append("经常饮酒;");
        }

        if (profile.getMedicalHistory() != null && !profile.getMedicalHistory().isEmpty()) {
            riskScore += 3;
            riskFactors.append("有既往病史;");
        }

        if (riskScore >= 5) {
            profile.setRiskLevel(3);
        } else if (riskScore >= 2) {
            profile.setRiskLevel(2);
        } else {
            profile.setRiskLevel(1);
        }

        profile.setRiskFactors(riskFactors.toString());
    }
}
