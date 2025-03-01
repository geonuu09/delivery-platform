package com.example.delivery.store.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.store.dto.*;
import com.example.delivery.store.service.StoreService;
import com.example.delivery.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController implements StoreControllerSwagger{

    private final StoreService storeService;

    // 가게 등록
    @Override
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<String> createStore(@Valid @RequestBody StoreRequestDto storeRequestDto) {
        storeService.createStore(storeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("가게가 성공적으로 등록되었습니다.");
    }

    //가게 전체 페이지(키워드 및 카테고리 검색)
    @Override
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<GetStoresResponseDto>> getStores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryName", required = false) String categoryName) {

        Page<GetStoresResponseDto> result = storeService.getStores(page, size, sortBy, isAsc, keyword, categoryName);
        return ResponseEntity.ok(result);
    }

    //가게 상세 페이지 조회(가게정보 및 메뉴(메뉴이름, 메뉴사진, 가격)리스트)
    @Override
    @GetMapping("/{storeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetStoreDetailsResponseDto> getStoreDetails(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam(value = "keyword", required = false) String keyword) {

        User user = userDetails.getUser();
        GetStoreDetailsResponseDto storeResponseDto = storeService.getStoreDetails(storeId, user, page, size, sortBy, isAsc, keyword);
        return ResponseEntity.ok(storeResponseDto);
    }

    // 가게 수정
    @Override
    @PutMapping("/{storeId}/update")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER') or @authService.isStoreOwner(principal, #storeId)")
    public ResponseEntity<String> updateStore(@PathVariable UUID storeId, @Valid @RequestBody StoreUpdateDto storeUpdateDto) {
        storeService.updateStore(storeUpdateDto, storeId);
        return ResponseEntity.ok("가게 정보가 성공적으로 수정되었습니다.");
    }

    // 가게 삭제
    @Override
    @DeleteMapping("/{storeId}/delete")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<String> deleteStore(@PathVariable UUID storeId, @RequestBody StoreRequestDto storeRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        storeRequestDto.setStoreId(storeId);
        storeService.deleteStore(storeRequestDto, userDetails.getUsername());
        return ResponseEntity.ok("가게가 성공적으로 삭제되었습니다.");
    }

}
