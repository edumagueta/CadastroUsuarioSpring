# Cadastro de Usuários

API REST com Spring Boot para cadastrar, consultar e excluir usuários. O cadastro recebe dados básicos e solicita enriquecimento em background via Kafka.

## Funcionalidades

- `POST /customers`: recebe nome, email e data de nascimento, salva o usuário com uma publicação pendente e retorna `201 Created`. Data de nascimento ausente ou futura retorna `400 Bad Request`.
- `GET /customers`: lista usuários ou filtra por `nome`, `email` ou `documento`. O filtro por nome ignora maiúsculas e minúsculas.
- `DELETE /customers?email=...`: exclui pelo email.
- Email é único; uma tentativa duplicada retorna `409 Conflict`.
- A resposta pública contém ID, nome, email e data de nascimento. CPF, regularidade e score ficam internos.
- A idade é calculada a partir da data de nascimento quando consultada no modelo. Os filtros por nome e CPF retornam listas, pois várias contas podem ter o mesmo nome ou CPF.

## Enriquecimento com Kafka

O produtor verifica as publicações pendentes em background e publica o ID no tópico `usuario-enriquecimento`. A publicação permanece pendente até a confirmação do Kafka. Se o envio falhar, a aplicação tenta novamente. O consumidor consulta as integrações e persiste CPF, indicador de regularidade e score.

O Docker Compose sobe um broker Kafka local em modo KRaft e o serviço `kafka-init` cria o tópico assim que o broker estiver pronto. O Kafka usa `localhost:9092` no Windows e `kafka:29092` na rede do Compose.

## Integrações mockadas

Os clientes HTTP usam `FEDERAL_MANAGER_BASE_URL` e `CREDIT_ANALYSIS_BASE_URL` (padrão `http://localhost:8089`). O Compose inicia o WireMock com respostas fixas em `wiremock/mappings`. Os testes iniciam sua própria instância do WireMock.

- `GET /federal-manager/{name}` retorna `numero_cpf` e `situacao_cadastral`.
- `GET /credit-analysis/person?document={cpf}` retorna `document`, `creditScore` e `rating`.

Os endpoints e campos seguem a proposta do antigo `apis.py`. Para a integração real, confirme com os sistemas responsáveis os contratos e um identificador confiável para a consulta federal, pois pessoas diferentes podem ter o mesmo nome.

## Executar localmente

Abra o Docker Desktop e execute os comandos na raiz do projeto.

### Kafka e WireMock no Docker, aplicação no Windows

Com Java 21 instalado, execute no PowerShell:

```powershell
docker compose up -d kafka-init wiremock
docker compose logs kafka-init
.\gradlew.bat bootRun
```

O log de `kafka-init` mostra a criação do tópico na primeira execução. Para confirmar que ele existe:

```powershell
docker compose exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:29092 --describe --topic usuario-enriquecimento
```

Para subir somente o Kafka com o tópico, use `docker compose up -d kafka-init`.

### Tudo no Docker

```powershell
docker compose up -d --build
docker compose logs -f app
```

O Dockerfile usa Java 21 e gera o JAR com o Gradle Wrapper. Nessa opção, o Java roda dentro do contêiner e a API fica em `http://localhost:8080`.

### Cadastro de exemplo

Use o nome `Eduardo` para corresponder ao cenário fixo do WireMock:

```powershell
$cadastro = @{ nome = 'Eduardo'; email = 'eduardo@example.com'; dataNascimento = '1990-05-20' } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:8080/customers -ContentType 'application/json' -Body $cadastro
```

O cadastro retorna HTTP 201 e o consumidor processa o enriquecimento em seguida. O CPF, a regularidade e o score ficam no banco.

Para parar os serviços, use `docker compose down`. Essa configuração é local e o comando remove os dados do Kafka junto com o contêiner. O banco H2 também é em memória e perde os dados quando a aplicação para.

### Testes

```powershell
.\gradlew.bat test
```

## Swagger / OpenAPI

Com a aplicação em execução, acesse `/swagger-ui.html` ou `/swagger-ui/index.html`. O documento OpenAPI fica em `/v3/api-docs`.
