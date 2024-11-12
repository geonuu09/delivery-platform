package com.example.delivery.cart.service;

import com.example.delivery.cart.dto.request.CartCreateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.entity.Cart;
import com.example.delivery.cart.repository.CartRepository;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.store.repository.StoreRepository;
import com.example.delivery.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final MenuRepository menuOptionRepository;

    // 장바구니 생성
    @Transactional
    public CartResponseDto createCart(User user, CartCreateRequestDto requestDto) {
        UUID storeId = requestDto.getStoreId();

        // 담은 메뉴
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MENU));

        // 메뉴 활성화 여부 확인
        if (menu.getIsDeleted() || menu.getIsHidden()) {
            throw new CustomException(ErrorCode.UNAVAILABLE_MENU);
        }

        // 선택 메뉴 옵션
        List<MenuOption> menuOptions = menuOptionRepository.findByMenuId(requestDto.getMenuId());

        // 메뉴 가격 산출
        int menuPrice = menu.getMenuPrice();
        for (MenuOption option : menuOptions) {
            menuPrice += option.getOptionPrice();
        }

        Cart cart = requestDto.toEntity(user, menu, menuOptions);
        cartRepository.save(cart);
        return CartResponseDto.from(cart, storeId, menuPrice);
    }

    // 장바구니 조회
    // 장바구니 수정
    // 장바구니 삭제
}
