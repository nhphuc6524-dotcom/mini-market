package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

import com.minimarket.model.Category;
import com.minimarket.repository.CategoryRepository;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo){
        this.repo = repo;
    }

    @GetMapping
    public List<Category> getAll(){
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable Integer id){
        // đảm bảo id không null
        Objects.requireNonNull(id, "Category ID must not be null");

        // trả về category hoặc ném exception nếu không tồn tại
        return repo.findById(id)
                   .orElseThrow(() -> new RuntimeException("Category not found: " + id));
    }

    @PostMapping
    public Category create(@RequestBody Category c){
        Objects.requireNonNull(c, "Category must not be null");
        return repo.save(c);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Integer id,
                           @RequestBody Category c){
        Objects.requireNonNull(id, "Category ID must not be null");
        Objects.requireNonNull(c, "Category must not be null");

        // đảm bảo tồn tại trước khi update
        Category existing = repo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Category not found: " + id));

        existing.setName(c.getName());
        // có thể copy thêm các trường khác nếu cần
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        Objects.requireNonNull(id, "Category ID must not be null");
        repo.deleteById(id);
    }
}