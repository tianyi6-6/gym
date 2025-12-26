package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Course;
import com.gym.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseMapper courseMapper;

    public Result<List<Course>> findAll() {
        return Result.success(courseMapper.findAll());
    }

    public Result<Course> findById(Integer id) {
        return Result.success(courseMapper.findById(id));
    }

    public Result<List<Course>> findByCoachId(Integer coachId) {
        return Result.success(courseMapper.findByCoachId(coachId));
    }

    public Result<Void> save(Course course) {
        if (course.getId() == null) {
            courseMapper.insert(course);
        } else {
            courseMapper.update(course);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        courseMapper.delete(id);
        return Result.success(null);
    }
}

