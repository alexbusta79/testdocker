# SOLUTION.md

# Inditex Supplier Management – Technical Challenge Solution

## 1. Executive Summary

This solution has been designed as a production-oriented implementation of the Inditex Supplier Management challenge.

The primary objective was not only to satisfy the functional requirements but also to demonstrate architectural decision making, domain modelling capabilities and long-term engineering vision.

Special attention has been paid to:

* Domain integrity
* Explicit business workflows
* Separation of concerns
* Infrastructure independence
* Testability
* Maintainability
* Evolvability

The implementation intentionally prioritizes architectural sustainability over implementation speed.

The system has been designed around stable business capabilities while treating infrastructure as replaceable components.

The resulting solution aims to resemble how a production-grade system would be structured while remaining lightweight enough to be easily executed and evaluated.

---

# 2. Architectural Vision

The solution has been built following the principle:

> Business rules are the most stable component of the system. Infrastructure is a replaceable implementation detail.

The objective was to avoid building a simple CRUD application and instead build a business-oriented system capable of evolving without requiring major refactoring.

The architecture has been designed to provide:

* High testability
* Strong separation of concerns
* Infrastructure independence
* Explicit business workflows
* Scalability readiness
* Operational extensibility

---

# 3. Architectural Principles

## Domain First

Business rules are considered first-class citizens.

The domain layer is completely independent from:

* Spring Boot
* JPA
* HTTP
* WireMock
* H2
* React

## Separation of Concerns

Responsibilities are isolated into three layers:

* Domain
* Application
* Infrastructure

Each layer owns a single responsibility.

## Dependency Inversion

Business logic depends only on abstractions (ports).

Infrastructure components depend on business layers, never the opposite.

## Explicit Use Cases

Every business capability is represented by an explicit command or query.

Business behaviour is self-documented.

## Replaceability

External technologies can be replaced without impacting business logic.

Examples:

* H2 → PostgreSQL
* WireMock → Real Country Service
* RestClient → WebClient
* React → Angular

No business code changes would be required.

---

# 4. Technology Stack

## Backend

* Java 21
* Spring Boot 3.3.5
* Spring Data JPA
* Spring Validation
* H2 Database
* Maven

## Frontend

* React
* Vite
* TypeScript

## Testing

* JUnit 5
* Mockito
* Spring Boot Test
* MockMvc

## External Integration

* WireMock

## Documentation

* OpenAPI 3.1

## Containerization

* Docker Compose

---

# 5. Solution Architecture

High-level architecture:

```text
┌───────────────────┐
│ React Dashboard   │
└─────────┬─────────┘
          │
          ▼
┌───────────────────┐
│ Spring Boot API   │
└─────────┬─────────┘
          │
 ┌────────┼─────────┐
 ▼        ▼         ▼

Commands Services Queries

         ▼

Domain Layer

         ▼

Ports

 ┌───────┴─────────┐

 ▼                 ▼

JPA Adapter   HTTP Adapter

 ▼                 ▼

H2 Database    WireMock
```

---

# 6. Backend Architecture

The backend follows Hexagonal Architecture (Ports and Adapters).

Main flow:

```text
Controller
 ↓
Command / Query
 ↓
Command Handler / Query Handler
 ↓
Application Service
 ↓
Domain Model
 ↓
Repository Port
 ↓
JPA Adapter
 ↓
H2 Database
```

External integration:

```text
SupplierLifecycleService
 ↓
CountryInformationPort
 ↓
HttpCountryInformationAdapter
 ↓
WireMock Country Service
```

The domain layer never depends on infrastructure.

---

# 7. Architectural Decision Records (ADR)

## ADR-001 – Hexagonal Architecture

Decision:

Hexagonal Architecture has been selected to isolate business rules from infrastructure concerns.

Motivation:

The challenge contains several infrastructure dependencies:

* Database
* REST API
* External Country Service
* Frontend Dashboard

Without clear boundaries, business rules would quickly become coupled to frameworks.

Benefits:

* Improved testability
* Improved maintainability
* Infrastructure independence

Trade-off:

Additional boilerplate and more classes.

This trade-off is considered acceptable.

---

## ADR-002 – WireMock

Decision:

The external country service has intentionally not been implemented in Java.

Motivation:

The challenge already provides WireMock mappings.

The backend should consume external systems, not recreate them.

Benefits:

* Realistic integration
* Better separation of responsibilities

Trade-off:

Additional runtime dependency.

---

## ADR-003 – Command Pattern

Decision:

A command-oriented application layer has been introduced.

Motivation:

Business use cases become explicit and independently testable.

Benefits:

* Thin controllers
* Centralized orchestration
* Self-documented workflows

Trade-off:

More classes are introduced.

---

# 8. Domain Modelling

## Ubiquitous Language

Candidate

A company requesting supplier onboarding.

Supplier

A company approved to operate with Inditex.

On Probation

A supplier under supervision.

Disqualified

A supplier permanently banned.

Potential Supplier

A supplier eligible to fulfil a specific order amount.

Country Validation

External compliance verification performed before onboarding.

---

# 9. Business Workflow

## Candidate Creation

Rules:

* One active candidate per DUNS.
* Candidate and Supplier cannot coexist.
* Disqualified suppliers cannot reapply.

Returns:

```text
201 Created
```

---

## Candidate Acceptance

Validation:

* Candidate exists.
* Country is not banned.
* Annual turnover >= 1M €.
* Sustainability rating is provided.

Ratings:

A,B → Active

C,D,E → On Probation

Returns:

```text
204 No Content
```

---

## Candidate Refusal

A refused candidate may reapply.

Returns:

```text
204 No Content
```

---

## Supplier Ban

Only suppliers on probation may be disqualified.

Once disqualified:

```text
Disqualified
```

The supplier cannot reapply.

Returns:

```text
204 No Content
```

---

# 10. Potential Suppliers

Eligibility:

```text
annualTurnover > rate

AND

status != DISQUALIFIED
```

Score:

```text
score = annualTurnover × 0.1 × ratingConstant
```

Constants:

A = 1

B = 0.75

C = 0.5

D = 0.25

E = 0.1

---

## Small Supplier Bonus

A 25% bonus is applied when a supplier belongs to one of the two lowest unique turnovers of its country.

Ordering:

```text
score DESC
```

Pagination is applied afterwards.

---

# 11. OpenAPI Strategy

The provided OpenAPI specifications are considered the source of truth.

The implementation follows them for:

* Endpoint paths
* Request payloads
* Response payloads
* Validation rules
* Error structures

The backend intentionally consumes the Country API instead of reimplementing its behaviour.

---

# 12. Frontend Architecture

The frontend intentionally focuses only on the dashboard requirements.

Implemented:

* Amount validation
* Loading state
* Error state
* Empty state
* Search by DUNS
* Search by Name
* Country filter
* Rating filter
* Column sorting
* Pagination
* Total results count
* Euro formatting

Administrative operations were intentionally excluded because they were not requested.

---

# 13. Quality Assurance Strategy

Three testing levels have been implemented.

## Domain Tests

Validate pure business logic.

## Application Service Tests

Validate use cases independently from infrastructure.

## Integration Tests

Validate:

* HTTP status codes
* JSON payloads
* Persistence behaviour
* Pagination
* OpenAPI compliance

The country integration is isolated behind a port, allowing both unit and integration testing.

---

# 14. Scalability Considerations

The challenge mentions supplier volumes up to 1 million records.

Potential future optimizations:

* SQL filtering
* Read models
* CQRS
* Materialized Views
* Country threshold caching

Recommended indexes:

```sql
CREATE INDEX idx_supplier_duns ON suppliers(duns);

CREATE INDEX idx_supplier_status ON suppliers(status);

CREATE INDEX idx_supplier_country_turnover
ON suppliers(country, annual_turnover);
```

---

# 15. Resilience Strategy

The country service is an external dependency.

Configuration:

```yaml
fail-open=false
```

This decision was intentional.

If the external service is unavailable, the candidate acceptance flow fails instead of accepting a potentially banned supplier.

This is safer from a compliance perspective.

---

# 16. Risk Assessment

## Country Service Unavailability

Risk:

A banned supplier could accidentally be accepted.

Mitigation:

fail-open=false

---

## Database Growth

Risk:

Potential supplier queries may become expensive.

Mitigation:

Indexes and future read optimization.

---

## Business Rule Complexity

Risk:

Lifecycle transitions become difficult to maintain.

Mitigation:

Explicit lifecycle modelling.

---

# 17. Production Evolution

If this system evolved into production, I would introduce:

## Observability

* OpenTelemetry
* Prometheus
* Grafana

## Resilience

* Resilience4j
* Circuit Breakers
* Retry Policies

## Security

* OAuth2
* JWT
* RBAC

## Database

* PostgreSQL

## CI/CD

* SonarQube
* SAST
* Dependency Scanning
* Contract Testing

## API Gateway

* Rate Limiting
* Authentication
* Monitoring

These improvements have intentionally been left out because they are outside the scope of the challenge.

The architecture already supports their future integration.

---

# 18. Technical Debt

Current technical debt is intentionally low.

Known simplifications:

* H2 instead of PostgreSQL.
* Manual DTO implementation instead of OpenAPI code generation.
* Synchronous country service calls.

These decisions were consciously taken to reduce complexity while preserving architectural quality.

---

# 19. Local Execution

## Start WireMock

```bash
java -jar wiremock-standalone-3.9.1.jar --port 8088 --root-dir ./wiremock
```

## Start Backend

```bash
cd backend

mvn spring-boot:run
```

Backend:

```text
http://localhost:8080
```

H2 Console:

```text
http://localhost:8080/h2-console
```

## Start Frontend

```bash
cd frontend

npm install

npm run dev
```

Frontend:

```text
http://localhost:5173
```

---

# 20. Docker Execution

```bash
docker compose up --build
```

Starts:

* Frontend
* Backend
* WireMock

The backend automatically connects to the external country service through Docker networking.

---

# 21. Final Statement

The purpose of this implementation was not to deliver a simple CRUD application.

The objective was to demonstrate:

* Architectural judgement
* Technical leadership
* Domain modelling
* Infrastructure isolation
* Operational awareness
* Long-term engineering vision

The resulting solution aims to resemble how a production-grade system would be structured while remaining lightweight enough to be executed and evaluated during a technical challenge.
