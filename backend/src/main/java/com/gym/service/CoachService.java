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
