package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.CoachMonthlyStats;
import com.gym.mapper.CoachMonthlyStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoachMonthlyStatsService {
    @Autowired
    private CoachMonthlyStatsMapper coachMonthlyStatsMapper;

    public Result<List<CoachMonthlyStats>> findByCoachId(Integer coachId) {
        return Result.success(coachMonthlyStatsMapper.findByCoachId(coachId));
    }

    public Result<List<CoachMonthlyStats>> findByCoachIdAndMonths(Integer coachId, int months) {
        return Result.success(coachMonthlyStatsMapper.findByCoachIdAndMonths(coachId, months));
    }

    public Result<Map<String, Object>> getMonthlyStatsData(Integer coachId) {
        List<CoachMonthlyStats> statsList = coachMonthlyStatsMapper.findByCoachIdAndMonths(coachId, 1);
        
        Map<String, Object> data = new HashMap<>();
        
        if (!statsList.isEmpty()) {
            CoachMonthlyStats stats = statsList.get(0);
            data.put("totalCourses", stats.getTotalCourses());
            data.put("attendanceRate", stats.getAttendanceRate());
            data.put("satisfactionScore", stats.getSatisfactionScore());
            data.put("totalHours", stats.getTotalHours());
            data.put("totalCalories", stats.getTotalCalories());
        } else {
            data.put("totalCourses", 0);
            data.put("attendanceRate", 0.0);
            data.put("satisfactionScore", 0.0);
            data.put("totalHours", 0.0);
            data.put("totalCalories", 0);
        }
        
        return Result.success(data);
    }

    public Result<Map<String, Object>> getMonthlyTrendData(Integer coachId) {
        List<CoachMonthlyStats> statsList = coachMonthlyStatsMapper.findByCoachIdAndMonths(coachId, 6);
        
        Map<String, Object> data = new HashMap<>();
        String[] months = new String[statsList.size()];
        int[] totalCourses = new int[statsList.size()];
        double[] attendanceRates = new double[statsList.size()];
        double[] satisfactionScores = new double[statsList.size()];
        
        for (int i = statsList.size() - 1; i >= 0; i--) {
            CoachMonthlyStats stats = statsList.get(i);
            int index = statsList.size() - 1 - i;
            months[index] = stats.getYear() + "年" + stats.getMonth() + "月";
            totalCourses[index] = stats.getTotalCourses();
            attendanceRates[index] = stats.getAttendanceRate().doubleValue();
            satisfactionScores[index] = stats.getSatisfactionScore().doubleValue();
        }
        
        data.put("months", months);
        data.put("totalCourses", totalCourses);
        data.put("attendanceRates", attendanceRates);
        data.put("satisfactionScores", satisfactionScores);
        
        return Result.success(data);
    }

    public Result<Void> save(CoachMonthlyStats stats) {
        if (stats.getId() == null) {
            coachMonthlyStatsMapper.insert(stats);
        } else {
            coachMonthlyStatsMapper.update(stats);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        coachMonthlyStatsMapper.delete(id);
        return Result.success(null);
    }
}