package com.exemplo.agendamento.controller;

import com.exemplo.agendamento.dto.Dtos;
import com.exemplo.agendamento.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admin/agendamentos")
@RequiredArgsConstructor
public class AdminAgendamentoController {

    private final AgendamentoService service;

    @GetMapping("/{id}/revisoes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Dtos.AuditRevisionResponse>> consultarRevisoes(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarHistorico(id));
    }
}

