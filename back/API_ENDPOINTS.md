# MondeDeDev Backend API Reference

Base URL: `http://localhost:${MAIN_APP_PORT}`

All endpoints use JSON request and response bodies.

## Authorization

- Most protected endpoints require a JWT bearer token.
- Use the HTTP header:

```http
Authorization: Bearer <token>
```

The token is returned by `POST /api/auth/login`.

## Common response types

- `UserResponse`:
  - `id` (integer)
  - `username` (string)
  - `email` (string)
  - `createdAt` (string)
  - `updatedAt` (string)

- `LoginResponse`:
  - `token` (string)
  - `expiresIn` (number)

- `ApiMessageResponse`:
  - `message` (string)

- `TopicDto`:
  - `id` (integer)
  - `title` (string)
  - `description` (string)

- `PostDto`:
  - `id` (integer)
  - `authorId` (integer)
  - `topicId` (integer)
  - `title` (string)
  - `content` (string)
  - `createdAt` (string)
  - `updatedAt` (string)

- `CommentDto`:
  - `id` (integer)
  - `authorId` (integer)
  - `postId` (integer)
  - `content` (string)
  - `createdAt` (string)
  - `updatedAt` (string)

## Authentication Endpoints

### POST /api/auth/register

Register a new user.

Request body:

```json
{
  "email": "alice@example.com",
  "password": "Str0ngP@ssword",
  "username": "Alice Martin"
}
```

Success response: `UserResponse`

### POST /api/auth/login

Authenticate and receive a JWT token.

Request body:

```json
{
  "email": "alice@example.com",
  "password": "Str0ngP@ssword"
}
```

Alternative request field name: `login`

Success response: `LoginResponse`

### GET /api/auth/me

Get the currently authenticated user.

Requires Authorization header.

Success response: `UserResponse`

## User Endpoints

### GET /api/user/{id}

Get one user by ID.

Requires Authorization header.

Success response: `UserResponse`

## Topic Endpoints

### GET /api/topics

List all topics.

Success response: array of `TopicDto`

### POST /api/topics/{topicId}/subscription

Subscribe the current authenticated user to a topic.

Requires Authorization header.

Success response: `ApiMessageResponse`

### DELETE /api/topics/{topicId}/subscription

Unsubscribe the current authenticated user from a topic.

Requires Authorization header.

Success response: `ApiMessageResponse`

### GET /api/topics/{topicId}/posts

List all posts in a topic.

Success response: array of `PostDto`

### POST /api/topics/{topicId}/posts

Create a new post in a topic.

Requires Authorization header.

Request body:

```json
{
  "title": "How to use the platform",
  "content": "This post explains how to create a topic post."
}
```

Success response: `PostDto`

## Post Endpoints

### GET /api/posts/{postId}

Get one post by ID.

Success response: `PostDto`

### POST /api/posts/{postId}/comments

Create a comment for a post.

Requires Authorization header.

Request body:

```json
{
  "content": "I found this article very useful."
}
```

Success response: `CommentDto`

## OpenAPI Documentation

When the backend is running, the OpenAPI UI is available at:

- `http://localhost:${MAIN_APP_PORT}/swagger-ui/index.html`

Raw OpenAPI JSON:

- `http://localhost:${MAIN_APP_PORT}/v3/api-docs`
