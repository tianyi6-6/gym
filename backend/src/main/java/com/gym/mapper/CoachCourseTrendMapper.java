package com.gym.mapper;

import com.gym.entity.CoachCourseTrend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CoachCourseTrendMapper {
    List<CoachCourseTrend> findByCoachId(@Param("coachId") Integer coachId);
    List<CoachCourseTrend> findByCoachIdAndMonths(@Param("coachId") Integer coachId, @Param("months") int months);
    CoachCourseTrend findByCoachIdAndYearMonth(@Param("coachId") Integer coachId, @Param("year") Integer year, @Param("month") Integer month);
    int insert(CoachCourseTrend trend);
    int update(CoachCourseTrend trend);
    int delete(@Param("id") Integer id);
}