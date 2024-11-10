package com.example.delivery.user.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.dto.UserUpdateRequestDto;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
        @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(responseDto);

    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean isAsc) {
        Page<UserResponseDto> users = userService.getAllUsers(page, size, sortBy, isAsc);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("userId") Long userId) {
        UserResponseDto userResponseDto = userService.getUserById(userId);
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER', 'MASTER')")
    public ResponseEntity<UserResponseDto> updateUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody UserUpdateRequestDto requestDto) {

        UserRoleEnum userRole = userDetails.getUser().getRole();
        Long userId = userDetails.getUser().getUserId();

        UserResponseDto responseDto;
        if(userRole == UserRoleEnum.CUSTOMER) {
            responseDto = userService.updateCustomer(userId, requestDto);
        } else if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            responseDto = userService.updateByAdmin(requestDto);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "해당 권한은 접근할 수 없습니다.");
        }
        return ResponseEntity.ok(responseDto);
    }


}

