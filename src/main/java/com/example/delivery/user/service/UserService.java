package com.example.delivery.user.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signup(@Valid SignupRequestDto requestDto) {
        String encodePassword = passwordEncoder.encode(requestDto.getPassword());

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        User user = User.builder()
            .userName(requestDto.getUserName())
            .email(requestDto.getEmail())
            .password(encodePassword)
            .streetAddress(requestDto.getStreetAddress())
            .detailAddress(requestDto.getDetailAddress())
            .phoneNum(requestDto.getPhoneNum())
            .role(requestDto.getRole() != null ? requestDto.getRole() : UserRoleEnum.CUSTOMER)
            .build();
        User savedUser = userRepository.save(user);

        return new SignupResponseDto(
            savedUser.getUserId(),
            savedUser.getEmail(),
            savedUser.getRole().getAuthority()
        );
    }

}
