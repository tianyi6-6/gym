package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.Coach;
import com.gym.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach")
@CrossOrigin
public class CoachController {
    @Autowired
    private CoachService coachService;

    @GetMapping("/list")
    public Result<List<Coach>> list() {
        return coachService.findAll();
    }

    @GetMapping("/{id}")
    public Result<Coach> getById(@PathVariable Integer id) {
        return coachService.findById(id);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Coach coach) {
        return coachService.save(coach);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return coachService.delete(id);
    }
}

