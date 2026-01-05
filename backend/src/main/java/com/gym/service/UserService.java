package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public Result<Void> register(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return Result.error("密码不能为空");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            return Result.error("姓名不能为空");
        }
        
        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser != null) {
            return Result.error("用户名已存在");
        }
        
        if (user.getMemberLevel() == null || user.getMemberLevel().isEmpty()) {
            user.setMemberLevel("普通会员");
        }
        
        userMapper.insert(user);
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
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword("123456");
            }
            userMapper.insert(user);
        } else {
            userMapper.update(user);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        userMapper.delete(id);
        return Result.success(null);
    }
}
