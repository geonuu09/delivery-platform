package com.example.delivery.store.dto;

import com.example.delivery.category.entity.Category;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class StoreRequestDto {

    @Schema(description = "가게 ID ", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID storeId;

    @NotNull(message = "사용자 ID는 필수입니다.")
    @Schema(description = "가게 소유자 ID", example = "1", required = true)
    private Long userId;

    @NotBlank(message = "가게 이름은 필수입니다.")
    @Schema(description = "가게 이름", example = "맛있는 김밥집", required = true)
    private String storeName;

    @NotBlank(message = "사업자 이름은 필수입니다.")
    @Schema(description = "사업자 이름", example = "홍길동", required = true)
    private String storeOwnerName;

    @NotBlank(message = "가게 위치는 필수입니다.")
    @Schema(description = "가게 위치", example = "서울특별시 강남구", required = true)
    private String storeLocation;

    @NotNull(message = "영업 상태는 필수입니다.")
    @Schema(description = "영업 상태 (true: 영업 중, false: 영업 종료)", example = "true", required = true)
    private boolean opened;

    @Schema(description = "삭제 여부", example = "false")
    private boolean deleted;

    @NotNull(message = "카테고리는 필수입니다.")
    @Schema(description = "카테고리 ID", example = "123e4567-e89b-12d3-a456-426614174001", required = true)
    private UUID categoryId;


    public Store toEntity(User user,Category category) {
        return Store.builder()
                .storeName(this.storeName)
                .storeOwnerName(this.storeOwnerName)
                .storeLocation(this.storeLocation)
                .opened(this.opened)
                .user(user)
                .category(category)
                .build();
    }

}
