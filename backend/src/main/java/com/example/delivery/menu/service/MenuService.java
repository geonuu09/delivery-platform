package com.example.delivery.menu.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.menu.dto.MenuOptionRequestDto;
import com.example.delivery.menu.dto.MenuOptionResponseDto;
import com.example.delivery.menu.dto.MenuRequestDto;
import com.example.delivery.menu.dto.MenuResponseDto;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.menu.repository.MenuOptionRepository;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.store.entity.Store;
import com.example.delivery.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final StoreRepository storeRepository;

    // 메뉴 등록
    @Transactional
    public MenuResponseDto createMenu(UUID storeId, MenuRequestDto menuRequestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Menu menu = menuRequestDto.toEntity(store);
        menu = menuRepository.save(menu);

        return new MenuResponseDto(menu);
    }

    // 메뉴 상세 조회
    @Transactional(readOnly = true)
    public MenuResponseDto getMenuDetails(UUID storeId, UUID menuId) {

        Menu menu = menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        return new MenuResponseDto(menu);
    }

    // 메뉴 수정
    @Transactional
    public void updateMenu(UUID storeId, UUID menuId, MenuRequestDto menuRequestDto) {

        Menu menu = menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

            menu.update(menuRequestDto);

    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(UUID storeId, UUID menuId, String username) {
        Menu menu = menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        menu.delete(username);

    }

    // 메뉴 옵션 등록
    @Transactional
    public MenuOptionResponseDto createMenuOption(UUID storeId, UUID menuId, MenuOptionRequestDto menuOptionRequestDto) {

        Menu menu = menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        MenuOption menuOption = menuOptionRequestDto.toEntity(menu);

        menu.getMenuOptions().add(menuOption);

        menuOptionRepository.save(menuOption);
        menuRepository.save(menu);

        return new MenuOptionResponseDto(menuOption);

    }

    // 메뉴 옵션 수정
    @Transactional
    public void updateMenuOption(UUID storeId, UUID menuId, MenuOptionRequestDto menuOptionRequestDto) {

        menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        UUID menuOptionId = menuOptionRequestDto.getMenuOptionId();

        MenuOption menuOption = menuOptionRepository.findByMenuOptionIdAndDeletedFalse(menuOptionId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

            menuOption.update(menuOptionRequestDto);
    }

    // 메뉴 옵션 삭제
    @Transactional
    public void deleteMenuOption(UUID storeId, UUID menuId, UUID menuOptionId, String username) {

        menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        MenuOption menuOption = menuOptionRepository.findByMenuOptionIdAndDeletedFalse(menuOptionId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

        menuOption.delete(username);

    }

}
