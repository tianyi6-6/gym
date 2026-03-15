package com.gym.mapper;

import com.gym.entity.HealthGoal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HealthGoalMapper {
    HealthGoal findById(@Param("id") Integer id);
    List<HealthGoal> findByUserId(@Param("userId") Integer userId);
    List<HealthGoal> findByStatus(@Param("status") Integer status);
    List<HealthGoal> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") Integer status);
    List<HealthGoal> findAll();
    int insert(HealthGoal healthGoal);
    int update(HealthGoal healthGoal);
    int updateProgress(@Param("id") Integer id, @Param("progress") Integer progress);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    int delete(@Param("id") Integer id);
    int deleteByUserId(@Param("userId") Integer userId);
}
