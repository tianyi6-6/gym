package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.CoachCourseTrend;
import com.gym.mapper.CoachCourseTrendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoachCourseTrendService {
  @Autowired
  private CoachCourseTrendMapper coachCourseTrendMapper;

  public Result<List<CoachCourseTrend>> findByCoachId(Integer coachId) {
    return Result.success(coachCourseTrendMapper.findByCoachId(coachId));
  }

  public Result<List<CoachCourseTrend>> findByCoachIdAndMonths(Integer coachId, int months) {
    return Result.success(coachCourseTrendMapper.findByCoachIdAndMonths(coachId, months));
  }

  public Result<Map<String, Object>> getCourseTrendData(Integer coachId) {
    List<CoachCourseTrend> trends = coachCourseTrendMapper.findByCoachIdAndMonths(coachId, 6);

    Map<String, Object> data = new HashMap<>();
    String[] months = new String[trends.size()];
    int[] courseCounts = new int[trends.size()];
    int[] studentCounts = new int[trends.size()];
    double[] revenues = new double[trends.size()];

    for (int i = trends.size() - 1; i >= 0; i--) {
      CoachCourseTrend trend = trends.get(i);
      int index = trends.size() - 1 - i;
      months[index] = trend.getYear() + "年" + trend.getMonth() + "月";
      courseCounts[index] = trend.getCourseCount();
      studentCounts[index] = trend.getStudentCount();
      revenues[index] = trend.getRevenue().doubleValue();
    }

    data.put("months", months);
    data.put("courseCounts", courseCounts);
    data.put("studentCounts", studentCounts);
    data.put("revenues", revenues);

    return Result.success(data);
  }

  public Result<Void> save(CoachCourseTrend trend) {
    if (trend.getId() == null) {
      coachCourseTrendMapper.insert(trend);
    } else {
      coachCourseTrendMapper.update(trend);
    }
    return Result.success(null);
  }

  public Result<Void> delete(Integer id) {
    coachCourseTrendMapper.delete(id);
    return Result.success(null);
  }
}