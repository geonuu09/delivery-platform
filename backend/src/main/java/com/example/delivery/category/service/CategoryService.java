package com.example.delivery.category.service;

import com.example.delivery.category.dto.CategoryRequestDto;
import com.example.delivery.category.entity.Category;
import com.example.delivery.category.repository.CategoryRepository;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 등록
    public void createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryRequestDto.toEntity();
        categoryRepository.save(category);
    }

    // 카테고리 수정
    @Transactional
    public void updateCategory(CategoryRequestDto categoryRequestDto) {

        Category category = categoryRepository.findByCategoryIdAndDeletedFalse(categoryRequestDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        category.update(categoryRequestDto);

    }

    // 카테고리 삭제
    @Transactional
    public void deleteCategory(CategoryRequestDto categoryRequestDto, String username) {

        Category category = categoryRepository.findByCategoryIdAndDeletedFalse(categoryRequestDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (category.getStores() != null && !category.getStores().isEmpty()) {
            throw new CustomException(ErrorCode.CATEGORY_DELETE_FAILED);
        }

        category.delete(username);

    }
}
