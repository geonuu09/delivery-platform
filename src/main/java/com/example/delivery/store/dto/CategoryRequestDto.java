package com.example.delivery.store.dto;

import com.example.delivery.store.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CategoryRequestDto {

    private UUID categoryId;

    @NotBlank(message = "카테고리 선택은 필수입니다.")
    private String categoryName;

    private boolean deleted;

    public Category toEntity() {
        return Category.builder()
                .categoryId(this.categoryId != null ? this.categoryId : UUID.randomUUID())
                .categoryName(this.categoryName)
                .build();
    }
}
