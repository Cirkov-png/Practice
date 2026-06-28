# Changelog

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
