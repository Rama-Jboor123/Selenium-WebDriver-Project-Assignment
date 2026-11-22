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
