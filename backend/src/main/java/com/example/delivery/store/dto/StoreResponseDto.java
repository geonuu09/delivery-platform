package com.example.delivery.store.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.review.entity.Review;
import com.example.delivery.category.entity.Category;
import com.example.delivery.store.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StoreResponseDto {

    @Schema(description = "가게 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID storeId;

    @Schema(description = "가게 이름", example = "맛있는 김밥집")
    private String storeName;

    @Schema(description = "사업자 이름", example = "홍길동")
    private String storeOwnerName;

    @Schema(description = "가게 위치", example = "서울특별시 강남구")
    private String storeLocation;

    @Schema(description = "영업 상태", example = "true")
    private boolean isOpened;

    @Schema(description = "카테고리 이름", example = "한식")
    private String categoryName;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.storeOwnerName = store.getStoreOwnerName();
        this.storeLocation = store.getStoreLocation();
        this.isOpened = store.isOpened();
        this.categoryName = store.getCategory().getCategoryName();
    }
}
