package com.example.delivery.bookmark.service;

import com.example.delivery.bookmark.dto.BookmarkedStoreResponseDto;
import com.example.delivery.bookmark.entity.Bookmark;
import com.example.delivery.bookmark.repository.BookmarkRepository;
import com.example.delivery.common.Util.PagingUtil;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public boolean toggleBookmark(UUID storeId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Optional<Bookmark> existingBookmark =
            bookmarkRepository.findByUserIdAndStoreId(user, store);

        if (existingBookmark.isPresent()) {
            bookmarkRepository.delete(existingBookmark.get());
            return false;
        } else {
            Bookmark bookmark = Bookmark.builder()
                .user(user)
                .store(store)
                .isBookmarked(true)
                .build();
            bookmarkRepository.save(bookmark);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public Page<BookmarkedStoreResponseDto> getUserBookmarKed(Long userId, int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        return bookmarkRepository.findAllByUserId(userId, pageable)
            .map(bookmark -> new BookmarkedStoreResponseDto(bookmark.getStore()));
    }


}
