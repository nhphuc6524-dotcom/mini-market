package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;
import com.minimarket.repository.CategoryRepository;
import com.minimarket.model.Category;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> getAll(){
        return categoryRepository.findAll();
    }
}