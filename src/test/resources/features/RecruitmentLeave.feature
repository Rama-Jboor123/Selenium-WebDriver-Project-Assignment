@authenticated
Feature: Recruitment and Leave

  @recruitment @TC08
  Scenario: TC08 - Reject an invalid candidate email address
    Given the administrator is on the Add Candidate form
    When the administrator submits the required candidate details with email "invalidEmail"
    Then the candidate email field should show "Expected format: admin@example.com"
    And the candidate should not be saved
