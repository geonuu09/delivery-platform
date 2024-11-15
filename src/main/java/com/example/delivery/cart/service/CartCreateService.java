package com.example.delivery.cart.service;

import com.example.delivery.cart.dto.request.CartCreateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.entity.Cart;
import com.example.delivery.cart.repository.CartRepository;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.menu.repository.MenuOptionRepository;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartCreateService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;

    // 장바구니 생성
    @Transactional
    public CartResponseDto createCart(Long userId, CartCreateRequestDto requestDto) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 담은 메뉴
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MENU));

        // 메뉴 활성화 여부 확인
        if (menu.isDeleted() || menu.isHidden()) {
            throw new CustomException(ErrorCode.UNAVAILABLE_MENU);
        }

        // 점주 : 자신의 가게 주문만
        if (user.getRole() == UserRoleEnum.OWNER){
            List<UUID> myStoreIdList = user.getStores().stream()
                    .map(Store::getStoreId)
                    .collect(Collectors.toList());

            if (!myStoreIdList.contains(requestDto.getStoreId())) {
                throw new CustomException(ErrorCode.CANNOT_ADD_MENU_FROM_UNOWNED_STORE);
            }
        }

        // 현재 장바구니 목록 : 아직 주문이 안된 대기상태
        List<Cart> cartList = cartRepository.findByUser_UserIdAndCartStatus(user.getUserId(), Cart.CartStatus.PENDING);

        // 가게 비교
        if (!cartList.isEmpty()) {
            UUID currentStoreId = cartList.get(0).getMenu().getStore().getStoreId();

            if (!currentStoreId.equals(requestDto.getStoreId())) {
                throw new CustomException(ErrorCode.CANNOT_ADD_DIFFERENT_STORE_MENU);
            }
        }

        // 선택 메뉴 옵션
        List<MenuOption> menuOptions = menuOptionRepository.findAllById(requestDto.getMenuOptionIds());

        // 메뉴 가격 산출
        int menuPrice = menu.getMenuPrice();
        for (MenuOption option : menuOptions) {
            menuPrice += option.getOptionPrice();
        }

        Cart cart = requestDto.toEntity(user, menu, menuOptions, menuPrice);
        cartRepository.save(cart);

        return CartResponseDto.from(cart);
    }
}
