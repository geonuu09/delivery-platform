package com.example.delivery.store.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.review.entity.Review;
import com.example.delivery.store.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetStoresResponseDto {

    @Schema(description = "가게 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID storeId;

    @Schema(description = "가게 이름", example = "맛있는 김밥집")
    private String storeName;

    @Schema(description = "평균 평점", example = "4.5")
    private double averageRating;

    @Schema(description = "리뷰 개수", example = "25")
    private int reviewCount;

    @Schema(description = "상점의 간단한 메뉴 리스트 (최대 5개)")
    private List<SimpleMenuDto> menus;

    @Schema(description = "카테고리 이름", example = "한식")
    private String categoryName;

    public GetStoresResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.averageRating = calculateAverageRating(store);
        this.reviewCount = store.getReviews().size();
        this.menus = store.getMenus().stream()
                .limit(5)
                .map(SimpleMenuDto::new)
                .collect(Collectors.toList());
        this.categoryName = store.getCategory().getCategoryName();
    }

    @Getter
    @NoArgsConstructor
    public static class SimpleMenuDto {

        @Schema(description = "메뉴 이름", example = "김치볶음밥")
        private String menuName;

        @Schema(description = "메뉴 가격", example = "10000")
        private int menuPrice;

        @Schema(description = "메뉴 이미지 URL", example = "http://example.com/images/menu1.png")
        private String menuImage;

        public SimpleMenuDto(Menu menu) {
            this.menuName = menu.getMenuName();
            this.menuPrice = menu.getMenuPrice();
            this.menuImage = menu.getMenuImage();
        }
    }

    private static double calculateAverageRating(Store store) {
        return store.getReviews().stream()
                .mapToInt(Review::getStarRating)
                .average()
                .orElse(0.0);
    }
}
