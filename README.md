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


### [Swagger URL of the application](http://localhost:8080/swagger-ui/index.html)

## API - quick examples

Base URL: `http://localhost:8080` (adjust port if required)



### Get interest rates
```bash
curl --location 'http://localhost:8080/api/interest-rates'
```

### Mortgage check (valid)
```
curl --location 'http://localhost:8080/api/mortgage-check' \
--header 'Content-Type: application/json' \
--data '{
    "income": 50000,
    "maturityPeriod": 20,
    "loanValue": 150000,
    "homeValue": 250000
}'
```

### Mortgage check (eligibility false  when Loan exceeds 4x income)
```

curl --location 'http://localhost:8080/api/mortgage-check' \
--header 'Content-Type: application/json' \
--data '{
    "income": 5,
    "maturityPeriod": 20,
    "loanValue": 150000,
    "homeValue": 250000
}'
```

### Mortgage check (invalid request)
```
curl --location 'http://localhost:8080/api/mortgage-check' \
--header 'Content-Type: application/json' \
--data '{
"income": null,
"maturityPeriod": 20,
"loanValue": 150000,
"homeValue": 250000
}'
```

### To Run Integration tests
```
mvn verify -P integration-tests
```
