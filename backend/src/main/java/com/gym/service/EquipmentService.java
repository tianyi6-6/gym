package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Equipment;
import com.gym.mapper.EquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {
    @Autowired
    private EquipmentMapper equipmentMapper;

    public Result<List<Equipment>> findAll() {
        return Result.success(equipmentMapper.findAll());
    }

    public Result<Equipment> findById(Integer id) {
        return Result.success(equipmentMapper.findById(id));
    }

    public Result<Void> save(Equipment equipment) {
        if (equipment.getId() == null) {
            equipmentMapper.insert(equipment);
        } else {
            equipmentMapper.update(equipment);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        equipmentMapper.delete(id);
        return Result.success(null);
    }
}

