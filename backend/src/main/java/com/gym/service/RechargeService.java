package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Recharge;
import com.gym.entity.User;
import com.gym.mapper.RechargeMapper;
import com.gym.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RechargeService {
    @Autowired
    private RechargeMapper rechargeMapper;
    @Autowired
    private UserMapper userMapper;

    public Result<List<Recharge>> findByUserId(Integer userId) {
        return Result.success(rechargeMapper.findByUserId(userId));
    }

    public Result<Void> recharge(Recharge recharge) {
        rechargeMapper.insert(recharge);
        if (recharge.getType() == 1) {
            // 充值
            userMapper.updateBalance(recharge.getUserId(), recharge.getAmount());
        } else {
            // 消费
            userMapper.updateBalance(recharge.getUserId(), recharge.getAmount().negate());
        }
        return Result.success(null);
    }
}

