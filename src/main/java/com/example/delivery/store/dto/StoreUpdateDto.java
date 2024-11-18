package com.example.delivery.store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreUpdateDto {

    private String storeName;

    private String storeOwnerName;

    private String storeLocation;

    private boolean opened;

    private String categoryName;

}
