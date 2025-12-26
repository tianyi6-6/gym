package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Recipe;
import com.gym.mapper.RecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipeMapper recipeMapper;

    public Result<List<Recipe>> findAll() {
        return Result.success(recipeMapper.findAll());
    }

    public Result<Recipe> findById(Integer id) {
        return Result.success(recipeMapper.findById(id));
    }

    public Result<Void> save(Recipe recipe) {
        if (recipe.getId() == null) {
            recipeMapper.insert(recipe);
        } else {
            recipeMapper.update(recipe);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        recipeMapper.delete(id);
        return Result.success(null);
    }
}

