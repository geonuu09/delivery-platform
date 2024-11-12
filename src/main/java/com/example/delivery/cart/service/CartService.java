package com.example.delivery.cart.service;

import com.example.delivery.cart.dto.request.CartCreateRequestDto;
import com.example.delivery.cart.dto.request.CartUpdateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.entity.Cart;
import com.example.delivery.cart.repository.CartRepository;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final MenuRepository menuOptionRepository;

    // 장바구니 생성
    @Transactional
    public CartResponseDto createCart(User user, CartCreateRequestDto requestDto) {

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

        Cart cart = requestDto.toEntity(user, menu, menuOptions,menuPrice);
        cartRepository.save(cart);

        return CartResponseDto.from(cart);
    }

    // 모든 장바구니 조회 : 관리자
    public List<CartResponseDto> getCartListByAdmin() {
        List<Cart> cartList = cartRepository.findAll();
        return cartList.stream()
                .map(CartResponseDto::from)
                .collect(Collectors.toList());
    }

    // 장바구니 조회
    public List<CartResponseDto> getCartList(Long userId) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 장바구니 목록 : 아직 주문이 안된 대기상태
        List<Cart> cartList = cartRepository.findByUser_UserIdAndStatus(userId, Cart.CartStatus.PENDING);

        return cartList.stream()
                .filter(cart -> user.getUserId().equals(cart.getUser().getUserId())) // 접근제한 : 주문자인지 확인
                .map(CartResponseDto::from)
                .collect(Collectors.toList());
    }

    // 장바구니 수정 : 관리자
    public CartResponseDto updateCartByAdmin(CartUpdateRequestDto requestDto) {
        // 해당 장바구니
        Cart cart = cartRepository.findById(requestDto.getCartId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CART));

        // 해당 메뉴
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MENU));

        // 메뉴 수량
        cart.setCount(requestDto.getMenuCount());

        // 메뉴 옵션
        cart.setMenuOptions(requestDto.getMenuOptionList());

        // 가격
        int menuPrice = menu.getMenuPrice();
        for (MenuOption option : requestDto.getMenuOptionList()) {
            menuPrice += option.getOptionPrice();
        }
        cart.setPrice(menuPrice);

        cartRepository.save(cart);
        return CartResponseDto.from(cart);
    }

    // 장바구니 수정
    public CartResponseDto updateCart(Long userId, CartUpdateRequestDto requestDto) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 장바구니
        Cart cart = cartRepository.findById(requestDto.getCartId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CART));

        // 접근제한 : 주문자인지 확인
        if (!user.getUserId().equals(cart.getUser().getUserId())) {
            throw new CustomException(ErrorCode.CART_PERMISSION_DENIED);
        }

        // 해당 메뉴
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MENU));

        // 메뉴 수량
        cart.setCount(requestDto.getMenuCount());

        // 메뉴 옵션
        cart.setMenuOptions(requestDto.getMenuOptionList());

        // 가격
        int menuPrice = menu.getMenuPrice();
        for (MenuOption option : requestDto.getMenuOptionList()) {
            menuPrice += option.getOptionPrice();
        }
        cart.setPrice(menuPrice);

        cartRepository.save(cart);
        return CartResponseDto.from(cart);
    }

    // 장바구니 삭제 : 관리자
    public CartResponseDto deleteCartByAdmin(String userEmail, UUID cartId) {
        // 해당 장바구니
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CART));

        cart.setCartStatus(Cart.CartStatus.DELETED);

        // 로그
        cart.setDeletedAt(LocalDateTime.now());
        cart.setDeletedBy(userEmail);
        return CartResponseDto.from(cart);
    }

    // 장바구니 삭제
    public CartResponseDto deleteOrder(Long userId, UUID cartId) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 장바구니
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CART));

        // 접근제한 : 주문자인지 확인
        if (!user.getUserId().equals(cart.getUser().getUserId())) {
            throw new CustomException(ErrorCode.CART_PERMISSION_DENIED);
        }

        cart.setCartStatus(Cart.CartStatus.DELETED);

        // 로그
        cart.setDeletedAt(LocalDateTime.now());
        cart.setDeletedBy(user.getEmail());
        return CartResponseDto.from(cart);
    }
}
