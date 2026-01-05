package com.gym.mapper;

import com.gym.entity.CoachMyCourses;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CoachMyCoursesMapper {
    List<CoachMyCourses> findAll();
    List<CoachMyCourses> findByCoachId(@Param("coachId") Integer coachId);
    CoachMyCourses findById(@Param("id") Integer id);
    int insert(CoachMyCourses coachMyCourses);
    int update(CoachMyCourses coachMyCourses);
    int delete(@Param("id") Integer id);
    int deleteByCoachId(@Param("coachId") Integer coachId);
}
