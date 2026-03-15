package com.gym.entity;

import lombok.Data;
import java.util.Date;

@Data
public class FitnessContent {
    private Integer id;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Integer authorId;
    private String authorName;
    private Integer status;
    private Integer viewCount;
    private Integer likeCount;
    private Integer collectCount;
    private Integer shareCount;
    private Date createTime;
    private Date updateTime;
    private Date publishTime;
}
