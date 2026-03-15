package com.gym.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private BigDecimal balance;
    private String memberLevel;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}

