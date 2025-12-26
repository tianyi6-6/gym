package com.gym.mapper;

import com.gym.entity.Experience;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExperienceMapper {
    List<Experience> findAll();
    Experience findById(@Param("id") Integer id);
    List<Experience> findByUserId(@Param("userId") Integer userId);
    List<Experience> findByCoachId(@Param("coachId") Integer coachId);
    int insert(Experience experience);
    int update(Experience experience);
    int delete(@Param("id") Integer id);
    int increaseViewCount(@Param("id") Integer id);
}

