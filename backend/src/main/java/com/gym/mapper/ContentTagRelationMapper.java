package com.gym.mapper;

import com.gym.entity.ContentTagRelation;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ContentTagRelationMapper {
    void insert(ContentTagRelation relation);
    void delete(Integer id);
    void deleteByContentId(Integer contentId);
    List<ContentTagRelation> selectByContentId(Integer contentId);
    List<ContentTagRelation> selectByTagId(Integer tagId);
}
