package com.example.delivery.bookmark.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.bookmark.dto.BookmarkedStoreResponseDto;
import com.example.delivery.bookmark.service.BookmarkService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/stores/{storeId}")
    public ResponseEntity<?> toggleStoreBookmark(
        @PathVariable UUID storeId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        bookmarkService.toggleBookmark(storeId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stores")
    public ResponseEntity<Page<BookmarkedStoreResponseDto>> getBookmarks(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean isAsc
    ) {
        Page<BookmarkedStoreResponseDto> bookmark = bookmarkService.getUserBookmarked(
            userDetails.getUserId(), page - 1, size, sortBy, isAsc);
        return ResponseEntity.ok(bookmark);
    }
}


