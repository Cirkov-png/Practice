# Stage 1 – Spring Basics

# Functional requirement [FR]

* The system should provide full **CRUD** functionality for managing [domain models](../Onboarding%20Project%20-%20Learning%20Management%20System.md#domain-models). 
* It must run a **daily** automated job to identify courses starting the next day and send email notifications to all enrolled students using a dedicated thread pool. 
* The system needs to allow students to purchase courses using an internal currency, coins, verifying that they have a sufficient balance before the transaction.

## Steps
1. **[SPRING]** Create Spring Boot project backbone with `pom.xml`
2. **[DB]** Setup database schema using Liquibase and configure `H2 in-memory` database
3. **[DB]** CRUD for Students and Courses (Use Lombok and mapping tools (e.g. Mapstruct))
4. **[SPRING]** Add error handler
5. **[WEB]** Enable `Spring Boot Actuator` (health, info, logger)  + Logs
6. **[WEB]** Add `Swagger` for REST API
7. **[WEB]** Add validation for DTOs  
8. **[SPRING]** Disable `Open Session in View` (OSIV) feature and try to retest API
9. **[FR]** Implement job triggered daily to collect a list of Courses that start tomorrow
10. **[JOB]** Create a custom thread pool and send each email using threads from it
11. **[FR]** Implement logic for sending a notification message via `email` to enrolled Students (e.g. using [Mailtrap](https://mailtrap.io/)) 
12. **[FR]** Implement logic for coin-based payment (Student buys Courses using coins)
13. **[TEST]** Cover logic with unit and integration tests
14. **[VERSION]** Create `CHANGELOG.md` file and fill with actual changes description (Remember to update it after each stage!)


# Achievements

#### [SPRING]

1. Spring Boot Actuator  
2. OSIV
3. Swagger 
4. Validation
5. @RestController
6. @RestControllerAdvice
7. @ConfigurationProperties
8. @Transactional
9. @Async
10. @Lock
11. @Scheduled

#### [WEB]

1. REST API conventions  

#### [DB]

1. JPA/Hibernate
2. Transaction isolation level  
3. Eager/Lazy fetch types  
4. N+1 problem
5. Liquibase  

#### [TOOLING]
1. Mapstruct 
2. Lombok

#### [TEST]
1. JUnit  
2. Mockito  

#### [VERSION]
1. CHANGELOG.md

## Advices

- [Value Objects](https://dev.to/kirekov/spring-boot-power-of-value-objects-1oah)
- [Logging Strategies](https://www.papertrail.com/solution/tips/logging-in-java-best-practices-and-tips/) 




# Comprehensive Code Review Guideline

This document is a set of rules and recommendations based on previous reviews, aimed at maintaining high quality, readability, and performance of code.

---

## 1. Architecture and Design

* **Value Objects (VO):** Avoid "Primitive Obsession" ([Primitive Obsession](https://refactoring.guru/smells/primitive-obsession)). Instead of `String email` or `BigDecimal price`, use `ValueObject` classes (for example, `record Email(String value)`).
* **DRY (Don't Repeat Yourself):** Don't duplicate logic. If you need to retrieve an entity, use an existing service method (for example, `userService.findById(id)`), rather than accessing the repository (`userRepository.findById(id)`) directly from another class.
* **Use interfaces (Strategy Pattern):** If you anticipate that an implementation (such as payment method, delivery method) may change or there will be multiple implementations, use interfaces.
* **Repository Isolation:** A repository (for example, `StudentRepository`) should be injected **only** into its "domain" service (`StudentService`). Other services (for example, `CourseService`) should not access `StudentRepository` directly.
* **Immutability:** Prefer immutable objects (`record`, `final` fields). Creating new objects instead of modifying old ones makes code thread-safe by default.
* **Validation:** Complex business validation should be moved to separate validator classes, rather than mixing it with business logic in services.
* **Code Nesting**: Avoid deep nesting (Nesting Hell). If you see 3-4 levels of `if/for` inside a method, it's a sign of poor design. Extract nested logic into separate, well-named method.
* **Entry Point**: The main application class (with the `@SpringBootApplication` annotation) should be "clean" and contain only the `main` method.
* **Configuration Files (@Configuration)**:
  * Configuration beans should not be located in the main application class.
  * Each configuration should be in its own file (for example, `SwaggerConfiguration`, `SecurityConfiguration`).
  * Configuration files should be located in the appropriate packages (...config.swagger or ...security).
* **Idempotency**: Idempotent requests are safer than their regular counterparts; consider this when developing logic.

---

## 2. API, DTO, and `record`

* **Correct HTTP Statuses:** Use the right response codes.
  * **201 Created:** For successful resource creation (`POST`).
  * **204 No Content:** For successful deletion (`DELETE`) when there's nothing to return in the body.
  * **200 OK:** For successful `GET` and `PUT`/`PATCH`.
* **DTO Separation:** Clearly separate DTOs for requests and responses (`RequestDto` / `ResponseDto`). If necessary, there can be multiple RequestDto or ResponseDto.
* **API Granularity:** Endpoints should be focused.
  * `GET /api/v1/students/{id}` should not return the student's courses.
  * For related data, there should be a separate endpoint: `GET /api/v1/students/{id}/courses`.
  * For performing actions, use separate endpoints (`POST /api/v1/enrollments` - if represented as an entity / `POST /api/v1/students/{studentId}/courses/{courseId}`), rather than overloading `PUT /api/v1/students/{id}`.
* **Use `record`:** For DTOs, Value Objects, and other "data carriers," use `record` instead of `class`.
* **Mapping (MapStruct):** Use MapStruct for mapping (Entity <-> DTO). MapStruct can inject other mappers or Spring beans (`@Context`, `@Autowired`) for complex logic.

---

## 3. Performance and Asynchronicity

* **[Bulkhead Pattern](https://www.systemdesignacademy.com/blog/bulkhead-pattern)** (Isolation of Thread Pools and other resources):
  * Never use the default Spring thread pool (task executor) for `@Async` or scheduler.
  * Always create **separate thread pools** (Thread Pools) for different tasks (for example, one for jobs, one for asynchronous API requests). This prevents "cascading failures" when one slow task "eats up" all threads.
  * Use `@Async("mySpecificThreadPool")` along with `@Scheduled` to specify which [pool](https://habr.com/ru/articles/771112/) the task should run in.
* **N+1 Problem:** Make sure the `N+1` problem doesn't occur. All related data that will be used should, if possible, be loaded from the database in a single query (for example, via `JOIN FETCH` or `@EntityGraph`).
  * **Bad:** `list.stream().map(id -> repository.findById(id)).toList()`
  * **Good:** `repository.findAllByIds(listOfIds)`
* **Race Conditions:** Consider race conditions. When performing critical operations (course enrollment, payment, inventory management), make sure you use protective mechanisms (pessimistic/optimistic locks, transactions with the appropriate isolation level).

---

## 4. Testing

* **Test Naming:** Use the convention `[UnitOfWork_StateUnderTest_ExpectedBehavior]`.
  * **Example:** `enrollStudent_WhenCourseIsFull_ShouldThrowException`
* **Structure (Given-When-Then):** Clearly divide the test into three blocks using comments.
    ```java
    // given
    var student = TestDataGenerator.createDefaultStudent();
    var course = TestDataGenerator.createFullCourse();
    
    // when
    Result result = service.enrollStudent(student, course);
    
    // then
    assertFalse(result.isSuccess());
    // ...
    ```
* **Data Generators:** Don't create test data "manually" in each test. Use utility generator classes (for example, `TestDataGenerator`) to create valid entities.
* **Context Cleanup:** If tests use a shared database or Spring Context, use `@BeforeEach` or `@AfterEach` to clean up the state (for example, via `repository.deleteAll()`), so tests don't affect each other.

---

## 5. Database

* **`@ManyToMany` is rarely used in real life:** Instead of `@ManyToMany`, it's often necessary to create an explicit "Join entity" (for example, `Enrollment`) with two `@ManyToOne` relationships. This allows adding additional fields to the junction table (status, flag, audit).
* **Transaction Size:** Transactions (`@Transactional`) should be as "short" as possible and used only where necessary (usually on service methods that modify data).
* **Transaction Rollback:** Remember that by default, Spring does **not** roll back transactions for **checked exceptions**.
* **Migrations (Liquibase/Flyway):** In migration files, the `author` field should contain the developer's real email or identifier.

---

## 6. Error Handling and Logging

* **Exception Hierarchy:** Use custom, semantic exceptions.
  * Create a base exception (for example, `LmsException`).
  * Use nested static classes for specific errors: `StudentException.NotFound`.
  * **Example:** `throw new StudentException.NotFound(id);`
* **Error Localization:** When throwing an exception, pass an error code (`"student.not.found"`) and parameters (ID), not a ready-made message.
* **Logging Levels:**
  * `DEBUG`: Debugging information (method entry/exit).
  * `INFO`: Important business events (User X registered).
  * `WARN`: Non-critical errors.
  * `ERROR`: Exceptions that broke business logic.
* **Log Format:** Dynamic data (IDs, names) should always be at the end of the message. This simplifies searching in ElasticSearch.
  * **Good:** `Student not found by id: {}`, `id`

---

## 7. Naming

* **Meaningful Class Names:** Names should accurately reflect responsibility.
  * **Bad:** `ApplicationAspect`, `MainService`.
  * **Good:** `LoggingAspect`, `UserRegistrationService`.
* **Dependency Naming:** Inside a class, a dependency can be named by its role.
  * In `StudentService`: `private final StudentRepository repository;`
  * In `StudentController`: `private final StudentService service;`
* **Self-Documenting Code:** If the conjunction **"and"** appears in a method name (for example, `findUserAndValidate`), split it into two methods.
* **Collections:** Collection names should be plural (`List<User> users`).
* **DTOs:** Suffixes should follow CamelCase (`UserDto`, not `UserDTO`).

---

## 8. Java and Code Style

* **Constants:** Should be `static final` fields.
* **Using `var`:** Use `var` when the type is obvious from the right side (`final var list = new ArrayList<String>();`).
    ```java
        final List<String> list = new ArrayList<String>(); // Wrong (Redundant)
        final List<String> list = new ArrayList<>(); // Better, but discouraged
        final var list = new ArrayList<String>(); // Preferred
    ```
* **Annotation (jspecify):** For improved static analysis and explicit contract specification (for example, `@NonNull`, `@Nullable`), it's good practice to use annotations from the `jspecify` library.
* **Annotation Aggregation:** Use the minimum number of annotations. If a set of annotations (for example, for DTOs or controllers) is frequently repeated, create a new custom "aggregating" annotation.
* **Method Parameters:** A method should not contain many parameters. The maximum number is **three**. If there are more parameters, it's necessary to either split the method into several or create a parameter object (Parameter Object) to encapsulate them.
* **Modern `switch`:** Use only modern `switch` expressions (Java 14+) with the `->` syntax, as they are safer and more readable.
* **Pattern Matching `instanceof`:** Use "Pattern-matching `instanceof`" (type checks with immediate casting) for cleaner and safer code.
    ```java
    // Bad
    if (obj instanceof String) {
        String s = (String) obj;
        // ...
    }
    // Good
    if (obj instanceof String s) {
        // ...
    }
    ```
* **String Handling:**
  * **Text Blocks:** For multi-line text (SQL, JSON, XML), **always** use text blocks (Java 15+) instead of string concatenation.
  * **Formatting:** Prefer the `"...".formatted()` method (Java 15+) over `String.format()`.
* **Raw Types:** It is **forbidden** to use raw types. Generics should always be parameterized (for example, `List<String>`, not `List`). If parameterization is impossible, a comment explaining the reason should be provided.
* **Using [`Lombok @Builder`](https://projectlombok.org/features/Builder)`:** Use `builder` instead of constructor whenever possible.
    ```java
        // Wrong
        final var order = new Order("John Smith", "TestStreet 42", "FakeStreet 1", "Time Machine", 1, 1337.0f, false);
        final var updatedOrder = new Order(
                order.getName(),
                order.getShippingAddress(),
                order.getBillingAddress(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                true);
        
        // Correct
        final var order = Order.builder()
                .customerName("John Smith")
                .shippingAddress("TestStreet 42")
                .billingAddress("FakeStreet 1")
                .productName("Time Machine")
                .quantity(1)
                .price(1337.0f)
                .isExpressShipping(false)
                .build();
        final var updatedOrder = order.toBuilder().isExpressShipping(true).build();
    ```
* **`null` Checks and `Optional`:**
  * **Checks:** Use `Objects.nonNull(obj)`, `Objects.isNull(obj)`, and `CollectionUtils.isEmpty(list)` (Apache utils).
      ```java
          obj != null -> nonNull(obj)
          list != null && !list.isEmpty() -> CollectionUtils.isEmpty(list) 
      ```
  * **Returning `null`:** Avoid returning `null` from methods. Use `Optional` to represent an absent value.
  * **`Optional` in arguments:** It is **forbidden** to use `Optional` as a method argument. In such cases, use method overloading (one with a parameter, one without) or perform the check *before* calling the method.
* **Functional Style (Streams & Lambdas):**
  * **Lambda Size:** The preferred size of a lambda is **one line**. If the logic is more complex, it should be extracted into a separate private method (Method Reference).
  * **Side Effects:** Streams should not be used if the operation in them causes "side effects" (changing the state of external objects).
  * **Preference:** Prefer streams for multi-step, functional-style processing.
  * **Returning Stream:** In some cases (for example, in repositories), it may be more convenient and efficient to return a `Stream` instead of a ready-made collection.
* **Libraries:** `vavr` is a useful library for writing code in a functional style (for example, `Try` for exception handling, `Either` for returning a result or error).
* **Imports:**
  * Static imports (`import static...`) are preferred except in cases where it's better to explicitly indicate which package the dependency is from.
  * Wildcard imports (`*`) are forbidden. **Configure your IDE!**

---

## 9. Readability and Formatting

* **Don't Skimp on Variables:** Avoid long chains of calls. Use intermediate variables — this simplifies debugging and code reading.
  * **Bad:** `return mapper.toResponseDTO(repository.save(mapper.toEntity(studentDTO)));`
  * **Good:**
      ```java
          var studentFromDto = mapper.toEntity(studentDTO);
          var studentSaved = repository.save(studentFromDto);
          return mapper.toResponseDTO(studentSaved);
      ```
* **Line Length:** Maximum line length is **120 characters**.
* **End of File:** All `.java` files should end with one empty line (newline).
* **Logical Blocks**: Separate logical blocks in code (groups of fields in a class, stages in a method) with an empty line.
    ```java
        Example of fields in a record:
    
        @NonNull UUID id,
    
        @NonNull Instant createdAt,
        @NonNull String createdBy,
        @Nullable Instant updatedAt,
    
        @NonNull String firstName,
        @NonNull String lastName
    ```
* **Order**:
  * Lists (for example, in @Import) can be sorted alphabetically for easier searching.
      ```java
      @Import({
          AddressBookService.class,
          BtpFeatureFlagsService.class,
          Сache...
          ...
      })
      ```
  * Annotations can be sorted by meaning (for example, @Slf4j at the top) or by length (from longest to shortest).
      ```java
      @Slf4j
      @Aspect
      @Component
      ```
---

## 10. Configuration and Security

* **Format:** It's preferable to use `application.yml` (hierarchical, but sensitive to indentation) instead of `application.properties`.
* **Security:** It is **forbidden** to store passwords, API keys, and other secrets in `application.yml`. Use environment variables (`${DB_PASSWORD}`).
* **Scheduling:** `cron` expressions for `@Scheduled` should be moved to `application.yml`.
  * **Good:** `@Scheduled(cron = "${course.reminder.cron}")`

---

## 11. Project Management

* **Absence of Unused Code:**
  * There should be no commented-out lines or "dead" (uncalled) methods in the code.
  * There should be no unused dependencies in `pom.xml`.
  * `.gitignore` should only contain rules relevant to the project.
* **`pom.xml` Configuration:**
  * **Metadata:** `<description>`, `<groupId>`, `<artifactId>` should be correct (not `org.example`).
  * **Project Version**: The initial version of the project should be 1.0.0
  * **Dependency Versions:** All versions should be moved to constants in the `<properties>` section.
  * **Cleanliness**: There should be no empty, generated blocks (`<licenses>, <developers>, <scm>`).
  * **Dependency Grouping**: Dependencies in the `<dependencies>` section should be logically grouped using comments `<!-- Web--> <!-- Mail --> <!-- DB -->` and separated by an empty line for readability.



# Learning Management System

# Stack

* **Programming languages:** Java 21
* **Frameworks and Libraries**: Spring (MVC, Data, Security, AOP, Boot), JPA/Hibernate, Swagger, Liquibase, Mustache
* **Databases**: H2, PostgreSQL, HANA DB
* **Platforms**: SAP BTP
* **Technologies**: Maven, Docker, RabbitMQ
* **Testing**: JUnit, Mockito
* **VCS**: Git
* **SAP BTP Services**: HANA DB, HANA Schema, Application Autoscaler, XSUAA Service, Application Logging Service,
  Destination Service, Feature Flags Service, Service Manager, SaaS Provisioning Service, User-provided service
* **Development Tools**: IntelliJ IDEA, Postman

# Description

**Learning Management System** allows Students to enroll in variety of Courses using virtual coins. Each Course is
composed of multiple Lessons. To keep Students informed and engaged, platform provides automated reminders for upcoming
Courses.

# Goals

1. Provide **starter kit** with the most **important** and **frequently** **repeated** **tasks** and their **solutions
   in a short time**.
2. Good knowledge of **Spring Framework**:
    1. Cover the most **important** and **commonly** **used** **modules**;
    2. Cover main **annotations**;
    3. Provide understanding of Spring’s **main features**.
3. Good knowledge of **SAP BTP**, understanding of **the main services** and how to **integrate with their API**.
4. Prepare **Postman collection** with requests for required SAP BTP services API.


# Git

**Branches:** main, stage-\<stage\_number\>  
**PR naming:** \[STAGE-*\<stage\_number\>*\] *\<short\_description\>*

### **Example**

1. **Stage 1** has started
2. New branch **stage-1** is created from **main**
3. PR is created \- **\[STAGE-1\] Spring Basics**
4. Each step from stage is represented by individual commit (e.g. **\[STEP-1\] Create Spring Boot Project backbone**)
5. All commits are pushed
6. Review is finished and comments are fixed
7. PR is merged into **main** branch

![git-flow.png](img/git-flow.png)

# Versioning

For each Stage, application must have unique version. For example:

- **Stage 1** is started, version of application is **1.0.0**
- **Stage 2** is started, version is increased to **1.1.0** (minor part is changed)
- …

#  

# Packages Structure

Project packages structure should
follow [Package by Feature strategy](http://www.javapractices.com/topic/TopicAction.do?Id=205):

```
com.example.project
├── student
│   ├── controller
│   ├── service
│   ...
│   └── model
├── course
│   ├── controller
│   ├── service
│   ...
│   └── model
...
```

# Web API

API should follow REST architectural style and
follow [best practices](https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design):

```
/api/v1/courses
/api/v1/courses/1 ...
/api/v1/students
/api/v1/students/1
...
```

# Recommendations

1. Start Spring Boot project **from scratch**.

2. Add **only required maven dependencies** into pom.xml.

3. **Do not copy code** from different sources, write it by yourself to remember everything better.

4. **Read javadoc for each Spring annotation** which is used in application.

# Domain models

## Student

| Field                  | Type       |
|------------------------|------------|
| id                     | UUID       |
| firstName              | String     |
| lastName               | String     |
| email                  | String     |
| dateOfBirth            | LocalDate  |
| coins                  | BigDecimal |
| courses (Many-to-Many) | Course     |

## Course

| Field                   | Type           |
|-------------------------|----------------|
| id                      | UUID           |
| title                   | String         |
| description             | String         |
| price                   | BigDecimal     |
| coinsPaid               | BigDecimal     |
| settings (One-to-One)   | CourseSettings |
| lessons (One-to-Many)   | Lesson         |
| students (Many-to-Many) | Student        |


## CourseSettings

| Field     | Type          |
|-----------|---------------|
| id        | UUID          |
| startDate | LocalDateTime |
| endDate   | LocalDateTime |
| isPublic  | Boolean       |

## Lesson

| Field                | Type    |
|----------------------|---------|
| id                   | UUID    |
| title                | String  |
| duration             | Integer |
| course (Many-to-One) | Course  |


Stage 2 – Security
Functional requirement [FR]
The application's API endpoints must be accessible only to authenticated users.
This authentication will vary by environment: locally, it will use Basic Auth, while in the cloud, it will be secured by the XSUAA service with OAuth2.
The Spring Actuator endpoints will be specifically restricted to users with the "MANAGER" role in both local and cloud environments, also using Basic Auth.
The system must use a PostgreSQL database running in a Docker container for local development, replacing the H2 in-memory database.
Steps
[SECURITY] Store user details in memory for Basic Auth flow
[SECURITY] Secure API endpoints locally and allow access for all authenticated users (Basic Auth)
[SECURITY] Configure security for Spring Actuator endpoints locally and allow access for MANAGER role only (Basic Auth)
[SECURITY] Secure API endpoints in cloud using XSUAA service and allow access for all authenticated users (OAuth2)
[SECURITY] Configure security for Spring Actuator endpoints in cloud (Basic Auth) and allow access for MANAGER role only (except /health)
[DB] Run PostgreSQL in Docker and switch to it locally (H2 is removed)
Achievements
[SECURITY]
Security configuration
Basic Auth configuration
OAuth2 configuration
XSUAA service
[DB]
Docker
PostgreSQL
