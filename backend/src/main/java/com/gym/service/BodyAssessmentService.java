package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.BodyAssessment;
import com.gym.mapper.BodyAssessmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BodyAssessmentService {
    @Autowired
    private BodyAssessmentMapper bodyAssessmentMapper;

    public Result<BodyAssessment> findById(Integer id) {
        BodyAssessment assessment = bodyAssessmentMapper.findById(id);
        if (assessment == null) {
            return Result.error("体测记录不存在");
        }
        return Result.success(assessment);
    }

    public Result<List<BodyAssessment>> findByUserId(Integer userId) {
        return Result.success(bodyAssessmentMapper.findByUserId(userId));
    }

    public Result<List<BodyAssessment>> findByUserIdOrderByDate(Integer userId) {
        return Result.success(bodyAssessmentMapper.findByUserIdOrderByDate(userId));
    }

    public Result<List<BodyAssessment>> findByDateRange(Integer userId, Date startDate, Date endDate) {
        return Result.success(bodyAssessmentMapper.findByDateRange(userId, startDate, endDate));
    }

    public Result<List<BodyAssessment>> findByCoachId(Integer coachId) {
        return Result.success(bodyAssessmentMapper.findByCoachId(coachId));
    }

    public Result<List<BodyAssessment>> findAll() {
        return Result.success(bodyAssessmentMapper.findAll());
    }

    public Result<BodyAssessment> findLatestByUserId(Integer userId) {
        BodyAssessment assessment = bodyAssessmentMapper.findLatestByUserId(userId);
        if (assessment == null) {
            return Result.error("暂无体测记录");
        }
        return Result.success(assessment);
    }

    public Result<Void> save(BodyAssessment bodyAssessment) {
        if (bodyAssessment.getId() == null) {
            if (bodyAssessment.getAssessmentDate() == null) {
                bodyAssessment.setAssessmentDate(new Date());
            }
            calculateBMI(bodyAssessment);
            generateAssessmentResult(bodyAssessment);
            bodyAssessmentMapper.insert(bodyAssessment);
        } else {
            calculateBMI(bodyAssessment);
            generateAssessmentResult(bodyAssessment);
            bodyAssessmentMapper.update(bodyAssessment);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        bodyAssessmentMapper.delete(id);
        return Result.success(null);
    }

    public Result<Void> deleteByUserId(Integer userId) {
        bodyAssessmentMapper.deleteByUserId(userId);
        return Result.success(null);
    }

    private void calculateBMI(BodyAssessment assessment) {
        if (assessment.getHeight() != null && assessment.getWeight() != null) {
            double heightInMeters = assessment.getHeight() / 100;
            double bmi = assessment.getWeight() / (heightInMeters * heightInMeters);
            assessment.setBmi(Math.round(bmi * 100.0) / 100.0);
        }
    }

    private void generateAssessmentResult(BodyAssessment assessment) {
        StringBuilder result = new StringBuilder();
        
        if (assessment.getBmi() != null) {
            double bmi = assessment.getBmi();
            if (bmi < 18.5) {
                result.append("BMI偏瘦;");
            } else if (bmi >= 18.5 && bmi < 24) {
                result.append("BMI正常;");
            } else if (bmi >= 24 && bmi < 28) {
                result.append("BMI超重;");
            } else {
                result.append("BMI肥胖;");
            }
        }

        if (assessment.getBodyFatRate() != null) {
            double bodyFatRate = assessment.getBodyFatRate();
            if (bodyFatRate < 10) {
                result.append("体脂率偏低;");
            } else if (bodyFatRate >= 10 && bodyFatRate < 20) {
                result.append("体脂率正常;");
            } else if (bodyFatRate >= 20 && bodyFatRate < 25) {
                result.append("体脂率偏高;");
            } else {
                result.append("体脂率过高;");
            }
        }

        if (assessment.getVisceralFatLevel() != null && assessment.getVisceralFatLevel() > 10) {
            result.append("内脏脂肪偏高;");
        }

        assessment.setAssessmentResult(result.toString());
    }
}
