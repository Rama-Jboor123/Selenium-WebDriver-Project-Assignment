package com.ramajboor.selenium.steps;

import com.ramajboor.selenium.base.DriverFactory;
import com.ramajboor.selenium.pages.AddEmployeePage;
import com.ramajboor.selenium.pages.EmployeeDetailsPage;
import com.ramajboor.selenium.pages.EmployeeListPage;
import com.ramajboor.selenium.pages.LoginPage;
import com.ramajboor.selenium.utils.CSVReader;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PIMEmployeeSteps {

    private final Map<Employee, String> savedCsvEmployees = new LinkedHashMap<>();
    private AddEmployeePage addEmployeePage;
    private EmployeeDetailsPage employeeDetailsPage;
    private EmployeeListPage employeeListPage;
    private Scenario scenario;
    private String rejectedEmployeeId;
    private List<Employee> csvEmployees;
    private Employee employeeToEdit;
    private String editableEmployeeNumber;

    @Before(value = "@pim", order = 3)
    public void preparePimPages(Scenario scenario) {
        if (scenario.isFailed()) {
            return;
        }

        WebDriver driver = DriverFactory.getDriver();
        new LoginPage(driver).waitUntilAuthenticated();

        addEmployeePage = new AddEmployeePage(driver);
        employeeDetailsPage = new EmployeeDetailsPage(driver);
        employeeListPage = new EmployeeListPage(driver);
        this.scenario = scenario;
    }

    @Given("the administrator is on the Employee List page")
    public void openEmployeeList() {
        employeeListPage.open();
    }

    @When("the administrator searches for an unused Employee ID")
    public void searchForUnusedEmployeeId() {
        employeeListPage.searchByEmployeeId("N4" + runSuffix());
    }

    @Then("the Employee List should show {string} and zero employee rows")
    public void verifyEmptyEmployeeList(String expectedMessage) {
        Assert.assertEquals(employeeListPage.getResultsSummary(), expectedMessage,
                "The Employee List showed a different search result.");
        Assert.assertEquals(employeeListPage.getDisplayedEmployeeCount(), 0,
                "The Employee List still shows employee rows.");
    }

    @Given("the administrator is on the Add Employee page")
    public void openAddEmployee() {
        addEmployeePage.open();
    }

    @When("the administrator saves the employee without a First Name or Last Name")
    public void submitWithoutRequiredNames() {
        rejectedEmployeeId = "N5" + runSuffix();
        addEmployeePage.setEmployeeId(rejectedEmployeeId);
        addEmployeePage.clickSave();
    }

    @Then("the First Name and Last Name fields should each show {string}")
    public void verifyRequiredNameMessages(String expectedMessage) {
        Assert.assertEquals(addEmployeePage.getFirstNameValidationMessage(), expectedMessage,
                "Unexpected First Name validation message.");
        Assert.assertEquals(addEmployeePage.getLastNameValidationMessage(), expectedMessage,
                "Unexpected Last Name validation message.");
        Assert.assertTrue(addEmployeePage.isFormDisplayed(),
                "The Add Employee form should remain open after validation.");
    }

    @Then("the rejected Employee ID should be absent from the Employee List")
    public void verifyEmployeeWasNotCreated() {
        employeeListPage.open();
        employeeListPage.searchByEmployeeId(rejectedEmployeeId);
        verifyEmptyEmployeeList("No Records Found");
    }

    @Given("the employee records in {string}")
    public void loadEmployeeRecords(String resourcePath) {
        List<Map<String, String>> rows = CSVReader.read(resourcePath);
        Assert.assertTrue(rows.size() >= 2, "Expected multiple employee records in " + resourcePath);
        Assert.assertTrue(rows.getFirst().keySet().containsAll(List.of("firstName", "middleName", "lastName", "employeeId")),
                "Missing an employee data column in " + resourcePath);

        String suffix = runSuffix();
        Set<String> employeeIds = new HashSet<>();
        csvEmployees = new ArrayList<>();
        for (Map<String, String> row : rows) {
            Employee employee = new Employee(
                    row.get("firstName").replace("{run}", suffix),
                    row.get("middleName"),
                    row.get("lastName"),
                    row.get("employeeId").replace("{run}", suffix));
            Assert.assertFalse(employee.firstName().isBlank() || employee.lastName().isBlank(),
                    "A CSV employee is missing a first or last name: " + row);
            Assert.assertTrue(!employee.employeeId().isBlank() && employee.employeeId().length() <= 10,
                    "A CSV employee needs an ID of 1 to 10 characters: " + row);
            Assert.assertTrue(employeeIds.add(employee.employeeId().toLowerCase(Locale.ROOT)),
                    "Duplicate Employee ID in " + resourcePath + ": " + employee.employeeId());
            csvEmployees.add(employee);
        }
    }

    @When("the administrator adds each CSV employee through Add Employee")
    public void addCsvEmployees() {
        for (Employee employee : csvEmployees) {
            scenario.log("Submitting CSV employee " + employee.firstName() + " with ID " + employee.employeeId());
            savedCsvEmployees.put(employee, createEmployee(employee));
        }
    }

    @Then("each saved employee's Personal Details should match its CSV record")
    public void verifySavedCsvEmployees() {
        for (var saved : savedCsvEmployees.entrySet()) {
            Employee employee = saved.getKey();
            employeeDetailsPage.openByEmployeeId(saved.getValue());
            assertSavedDetails(employee);
            scenario.log("Verified CSV employee " + employee.firstName() + " with ID " + employee.employeeId());
        }
    }

    @Given("the administrator has created an employee to edit")
    public void createEmployeeToEdit() {
        String suffix = runSuffix();
        employeeToEdit = new Employee("PimEdit" + suffix, "Original", "Employee", "E7" + suffix);
        editableEmployeeNumber = createEmployee(employeeToEdit);
        assertSavedDetails(employeeToEdit);
    }

    @When("the administrator changes the employee's Middle Name to {string} and saves")
    public void saveNewMiddleName(String middleName) {
        employeeDetailsPage.updateMiddleName(middleName);
    }

    @Then("the employee's Middle Name should still be {string} after reopening Personal Details")
    public void verifyPersistedMiddleName(String middleName) {
        employeeDetailsPage.openByEmployeeId(editableEmployeeNumber);
        assertSavedDetails(new Employee(
                employeeToEdit.firstName(), middleName, employeeToEdit.lastName(), employeeToEdit.employeeId()));
    }

    private String createEmployee(Employee employee) {
        addEmployeePage.open();
        addEmployeePage.fillName(employee.firstName(), employee.middleName(), employee.lastName());
        addEmployeePage.setEmployeeId(employee.employeeId());
        return addEmployeePage.saveAndGetEmployeeNumber();
    }

    private void assertSavedDetails(Employee expected) {
        employeeDetailsPage.waitForDetails();
        Employee actual = new Employee(
                employeeDetailsPage.getFirstName(),
                employeeDetailsPage.getMiddleName(),
                employeeDetailsPage.getLastName(),
                employeeDetailsPage.getEmployeeId());
        Assert.assertEquals(actual, expected, "Unexpected Personal Details for Employee ID " + expected.employeeId());
    }

    private static String runSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private record Employee(String firstName, String middleName, String lastName, String employeeId) {
    }
}
