package com.gym.mapper;

import com.gym.entity.HealthProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HealthProfileMapper {
    HealthProfile findByUserId(@Param("userId") Integer userId);
    HealthProfile findById(@Param("id") Integer id);
    List<HealthProfile> findAll();
    List<HealthProfile> findByRiskLevel(@Param("riskLevel") Integer riskLevel);
    int insert(HealthProfile healthProfile);
    int update(HealthProfile healthProfile);
    int delete(@Param("id") Integer id);
    int deleteByUserId(@Param("userId") Integer userId);
}
