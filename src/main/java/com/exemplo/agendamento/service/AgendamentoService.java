package com.exemplo.agendamento.service;

import com.exemplo.agendamento.dto.Dtos;
import com.exemplo.agendamento.entity.Agendamento;
import com.exemplo.agendamento.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;

    @Transactional
    public Dtos.AgendamentoResponse criar(Dtos.AgendamentoRequest request) {
        var agendamento = Agendamento.builder()
                .dataHora(request.dataHora())
                .clienteNome(request.clienteNome())
                .servico(request.servico())
                .status(request.status())
                .build();
        return Dtos.AgendamentoResponse.fromEntity(repository.save(agendamento));
    }

    @Transactional
    public Dtos.AgendamentoResponse atualizar(Long id, Dtos.AgendamentoRequest request) {
        var agendamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        agendamento.setDataHora(request.dataHora());
        agendamento.setClienteNome(request.clienteNome());
        agendamento.setServico(request.servico());
        agendamento.setStatus(request.status());

        return Dtos.AgendamentoResponse.fromEntity(repository.save(agendamento));
    }

    @Transactional(readOnly = true)
    public List<Dtos.AuditRevisionResponse> buscarHistorico(Long id) {
        // findRevisions retorna as revisões ordenadas por número de revisão
        return repository.findRevisions(id).stream()
                .map(this::toAuditResponse)
                .collect(Collectors.toList());
    }

    private Dtos.AuditRevisionResponse toAuditResponse(Revision<Integer, Agendamento> revision) {
        return new Dtos.AuditRevisionResponse(
                revision.getRequiredRevisionNumber(),
                revision.getMetadata().getRevisionInstant()
                        .map(instant -> instant.atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .orElse(null), // Mantendo null se não houver data registrada
                revision.getMetadata().getRevisionType().name(),
                Dtos.AgendamentoResponse.fromEntity(revision.getEntity())
        );
    }
}