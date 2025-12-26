package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result<List<User>> list() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return userService.delete(id);
    }
}

