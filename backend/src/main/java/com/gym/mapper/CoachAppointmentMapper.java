package com.gym.mapper;

import com.gym.entity.CoachAppointment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface CoachAppointmentMapper {
    void insert(CoachAppointment appointment);
    void update(CoachAppointment appointment);
    void delete(Integer id);
    CoachAppointment selectById(Integer id);
    List<CoachAppointment> selectByCoachId(Integer coachId);
    List<CoachAppointment> selectByUserId(Integer userId);
    List<CoachAppointment> selectByStatus(Integer status);
    List<CoachAppointment> selectByCoachIdAndStatus(Integer coachId, Integer status);
    List<CoachAppointment> selectByConditions(Map<String, Object> conditions);
    List<CoachAppointment> selectByDateRange(Map<String, Object> params);
}
