package com.gym.service.impl;

import com.gym.entity.ContentTag;
import com.gym.mapper.ContentTagMapper;
import com.gym.service.ContentTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContentTagServiceImpl implements ContentTagService {

    @Autowired
    private ContentTagMapper tagMapper;

    @Override
    public void createTag(ContentTag tag) {
        tagMapper.insert(tag);
    }

    @Override
    public void updateTag(ContentTag tag) {
        tagMapper.update(tag);
    }

    @Override
    public void deleteTag(Integer id) {
        tagMapper.delete(id);
    }

    @Override
    public ContentTag getTagById(Integer id) {
        return tagMapper.selectById(id);
    }

    @Override
    public ContentTag getTagByName(String name) {
        return tagMapper.selectByName(name);
    }

    @Override
    public List<ContentTag> getAllTags() {
        return tagMapper.selectAll();
    }

    @Override
    public List<ContentTag> getTagsByContentId(Integer contentId) {
        return tagMapper.selectByContentId(contentId);
    }
}
