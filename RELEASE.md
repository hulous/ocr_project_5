# Release Notes

## 1.0.2

This release improves backend error handling, API error documentation, and topic querying.

- Updated Swagger/OpenAPI error documentation for authentication, post, and topic endpoints.
- Added a dedicated `UserAlreadyExistsException` and return `409 Conflict` when registering an existing user.
- Improved global exception handling to return `400 Bad Request` for illegal argument errors instead of `500`.
- Cleaned up backend topic query logic and enforced alphabetic ordering for topics.
- Refined topic-related controller documentation and error response handling.

## 1.0.1

This release updates documentation and release artifacts to reflect the `1.0.0` tag and adds API documentation links.

- Added backend `plant_diagram.uml` documentation.
- Added links to the API endpoints in the README.
- Updated release documentation with `1.0.0` tag information.

## 1.0.0

This release improves testing documentation, validation coverage, and backend test execution.

- Added `TEST_REPORT.md` to document front-end and back-end test results, coverage, and report locations.
- Updated front-end test coverage with Jest and generated the `front/coverage/` report.
- Resolved backend Mockito/ByteBuddy initialization issues and reran all backend tests successfully.
- Generated backend Jacoco coverage reports in `back/target/site/jacoco/`.
- Cleaned up unused test imports in backend test code.

## 0.0.12

This release improves the front-end post creation experience.

- Updated the post creation page and form UI.
- Refined post creation behavior and related styles.

## 0.0.11

This release adds front-end user profile management and back-end user update support.

- Added a user profile component and update form in the Angular front-end.
- Added `UpdateUserDto` and `UserUpdateService` in the backend.
- Improved authenticated user handling and API response shape consistency.
- Updated styles for the user profile experience.

## 0.0.10

This release adds comments on posts.

- Added comment model, mapper, service, and response support in the backend.
- Added front-end comment create and display support in post detail pages.
- Added new UI assets and styling for comment submission.

## 0.0.9

This release adds front-end post browsing and creation pages.

- Added post index, show, and create pages to the Angular front-end.
- Added front-end post model and service support.
- Updated styles and navigation for posts.

## 0.0.8

This release adds topic browsing and subscription support.

- Added topic listing and topic subscription features in the front-end.
- Added topic service, topic response DTO, and updated backend topic behavior.
- Added header bar improvements and topic page styling.

## 0.0.7

This release fixes authenticated user loading and topic/subscription data retrieval.

- Ensures user login loads topic and subscription data together.
- Refined back-end user response mapping and repository behavior.

## 0.0.6

This release adds backend integration tests.

- Added authentication controller integration tests.
- Added topic post creation integration tests.
- Added topic subscription integration tests.
- Improved Spring Boot test coverage for JWT and user flows.

## 0.0.5

This release refines API response handling and documentation.

- Updated response DTOs, mappers, and exception handling for API responses.
- Added more robust backend error conditions and response validation.
- Updated backend API documentation and README content.

## 0.0.4

This release implements topic, post, and comment domain support in the backend.

- Added `TopicsController` and `PostsController` with topic and post endpoints.
- Added `TopicService`, `PostService`, and create DTOs for posts and comments.
- Added topic and post data mappings and persistence support.

## 0.0.2

This release implements the initial backend data model and entities.

- Added entity classes for topics, posts, comments, and subscriptions.
- Added DTOs and response objects for topic and post domain models.
- Added Liquibase changelog updates for database schema.

## 0.0.1

This release builds the initial user authentication and application scaffold.

- Added Spring Boot backend with authentication, user management, security, OpenAPI, and JWT support.
- Added Angular front-end skeleton with login, register, and basic routing.
- Added initial backend and frontend tests, configuration, and project setup.
