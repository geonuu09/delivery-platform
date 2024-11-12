package com.example.delivery.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.delivery.bookmark.dto.BookmarkedStoreResponseDto;
import com.example.delivery.bookmark.entity.Bookmark;
import com.example.delivery.bookmark.repository.BookmarkRepository;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.store.entity.Store;
import com.example.delivery.store.respository.StoreRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * BookmarkService 클래스의 단위 테스트 북마크 생성, 삭제, 예외 처리 등의 기능을 테스트합니다.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)  // Mockito의 Mock 기능을 JUnit과 통합

class BookmarkServiceTest {

    @Mock  // 실제 구현체 대신 Mock 객체를 생성
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks  // Mock 객체들을 주입받는 테스트 대상 서비스
    private BookmarkService bookmarkService;

    /**
     * 새로운 북마크 생성 테스트 시나리오: 사용자가 처음으로 가게를 북마크할 때
     */
    @Test
    @DisplayName("북마크 생성 테스트 - 기존 북마크가 없는 경우")
    void createBookmark_Success() {
        // given - 테스트에 필요한 데이터와 상황을 설정
        UUID storeId = UUID.randomUUID();
        Long userId = 1L;
        log.info("테스트 시작: 북마크 생성 - storeId: {}, userId: {}", storeId, userId);

        // Mock 객체 생성
        User user = User.builder()
            .userId(userId)
            .build();
        Store store = Store.builder()
            .storeId(storeId)
            .build();

        log.info("Mock 객체 생성 완료 - user: {}, store: {}", user.getUserId(), store.getStoreId());

        // Mock 객체의 동작 정의
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(bookmarkRepository.findByUserIdAndStoreId(user, store)).thenReturn(Optional.empty());
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(new Bookmark());

        log.info("Mock 동작 정의 완료");

        // when - 테스트할 메소드 실행
        log.info("북마크 토글 실행");
        boolean result = bookmarkService.toggleBookmark(storeId, userId);

        // then - 결과 검증
        assertThat(result).isTrue();  // 새로운 북마크가 생성되었으므로 true 반환
        verify(bookmarkRepository, times(1)).save(
            any(Bookmark.class));  // save 메소드가 정확히 1번 호출되었는지 검증
        log.info("북마크 생성 테스트 성공 - result: {}", result);
    }

    /**
     * 기존 북마크 삭제 테스트 시나리오: 이미 북마크한 가게를 다시 북마크 해제할 때
     */
    @Test
    @DisplayName("북마크 삭제 테스트 - 기존 북마크가 있는 경우")
    void deleteBookmark_Success() {
        // given
        UUID storeId = UUID.randomUUID();
        Long userId = 1L;
        log.info("테스트 시작: 북마크 삭제 - storeId: {}, userId: {}", storeId, userId);

        User user = User.builder()
            .userId(userId)
            .build();
        Store store = Store.builder()
            .storeId(storeId)
            .build();
        Bookmark existingBookmark = Bookmark.builder()
            .user(user)
            .store(store)
            .isBookmarked(true)
            .build();

        log.debug("Mock 객체 및 기존 북마크 생성 완료");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(bookmarkRepository.findByUserIdAndStoreId(user, store)).thenReturn(
            Optional.of(existingBookmark));

        log.debug("Mock 동작 정의 완료");

        // when
        log.info("북마크 토글 실행");
        boolean result = bookmarkService.toggleBookmark(storeId, userId);

        // then
        assertThat(result).isFalse();  // 북마크가 삭제되었으므로 false 반환
        verify(bookmarkRepository, times(1)).delete(existingBookmark);
        log.info("북마크 삭제 테스트 성공 - result: {}", result);
    }

    /**
     * 사용자를 찾을 수 없는 경우의 예외 처리 테스트 시나리오: 존재하지 않는 사용자가 북마크를 시도할 때
     */
    @Test
    @DisplayName("북마크 토글 실패 테스트 - 사용자를 찾을 수 없는 경우")
    void toggleBookmark_UserNotFound() {
        // given
        UUID storeId = UUID.randomUUID();
        Long userId = 1L;
        log.info("테스트 시작: 사용자 없음 예외 - storeId: {}, userId: {}", storeId, userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        log.info("사용자 없음 상황 설정 완료");

        // when & then
        assertThatThrownBy(() -> bookmarkService.toggleBookmark(storeId, userId))
            .isInstanceOf(CustomException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

        // 예외 상황에서는 save나 delete가 호출되지 않아야 함
        verify(bookmarkRepository, never()).save(any(Bookmark.class));
        verify(bookmarkRepository, never()).delete(any(Bookmark.class));

        log.info("사용자 없음 예외 테스트 성공");
    }

    /**
     * 가게를 찾을 수 없는 경우의 예외 처리 테스트 시나리오: 존재하지 않는 가게에 대해 북마크를 시도할 때
     */
    @Test
    @DisplayName("북마크 토글 실패 테스트 - 가게를 찾을 수 없는 경우")
    void toggleBookmark_StoreNotFound() {
        // given
        UUID storeId = UUID.randomUUID();
        Long userId = 1L;
        log.info("테스트 시작: 가게 없음 예외 - storeId: {}, userId: {}", storeId, userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());
        log.debug("가게 없음 상황 설정 완료");

        // when & then
        assertThatThrownBy(() -> bookmarkService.toggleBookmark(storeId, userId))
            .isInstanceOf(CustomException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.STORE_NOT_FOUND);

        verify(bookmarkRepository, never()).save(any(Bookmark.class));
        verify(bookmarkRepository, never()).delete(any(Bookmark.class));

        log.info("가게 없음 예외 테스트 성공");
    }



    private User testUser;
    private Store testStore;
    private Bookmark testBookmark;
    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        testUser = User.builder()
            .userId(1L)
            .build();

        // 테스트용 가게 생성
        testStore = Store.builder()
            .storeId(UUID.randomUUID())
            .build();

        // 테스트용 북마크 생성
        testBookmark = Bookmark.builder()
            .bookmarkId(UUID.randomUUID())
            .user(testUser)
            .store(testStore)
            .isBookmarked(true)
            .build();
    }

    @Test
    @DisplayName("사용자의 북마크된 가게 목록 조회 테스트")
    void getUserBookmarkedTest() {
        // given - 테스트를 위한 기본 데이터 설정

        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        boolean isAsc = true;

        // 테스트용 북마크 리스트 생성
        List<Bookmark> bookmarks = new ArrayList<>();
        bookmarks.add(testBookmark);


        Pageable pageable = PageRequest.of(page, size,
            isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Page<Bookmark> bookmarkPage = new PageImpl<>(bookmarks, pageable, bookmarks.size());

        // when - 실제 테스트 수행
        // bookmarkRepository의 findAllByUserId 메소드 호출 시 생성한 bookmarkPage 반환하도록 설정
        when(bookmarkRepository.findAllByUserId(testUser.getUserId(), pageable))
            .thenReturn(bookmarkPage);

        log.info("bookmarkPage: {}", bookmarkPage);

        // then - 테스트 결과 검증
        // 실제 서비스 메소드 호출
        Page<BookmarkedStoreResponseDto> result =
            bookmarkService.getUserBookmarKed(testUser.getUserId(), page, size, sortBy, isAsc);

        // 결과 로그 출력
        log.info("result: {}", result);

        // 검증 1: 결과가 null이 아닌지 확인
        assertThat(result).isNotNull();
        // 검증 2: 결과 리스트의 크기가 1인지 확인
        assertThat(result.getContent()).hasSize(1);
        // 검증 3: 반환된 가게 이름이 테스트 가게의 이름과 일치하는지 확인
        assertThat(result.getContent().get(0).getStoreName())
            .isEqualTo(testStore.getStoreName());
    }










}