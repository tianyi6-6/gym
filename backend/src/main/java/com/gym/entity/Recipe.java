package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Recipe {
    private Integer id;
    private String title;
    private String content;
    private String image;
    private String category;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}

