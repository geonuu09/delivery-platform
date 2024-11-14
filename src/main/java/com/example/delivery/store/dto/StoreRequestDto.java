package com.example.delivery.store.dto;

import com.example.delivery.category.entity.Category;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class StoreRequestDto {

    private UUID storeId;

    @NotNull(message = "사용자 정보는 필수입니다.")
    private Long userId;

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String storeName;

    @NotBlank(message = "사업자 이름은 필수입니다.")
    private String storeOwnerName;

    @NotBlank(message = "가게 위치는 필수입니다.")
    private String storeLocation;

    @NotNull(message = "영업 상태는 필수입니다.")
    private boolean opened;

    private boolean deleted;

    @NotNull(message = "카테고리는 필수입니다.")
    private UUID categoryId;


    public Store toEntity(User user,Category category) {
        return Store.builder()
                .storeName(this.storeName)
                .storeOwnerName(this.storeOwnerName)
                .storeLocation(this.storeLocation)
                .opened(this.opened)
                .deleted(this.deleted)
                .user(user)
                .category(category)
                .build();
    }

}
