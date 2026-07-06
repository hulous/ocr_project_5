# MondeDeDev Backend API Reference

Base URL: `http://localhost:8018`

All endpoints use JSON request and response bodies.

## Authorization

- Protected endpoints require a JWT bearer token.
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

- `TopicResponse`:
  - `id` (integer)
  - `title` (string)
  - `description` (string)

- `PostResponse`:
  - `id` (integer)
  - `authorId` (integer)
  - `topicId` (integer)
  - `title` (string)
  - `content` (string)
  - `createdAt` (string)
  - `updatedAt` (string)

- `PostDetailResponse`:
  - `id` (integer)
  - `authorId` (integer)
  - `topicId` (integer)
  - `title` (string)
  - `content` (string)
  - `createdAt` (string)
  - `updatedAt` (string)
  - `comments` (array)

- `CommentResponse`:
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

### PUT /api/auth/me

Update the currently authenticated user profile.

Requires Authorization header.

Request body example:

```json
{
  "email": "alice.updated@example.com",
  "password": "NewStr0ngP@ssword",
  "username": "Alice M."
}
```

Success response: `UserResponse`

## Topic Endpoints

### GET /api/topics

List all topics.

Success response: array of `TopicResponse`

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

Success response: array of `PostResponse`

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

Success response: `PostResponse`

## Post Endpoints

### GET /api/posts/{postId}

Get one post by ID.

Success response: `PostDetailResponse`

### POST /api/posts/{postId}/comments

Create a comment for a post.

Requires Authorization header.

Request body:

```json
{
  "content": "I found this article very useful."
}
```

Success response: `CommentResponse`

## OpenAPI Documentation

When the backend is running, the OpenAPI UI is available at:

- `http://localhost:8018/swagger-ui/index.html`

Raw OpenAPI JSON:

- `http://localhost:8018/v3/api-docs`
