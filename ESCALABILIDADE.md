# Exemplo de Docker Compose para Escalabilidade

Para escalar os microserviços, você pode usar o comando `docker-compose up --scale`:

## Comandos para escalar:

```bash
# Escalar o serviço de usuários para 3 instâncias
docker-compose up --scale usuario=3

# Escalar múltiplos serviços
docker-compose up --scale usuario=2 --scale sala=2 --scale reserva=3

# Escalar em modo detached (background)
docker-compose up -d --scale usuario=3 --scale sala=2 --scale reserva=2
```

## Configuração com nomes específicos (alternativa):

Se você quiser nomear instâncias específicas, pode criar um docker-compose.scale.yml:

```yaml
version: '3.8'

services:
  # Instâncias adicionais do serviço usuario
  usuario-2:
    build: ./usuario
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres-usuario:5432/usuario
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: admin
      RABBITMQ_HOST: rabbitmq
      RABBITMQ_PORT: 5672
      RABBITMQ_USERNAME: guest
      RABBITMQ_PASSWORD: guest
    depends_on:
      postgres-usuario:
        condition: service_started
      rabbitmq:
        condition: service_healthy
    networks:
      - backend
    restart: unless-stopped

  usuario-3:
    build: ./usuario
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres-usuario:5432/usuario
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: admin
      RABBITMQ_HOST: rabbitmq
      RABBITMQ_PORT: 5672
      RABBITMQ_USERNAME: guest
      RABBITMQ_PASSWORD: guest
    depends_on:
      postgres-usuario:
        condition: service_started
      rabbitmq:
        condition: service_healthy
    networks:
      - backend
    restart: unless-stopped

networks:
  backend:
    external: true
```

## Para usar o arquivo de escalabilidade:
```bash
docker-compose -f docker-compose.yml -f docker-compose.scale.yml up
```

## Atualizando nginx.conf para múltiplas instâncias:

Quando adicionar instâncias nomeadas, descomente e ajuste as linhas no nginx.conf:

```nginx
upstream usuario_service {
    server usuario:8080;
    server usuario-2:8080;
    server usuario-3:8080;
}
```

## Monitoramento:

Para verificar se as instâncias estão funcionando:
```bash
# Ver containers rodando
docker-compose ps

# Ver logs de um serviço específico
docker-compose logs usuario

# Ver logs em tempo real
docker-compose logs -f usuario
```
