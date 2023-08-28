package com.btk.ordercorner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btk.ordercorner.model.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByCategoryNameIgnoreCase(String categoryName);

    Optional<Category> findByCategoryName(String categoryName);

}
