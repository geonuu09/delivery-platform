package com.example.delivery.auth.controller;

import com.example.delivery.auth.dto.SigninRequestDto;
import com.example.delivery.auth.dto.SigninResponseDto;
import com.example.delivery.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody @Valid SigninRequestDto request) {
        SigninResponseDto response = authService.signin(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}
