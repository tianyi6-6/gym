package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Recharge;
import com.gym.mapper.RechargeMapper;
import com.gym.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RechargeService {
    private static final Logger logger = LoggerFactory.getLogger(RechargeService.class);

    @Autowired
    private RechargeMapper rechargeMapper;
    @Autowired
    private UserMapper userMapper;

    public Result<List<Recharge>> findByUserId(Integer userId) {
        return Result.success(rechargeMapper.findByUserId(userId));
    }

    @Transactional
    public Result<Void> recharge(Recharge recharge) {
        logger.info("开始处理充值/消费操作，用户ID: {}, 金额: {}, 类型: {}",
                recharge.getUserId(), recharge.getAmount(), recharge.getType() == 1 ? "充值" : "消费");

        try {
            rechargeMapper.insert(recharge);
            logger.debug("充值记录插入成功，记录ID: {}", recharge.getId());

            if (recharge.getType() == 1) {
                // 充值
                userMapper.updateBalance(recharge.getUserId(), recharge.getAmount());
                logger.info("用户ID: {} 充值成功，金额: {}", recharge.getUserId(), recharge.getAmount());
            } else {
                // 消费
                userMapper.updateBalance(recharge.getUserId(), recharge.getAmount().negate());
                logger.info("用户ID: {} 消费成功，金额: {}", recharge.getUserId(), recharge.getAmount());
            }

            logger.info("充值/消费操作完成，用户ID: {}, 金额: {}, 类型: {}",
                    recharge.getUserId(), recharge.getAmount(), recharge.getType() == 1 ? "充值" : "消费");
            return Result.success(null);
        } catch (Exception e) {
            logger.error("充值/消费操作失败，用户ID: {}, 金额: {}, 类型: {}",
                    recharge.getUserId(), recharge.getAmount(), recharge.getType() == 1 ? "充值" : "消费", e);
            throw e;
        }
    }
}
