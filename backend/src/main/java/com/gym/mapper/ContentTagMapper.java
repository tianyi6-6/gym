package com.gym.mapper;

import com.gym.entity.ContentTag;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ContentTagMapper {
    void insert(ContentTag tag);
    void update(ContentTag tag);
    void delete(Integer id);
    ContentTag selectById(Integer id);
    ContentTag selectByName(String name);
    List<ContentTag> selectAll();
    List<ContentTag> selectByContentId(Integer contentId);
    void incrementUseCount(Integer id);
    void decrementUseCount(Integer id);
}
