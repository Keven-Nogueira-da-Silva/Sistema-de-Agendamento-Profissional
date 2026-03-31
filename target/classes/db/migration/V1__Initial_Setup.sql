CREATE TABLE usuarios (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          senha VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL
);

CREATE TABLE agendamentos (
                              id BIGSERIAL PRIMARY KEY,
                              data_hora TIMESTAMP NOT NULL,
                              cliente_nome VARCHAR(100) NOT NULL,
                              servico VARCHAR(100) NOT NULL,
                              status VARCHAR(20) NOT NULL,
                              created_by VARCHAR(100),
                              last_modified_by VARCHAR(100),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabelas de Auditoria (Hibernate Envers)
CREATE TABLE revinfo (
                         rev INTEGER PRIMARY KEY,
                         revtstmp BIGINT
);

-- Sequência para controle de revisões
CREATE SEQUENCE revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE usuarios_aud (
                              id BIGINT NOT NULL,
                              rev INTEGER NOT NULL,
                              revtype SMALLINT,
                              nome VARCHAR(100),
                              email VARCHAR(100),
                              senha VARCHAR(255),
                              role VARCHAR(20),
                              PRIMARY KEY (id, rev),
                              CONSTRAINT fk_usuarios_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE agendamentos_aud (
                                  id BIGINT NOT NULL,
                                  rev INTEGER NOT NULL,
                                  revtype SMALLINT,
                                  data_hora TIMESTAMP,
                                  cliente_nome VARCHAR(100),
                                  servico VARCHAR(100),
                                  status VARCHAR(20),
                                  created_by VARCHAR(100),
                                  last_modified_by VARCHAR(100),
    -- AS COLUNAS ABAIXO ERAM AS QUE FALTAVAM:
                                  created_at TIMESTAMP,
                                  last_modified_at TIMESTAMP,
                                  PRIMARY KEY (id, rev),
                                  CONSTRAINT fk_agendamentos_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);