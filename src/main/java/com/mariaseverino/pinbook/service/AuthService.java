package com.mariaseverino.pinbook.service;

import com.mariaseverino.pinbook.dto.LoginRequest;
import com.mariaseverino.pinbook.dto.AuthenticatedResponse;
import com.mariaseverino.pinbook.dto.RegisterRequest;
import com.mariaseverino.pinbook.entity.User;
import com.mariaseverino.pinbook.repository.UserRepository;
import com.mariaseverino.pinbook.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticatedResponse login (LoginRequest request){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole().name(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new AuthenticatedResponse(accessToken, refreshToken);
    }

    public AuthenticatedResponse register(RegisterRequest request){
        boolean userExists = userRepository.existsByEmail(request.email());

        if (userExists){
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String passwordHash = passwordEncoder.encode(request.password());


        User newUser = User.builder().name(request.name())
                .email(request.email())
                .passwordHash(passwordHash)
                .role(User.Role.CLIENT)
                .build();


        userRepository.save(newUser);

        String accessToken = jwtService.generateAccessToken(newUser.getEmail(), newUser.getRole().name(), newUser.getId());
        String refreshToken = jwtService.generateRefreshToken(newUser.getEmail());

        return new AuthenticatedResponse(accessToken, refreshToken);

    }
}
