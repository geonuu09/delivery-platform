package com.example.delivery.store.service;

import com.example.delivery.category.entity.Category;
import com.example.delivery.category.repository.CategoryRepository;
import com.example.delivery.common.Util.PagingUtil;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.store.dto.GetStoreDetailsResponseDto;
import com.example.delivery.store.dto.GetStoresResponseDto;
import com.example.delivery.store.dto.StoreRequestDto;
import com.example.delivery.store.dto.StoreResponseDto;
import com.example.delivery.store.entity.Store;
import com.example.delivery.store.repository.StoreRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
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

        Store store = storeRequestDto.toEntity(user, category);
        store = storeRepository.save(store);

        return new StoreResponseDto(store);
    }

    //가게 전체 페이지(키워드 및 카테고리 검색)
    @Transactional(readOnly = true)
    public Page<GetStoresResponseDto> getStores(int page, int size, String sortBy, boolean isAsc, String keyword, String categoryName) {

        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);
        Page<Store> stores;


        // 카테고리 이름과 키워드 모두 있는 경우
        if (categoryName != null && keyword != null) {
            Category category = categoryRepository.findByCategoryNameAndDeletedFalse(categoryName)
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
            stores = storeRepository.findStoresByCategoryAndKeyword(category.getCategoryId(), keyword, pageable);

            // 카테고리 이름만 있는 경우
        } else if (categoryName != null) {
            Category category = categoryRepository.findByCategoryNameAndDeletedFalse(categoryName)
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
            stores = storeRepository.findByCategory_CategoryIdAndDeletedFalse(category.getCategoryId(), pageable);

            // 키워드만 있는 경우
        } else if (keyword != null) {
            stores = storeRepository.findStoresByKeyword(keyword, pageable);

            // 검색 조건이 없는 경우
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "검색 조건이 필요합니다.");
        }

        // storesList 반환
        return stores.map(GetStoresResponseDto::new);
    }

    // 가게 상세 조회
    @Transactional(readOnly = true)
    public GetStoreDetailsResponseDto getStoreDetails(UUID storeId, User user, int page, int size, String sortBy, boolean isAsc, String keyword) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        Page<Menu> filteredMenus;

        boolean isOwnerOrAdmin = store.getUser().getUserId().equals(user.getUserId()) ||
                user.getRole() == UserRoleEnum.MANAGER ||
                user.getRole() == UserRoleEnum.MASTER;

        //키워드가 없을 때
        if (keyword == null || keyword.isEmpty()) {
            filteredMenus = isOwnerOrAdmin
                    ? menuRepository.findByStore_StoreIdAndDeletedFalse(storeId, pageable)
                    : menuRepository.findByStore_StoreIdAndDeletedFalseAndHiddenFalse(storeId, pageable);
        } else {
            // 키워드 있을 때
            filteredMenus = menuRepository.findMenusByKeyword(storeId, keyword, pageable, isOwnerOrAdmin);
        }

        return new GetStoreDetailsResponseDto(store, filteredMenus);

    }

    // 가게 수정
    @Transactional
    public void updateStore(StoreRequestDto storeRequestDto, UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        User user = userRepository.findById(storeRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        Category category = categoryRepository.findById(storeRequestDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        store.update(storeRequestDto, category);

    }

    // 가게 삭제
    @Transactional
    public void deleteStore(StoreRequestDto storeRequestDto, String username) {
        UUID storeId = storeRequestDto.getStoreId();
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        store.delete(username);

    }
}
