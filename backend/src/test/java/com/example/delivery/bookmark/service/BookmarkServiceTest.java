package com.example.delivery.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.delivery.bookmark.dto.BookmarkedStoreResponseDto;
import com.example.delivery.bookmark.entity.Bookmark;
import com.example.delivery.bookmark.repository.BookmarkRepository;
import com.example.delivery.store.entity.Store;
import com.example.delivery.store.repository.StoreRepository;
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
            .storeName("테스트 가게")
            .build();

        // 테스트용 북마크 생성
        testBookmark = Bookmark.builder()
            .bookmarkId(UUID.randomUUID())
            .user(testUser)
            .store(testStore)
            .build();
    }


    @Test
    @DisplayName("북마크 생성 테스트 - 기존 북마크가 없는 경우")
    void createBookmark_Success() {
        // given - 테스트에 필요한 데이터와 상황을 설정

        UUID storeId = UUID.randomUUID();
        Long userId = 1L;

        // Mock 객체의 동작 정의
        // When - 어느 시점에, thenReturn - 예상되는 결과값

        // ex :
        // When - userRepository.findById(userId)가 호출되는 시점에
        // thenReturn - Optional.of(user)라는 결과값을 반환하도록 가정(stub)함
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(testStore));
        when(bookmarkRepository.findByUserAndStore(testUser, testStore)).thenReturn(
            Optional.empty());
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(new Bookmark());

        // when - 테스트할 메소드 실행
        boolean result = bookmarkService.toggleBookmark(storeId, userId);

        // then - 결과 검증
        assertThat(result).isTrue();  // 새로운 북마크가 생성되었으므로 true 반환
        verify(bookmarkRepository, times(1)).save(
            any(Bookmark.class));  // save 메소드가 정확히 1번 호출되었는지 검증
    }

    @Test
    @DisplayName("북마크 삭제 테스트 - 기존 북마크가 있는 경우")
    void deleteBookmark_Success() {
        // given
        UUID storeId = UUID.randomUUID();
        Long userId = 1L;
        log.info("테스트 시작: 북마크 삭제 - storeId: {}, userId: {}", storeId, userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(testStore));
        when(bookmarkRepository.findByUserAndStore(testUser, testStore)).thenReturn(
            Optional.of(testBookmark));

        // when
        log.info("북마크 토글 실행");
        boolean result = bookmarkService.toggleBookmark(storeId, userId);

        // then
        assertThat(result).isFalse();  // 북마크가 삭제되었으므로 false 반환
        verify(bookmarkRepository, times(1)).delete(testBookmark);
    }


    @Test
    @DisplayName("사용자의 북마크된 가게 목록 조회 테스트")
    void getUserBookmarkedTest() {
        // given
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        boolean isAsc = true;

        // 테스트용 북마크 리스트 생성
        List<Bookmark> bookmarks = new ArrayList<>();
        bookmarks.add(testBookmark);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);
        Page<Bookmark> bookmarkPage = new PageImpl<>(bookmarks, pageable, bookmarks.size());

        // Mock 설정 추가
        when(userRepository.findById(testUser.getUserId()))
            .thenReturn(Optional.of(testUser));
        when(bookmarkRepository.findAllByUser(testUser, pageable))
            .thenReturn(bookmarkPage);
        log.info("Mock 설정 완료");

        // when - 실제 테스트 수행
        Page<BookmarkedStoreResponseDto> result =
            bookmarkService.getUserBookmarked(testUser.getUserId(), page, size, sortBy, isAsc);
        log.info("북마크 서비스 호출 완료");

        // then - 테스트 결과 검증


        assertThat(result).isNotNull();
        log.info("검증 1 완료: 결과가 null이 아님");
        assertThat(result.getContent()).hasSize(1);
        log.info("검증 2 완료: 결과 리스트 크기 = {}", result.getContent().size());
        assertThat(result.getContent().get(0).getStoreName())
            .isEqualTo(testStore.getStoreName());
        log.info("검증 3 완료: 가게 이름 일치 확인 (예상값={}, 실제값={})",
            testStore.getStoreName(), result.getContent().get(0).getStoreName());

        verify(userRepository, times(1)).findById(testUser.getUserId());
        log.info("검증 4 완료: Repository 메소드 호출 횟수 확인");
        verify(bookmarkRepository, times(1)).findAllByUser(eq(testUser), any(Pageable.class));
        log.info("북마크 목록 조회 테스트 종료 - 모든 검증 통과");
    }
}