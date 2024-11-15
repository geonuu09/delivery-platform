package com.example.delivery.user.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.dto.UserUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User API", description = "사용자 관리 API")
public interface UserControllerSwagger {

    @Operation(
        summary = "회원 가입",
        description = "새로운 사용자를 등록합니다."
    )
    @PostMapping("/signup")
    ResponseEntity<SignupResponseDto> signup(
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
        @RequestPart("userData") @Valid SignupRequestDto requestDto
    );

    @Operation(
        summary = "이메일 중복 체크",
        description = "이메일의 중복 여부를 확인합니다."
    )
    @GetMapping("/users/email/check")
    ResponseEntity<Boolean> checkEmail(
        @Parameter(description = "확인할 이메일 주소") @RequestParam String email
    );

    @Operation(
        summary = "회원 정보 수정",
        description = "자신의 회원 정보를 수정합니다."
    )
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER','MANAGER','MASTER')")
    ResponseEntity<UserResponseDto> updateUser(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
        @RequestPart("userData") @Valid UserUpdateRequestDto requestDto
    );

    @Operation(
        summary = "관리자의 회원 정보 수정",
        description = "관리자가 특정 회원의 정보를 수정합니다."
    )
    @PutMapping("/admin/{userId}")
    @PreAuthorize("hasAnyRole('MASTER')")
    ResponseEntity<UserResponseDto> updateUserAdmin(
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
        @RequestPart("userData") @Valid UserUpdateRequestDto requestDto,
        @Parameter(description = "수정할 사용자 ID") @PathVariable Long userId
    );

    @Operation(
        summary = "회원 탈퇴",
        description = "자신의 계정을 삭제합니다."
    )
    @DeleteMapping("/me")
    ResponseEntity<?> deleteUser(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(
        summary = "관리자의 회원 삭제",
        description = "관리자가 특정 회원을 삭제합니다."
    )
    @DeleteMapping("/admin/{userId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    ResponseEntity<?> deleteUserAdmin(
        @Parameter(description = "삭제할 사용자 ID") @PathVariable Long userId,
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(
        summary = "전체 회원 조회",
        description = "관리자가 모든 회원을 조회합니다."
    )
    @GetMapping("/admin/users")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    ResponseEntity<Page<UserResponseDto>> getAllUsers(
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "정렬 기준", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
        @Parameter(description = "오름차순 여부", example = "true") @RequestParam(defaultValue = "true") boolean isAsc
    );

    @Operation(
        summary = "회원 단일 조회",
        description = "관리자가 특정 회원을 조회합니다."
    )
    @GetMapping("/admin/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    ResponseEntity<UserResponseDto> getUserById(
        @Parameter(description = "사용자 ID", example = "1") @PathVariable Long userId
    );
}
