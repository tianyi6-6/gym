package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ContentTag {
    private Integer id;
    private String name;
    private String description;
    private Integer useCount;
    private Date createTime;
    private Date updateTime;
}
