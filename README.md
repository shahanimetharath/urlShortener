# URL Shortener

A minimal but production-ready URL Shortener API built with Spring Boot 3 and Kotlin/Java.  
This project demonstrates how to design a maintainable, scalable REST service with clean architecture and modern practices.

---

## Features

- Shorten the given URLs → Generate a short ID for any given URL.
- Redirect → Resolve a short ID back to its original URL with HTTP redirects.
- In-memory database (H2) → For local dev/testing, easily swappable with Postgres/MySQL.
- RESTful API → JSON endpoints for programmatic use.
- Spring Boot ecosystem → JPA, logging.
- Extensible design → Ready to replace H2 with PostgresDB / Redis for caching, authentication, or analytics.
                    → Short code generation logic can be easily modified if requirements change to avoid DB lookups on hot path and ensure distributed uniqueness.

---

## Tech Stack

- Java 17 / Kotlin
- Spring Boot 3.5.6
  - `spring-boot-starter-web` → REST API
  - `spring-boot-starter-data-jpa` → persistence
  - `spring-boot-starter` → core
- H2 Database (default; use Postgres in production)
- Maven for build & dependency management

---

## Getting Started

### Prerequisites
- JDK 17+
- Maven 3.9+
- (Optional) Docker if running with containers

### Run locally

```bash
# build
./mvnw clean install

# run
./mvnw spring-boot:run




# run
./mvnw spring-boot:run
