package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.CoachCourseSchedule;
import com.gym.mapper.CoachCourseScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachCourseScheduleService {
    @Autowired
    private CoachCourseScheduleMapper coachCourseScheduleMapper;

    public Result<List<CoachCourseSchedule>> findAll() {
        try {
            return Result.success(coachCourseScheduleMapper.findAll());
        } catch (DataAccessException e) {
            return Result.error("获取课程安排列表失败: " + e.getMessage());
        }
    }

    public Result<List<CoachCourseSchedule>> findByCoachId(Integer coachId) {
        try {
            return Result.success(coachCourseScheduleMapper.findByCoachId(coachId));
        } catch (DataAccessException e) {
            return Result.error("获取教练课程安排失败: " + e.getMessage());
        }
    }

    public Result<CoachCourseSchedule> findById(Integer id) {
        try {
            CoachCourseSchedule schedule = coachCourseScheduleMapper.findById(id);
            return Result.success(schedule);
        } catch (DataAccessException e) {
            return Result.error("获取课程安排详情失败: " + e.getMessage());
        }
    }

    public Result<Void> save(CoachCourseSchedule coachCourseSchedule) {
        try {
            if (coachCourseSchedule.getId() == null) {
                coachCourseScheduleMapper.insert(coachCourseSchedule);
            } else {
                coachCourseScheduleMapper.update(coachCourseSchedule);
            }
            return Result.success("保存成功", null);
        } catch (DataAccessException e) {
            return Result.error("保存课程安排失败: " + e.getMessage());
        }
    }

    public Result<Void> delete(Integer id) {
        try {
            coachCourseScheduleMapper.delete(id);
            return Result.success("删除成功", null);
        } catch (DataAccessException e) {
            return Result.error("删除课程安排失败: " + e.getMessage());
        }
    }

    public Result<Void> deleteByCoachId(Integer coachId) {
        try {
            coachCourseScheduleMapper.deleteByCoachId(coachId);
            return Result.success("批量删除成功", null);
        } catch (DataAccessException e) {
            return Result.error("批量删除课程安排失败: " + e.getMessage());
        }
    }
}
