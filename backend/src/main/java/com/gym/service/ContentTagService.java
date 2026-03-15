package com.gym.service;

import com.gym.entity.ContentTag;
import java.util.List;

public interface ContentTagService {
    void createTag(ContentTag tag);
    void updateTag(ContentTag tag);
    void deleteTag(Integer id);
    ContentTag getTagById(Integer id);
    ContentTag getTagByName(String name);
    List<ContentTag> getAllTags();
    List<ContentTag> getTagsByContentId(Integer contentId);
}
