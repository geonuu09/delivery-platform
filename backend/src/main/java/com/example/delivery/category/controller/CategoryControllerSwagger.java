package com.example.delivery.category.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.category.dto.CategoryRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Category API", description = "카테고리 관리 API")
public interface CategoryControllerSwagger {

    @Operation(
            summary = "카테고리 등록",
            description = "새로운 카테고리를 등록합니다."
    )
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    ResponseEntity<String> createCategory(
            @Valid @RequestBody CategoryRequestDto categoryRequestDto
    );

    @Operation(
            summary = "카테고리 수정",
            description = "특정 카테고리를 수정합니다."
    )
    @PutMapping("/{categoryId}/update")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    ResponseEntity<String> updateCategory(
            @Parameter(description = "카테고리 ID") @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto
    );

    @Operation(
            summary = "카테고리 삭제",
            description = "특정 카테고리를 삭제합니다."
    )
    @DeleteMapping("/{categoryId}/delete")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    ResponseEntity<String> deleteCategory(
            @Parameter(description = "삭제할 카테고리 ID") @PathVariable UUID categoryId,
            @RequestBody CategoryRequestDto categoryRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );
}