# ConBuzz Backend

A Spring Boot REST API for ConBuzz — a community platform for convention-goers to discuss panels, cosplay, events, and everything in between.

> Early development — domain model layer complete.

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Security | Spring Security + JWT |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Build tool | Maven |
| Utilities | Lombok, Jackson |

---

## Project structure

Feature-based packaging — every file related to a domain concept lives together.

```
src/main/java/com/rikko/con_buzz_backend/
├── ConBuzzApplication.java
│
├── user/                   # user accounts and roles
├── convention/             # convention events that host channels
├── channel/                # topic-based channels within a convention
├── post/                   # posts created inside channels
├── comment/                # comments on posts
├── reaction/               # likes and dislikes on posts
│
└── shared/
    ├── config/             # Spring Security, CORS, Jackson
    ├── security/           # JWT filter, JWT service, UserPrincipal
    ├── exception/          # global exception handler, custom exceptions
    └── util/               # time utilities, mapper helpers
```

---

## Domain model

```
Convention
  └── Channel (many)
        └── Post (many)
              ├── Comment (many)
              └── Reaction (many)

User
  ├── authored Posts
  ├── authored Comments
  └── authored Reactions
```

### Enums

| Enum | Values                          | Package |
|---|---------------------------------|---|
| `Role` | `USER`, `MOD`, `ADMIN`          | `user/` |
| `PostStatus` | `PUBLISHED`, `DRAFT`, `DELETED` | `post/` |
| `ChannelStatus` | `ACTIVE`, `ARCHIVED`            | `channel/` |
| `ReactionType` | `LIKE`, `DISLIKE`               | `reaction/` |

---

## Things to work on

- [ ] Repository layer — JPA repositories and custom queries for each feature
- [ ] Service layer — business logic, reaction toggle, soft delete, lock/pin
- [ ] Controller layer — REST endpoints wired to services
- [ ] DTO mapping — map entities to response objects
- [ ] Auth endpoints — `/register` and `/login` returning a JWT
- [ ] Request validation — `@NotBlank`, `@Size`, `@Email` on request DTOs
- [ ] Pagination — `Page<T>` with `Pageable` on all list endpoints
- [ ] Tests — repository, service, and auth integration tests