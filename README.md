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
mvn test -Dcucumber.filter.tags=@TC01
```

Browser binaries are resolved automatically by Selenium Manager. Cucumber feature files should be added under `src/test/resources/features/`; step definitions belong under `com.ramajboor.selenium.steps`. CSV test data is loaded from `src/test/resources/testdata/`.

Configuration is defined in `src/test/resources/config.properties`. It can be overridden with Maven/system properties, for example `-Dbrowser=firefox`, `-Dheadless=true`, `-DbaseUrl=...` and `-DexplicitWait=20`. Parallel scenarios use `-DparallelThreads=3`.

## Authentication setup

Use the `@authenticated` tag on scenarios that require an OrangeHRM session:

```gherkin
@authenticated
Scenario: Example placeholder
```

The framework does not count login itself as a scenario. Login runs through a reusable hook and `LoginPage.loginWithConfiguredCredentials()`.

Set credentials with environment variables or a local ignored properties file:

```bash
set ORANGEHRM_USERNAME=your-local-user
set ORANGEHRM_PASSWORD=your-local-password
mvn test
```

For IntelliJ IDEA, create `src/test/resources/config.local.properties` with `orangehrm.username` and `orangehrm.password`, or set `ORANGEHRM_USERNAME` and `ORANGEHRM_PASSWORD` in the test run configuration environment. The local file is gitignored and must not be committed.

## TC03 cross-browser execution

Run TC03 sequentially in Chrome, Firefox, and Edge:

```powershell
.\run-tc03-crossbrowser.ps1
```

Per-browser reports and screenshots are stored under `target/cross-browser-reports/TC03/<browser>/`. Individual browser commands can also be run directly:

```bash
mvn test "-Dcucumber.filter.tags=@TC03" -Dbrowser=chrome
mvn test "-Dcucumber.filter.tags=@TC03" -Dbrowser=firefox
mvn test "-Dcucumber.filter.tags=@TC03" -Dbrowser=edge
```

## TC08 candidate email validation

Configure the OrangeHRM credentials as described above, then run:

```bash
mvn test -Dcucumber.filter.tags=@TC08 -Dbrowser=chrome -Dheadless=true
```

TC08 submits the Add Candidate form with a unique first name and an invalid email.
It checks the inline email validation, confirms that the form stays open, and looks up
the name in a fresh Candidates view to verify that no record was saved.
Cucumber writes the report to `target/cucumber-report/cucumber.html`.
