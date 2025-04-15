DROP TABLE IF EXISTS reserva;

CREATE TABLE IF NOT EXISTS reserva (
  codigo VARCHAR(6) PRIMARY KEY,
  data_reserva TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_voo TIMESTAMP,
  milhas_utilizadas INTEGER,
  quantidade_poltronas INTEGER,
  codigo_cliente INTEGER,
  estado VARCHAR(30),
  aeroporto_origem VARCHAR(4),
  aeroporto_destino VARCHAR(4),
  valor NUMERIC(10,2),
  historico JSONB
);
