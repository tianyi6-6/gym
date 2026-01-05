package com.gym.mapper;

import com.gym.entity.CoachHealthFitness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CoachHealthFitnessMapper {
    List<CoachHealthFitness> findByCoachId(@Param("coachId") Integer coachId);

    List<CoachHealthFitness> findByCoachIdAndLimit(@Param("coachId") Integer coachId, @Param("limit") int limit);

    List<CoachHealthFitness> findRecentByCoachId(@Param("coachId") Integer coachId, @Param("days") int days);

    CoachHealthFitness findLatestByCoachId(@Param("coachId") Integer coachId);

    CoachHealthFitness findByCoachIdAndDate(@Param("coachId") Integer coachId, @Param("recordDate") String recordDate);

    int insert(CoachHealthFitness healthFitness);

    int update(CoachHealthFitness healthFitness);

    int delete(@Param("id") Integer id);
}