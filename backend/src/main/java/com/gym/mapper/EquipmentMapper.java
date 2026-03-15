package com.gym.mapper;

import com.gym.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EquipmentMapper {
    List<Equipment> findAll();
    Equipment findById(@Param("id") Integer id);
    int insert(Equipment equipment);
    int update(Equipment equipment);
    int delete(@Param("id") Integer id);
}

