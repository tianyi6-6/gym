package com.gym.mapper;

import com.gym.entity.CoachMonthlyStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CoachMonthlyStatsMapper {
    List<CoachMonthlyStats> findByCoachId(@Param("coachId") Integer coachId);
    List<CoachMonthlyStats> findByCoachIdAndMonths(@Param("coachId") Integer coachId, @Param("months") int months);
    CoachMonthlyStats findByCoachIdAndYearMonth(@Param("coachId") Integer coachId, @Param("year") Integer year, @Param("month") Integer month);
    int insert(CoachMonthlyStats stats);
    int update(CoachMonthlyStats stats);
    int delete(@Param("id") Integer id);
}