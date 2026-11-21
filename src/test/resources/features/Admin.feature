@admin @smoke  @authenticated
Feature: Admin User Management
  @TC01
  Scenario: TC01 - Search for an existing system user
    Given the administrator is on the System Users page
    When the administrator searches for username "Admin"
    Then the search results should contain username "Admin"
    And all displayed usernames should match "Admin"

  Scenario: TC02 - Filter for an existing system role
    Given the administrator is on the system user page
    When the administrator filter using Role "Admin"
    Then the filter results should contain Role "Admin"
    And all displayed role should match "Admin"