DROP TABLE IF EXISTS reserva;

CREATE TABLE IF NOT EXISTS reserva (
  codigo VARCHAR(6) PRIMARY KEY,
  data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valor NUMERIC(10,2),
  milhas_utilizadas INTEGER,
  quantidade_poltronas INTEGER,
  codigo_cliente INTEGER,
  estado VARCHAR(30),
  codigo_voo VARCHAR(6)
);
