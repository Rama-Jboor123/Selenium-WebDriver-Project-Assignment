# Student 2 – PIM and Employee Management

Student 2 covers TC04–TC07 and reads employee test data from CSV for TC06. The feature uses the shared `@authenticated` hook to sign in before each scenario. The TestNG runner can run the four scenarios in parallel with one WebDriver per thread.

| Scenario | What it verifies |
| --- | --- |
| TC04 | Searching for an unused Employee ID returns `No Records Found` and zero employee rows. |
| TC05 | Submitting with blank First Name and Last Name shows `Required` on both fields. The form stays open, and a fresh search finds no record for the submitted ID. |
| TC06 | Each CSV employee can be saved through Add Employee. Reopening each saved profile shows the exact first, middle and last names and Employee ID submitted for that row. |
| TC07 | A scenario-owned employee keeps its updated Middle Name after Personal Details is reopened. |

## Implementation

- Declares the four scenarios and their tags. [PIMEmployeeManagement.feature](../src/test/resources/features/PIMEmployeeManagement.feature)
- Connects the scenarios to the page objects and makes the TestNG assertions. [PIMEmployeeSteps.java](../src/test/java/com/ramajboor/selenium/steps/PIMEmployeeSteps.java)
- Opens Employee List, searches by ID, and reads the refreshed summary and rows. [EmployeeListPage.java](../src/test/java/com/ramajboor/selenium/pages/EmployeeListPage.java)
- Fills and saves Add Employee, reads field-specific validation, and captures the new profile's internal employee number. [AddEmployeePage.java](../src/test/java/com/ramajboor/selenium/pages/AddEmployeePage.java)
- Opens Personal Details by internal employee number, reads persisted field values, and saves Middle Name changes. [EmployeeDetailsPage.java](../src/test/java/com/ramajboor/selenium/pages/EmployeeDetailsPage.java)

The PIM setup waits for the shared login redirect and reloads the login form once if OrangeHRM rejects its token. The page objects reuse the explicit waits in `BasePage` and `WaitUtils`. Employee List gives a stalled initial load one refresh, then waits for the results summary to update after searching. Add Employee waits for the loading overlay to clear before clicking Save, then waits for the Personal Details redirect or visible validation. The update waits for save confirmation before reopening the profile and reading its fields.

## TC06 CSV data

The dataset contains `firstName,middleName,lastName,employeeId` and three rows. TC06 passes its resource path to the existing CSV reader, which parses the UTF-8 data from Maven's test classpath with Apache Commons CSV. [employees.csv](../src/test/resources/testdata/employees.csv) · [CSVReader.java](../src/test/java/com/ramajboor/selenium/utils/CSVReader.java)

The loading step replaces `{run}` in the first name and Employee ID with the same eight-character suffix for that scenario. Each row has a distinct ID prefix to keep the IDs unique within the run and within OrangeHRM's 10-character limit. The load step rejects duplicate IDs after substitution, ignoring case. If the demo already has a submitted ID, the form validation fails the scenario and the report identifies the attempted row.

Cucumber runs TC06 once. Its steps save each resolved row through the browser, reopen each profile, compare all four fields with the resolved CSV values, and log each verified record to the Cucumber report.

## Run and verify

Use Java 21 or later, Maven, an installed browser, and configured OrangeHRM credentials. [Shared authentication setup](../README.md#authentication-setup)

Run all four PIM scenarios or just TC06 from the project root. These Maven commands also work from IntelliJ's terminal and use the project's configured TestNG thread count.

```powershell
mvn test '-Dcucumber.filter.tags=@pim' '-Dbrowser=chrome' '-Dheadless=true'
mvn test '-Dcucumber.filter.tags=@TC06' '-Dbrowser=chrome' '-Dheadless=true'
```

Expect four passing PIM scenarios and three verified CSV records in `target/cucumber-report/cucumber.html`. The suite uses the English UI labels. The public demo is shared and can reset its data between requests. Successful TC06 and TC07 runs leave their generated test records in the demo.
