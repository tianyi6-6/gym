package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.Recipe;
import com.gym.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@CrossOrigin
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/list")
    public Result<List<Recipe>> list() {
        return recipeService.findAll();
    }

    @GetMapping("/{id}")
    public Result<Recipe> getById(@PathVariable Integer id) {
        return recipeService.findById(id);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Recipe recipe) {
        return recipeService.save(recipe);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return recipeService.delete(id);
    }
}

