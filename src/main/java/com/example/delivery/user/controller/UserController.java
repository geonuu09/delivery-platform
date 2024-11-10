package com.example.delivery.user.controller;

import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @PostMapping("/auth/signup")
    public ResponseEntity<SignupResponseDto> signup(
        @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(responseDto);

    }


    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean isAsc) {
        Page<UserResponseDto> users = userService.getAllUsers(page, size, sortBy, isAsc);
        return ResponseEntity.ok(users);
    }


}

