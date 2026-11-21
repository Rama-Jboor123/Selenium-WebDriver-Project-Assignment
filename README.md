# Selenium WebDriver Project Assignment

This repository contains a Spring Boot application built with Java 21 and Maven.

## Run the application

```bash
mvn spring-boot:run
```

The application is available at `http://localhost:8080/`.

## Run tests

Run all TestNG and Cucumber tests:

```bash
mvn test
```

Run Chrome, Firefox, or Edge from Maven:

```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
mvn test -Dbrowser=chrome -Dheadless=true
```

Browser binaries are resolved automatically by Selenium Manager. Cucumber feature files should be added under `src/test/resources/features/`; step definitions belong under `com.ramajboor.selenium.steps`. CSV test data is loaded from `src/test/resources/testdata/`.