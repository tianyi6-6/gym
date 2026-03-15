package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.Equipment;
import com.gym.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@CrossOrigin
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/list")
    public Result<List<Equipment>> list() {
        return equipmentService.findAll();
    }

    @GetMapping("/{id}")
    public Result<Equipment> getById(@PathVariable Integer id) {
        return equipmentService.findById(id);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Equipment equipment) {
        return equipmentService.save(equipment);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return equipmentService.delete(id);
    }
}

