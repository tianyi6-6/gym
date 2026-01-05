package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Coach;
import com.gym.mapper.CoachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {
  @Autowired
  private CoachMapper coachMapper;

  public Result<Void> register(Coach coach) {
    if (coach.getUsername() == null || coach.getUsername().isEmpty()) {
      return Result.error("用户名不能为空");
    }
    if (coach.getPassword() == null || coach.getPassword().isEmpty()) {
      return Result.error("密码不能为空");
    }
    if (coach.getName() == null || coach.getName().isEmpty()) {
      return Result.error("姓名不能为空");
    }
    
    Coach existingCoach = coachMapper.findByUsername(coach.getUsername());
    if (existingCoach != null) {
      return Result.error("用户名已存在");
    }
    
    if (coach.getProfessionalScore() == null) {
      coach.setProfessionalScore(80);
    }
    if (coach.getTeachingScore() == null) {
      coach.setTeachingScore(80);
    }
    if (coach.getCommunicationScore() == null) {
      coach.setCommunicationScore(80);
    }
    if (coach.getServiceScore() == null) {
      coach.setServiceScore(80);
    }
    if (coach.getPerformanceScore() == null) {
      coach.setPerformanceScore(80);
    }
    
    coachMapper.insert(coach);
    return Result.success(null);
  }

  public Result<List<Coach>> findAll() {
    return Result.success(coachMapper.findAll());
  }

  public Result<Coach> findById(Integer id) {
    return Result.success(coachMapper.findById(id));
  }

  public Result<Void> save(Coach coach) {
    if (coach.getId() == null) {
      if (coach.getPassword() == null || coach.getPassword().isEmpty()) {
        coach.setPassword("123456");
      }
      coachMapper.insert(coach);
    } else {
      coachMapper.update(coach);
    }
    return Result.success(null);
  }

  public Result<Void> delete(Integer id) {
    coachMapper.delete(id);
    return Result.success(null);
  }
}
