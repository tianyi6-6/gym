package com.gym.mapper;

import com.gym.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RecipeMapper {
    List<Recipe> findAll();
    Recipe findById(@Param("id") Integer id);
    int insert(Recipe recipe);
    int update(Recipe recipe);
    int delete(@Param("id") Integer id);
}

