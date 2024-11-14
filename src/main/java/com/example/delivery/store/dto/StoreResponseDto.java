package com.example.delivery.store.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.review.entity.Review;
import com.example.delivery.category.entity.Category;
import com.example.delivery.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {

    private UUID storeId;
    private String storeName;
    private String storeOwnerName;
    private String storeLocation;
    private boolean isOpened;
    private Category category;

    private List<Menu> menus;
    private List<Review> reviews;

    private int reviewCount;
    private double averageRating;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.storeOwnerName = store.getStoreOwnerName();
        this.storeLocation = store.getStoreLocation();
        this.isOpened = store.isOpened();
        this.category = store.getCategory();
    }

    private static double calculateAverageRating(Store store) {
        return store.getReviews().stream()
                .mapToInt(Review::getStarRating)
                .average()
                .orElse(0.0);
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetStoresResponseDto {
        private UUID storeId;
        private String storeName;
        private double averageRating;
        private int reviewCount;
        private List<Menu> menus;
        private Category category;

        public GetStoresResponseDto(Store store) {
            this.storeId = store.getStoreId();
            this.storeName = store.getStoreName();
            this.averageRating = calculateAverageRating(store);
            this.reviewCount = store.getReviews().size();
            this.menus = store.getMenus().stream()
                    .filter(menu -> !menu.isHidden()&& !menu.isDeleted())
                    .limit(5)
                    .collect(Collectors.toList());
            this.category = store.getCategory();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetStoreDetailsResponseDto {
        private UUID storeId;
        private String storeName;
        private String storeOwnerName;
        private String storeLocation;
        private boolean opened;
        private Category category;
        private double averageRating;
        private int reviewCount;
        private Page<Menu> filteredMenus;
        private List<Review> reviews;

        public GetStoreDetailsResponseDto(Store store, Page<Menu> filteredMenus) {
            this.storeId = store.getStoreId();
            this.storeName = store.getStoreName();
            this.storeOwnerName = store.getStoreOwnerName();
            this.storeLocation = store.getStoreLocation();
            this.opened = store.isOpened();
            this.category = store.getCategory();
            this.averageRating = calculateAverageRating(store);
            this.reviewCount = store.getReviews().size();
            this.filteredMenus = filteredMenus;
            this.reviews = store.getReviews();
        }
    }
}