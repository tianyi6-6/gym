package com.gym.service.impl;

import com.gym.entity.FitnessContent;
import com.gym.entity.ContentTagRelation;
import com.gym.mapper.FitnessContentMapper;
import com.gym.mapper.ContentTagMapper;
import com.gym.mapper.ContentTagRelationMapper;
import com.gym.service.FitnessContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class FitnessContentServiceImpl implements FitnessContentService {

    @Autowired
    private FitnessContentMapper contentMapper;

    @Autowired
    private ContentTagMapper tagMapper;

    @Autowired
    private ContentTagRelationMapper relationMapper;

    @Override
    @Transactional
    public void createContent(FitnessContent content, List<Integer> tagIds) {
        contentMapper.insert(content);
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Integer tagId : tagIds) {
                ContentTagRelation relation = new ContentTagRelation();
                relation.setContentId(content.getId());
                relation.setTagId(tagId);
                relationMapper.insert(relation);
                tagMapper.incrementUseCount(tagId);
            }
        }
    }

    @Override
    @Transactional
    public void updateContent(FitnessContent content, List<Integer> tagIds) {
        contentMapper.update(content);
        // 先删除旧的标签关联
        relationMapper.deleteByContentId(content.getId());
        // 添加新的标签关联
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Integer tagId : tagIds) {
                ContentTagRelation relation = new ContentTagRelation();
                relation.setContentId(content.getId());
                relation.setTagId(tagId);
                relationMapper.insert(relation);
                tagMapper.incrementUseCount(tagId);
            }
        }
    }

    @Override
    @Transactional
    public void deleteContent(Integer id) {
        // 先删除标签关联
        List<ContentTagRelation> relations = relationMapper.selectByContentId(id);
        for (ContentTagRelation relation : relations) {
            tagMapper.decrementUseCount(relation.getTagId());
        }
        relationMapper.deleteByContentId(id);
        // 删除内容
        contentMapper.delete(id);
    }

    @Override
    public FitnessContent getContentById(Integer id) {
        return contentMapper.selectById(id);
    }

    @Override
    public List<FitnessContent> getContentsByAuthorId(Integer authorId) {
        return contentMapper.selectByAuthorId(authorId);
    }

    @Override
    public List<FitnessContent> getContentsByStatus(Integer status) {
        return contentMapper.selectByStatus(status);
    }

    @Override
    public List<FitnessContent> getContentsByTagId(Integer tagId) {
        return contentMapper.selectByTagId(tagId);
    }

    @Override
    public List<FitnessContent> getContentsByConditions(Map<String, Object> conditions) {
        return contentMapper.selectByConditions(conditions);
    }

    @Override
    public List<FitnessContent> getHotContents(Integer limit) {
        return contentMapper.selectHotContent(limit);
    }

    @Override
    public void incrementViewCount(Integer id) {
        contentMapper.incrementViewCount(id);
    }

    @Override
    public void incrementLikeCount(Integer id) {
        contentMapper.incrementLikeCount(id);
    }

    @Override
    public void incrementCollectCount(Integer id) {
        contentMapper.incrementCollectCount(id);
    }

    @Override
    public void incrementShareCount(Integer id) {
        contentMapper.incrementShareCount(id);
    }
}
