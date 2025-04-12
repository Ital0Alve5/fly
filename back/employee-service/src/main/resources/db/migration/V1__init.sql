DROP TABLE IF EXISTS funcionario;

CREATE TABLE IF NOT EXISTS funcionario (
  codigo SERIAL PRIMARY KEY,
  cpf VARCHAR(14) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  nome VARCHAR(100) NOT NULL,
  telefone VARCHAR(15) -- (xx) xxxxx-xxxx
);

INSERT INTO funcionario (cpf, email, nome, telefone)
VALUES ('907.692.810-01', 'func_pre@gmail.com', 'Razer', '(41) 91234-5678');
