package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.Course;
import com.gym.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public Result<List<Course>> list() {
        return courseService.findAll();
    }

    @GetMapping("/page")
    public Result<List<Course>> page(@RequestParam int page, @RequestParam int size) {
        return courseService.findByPage(page, size);
    }

    @GetMapping("/count")
    public Result<Long> count() {
        return courseService.count();
    }

    @GetMapping("/{id}")
    public Result<Course> getById(@PathVariable Integer id) {
        return courseService.findById(id);
    }

    @GetMapping("/coach/{coachId}")
    public Result<List<Course>> getByCoachId(@PathVariable Integer coachId) {
        return courseService.findByCoachId(coachId);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Course course) {
        return courseService.save(course);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return courseService.delete(id);
    }
}

