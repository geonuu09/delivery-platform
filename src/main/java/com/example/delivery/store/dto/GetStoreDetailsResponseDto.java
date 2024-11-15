package com.example.delivery.store.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.review.entity.Review;
import com.example.delivery.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetStoreDetailsResponseDto {
    private UUID storeId;
    private String storeName;
    private String storeOwnerName;
    private String storeLocation;
    private boolean opened;
    private String categoryName;
    private double averageRating;
    private int reviewCount;
    private Page<MenuDto> filteredMenus;
    private List<ReviewDto> reviews;

    public GetStoreDetailsResponseDto(Store store, Page<Menu> filteredMenus) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.storeOwnerName = store.getStoreOwnerName();
        this.storeLocation = store.getStoreLocation();
        this.opened = store.isOpened();
        this.categoryName = store.getCategory().getCategoryName();
        this.averageRating = calculateAverageRating(store);
        this.reviewCount = store.getReviews().size();
        this.filteredMenus = filteredMenus.map(MenuDto::new);
        this.reviews = store.getReviews().stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    @NoArgsConstructor
    public static class MenuDto {
        private String menuName;
        private int menuPrice;
        private String menuImage;
        private List<MenuOptionDto> menuOptionList;

        public MenuDto(Menu menu) {
            this.menuName = menu.getMenuName();
            this.menuPrice = menu.getMenuPrice();
            this.menuImage = menu.getMenuImage();
            this.menuOptionList = menu.getMenuOptions().stream()
                    .map(MenuOptionDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MenuOptionDto {
        private String optionName;
        private int optionPrice;

        public MenuOptionDto(MenuOption option) {
            this.optionName = option.getOptionName();
            this.optionPrice = option.getOptionPrice();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewDto {
        private String reviewId;
        private String content;
        private int starRating;

        public ReviewDto(Review review) {
            this.reviewId = review.getUser().getUserName();
            this.content = review.getContent();
            this.starRating = review.getStarRating();
        }
    }

    private static double calculateAverageRating(Store store) {
        return store.getReviews().stream()
                .mapToInt(Review::getStarRating)
                .average()
                .orElse(0.0);
    }
}
