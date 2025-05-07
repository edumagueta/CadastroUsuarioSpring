# Cadastro de Usuários - Spring Boot API

Este projeto é uma API RESTful simples desenvolvida com **Spring Boot**, com o objetivo de gerenciar um cadastro de usuários. Ele faz parte de um estudo prático sobre criação de APIs, testes automatizados e boas práticas em desenvolvimento backend com Java.

## ✨ Funcionalidades

- Cadastro de usuários (`POST /customers`)
- Consulta de usuários por nome (`GET /customers/customer?nome=...`)
- Listagem de todos os usuários (`GET /customers`)
- Exclusão de usuários por nome (`DELETE /customers?nome=...`)

## 🔧 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- Banco de Dados H2
- JUnit 5
- Mockito
- MockMvc
- Insomnia (testes manuais via requisições HTTP)

## 🧪 Testes Automatizados

O projeto foi testado com três níveis de testes:

- ✅ **Testes Unitários**: cobrem a lógica da `UsuarioService`.
- ✅ **Testes de Integração e Componente**: realizados com `@SpringBootTest` usando banco H2 e verificando comportamento do `UsuarioController`.