package com.example.delivery.store.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.review.entity.Review;
import com.example.delivery.category.entity.Category;
import com.example.delivery.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StoreResponseDto {

    private UUID storeId;
    private String storeName;
    private String storeOwnerName;
    private String storeLocation;
    private boolean isOpened;
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
