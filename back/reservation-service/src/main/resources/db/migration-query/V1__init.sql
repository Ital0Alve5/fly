DROP TABLE IF EXISTS reserva;

CREATE TABLE IF NOT EXISTS reserva (
  codigo VARCHAR(6) PRIMARY KEY,
  data_reserva TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  milhas_utilizadas INTEGER,
  quantidade_poltronas INTEGER,
  codigo_cliente INTEGER,
  estado VARCHAR(30),
  codigo_voo VARCHAR(8),
  aeroporto_origem VARCHAR(4),
  aeroporto_destino VARCHAR(4),
  valor NUMERIC(10,2),
  historico TEXT
);
