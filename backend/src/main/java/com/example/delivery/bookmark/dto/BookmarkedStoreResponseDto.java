package com.example.delivery.bookmark.dto;

import com.example.delivery.store.entity.Store;
import java.util.UUID;
import lombok.Getter;

@Getter
public class  BookmarkedStoreResponseDto{
    private UUID storeId;
    private String storeName;

    public BookmarkedStoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
    }
}