package com.example.delivery.store.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.store.dto.GetStoreDetailsResponseDto;
import com.example.delivery.store.dto.GetStoresResponseDto;
import com.example.delivery.store.dto.StoreRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Store API", description = "가게 관리 API")
public interface StoreControllerSwagger {

    @Operation(
            summary = "가게 등록",
            description = "새로운 가게를 등록합니다."
    )
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    ResponseEntity<String> createStore(
            @Valid @RequestBody StoreRequestDto storeRequestDto
    );

    @Operation(
            summary = "가게 전체 조회",
            description = "키워드와 카테고리로 가게를 검색합니다."
    )
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<Page<GetStoresResponseDto>> getStores(
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "오름차순 여부", example = "true") @RequestParam(defaultValue = "true") boolean isAsc,
            @Parameter(description = "검색 키워드") @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "카테고리 이름") @RequestParam(value = "categoryName", required = false) String categoryName
    );

    @Operation(
            summary = "가게 상세 조회",
            description = "특정 가게와 해당 가게의 메뉴 리스트를 조회합니다."
    )
    @GetMapping("/{storeId}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<GetStoreDetailsResponseDto> getStoreDetails(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "오름차순 여부", example = "true") @RequestParam(defaultValue = "true") boolean isAsc,
            @Parameter(description = "검색 키워드") @RequestParam(value = "keyword", required = false) String keyword
    );

    @Operation(
            summary = "가게 수정",
            description = "가게 정보를 수정합니다."
    )
    @PutMapping("/{storeId}/update")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    ResponseEntity<String> updateStore(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Valid @RequestBody StoreRequestDto storeRequestDto
    );

    @Operation(
            summary = "가게 삭제",
            description = "특정 가게를 삭제합니다."
    )
    @DeleteMapping("/{storeId}/delete")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    ResponseEntity<String> deleteStore(
            @Parameter(description = "삭제할 가게 ID") @PathVariable UUID storeId,
            @RequestBody StoreRequestDto storeRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );
}

