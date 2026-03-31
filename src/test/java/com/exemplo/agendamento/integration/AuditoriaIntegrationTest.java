package com.exemplo.agendamento.integration;

import com.exemplo.agendamento.entity.Agendamento;
import com.exemplo.agendamento.entity.Usuario;
import com.exemplo.agendamento.repository.AgendamentoRepository;
import com.exemplo.agendamento.repository.UserRepositories;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@RequiredArgsConstructor
public class AuditoriaIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");


    private final AgendamentoRepository agendamentoRepository;


    private final UserRepositories usuarioRepository;

    @BeforeEach
    void setup() {
        agendamentoRepository.deleteAll();
        usuarioRepository.deleteAll();
        
        // Simular usuário logado para o Spring Security / AuditorAware
        Usuario admin = Usuario.builder()
                .nome("Admin Teste")
                .email("admin@teste.com")
                .senha("senha")
                .role(Usuario.Role.ADMIN)
                .build();
        usuarioRepository.save(admin);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                admin.getEmail(), null, admin.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void deveSalvarAuditoriaAoCriarEAtualizarAgendamento() {
        // 1. Criar agendamento
        Agendamento agendamento = Agendamento.builder()
                .clienteNome("Cliente Teste")
                .servico("Consultoria")
                .dataHora(LocalDateTime.now())
                .status(Agendamento.Status.PENDENTE)
                .build();

        Agendamento salvo = agendamentoRepository.save(agendamento);
        
        // 2. Verificar auditoria de criação (Spring Data JPA Auditing)
        Assertions.assertEquals("admin@teste.com", salvo.getCreatedBy());
        Assertions.assertNotNull(salvo.getCreatedAt());

        // 3. Atualizar agendamento
        salvo.setStatus(Agendamento.Status.CONCLUIDO);
        agendamentoRepository.save(salvo);

        // 4. Verificar histórico de revisões (Hibernate Envers)
        var revisoes = agendamentoRepository.findRevisions(salvo.getId()).getContent();
        
        Assertions.assertTrue(revisoes.size() >= 2, "Deve haver pelo menos 2 revisões no histórico");
        Assertions.assertEquals(Agendamento.Status.PENDENTE, revisoes.get(0).getEntity().getStatus());
        Assertions.assertEquals(Agendamento.Status.CONCLUIDO, revisoes.get(1).getEntity().getStatus());
    }
}
