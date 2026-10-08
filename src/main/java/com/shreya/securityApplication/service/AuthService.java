package com.shreya.securityApplication.service;

import com.shreya.securityApplication.dto.LoginDTO;
import com.shreya.securityApplication.dto.LoginResponseDTO;
import com.shreya.securityApplication.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final SessionService sessionService;

    public LoginResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
//        String token = jwtService.generateToken(userEntity);
        String accessToken  = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);
        sessionService.generateNewSession(userEntity, refreshToken);

        return new LoginResponseDTO(userEntity.getId(), accessToken, refreshToken);

    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        UserEntity userEntity = userService.getUserById(userId);
        sessionService.validateSession(refreshToken);

        String accessToken = jwtService.generateAccessToken(userEntity);
        return new LoginResponseDTO(userEntity.getId(), accessToken, refreshToken);

    }
}
