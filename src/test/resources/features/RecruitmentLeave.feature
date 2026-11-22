@authenticated
Feature: Recruitment and Leave

  @recruitment @TC08
  Scenario: TC08 - Reject an invalid candidate email address
    Given the administrator is on the Add Candidate form
    When the administrator submits the required candidate details with email "invalidEmail"
    Then the candidate email field should show "Expected format: admin@example.com"
    And the candidate should not be saved

  @recruitment @TC09
  Scenario: TC09 - Add a candidate and find the saved details
    Given the administrator is on the Add Candidate form
    When the administrator saves a candidate with a unique name and valid email
    And the administrator searches the Candidates list for the new candidate
    Then the candidate is listed with the submitted name
    And the candidate profile shows the submitted email

  @leave @TC10
  Scenario: TC10 - Reject a leave search with the end date before the start date
    Given the administrator is on the Leave List page
    When the administrator searches for leave from "2026-11-20" to "2026-11-17"
    Then the To Date field should show "To date should be after from date"
    And the Leave List should keep the previous results
