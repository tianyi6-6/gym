package com.gym.mapper;

import com.gym.entity.BodyAssessment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface BodyAssessmentMapper {
    BodyAssessment findById(@Param("id") Integer id);
    List<BodyAssessment> findByUserId(@Param("userId") Integer userId);
    List<BodyAssessment> findByUserIdOrderByDate(@Param("userId") Integer userId);
    List<BodyAssessment> findByDateRange(@Param("userId") Integer userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    List<BodyAssessment> findByCoachId(@Param("coachId") Integer coachId);
    List<BodyAssessment> findAll();
    BodyAssessment findLatestByUserId(@Param("userId") Integer userId);
    int insert(BodyAssessment bodyAssessment);
    int update(BodyAssessment bodyAssessment);
    int delete(@Param("id") Integer id);
    int deleteByUserId(@Param("userId") Integer userId);
}
