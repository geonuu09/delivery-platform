package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class MenuResponseDto {

    private UUID menuId;
    private String menuName;
    private int menuPrice;
    private String menuDescription;
    private String menuImage;
    private List<MenuOption> menuOptions;

    public MenuResponseDto(Menu menu) {
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.menuPrice = menu.getMenuPrice();
        this.menuDescription = menu.getMenuDescription();
        this.menuImage = menu.getMenuImage();
        this.menuOptions = menu.getMenuOptions();
    }
}