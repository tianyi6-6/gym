package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.CoachHealthFitness;
import com.gym.mapper.CoachHealthFitnessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoachHealthFitnessService {
  @Autowired
  private CoachHealthFitnessMapper coachHealthFitnessMapper;

  public Result<List<CoachHealthFitness>> findByCoachId(Integer coachId) {
    return Result.success(coachHealthFitnessMapper.findByCoachId(coachId));
  }

  public Result<List<CoachHealthFitness>> findByCoachIdAndLimit(Integer coachId, int limit) {
    return Result.success(coachHealthFitnessMapper.findByCoachIdAndLimit(coachId, limit));
  }

  public Result<List<CoachHealthFitness>> findRecentByCoachId(Integer coachId, int days) {
    return Result.success(coachHealthFitnessMapper.findRecentByCoachId(coachId, days));
  }

  public Result<CoachHealthFitness> findLatestByCoachId(Integer coachId) {
    return Result.success(coachHealthFitnessMapper.findLatestByCoachId(coachId));
  }

  public Result<Map<String, Object>> getHealthFitnessData(Integer coachId) {
    CoachHealthFitness latest = coachHealthFitnessMapper.findLatestByCoachId(coachId);
    List<CoachHealthFitness> history = coachHealthFitnessMapper.findByCoachIdAndLimit(coachId, 10);

    Map<String, Object> data = new HashMap<>();

    if (latest != null) {
      data.put("weight", latest.getWeight());
      data.put("bodyFatRate", latest.getBodyFatRate());
      data.put("heartRate", latest.getHeartRate());
      data.put("trainingHours", latest.getTrainingHours());
      data.put("caloriesBurned", latest.getCaloriesBurned());
      data.put("muscleMass", latest.getMuscleMass());
      data.put("bmi", latest.getBmi());

      Map<String, Integer> abilityScores = new HashMap<>();
      abilityScores.put("strength", latest.getStrengthScore());
      abilityScores.put("endurance", latest.getEnduranceScore());
      abilityScores.put("flexibility", latest.getFlexibilityScore());
      abilityScores.put("speed", latest.getSpeedScore());
      abilityScores.put("coordination", latest.getCoordinationScore());
      data.put("abilityScores", abilityScores);
    } else {
      data.put("weight", 0.0);
      data.put("bodyFatRate", 0.0);
      data.put("heartRate", 0);
      data.put("trainingHours", 0.0);
      data.put("caloriesBurned", 0);
      data.put("muscleMass", 0.0);
      data.put("bmi", 0.0);

      Map<String, Integer> abilityScores = new HashMap<>();
      abilityScores.put("strength", 0);
      abilityScores.put("endurance", 0);
      abilityScores.put("flexibility", 0);
      abilityScores.put("speed", 0);
      abilityScores.put("coordination", 0);
      data.put("abilityScores", abilityScores);
    }

    String[] dates = new String[history.size()];
    double[] weights = new double[history.size()];
    double[] bodyFatRates = new double[history.size()];
    int[] heartRates = new int[history.size()];

    for (int i = 0; i < history.size(); i++) {
      CoachHealthFitness record = history.get(i);
      dates[i] = record.getRecordDate().toString();
      weights[i] = record.getWeight().doubleValue();
      bodyFatRates[i] = record.getBodyFatRate().doubleValue();
      heartRates[i] = record.getHeartRate();
    }

    data.put("dates", dates);
    data.put("weights", weights);
    data.put("bodyFatRates", bodyFatRates);
    data.put("heartRates", heartRates);

    return Result.success(data);
  }

  public Result<Map<String, Object>> getWeeklyAverageData(Integer coachId) {
    List<CoachHealthFitness> recentRecords = coachHealthFitnessMapper.findRecentByCoachId(coachId, 7);

    Map<String, Object> weeklyData = new HashMap<>();

    if (recentRecords.isEmpty()) {
      weeklyData.put("weight", 0.0);
      weeklyData.put("bodyFatRate", 0.0);
      weeklyData.put("heartRate", 0);
      weeklyData.put("trainingHours", 0.0);
      weeklyData.put("caloriesBurned", 0);
      weeklyData.put("muscleMass", 0.0);
      weeklyData.put("bmi", 0.0);
      weeklyData.put("strengthScore", 0);
      weeklyData.put("enduranceScore", 0);
      weeklyData.put("flexibilityScore", 0);
      weeklyData.put("speedScore", 0);
      weeklyData.put("coordinationScore", 0);
      weeklyData.put("recordCount", 0);
    } else {
      double totalWeight = 0.0;
      double totalBodyFatRate = 0.0;
      int totalHeartRate = 0;
      double totalTrainingHours = 0.0;
      int totalCaloriesBurned = 0;
      double totalMuscleMass = 0.0;
      double totalBmi = 0.0;
      int totalStrengthScore = 0;
      int totalEnduranceScore = 0;
      int totalFlexibilityScore = 0;
      int totalSpeedScore = 0;
      int totalCoordinationScore = 0;

      for (CoachHealthFitness record : recentRecords) {
        totalWeight += record.getWeight().doubleValue();
        totalBodyFatRate += record.getBodyFatRate().doubleValue();
        totalHeartRate += record.getHeartRate();
        totalTrainingHours += record.getTrainingHours().doubleValue();
        totalCaloriesBurned += record.getCaloriesBurned();
        totalMuscleMass += record.getMuscleMass().doubleValue();
        totalBmi += record.getBmi().doubleValue();
        totalStrengthScore += record.getStrengthScore();
        totalEnduranceScore += record.getEnduranceScore();
        totalFlexibilityScore += record.getFlexibilityScore();
        totalSpeedScore += record.getSpeedScore();
        totalCoordinationScore += record.getCoordinationScore();
      }

      int count = recentRecords.size();
      weeklyData.put("weight", totalWeight / count);
      weeklyData.put("bodyFatRate", totalBodyFatRate / count);
      weeklyData.put("heartRate", totalHeartRate / count);
      weeklyData.put("trainingHours", totalTrainingHours / count);
      weeklyData.put("caloriesBurned", totalCaloriesBurned / count);
      weeklyData.put("muscleMass", totalMuscleMass / count);
      weeklyData.put("bmi", totalBmi / count);
      weeklyData.put("strengthScore", totalStrengthScore / count);
      weeklyData.put("enduranceScore", totalEnduranceScore / count);
      weeklyData.put("flexibilityScore", totalFlexibilityScore / count);
      weeklyData.put("speedScore", totalSpeedScore / count);
      weeklyData.put("coordinationScore", totalCoordinationScore / count);
      weeklyData.put("recordCount", count);
    }

    return Result.success(weeklyData);
  }

  public Result<CoachHealthFitness> save(CoachHealthFitness healthFitness) {
    if (healthFitness.getId() == null) {
      coachHealthFitnessMapper.insert(healthFitness);
    } else {
      coachHealthFitnessMapper.update(healthFitness);
    }
    return Result.success(healthFitness);
  }

  public Result<Void> delete(Integer id) {
    coachHealthFitnessMapper.delete(id);
    return Result.success(null);
  }
}