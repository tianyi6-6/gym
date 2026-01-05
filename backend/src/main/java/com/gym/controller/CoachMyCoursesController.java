package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.CoachMyCourses;
import com.gym.service.CoachMyCoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach-my-courses")
@CrossOrigin
public class CoachMyCoursesController {
    @Autowired
    private CoachMyCoursesService coachMyCoursesService;

    @GetMapping("/list")
    public Result<List<CoachMyCourses>> list() {
        return coachMyCoursesService.findAll();
    }

    @GetMapping("/coach/{coachId}")
    public Result<List<CoachMyCourses>> getByCoachId(@PathVariable Integer coachId) {
        return coachMyCoursesService.findByCoachId(coachId);
    }

    @GetMapping("/{id}")
    public Result<CoachMyCourses> getById(@PathVariable Integer id) {
        return coachMyCoursesService.findById(id);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody CoachMyCourses coachMyCourses) {
        return coachMyCoursesService.save(coachMyCourses);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return coachMyCoursesService.delete(id);
    }

    @DeleteMapping("/coach/{coachId}")
    public Result<Void> deleteByCoachId(@PathVariable Integer coachId) {
        return coachMyCoursesService.deleteByCoachId(coachId);
    }
}
