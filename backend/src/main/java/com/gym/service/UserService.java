package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Result<Void> register(User user) {
        logger.info("开始处理用户注册: {}", user.getUsername());

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            logger.warn("注册失败：用户名为空");
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.warn("注册失败：密码为空");
            return Result.error("密码不能为空");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            logger.warn("注册失败：姓名为空");
            return Result.error("姓名不能为空");
        }

        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser != null) {
            logger.warn("注册失败：用户名已存在: {}", user.getUsername());
            return Result.error("用户名已存在");
        }

        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            User existingPhoneUser = userMapper.findByPhone(user.getPhone());
            if (existingPhoneUser != null) {
                logger.warn("注册失败：手机号已被注册: {}", user.getPhone());
                return Result.error("手机号已被注册");
            }
        }

        if (user.getMemberLevel() == null || user.getMemberLevel().isEmpty()) {
            user.setMemberLevel("普通会员");
        }

        // 使用BCrypt加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);

        logger.info("用户注册成功: {}", user.getUsername());
        return Result.success(null);
    }

    public Result<List<User>> findAll() {
        return Result.success(userMapper.findAll());
    }

    public Result<User> findById(Integer id) {
        return Result.success(userMapper.findById(id));
    }

    public Result<Void> save(User user) {
        if (user.getId() == null) {
            logger.info("开始处理新增用户: {}", user.getUsername());
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword("123456");
            }
            // 加密密码
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.insert(user);
            logger.info("新增用户成功: {}", user.getUsername());
        } else {
            logger.info("开始处理更新用户: {}", user.getUsername());
            // 如果密码被修改，重新加密
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userMapper.update(user);
            logger.info("更新用户成功: {}", user.getUsername());
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        userMapper.delete(id);
        return Result.success(null);
    }
}
