package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.Experience;
import com.gym.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experience")
@CrossOrigin
public class ExperienceController {
    @Autowired
    private ExperienceService experienceService;

    @GetMapping("/list")
    public Result<List<Experience>> list() {
        return experienceService.findAll();
    }

    @GetMapping("/{id}")
    public Result<Experience> getById(@PathVariable Integer id) {
        return experienceService.findById(id);
    }

    @GetMapping("/user/{userId}")
    public Result<List<Experience>> getByUserId(@PathVariable Integer userId) {
        return experienceService.findByUserId(userId);
    }

    @GetMapping("/coach/{coachId}")
    public Result<List<Experience>> getByCoachId(@PathVariable Integer coachId) {
        return experienceService.findByCoachId(coachId);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Experience experience) {
        return experienceService.save(experience);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return experienceService.delete(id);
    }
}

