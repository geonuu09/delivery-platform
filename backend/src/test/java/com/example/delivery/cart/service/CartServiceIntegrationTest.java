package com.example.delivery.cart.service;

import com.example.delivery.cart.dto.request.CartCreateRequestDto;
import com.example.delivery.cart.dto.request.CartUpdateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.entity.Cart;
import com.example.delivery.cart.repository.CartRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartServiceIntegrationTest {

    @Autowired
    CartCreateService cartCreateService;
    @Autowired
    CartGetListService cartGetListService;
    @Autowired
    CartUpdateService cartUpdateService;
    @Autowired
    CartDeleteService cartDeleteService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;

    User user;
    Long userId;
    CartResponseDto createdCart = null;
    int updatedCount = 0;

    @Test
    @Order(1)
    @DisplayName("장바구니 생성")
    void test1() {
        // given
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UUID menuId = UUID.fromString("d5e3f7b8-b7f5-4c3f-b071-f63f6cf62d6b");
        List<UUID> menuOptionIds = Arrays.asList(
                UUID.fromString("d5e3f7b8-b7f5-4c3f-b071-f63f6cf62d81"),
                UUID.fromString("d5e3f7b8-b7f5-4c3f-b071-f63f6cf62d80")
        );
        int count =2;
        Cart.CartStatus cartStatus = Cart.CartStatus.PENDING;

        CartCreateRequestDto requestDto = new CartCreateRequestDto(
                storeId,
                menuId,
                menuOptionIds,
                count,
                cartStatus
        );

        //Owner 유저
        user = userRepository.findById(1L).orElse(null);
        userId = user.getUserId();

        // when
        CartResponseDto cart = cartCreateService.createCart(userId, requestDto);

        // then
        assertNotNull(cart.getCartId());
        assertEquals(menuId, cart.getMenuId());
        assertEquals(2, cart.getMenuCount());

        createdCart = cart;
    }

    @Test
    @Order(2)
    @DisplayName("장바구니 수정")
    void test2() {
        // given
        UserRoleEnum userRole = UserRoleEnum.CUSTOMER;

        UUID cartId = this.createdCart.getCartId();
        UUID menuId = this.createdCart.getMenuId();
        List<UUID> menuOptionIdList = Arrays.asList(
                UUID.fromString("d5e3f7b8-b7f5-4c3f-b071-f63f6cf62d81"),
                UUID.fromString("d5e3f7b8-b7f5-4c3f-b071-f63f6cf62d80")
        );
        int menuCount = 5;

        CartUpdateRequestDto requestDto = new CartUpdateRequestDto(
                cartId,
                menuId,
                menuOptionIdList,
                menuCount
        );

        // when
        CartResponseDto cart = cartUpdateService.updateCart(userId, userRole, requestDto);

        // then
        assertEquals(this.createdCart.getCartId(), cart.getCartId());
        assertEquals(this.createdCart.getMenuId(), cart.getMenuId());

        this.updatedCount = 5;

    }

    @Test
    @Order(3)
    @DisplayName("장바구니 조회")
    void test3() {
        // given

        // when
        List<CartResponseDto> cartList = cartGetListService.getCartList(userId);

        // then
        // 1. 전체 장바구니에서 테스트에 의해 생성된 장바구니 찾기
        UUID createdCartId = this.createdCart.getCartId();
        CartResponseDto foundCart = cartList.stream()
                .filter(Cart -> Cart.getCartId().equals(createdCartId))
                .findFirst()
                .orElse(null);

        // 2. 1번 테스트에 의해 생성된 장바구니와 일치하는지 검증
        assertEquals(this.createdCart.getCartId(), foundCart.getCartId());
        assertEquals(this.createdCart.getMenuName(), foundCart.getMenuName());

        // 3. 2번 테스트에 의해 수량이 정상적으로 업데이트되었는지 검증
        assertEquals(this.updatedCount, foundCart.getMenuCount());
    }

    @Test
    @Order(4)
    @DisplayName("장바구니 삭제")
    void test4() {
        // given
        UUID cartId = this.createdCart.getCartId();
        // when
        CartResponseDto cart = cartDeleteService.deleteCart(userId, cartId);

        assertNotNull(cart);
        assertEquals(cartId, cart.getCartId());
    }
}
