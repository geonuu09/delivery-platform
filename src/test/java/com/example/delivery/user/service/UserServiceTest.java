package com.example.delivery.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.dto.UserUpdateRequestDto;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.User.UserStatus;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@Slf4j
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SignupRequestDto signupRequestDto;
    private User testUser;

    /**
     * 각 테스트 실행 전 필요한 테스트 데이터 초기화
     * - 회원가입 요청 DTO 생성
     * - 테스트용 User 엔티티 생성
     */
    @BeforeEach
    void setUp() {
        log.info("테스트 데이터 초기화 시작");

        // 회원가입 요청 DTO 생성
        signupRequestDto = SignupRequestDto.builder()
            .userName("testUser")
            .email("test111@example.com")
            .password("Test1234!")
            .streetAddress("서울시 강남구")
            .detailAddress("123-456")
            .phoneNum("010-1234-5678")
            .role(UserRoleEnum.CUSTOMER)
            .build();

        log.info("회원가입 요청 DTO 생성 완료 - email: {}, role: {}",
            signupRequestDto.getEmail(),
            signupRequestDto.getRole());

        // 테스트용 User 엔티티 생성
        testUser = User.builder()
            .userId(1L)
            .userName(signupRequestDto.getUserName())
            .email(signupRequestDto.getEmail())
            .password("encodedPassword")
            .streetAddress(signupRequestDto.getStreetAddress())
            .detailAddress(signupRequestDto.getDetailAddress())
            .phoneNum(signupRequestDto.getPhoneNum())
            .role(signupRequestDto.getRole())
            .status(signupRequestDto.getStatus())
            .build();

        log.info("테스트용 User 엔티티 생성 완료 - userId: {}", testUser.getUserId());
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTest {

        @Test
        @DisplayName("정상적인 회원가입 성공")
        void signup_Success() {
            log.info("회원가입 성공 테스트 시작");

            // given: Mock 객체 행동 정의
            log.info("이메일 중복 체크 Mock 설정 - email: {}", signupRequestDto.getEmail());
            when(userRepository.findByEmail(signupRequestDto.getEmail()))
                .thenReturn(Optional.empty());

            log.info("비밀번호 암호화 Mock 설정");
            when(passwordEncoder.encode(any()))
                .thenReturn("encodedPassword");

            log.info("사용자 저장 Mock 설정");
            when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

            // when: 회원가입 서비스 실행
            log.info("회원가입 서비스 실행");
            SignupResponseDto response = userService.signup(signupRequestDto);
            log.info("회원가입 서비스 실행 완료 - 생성된 userId: {}", response.getUserId());

            // then: 결과 검증
            log.info("회원가입 결과 검증 시작");
            assertThat(response).isNotNull();
            assertThat(response.getUserId()).isEqualTo(1L);
            assertThat(response.getEmail()).isEqualTo(signupRequestDto.getEmail());
            assertThat(response.getRole()).isEqualTo(UserRoleEnum.CUSTOMER.getAuthority());
            log.info("회원가입 결과 검증 완료 - 모든 검증 통과");
        }
    }

    @Nested
    @DisplayName("사용자 목록 조회 테스트")
    class GetAllUsersTest {

        @Test
        @DisplayName("정상적인 페이징 조회 성공")
        void getAllUsers_Success() {
            // given
            log.info("사용자 목록 조회 테스트 시작");
            int page = 0;
            int size = 10;
            String sortBy = "userId";
            boolean isAsc = true;

            List<User> userList = List.of(testUser);
            Page<User> userPage = new PageImpl<>(userList);

            when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

            // when
            log.info("페이징 파라미터 - page: {}, size: {}, sortBy: {}, isAsc: {}",
                page, size, sortBy, isAsc);
            Page<UserResponseDto> response = userService.getAllUsers(page, size, sortBy, isAsc);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).hasSize(1);
            assertThat(response.getContent().get(0).getEmail()).isEqualTo(testUser.getEmail());
            log.info("페이징 조회 결과 검증 완료 - 총 건수: {}", response.getTotalElements());
        }
    }

    @Nested
    @DisplayName("단일 사용자 조회 테스트")
    class GetUserByIdTest {

        @Test
        @DisplayName("존재하는 사용자 ID로 조회 성공")
        void getUserById_Success() {
            // given
            log.info("단일 사용자 조회 테스트 시작 - userId: {}", testUser.getUserId());
            when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

            // when
            UserResponseDto response = userService.getUserById(testUser.getUserId());

            // then
            assertThat(response).isNotNull();
            assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
            log.info("사용자 조회 결과 검증 완료 - email: {}", response.getEmail());
        }

        @Test
        @DisplayName("존재하지 않는 사용자 ID로 조회 실패")
        void getUserById_UserNotFound() {
            // given
            Long nonExistentUserId = 999L;
            log.info("존재하지 않는 사용자 조회 테스트 시작 - userId: {}", nonExistentUserId);
            when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                () -> userService.getUserById(nonExistentUserId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
            log.info("예외 발생 확인 - ErrorCode: {}", exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("사용자 정보 수정 테스트")
    class UpdateUserTest {

        @Test
        @DisplayName("정상적인 사용자 정보 수정 성공")
        void updateUser_Success() {
            // given
            log.info("사용자 정보 수정 테스트 시작");
            UserUpdateRequestDto updateRequestDto = UserUpdateRequestDto
                .builder()
                .userName(testUser.getUserName())
                .password(testUser.getPassword())
                .streetAddress(testUser.getStreetAddress())
                .detailAddress(testUser.getDetailAddress())
                .phoneNum(testUser.getPhoneNum())
                .build();

            when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
            when(passwordEncoder.encode(any())).thenReturn("encodedUpdatedPassword");

            // when
            log.info("사용자 정보 수정 요청 - userId: {}", testUser.getUserId());
            UserResponseDto response = userService.updateUser(testUser.getUserId(), updateRequestDto);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getUserName()).isEqualTo(updateRequestDto.getUserName());
            assertThat(response.getStreetAddress()).isEqualTo(updateRequestDto.getStreetAddress());
            log.info("사용자 정보 수정 결과 검증 완료");
        }

        @Test
        @DisplayName("존재하지 않는 사용자 정보 수정 실패")
        void updateUser_UserNotFound() {
            // given
            Long nonExistentUserId = 999L;
            UserUpdateRequestDto updateRequestDto = UserUpdateRequestDto
                .builder()
                .userName(testUser.getUserName())
                .password(testUser.getPassword())
                .streetAddress(testUser.getStreetAddress())
                .detailAddress(testUser.getDetailAddress())
                .phoneNum(testUser.getPhoneNum())
                .build();

            when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                () -> userService.updateUser(nonExistentUserId, updateRequestDto));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
            log.info("예외 발생 - ErrorCode: {}", exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("사용자 삭제 테스트")
    class DeleteUserTest {

        @Test
        @DisplayName("사용자 삭제 성공")
        void deleteUser_Success() {
            // given
            log.info("사용자 삭제 테스트 시작 - userId: {}", testUser.getUserId());
            String deleteBy = "admin";
            when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

            // when
            log.info("사용자 삭제 요청 - deleteBy: {}", deleteBy);
            userService.deleteUser(testUser.getUserId(), deleteBy);

            // then
            assertThat(testUser.getStatus()).isEqualTo(UserStatus.INACTIVE);
            assertThat(testUser.getDeletedBy()).isEqualTo(deleteBy);
            assertThat(testUser.getDeletedAt()).isNotNull();
            log.info("사용자 삭제 상태 변경");
        }

        @Test
        @DisplayName("MASTER 권한 사용자 삭제 실패")
        void deleteUser_MasterUser_Fail() {
            // given
            User masterUser = User.builder()
                .userId(2L)
                .userName("master")
                .email("master@example.com")
                .password("encodedPassword")
                .role(UserRoleEnum.MASTER)
                .status(UserStatus.ACTIVE)
                .build();

            when(userRepository.findById(masterUser.getUserId()))
                .thenReturn(Optional.of(masterUser));

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                () -> userService.deleteUser(masterUser.getUserId(), "admin"));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_PERMISSION);
            log.info("MASTER 권한 삭제 시도 예외 발생 - ErrorCode: {}", exception.getErrorCode());
        }

        @Test
        @DisplayName("이미 삭제된 사용자 삭제 실패")
        void deleteUser_AlreadyDeleted_Fail() {
            // given
            testUser.setStatus(UserStatus.INACTIVE);
            when(userRepository.findById(testUser.getUserId()))
                .thenReturn(Optional.of(testUser));

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                () -> userService.deleteUser(testUser.getUserId(), "admin"));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_ALREADY_DELETE);
            log.info("삭제 시도 예외 발생 - ErrorCode: {}", exception.getErrorCode());
        }
    }
}
