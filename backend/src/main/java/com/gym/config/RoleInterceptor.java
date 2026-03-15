package com.gym.config;

import com.gym.common.RequireRole;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        System.out.println("=== RoleInterceptor: Request URI = " + uri + ", Method = " + request.getMethod());

        if (!(handler instanceof HandlerMethod)) {
            System.out.println("=== RoleInterceptor: Not a HandlerMethod, allowing access");
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);

        if (requireRole == null) {
            System.out.println("=== RoleInterceptor: No @RequireRole annotation, allowing access");
            return true;
        }

        String role = request.getHeader("X-Role");
        System.out.println("=== RoleInterceptor: Required roles = " + String.join(", ", requireRole.value())
                + ", User role = " + role);

        if (role == null) {
            System.out.println("=== RoleInterceptor: No role in header, returning 401");
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或角色信息缺失\"}");
            return false;
        }

        String[] requiredRoles = requireRole.value();
        for (String requiredRole : requiredRoles) {
            if (requiredRole.equals(role)) {
                System.out.println("=== RoleInterceptor: Role matched, allowing access");
                return true;
            }
        }

        System.out.println("=== RoleInterceptor: Role not matched, returning 403");
        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":403,\"message\":\"权限不足\"}");
        return false;
    }
}
