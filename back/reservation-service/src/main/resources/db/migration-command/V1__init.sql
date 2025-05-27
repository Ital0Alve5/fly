DROP TABLE IF EXISTS historico CASCADE;
DROP TABLE IF EXISTS reserva CASCADE;
DROP TABLE IF EXISTS estado CASCADE;

CREATE TABLE IF NOT EXISTS estado (
  codigo BIGSERIAL PRIMARY KEY,
  nome VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS reserva (
  codigo VARCHAR(6) PRIMARY KEY,
  data_reserva TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valor_pago NUMERIC(10,2),
  milhas_utilizadas INTEGER,
  quantidade_poltronas INTEGER,
  codigo_cliente INTEGER,
  estado BIGINT,
  codigo_voo VARCHAR(8),
  CONSTRAINT fk_estado FOREIGN KEY (estado) REFERENCES estado(codigo)
);

CREATE TABLE IF NOT EXISTS historico (
  id BIGSERIAL PRIMARY KEY,
  codigo_reserva VARCHAR(6),
  data TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado_origem BIGINT,
  estado_destino BIGINT,
  CONSTRAINT fk_codigo_reserva FOREIGN KEY (codigo_reserva) REFERENCES reserva(codigo),
  CONSTRAINT fk_estado_origem FOREIGN KEY (estado_origem) REFERENCES estado(codigo),
  CONSTRAINT fk_estado_destino FOREIGN KEY (estado_destino) REFERENCES estado(codigo)
);

-- Popula os estados
INSERT INTO estado (nome) VALUES 
  ('CRIADA'),
  ('CHECK-IN'),
  ('CANCELADA'),
  ('CANCELADA VOO'),
  ('EMBARCADA'),
  ('REALIZADA'),
  ('NÃO REALIZADA');
