package com.gym.mapper;

import com.gym.entity.HealthRiskWarning;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface HealthRiskWarningMapper {
    HealthRiskWarning findById(@Param("id") Integer id);
    List<HealthRiskWarning> findByUserId(@Param("userId") Integer userId);
    List<HealthRiskWarning> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") Integer status);
    List<HealthRiskWarning> findByStatus(@Param("status") Integer status);
    List<HealthRiskWarning> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    List<HealthRiskWarning> findAll();
    int insert(HealthRiskWarning healthRiskWarning);
    int update(HealthRiskWarning healthRiskWarning);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    int updateHandleStatus(@Param("id") Integer id, @Param("handleStatus") Integer handleStatus);
    int delete(@Param("id") Integer id);
    int deleteByUserId(@Param("userId") Integer userId);
}
