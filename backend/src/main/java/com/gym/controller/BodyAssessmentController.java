package com.gym.controller;

import com.gym.common.RequireRole;
import com.gym.common.Result;
import com.gym.entity.BodyAssessment;
import com.gym.service.BodyAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/body-assessment")
@CrossOrigin
public class BodyAssessmentController {
    @Autowired
    private BodyAssessmentService bodyAssessmentService;

    @GetMapping("/{id}")
    @RequireRole("admin")
    public Result<BodyAssessment> getById(@PathVariable Integer id) {
        return bodyAssessmentService.findById(id);
    }

    @GetMapping("/user/{userId}")
    @RequireRole({ "admin", "coach" })
    public Result<List<BodyAssessment>> getByUserId(@PathVariable Integer userId) {
        return bodyAssessmentService.findByUserId(userId);
    }

    @GetMapping("/user/{userId}/latest")
    @RequireRole({ "admin", "coach" })
    public Result<BodyAssessment> getLatestByUserId(@PathVariable Integer userId) {
        return bodyAssessmentService.findLatestByUserId(userId);
    }

    @GetMapping("/user/{userId}/date-range")
    @RequireRole({ "admin", "coach" })
    public Result<List<BodyAssessment>> getByDateRange(
            @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return bodyAssessmentService.findByDateRange(userId, startDate, endDate);
    }

    @GetMapping("/coach/{coachId}")
    @RequireRole({ "admin", "coach" })
    public Result<List<BodyAssessment>> getByCoachId(@PathVariable Integer coachId) {
        return bodyAssessmentService.findByCoachId(coachId);
    }

    @GetMapping("/list")
    @RequireRole("admin")
    public Result<List<BodyAssessment>> list() {
        return bodyAssessmentService.findAll();
    }

    @PostMapping("/save")
    @RequireRole("admin")
    public Result<Void> save(@RequestBody BodyAssessment bodyAssessment) {
        return bodyAssessmentService.save(bodyAssessment);
    }

    @DeleteMapping("/{id}")
    @RequireRole("admin")
    public Result<Void> delete(@PathVariable Integer id) {
        return bodyAssessmentService.delete(id);
    }

    @DeleteMapping("/user/{userId}")
    @RequireRole("admin")
    public Result<Void> deleteByUserId(@PathVariable Integer userId) {
        return bodyAssessmentService.deleteByUserId(userId);
    }
}
