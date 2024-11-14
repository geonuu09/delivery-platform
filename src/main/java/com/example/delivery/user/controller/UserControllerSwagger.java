package com.example.delivery.user.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.dto.UserUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User API", description = "사용자 관리 API")
public interface UserControllerSwagger {

    @Operation(
        summary = "회원 가입",
        description = "새로운 사용자를 등록합니다."
    )
    ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto);

    @Operation(
        summary = "회원 정보 수정",
        description = "자신의 회원 정보를 수정합니다."
    )
    ResponseEntity<UserResponseDto> updateUser(
        @Parameter(hidden = true) UserDetailsImpl userDetails,
        @RequestBody UserUpdateRequestDto requestDto
    );

    @Operation(
        summary = "회원 탈퇴",
        description = "자신의 계정을 삭제합니다."
    )
    ResponseEntity<?> deleteUser(
        @Parameter(hidden = true) UserDetailsImpl userDetails
    );

    @Operation(
        summary = "전체 회원 조회",
        description = "관리자가 모든 회원을 조회합니다."
    )
    ResponseEntity<Page<UserResponseDto>> getAllUsers(
        @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "정렬 기준") @RequestParam(defaultValue = "createdAt") String sortBy,
        @Parameter(description = "오름차순 여부") @RequestParam(defaultValue = "true") boolean isAsc
    );

    @Operation(
        summary = "회원 단일 조회",
        description = "관리자가 특정 회원을 조회합니다."
    )
    ResponseEntity<UserResponseDto> getUserById(
        @Parameter(description = "사용자 ID") @PathVariable Long userId
    );

    @Operation(
        summary = "관리자의 회원 정보 수정",
        description = "관리자가 특정 회원의 정보를 수정합니다."
    )
    ResponseEntity<UserResponseDto> updateUserAdmin(
        @RequestBody UserUpdateRequestDto requestDto,
        @Parameter(description = "수정할 사용자 ID") @PathVariable Long userId
    );

    @Operation(
        summary = "관리자의 회원 삭제",
        description = "관리자가 특정 회원을 삭제합니다."
    )
    ResponseEntity<?> deleteUserAdmin(
        @Parameter(description = "삭제할 사용자 ID") @PathVariable Long userId,
        @Parameter(hidden = true) UserDetailsImpl userDetails
    );
}