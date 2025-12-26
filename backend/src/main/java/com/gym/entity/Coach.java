package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Coach {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private String specialty;
    private String experience;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}

