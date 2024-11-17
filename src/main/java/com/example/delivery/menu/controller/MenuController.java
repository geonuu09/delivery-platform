package com.example.delivery.menu.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.auth.service.AuthService;
import com.example.delivery.menu.dto.*;
import com.example.delivery.menu.service.MenuService;
import com.example.delivery.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    public ResponseEntity<String> createMenu(@PathVariable UUID storeId, @Valid @RequestBody MenuRequestDto menuRequestDto) {
        menuService.createMenu(storeId, menuRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("메뉴가 성공적으로 등록되었습니다.");
    }

    // 메뉴 상세 페이지 조회
    @GetMapping("/{menuId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MenuResponseDto> getMenuDetails(@PathVariable UUID storeId, @PathVariable UUID menuId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        MenuResponseDto menuResponseDto = menuService.getMenuDetails(storeId, menuId, user);
        return ResponseEntity.ok(menuResponseDto);
    }

    // 메뉴 수정
    @PutMapping("/{menuId}/update")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    public ResponseEntity<String> updateMenu(@PathVariable UUID storeId, @PathVariable UUID menuId, @Valid @RequestBody MenuRequestDto menuRequestDto) {
        menuService.updateMenu(storeId, menuId, menuRequestDto);
        return ResponseEntity.ok("메뉴가 성공적으로 수정되었습니다.");
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}/delete")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    public ResponseEntity<String> deleteMenu(@PathVariable UUID storeId, @PathVariable UUID menuId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        menuService.deleteMenu(storeId, menuId, userDetails.getUsername());
        return ResponseEntity.ok("메뉴가 성공적으로 삭제되었습니다.");
    }

    // 메뉴 옵션 추가
    @PostMapping("/{menuId}/options/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    public ResponseEntity<String> createMenuOption(@PathVariable UUID storeId, @PathVariable UUID menuId, @Valid @RequestBody MenuOptionRequestDto menuOptionRequestDto) {
        menuService.createMenuOption(storeId, menuId, menuOptionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("메뉴 옵션이 성공적으로 등록되었습니다.");
    }

    // 메뉴 옵션 수정
    @PutMapping("/{menuId}/options/update")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    public ResponseEntity<String> updateMenuOption(@PathVariable UUID storeId, @PathVariable UUID menuId, @Valid @RequestBody MenuOptionRequestDto menuOptionRequestDto) {
        menuService.updateMenuOption(storeId, menuId, menuOptionRequestDto);
        return ResponseEntity.ok("메뉴 옵션이 성공적으로 수정되었습니다.");
    }

    // 메뉴 옵션 삭제
    @DeleteMapping("/{menuId}/options/delete")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    public ResponseEntity<String> deleteMenuOption(@PathVariable UUID storeId, @PathVariable UUID menuId, @RequestBody MenuOptionRequestDto menuOptionRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID menuOptionId = menuOptionRequestDto.getMenuOptionId();
        menuService.deleteMenuOption(storeId, menuId, menuOptionId ,userDetails.getUsername());
        return ResponseEntity.ok("메뉴 옵션이 성공적으로 삭제되었습니다.");
    }

    //메뉴 설명 AI 요청
    @PostMapping("/{menuId}/aiQuestion")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    public ResponseEntity<AiDescriptionClientResponseDto> getAiDescription(@PathVariable UUID storeId, @PathVariable UUID menuId, @Valid @RequestBody AiDescriptionRequestDto aiDescriptionRequestDto) {
        AiDescriptionClientResponseDto aiDescriptionResponseDto = menuService.createAiDescription(storeId, menuId, aiDescriptionRequestDto);
        return ResponseEntity.ok(aiDescriptionResponseDto);
    }
}
