package com.example.delivery.store.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.review.entity.Review;
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
public class GetStoreDetailsResponseDto {
    @Schema(description = "가게 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID storeId;

    @Schema(description = "가게 이름", example = "맛있는 김밥집")
    private String storeName;

    @Schema(description = "사업자 이름", example = "홍길동")
    private String storeOwnerName;

    @Schema(description = "가게 위치", example = "서울시 강남구")
    private String storeLocation;

    @Schema(description = "영업 여부", example = "true")
    private boolean opened;

    @Schema(description = "카테고리 이름", example = "한식")
    private String categoryName;

    @Schema(description = "평균 평점", example = "4.5")
    private double averageRating;

    @Schema(description = "리뷰 개수", example = "25")
    private int reviewCount;

    @Schema(description = "필터링된 메뉴 목록")
    private Page<MenuDto> filteredMenus;

    @Schema(description = "리뷰 목록")
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

        @Schema(description = "메뉴 이름", example = "김치볶음밥")
        private String menuName;

        @Schema(description = "메뉴 가격", example = "10000")
        private int menuPrice;

        @Schema(description = "메뉴 이미지 URL", example = "http://example.com/images/menu1.png")
        private String menuImage;

        @Schema(description = "메뉴 옵션 리스트")
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

        @Schema(description = "옵션 이름", example = "치즈 추가")
        private String optionName;

        @Schema(description = "옵션 가격", example = "500")
        private int optionPrice;

        public MenuOptionDto(MenuOption option) {
            this.optionName = option.getOptionName();
            this.optionPrice = option.getOptionPrice();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewDto {

        @Schema(description = "리뷰 작성자 이름", example = "오바마")
        private String reviewId;

        @Schema(description = "리뷰 내용", example = "너무 맛있어요!")
        private String content;

        @Schema(description = "별점", example = "5")
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
