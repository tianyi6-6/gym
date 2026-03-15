package com.gym.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具类
 * 用于生成BCrypt加密的密码
 * 
 * 使用方法：
 * 1. 运行此类的main方法
 * 2. 复制输出的加密密码
 * 3. 更新数据库中的password字段
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 生成123456的加密密码
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        
        System.out.println("========================================");
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt加密后: " + encodedPassword);
        System.out.println("========================================");
        
        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("验证结果: " + (matches ? "✓ 密码匹配" : "✗ 密码不匹配"));
        System.out.println("========================================");
        System.out.println();
        System.out.println("SQL更新语句：");
        System.out.println("UPDATE admin SET password = '" + encodedPassword + "' WHERE username = 'admin';");
        System.out.println("UPDATE coach SET password = '" + encodedPassword + "' WHERE username = 'zhang';");
        System.out.println("UPDATE user SET password = '" + encodedPassword + "' WHERE username = 'zhangsan';");
    }
}
