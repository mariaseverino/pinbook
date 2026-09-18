package com.mariaseverino.pinbook.controller;

import com.mariaseverino.pinbook.dto.LoginRequest;
import com.mariaseverino.pinbook.dto.AuthenticatedResponse;
import com.mariaseverino.pinbook.dto.RegisterRequest;
import com.mariaseverino.pinbook.dto.SuccessResponse;
import com.mariaseverino.pinbook.service.AuthService;
import com.mariaseverino.pinbook.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<AuthenticatedResponse>> login(@Valid @RequestBody LoginRequest request){
        return ApiResponse.ok(
            authService.login(request),
            "Usuario logado com sucesso"
        );
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<AuthenticatedResponse>> register(@Valid @RequestBody RegisterRequest request){
        return ApiResponse.created(
            authService.register(request),
            "Registro criado com suceesso"
        );
    }
}