package com.example.delivery.store.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.review.entity.Review;
import com.example.delivery.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetStoresResponseDto {
    private UUID storeId;
    private String storeName;
    private double averageRating;
    private int reviewCount;
    private List<SimpleMenuDto> menus;
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
        private String menuName;
        private int menuPrice;
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
