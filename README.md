Sistema de Agendamento Profissional

Este projeto é um Sistema de Agendamento Profissional desenvolvido com Spring Boot 3.4.0 e Java 17, demonstrando a implementação de funcionalidades de segurança, auditoria e testes de nível Pleno/Sênior.

Requisitos Técnicos (Stack)

•
Java 17 e Maven

•
Spring Security + JWT: Implementação de RBAC (Role-Based Access Control) com as roles: ADMIN, SECRETARIA e CLIENTE.

•
Hibernate Envers: Configuração de auditoria automática em todas as entidades principais usando @Audited.

•
Banco de Dados: PostgreSQL com Flyway para migrações.

•
Lombok: Para redução de boilerplate.

•
Testcontainers: Exemplo de teste de integração que utiliza um container PostgreSQL real para validar a auditoria.

Entidades e Regras de Negócio

•
Usuario: id, nome, email, senha, role.

•
Agendamento: id, dataHora, clienteNome, servico, status (PENDENTE, CONCLUIDO, CANCELADO). Contém @CreatedBy e @LastModifiedBy integrados ao Spring Security.

•
Auditoria: AuditorAware implementado para capturar o usuário logado no JWT e salvar nas tabelas de auditoria.

Estrutura de Código

•
Security: JwtService, JwtAuthenticationFilter, e SecurityConfig (estratégia Stateless).

•
Controllers: Endpoints para Login, Cadastro de Agendamento, e um endpoint exclusivo para o ADMIN consultar o histórico de revisões de um agendamento (usando AuditReader).

•
DTOs: Utilização de Java Records para todas as entradas e saídas da API.

•
Tratamento de Erros: GlobalExceptionHandler para lidar com exceções de segurança e validação.

Decisões de Arquitetura

Hibernate Envers para Auditoria

A escolha do Hibernate Envers para a funcionalidade de auditoria foi motivada por sua capacidade de fornecer um histórico completo e automático de todas as alterações realizadas nas entidades do sistema. Ao anotar as entidades com @Audited, o Envers cria e mantém tabelas de auditoria (_aud) para cada entidade auditada, registrando o estado da entidade em cada revisão. Isso é crucial para sistemas que exigem rastreabilidade de dados, conformidade regulatória ou a capacidade de reverter a estados anteriores. A integração com o Spring Data JPA Auditing, através do AuditorAware, permite que o sistema capture automaticamente o usuário responsável pela alteração (obtido do contexto de segurança JWT), enriquecendo os registros de auditoria com informações sobre
quem realizou a modificação.

Controle de Acesso Baseado em Papéis (RBAC) com Spring Security e JWT

O Controle de Acesso Baseado em Papéis (RBAC), implementado com Spring Security e JWT (JSON Web Tokens), é fundamental para proteger os dados sensíveis e garantir que apenas usuários autorizados possam acessar recursos específicos. As roles ADMIN, SECRETARIA e CLIENTE definem hierarquias de permissão, onde:

•
ADMIN: Possui acesso total, incluindo a consulta do histórico de revisões de agendamentos.

•
SECRETARIA: Pode criar e atualizar agendamentos.

•
CLIENTE: Pode visualizar seus próprios agendamentos (não implementado neste escopo, mas facilmente extensível).

O uso de JWTs garante que as sessões sejam stateless, o que é ideal para APIs RESTful, pois não há necessidade de manter o estado da sessão no servidor. Cada requisição autenticada carrega o token, que é validado para verificar a identidade e as permissões do usuário. Isso não só melhora a escalabilidade da aplicação, mas também a segurança, minimizando a superfície de ataque associada a sessões baseadas em cookies.

Como Rodar o Projeto

1.
Pré-requisitos:

•
Java 17

•
Maven

•
Docker (para Testcontainers e PostgreSQL)



2.
Configuração do Banco de Dados:
Certifique-se de que o Docker esteja rodando. O Flyway cuidará das migrações do banco de dados ao iniciar a aplicação.

3.
Executar a Aplicação:

Bash


mvn spring-boot:run





4.
Testar a Auditoria:

•
Faça um PUT em um agendamento (requer autenticação).

•
Consulte a tabela agendamentos_aud no seu banco de dados para verificar o registro da alteração e quem a fez.



5.
Executar Testes de Integração:

Bash


mvn test





