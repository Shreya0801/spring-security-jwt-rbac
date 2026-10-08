package com.shreya.securityApplication.controller;

import com.shreya.securityApplication.dto.LoginDTO;
import com.shreya.securityApplication.dto.LoginResponseDTO;
import com.shreya.securityApplication.dto.SignUpDTO;
import com.shreya.securityApplication.dto.UserDTO;

import com.shreya.securityApplication.service.AuthService;
import com.shreya.securityApplication.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${deploy.env}")
    private String deployEnv;

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpDTO signUpDTO){
         UserDTO userDTO = userService.signUp(signUpDTO);
         return ResponseEntity.ok(userDTO );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response){
        LoginResponseDTO loginResponseDTO = authService.login(loginDTO);

        Cookie cookie = new Cookie("refreshToken", loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/refresh")
    public  ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName())).
                findFirst().
                map(cookie -> cookie.getValue()).
                orElseThrow(()-> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
