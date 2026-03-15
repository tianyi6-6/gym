package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Equipment {
    private Integer id;
    private String name;
    private String type;
    private String description;
    private String image;
    private Integer totalCount;
    private Integer availableCount;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}

