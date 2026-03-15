package com.gym.mapper;

import com.gym.entity.CoachSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface CoachScheduleMapper {
    CoachSchedule findById(@Param("id") Integer id);
    List<CoachSchedule> findByCoachId(@Param("coachId") Integer coachId);
    List<CoachSchedule> findByCoachIdAndDateRange(@Param("coachId") Integer coachId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    List<CoachSchedule> findByCoachIdAndDate(@Param("coachId") Integer coachId, @Param("scheduleDate") Date scheduleDate);
    int insert(CoachSchedule schedule);
    int update(CoachSchedule schedule);
    int delete(@Param("id") Integer id);
    // 检查时间冲突
    int countTimeConflict(@Param("coachId") Integer coachId, @Param("scheduleDate") Date scheduleDate, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("excludeId") Integer excludeId);
}