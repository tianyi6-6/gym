package com.gym.util;

import org.springframework.stereotype.Component;

@Component
public class JWTUtil {
    // 临时实现，返回空字符串
    public String generateToken(String username, String role, Integer userId) {
        return "";
    }

    // 临时实现，返回null
    public Object parseToken(String token) {
        return null;
    }

    // 临时实现，总是返回true
    public boolean validateToken(String token) {
        return true;
    }
}
