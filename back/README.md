# MondeDeDev Backend

Spring Boot backend service with:

- Spring Web MVC
- Spring Security
- Spring Data JPA
- Liquibase database migrations
- PostgreSQL runtime database
- H2 in-memory database for tests
- OpenAPI UI via springdoc

## Prerequisites

- Java 21+
- Maven 3.8+ or the included Maven wrapper
- PostgreSQL running locally or reachable from this app

## Project structure

- Application entry point: `src/main/java/com/openclassrooms/mddapi/BaseApplication.java`
- Main configuration: `src/main/resources/application.yaml`
- Environment values: `src/main/resources/env.properties`
- Sample environment file: `.env.sample.properties`
- Liquibase changelogs: `src/main/resources/db/changelog`

## Environment configuration

The application loads environment values from `src/main/resources/env.properties` using Spring Boot config import.

1. Copy the sample file into `back/`:

```bash
cd back
cp .env.sample.properties src/main/resources/env.properties
```

2. Update the values in `src/main/resources/env.properties`.

Required keys:

- `DB_NAME`
- `DB_HOST`
- `DB_PORT`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET_TOKEN`
- `MAIN_APP_PORT`

Notes:

- Use a strong random value for `JWT_SECRET_TOKEN`.
- `MAIN_APP_PORT` controls the HTTP port that Spring Boot listens on.

## Run the application

From the `back/` folder:

```bash
./mvnw spring-boot:run
```

Build the jar:

```bash
./mvnw clean package
```

Run the packaged jar:

```bash
java -jar target/mdd-api-0.0.1-SNAPSHOT.jar
```

## Tests

Run tests from `back/`:

```bash
./mvnw test
```

Test configuration uses `src/test/resources/env.test.properties` and `src/test/resources/application.yaml`.

## API documentation

When the application is running, OpenAPI UI is available at:

- `http://localhost:${MAIN_APP_PORT}/swagger-ui/index.html`

The raw OpenAPI JSON is available at:

- `http://localhost:${MAIN_APP_PORT}/v3/api-docs`

The documentation is here too: [API_ENDPOINTS.md](API_ENDPOINTS.md).

## Database migrations

Liquibase is enabled in `application.yaml`.
Add changelog files under:

- `src/main/resources/db/changelog`

The main changelog is:

- `classpath:db/changelog/db.changelog-master.yaml`

