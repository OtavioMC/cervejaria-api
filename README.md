# 🍺 Cervejaria API - Sistema de Gerenciamento

API RESTful completa para gerenciamento de cervejaria desenvolvida com Spring Boot.

## 📋 Funcionalidades

### Módulos Principais

- **Produtos** - Gerenciamento completo do catálogo de produtos
- **Garçons** - Cadastro e gerenciamento de garçons
- **Pedidos** - Controle de pedidos por mesa
- **Itens de Pedido** - Itens individuais de cada pedido

### Recursos

✅ CRUD completo para todas as entidades  
✅ Controle automático de estoque  
✅ Cálculo automático de valores (subtotal, total)  
✅ Relatórios e estatísticas de vendas  
✅ Validações de negócio  
✅ Tratamento global de exceções  
✅ API RESTful com retornos em JSON  
✅ Suporte a Docker  

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Hibernate**
- **MySQL 8.0**
- **Maven**
- **Docker & Docker Compose**

## 🚀 Como Executar

### Opção 1: Com Docker (Recomendado)

```bash
# 1. Clone o repositório
cd c:\xampp\htdocs\cervejaria-api\cervejaria-api

# 2. Suba os containers
docker-compose up -d

# 3. Acompanhe os logs
docker-compose logs -f app

# 4. Acesse a aplicação
http://localhost:8080
```

**Comandos úteis:**
```bash
# Parar os containers
docker-compose down

# Parar e remover volumes (limpa o banco)
docker-compose down -v

# Rebuild da aplicação
docker-compose up -d --build

# Ver logs
docker-compose logs -f mysql
docker-compose logs -f app
```

### Opção 2: Sem Docker (Local)

```bash
# 1. Inicie o MySQL (via XAMPP ou standalone)

# 2. Configure o application.properties
# Certifique-se de que a URL, usuário e senha estão corretos

# 3. Compile e execute
cd c:\xampp\htdocs\cervejaria-api\cervejaria-api
mvn clean install
mvn spring-boot:run

# 4. Acesse a aplicação
http://localhost:8080
```

## 📡 Endpoints da API

### 🍺 Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/produtos` | Lista todos os produtos |
| GET | `/api/produtos/ativos` | Lista produtos ativos |
| GET | `/api/produtos/{id}` | Busca produto por ID |
| GET | `/api/produtos/categoria/{categoria}` | Lista por categoria |
| GET | `/api/produtos/buscar?nome=` | Busca por nome |
| GET | `/api/produtos/estoque-baixo?quantidade=` | Produtos com estoque baixo |
| GET | `/api/produtos/categorias` | Lista todas categorias |
| POST | `/api/produtos` | Cria novo produto |
| PUT | `/api/produtos/{id}` | Atualiza produto |
| DELETE | `/api/produtos/{id}` | Inativa produto |
| PATCH | `/api/produtos/{id}/estoque/adicionar?quantidade=` | Adiciona estoque |
| PATCH | `/api/produtos/{id}/estoque/remover?quantidade=` | Remove estoque |

### 👨‍🍳 Garçons

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/garcons` | Lista todos os garçons |
| GET | `/api/garcons/ativos` | Lista garçons ativos |
| GET | `/api/garcons/{id}` | Busca garçom por ID |
| GET | `/api/garcons/cpf/{cpf}` | Busca por CPF |
| GET | `/api/garcons/buscar?nome=` | Busca por nome |
| POST | `/api/garcons` | Cria novo garçom |
| PUT | `/api/garcons/{id}` | Atualiza garçom |
| DELETE | `/api/garcons/{id}` | Inativa garçom |

### 📝 Pedidos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/pedidos` | Lista todos os pedidos |
| GET | `/api/pedidos/{id}` | Busca pedido por ID |
| GET | `/api/pedidos/{id}/completo` | Busca pedido com itens |
| GET | `/api/pedidos/status/{status}` | Lista por status |
| GET | `/api/pedidos/mesa/{numeroMesa}` | Lista por mesa |
| GET | `/api/pedidos/mesa/{numeroMesa}/abertos` | Pedidos abertos da mesa |
| GET | `/api/pedidos/garcom/{garcomId}` | Lista por garçom |
| GET | `/api/pedidos/periodo?inicio=&fim=` | Lista por período |
| POST | `/api/pedidos` | Cria novo pedido |
| PUT | `/api/pedidos/{id}` | Atualiza pedido |
| PATCH | `/api/pedidos/{id}/fechar` | Fecha pedido |
| PATCH | `/api/pedidos/{id}/pagar` | Paga pedido |
| PATCH | `/api/pedidos/{id}/cancelar` | Cancela pedido |
| DELETE | `/api/pedidos/{id}` | Remove pedido |

### 🛒 Itens de Pedido

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/itens-pedido` | Lista todos os itens |
| GET | `/api/itens-pedido/{id}` | Busca item por ID |
| GET | `/api/itens-pedido/pedido/{pedidoId}` | Itens de um pedido |
| GET | `/api/itens-pedido/produto/{produtoId}` | Itens por produto |
| GET | `/api/itens-pedido/relatorios/mais-vendidos` | Produtos mais vendidos |
| POST | `/api/itens-pedido` | Adiciona item ao pedido |
| PUT | `/api/itens-pedido/{id}` | Atualiza item |
| DELETE | `/api/itens-pedido/{id}` | Remove item |

## 📝 Exemplos de Uso

### Criar Produto

```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Cerveja IPA",
    "descricao": "Cerveja artesanal IPA 500ml",
    "preco": 15.90,
    "categoria": "CERVEJA",
    "estoque": 100
  }'
```

### Criar Garçom

```bash
curl -X POST http://localhost:8080/api/garcons \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678901",
    "telefone": "11999999999",
    "email": "joao@cervejaria.com"
  }'
```

### Criar Pedido

```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "numeroMesa": 5,
    "garcom": {"id": 1},
    "observacoes": "Cliente solicitou copo gelado"
  }'
```

### Adicionar Item ao Pedido

```bash
curl -X POST http://localhost:8080/api/itens-pedido \
  -H "Content-Type: application/json" \
  -d '{
    "pedido": {"id": 1},
    "produto": {"id": 1},
    "quantidade": 2,
    "observacoes": "Bem gelada"
  }'
```

## 🔒 Regras de Negócio

- ✅ Não é possível adicionar/remover itens de pedidos fechados ou pagos
- ✅ Estoque é atualizado automaticamente ao criar/atualizar/remover itens
- ✅ Total do pedido é calculado automaticamente
- ✅ Produtos inativos não podem ser adicionados a pedidos
- ✅ CPF e Email de garçons devem ser únicos
- ✅ Preço unitário do item é fixado no momento da criação

## 📊 Status dos Pedidos

- `ABERTO` - Pedido em andamento (padrão)
- `FECHADO` - Pedido finalizado, aguardando pagamento
- `PAGO` - Pedido pago
- `CANCELADO` - Pedido cancelado

## 🐛 Tratamento de Erros

A API retorna erros no formato:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Estoque insuficiente para o produto: Cerveja IPA"
}
```

## 🗂️ Estrutura do Projeto

```
cervejaria-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/cervejaria_api/
│   │   │   ├── controller/      # Controllers REST
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositories
│   │   │   ├── service/         # Camada de serviço
│   │   │   └── exception/       # Tratamento de exceções
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
├── Dockerfile
├── docker-compose.yaml
├── .dockerignore
├── pom.xml
└── README.md
```

## 🔧 Variáveis de Ambiente

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/cervejaria_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=

# Server
SERVER_PORT=8080
```

## 📄 Licença

Este projeto está sob a licença MIT.

---

**Desenvolvido com ☕ e 🍺**
