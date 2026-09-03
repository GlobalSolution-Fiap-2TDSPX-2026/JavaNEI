# 🌍 NEI — Near Earth Impact

> Plataforma de monitoramento e avaliação de risco de impactos de asteroides próximos à Terra, consumindo a API NASA NeoWs (Near Earth Object Web Service).

---

## 👥 Integrantes

| Nome | RM |
|---|---|
| Anthony de Souza Henriques | RM 566188 |
| Guilherme Santos Fonseca | RM 564232 |
| Gustavo Araújo da Silva | RM 566526 |


---

# Java NEI - Containerization & Cloud Deployment

## Build e Push das Imagens no Azure Container Registry (ACR)

```bash
# Build e Push do MySQL
cd db
docker build -f Dockerfile.mysql -t nei-mysql .
docker tag nei-mysql rm564232nei.azurecr.io/rm564232-nei-mysql:v2
docker push rm564232nei.azurecr.io/rm564232-nei-mysql:v2

# Build e Push da Aplicação Spring Boot
cd ..
docker build -t nei-app .
docker tag nei-app rm564232nei.azurecr.io/rm564232-nei-app:v4
docker push rm564232nei.azurecr.io/rm564232-nei-app:v4
```

## Instanciação da Infraestrutura na Azure via CLI (ACI & File Share)

```bash
# 1. Criar o Storage Share para Persistência do MySQL
az storage share create --account-name voldbrm564232 --name nei-mysql-data

# 2. Obter a chave de acesso da Storage Account
STORAGE_KEY=$(az storage account keys list --resource-group rg-nei-rm564232 --account-name voldbrm564232 --query "[0].value" -o tsv)

# 3. Criar o Container do Banco de Dados MySQL
MSYS_NO_PATHCONV=1 az container create \
  --resource-group rg-nei-rm564232 \
  --name rm564232-nei-mysql \
  --image rm564232nei.azurecr.io/rm564232-nei-mysql:v2 \
  --os-type Linux \
  --registry-login-server rm564232nei.azurecr.io \
  --registry-username rm564232nei \
  --registry-password $(az acr credential show --name rm564232nei --query "passwords[0].value" -o tsv) \
  --dns-name-label rm564232-nei-mysql-db \
  --ports 3306 \
  --azure-file-volume-account-name voldbrm564232 \
  --azure-file-volume-account-key $STORAGE_KEY \
  --azure-file-volume-share-name nei-mysql-data \
  --azure-file-volume-mount-path /var/lib/mysql \
  --cpu 1 --memory 2

# 4. Criar o Container da Aplicação Java Spring Boot
MSYS_NO_PATHCONV=1 az container create \
  --resource-group rg-nei-rm564232 \
  --name rm564232-nei-app \
  --image rm564232nei.azurecr.io/rm564232-nei-app:v4 \
  --os-type Linux \
  --registry-login-server rm564232nei.azurecr.io \
  --registry-username rm564232nei \
  --registry-password $(az acr credential show --name rm564232nei --query "passwords[0].value" -o tsv) \
  --dns-name-label rm564232-nei-app \
  --ports 8080 \
  --environment-variables SPRING_PROFILES_ACTIVE=docker \
  DB_URL="jdbc:mysql://rm564232-nei-mysql-db.canadacentral.azurecontainer.io:3306/nei_db" \
  DB_USERNAME=nei_user \
  DB_PASSWORD=nei_pass \
  NASA_API_KEY=s9gF1rPUkfxgXPIEvKZ4sc5SiLbaM5hKTQGmkCSM \
  JWT_SECRET=pudimcomfarinhaquente \
  JWT_EXPIRATION=86400000 \
  --cpu 1 --memory 2
```

## 🔗 Links

| | |
|---|---|
| 🚀 Deploy | https://javanei.onrender.com |
| 🎥 Vídeo de Apresentação | https://youtu.be/hxDZhr_z55s |
| 📖 Repositório GitHub | https://github.com/GlobalSolution-Fiap-2TDSPX-2026/JavaNEI |
| 📋 Documentação Swagger | https://javanei.onrender.com/swagger-ui/index.html |

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 4**
- **Spring Security + JWT** — autenticação e autorização
- **Spring Data JPA + Hibernate** — persistência com Oracle DB
- **Spring Validation** — validação de dados de entrada
- **Spring HATEOAS** — hipermídia nas respostas da API
- **Spring Cache** — cache de consultas
- **Springdoc OpenAPI / Swagger UI** — documentação interativa
- **Lombok** — produtividade no código
- **Spring Boot DevTools** — hot reload em desenvolvimento
- **Oracle Database** — banco de dados relacional
- **NASA NeoWs API** — fonte de dados de asteroides

---

## 🚀 Como Executar Localmente

### Pré-requisitos
- Java 17+
- Maven 3.8+
- Acesso ao banco Oracle FIAP
- Chave da API NEO Ws da NASA

### Passos

```bash
# Clone o repositório
git clone https://github.com/GlobalSolution-Fiap-2TDSPX-2026/JavaNEI.git
cd JavaNEI

declare a sua chave de api no application.properties

# Execute a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

A documentação Swagger estará em `http://localhost:8080/swagger-ui/index.html`.

---

## 📡 Principais Endpoints

### Autenticação
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/login` | Autenticar e obter token JWT |
| POST | `/users` | Cadastrar novo usuário |

### Asteroides
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/asteroids` | Listar todos (paginado) |
| GET | `/asteroids/{id}` | Buscar por ID (com HATEOAS) |
| GET | `/asteroids/nasa/{nasaId}` | Buscar pelo ID da NASA |
| GET | `/asteroids/search?name=` | Buscar por nome |
| GET | `/asteroids/hazardous?isPotentiallyDangerous=` | Filtrar por periculosidade |
| POST | `/asteroids` | Cadastrar manualmente |
| PUT | `/asteroids/{id}` | Atualizar |
| DELETE | `/asteroids/{id}` | Remover |

### Aproximações
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/close-approaches` | Resumo das aproximações do dia |
| GET | `/close-approaches/{id}` | Buscar por ID |
| GET | `/close-approaches/asteroid/{asteroidId}` | Por asteroide |
| GET | `/close-approaches/date-range?start=&end=` | Por intervalo de datas |
| GET | `/close-approaches/distance?maxDistanceKm=` | Por distância máxima |
| POST | `/close-approaches` | Cadastrar manualmente |
| PUT | `/close-approaches/{id}` | Atualizar |
| DELETE | `/close-approaches/{id}` | Remover |

### Sincronização NASA
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/nasa/sync/today` | Sincronizar asteroides do dia atual |
| POST | `/nasa/sync?startDate=&endDate=` | Sincronizar por intervalo de datas |

---

## 🔐 Autenticação

A API utiliza **JWT (Bearer Token)**. Para acessar endpoints protegidos:

**1. Crie um usuário:**
```json
POST /users
{
  "name": "Seu Nome",
  "email": "email@exemplo.com",
  "username": "seuusername",
  "password": "suasenha123"
}
```

**2. Faça login:**
```json
POST /auth/login
{
  "email": "email@exemplo.com",
  "password": "suasenha123"
}
```

**3. Use o token retornado no header:**
```
Authorization: Bearer <token>
```

