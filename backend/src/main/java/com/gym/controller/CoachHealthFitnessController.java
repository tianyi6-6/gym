package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.CoachHealthFitness;
import com.gym.service.CoachHealthFitnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coach-health-fitness")
@CrossOrigin
public class CoachHealthFitnessController {
    @Autowired
    private CoachHealthFitnessService coachHealthFitnessService;

    @GetMapping("/coach/{coachId}")
    public Result<List<CoachHealthFitness>> getByCoachId(@PathVariable Integer coachId) {
        return coachHealthFitnessService.findByCoachId(coachId);
    }

    @GetMapping("/coach/{coachId}/latest")
    public Result<CoachHealthFitness> getLatestByCoachId(@PathVariable Integer coachId) {
        return coachHealthFitnessService.findLatestByCoachId(coachId);
    }

    @GetMapping("/coach/{coachId}/data")
    public Result<Map<String, Object>> getHealthFitnessData(@PathVariable Integer coachId) {
        return coachHealthFitnessService.getHealthFitnessData(coachId);
    }

    @PostMapping("/save")
    public Result<CoachHealthFitness> save(@RequestBody CoachHealthFitness coachHealthFitness) {
        return coachHealthFitnessService.save(coachHealthFitness);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return coachHealthFitnessService.delete(id);
    }
}
