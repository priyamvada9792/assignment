# 🧢 Player Data Service

This Spring Boot application manages player data and uses a local LLM (TinyLlama via Ollama) to generate nicknames. It features REST endpoints, streaming AI responses, CSV integration, structured logging, exception handling, and full unit/integration tests.

---

## 🚀 Features
- ✅ **Role-based player data access and age calculation**
- ✅ **AI-generated nicknames** from TinyLlama (via Ollama)
- ✅ **CSV-based input support** for batch nickname generation
- ✅ **Streaming support** for parsing token-by-token LLM output
- ✅ **Custom exception handling** with proper error messages
- ✅ **Logging** for both operations and failures
- ✅ **Unit & Integration test coverage**

---

## 🗂 Directory Overview

```
src/
├── main/
│   ├── java/com/assignment/playerdata/                        
│   │   ├── controller/                 → REST endpoints for player and nickname
│   │   ├── exception/                  → Custom + global exception classes
│   │   ├── model/                      → Player, Manager, Team, Stats
│   │   ├── repository/                 → PlayerRepository
│   │   └── service/                    → PlayerService logic, NicknameService logic
│   └── resources/
│       └── Player.csv                  → Sample input file
└── test/
    └── java/com/assignment/playerdata/   → JUnit test classes
```

---

## 📋 Player API Overview

### 🔍 Get All Players (Role-Based)
```
GET /v1/players?isAdmin=true|false
```
- If `isAdmin=true`: returns `firstName`, `lastName`, `age`
- If `isAdmin=false`: returns only `firstName`, `age`

### 🔍 Get Player by ID
```
GET /v1/players/{id}
```
Returns full player details including age and associated data.

### 🔁 Relationships & Stats
- Players are linked with `Manager`, `Team`
- Have related `BattingStat`, `PitchingStat`, `FieldingStat`

### ❌ Exception Handling
- `PlayerNotFoundException` → HTTP 404 with clear message

### ✅ Test Coverage
- `PlayerServiceTests` for logic
- `PlayerControllerTests` for endpoint validation

---

## 📡 Nickname API Endpoints

### ➕ Generate Nickname for a Country
```
GET /v1/nickname?country=Japan
```
#### ✅ Example Response:
```json
{
  "country": "Japan",
  "nickname": "Samurai Swinger"
}
```

### 📄 Generate Nicknames from CSV
```
GET /v1/nickname/from-csv
```
Reads `src/main/resources/Player.csv` (format: `PlayerName,Country`)

#### ✅ Example CSV:
```csv
Sachin Tendulkar,India
Lionel Messi,Argentina
Megan Rapinoe,USA
```

#### ✅ Example Response:
```json
[
  { "player": "Sachin Tendulkar", "country": "India", "nickname": "Cricket Commander" },
  { "player": "Lionel Messi", "country": "Argentina", "nickname": "Football Fantasma" }
]
```

---

## ❗ Error Handling
All exceptions return structured JSON with meaningful messages:
```json
{
  "error": "Nickname Generation Failed",
  "message": "Failed to generate nickname for: India"
}
```

---

## ⚙️ Setup Instructions

### 🛠 Requirements:
- Java 17+
- Maven
- [Ollama](https://ollama.com) installed

### 🔧 Run Ollama Locally:
```bash
ollama serve
ollama run tinyllama
```

### ▶️ Run the Application:
```bash
mvn spring-boot:run
```

### 🧪 Run Tests:

```bash
mvn test
````

Tests include:

Unit tests for services and utilities

Integration tests for all REST endpoints using MockMvc

Edge case validation for bad input, missing params, malformed data

---
## 📐 Design Choices

1. ⚙️ Tech Stack

Spring Boot 3.4.4 for service framework

H2 in-memory DB for portability and ease of setup

Lombok to reduce boilerplate

Ollama + TinyLlama for local LLM nickname generation

MockMvc + JUnit 5 for integration and unit tests

2. 🗄️ Database Selection

Used H2 in-memory database for simplicity, fast startup, and developer-friendliness

Ideal for small projects, self-contained services, and testing

Easily swappable to Postgres or MySQL via application.properties

3. 🧠 LLM Nickname Generation

Uses OpenAI-style chat format with system & user prompts

Streams responses token-by-token for flexibility.

Graceful fallback when model is unavailable or slow.


---
## 🗃 Access the H2 Console (for debugging DB)

http://localhost:8080/h2-console

JDBC URL:

jdbc:h2:mem:testdb

Username: sa

Password: (leave blank)

If you see Database "mem:testdb" not found, ensure you first hit any /v1/players endpoint to trigger schema creation, or seed data using data.sql.

---
## 📉 Database Design

### 📊 Entity Relationship Diagram

```
+------------------+          +-------------------+          +------------------+
|     managers     |●o————————+  players          |●o——-——►|      teams        |
|------------------|          |-------------------|          |------------------|
| id (PK)          |          | id (PK)           |          | id (PK)          |
| name             |          | first_name        |          | name             |
+------------------+          | last_name         |          +------------------+
                              | birth_date        |
                              | manager_id (FK)   |
                              | team_id (FK)      |
                              +-------------------+
                                       │
                                       ▼
        +------------------+   +-------------------+   +-------------------+
        |  batting_stat    |   |  pitching_stat    |   |  fielding_stat    |
        |------------------|   |-------------------|   |-------------------|
        | id (PK)          |   | id (PK)           |   | id (PK)           |
        | player_id (FK)   |   | player_id (FK)    |   | player_id (FK)    |
        | stat_name        |   | stat_name         |   | stat_name         |
        | value            |   | value             |   | value             |
        +------------------+   +-------------------+   +-------------------+
```

### 🔗 Relationships
- One `Manager` ➔ Many `Players`
- One `Team` ➔ Many `Players`
- One `Player` ➔ Many `Stats` (batting, pitching, fielding)

---
## 🤔 Assumptions Made

- isAdmin flag is required or defaults to false if not provided

- LLM nickname generation may be non-deterministic (depends on model)

- Player data can be seeded via data.sql or Player.csv

- Only basic fields are required for Player; stats are optional

---

## 🧪 Testing Strategy

✅ Unit Tests

Pure logic tests for PlayerService, NicknameGeneratorService

Edge handling: null input, empty result, CSV parsing errors

✅ Integration Tests

Use @SpringBootTest + @AutoConfigureMockMvc

Verify endpoint behavior, status codes, and JSON responses

Covers /v1/players, /v1/players/{id}, /v1/nickname, and /v1/nickname/from-csv

✅ Edge Cases

Missing/invalid query params (e.g., isAdmin, country, ID type mismatch)

Empty or malformed input data

LLM API failure handling (simulate unavailability)

Special character encoding and response validation


---
## 🔮 Future Enhancements
- Swagger/OpenAPI docs
- CSV upload endpoint
- Database persistence for nickname history
- Retry logic and timeout management for AI service

---



