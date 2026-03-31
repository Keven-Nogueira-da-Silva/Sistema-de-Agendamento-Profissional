package com.exemplo.agendamento.controller;

import com.exemplo.agendamento.dto.Dtos;
import com.exemplo.agendamento.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {
    private final AgendamentoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIA')")
    public ResponseEntity<Dtos.AgendamentoResponse> criar(@RequestBody @Valid Dtos.AgendamentoRequest request) {
        return ResponseEntity.ok(service.criar(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIA')")
    public ResponseEntity<Dtos.AgendamentoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid Dtos.AgendamentoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }
}
