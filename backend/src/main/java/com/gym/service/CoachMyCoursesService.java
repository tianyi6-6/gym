package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.CoachMyCourses;
import com.gym.mapper.CoachMyCoursesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachMyCoursesService {
    @Autowired
    private CoachMyCoursesMapper coachMyCoursesMapper;

    public Result<List<CoachMyCourses>> findAll() {
        try {
            return Result.success(coachMyCoursesMapper.findAll());
        } catch (DataAccessException e) {
            return Result.error("获取我的授课列表失败: " + e.getMessage());
        }
    }

    public Result<List<CoachMyCourses>> findByCoachId(Integer coachId) {
        try {
            return Result.success(coachMyCoursesMapper.findByCoachId(coachId));
        } catch (DataAccessException e) {
            return Result.error("获取教练授课信息失败: " + e.getMessage());
        }
    }

    public Result<CoachMyCourses> findById(Integer id) {
        try {
            CoachMyCourses course = coachMyCoursesMapper.findById(id);
            return Result.success(course);
        } catch (DataAccessException e) {
            return Result.error("获取授课详情失败: " + e.getMessage());
        }
    }

    public Result<Void> save(CoachMyCourses coachMyCourses) {
        try {
            if (coachMyCourses.getId() == null) {
                coachMyCoursesMapper.insert(coachMyCourses);
            } else {
                coachMyCoursesMapper.update(coachMyCourses);
            }
            return Result.success("保存成功", null);
        } catch (DataAccessException e) {
            return Result.error("保存授课信息失败: " + e.getMessage());
        }
    }

    public Result<Void> delete(Integer id) {
        try {
            coachMyCoursesMapper.delete(id);
            return Result.success("删除成功", null);
        } catch (DataAccessException e) {
            return Result.error("删除授课信息失败: " + e.getMessage());
        }
    }

    public Result<Void> deleteByCoachId(Integer coachId) {
        try {
            coachMyCoursesMapper.deleteByCoachId(coachId);
            return Result.success("批量删除成功", null);
        } catch (DataAccessException e) {
            return Result.error("批量删除授课信息失败: " + e.getMessage());
        }
    }
}
