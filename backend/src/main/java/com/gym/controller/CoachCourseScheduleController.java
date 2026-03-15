package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.CoachCourseSchedule;
import com.gym.service.CoachCourseScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach-course-schedule")
@CrossOrigin
public class CoachCourseScheduleController {
    @Autowired
    private CoachCourseScheduleService coachCourseScheduleService;

    @GetMapping("/list")
    public Result<List<CoachCourseSchedule>> list() {
        return coachCourseScheduleService.findAll();
    }

    @GetMapping("/coach/{coachId}")
    public Result<List<CoachCourseSchedule>> getByCoachId(@PathVariable Integer coachId) {
        return coachCourseScheduleService.findByCoachId(coachId);
    }

    @GetMapping("/{id}")
    public Result<CoachCourseSchedule> getById(@PathVariable Integer id) {
        return coachCourseScheduleService.findById(id);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody CoachCourseSchedule coachCourseSchedule) {
        return coachCourseScheduleService.save(coachCourseSchedule);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return coachCourseScheduleService.delete(id);
    }

    @DeleteMapping("/coach/{coachId}")
    public Result<Void> deleteByCoachId(@PathVariable Integer coachId) {
        return coachCourseScheduleService.deleteByCoachId(coachId);
    }
}
