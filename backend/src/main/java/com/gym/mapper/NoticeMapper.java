package com.gym.mapper;

import com.gym.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NoticeMapper {
    List<Notice> findAll();
    Notice findById(@Param("id") Integer id);
    int insert(Notice notice);
    int update(Notice notice);
    int delete(@Param("id") Integer id);
}

