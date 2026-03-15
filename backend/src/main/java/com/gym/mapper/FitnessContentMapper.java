package com.gym.mapper;

import com.gym.entity.FitnessContent;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface FitnessContentMapper {
    void insert(FitnessContent content);
    void update(FitnessContent content);
    void delete(Integer id);
    FitnessContent selectById(Integer id);
    List<FitnessContent> selectByAuthorId(Integer authorId);
    List<FitnessContent> selectByStatus(Integer status);
    List<FitnessContent> selectByTagId(Integer tagId);
    List<FitnessContent> selectByConditions(Map<String, Object> conditions);
    List<FitnessContent> selectHotContent(Integer limit);
    void incrementViewCount(Integer id);
    void incrementLikeCount(Integer id);
    void incrementCollectCount(Integer id);
    void incrementShareCount(Integer id);
}
