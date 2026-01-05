package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.*;
import com.gym.mapper.AdminMapper;
import com.gym.mapper.CoachMapper;
import com.gym.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  @Autowired
  private AdminMapper adminMapper;
  @Autowired
  private CoachMapper coachMapper;
  @Autowired
  private UserMapper userMapper;

  public Result<LoginResponse> login(LoginRequest request) {
    System.out.println("=== AuthService.login: request = " + request);
    String role = request.getRole();
    String username = request.getUsername();
    String password = request.getPassword();
    System.out.println(
        "=== AuthService.login: role = " + role + ", username = " + username + ", password = " + password);

    LoginResponse response = new LoginResponse();
    Object userInfo = null;

    if ("admin".equals(role)) {
      Admin admin = adminMapper.findByUsername(username);
      if (admin == null || !password.equals(admin.getPassword())) {
        return Result.error("用户名或密码错误");
      }
      response.setToken("");
      response.setUsername(admin.getUsername());
      response.setName(admin.getName());
      response.setRole(role);
      userInfo = admin;
    } else if ("coach".equals(role)) {
      Coach coach = coachMapper.findByUsername(username);
      if (coach == null || !password.equals(coach.getPassword())) {
        return Result.error("用户名或密码错误");
      }
      if (coach.getStatus() == 0) {
        return Result.error("账号已被禁用");
      }
      response.setToken("");
      response.setUsername(coach.getUsername());
      response.setName(coach.getName());
      response.setRole(role);
      userInfo = coach;
    } else if ("user".equals(role)) {
      User user = userMapper.findByUsername(username);
      if (user == null || !password.equals(user.getPassword())) {
        return Result.error("用户名或密码错误");
      }
      if (user.getStatus() == 0) {
        return Result.error("账号已被禁用");
      }
      response.setToken("");
      response.setUsername(user.getUsername());
      response.setName(user.getName());
      response.setRole(role);
      userInfo = user;
    } else {
      return Result.error("角色类型错误");
    }

    response.setUserInfo(userInfo);
    return Result.success(response);
  }
}
