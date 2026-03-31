package com.exemplo.agendamento.controller;

import com.exemplo.agendamento.dto.Dtos;
import com.exemplo.agendamento.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<Dtos.LoginResponse> login(@RequestBody @Valid Dtos.LoginRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}