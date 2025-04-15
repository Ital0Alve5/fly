DROP TABLE IF EXISTS estado CASCADE;

DROP TABLE IF EXISTS reserva CASCADE;

DROP TABLE IF EXISTS historico CASCADE;

CREATE TABLE IF NOT EXISTS estado (
  codigo BIGSERIAL PRIMARY KEY,
  nome VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS reserva (
  codigo VARCHAR(6) PRIMARY KEY,
  data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valor NUMERIC(10,2),
  milhas_utilizadas INTEGER,
  quantidade_poltronas INTEGER,
  codigo_cliente INTEGER,
  estado BIGINT,
  codigo_voo VARCHAR(6),
  CONSTRAINT fk_estado FOREIGN KEY (estado) REFERENCES estado(codigo)
);

CREATE TABLE IF NOT EXISTS historico (
  id BIGSERIAL PRIMARY KEY,
  codigo_reserva VARCHAR(6),
  data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado_origem BIGINT,
  estado_destino BIGINT,
  CONSTRAINT fk_codigo_reserva FOREIGN KEY (codigo_reserva) REFERENCES reserva(codigo),
  CONSTRAINT fk_estado_origem FOREIGN KEY (estado_origem) REFERENCES estado(codigo),
  CONSTRAINT fk_estado_destino FOREIGN KEY (estado_destino) REFERENCES estado(codigo)
);

