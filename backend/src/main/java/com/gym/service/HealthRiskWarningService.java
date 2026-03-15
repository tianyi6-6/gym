package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.HealthRiskWarning;
import com.gym.mapper.HealthRiskWarningMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HealthRiskWarningService {
    @Autowired
    private HealthRiskWarningMapper healthRiskWarningMapper;

    public Result<HealthRiskWarning> findById(Integer id) {
        HealthRiskWarning warning = healthRiskWarningMapper.findById(id);
        if (warning == null) {
            return Result.error("风险预警不存在");
        }
        return Result.success(warning);
    }

    public Result<List<HealthRiskWarning>> findByUserId(Integer userId) {
        return Result.success(healthRiskWarningMapper.findByUserId(userId));
    }

    public Result<List<HealthRiskWarning>> findByUserIdAndStatus(Integer userId, Integer status) {
        return Result.success(healthRiskWarningMapper.findByUserIdAndStatus(userId, status));
    }

    public Result<List<HealthRiskWarning>> findByStatus(Integer status) {
        return Result.success(healthRiskWarningMapper.findByStatus(status));
    }

    public Result<List<HealthRiskWarning>> findByDateRange(Date startDate, Date endDate) {
        return Result.success(healthRiskWarningMapper.findByDateRange(startDate, endDate));
    }

    public Result<List<HealthRiskWarning>> findAll() {
        return Result.success(healthRiskWarningMapper.findAll());
    }

    public Result<Void> save(HealthRiskWarning healthRiskWarning) {
        if (healthRiskWarning.getId() == null) {
            if (healthRiskWarning.getIsHandled() == null) {
                healthRiskWarning.setIsHandled(0);
            }
            healthRiskWarningMapper.insert(healthRiskWarning);
        } else {
            healthRiskWarningMapper.update(healthRiskWarning);
        }
        return Result.success(null);
    }

    public Result<Void> updateStatus(Integer id, Integer status) {
        HealthRiskWarning warning = healthRiskWarningMapper.findById(id);
        if (warning == null) {
            return Result.error("风险预警不存在");
        }
        healthRiskWarningMapper.updateStatus(id, status);
        return Result.success(null);
    }

    public Result<Void> updateHandleStatus(Integer id, Integer handleStatus) {
        HealthRiskWarning warning = healthRiskWarningMapper.findById(id);
        if (warning == null) {
            return Result.error("风险预警不存在");
        }
        healthRiskWarningMapper.updateHandleStatus(id, handleStatus);
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        healthRiskWarningMapper.delete(id);
        return Result.success(null);
    }

    public Result<Void> deleteByUserId(Integer userId) {
        healthRiskWarningMapper.deleteByUserId(userId);
        return Result.success(null);
    }
}
