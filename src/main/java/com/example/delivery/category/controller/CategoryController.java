package com.example.delivery.category.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.category.dto.CategoryRequestDto;
import com.example.delivery.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 가게 카테고리 등록
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.createCategory(categoryRequestDto);
        return ResponseEntity.ok("카테고리가 성공적으로 등록되었습니다.");
    }

    // 가게 카테고리 수정
    @PutMapping("{categoryId}/update")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<String> updateCategory(@PathVariable UUID categoryId,@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        categoryRequestDto.setCategoryId(categoryId);
        categoryService.updateCategory(categoryRequestDto);
        return ResponseEntity.ok("카테고리가 성공적으로 수정되었습니다.");
    }

    // 가게 카테고리 삭제
    @DeleteMapping("{categoryId}/delete")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID categoryId, @Valid @RequestBody CategoryRequestDto categoryRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        categoryRequestDto.setCategoryId(categoryId);
        categoryService.deleteCategory(categoryRequestDto, userDetails.getUsername());
        return ResponseEntity.ok("카테고리가 성공적으로 삭제되었습니다.");
    }
}
