package com.gym.mapper;

import com.gym.entity.Recharge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RechargeMapper {
    List<Recharge> findAll();
    List<Recharge> findByUserId(@Param("userId") Integer userId);
    int insert(Recharge recharge);
}

