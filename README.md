🌦️ Weather API — Spring Boot + Redis + PostgreSQL

A production-ready Weather API built with Spring Boot, featuring:

- Real-time Weather Fetching from a 3rd-party API (Visual Crossing)
- High-performance Caching using Redis (12-hour TTL)
- Storing request logs in PostgreSQL
- Clean REST architecture with Service + Repository layers
- Environment variable based configuration

This project is perfect for learning API integration, caching, database usage, and Spring Boot best practices.

---

## 🚀 Features

### ✔ Fetch Weather Data

- Uses Visual Crossing Weather API (or any external weather provider).

### ✔ Redis Caching

- Stores API responses as JSON
- Uses city name as cache key
- Auto-expiry after 12 hours

### ✔ PostgreSQL Database Logging

Every API request is saved with:
- city
- temperature
- description
- timestamp

### ✔ Clean Code Architecture

```
Controller → Service → Repository → Redis Cache → Database
```

---

## 🛠️ Tech Stack

| Component     | Technology        |
|---------------|------------------|
| Backend       | Spring Boot      |
| Cache         | Redis            |
| Database      | PostgreSQL       |
| ORM           | Spring Data JPA  |
| HTTP Client   | RestTemplate     |
| Build Tool    | Maven            |
| JSON Mapper   | Jackson          |

---

## 📂 Project Structure

```
src/main/java/com/example/weatherapi
├── controller
│   └── WeatherController.java
├── service
│   └── WeatherService.java
├── model
│   └── WeatherRequest.java
├── repository
│   └── WeatherRequestRepository.java
├── config
│   ├── RedisConfig.java
│   └── RestTemplateConfig.java
└── WeatherAppApplication.java
```

---

## 🗄️ Database Schema (PostgreSQL)

```sql
CREATE TABLE weather_requests (
    id BIGSERIAL PRIMARY KEY,
    city VARCHAR(100),
    temperature DOUBLE PRECISION,
    weather_description VARCHAR(255),
    request_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## ⚙️ Configuration

### application.properties

```properties
# Weather API
weather.api.url=https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/
weather.api.key=YOUR_API_KEY

# Redis
spring.redis.host=localhost
spring.redis.port=6379

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/weatherdb
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
```

---

## 🔧 Core Logic — Redis Cache Usage

### ✔ Store JSON in Redis

```java
redisTemplate.opsForValue().set(cacheKey, responseBody, Duration.ofHours(12));
```

### ✔ Retrieve JSON from Redis

```java
Map<String, Object> cachedData =
    (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
```

---

## 🌐 API Endpoint

**GET /api/weather/{city}**

Fetches current weather for a city.

### Example

```
GET http://localhost:8080/api/weather/London
```

#### Sample Response

```json
{
  "resolvedAddress": "London, England",
  "days": [
    {
      "temp": 14.2,
      "description": "Clear conditions"
    }
  ]
}
```

---

## ▶️ How to Run the Project

1️⃣ **Start PostgreSQL**  
Create database:
```sql
CREATE DATABASE weatherdb;
```

2️⃣ **Start Redis**  
Docker:
```sh
docker run -p 6379:6379 redis
```

3️⃣ **Add Your Weather API Key**  
Edit `application.properties`.

4️⃣ **Run Application**  
```sh
mvn spring-boot:run
```

---

## 🧪 Testing

Using curl:
```sh
curl http://localhost:8080/api/weather/Delhi
```

---

## 📌 Improvements (Optional)

- Add Swagger documentation
- Add retry logic for external API failures
- Add Prometheus + Grafana monitoring
- Use Redisson for distributed caching
- Add unit + integration tests

---

## 🤝 Contributing

Contributions are welcome!  
Please open an issue or submit a pull request.

---

## 📜 License

Distributed under the MIT License.
