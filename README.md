# Desafio do Banco verde 

## Descrição

Este projeto é uma aplicação Java utilizando Spring Boot e Maven. A aplicação simula um sistema de pagamentos, onde usuários podem realizar transferências e consultar o histórico de transações.

## Tecnologias Utilizadas

- Java
- Spring Boot
- Maven
- PostgreSQL
- Flyway

## Instalação

Siga os passos abaixo para configurar e executar a aplicação:

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/henriquernandes/PicPay.git
   cd PicPay
   ```

2. **Configure o banco de dados:**

   Certifique-se de ter um banco de dados SQL configurado e atualize as propriedades de conexão no arquivo `application.properties`.

3. **Compile e execute a aplicação pela sua IDE ou através do cli `mvn`:**
## Endpoints

### Autenticação

- **Registrar Usuário:**
    - **URL:** `/auth/signup`
    - **Método:** `POST`
    - **Descrição:** Registra um novo usuário.
    - **Corpo da Requisição:**
      ```json
      {
        "firstName": "string",
        "lastName": "string",
        "cpfCnpj": "string",
        "email": "string",
        "password": "string",
        "balance": "number",
        "type": "CONSUMER | SHOPKEEPER"
      }
      ```

- **Login:**
    - **URL:** `/auth/login`
    - **Método:** `POST`
    - **Descrição:** Autentica um usuário.
    - **Corpo da Requisição:**
      ```json
      {
        "email": "string",
        "password": "string"
      }
      ```

### Usuário

- **Obter Usuário Autenticado:**
    - **URL:** `/users/me`
    - **Método:** `GET`
    - **Descrição:** Retorna os dados do usuário autenticado.

### Transferências

- **Realizar Transferência:**
    - **URL:** `/transfer`
    - **Método:** `POST`
    - **Descrição:** Realiza uma transferência entre usuários.
    - **Corpo da Requisição:**
      ```json
      {
        "payeeCpfCnpj": "string",
        "value": "number"
      }
      ```

- **Histórico de Transferências:**
    - **URL:** `/transfer/history`
    - **Método:** `GET`
    - **Descrição:** Retorna o histórico de transferências do usuário autenticado.
