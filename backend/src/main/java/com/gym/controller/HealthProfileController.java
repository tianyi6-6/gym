package com.gym.controller;

import com.gym.common.RequireRole;
import com.gym.common.Result;
import com.gym.entity.HealthProfile;
import com.gym.service.HealthProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health-profile")
@CrossOrigin
public class HealthProfileController {
    @Autowired
    private HealthProfileService healthProfileService;

    @GetMapping("/user/{userId}")
    @RequireRole("admin")
    public Result<HealthProfile> getByUserId(@PathVariable Integer userId) {
        return healthProfileService.findByUserId(userId);
    }

    @GetMapping("/{id}")
    @RequireRole("admin")
    public Result<HealthProfile> getById(@PathVariable Integer id) {
        return healthProfileService.findById(id);
    }

    @GetMapping("/list")
    @RequireRole("admin")
    public Result<List<HealthProfile>> list() {
        return healthProfileService.findAll();
    }

    @GetMapping("/risk/{riskLevel}")
    @RequireRole("admin")
    public Result<List<HealthProfile>> listByRiskLevel(@PathVariable Integer riskLevel) {
        return healthProfileService.findByRiskLevel(riskLevel);
    }

    @PostMapping("/save")
    @RequireRole("admin")
    public Result<Void> save(@RequestBody HealthProfile healthProfile) {
        return healthProfileService.save(healthProfile);
    }

    @DeleteMapping("/{id}")
    @RequireRole("admin")
    public Result<Void> delete(@PathVariable Integer id) {
        return healthProfileService.delete(id);
    }

    @DeleteMapping("/user/{userId}")
    @RequireRole("admin")
    public Result<Void> deleteByUserId(@PathVariable Integer userId) {
        return healthProfileService.deleteByUserId(userId);
    }
}
