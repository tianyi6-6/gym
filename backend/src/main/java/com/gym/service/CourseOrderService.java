package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.CourseOrder;
import com.gym.entity.User;
import com.gym.mapper.CourseOrderMapper;
import com.gym.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class CourseOrderService {
    private static final Logger logger = LoggerFactory.getLogger(CourseOrderService.class);

    @Autowired
    private CourseOrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    public Result<List<CourseOrder>> findAll() {
        return Result.success(orderMapper.findAll());
    }

    public Result<List<CourseOrder>> findByUserId(Integer userId) {
        return Result.success(orderMapper.findByUserId(userId));
    }

    public Result<Void> createOrder(CourseOrder order) {
        // 生成订单号
        order.setOrderNo(
                "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setStatus(0); // 待支付
        orderMapper.insert(order);
        return Result.success(null);
    }

    public Result<CourseOrder> findById(Integer id) {
        CourseOrder order = orderMapper.findById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    public Result<Void> updateOrder(CourseOrder order) {
        CourseOrder existingOrder = orderMapper.findById(order.getId());
        if (existingOrder == null) {
            return Result.error("订单不存在");
        }
        orderMapper.update(order);
        return Result.success(null);
    }

    public Result<Void> deleteOrder(Integer id) {
        CourseOrder order = orderMapper.findById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        orderMapper.delete(id);
        return Result.success(null);
    }

    public Result<Void> cancelOrder(Integer id) {
        CourseOrder order = orderMapper.findById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (order.getStatus() != 0) {
            return Result.error("只能取消待支付的订单");
        }
        order.setStatus(2);
        orderMapper.update(order);
        return Result.success(null);
    }

    @Transactional
    public Result<Void> payOrder(Integer id) {
        logger.info("开始处理订单支付，订单ID: {}", id);

        try {
            CourseOrder order = orderMapper.findById(id);
            if (order == null) {
                logger.warn("订单不存在，订单ID: {}", id);
                return Result.error("订单不存在");
            }
            if (order.getStatus() != 0) {
                logger.warn("订单状态错误，订单ID: {}, 当前状态: {}", id, order.getStatus());
                return Result.error("订单状态错误");
            }

            User user = userMapper.findById(order.getUserId());
            if (user.getBalance().compareTo(order.getPrice()) < 0) {
                logger.warn("用户余额不足，用户ID: {}, 余额: {}, 订单金额: {}",
                        order.getUserId(), user.getBalance(), order.getPrice());
                return Result.error("余额不足");
            }

            userMapper.updateBalance(order.getUserId(), order.getPrice().negate());
            order.setStatus(1);
            orderMapper.update(order);

            logger.info("订单支付成功，订单ID: {}, 用户ID: {}, 金额: {}",
                    id, order.getUserId(), order.getPrice());
            return Result.success(null);
        } catch (Exception e) {
            logger.error("订单支付失败，订单ID: {}", id, e);
            throw e;
        }
    }
}
