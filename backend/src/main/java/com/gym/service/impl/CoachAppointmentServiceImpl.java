package com.gym.service.impl;

import com.gym.entity.CoachAppointment;
import com.gym.mapper.CoachAppointmentMapper;
import com.gym.service.CoachAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class CoachAppointmentServiceImpl implements CoachAppointmentService {

    @Autowired
    private CoachAppointmentMapper appointmentMapper;

    @Override
    public void createAppointment(CoachAppointment appointment) {
        appointmentMapper.insert(appointment);
    }

    @Override
    public void updateAppointment(CoachAppointment appointment) {
        appointmentMapper.update(appointment);
    }

    @Override
    public void deleteAppointment(Integer id) {
        appointmentMapper.delete(id);
    }

    @Override
    public CoachAppointment getAppointmentById(Integer id) {
        return appointmentMapper.selectById(id);
    }

    @Override
    public List<CoachAppointment> getAppointmentsByCoachId(Integer coachId) {
        return appointmentMapper.selectByCoachId(coachId);
    }

    @Override
    public List<CoachAppointment> getAppointmentsByUserId(Integer userId) {
        return appointmentMapper.selectByUserId(userId);
    }

    @Override
    public List<CoachAppointment> getAppointmentsByStatus(Integer status) {
        return appointmentMapper.selectByStatus(status);
    }

    @Override
    public List<CoachAppointment> getAppointmentsByCoachIdAndStatus(Integer coachId, Integer status) {
        return appointmentMapper.selectByCoachIdAndStatus(coachId, status);
    }

    @Override
    public List<CoachAppointment> getAppointmentsByConditions(Map<String, Object> conditions) {
        return appointmentMapper.selectByConditions(conditions);
    }

    @Override
    public List<CoachAppointment> getAppointmentsByDateRange(Integer coachId, String startDate, String endDate) {
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("coachId", coachId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return appointmentMapper.selectByDateRange(params);
    }

    @Override
    public void batchUpdateStatus(List<Integer> ids, Integer status) {
        for (Integer id : ids) {
            CoachAppointment appointment = getAppointmentById(id);
            if (appointment != null) {
                appointment.setStatus(status);
                updateAppointment(appointment);
            }
        }
    }

    @Override
    public boolean checkTimeConflict(Integer coachId, String appointmentTime, Integer duration, Integer excludeId) {
        // 这里需要实现时间冲突检测逻辑
        // 可以通过查询指定时间范围内的预约来判断是否冲突
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("coachId", coachId);
        params.put("startDate", appointmentTime);
        params.put("endDate", appointmentTime); // 这里需要根据duration计算结束时间
        List<CoachAppointment> appointments = appointmentMapper.selectByDateRange(params);
        for (CoachAppointment appointment : appointments) {
            if (excludeId != null && appointment.getId().equals(excludeId)) {
                continue;
            }
            // 简单的时间冲突检测逻辑
            // 实际应用中需要更精确的时间计算
            return true;
        }
        return false;
    }
}
