package com.gym.controller;

import com.gym.common.Result;
import com.gym.service.CoachCourseTrendService;
import com.gym.service.CoachMonthlyStatsService;
import com.gym.service.CoachHealthFitnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/coach-dashboard")
@CrossOrigin
public class CoachDashboardController {
    @Autowired
    private CoachCourseTrendService coachCourseTrendService;

    @Autowired
    private CoachMonthlyStatsService coachMonthlyStatsService;

    @Autowired
    private CoachHealthFitnessService coachHealthFitnessService;

    @GetMapping("/course-trend/{coachId}")
    public Result<Map<String, Object>> getCourseTrend(@PathVariable Integer coachId) {
        return coachCourseTrendService.getCourseTrendData(coachId);
    }

    @GetMapping("/monthly-stats/{coachId}")
    public Result<Map<String, Object>> getMonthlyStats(@PathVariable Integer coachId) {
        return coachMonthlyStatsService.getMonthlyStatsData(coachId);
    }

    @GetMapping("/monthly-trend/{coachId}")
    public Result<Map<String, Object>> getMonthlyTrend(@PathVariable Integer coachId) {
        return coachMonthlyStatsService.getMonthlyTrendData(coachId);
    }

    @GetMapping("/health-fitness/{coachId}")
    public Result<Map<String, Object>> getHealthFitness(@PathVariable Integer coachId) {
        return coachHealthFitnessService.getHealthFitnessData(coachId);
    }

    @GetMapping("/health-fitness/weekly-average/{coachId}")
    public Result<Map<String, Object>> getWeeklyAverage(@PathVariable Integer coachId) {
        return coachHealthFitnessService.getWeeklyAverageData(coachId);
    }

    @GetMapping("/overview/{coachId}")
    public Result<Map<String, Object>> getOverview(@PathVariable Integer coachId) {
        Map<String, Object> overview = new java.util.HashMap<>();

        Result<Map<String, Object>> courseTrendResult = coachCourseTrendService.getCourseTrendData(coachId);
        Result<Map<String, Object>> monthlyStatsResult = coachMonthlyStatsService.getMonthlyStatsData(coachId);
        Result<Map<String, Object>> healthFitnessResult = coachHealthFitnessService.getHealthFitnessData(coachId);
        Result<Map<String, Object>> weeklyAverageResult = coachHealthFitnessService.getWeeklyAverageData(coachId);

        if (courseTrendResult.getCode() == 200) {
            overview.put("courseTrend", courseTrendResult.getData());
        }
        if (monthlyStatsResult.getCode() == 200) {
            overview.put("monthlyStats", monthlyStatsResult.getData());
        }
        if (healthFitnessResult.getCode() == 200) {
            overview.put("healthFitness", healthFitnessResult.getData());
        }
        if (weeklyAverageResult.getCode() == 200) {
            overview.put("weeklyAverage", weeklyAverageResult.getData());
        }

        return Result.success(overview);
    }
}