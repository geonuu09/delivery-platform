package com.example.delivery.user.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.user.dto.SignupRequestDto;
import com.example.delivery.user.dto.SignupResponseDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.dto.UserUpdateRequestDto;
import com.example.delivery.user.service.UserService;
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
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER','MANAGER', 'MASTER')")
    public ResponseEntity<UserResponseDto> updateUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody UserUpdateRequestDto requestDto) {

        UserResponseDto responseDto = userService.updateUser(userDetails.getUserId(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/admin/{userId}")
    @PreAuthorize("hasAnyRole('MASTER')")
    public ResponseEntity<UserResponseDto> updateUserAdmin(
        @RequestBody UserUpdateRequestDto requestDto, @PathVariable Long userId) {

        UserResponseDto responseDto = userService.updateUser(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.deleteUser(userDetails.getUserId(), userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

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

