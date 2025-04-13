DROP TABLE IF EXISTS transacoes CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS endereco CASCADE;

CREATE TABLE IF NOT EXISTS endereco (
  id BIGSERIAL PRIMARY KEY,
  cep VARCHAR(10),
  uf VARCHAR(2),
  cidade VARCHAR(100),
  bairro VARCHAR(100),
  rua VARCHAR(100),
  numero VARCHAR(10),
  complemento VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS cliente (
  codigo BIGSERIAL PRIMARY KEY,
  endereco_id BIGINT,
  cpf VARCHAR(14) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  nome VARCHAR(100) NOT NULL,
  saldo_milhas INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transacoes (
  id BIGSERIAL PRIMARY KEY,
  codigo_cliente BIGINT,
  data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valor_reais NUMERIC(10,2),
  quantidade_milhas INTEGER,
  descricao TEXT,
  codigo_reserva VARCHAR(50),
  tipo VARCHAR(20)
);

ALTER TABLE cliente
ADD CONSTRAINT fk_endereco
FOREIGN KEY (endereco_id)
REFERENCES endereco(id)
ON DELETE SET NULL;

ALTER TABLE transacoes
ADD CONSTRAINT fk_cliente
FOREIGN KEY (codigo_cliente)
REFERENCES cliente(codigo)
ON DELETE CASCADE;

INSERT INTO endereco (cep, uf, cidade, bairro, rua, numero, complemento)
VALUES (
  '82590-300',
  'PR',
  'Curitiba',
  'Boa Vista',
  'Rua Fulano de Tal',
  '123',
  'Casa 2'
);


INSERT INTO cliente (endereco_id, cpf, email, nome, saldo_milhas)
VALUES (
  1,
  '90769281001',
  'heitor@gmail.com',
  'Heitor',
  0
);
