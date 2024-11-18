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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String processProfileImage(MultipartFile profileImage) {
        String profileImagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImagePath = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
        }
        return profileImagePath;
    }

    public SignupResponseDto signup(@Valid SignupRequestDto requestDto,
        MultipartFile profileImage) {
        String encodePassword = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkEmail = userRepository.findByEmail(requestDto.getEmail());
        if (checkEmail.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String profileImagePath = processProfileImage(profileImage);

        User user = User.builder()
            .userName(requestDto.getUserName())
            .email(requestDto.getEmail())
            .password(encodePassword)
            .streetAddress(requestDto.getStreetAddress())
            .detailAddress(requestDto.getDetailAddress())
            .phoneNum(requestDto.getPhoneNum())
            .role(requestDto.getRole())
            .profileImagePath(profileImagePath)
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
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto,
        MultipartFile profileImage) {
        return updateUserInfo(userId, requestDto, profileImage);
    }

    private UserResponseDto updateUserInfo(Long userId, UserUpdateRequestDto requestDto,
        MultipartFile profileImage) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String encodePassword = null;
        if (requestDto.getPassword() != null) {
            encodePassword = passwordEncoder.encode(requestDto.getPassword());
        }

        String profileImagePath = processProfileImage(profileImage);

        user.updateUserInfo(
            requestDto.getUserName(),
            encodePassword,
            requestDto.getPhoneNum(),
            requestDto.getStreetAddress(),
            requestDto.getDetailAddress(),
            profileImagePath
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
