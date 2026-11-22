@pim @authenticated
Feature: PIM Employee Management

  @TC04
  Scenario: TC04 - Search for a nonexistent employee
    Given the administrator is on the Employee List page
    When the administrator searches for an unused Employee ID
    Then the Employee List should show "No Records Found" and zero employee rows

  @TC05
  Scenario: TC05 - Reject employee creation without required names
    Given the administrator is on the Add Employee page
    When the administrator saves the employee without a First Name or Last Name
    Then the First Name and Last Name fields should each show "Required"
    And the rejected Employee ID should be absent from the Employee List

  @TC06
  Scenario: TC06 - Add employees from a CSV file
    Given the employee records in "testdata/employees.csv"
    When the administrator adds each CSV employee through Add Employee
    Then each saved employee's Personal Details should match its CSV record

  @TC07
  Scenario: TC07 - Update an employee's Middle Name
    Given the administrator has created an employee to edit
    When the administrator changes the employee's Middle Name to "Updated" and saves
    Then the employee's Middle Name should still be "Updated" after reopening Personal Details
