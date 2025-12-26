package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.HealthGoal;
import com.gym.mapper.HealthGoalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class HealthGoalService {
    @Autowired
    private HealthGoalMapper healthGoalMapper;

    public Result<HealthGoal> findById(Integer id) {
        HealthGoal goal = healthGoalMapper.findById(id);
        if (goal == null) {
            return Result.error("健康目标不存在");
        }
        return Result.success(goal);
    }

    public Result<List<HealthGoal>> findByUserId(Integer userId) {
        return Result.success(healthGoalMapper.findByUserId(userId));
    }

    public Result<List<HealthGoal>> findByStatus(Integer status) {
        return Result.success(healthGoalMapper.findByStatus(status));
    }

    public Result<List<HealthGoal>> findByUserIdAndStatus(Integer userId, Integer status) {
        return Result.success(healthGoalMapper.findByUserIdAndStatus(userId, status));
    }

    public Result<List<HealthGoal>> findAll() {
        return Result.success(healthGoalMapper.findAll());
    }

    public Result<Void> save(HealthGoal healthGoal) {
        if (healthGoal.getId() == null) {
            if (healthGoal.getStartDate() == null) {
                healthGoal.setStartDate(new Date());
            }
            if (healthGoal.getStatus() == null) {
                healthGoal.setStatus(1);
            }
            if (healthGoal.getProgress() == null) {
                healthGoal.setProgress(BigDecimal.ZERO);
            }
            healthGoalMapper.insert(healthGoal);
        } else {
            healthGoalMapper.update(healthGoal);
        }
        return Result.success(null);
    }

    public Result<Void> updateProgress(Integer id, Integer progress) {
        HealthGoal goal = healthGoalMapper.findById(id);
        if (goal == null) {
            return Result.error("健康目标不存在");
        }
        healthGoalMapper.updateProgress(id, progress);

        if (progress >= 100) {
            healthGoalMapper.updateStatus(id, 3);
        }

        return Result.success(null);
    }

    public Result<Void> updateStatus(Integer id, Integer status) {
        HealthGoal goal = healthGoalMapper.findById(id);
        if (goal == null) {
            return Result.error("健康目标不存在");
        }
        healthGoalMapper.updateStatus(id, status);
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        healthGoalMapper.delete(id);
        return Result.success(null);
    }

    public Result<Void> deleteByUserId(Integer userId) {
        healthGoalMapper.deleteByUserId(userId);
        return Result.success(null);
    }
}
