package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.CoachSchedule;
import com.gym.service.CoachScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/coach/schedule")
@CrossOrigin
public class CoachScheduleController {
    @Autowired
    private CoachScheduleService scheduleService;

    @GetMapping("/{id}")
    public Result<CoachSchedule> getById(@PathVariable Integer id) {
        return scheduleService.getById(id);
    }

    @GetMapping("/coach/{coachId}")
    public Result<List<CoachSchedule>> getByCoachId(@PathVariable Integer coachId) {
        return scheduleService.getByCoachId(coachId);
    }

    @GetMapping("/date-range")
    public Result<List<CoachSchedule>> getByDateRange(
            @RequestParam Integer coachId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // 解析日期字符串
            Date start = new Date(Long.parseLong(startDate));
            Date end = new Date(Long.parseLong(endDate));
            return scheduleService.getByDateRange(coachId, start, end);
        } catch (Exception e) {
            return Result.error("日期格式错误");
        }
    }

    @PostMapping("/create")
    public Result<Void> create(@RequestBody CoachSchedule schedule) {
        return scheduleService.create(schedule);
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody CoachSchedule schedule) {
        return scheduleService.update(schedule);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return scheduleService.delete(id);
    }

    @PostMapping("/batch-create")
    public Result<Void> batchCreate(@RequestBody List<CoachSchedule> schedules) {
        return scheduleService.batchCreate(schedules);
    }
}
