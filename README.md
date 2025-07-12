# Sistema de Adoção de Pets

Este é um sistema desktop desenvolvido em Java com JavaFX para gerenciar adoção de pets, cadastro de usuários, voluntários, funções, além de relatórios e gráficos. O projeto foi desenvolvido como parte de um trabalho de faculdade, em parceria entre **Caio Torres Seares** e **Gabriela Benevides Pereira Marques**.

## Sumário

- [Descrição](#descrição)
- [Funcionalidades](#funcionalidades)
- [Requisitos](#requisitos)
- [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
- [Como Executar](#como-executar)
- [Autores](#autores)

---

## Descrição

O sistema tem como objetivo facilitar o gerenciamento de adoções de animais, permitindo o cadastro de pets, usuários, voluntários e suas funções, além de controlar processos de adoção, emitir relatórios e exibir gráficos estatísticos. O sistema também possui integração com banco de dados PostgreSQL e recursos de logs e comunicação via sockets.

---

## Funcionalidades

- **Cadastro de Usuários:** Gerencie informações de pessoas interessadas em adotar ou atuar como voluntárias.
- **Cadastro de Pets:** Registre animais disponíveis, com informações como espécie, raça, idade, sexo e status (disponível, indisponível, adotado).
- **Cadastro de Voluntários e Funções:** Associe usuários a funções voluntárias, com controle de limite por função.
- **Processo de Adoção:** Controle adoções, garantindo regras de negócio (ex: limite de 5 pets por usuário).
- **Relatórios:** Gere relatórios de pets disponíveis/adotados e voluntários por função.
- **Gráficos:** Visualize estatísticas de adoções e voluntariado por mês.
- **Logs e Sockets:** Visualize logs de operações e ranking de grupos via comunicação socket.
- **Interface Gráfica Moderna:** Desenvolvida com JavaFX, menus intuitivos e navegação por telas.

---

## Requisitos

- **Java JDK:** Versão 8 ou superior (recomendado 8 para compatibilidade total com JavaFX nativo).
- **JavaFX:** Já incluso no JDK 8. Para versões superiores, pode ser necessário instalar o JavaFX SDK separadamente.
- **PostgreSQL:** Versão 9.5 ou superior.
- **Ant:** Para compilação e execução via linha de comando (ou utilize o NetBeans).
- **NetBeans (opcional):** Projeto compatível com NetBeans, facilitando o build e execução.

As bibliotecas necessárias já estão incluídas na pasta `libs/` do projeto (JasperReports, PostgreSQL JDBC, Apache Commons, etc).

---

## Configuração do Banco de Dados

1. **Instale o PostgreSQL** e crie um usuário com permissão para criar bancos de dados.
2. **Execute o script `SQL-adocao_pets.sql`** (raiz do projeto) para criar e popular o banco de dados:
   - Você pode usar o pgAdmin, DBeaver ou o terminal:
     ```sh
     psql -U postgres -f SQL-adocao_pets.sql
     ```
   - O banco será criado com o nome `adocao_pets` e já virá com dados de exemplo.

3. **Configuração de conexão:**
   - O sistema conecta por padrão em:
     ```
     jdbc:postgresql://127.0.0.1/adocao_pets
     Usuário: postgres
     Senha: postgres
     ```
   - Se necessário, altere o usuário/senha em `src/adocaopets/model/database/DatabasePostgreSQL.java`.

---

## Como Executar

### Via NetBeans

1. **Abra o projeto** no NetBeans (`Arquivo > Abrir Projeto`).
2. **Garanta que o JDK 8+ está configurado** como plataforma do projeto.
3. **Clique em "Executar Projeto"** (ícone de play ou F6).

### Via Linha de Comando (Ant)

1. **Abra o terminal na raiz do projeto.**
2. **Compile o projeto:**
   ```sh
   ant clean
   ant jar
   ```
3. **Execute o projeto:**
   ```sh
   ant run
   ```
   - O Ant utilizará o arquivo `build.xml` e as configurações do NetBeans.

---

## Estrutura do Projeto

- `src/adocaopets/` - Código-fonte principal (controllers, models, DAOs, views).
- `libs/` - Bibliotecas externas necessárias.
- `SQL-adocao_pets.sql` - Script para criação e popularização do banco de dados.
- `build.xml` - Script de build do Ant.
- `manifest.mf` - Manifesto do projeto.

---

## Autores

- **Caio Torres Seares**
- **Gabriela Benevides Pereira Marques**

Projeto desenvolvido para fins acadêmicos, com foco em aprendizado de POO, JavaFX, JDBC, relatórios e integração com banco de dados.

---

Se tiver dúvidas ou sugestões, entre em contato com os autores! 