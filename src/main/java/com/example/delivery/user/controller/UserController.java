package com.example.delivery.user.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.dto.UserUpdateRequestDto;
import com.example.delivery.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    // User 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
        @RequestBody @Valid SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(responseDto);

    }

    // Admin -> 회원 전체 조회
    @GetMapping("/admin/users")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean isAsc) {
        Page<UserResponseDto> users = userService.getAllUsers(page, size, sortBy, isAsc);
        return ResponseEntity.ok(users);
    }

    // Admin -> 회원 단일 조회
    @GetMapping("/admin/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("userId") Long userId) {
        UserResponseDto userResponseDto = userService.getUserById(userId);
        return ResponseEntity.ok(userResponseDto);
    }

    // 자신의 계정 수정
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER','MANAGER', 'MASTER')")
    public ResponseEntity<UserResponseDto> updateUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody @Valid UserUpdateRequestDto requestDto) {

        UserResponseDto responseDto = userService.updateUser(userDetails.getUserId(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // Admin -> 유저 수정
    @PutMapping("/admin/{userId}")
    @PreAuthorize("hasAnyRole('MASTER')")
    public ResponseEntity<UserResponseDto> updateUserAdmin(
        @RequestBody @Valid UserUpdateRequestDto requestDto, @PathVariable Long userId) {

        UserResponseDto responseDto = userService.updateUser(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 자신의 계정 삭제
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.deleteUser(userDetails.getUserId(), userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    // Admin -> 유저 삭제
    @DeleteMapping("/admin/{userId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public ResponseEntity<?> deleteUserAdmin(
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.deleteUser(userId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

}

