package com.gym.service;

import com.gym.entity.FitnessContent;
import java.util.List;
import java.util.Map;

public interface FitnessContentService {
    void createContent(FitnessContent content, List<Integer> tagIds);
    void updateContent(FitnessContent content, List<Integer> tagIds);
    void deleteContent(Integer id);
    FitnessContent getContentById(Integer id);
    List<FitnessContent> getContentsByAuthorId(Integer authorId);
    List<FitnessContent> getContentsByStatus(Integer status);
    List<FitnessContent> getContentsByTagId(Integer tagId);
    List<FitnessContent> getContentsByConditions(Map<String, Object> conditions);
    List<FitnessContent> getHotContents(Integer limit);
    void incrementViewCount(Integer id);
    void incrementLikeCount(Integer id);
    void incrementCollectCount(Integer id);
    void incrementShareCount(Integer id);
}
