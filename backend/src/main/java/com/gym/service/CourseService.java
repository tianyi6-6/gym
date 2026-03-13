package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Course;
import com.gym.mapper.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    
    @Autowired
    private CourseMapper courseMapper;

    public Result<List<Course>> findAll() {
        logger.info("查询所有课程");
        return Result.success(courseMapper.findAll());
    }

    public Result<Course> findById(Integer id) {
        logger.info("根据ID查询课程: {}", id);
        return Result.success(courseMapper.findById(id));
    }

    public Result<List<Course>> findByCoachId(Integer coachId) {
        logger.info("根据教练ID查询课程: {}", coachId);
        return Result.success(courseMapper.findByCoachId(coachId));
    }

    public Result<List<Course>> findByPage(int page, int size) {
        logger.info("分页查询课程: 第{}页，每页{}条", page, size);
        int offset = (page - 1) * size;
        return Result.success(courseMapper.findByPage(offset, size));
    }

    public Result<Long> count() {
        return Result.success(courseMapper.count());
    }

    public Result<Void> save(Course course) {
        if (course.getId() == null) {
            logger.info("新增课程: {}", course.getName());
            courseMapper.insert(course);
        } else {
            logger.info("更新课程: {}", course.getName());
            courseMapper.update(course);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        logger.info("删除课程: {}", id);
        courseMapper.delete(id);
        return Result.success(null);
    }
}

