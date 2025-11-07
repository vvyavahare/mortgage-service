# Mortgage Service

MVP Spring Boot application implementing:
- GET /api/interest-rates
- POST /api/mortgage-check

Build & run:
```
mvn -DskipTests=false clean verify
mvn spring-boot:run
```

Notes:
- Java 17
- Spring Boot 3.x


## API - quick examples

Base URL: `http://localhost:8080` (adjust port if required)

### Get interest rates
```bash
curl -s http://localhost:8080/api/interest-rates | jq
```

### Mortgage check (valid)
```
curl -s -X POST http://localhost:8080/api/mortgage-check \
  -H "Content-Type: application/json" \
  -d '{"income":50000,"maturityPeriod":20,"loanValue":150000,"homeValue":250000}' | jq
```

### Mortgage check (invalid request)
```
curl -s -X POST http://localhost:8080/api/mortgage-check \
  -H "Content-Type: application/json" \
  -d '{"income":null,"maturityPeriod":20,"loanValue":150000,"homeValue":250000}' | jq

```
