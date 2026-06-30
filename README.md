# P6-Full-Stack-reseau-dev

## Front

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 21.2.14.

Don't forget to install your node_modules before starting (`npm install`).

### Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

### Where to start

As you may have seen if you already started the app, a simple home page containing a logo, a title and a button is available. If you take a look at its code (in the `home.component.html`) you will see that an external UI library is already configured in the project.

This library is `@angular/material`, it's one of the most famous in the angular ecosystem. As you can see on their docs (https://material.angular.io/), it contains a lot of highly customizable components that will help you design your interfaces quickly.

Note: I recommend to use material however it's not mandatory, if you prefer you can get rid of it.

## Back / Spring Boot

The backend is a Spring Boot application located in the `back/` folder.

- Spring Boot starter parent version: `4.0.6`
- Java version: `21`
- Maven is used for build and dependency management
- Spring modules included: Web MVC, Security, Data JPA, Liquibase, OpenAPI
- Runtime database: PostgreSQL
- Test database: H2 in-memory

### Running the backend

From the `back/` directory:

```bash
mvn spring-boot:run
```

Build the backend jar:

```bash
mvn clean package
```

Run tests:

```bash
mvn test
```

### Backend config

- Main config: `src/main/resources/application.yaml`
- Environment values: `src/main/resources/env.properties`
- Example env file: `.env.sample.properties`

For release notes, see `RELEASE.md`.
