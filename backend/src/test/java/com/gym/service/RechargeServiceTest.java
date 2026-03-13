package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Recharge;
import com.gym.mapper.RechargeMapper;
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
public class RechargeServiceTest {

    @Mock
    private RechargeMapper rechargeMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RechargeService rechargeService;

    @Test
    public void testRechargeSuccess() {
        // 准备测试数据
        Recharge recharge = new Recharge();
        recharge.setUserId(1);
        recharge.setAmount(new BigDecimal(100));
        recharge.setType(1); // 充值
        recharge.setRemark("测试充值");

        // 模拟方法调用
        when(rechargeMapper.insert(recharge)).thenReturn(1);
        when(userMapper.updateBalance(1, new BigDecimal(100))).thenReturn(1);

        // 执行测试
        Result<Void> result = rechargeService.recharge(recharge);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());

        // 验证方法调用
        verify(rechargeMapper, times(1)).insert(recharge);
        verify(userMapper, times(1)).updateBalance(1, new BigDecimal(100));
    }

    @Test
    public void testConsumeSuccess() {
        // 准备测试数据
        Recharge recharge = new Recharge();
        recharge.setUserId(1);
        recharge.setAmount(new BigDecimal(50));
        recharge.setType(2); // 消费
        recharge.setRemark("测试消费");

        // 模拟方法调用
        when(rechargeMapper.insert(recharge)).thenReturn(1);
        when(userMapper.updateBalance(1, new BigDecimal(-50))).thenReturn(1);

        // 执行测试
        Result<Void> result = rechargeService.recharge(recharge);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());

        // 验证方法调用
        verify(rechargeMapper, times(1)).insert(recharge);
        verify(userMapper, times(1)).updateBalance(1, new BigDecimal(-50));
    }

    @Test
    public void testRechargeWithException() {
        // 准备测试数据
        Recharge recharge = new Recharge();
        recharge.setUserId(1);
        recharge.setAmount(new BigDecimal(100));
        recharge.setType(1); // 充值
        recharge.setRemark("测试充值");

        // 模拟方法调用，抛出异常
        when(rechargeMapper.insert(recharge)).thenReturn(1);
        when(userMapper.updateBalance(1, new BigDecimal(100))).thenThrow(new RuntimeException("数据库操作失败"));

        // 执行测试，验证异常是否被正确处理
        try {
            rechargeService.recharge(recharge);
        } catch (Exception e) {
            assertEquals("数据库操作失败", e.getMessage());
        }

        // 验证方法调用
        verify(rechargeMapper, times(1)).insert(recharge);
        verify(userMapper, times(1)).updateBalance(1, new BigDecimal(100));
    }
}
