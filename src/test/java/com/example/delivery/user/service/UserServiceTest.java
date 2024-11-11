package com.example.delivery.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTest {

    @SpyBean  // 실제 UserService를 사용하되 일부 메서드는 모킹 가능
    private UserService userService;

    @MockBean  // Spring의 MockBean으로 가짜 객체 생성
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupSuccessTest() {
        // Given
        SignupRequestDto request = SignupRequestDto.builder()
            .userName("testUser")
            .email("test@test.com")
            .password("password123")
            .streetAddress("서울시 강남구")
            .detailAddress("101동 1001호")
            .role(UserRoleEnum.CUSTOMER)
            .phoneNum("010-1234-5678")
            .build();

        // Mock 동작 정의
        when(passwordEncoder.encode(any(String.class)))
            .thenReturn("encodedPassword");

        User savedUser = User.builder()
            .userName(request.getUserName())
            .email(request.getEmail())
            .password("encodedPassword")
            .streetAddress(request.getStreetAddress())
            .detailAddress(request.getDetailAddress())
            .role(request.getRole())
            .phoneNum(request.getPhoneNum())
            .build();

        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

        // When
        SignupResponseDto response = userService.signup(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getRole()).isEqualTo(UserRoleEnum.CUSTOMER.getAuthority());

        // verify 호출 여부 검증
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }
}