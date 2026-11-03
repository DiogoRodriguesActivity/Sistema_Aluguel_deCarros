# Sistema de Aluguel de Carros

Sistema web para apoio à gestão de aluguéis de automóveis. Clientes podem se cadastrar, fazer login e criar, consultar, modificar e cancelar pedidos de aluguel pela Internet. Agentes (empresas e bancos) consultam e avaliam os pedidos e podem associar contratos de crédito a eles.

Projeto desenvolvido para o **Laboratório 02** da disciplina **Desenvolvimento e Integração de Aplicações Web (DIAW)**, do curso de Engenharia de Software da PUC Minas (professor João Paulo Carneiro Aramuni).

## Integrantes

- Diogo

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java (JDK 25) |
| Back-end | Spring Boot 4.0.x, arquitetura MVC |
| Build e dependências | Apache Maven |
| Front-end | React + Vite |
| Banco de dados | Supabase (PostgreSQL) |
| Autenticação | Login com usuário (e-mail) e senha, Spring Security + JWT |
| Modelagem | UML (PlantUML): casos de uso, classes e pacotes |
| Protótipo de telas | [Figma](https://www.figma.com/design/WZCUWD0zUlNN2NJHbErRNI/Sistema_Aluguel_deCarros?node-id=5-18761&t=4CbvglZeYtGwkkou-1) |

## Funcionalidades

### Cliente
- Cadastro e login.
- Consulta e alteração dos próprios dados: RG, CPF, nome, endereço e profissão.
- Cadastro de até 3 empregadores com os respectivos rendimentos.
- Criação, consulta, alteração e cancelamento de pedidos de aluguel, com acompanhamento do status.

### Agente (empresa ou banco)
- Consulta de clientes e de pedidos.
- Cadastro, alteração e exclusão de automóveis.
- Avaliação financeira dos pedidos e associação de contratos de crédito (em desenvolvimento, veja o [estado do projeto](#estado-do-projeto)).

### Automóveis
Registro de matrícula, ano, marca, modelo e placa. O proprietário pode ser um cliente, uma empresa ou um banco.

## Estado do projeto

| Item | Situação |
|---|---|
| Levantamento de requisitos, casos de uso e diagrama de classes | Concluído |
| Protótipo das telas no Figma | Concluído |
| Banco de dados (Supabase) | Concluído |
| Back-end: entidades e repositórios | Concluído |
| Back-end: CRUD de clientes | Concluído |
| Back-end: login com JWT e controle de acesso por papel | Implementado |
| Back-end: automóveis e pedidos (criar, consultar, alterar, cancelar) | Implementado |
| Front-end: configuração (React + Vite) e tela de cadastro | Concluído |
| Front-end: login, pedidos e painel do agente | Em desenvolvimento |
| Back-end: avaliação financeira e contrato de crédito | Em desenvolvimento |
| Diagrama de pacotes e revisão dos diagramas conforme o código | Em desenvolvimento |

## Estrutura do repositório

```text
Sistema_Aluguel_deCarros/
│
├── backend/
│   └── aluguel-carros-bh/        # API Spring Boot (Maven)
│       └── src/main/java/br/com/pucminas/aluguel_carros_bh/
│           ├── controller/       # Recebe as requisições HTTP
│           ├── service/          # Regras de negócio
│           ├── repository/       # Acesso ao banco (Spring Data JPA)
│           ├── model/            # Entidades JPA
│           ├── dto/              # Objetos de entrada e saída da API
│           ├── security/         # Utilitários de autenticação
│           └── config/           # Configuração de segurança e CORS
│
├── frontend/                     # Aplicação React + Vite
│   └── src/
│
├── docs/
│   ├── diagramas/                # Diagramas UML
│   └── documentacao/             # schema.sql e demais documentos
│
└── README.md
```

## Arquitetura

O back-end segue o padrão **MVC em camadas**:

1. **Controller:** expõe a API REST em `/api/...` e valida a entrada.
2. **Service:** aplica as regras de negócio (CPF e e-mail únicos, máximo de 3 rendimentos, senha criptografada com BCrypt, regras de alteração e cancelamento de pedidos).
3. **Repository:** persiste e consulta os dados no PostgreSQL do Supabase.
4. **Model:** entidades JPA alinhadas ao diagrama de classes e às tabelas do banco.

O front-end React consome a API e envia o token JWT no cabeçalho `Authorization`.

### Modelo de dados

- `Usuario` (abstrata) é a base de `Cliente` e `Agente`; `Agente` é especializada em `Empresa` e `Banco`.
- `Cliente` possui até 3 `Rendimento`.
- `Pedido` liga um `Cliente` a um `Automovel` e possui um `StatusPedido` (`CRIADO`, `EM_ANALISE`, `APROVADO`, `REPROVADO`, `CANCELADO`).
- `AvaliacaoFinanceira` registra o parecer de um agente sobre um pedido.
- `ContratoCredito` associa um pedido a um crédito concedido por um `Banco`.

O diagrama de classes está em `docs/diagramas/` e o script das tabelas em `docs/documentacao/schema.sql`.

## Como executar

### Pré-requisitos

- JDK 25
- Node.js (versão LTS) e npm
- Um projeto no [Supabase](https://supabase.com)

### 1. Banco de dados

1. Crie um projeto no Supabase.
2. No **SQL Editor**, execute o conteúdo de `docs/documentacao/schema.sql`.
3. Para testar as telas e rotas do agente, cadastre um agente executando `docs/documentacao/seed-agente.sql` (agentes não possuem cadastro pela API).

### 2. Back-end

Crie o arquivo `backend/aluguel-carros-bh/src/main/resources/application-local.properties`. Ele **não deve ser versionado** (já está no `.gitignore`):

```properties
spring.datasource.url=jdbc:postgresql://<host-do-session-pooler>:5432/postgres
spring.datasource.username=postgres.<id-do-projeto>
spring.datasource.password=<senha-do-banco>
app.jwt.secret=<texto-aleatorio-com-pelo-menos-32-caracteres>
```

Os dados de conexão estão no botão **Connect** do Supabase (use o *Session pooler*). Em seguida:

```powershell
cd backend/aluguel-carros-bh
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

No Linux ou macOS, use `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`. A API sobe em `http://localhost:8080`.

### 3. Front-end

```bash
cd frontend
npm install
npm run dev
```

Crie o arquivo `frontend/.env` com:

```
VITE_API_URL=http://localhost:8080
```

A aplicação abre em `http://localhost:5173`.

## API REST

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/api/auth/login` | Público | Login; retorna o token JWT, o papel e o nome |
| POST | `/api/clientes` | Público | Cadastro de cliente |
| GET | `/api/clientes` | Agente | Lista clientes |
| GET | `/api/clientes/{id}` | Próprio cliente ou agente | Consulta um cliente |
| PUT | `/api/clientes/{id}` | Próprio cliente | Altera dados cadastrais |
| DELETE | `/api/clientes/{id}` | Próprio cliente | Exclui o cliente |
| GET | `/api/automoveis` e `/api/automoveis/{id}` | Usuário logado | Consulta automóveis |
| POST, PUT, DELETE | `/api/automoveis` | Agente | Cadastra, altera e exclui automóveis |
| POST | `/api/pedidos` | Cliente | Cria um pedido de aluguel |
| GET | `/api/pedidos/meus` | Cliente | Lista os pedidos do cliente logado |
| GET | `/api/pedidos` | Agente | Lista todos os pedidos |
| GET | `/api/pedidos/{id}` | Dono do pedido ou agente | Consulta um pedido e seu status |
| PUT | `/api/pedidos/{id}` | Dono do pedido | Altera o automóvel (somente com status `CRIADO`) |
| PATCH | `/api/pedidos/{id}/cancelar` | Dono do pedido | Cancela (status `CRIADO` ou `EM_ANALISE`) |

## Regras assumidas no protótipo

O enunciado não define alguns detalhes, então foram adotadas as regras abaixo:

- O login é feito com o e-mail do usuário.
- O cliente só altera um pedido com status `CRIADO` e só o cancela com status `CRIADO` ou `EM_ANALISE`.
- No `PUT` de cliente, e-mail, senha e CPF não são alterados.
- Agentes são cadastrados diretamente no banco.
- O proprietário de um automóvel é o agente que o cadastrou.

## Documentação

- Diagramas UML (casos de uso, classes e pacotes): `docs/diagramas/`
- Script do banco de dados: `docs/documentacao/schema.sql`
- Protótipo no Figma: _(adicionar o link)_

## Segurança

- As senhas são armazenadas com hash BCrypt.
- A API é stateless: toda rota, exceto login e cadastro de cliente, exige token JWT.
- Segredos (senha do banco e `app.jwt.secret`) ficam apenas em `application-local.properties`, fora do Git.
