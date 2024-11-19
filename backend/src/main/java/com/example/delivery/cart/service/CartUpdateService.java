package com.example.delivery.cart.service;

import com.example.delivery.cart.dto.request.CartUpdateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.entity.Cart;
import com.example.delivery.cart.repository.CartRepository;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.menu.repository.MenuOptionRepository;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.user.entity.UserRoleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartUpdateService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;

    // 장바구니 수정
    @Transactional
    public CartResponseDto updateCart(Long userId, UserRoleEnum userRole, CartUpdateRequestDto requestDto) {
        // 해당 장바구니
        Cart cart = cartRepository.findById(requestDto.getCartId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CART));

        // 고객과 점주 : 자신의 장바구니만 수정 가능
        if (userRole == UserRoleEnum.CUSTOMER || userRole == UserRoleEnum.OWNER) {

            // 접근제한 : 주문자인지 확인
            if (!userId.equals(cart.getUser().getUserId())) {
                throw new CustomException(ErrorCode.CART_PERMISSION_DENIED);
            }

            // 장바구니 상태 체크 : 대기상태만 수정 가능
            if (cart.getCartStatus() != Cart.CartStatus.PENDING) {
                throw new CustomException(ErrorCode.CART_MODIFICATION_NOT_ALLOWED);
            }
        }

        // 해당 메뉴
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MENU));

        // 메뉴 수량 : 0 이상
        if (requestDto.getMenuCount() <= 0) {
            throw new CustomException(ErrorCode.INVALID_MENU_COUNT);
        }
        cart.setCount(requestDto.getMenuCount());

        // 메뉴 옵션
        List<MenuOption> updatedMenuOptions = new ArrayList<>();

        if (requestDto.getMenuOptionIdList() != null && !requestDto.getMenuOptionIdList().isEmpty()) {
            for (UUID menuOptionId : requestDto.getMenuOptionIdList()) {
                MenuOption menuOption = menuOptionRepository.findById(menuOptionId)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MENU_OPTION));
                updatedMenuOptions.add(menuOption);
            }
        }
        cart.setMenuOptions(updatedMenuOptions);

        // 가격
        int menuPrice = menu.getMenuPrice() * cart.getCount();
        for (MenuOption option : updatedMenuOptions) {
            menuPrice += option.getOptionPrice();
        }
        cart.setPrice(menuPrice);
        return CartResponseDto.from(cart);
    }
}
