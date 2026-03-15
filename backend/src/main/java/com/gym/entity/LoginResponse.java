package com.gym.entity;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String username;
    private String name;
    private String role;
    private Object userInfo;
}

