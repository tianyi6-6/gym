package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Admin {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private Date createTime;
    private Date updateTime;
}

