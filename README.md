# SyncoTable

[![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)

Aplicação web para gerenciamento de dados tabulares e cartões, com importação e exportação de planilhas.

## Stack

- Java 17
- Spring Boot 3.5.7 (Web, Security, Data JPA, Validation)
- Thymeleaf
- PostgreSQL (padrão) e H2 (opcional)
- MapStruct, Lombok
- Apache POI, Commons CSV
- Maven

## Padrões e princípios

- MVC (Controllers + Thymeleaf views)
- Service Layer
- Repository (Spring Data JPA)
- DTO + Mapper (MapStruct)
- Factory e Strategy para leitores de planilha (CSV/XLS/XLSX)
- Injeção de dependências (IoC do Spring)
- SOLID aplicado por separação de responsabilidades e uso de interfaces

## Como rodar

Pré-requisitos: Java 17, acesso a um PostgreSQL local.

1. Crie o banco:

```sql
CREATE DATABASE syncotable;
```

2. Ajuste credenciais em [src/main/resources/application.properties](src/main/resources/application.properties) se necessário.
3. Suba a aplicação:

```bash
./mvnw spring-boot:run
```

4. Acesse: http://localhost:5012

Usuário padrão (configurado em properties): `admin` / `admin`.

## Build

```bash
./mvnw clean package
java -jar target/SyncoTable-0.0.1.jar
```
