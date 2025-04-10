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
```

---
## 🗃 Access the H2 Console (for debugging DB)

http://localhost:8080/h2-console

JDBC URL:

jdbc:h2:mem:testdb

Username: sa

Password: (leave blank)

If you see Database "mem:testdb" not found, ensure you first hit any /v1/players endpoint to trigger schema creation, or seed data using data.sql.


---
## 🔮 Future Enhancements
- Swagger/OpenAPI docs
- CSV upload endpoint
- Database persistence for nickname history
- Retry logic and timeout management for AI service

---

## Credits
- [TinyLlama Model](https://huggingface.co/cerebras/TinyLlama-1.1B-Chat-v1.0)
- [Ollama](https://ollama.com) — Local LLM serving

