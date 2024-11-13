package com.example.delivery.store.repository;

import com.example.delivery.store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByCategoryNameAndDeletedFalse(String categoryName);

    //테스트용
    Optional<Category> findByCategoryIdAndDeletedTrue(UUID categoryId);
    Optional<Category> findByCategoryIdAndDeletedFalse(UUID CategoryName);
}
