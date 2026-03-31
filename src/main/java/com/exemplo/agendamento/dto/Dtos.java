package com.exemplo.agendamento.dto;

import com.exemplo.agendamento.entity.Agendamento;
import com.exemplo.agendamento.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class Dtos {

    public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String senha
    ) {}

    public record LoginResponse(String token) {}

    public record AgendamentoRequest(
        @NotNull LocalDateTime dataHora,
        @NotBlank String clienteNome,
        @NotBlank String servico,
        @NotNull Agendamento.Status status
    ) {}

    public record AgendamentoResponse(
        Long id,
        LocalDateTime dataHora,
        String clienteNome,
        String servico,
        Agendamento.Status status,
        String createdBy,
        String lastModifiedBy
    ) {
        public static AgendamentoResponse fromEntity(Agendamento entity) {
            return new AgendamentoResponse(
                entity.getId(),
                entity.getDataHora(),
                entity.getClienteNome(),
                entity.getServico(),
                entity.getStatus(),
                entity.getCreatedBy(),
                entity.getLastModifiedBy()
            );
        }
    }

    public record AuditRevisionResponse(
        int revisionId,
        LocalDateTime revisionDate,
        String revisionType,
        AgendamentoResponse data
    ) {}
}
