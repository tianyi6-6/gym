package com.gym.mapper;

import com.gym.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findByPhone(@Param("phone") String phone);
    User findById(@Param("id") Integer id);
    List<User> findAll();
    int insert(User user);
    int update(User user);
    int updateBalance(@Param("id") Integer id, @Param("amount") BigDecimal amount);
    int delete(@Param("id") Integer id);
}

