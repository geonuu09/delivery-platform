package com.example.delivery.user.service;

import com.example.delivery.common.Util.PagingUtil;
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
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            .userProfileImageUrl(requestDto.getUserProfileImageUrl())
            .status(requestDto.getStatus())
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
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        return userRepository.findAll(pageable).map(UserResponseDto::new);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long userId) {
        return userRepository.findById(userId)
            .map(UserResponseDto::new)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 유저 전용 수정
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {
        return updateUserInfo(userId, requestDto);
    }

    private UserResponseDto updateUserInfo(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String encodePassword = passwordEncoder.encode(requestDto.getPassword());

        user.updateUserInfo(
            requestDto.getUserName(),
            encodePassword,
            requestDto.getPhoneNum(),
            requestDto.getStreetAddress(),
            requestDto.getDetailAddress(),
            requestDto.getUserProfileImageUrl()
        );
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId, String deleteBy) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getRole() == UserRoleEnum.MASTER) {
            throw new CustomException(ErrorCode.INVALID_PERMISSION, "이 권한은 삭제할 수 없습니다.");
        }
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new CustomException(ErrorCode.USER_ALREADY_DELETE);
        }
        user.setStatus(UserStatus.INACTIVE);
        user.setDeletedAt(LocalDateTime.now());
        user.setDeletedBy(deleteBy);
    }
}
