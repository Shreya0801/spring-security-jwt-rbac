# Spring Security JWT RBAC

A Spring Boot REST API demonstrating stateless authentication with **JWT**, **role- and permission-based access control**, **refresh-token session management**, and **OAuth2 login**.

## Features

- **JWT auth**: short-lived access token (5 min) + HttpOnly refresh-token cookie
- **RBAC**: three roles (`USER`, `CREATOR`, `ADMIN`) mapped to fine-grained permissions
- **Session limit**: max 2 active sessions per user; the least recently used is evicted
- **OAuth2 login**: social sign-in that issues the same JWTs
- **Method-level security**: `@Secured` and `@PreAuthorize`, including post-ownership checks
- **Global exception handling** with a consistent error response

## Tech Stack

Java 21 · Spring Boot 4.1 · Spring Security · Spring Data JPA · MySQL · JJWT 0.12 · ModelMapper · Lombok · Maven

## Roles & Permissions

| Role      | Permissions                                                              |
|-----------|--------------------------------------------------------------------------|
| `USER`    | `POST_VIEW`, `USER_VIEW`                                                 |
| `CREATOR` | `POST_CREATE`, `POST_UPDATE`, `USER_UPDATE`                              |
| `ADMIN`   | `POST_CREATE`, `POST_UPDATE`, `POST_DELETE`, `USER_CREATE/UPDATE/DELETE` |

## API

| Method | Endpoint        | Access                          |
|--------|-----------------|---------------------------------|
| POST   | `/auth/signup`  | Public                          |
| POST   | `/auth/login`   | Public                          |
| POST   | `/auth/refresh` | Public (needs refresh cookie)   |
| GET    | `/posts`        | `USER`                          |
| GET    | `/posts/{id}`   | Post owner only                 |
| POST   | `/posts`        | `ADMIN` / `CREATOR`             |
| PUT    | `/posts/{id}`   | `POST_UPDATE`                   |
| DELETE | `/posts/{id}`   | `POST_DELETE`                   |

Send the access token as `Authorization: Bearer <token>`.

## Getting Started

**Prerequisites:** JDK 21, MySQL

1. Clone the repo
   ```bash
   git clone https://github.com/Shreya0801/spring-security-jwt-rbac.git
   cd spring-security-jwt-rbac
   ```

2. Create `src/main/resources/application.properties` (git-ignored):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/<db_name>
   spring.datasource.username=<user>
   spring.datasource.password=<password>
   spring.jpa.hibernate.ddl-auto=update

   jwt.secretKey=<random-string-of-at-least-32-chars>
   deploy.env=development

   # OAuth2 client (e.g. Google)
   spring.security.oauth2.client.registration.google.client-id=<id>
   spring.security.oauth2.client.registration.google.client-secret=<secret>
   ```

3. Run
   ```bash
   ./mvnw spring-boot:run
   ```

The app starts on `http://localhost:8080`. Open `/home.html` for the OAuth2 login demo.

## Quick Test

```bash
# Sign up
curl -X POST localhost:8080/auth/signup -H "Content-Type: application/json" \
  -d '{"name":"Shreya","email":"shreya@example.com","password":"secret123"}'

# Log in (returns accessToken, sets refreshToken cookie)
curl -i -X POST localhost:8080/auth/login -H "Content-Type: application/json" \
  -d '{"email":"shreya@example.com","password":"secret123"}'
```

## Project Structure

```
config/      Security filter chain
filter/      JwtAuthFilter
service/     Auth, JWT, Session, User, Post
controller/  Auth and Post endpoints
handler/     OAuth2 success handler
advice/      Global exception handling
entity/      JPA entities, Role and Permission enums
```
