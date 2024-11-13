package com.example.delivery.store.service;

import com.example.delivery.common.Util.PagingUtil;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.store.dto.CategoryRequestDto;
import com.example.delivery.store.dto.StoreRequestDto;
import com.example.delivery.store.dto.StoreResponseDto;
import com.example.delivery.store.entity.Category;
import com.example.delivery.store.entity.Store;
import com.example.delivery.store.repository.CategoryRepository;
import com.example.delivery.store.repository.StoreRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    // 가게 등록
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto storeRequestDto) {
        User user = userRepository.findById(storeRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (storeRepository.existsByStoreNameAndDeletedFalse(storeRequestDto.getStoreName())) {
            throw new CustomException(ErrorCode.STORE_ALREADY_EXISTS);
        }

        Category category = categoryRepository.findById(storeRequestDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Store store = storeRequestDto.toEntity(user ,category);
        store = storeRepository.save(store);

        return new StoreResponseDto(store);
    }

    //가게 전체 페이지(키워드 및 카테고리 검색)
    @Transactional(readOnly = true)
    public Page<StoreResponseDto.GetStoresResponseDto> getStores(int page, int size, String sortBy, boolean isAsc, String keyword, String categoryName) {

        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        Page<Store> stores;

        // 카테고리 이름으로 검색
        if (categoryName != null) {
            Category category = categoryRepository.findByCategoryNameAndDeletedFalse(categoryName)
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

            stores = storeRepository.findByCategory_CategoryIdAndDeletedFalse(category.getCategoryId(), pageable);
            // 키워드로 검색
//        } else if (keyword != null) {
//
//            //query dsl 적용해야함
//            //stores = storeRepository.findStoresByKeyword(keyword, pageable);
//

            // 검색 조건이 없는 경우
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "검색 조건이 필요합니다.");
        }

        // storesList 반환
        return stores.map(StoreResponseDto.GetStoresResponseDto::new);
    }

    // 가게 상세 조회
    @Transactional(readOnly = true)
    public StoreResponseDto.GetStoreDetailsResponseDto getStoreDetails(UUID storeId, int page, int size, String sortBy, boolean isAsc, String keyword) {

        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        Page<Menu> filteredMenus;

        if (keyword == null || keyword.isEmpty()) {
            // 키워드가 없을 때
            filteredMenus = menuRepository.findByStore_StoreIdAndDeletedFalseAndHiddenFalse(storeId, pageable);
        } else {
            // 키워드가 있을 때
            // querydsl 적용예정
            // filteredMenus = menuRepository.findMenusByStore_StoreIdAndKeyword(storeId, keyword, pageable);

            //임시
            filteredMenus = menuRepository.findByStore_StoreIdAndDeletedFalseAndHiddenFalse(storeId, pageable);
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        return new StoreResponseDto.GetStoreDetailsResponseDto(store, filteredMenus);
    }

    // 가게 수정
    @Transactional
    public void updateStore(StoreRequestDto storeRequestDto, UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Category category = categoryRepository.findById(storeRequestDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        try {
            store.update(storeRequestDto, category);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.STORE_UPDATE_FAILED);
        }
    }

    // 가게 삭제
    @Transactional
    public void deleteStore(UUID storeId, String username) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        try {
            store.delete(username);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.STORE_DELETE_FAILED);
        }
    }

    // 가게 카테고리 등록
    @Transactional
    public void createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryRequestDto.toEntity();
        categoryRepository.save(category);
    }

    // 가게 카테고리 수정
    @Transactional
    public void updateCategory(CategoryRequestDto categoryRequestDto) {
        UUID categoryId = categoryRequestDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        try {
            category.update(categoryRequestDto);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.CATEGORY_UPDATE_FAILED);
        }
    }

    // 가게 카테고리 삭제
    @Transactional
    public void deleteCategory(UUID categoryId, String username) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        try {
            category.delete(username);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.CATEGORY_DELETE_FAILED);
        }
    }

}
