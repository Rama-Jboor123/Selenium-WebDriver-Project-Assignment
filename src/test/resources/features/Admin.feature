@admin @smoke @TC01 @authenticated
Feature: Admin User Management

  Scenario: TC01 - Search for an existing system user
    Given the administrator is on the System Users page
    When the administrator searches for username "Admin"
    Then the search results should contain username "Admin"
    And all displayed usernames should match "Admin"
