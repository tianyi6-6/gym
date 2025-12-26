package com.gym.mapper;

import com.gym.entity.CourseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseOrderMapper {
  List<CourseOrder> findAll();

  CourseOrder findById(@Param("id") Integer id);

  List<CourseOrder> findByUserId(@Param("userId") Integer userId);

  int insert(CourseOrder order);

  int update(CourseOrder order);

  int delete(@Param("id") Integer id);
}
