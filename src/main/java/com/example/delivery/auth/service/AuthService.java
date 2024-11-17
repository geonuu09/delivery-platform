package com.example.delivery.auth.service;

import com.example.delivery.auth.dto.SigninResponseDto;
import com.example.delivery.auth.jwt.JwtUtil;
import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager; // 인증을 위한 매니저
    private final JwtUtil jwtUtil; // JWT 토큰 생성기
    private final StoreRepository storeRepository;
//    private final UserDetailsServiceImpl userDetailsService; // 사용자 정보 로딩을 위한 서비스

    public SigninResponseDto signin(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                String authToken = jwtUtil.createToken(userDetails.getUsername(),
                    userDetails.getRole());
                return new SigninResponseDto(
                    authToken,
                    userDetails.getUsername()
                );
            }
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        } catch (BadCredentialsException e) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        } catch (ClassCastException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "사용자 정보 변환 중 오류가 발생했습니다.");
        }
    }

    public boolean isStoreOwner(UserDetailsImpl userDetails, UUID storeId) {
        Long userId = userDetails.getUser().getUserId();
        return storeRepository.existsByUser_UserIdAndStoreId(userId, storeId);
    }
}