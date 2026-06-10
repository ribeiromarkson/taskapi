# Task API

API RESTful desenvolvida com Spring Boot para gerenciamento de tarefas.

## Descrição

O projeto é um microserviço para cadastro e gerenciamento de tarefas. A aplicação inclui a criação de categorias, cadastro de tarefas, listar registros, buscar por ID, filtrar tarefas por status, atualizar e deletar tarefas.

A entidade principal é `Tarefa`, que tem título, descrição, status, data de entrega e categoria. A entidade `Categoria` se relaciona com `Tarefa`, o que permite agrupar várias tarefas dentro da mesma categoria.

## Tecnologia

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- PostgreSQL Driver
- Springdoc OpenAPI / Swagger
- Maven
- Git e GitHub

## Arquitetura Interna

Eu fiz o projeto/trabalho separado em:

```text
controller  -> recebe as requisições HTTP
service     -> concentra as regras de negócio
repository  -> realiza a comunicação com o banco de dados
model       -> representa as entidades JPA
dto         -> controla os dados de entrada e saída da API
exception   -> centraliza o tratamento de erros
```
## Deploy's

API publicada no Render:
https://taskapi-9o37.onrender.com

Swagger online:
https://taskapi-9o37.onrender.com/swagger-ui/index.html

Documentação OpenAPI:
https://taskapi-9o37.onrender.com/v3/api-docs