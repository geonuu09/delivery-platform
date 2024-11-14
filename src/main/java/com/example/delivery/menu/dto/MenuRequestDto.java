package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
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

    private List<MenuOption> menuOptions;



    public Menu toEntity(Store store) {

        if (this.menuOptions == null) {
            this.menuOptions = new ArrayList<>();
        }

        return Menu.builder()
                .menuName(this.menuName)
                .menuPrice(this.menuPrice)
                .menuImage(this.menuImage)
                .menuDescription(this.menuDescription)
                .menuOptions(this.menuOptions)
                .deleted(this.deleted)
                .hidden(this.hidden)
                .store(store)
                .build();
    }

}
