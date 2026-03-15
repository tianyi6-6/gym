package com.gym.service;

import com.gym.entity.CoachAppointment;
import java.util.List;
import java.util.Map;

public interface CoachAppointmentService {
    void createAppointment(CoachAppointment appointment);
    void updateAppointment(CoachAppointment appointment);
    void deleteAppointment(Integer id);
    CoachAppointment getAppointmentById(Integer id);
    List<CoachAppointment> getAppointmentsByCoachId(Integer coachId);
    List<CoachAppointment> getAppointmentsByUserId(Integer userId);
    List<CoachAppointment> getAppointmentsByStatus(Integer status);
    List<CoachAppointment> getAppointmentsByCoachIdAndStatus(Integer coachId, Integer status);
    List<CoachAppointment> getAppointmentsByConditions(Map<String, Object> conditions);
    List<CoachAppointment> getAppointmentsByDateRange(Integer coachId, String startDate, String endDate);
    void batchUpdateStatus(List<Integer> ids, Integer status);
    boolean checkTimeConflict(Integer coachId, String appointmentTime, Integer duration, Integer excludeId);
}
