package com.example.delivery.menu.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.menu.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Menu API", description = "메뉴 관리 API")
public interface MenuControllerSwagger {

    @Operation(
            summary = "메뉴 등록",
            description = "새로운 메뉴를 등록합니다."
    )
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    ResponseEntity<String> createMenu(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Valid @RequestBody MenuRequestDto menuRequestDto
    );

    @Operation(
            summary = "메뉴 상세 조회",
            description = "특정 메뉴의 상세 정보를 조회합니다."
    )
    @GetMapping("/{menuId}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<MenuResponseDto> getMenuDetails(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(
            summary = "메뉴 수정",
            description = "특정 메뉴 정보를 수정합니다."
    )
    @PutMapping("/{menuId}/update")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    ResponseEntity<String> updateMenu(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId,
            @Valid @RequestBody MenuRequestDto menuRequestDto
    );

    @Operation(
            summary = "메뉴 삭제",
            description = "특정 메뉴를 삭제합니다."
    )
    @DeleteMapping("/{menuId}/delete")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    ResponseEntity<String> deleteMenu(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(
            summary = "메뉴 옵션 추가",
            description = "특정 메뉴에 옵션을 추가합니다."
    )
    @PostMapping("/{menuId}/options/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    ResponseEntity<String> createMenuOption(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId,
            @Valid @RequestBody MenuOptionRequestDto menuOptionRequestDto
    );

    @Operation(
            summary = "메뉴 옵션 수정",
            description = "특정 메뉴의 옵션을 수정합니다."
    )
    @PutMapping("/{menuId}/options/update")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    ResponseEntity<String> updateMenuOption(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId,
            @Valid @RequestBody MenuOptionRequestDto menuOptionRequestDto
    );

    @Operation(
            summary = "메뉴 옵션 삭제",
            description = "특정 메뉴의 옵션을 삭제합니다."
    )
    @DeleteMapping("/{menuId}/options/delete")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    ResponseEntity<String> deleteMenuOption(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId,
            @RequestBody MenuOptionRequestDto menuOptionRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(
            summary = "AI 메뉴 설명 생성",
            description = "AI를 통해 메뉴 설명을 생성합니다."
    )
    @PostMapping("/{menuId}/aiQuestion")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    ResponseEntity<AiDescriptionClientResponseDto> getAiDescription(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId,
            @Valid @RequestBody AiDescriptionRequestDto aiDescriptionRequestDto
    );
}
