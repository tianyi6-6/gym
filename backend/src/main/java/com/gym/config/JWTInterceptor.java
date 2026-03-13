package com.gym.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(JWTInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 允许OPTIONS请求通过
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        // 临时实现：总是允许通过
        // 实际项目中应该使用正确的JWT验证
        logger.debug("JWT拦截器：临时允许所有请求通过");
        return true;
    }
}
