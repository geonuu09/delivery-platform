package com.example.delivery.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.dto.UserUpdateRequestDto;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MultipartFile profileImage;

    private User testUser;
    private SignupRequestDto signupRequestDto;

    @BeforeEach
    void setUp() {
        signupRequestDto = SignupRequestDto.builder()
            .userName("testUser")
            .email("test@example.com")
            .password("password123!")
            .streetAddress("Test Street")
            .detailAddress("Test Detail")
            .phoneNum("010-1234-5678")
            .role(UserRoleEnum.CUSTOMER)
            .status(User.UserStatus.ACTIVE)
            .profileImage("test_image.png")
            .build();

        testUser = User.builder()
            .userId(1L)
            .userName("testUser")
            .email("test@example.com")
            .password("encodedPassword")
            .streetAddress("Test Street")
            .detailAddress("Test Detail")
            .phoneNum("010-1234-5678")
            .role(UserRoleEnum.CUSTOMER)
            .profileImagePath("test_image.png")
            .status(User.UserStatus.ACTIVE)
            .build();
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTest {

        @Test
        @DisplayName("정상적인 회원가입 성공")
        void signup_Success() {
            when(userRepository.findByEmail(signupRequestDto.getEmail()))
                .thenReturn(Optional.empty());
            when(passwordEncoder.encode(signupRequestDto.getPassword()))
                .thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            SignupResponseDto response = userService.signup(signupRequestDto, profileImage);

            assertThat(response).isNotNull();
            assertThat(response.getUserId()).isEqualTo(testUser.getUserId());
            assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
            assertThat(response.getRole()).isEqualTo(testUser.getRole().getAuthority());
        }

        @Test
        @DisplayName("이메일 중복으로 회원가입 실패")
        void signup_Fail_DuplicateEmail() {
            when(userRepository.findByEmail(signupRequestDto.getEmail()))
                .thenReturn(Optional.of(testUser));

            CustomException exception = assertThrows(CustomException.class,
                () -> userService.signup(signupRequestDto, profileImage));

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Nested
    @DisplayName("사용자 목록 조회 테스트")
    class GetAllUsersTest {

        @Test
        @DisplayName("페이징 사용자 목록 조회 성공")
        void getAllUsers_Success() {
            Page<User> userPage = new PageImpl<>(List.of(testUser));
            when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

            Page<UserResponseDto> response = userService.getAllUsers(0, 10, "userId", true);

            assertThat(response).isNotNull();
            assertThat(response.getContent()).hasSize(1);
            assertThat(response.getContent().get(0).getEmail()).isEqualTo(testUser.getEmail());
        }
    }

    @Nested
    @DisplayName("단일 사용자 조회 테스트")
    class GetUserByIdTest {

        @Test
        @DisplayName("존재하는 사용자 조회 성공")
        void getUserById_Success() {
            when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

            UserResponseDto response = userService.getUserById(testUser.getUserId());

            assertThat(response).isNotNull();
            assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
        }

        @Test
        @DisplayName("존재하지 않는 사용자 조회 실패")
        void getUserById_Fail_UserNotFound() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            CustomException exception = assertThrows(CustomException.class,
                () -> userService.getUserById(999L));

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("사용자 정보 수정 테스트")
    class UpdateUserTest {

        @Test
        @DisplayName("정상적인 사용자 정보 수정 성공")
        void updateUser_Success() {
            UserUpdateRequestDto updateDto = UserUpdateRequestDto.builder()
                .userName("updatedName")
                .password("newPassword123!")
                .build();
            when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
            when(passwordEncoder.encode(any())).thenReturn("encodedUpdatedPassword");

            UserResponseDto response = userService.updateUser(testUser.getUserId(), updateDto,
                profileImage);

            assertThat(response).isNotNull();
            assertThat(response.getUserName()).isEqualTo("updatedName");
        }
    }

    @Nested
    @DisplayName("사용자 삭제 테스트")
    class DeleteUserTest {

        @Test
        @DisplayName("정상적인 사용자 삭제 성공")
        void deleteUser_Success() {
            when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

            userService.deleteUser(testUser.getUserId(), "admin");

            assertThat(testUser.getStatus()).isEqualTo(User.UserStatus.INACTIVE);
        }

        @Test
        @DisplayName("MASTER 권한 사용자 삭제 실패")
        void deleteUser_MasterUser_Fail() {
            testUser.setRole(UserRoleEnum.MASTER);
            when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

            CustomException exception = assertThrows(CustomException.class,
                () -> userService.deleteUser(testUser.getUserId(), "admin"));

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_PERMISSION);
        }
    }
}
