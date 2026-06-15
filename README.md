# CosPlace Backend

A Spring Boot REST API for CosPlace — a community platform for cosplayers to view listings from other cosplayers trying to declutter their cosplay wardrobe.

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
├── listing/                # listings created by users
├── like/                   # likes on listings
├── follow/                 # follows on profiles
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
User
  ├── authored Listings
  └── authored Likes
```

### Enums

| Enum            | Values                          | Package |
|-----------------|---------------------------------|---|
| `Role`          | `USER`, `MOD`, `ADMIN`          | `user/` |
| `ListingStatus` | `PUBLISHED`, `DRAFT`, `DELETED` | `listing/` |

---

## Things to work on

- [ ] Repository layer — JPA repositories and custom queries for each feature
- [ ] Service layer — business logic, like toggle, soft delete, lock/pin
- [ ] Controller layer — REST endpoints wired to services
- [ ] DTO mapping — map entities to response objects
- [ ] Auth endpoints — `/register` and `/login` returning a JWT
- [ ] Request validation — `@NotBlank`, `@Size`, `@Email` on request DTOs
- [ ] Pagination — `Page<T>` with `Pageable` on all list endpoints
- [ ] Tests — repository, service, and auth integration tests