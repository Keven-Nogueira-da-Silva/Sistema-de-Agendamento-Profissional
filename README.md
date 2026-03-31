# Sistema de Agendamento Profissional

Este projeto é um **Sistema de Agendamento Profissional** desenvolvido com **Spring Boot 3.4.0** e **Java 17**, com foco na implementação de funcionalidades de nível Pleno/Sênior, como segurança, auditoria e testes de integração realistas.

---

## 🚀 Requisitos Técnicos (Stack)

* **Java 17 + Maven**

* **Spring Security + JWT**
  Implementação de RBAC (Role-Based Access Control) com as roles:

  * ADMIN
  * SECRETARIA
  * CLIENTE

* **Hibernate Envers**
  Auditoria automática nas entidades utilizando `@Audited`

* **Banco de Dados**
  PostgreSQL com versionamento via Flyway

* **Lombok**
  Redução de código boilerplate

* **Testcontainers**
  Testes de integração utilizando PostgreSQL real em container

---

## 📌 Entidades e Regras de Negócio

### Usuario

* id
* nome
* email
* senha
* role

### Agendamento

* id
* dataHora
* clienteNome
* servico
* status (PENDENTE, CONCLUIDO, CANCELADO)

Inclui:

* `@CreatedBy`
* `@LastModifiedBy`
  Integrados ao Spring Security

### Auditoria

* Implementação de `AuditorAware`
* Captura automaticamente o usuário autenticado via JWT
* Registra quem realizou cada alteração

---

## 🧱 Estrutura de Código

### Security

* `JwtService`
* `JwtAuthenticationFilter`
* `SecurityConfig` (API Stateless)

### Controllers

* Login
* Cadastro de Agendamento
* Consulta de auditoria (exclusivo ADMIN, usando `AuditReader`)

### DTOs

* Utilização de **Java Records** para entrada e saída de dados

### Tratamento de Erros

* `GlobalExceptionHandler` para exceções de segurança e validação

---

## 🧠 Decisões de Arquitetura

### 📊 Hibernate Envers para Auditoria

O Hibernate Envers foi escolhido por permitir auditoria automática e completa das entidades.

* Cria tabelas `_aud` automaticamente
* Registra cada alteração com histórico completo
* Permite rastreabilidade total dos dados
* Integração com `AuditorAware` adiciona:

  * Quem fez a alteração (via JWT)

Ideal para:

* Sistemas auditáveis
* Compliance
* Histórico e versionamento de dados

---

### 🔐 RBAC com Spring Security + JWT

Controle de acesso baseado em papéis:

* **ADMIN**

  * Acesso total
  * Consulta auditoria

* **SECRETARIA**

  * Criar e atualizar agendamentos

* **CLIENTE**

  * Visualizar seus próprios agendamentos (extensível)

#### Vantagens do JWT:

* API Stateless
* Escalabilidade
* Segurança reforçada
* Sem necessidade de sessão no servidor

---

## ▶️ Como Rodar o Projeto

### 1. Pré-requisitos

* Java 17
* Maven
* Docker

---

### 2. Banco de Dados

Certifique-se de que o Docker está rodando.

O Flyway executará automaticamente as migrações ao iniciar o projeto.

---

### 3. Executar a Aplicação

```bash
mvn spring-boot:run
```

---

### 4. Testar Auditoria

* Realize um `PUT` em um agendamento (com autenticação)
* Verifique a tabela:

```
agendamentos_aud
```

* Confirme:

  * Alteração registrada
  * Usuário responsável

---

### 5. Executar Testes

```bash
mvn test
```

---

## 🎯 Conclusão

Este projeto demonstra a construção de uma aplicação robusta, focada em:

* Segurança
* Auditoria completa
* Boas práticas de arquitetura
* Testes realistas

Mais do que funcional, o sistema foi pensado para ser **escalável, seguro e preparado para cenários reais de produção**.
