package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Experience;
import com.gym.mapper.ExperienceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceService {
    @Autowired
    private ExperienceMapper experienceMapper;

    public Result<List<Experience>> findAll() {
        return Result.success(experienceMapper.findAll());
    }

    public Result<Experience> findById(Integer id) {
        experienceMapper.increaseViewCount(id);
        return Result.success(experienceMapper.findById(id));
    }

    public Result<List<Experience>> findByUserId(Integer userId) {
        return Result.success(experienceMapper.findByUserId(userId));
    }

    public Result<List<Experience>> findByCoachId(Integer coachId) {
        return Result.success(experienceMapper.findByCoachId(coachId));
    }

    public Result<Void> save(Experience experience) {
        if (experience.getId() == null) {
            experienceMapper.insert(experience);
        } else {
            experienceMapper.update(experience);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        experienceMapper.delete(id);
        return Result.success(null);
    }
}

