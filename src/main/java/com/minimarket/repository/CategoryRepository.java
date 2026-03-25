package com.minimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.minimarket.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}