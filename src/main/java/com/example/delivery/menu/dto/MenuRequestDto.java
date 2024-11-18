package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MenuRequestDto {

    private UUID storeId;

    private UUID menuId;

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    private String menuName;

    @Positive(message = "메뉴 가격은 0보다 커야 합니다.")
    private int menuPrice;

    private boolean deleted;

    private boolean hidden;

    private String menuDescription;

    private String menuImage;

    public Menu toEntity(Store store) {

        return Menu.builder()
                .menuName(this.menuName)
                .menuPrice(this.menuPrice)
                .menuImage(this.menuImage)
                .menuDescription(this.menuDescription)
                .store(store)
                .build();
    }

}
