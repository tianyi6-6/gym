package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.HealthRiskWarning;
import com.gym.service.HealthRiskWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/health-risk-warning")
@CrossOrigin
public class HealthRiskWarningController {
    @Autowired
    private HealthRiskWarningService healthRiskWarningService;

    @GetMapping("/{id}")
    public Result<HealthRiskWarning> getById(@PathVariable Integer id) {
        return healthRiskWarningService.findById(id);
    }

    @GetMapping("/user/{userId}")
    public Result<List<HealthRiskWarning>> getByUserId(@PathVariable Integer userId) {
        return healthRiskWarningService.findByUserId(userId);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public Result<List<HealthRiskWarning>> getByUserIdAndStatus(
            @PathVariable Integer userId,
            @PathVariable Integer status) {
        return healthRiskWarningService.findByUserIdAndStatus(userId, status);
    }

    @GetMapping("/status/{status}")
    public Result<List<HealthRiskWarning>> getByStatus(@PathVariable Integer status) {
        return healthRiskWarningService.findByStatus(status);
    }

    @GetMapping("/date-range")
    public Result<List<HealthRiskWarning>> getByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return healthRiskWarningService.findByDateRange(startDate, endDate);
    }

    @GetMapping("/list")
    public Result<List<HealthRiskWarning>> list() {
        return healthRiskWarningService.findAll();
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody HealthRiskWarning healthRiskWarning) {
        return healthRiskWarningService.save(healthRiskWarning);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @PathVariable Integer id,
            @RequestParam Integer status) {
        return healthRiskWarningService.updateStatus(id, status);
    }

    @PutMapping("/{id}/handle-status")
    public Result<Void> updateHandleStatus(
            @PathVariable Integer id,
            @RequestParam Integer handleStatus) {
        return healthRiskWarningService.updateHandleStatus(id, handleStatus);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return healthRiskWarningService.delete(id);
    }

    @DeleteMapping("/user/{userId}")
    public Result<Void> deleteByUserId(@PathVariable Integer userId) {
        return healthRiskWarningService.deleteByUserId(userId);
    }
}
