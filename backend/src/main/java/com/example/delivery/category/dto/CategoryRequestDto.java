package com.example.delivery.category.dto;

import com.example.delivery.category.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryRequestDto {

    @Schema(description = "카테고리 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID categoryId;

    @NotBlank(message = "카테고리 선택은 필수입니다.")
    @Schema(description = "카테고리 이름", example = "음식")
    private String categoryName;

    @Schema(description = "삭제 여부", example = "false")
    private boolean deleted;

    public Category toEntity() {
        return Category.builder()
                .categoryName(this.categoryName)
                .build();
    }
}
