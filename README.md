# API Test Automation

API test automation project built with Java, REST Assured, JUnit 5 and Maven.

The project provides automated API testing for a microservice-based backend with authentication and user management functionality.

## Tech Stack

- Java 25
- Maven
- REST Assured
- JUnit 5
- Gson
- JWT
- JSON Schema
- Allure
- Docker
- Docker Compose
- GitHub Actions

## Project Structure

```text
src/test/java
├── base
├── client
│   ├── AuthClient
│   └── UserClient
├── config
│   └── Config
├── data
│   ├── ErrorResponse
│   ├── LoginRequest
│   ├── UserRequest
│   └── UserResponse
├── tests
│   ├── auth
│   │   ├── AuthorizationTests
│   │   └── LoginTests
│   └── user
│       ├── UserContractTests
│       ├── UserCrudTests
│       ├── UserFilterTests
│       ├── UserProtocolTests
│       └── UserValidationTests
└── utils
    └── JwtUtils

src/test/resources
└── schemas
    ├── error-response-schema.json
    └── users-response-schema.json
```

## Test Coverage

### Authentication

- Login API
- Authentication scenarios
- Authorization scenarios
- JWT-based authorization

### User API

- CRUD operations
- User validation
- User filtering
- HTTP protocol behavior
- Response contract validation

### Contract Testing

API responses are validated against JSON schemas using JSON Schema validation.

## API Clients

API requests are encapsulated in dedicated client classes:

- `AuthClient` — authentication-related requests
- `UserClient` — user-related API requests

This keeps API communication separate from test scenarios.

## Test Data

Request and response models are represented by dedicated Java classes:

- `LoginRequest`
- `UserRequest`
- `UserResponse`
- `ErrorResponse`

## Configuration

Project configuration is centralized in the `Config` class.

## JWT

`JwtUtils` provides JWT-related utility functionality used by authentication and authorization tests.

## Allure

Allure is used to generate test execution reports.

To generate the report locally:

```bash
mvn allure:report
```

The generated report can be found in:

```text
target/site/allure-maven-plugin
```

## Docker

The project uses Docker Compose to run the backend services required for API testing.

Start the services:

```bash
docker compose up -d
```

Stop the services:

```bash
docker compose down
```

## Running Tests

Run all API tests:

```bash
mvn clean test
```

## CI/CD

The project uses GitHub Actions for automated test execution.

The pipeline:

1. Checks out the test project.
2. Sets up JDK 25.
3. Checks out the required backend services.
4. Builds the backend services with Maven.
5. Builds Docker images.
6. Starts backend services using Docker Compose.
7. Runs API tests.
8. Generates an Allure report.
9. Uploads the Allure report as a GitHub Actions artifact.
10. Stops the backend services.

The workflow can be triggered by:

- Push to `main`
- Pull request to `main`
- Manual workflow dispatch

## Project Goals

The project demonstrates practical experience with:

- API test automation
- REST Assured
- Java
- JUnit 5
- API clients
- DTO/request/response models
- Authentication and authorization testing
- JWT
- JSON Schema validation
- Docker
- Docker Compose
- CI/CD with GitHub Actions
- Allure reporting