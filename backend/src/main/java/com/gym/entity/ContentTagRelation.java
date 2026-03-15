package com.gym.entity;

import lombok.Data;

@Data
public class ContentTagRelation {
    private Integer id;
    private Integer contentId;
    private Integer tagId;
}
