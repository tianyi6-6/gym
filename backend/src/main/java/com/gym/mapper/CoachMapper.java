package com.gym.mapper;

import com.gym.entity.Coach;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CoachMapper {
    Coach findByUsername(@Param("username") String username);
    Coach findById(@Param("id") Integer id);
    List<Coach> findAll();
    int insert(Coach coach);
    int update(Coach coach);
    int delete(@Param("id") Integer id);
}

