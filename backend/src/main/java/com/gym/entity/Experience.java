package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Experience {
    private Integer id;
    private Integer userId;
    private Integer coachId;
    private String title;
    private String content;
    private String image;
    private Integer viewCount;
    private Integer likeCount;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    // 关联字段
    private String userName;
    private String coachName;
}

