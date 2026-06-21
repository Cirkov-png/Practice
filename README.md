# Learning Management System (LMS)

A Spring Boot and Java 21-based backend application that enables students to enroll in various courses using an internal virtual currency called coins. The platform features automated daily reminders for upcoming courses to keep students informed and engaged.

---

## Tech Stack
* **Programming Language:** Java 21
* **Frameworks & Libraries:** Spring Boot (Web, Data JPA, Actuator), Liquibase
* **Database:** H2 (In-Memory)
* **Tooling:** Maven, Lombok, MapStruct

---

## Current Status: Stage 1 – Spring Basics
The core architecture and baseline business workflows have been successfully implemented during this stage:
1. **Full CRUD Functionality:** Implemented for managing core domain models including Students, Courses, CourseSettings, and Lessons.
2. **Repository Isolation:** Strictly maintained design where repositories are only injected into their respective domain services to ensure decoupling.
3. **Package by Feature:** Structured the codebase around features rather than technical layers to maximize cohesion and maintainability.
4. **Race Condition Protection:** Secured critical financial operations (course enrollment and coin transactions) using protective DB-level mechanisms like pessimistic locking (`@Lock`).
5. **Bulkhead Pattern Integration:** Isolated email scheduling workloads by spinning up a dedicated, custom thread pool (`ThreadPoolTaskExecutor`) for asynchronous notifications.
6. **OSIV Disabled:** Disabled the `Open Session in View` feature to strictly enforce transaction boundaries and catch hidden N+1 fetch issues early.
7. **Production-Ready Features:** Enabled Spring Boot Actuator endpoints, request DTO validation via Hibernate Validator, and self-documenting REST APIs with Swagger/OpenAPI.

---

## Getting Started

### Prerequisites
Make sure you have JDK 21 and Maven installed.

### Installation & Launch
1. **Compile and build the project:**
   ```bash
   mvn clean compile