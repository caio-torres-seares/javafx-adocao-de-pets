-- Criar o banco de dados (se ainda não existir)
CREATE DATABASE adocao_pets;


-- Remover tabelas existentes (em ordem correta para evitar problemas de FK)
DROP TABLE IF EXISTS voluntarios_funcoes;
DROP TABLE IF EXISTS voluntarios;
DROP TABLE IF EXISTS adocoes;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS funcoes_voluntario;
DROP TABLE IF EXISTS usuarios;


-- Criar tabela de usuários
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    voluntario BOOLEAN NOT NULL DEFAULT false
);


-- Criar tabela de funções de voluntário
CREATE TABLE funcoes_voluntario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao TEXT,
    limite_voluntarios INTEGER NOT NULL
);

-- Criar tabela de pets
CREATE TABLE pets (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raca VARCHAR(50),
    idade INTEGER,
    sexo CHAR(1) CHECK (sexo IN ('M', 'F')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('DISPONIVEL', 'INDISPONIVEL', 'ADOTADO')) DEFAULT 'DISPONIVEL'
);

-- Criar tabela de voluntários
CREATE TABLE voluntarios (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL UNIQUE,
    data_cadastro DATE NOT NULL DEFAULT CURRENT_DATE,
    ativo BOOLEAN NOT NULL DEFAULT true,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);


-- Criar tabela de relacionamento entre voluntários e funções
CREATE TABLE voluntarios_funcoes (
    voluntario_id INTEGER NOT NULL,
    funcao_id INTEGER NOT NULL,
    PRIMARY KEY (voluntario_id, funcao_id),
    FOREIGN KEY (voluntario_id) REFERENCES voluntarios(id) ON DELETE CASCADE,
    FOREIGN KEY (funcao_id) REFERENCES funcoes_voluntario(id) ON DELETE CASCADE
);


-- Criar tabela de adoções
CREATE TABLE adocoes (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL,
    pet_id INTEGER NOT NULL UNIQUE, -- Um pet só pode ser adotado uma vez
    data DATE NOT NULL DEFAULT CURRENT_DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE
);


-- Inserir usuários
INSERT INTO usuarios (nome, cpf, voluntario) VALUES 
('João Silva', '111.222.333-44', false),
('Maria Santos', '222.333.444-55', true),
('Carlos Oliveira', '333.444.555-66', false);

-- Inserir funções de voluntário
INSERT INTO funcoes_voluntario (nome, descricao, limite_voluntarios) VALUES 
('Banho', 'Dar banho nos animais', 3),
('Tosa', 'Tosar os animais', 2),
('Passeio', 'Passear com os animais', 5);

-- Inserir pets
INSERT INTO pets (nome, especie, raca, idade, sexo, status) VALUES 
('Rex', 'Cachorro', 'Labrador', 3, 'M', 'DISPONIVEL'),
('Malu', 'Cachorro', 'Pinscher', 2, 'F', 'DISPONIVEL'),
('Minnie', 'Cachorro', 'Pinscher', 3, 'F', 'DISPONIVEL'),
('Nina', 'Cachorro', 'Pinscher', 11, 'F', 'DISPONIVEL'),
('Shoyo', 'Pássaro', 'Agapornis', 2, 'F', 'DISPONIVEL'),
('Tom', 'Gato', 'Siamês', 4, 'M', 'DISPONIVEL'),
('Thor', 'Cachorro', 'Bulldog', 4, 'M', 'INDISPONIVEL');

-- Inserir voluntários (Maria Santos é voluntária)
INSERT INTO voluntarios (usuario_id, data_cadastro, ativo) VALUES 
(2, CURRENT_DATE, true);

-- Associar funções ao voluntário (Maria faz Banho e Passeio)
INSERT INTO voluntarios_funcoes (voluntario_id, funcao_id) VALUES 
(1, 1), -- Banho
(1, 3); -- Passeio

-- Inserir uma adoção (João adotou 5 pets)
INSERT INTO adocoes (usuario_id, pet_id, data) VALUES 
(1, 2, CURRENT_DATE);
(1, 3, CURRENT_DATE);
(1, 4, CURRENT_DATE);
(1, 5, CURRENT_DATE);
(1, 6, CURRENT_DATE);


-- Atualizar status do pet adotado
UPDATE pets SET status = 'ADOTADO' WHERE id = 2;
UPDATE pets SET status = 'ADOTADO' WHERE id = 3;
UPDATE pets SET status = 'ADOTADO' WHERE id = 4;
UPDATE pets SET status = 'ADOTADO' WHERE id = 5;
UPDATE pets SET status = 'ADOTADO' WHERE id = 6;

-- Caso queira listar as tabelas
SELECT * FROM pets;
SELECT * FROM funcoes_voluntario;
SELECT * FROM voluntarios;
SELECT * FROM usuarios;
SELECT * FROM adocoes;
