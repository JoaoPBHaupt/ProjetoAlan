# Sistema de Reserva de Salas - Microserviços

## 📖 Sobre o Projeto

Este projeto é um sistema distribuído de reserva de salas desenvolvido com arquitetura de microserviços. O sistema permite o gerenciamento completo de usuários, salas e reservas através de APIs REST independentes, utilizando mensageria assíncrona para comunicação entre serviços.

## 🏗️ Arquitetura

O sistema é composto por:

- **API Gateway (Nginx)**: Ponto único de entrada que roteia requisições
- **Microserviço de Usuários**: Gerenciamento de usuários do sistema
- **Microserviço de Salas**: Controle de salas disponíveis
- **Microserviço de Reservas**: Processamento de reservas de salas
- **Banco de Dados**: PostgreSQL independente para cada serviço
- **Message Broker**: RabbitMQ para comunicação assíncrona

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem de programação
- **Spring Boot 3.4.4** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring AMQP** - Integração com RabbitMQ
- **PostgreSQL** - Banco de dados relacional
- **Maven** - Gerenciamento de dependências

### Infraestrutura
- **Docker** - Containerização
- **Docker Compose** - Orquestração de containers
- **Nginx** - API Gateway e load balancer
- **RabbitMQ** - Message broker para comunicação assíncrona

### Documentação
- **Swagger/OpenAPI 3** - Documentação das APIs

## 📋 Pré-requisitos

- **Docker** 20.10 ou superior
- **Docker Compose** 2.0 ou superior
- **Git** para clonar o repositório

## ⚡ Como Executar

### 1. Clone o Repositório
```bash
git clone <url-do-repositorio>
cd ProjetoAlan
```

### 2. Execute o Sistema
```bash
# Execução básica (1 instância de cada serviço)
docker-compose up --build

# Ou executar em background
docker-compose up --build -d
```

### 3. Acesse as APIs e Ferramentas
- **API Gateway**: http://localhost
- **Usuários**: http://localhost/api/usuarios
- **Salas**: http://localhost/api/salas  
- **Reservas**: http://localhost/api/reservas
- **Health Check**: http://localhost/health
- **Adminer**: http://localhost:4040 (Interface para gerenciar bancos de dados)
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

## 🔄 Escalabilidade Horizontal

O sistema suporta escalabilidade horizontal através do Docker Compose. Você pode aumentar o número de instâncias de cada serviço conforme necessário:

### Comando para Escalabilidade
```bash
# Escalar serviços (recomendado)
docker-compose up --scale usuario=3 --scale sala=2 --build

# Com detach (background)
docker-compose up --scale usuario=3 --scale sala=2 --build -d
```

### ⚠️ Importante sobre Escalabilidade

- **Usuários e Salas**: Podem ser escalados livremente (stateless)
- **Reservas**: Manter apenas 1 instância devido à comunicação síncrona com RabbitMQ
- **Nginx**: Automaticamente distribui carga entre as instâncias disponíveis

### Verificar Instâncias Ativas
```bash
# Ver status dos containers
docker-compose ps

# Ver logs de um serviço específico
docker-compose logs usuario
```

## 🔧 Configuração dos Serviços

### Portas Utilizadas
- **80**: Nginx (API Gateway)
- **4040**: Adminer (Interface web para bancos de dados)
- **5432**: PostgreSQL Usuários
- **5433**: PostgreSQL Salas
- **5434**: PostgreSQL Reservas
- **5672**: RabbitMQ
- **15672**: RabbitMQ Management UI

### Variáveis de Ambiente
Os serviços são configurados através de variáveis de ambiente definidas no `docker-compose.yml`:

- Conexões de banco de dados
- Configurações do RabbitMQ
- Credenciais de acesso

## 📡 APIs Disponíveis

### Serviço de Usuários (`/api/usuarios`)
- `GET /api/usuarios` - Listar todos os usuários
- `GET /api/usuarios/{id}` - Buscar usuário por ID
- `POST /api/usuarios` - Criar novo usuário
- `PUT /api/usuarios/{id}` - Atualizar usuário
- `DELETE /api/usuarios/{id}` - Excluir usuário

### Serviço de Salas (`/api/salas`)
- `GET /api/salas` - Listar todas as salas
- `GET /api/salas/{id}` - Buscar sala por ID
- `GET /api/salas/disponiveis` - Listar salas disponíveis
- `POST /api/salas` - Criar nova sala
- `PUT /api/salas/{id}` - Atualizar sala
- `DELETE /api/salas/{id}` - Excluir sala

### Serviço de Reservas (`/api/reservas`)
- `GET /api/reservas` - Listar todas as reservas
- `GET /api/reservas/{id}` - Buscar reserva por ID
- `GET /api/reservas/usuario/{usuarioId}` - Reservas por usuário
- `POST /api/reservas` - Criar nova reserva
- `PUT /api/reservas/{id}/status` - Atualizar status da reserva
- `DELETE /api/reservas/{id}` - Cancelar reserva

## 🔄 Comunicação entre Serviços

O sistema utiliza **RabbitMQ** para comunicação assíncrona:

- **Reservas → Usuários**: Validação de usuários antes de criar reservas
- **Filas**: `usuario.request` e `usuario.response`
- **Padrão**: Request-Response assíncrono

## 🛠️ Comandos Úteis

```bash
# Parar todos os serviços
docker-compose down

# Rebuild completo (limpar cache)
docker-compose down --volumes
docker-compose build --no-cache
docker-compose up

# Ver logs em tempo real
docker-compose logs -f

# Ver logs de um serviço específico
docker-compose logs -f usuario

# Acessar shell de um container
docker-compose exec usuario bash

# Ver uso de recursos
docker stats
```

## �️ Ferramentas de Administração

### Adminer - Interface Web para Bancos de Dados
O Adminer está disponível em **http://localhost:4040** e permite gerenciar todos os bancos de dados do sistema através de uma interface web intuitiva.

#### Conexões dos Bancos:

**Banco de Usuários:**
- Servidor: `postgres-usuario`
- Usuário: `admin`
- Senha: `admin`
- Base de dados: `usuario`

**Banco de Salas:**
- Servidor: `postgres-sala`
- Usuário: `admin`
- Senha: `admin`
- Base de dados: `sala`

**Banco de Reservas:**
- Servidor: `postgres-reserva`
- Usuário: `postgres`
- Senha: `postgres`
- Base de dados: `reserva_sala_db`

#### Funcionalidades do Adminer:
- ✅ Visualizar estrutura das tabelas
- ✅ Executar consultas SQL
- ✅ Inserir, editar e excluir registros
- ✅ Importar/exportar dados
- ✅ Visualizar relacionamentos entre tabelas

## �🐛 Troubleshooting

### Problemas Comuns

1. **Erro de conexão com banco**: Aguarde todos os containers subirem completamente
2. **RabbitMQ connection refused**: Verifique se o RabbitMQ está saudável antes dos outros serviços
3. **Nginx 502 Bad Gateway**: Verifique se os microserviços estão rodando

### Verificar Saúde dos Serviços
```bash
# Health check do Nginx
curl http://localhost/health

# Status dos containers
docker-compose ps

# Logs do RabbitMQ
docker-compose logs rabbitmq
```

---

## 📝 Tutorial Prático - Testando o Sistema

Este tutorial vai te guiar passo a passo para executar e testar todas as funcionalidades do sistema.

### 🚀 Passo 1: Executando o Sistema

```bash
# 1. Clone o repositório (se ainda não fez)
git clone <url-do-repositorio>
cd ProjetoAlan

# 2. Execute o sistema
docker-compose up --build

# 3. Aguarde todos os serviços subirem (pode levar alguns minutos)
# Você verá logs de todos os containers

# 4. Verifique se está funcionando
curl http://localhost/health
```

### ✅ Passo 2: Verificando se tudo está rodando

```bash
# Ver status dos containers
docker-compose ps

# Deve mostrar algo como:
# nginx-gateway    nginx ...  Up
# usuario_1        java ...   Up  
# sala_1           java ...   Up
# reserva_1        java ...   Up
# postgres-*       postgres   Up
# rabbitmq         rabbitmq   Up
```

### 👤 Passo 3: Criando um Usuário

```bash
curl -X POST http://localhost/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao.silva@email.com",
    "senha": "senha123",
    "tipo": "ADMIN",
    "ativo": true
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "senha": "senha123",
  "tipo": "ADMIN",
  "ativo": true
}
```

### 🏢 Passo 4: Criando uma Sala

```bash
curl -X POST http://localhost/api/salas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Sala 101",
    "capacidade": 30,
    "localizacao": "Bloco A, 1º andar",
    "disponivel": true
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "nome": "Sala 101",
  "capacidade": 30,
  "localizacao": "Bloco A, 1º andar",
  "disponivel": true
}
```

### 📅 Passo 5: Criando uma Reserva

```bash
curl -X POST http://localhost/api/reservas \
  -H "Content-Type: application/json" \
  -d '{
    "salaId": 1,
    "usuarioId": 1,
    "dataHoraInicio": "2025-07-14T01:55:26.399Z",
    "dataHoraFim": "2025-07-14T01:57:26.399Z",
    "motivo": "Reunião de projeto",
    "status": "PENDENTE"
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "salaId": 1,
  "usuarioId": 1,
  "dataHoraInicio": "2025-07-14T01:55:26.399Z",
  "dataHoraFim": "2025-07-14T01:57:26.399Z",
  "motivo": "Reunião de projeto",
  "status": "PENDENTE"
}
```

### 📋 Passo 6: Consultando os Dados

```bash
# Listar todos os usuários
curl http://localhost/api/usuarios

# Listar todas as salas
curl http://localhost/api/salas

# Listar todas as reservas
curl http://localhost/api/reservas

# Buscar reservas de um usuário específico
curl http://localhost/api/reservas/usuario/1
```

### 🔄 Passo 7: Testando Escalabilidade

```bash
# 1. Pare o sistema
docker-compose down

# 2. Execute com múltiplas instâncias
docker-compose up --scale usuario=3 --scale sala=2 --build

# 3. Verifique as instâncias
docker-compose ps

# 4. Teste que o load balancing está funcionando
for i in {1..10}; do
  echo "Requisição $i:"
  curl http://localhost/api/usuarios
  echo ""
done

# 5. Veja os logs para confirmar que diferentes instâncias estão respondendo
docker-compose logs usuario
```

### 🔍 Passo 8: Monitoramento

```bash
# 1. Acesse o RabbitMQ Management
open http://localhost:15672
# Usuário: guest, Senha: guest

# 2. Acesse o Adminer para gerenciar bancos de dados
open http://localhost:4040
# Configurações de conexão:
# - Servidor: postgres-usuario (para usuários)
# - Usuário: admin, Senha: admin, Base de dados: usuario
# 
# - Servidor: postgres-sala (para salas)  
# - Usuário: admin, Senha: admin, Base de dados: sala
#
# - Servidor: postgres-reserva (para reservas)
# - Usuário: postgres, Senha: postgres, Base de dados: reserva_sala_db

# 3. Veja as filas sendo utilizadas:
# - usuario.request
# - usuario.response

# 4. Monitore recursos dos containers
docker stats

# 5. Acompanhe logs em tempo real
docker-compose logs -f
```

### 🧪 Passo 9: Testando Cenários Avançados

#### Validação de Usuário Inválido
```bash
# Tente criar uma reserva com usuário que não existe
curl -X POST http://localhost/api/reservas \
  -H "Content-Type: application/json" \
  -d '{
    "salaId": 1,
    "usuarioId": 999,
    "dataHoraInicio": "2025-07-14T02:00:00.000Z",
    "dataHoraFim": "2025-07-14T03:00:00.000Z",
    "motivo": "Teste usuário inválido",
    "status": "PENDENTE"
  }'

# Deve retornar erro 400
```

#### Conflito de Horário
```bash
# Tente criar uma reserva no mesmo horário
curl -X POST http://localhost/api/reservas \
  -H "Content-Type: application/json" \
  -d '{
    "salaId": 1,
    "usuarioId": 1,
    "dataHoraInicio": "2025-07-14T01:55:26.399Z",
    "dataHoraFim": "2025-07-14T01:57:26.399Z",
    "motivo": "Conflito de horário",
    "status": "PENDENTE"
  }'

# Deve retornar erro indicando conflito
```

#### Atualizando Status da Reserva
```bash
# Confirmar uma reserva
curl -X PUT "http://localhost/api/reservas/1/status?status=CONFIRMADA"

# Cancelar uma reserva
curl -X PUT "http://localhost/api/reservas/1/status?status=CANCELADA"
```

### 🎯 Passo 10: Limpeza

```bash
# Para parar todos os serviços
docker-compose down

# Para remover volumes (limpar dados)
docker-compose down --volumes

# Para remover images também
docker-compose down --volumes --rmi all
```

---

## 📊 Exemplos de Uso com curl

### Usuários

```bash
# Criar usuário
curl -X POST http://localhost/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome": "Maria Santos", "email": "maria@email.com", "senha": "123456", "tipo": "PROFESSOR", "ativo": true}'

# Listar usuários
curl http://localhost/api/usuarios

# Buscar por ID
curl http://localhost/api/usuarios/1

# Buscar por email
curl http://localhost/api/usuarios/email/joao.silva@email.com

# Atualizar usuário
curl -X PUT http://localhost/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{"nome": "João Silva Jr", "email": "joao.silva@email.com", "senha": "senha123", "tipo": "ADMIN", "ativo": true}'

# Desativar usuário
curl -X PUT "http://localhost/api/usuarios/1/ativo?ativo=false"
```

### Salas

```bash
# Criar sala
curl -X POST http://localhost/api/salas \
  -H "Content-Type: application/json" \
  -d '{"nome": "Sala 102", "capacidade": 20, "localizacao": "Bloco B, 2º andar", "disponivel": true}'

# Listar salas
curl http://localhost/api/salas

# Listar apenas salas disponíveis
curl http://localhost/api/salas/disponiveis

# Atualizar disponibilidade
curl -X PUT "http://localhost/api/salas/1/disponibilidade?disponivel=false"
```

### Reservas

```bash
# Criar reserva
curl -X POST http://localhost/api/reservas \
  -H "Content-Type: application/json" \
  -d '{"salaId": 1, "usuarioId": 1, "dataHoraInicio": "2025-07-15T09:00:00.000Z", "dataHoraFim": "2025-07-15T10:00:00.000Z", "motivo": "Aula de programação", "status": "PENDENTE"}'

# Listar reservas
curl http://localhost/api/reservas

# Reservas por usuário
curl http://localhost/api/reservas/usuario/1

# Atualizar status
curl -X PUT "http://localhost/api/reservas/1/status?status=CONFIRMADA"

# Cancelar reserva
curl -X DELETE http://localhost/api/reservas/1
```

---

## 🎉 Parabéns!

Você completou o tutorial! Agora você sabe como:

✅ Executar o sistema completo  
✅ Criar usuários, salas e reservas  
✅ Escalar os serviços horizontalmente  
✅ Monitorar o sistema  
✅ Testar cenários de erro  
✅ Usar todas as APIs disponíveis  