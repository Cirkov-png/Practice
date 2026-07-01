# Changelog

## [1.1.0] - 2026-06-30

### Stage 2 – Security

#### Added
- `spring-boot-starter-security` and `spring-boot-starter-oauth2-resource-server` dependencies
- `SecurityConfig` (`@Profile("!cloud")`) — Basic Auth for local environment
  - API endpoints accessible to all authenticated users
  - Actuator endpoints restricted to `MANAGER` role only
  - In-memory users: `user` (role USER) and `manager` (roles USER + MANAGER)
- `CloudSecurityConfig` (`@Profile("cloud")`) — OAuth2/XSUAA for cloud environment
  - API secured via JWT tokens from XSUAA service
  - Actuator secured via Basic Auth with `MANAGER` role (`/health` is public)
  - Two separate `SecurityFilterChain` beans with `@Order`
- Swagger UI `Authorize` button via Basic Auth security scheme in `OpenApiConfig`
- `docker-compose.yml` with PostgreSQL 17 for local development

#### Changed
- Switched from H2 in-memory to PostgreSQL (port `5433` locally via Docker)
- H2 kept as `test` scope for unit and integration tests
- Project version bumped to `1.1.0`
- `OpenApiConfig` version updated to `1.1.0`

#### Configuration
- User credentials externalized via env variables: `SECURITY_USER_NAME`, `SECURITY_USER_PASSWORD`
- Manager credentials externalized via env variables: `SECURITY_MANAGER_NAME`, `SECURITY_MANAGER_PASSWORD`
- Database credentials externalized via env variables: `DB_USERNAME`, `DB_PASSWORD`
- XSUAA issuer URI externalized via `${XSUAA_URL}` (cloud profile only)

---

## [1.0.0] - 2026-06-24

### Stage 1 – Spring Basics

#### Added
- Spring Boot project backbone with `pom.xml` (Java 21, Spring Boot 3.4.2)
- H2 in-memory database configuration
- Liquibase database migrations for all domain tables: `students`, `courses`, `course_settings`, `lessons`, `enrollments`
- Full CRUD REST API for `Student`, `Course`, `CourseSettings`, `Lesson`
- `Enrollment` entity with coin-based payment logic (pessimistic locking to prevent race conditions)
- Value Objects: `Money` and `Email` (`@Embeddable` records) to prevent Primitive Obsession
- `GlobalExceptionHandler` with semantic exception hierarchy (`LmsException` → `StudentException`, `CourseException`, `EnrollmentException`)
- Request DTO validation via Hibernate Validator (`@Valid`, `@NotBlank`, `@Future`, etc.)
- Spring Boot Actuator endpoints: `health`, `info`, `loggers`
- Swagger / OpenAPI documentation via springdoc
- Daily cron job (`CourseReminderJob`) to send email reminders for courses starting tomorrow
- Dedicated async thread pool (`emailTaskExecutor`) for email sending — Bulkhead Pattern
- `EmailNotificationServiceImpl` using `JavaMailSender` with `MimeMessage` and Mailtrap SMTP
- `NotificationTestController` (dev profile only) for manual reminder trigger
- `OSIV` disabled (`open-in-view: false`)
- MapStruct mappers for all domain models
- Package-by-feature project structure
- Unit tests: `MoneyTest`, `EnrollmentServiceImplTest`, `CourseReminderServiceTest`
- Integration tests: `EnrollmentIntegrationTest`
- `TestDataFactory` utility class for test data generation

#### Configuration
- All secrets (`MAIL_USERNAME`, `MAIL_PASSWORD`) via environment variables
- Cron expression externalized to `application.yml` via `${COURSE_REMINDER_CRON}`
- Mail `from` address externalized via `${MAIL_FROM}`
