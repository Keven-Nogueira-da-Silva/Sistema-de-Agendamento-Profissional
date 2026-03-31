package com.exemplo.agendamento.service;

import com.exemplo.agendamento.dto.Dtos;
import com.exemplo.agendamento.repository.UserRepositories;
import com.exemplo.agendamento.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositories repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Dtos.LoginResponse authenticate(Dtos.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );
        var user = repository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new Dtos.LoginResponse(jwtToken);
    }
}
