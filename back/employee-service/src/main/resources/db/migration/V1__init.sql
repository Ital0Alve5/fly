DROP TABLE IF EXISTS funcionario;

CREATE TABLE IF NOT EXISTS funcionario (
  codigo BIGSERIAL PRIMARY KEY,
  cpf VARCHAR(14) NOT NULL,
  email VARCHAR(100) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  telefone VARCHAR(15), -- (xx) xxxxx-xxxx
  deleted_at TIMESTAMP WITH TIME ZONE
);

INSERT INTO funcionario (cpf, email, nome, telefone)
VALUES ('90769281001', 'func_pre@gmail.com', 'Razer', '(41) 91234-5678');
