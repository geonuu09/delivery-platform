package com.example.delivery.user.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signup(@Valid SignupRequestDto requestDto) {
        String encodePassword = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkEmail = userRepository.findByEmail(requestDto.getEmail());
        if (checkEmail.isPresent()) {
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

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(int page, int size, String sortBy, boolean isAsc) {
        // 페이지 사이즈 설정 (10, 30, 50 중 하나로 제한)
        size = (size == 10 || size == 30 || size == 50) ? size : 10;

        // 정렬 방향 설정
        // isAsc = true -> 오름차순
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy.equals("updatedAt") ? "updatedAt" : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        // 모든 유저 조회
        return userRepository.findAll(pageable).map(UserResponseDto::new);
    }

}
