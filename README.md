# 🌍 NEI — Near Earth Impact

> Plataforma de monitoramento e avaliação de risco de impactos de asteroides próximos à Terra, consumindo a API NASA NeoWs (Near Earth Object Web Service).

---

## 👥 Integrantes

| Nome | RM |
|---|---|
| Anthony de Souza Henriques | RM 566188 |
| Guilherme Santos Fonseca | RM 564232 |
| Gustavo Araújo da Silva | RM 566526 |
| Nathan Gonçalves Pereira Mendes | RM 564666 |

---

## 🔗 Links

| | |
|---|---|
| 🚀 Deploy | `[preencher URL do Railway]` |
| 🎥 Vídeo de Apresentação | `[preencher link do vídeo]` |
| 📖 Repositório GitHub | https://github.com/GlobalSolution-Fiap-2TDSPX-2026/JavaNEI |
| 📋 Documentação Swagger | `[URL do deploy]/swagger-ui/index.html` |

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

### Passos

```bash
# Clone o repositório
git clone https://github.com/GlobalSolution-Fiap-2TDSPX-2026/JavaNEI.git
cd JavaNEI

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

