package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.CourseOrder;
import com.gym.entity.User;
import com.gym.mapper.CourseOrderMapper;
import com.gym.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseOrderServiceTest {

    @Mock
    private CourseOrderMapper orderMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CourseOrderService courseOrderService;

    @Test
    public void testPayOrderSuccess() {
        // 准备测试数据
        Integer orderId = 1;
        CourseOrder order = new CourseOrder();
        order.setId(orderId);
        order.setUserId(1);
        order.setPrice(new BigDecimal(200));
        order.setStatus(0); // 待支付

        User user = new User();
        user.setBalance(new BigDecimal(500));

        // 模拟方法调用
        when(orderMapper.findById(orderId)).thenReturn(order);
        when(userMapper.findById(1)).thenReturn(user);
        when(userMapper.updateBalance(1, new BigDecimal(-200))).thenReturn(1);
        when(orderMapper.update(order)).thenReturn(1);

        // 执行测试
        Result<Void> result = courseOrderService.payOrder(orderId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals(1, order.getStatus()); // 状态应该变为已支付

        // 验证方法调用
        verify(orderMapper, times(1)).findById(orderId);
        verify(userMapper, times(1)).findById(1);
        verify(userMapper, times(1)).updateBalance(1, new BigDecimal(-200));
        verify(orderMapper, times(1)).update(order);
    }

    @Test
    public void testPayOrderWithInsufficientBalance() {
        // 准备测试数据
        Integer orderId = 1;
        CourseOrder order = new CourseOrder();
        order.setId(orderId);
        order.setUserId(1);
        order.setPrice(new BigDecimal(300));
        order.setStatus(0); // 待支付

        User user = new User();
        user.setBalance(new BigDecimal(100)); // 余额不足

        // 模拟方法调用
        when(orderMapper.findById(orderId)).thenReturn(order);
        when(userMapper.findById(1)).thenReturn(user);

        // 执行测试
        Result<Void> result = courseOrderService.payOrder(orderId);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("余额不足", result.getMessage());

        // 验证方法调用
        verify(orderMapper, times(1)).findById(orderId);
        verify(userMapper, times(1)).findById(1);
        verify(userMapper, never()).updateBalance(anyInt(), any());
        verify(orderMapper, never()).update(any());
    }

    @Test
    public void testPayOrderWithInvalidStatus() {
        // 准备测试数据
        Integer orderId = 1;
        CourseOrder order = new CourseOrder();
        order.setId(orderId);
        order.setUserId(1);
        order.setPrice(new BigDecimal(200));
        order.setStatus(1); // 已支付，状态错误

        // 模拟方法调用
        when(orderMapper.findById(orderId)).thenReturn(order);

        // 执行测试
        Result<Void> result = courseOrderService.payOrder(orderId);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("订单状态错误", result.getMessage());

        // 验证方法调用
        verify(orderMapper, times(1)).findById(orderId);
        verify(userMapper, never()).findById(anyInt());
        verify(userMapper, never()).updateBalance(anyInt(), any());
        verify(orderMapper, never()).update(any());
    }

    @Test
    public void testPayOrderWithNonExistentOrder() {
        // 准备测试数据
        Integer orderId = 1;

        // 模拟方法调用
        when(orderMapper.findById(orderId)).thenReturn(null);

        // 执行测试
        Result<Void> result = courseOrderService.payOrder(orderId);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("订单不存在", result.getMessage());

        // 验证方法调用
        verify(orderMapper, times(1)).findById(orderId);
        verify(userMapper, never()).findById(anyInt());
        verify(userMapper, never()).updateBalance(anyInt(), any());
        verify(orderMapper, never()).update(any());
    }
}
