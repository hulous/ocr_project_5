# MondeDeDev

Full-stack developer social network with an Angular front-end and Spring Boot backend.

## Front-end (Angular)

The front-end project is located in `front/`.

- Angular version: `21.2.14`
- TypeScript version: `5.9.3`
- Uses `@angular/material` for UI components
- Dev server: `http://localhost:4200/`
- Proxy config: `front/proxy.conf.json` forwards `/api` requests to `http://localhost:8018`

### Setup

```bash
cd front
npm install
```

### Development server

```bash
npm start
```

### Build

```bash
npm run build
```

### Tests

```bash
npm test
```

## Backend (Spring Boot)

The backend project is located in `back/`.

- Spring Boot starter parent version: `4.0.6`
- Java version: `21`
- Maven wrapper included: `./mvnw`
- Spring modules: Web MVC, Security, Data JPA, Liquibase, OpenAPI
- Runtime database: PostgreSQL
- Test database: H2 in-memory

### Environment

Copy the sample env file and update values before running:

```bash
cd back
cp .env.sample.properties src/main/resources/env.properties
```

The backend loads environment values from `back/src/main/resources/env.properties`.

Required keys:

- `DB_NAME`
- `DB_HOST`
- `DB_PORT`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET_TOKEN`
- `MAIN_APP_PORT`

### Run the backend

```bash
cd back
./mvnw spring-boot:run
```

### Build the backend

```bash
./mvnw clean package
```

### Run tests

```bash
./mvnw test
```

### Test report

- See (TEST_REPORT.md)[TEST_REPORT.md] for a consolidated front-end and back-end test status summary.

### API documentation

When the backend is running, OpenAPI UI is available at:

`http://localhost:8018/swagger-ui/index.html`

## Notes

- Use `Authorization: Bearer <token>` for protected API requests.
- See `RELEASE.md` for release notes.
