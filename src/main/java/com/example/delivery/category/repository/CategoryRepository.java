package com.example.delivery.category.repository;

import com.example.delivery.category.entity.Category;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByCategoryNameAndDeletedFalse(String categoryName);

    Optional<Category> findByCategoryIdAndDeletedFalse(@NotNull(message = "카테고리는 필수입니다.") UUID categoryId);
}
