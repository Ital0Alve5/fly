# Trabalho DAC - Arquitetura de Microsserviços

Este projeto foi construído utilizando uma arquitetura de serviços distribuídos, todos gerenciados por contêineres com docker-compose, a fim de facilitar o uso por parte do front-end.

## Como executar o projeto

Para executar o projeto, certifique-se de que o Docker e o Docker Compose estejam instalados e devidamente configurados. Em seguida, abra um terminal na raiz do repositório e execute o comando abaixo:

```bash
docker-compose up
```

## Microsserviços

A seguir, a lista dos microsserviços gerenciados e suas respectivas portas:

- **API Gateway**: exposto na porta 8000; é o serviço principal para interface com o back-end.
- **Auth Service (MS Auth)**: exposto na porta 8080.
- **Fly Service (MS Voos)**: exposto na porta 8081.
- **Client Service (MS Cliente)**: exposto na porta 8082.
- **Employee Service (MS Funcionário)**: exposto na porta 8083.
- **Reservation Service (MS Reserva)**: exposto na porta 8084.
- **SAGA**: responsável por orquestrar transações distribuídas; exposto na porta 8085.

## Bancos de Dados

Foram criados dois serviços de banco de dados para esta arquitetura: um SGBD Postgres, que abriga todos os bancos relacionais, e um MongoDB para o serviço de autenticação.

- **Bancos relacionais**:  
  - *fly_service*, *employee_service* e *client_service*, utilizados pelos microsserviços de Voos, Funcionário e Cliente, respectivamente.  
  - Para o MS Reserva, foram criados os bancos *reservation_service_query* e *reservation_service_command*, seguindo o padrão de consulta/comando do modelo CQRS.  
  - Estes bancos estão expostos na porta 5432.

- **Banco não relacional**:  
  - MongoDB para o MS Auth, exposto na porta 27017.

## Mensageria

Para a comunicação assíncrona entre os serviços da arquitetura, foi criado um serviço de RabbitMQ. Este serviço está exposto na porta 5672 para conexão com os microsserviços, e a porta 15672 disponibiliza a interface de gerenciamento do RabbitMQ.

---