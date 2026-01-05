package com.gym.mapper;

import com.gym.entity.CoachCourseSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CoachCourseScheduleMapper {
    List<CoachCourseSchedule> findAll();
    List<CoachCourseSchedule> findByCoachId(@Param("coachId") Integer coachId);
    CoachCourseSchedule findById(@Param("id") Integer id);
    int insert(CoachCourseSchedule coachCourseSchedule);
    int update(CoachCourseSchedule coachCourseSchedule);
    int delete(@Param("id") Integer id);
    int deleteByCoachId(@Param("coachId") Integer coachId);
}
