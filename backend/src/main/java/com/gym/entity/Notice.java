package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Notice {
    private Integer id;
    private String title;
    private String content;
    private Integer type;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}

