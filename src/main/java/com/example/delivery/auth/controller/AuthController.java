package com.example.delivery.auth.controller;

import com.example.delivery.auth.dto.SigninResponseDto;
import com.example.delivery.auth.dto.SigninRequestDto;
import com.example.delivery.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signin")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody @Valid SigninRequestDto request) {
        log.info("로그인 요청 받음 - 이메일: {}", request.getEmail());

        SigninResponseDto response = authService.signin(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }
}
