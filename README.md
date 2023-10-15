## Getting Started

This project example aims to demonstrate the production-ready code for implementing a RESTful API backend. 
It includes the project structure and utilizes modern technologies. 
It is important to note that this example does not cover all possible business use cases. 
The readme file will list possible improvements and suggestions for better implementation in terms of logic.

### Technologies used
Kotlin, Gradle, SpringBoot, Liquibase, H2, JUnit, OpenAPI

### Implemented features
- Ability to create, get, update, and delete an article using the `/articles` and `/articles/{id}` endpoints.
- List all articles for a given author using the `/articles/byAuthor?searchValue={value}` endpoint.
- List all articles for a specific keyword using the `/articles/byKeyword?searchValue={value}` endpoint.
- List all articles for a given period using the `/articles/byPeriod?start={dateTime}&end={dateTime}` endpoint.
- Swagger UI page is available at `http://localhost:8080/swagger-ui.html`.
- SpringBoot Actuator endpoint: `http://localhost:8080/actuator`.

### Trade-offs explained
- The article entity is implemented using a single domain `Article` to simplify the code. However, in a real application, it may not be sufficient as different use cases may require the use of DTOs.
- The `keywords` and `authors` are implemented as arrays in the current implementation. However, in reality, complex objects with their own APIs may be needed.
- Auditing properties such as `createdBy, updatedBy, createdAt, updatedAt` have not been implemented, which are essential in real applications.
- The security layer, such as Spring Security, is not included in this example.
- The list endpoints have been simplified using the `/articles/by**` URL template. However, in a real application, this may not be considered a good practice as it could be seen as RESTful bad practices. Implementing search with pagination using a URL like `/articles?search={searchLine}&page=0&size=20` may be preferred.
- Application profiles like `dev` and `prod` are not currently being used, but it is considered good practice for such applications.
- The validation of the `Article` was simplified to allow null values for properties, but in a real application, it may be necessary to implement proper and complex validation.

### How to run
- Run application tests: `./gradlew test`
- Start application server: `./gradlew bootRun`
