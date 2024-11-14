package com.example.delivery.category.dto;

import com.example.delivery.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryRequestDto {

    private UUID categoryId;

    @NotBlank(message = "카테고리 선택은 필수입니다.")
    private String categoryName;

    private boolean deleted;

    public Category toEntity() {
        return Category.builder()
                .categoryName(this.categoryName)
                .build();
    }
}
