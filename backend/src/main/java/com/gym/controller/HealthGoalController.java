package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.HealthGoal;
import com.gym.service.HealthGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health-goal")
@CrossOrigin
public class HealthGoalController {
    @Autowired
    private HealthGoalService healthGoalService;

    @GetMapping("/{id}")
    public Result<HealthGoal> getById(@PathVariable Integer id) {
        return healthGoalService.findById(id);
    }

    @GetMapping("/user/{userId}")
    public Result<List<HealthGoal>> getByUserId(@PathVariable Integer userId) {
        return healthGoalService.findByUserId(userId);
    }

    @GetMapping("/status/{status}")
    public Result<List<HealthGoal>> getByStatus(@PathVariable Integer status) {
        return healthGoalService.findByStatus(status);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public Result<List<HealthGoal>> getByUserIdAndStatus(
            @PathVariable Integer userId,
            @PathVariable Integer status) {
        return healthGoalService.findByUserIdAndStatus(userId, status);
    }

    @GetMapping("/list")
    public Result<List<HealthGoal>> list() {
        return healthGoalService.findAll();
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody HealthGoal healthGoal) {
        return healthGoalService.save(healthGoal);
    }

    @PutMapping("/{id}/progress")
    public Result<Void> updateProgress(
            @PathVariable Integer id,
            @RequestParam Integer progress) {
        return healthGoalService.updateProgress(id, progress);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @PathVariable Integer id,
            @RequestParam Integer status) {
        return healthGoalService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return healthGoalService.delete(id);
    }

    @DeleteMapping("/user/{userId}")
    public Result<Void> deleteByUserId(@PathVariable Integer userId) {
        return healthGoalService.deleteByUserId(userId);
    }
}
