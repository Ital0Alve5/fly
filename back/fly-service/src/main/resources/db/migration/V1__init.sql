DROP TABLE IF EXISTS voos CASCADE;

DROP TABLE IF EXISTS aeroportos CASCADE;

CREATE TABLE IF NOT EXISTS aeroportos (
  codigo VARCHAR(4) PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  cidade VARCHAR(100) NOT NULL,
  uf CHAR(2) NOT NULL
);

CREATE TABLE IF NOT EXISTS voos (
  codigo VARCHAR(6) PRIMARY KEY,
  data TIMESTAMP NOT NULL,
  valor_passagem NUMERIC(10,2),
  quantidade_poltronas_total INTEGER,
  quantidade_poltronas_ocupadas INTEGER,
  estado VARCHAR(30),
  aeroporto_origem VARCHAR(4),
  aeroporto_destino VARCHAR(4),
  CONSTRAINT fk_voo_origem FOREIGN KEY (aeroporto_origem) REFERENCES aeroportos(codigo),
  CONSTRAINT fk_voo_destino FOREIGN KEY (aeroporto_destino) REFERENCES aeroportos(codigo)
);

INSERT INTO aeroportos (codigo, nome, cidade, uf) VALUES
('GRU', 'Aeroporto Internacional de São Paulo/Guarulhos', 'Guarulhos', 'SP'),
('GIG', 'Aeroporto Internacional do Rio de Janeiro/Galeão', 'Rio de Janeiro', 'RJ'),
('CWB', 'Aeroporto Internacional de Curitiba', 'Curitiba', 'PR'),
('POA', 'Aeroporto Internacional Salgado Filho', 'Porto Alegre', 'RS');

INSERT INTO voos (codigo, data, valor_passagem, quantidade_poltronas_total, quantidade_poltronas_ocupadas, estado, aeroporto_origem, aeroporto_destino) VALUES
('FLW234', '2025-08-10T10:30:00-03:00', 550.00, 180, 50, 'CONFIRMADO', 'POA', 'CWB'),
('FLW235', '2025-09-11T09:30:00-03:00', 620.00, 180, 90, 'CONFIRMADO', 'CWB', 'GIG'),
('FLW236', '2025-10-12T08:30:00-03:00', 480.00, 180, 40, 'CONFIRMADO', 'CWB', 'POA');
