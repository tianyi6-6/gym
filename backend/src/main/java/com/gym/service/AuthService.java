package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.*;
import com.gym.mapper.AdminMapper;
import com.gym.mapper.CoachMapper;
import com.gym.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  @Autowired
  private AdminMapper adminMapper;
  @Autowired
  private CoachMapper coachMapper;
  @Autowired
  private UserMapper userMapper;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  public Result<LoginResponse> login(LoginRequest request) {
    logger.info("开始处理登录请求: {}", request);
    String role = request.getRole();
    String username = request.getUsername();
    String password = request.getPassword();
    logger.debug("登录信息: role = {}, username = {}, password = ******", role, username);

    LoginResponse response = new LoginResponse();
    Object userInfo = null;

    if ("admin".equals(role)) {
      Admin admin = adminMapper.findByUsername(username);
      if (admin == null
          || (!passwordEncoder.matches(password, admin.getPassword()) && !password.equals(admin.getPassword()))) {
        logger.warn("管理员登录失败，用户名或密码错误: {}", username);
        return Result.error("用户名或密码错误");
      }
      response.setToken("");
      response.setUsername(admin.getUsername());
      response.setName(admin.getName());
      response.setRole(role);
      userInfo = admin;
      logger.info("管理员登录成功: {}", username);
    } else if ("coach".equals(role)) {
      Coach coach = coachMapper.findByUsername(username);
      if (coach == null
          || (!passwordEncoder.matches(password, coach.getPassword()) && !password.equals(coach.getPassword()))) {
        logger.warn("教练登录失败，用户名或密码错误: {}", username);
        return Result.error("用户名或密码错误");
      }
      if (coach.getStatus() == 0) {
        logger.warn("教练登录失败，账号已被禁用: {}", username);
        return Result.error("账号已被禁用");
      }
      response.setToken("");
      response.setUsername(coach.getUsername());
      response.setName(coach.getName());
      response.setRole(role);
      userInfo = coach;
      logger.info("教练登录成功: {}", username);
    } else if ("user".equals(role)) {
      User user = userMapper.findByUsername(username);
      if (user == null
          || (!passwordEncoder.matches(password, user.getPassword()) && !password.equals(user.getPassword()))) {
        logger.warn("用户登录失败，用户名或密码错误: {}", username);
        return Result.error("用户名或密码错误");
      }
      if (user.getStatus() == 0) {
        logger.warn("用户登录失败，账号已被禁用: {}", username);
        return Result.error("账号已被禁用");
      }
      response.setToken("");
      response.setUsername(user.getUsername());
      response.setName(user.getName());
      response.setRole(role);
      userInfo = user;
      logger.info("用户登录成功: {}", username);
    } else {
      logger.warn("登录失败，角色类型错误: {}", role);
      return Result.error("角色类型错误");
    }

    response.setUserInfo(userInfo);
    logger.info("登录请求处理完成: {}", username);
    return Result.success(response);
  }
}
