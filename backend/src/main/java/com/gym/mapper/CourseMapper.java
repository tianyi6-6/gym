package com.gym.mapper;

import com.gym.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> findAll();
    Course findById(@Param("id") Integer id);
    List<Course> findByCoachId(@Param("coachId") Integer coachId);
    List<Course> findByPage(@Param("offset") int offset, @Param("size") int size);
    long count();
    int insert(Course course);
    int update(Course course);
    int delete(@Param("id") Integer id);
}

