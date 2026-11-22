package com.ramajboor.selenium.steps;

import com.ramajboor.selenium.base.DriverFactory;
import com.ramajboor.selenium.pages.AdminPage;
import com.ramajboor.selenium.pages.AddUserPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;

public class AdminSteps {

    private AdminPage adminPage;
    private AddUserPage addUserPage;

    @Given("the administrator is on the System Users page")
    public void theAdministratorIsOnTheSystemUsersPage() {
        adminPage = new AdminPage(DriverFactory.getDriver());
        adminPage.navigateToAdmin();
        Assert.assertTrue(adminPage.isSystemUsersPageDisplayed(),
                "The System Users page was not displayed.");
    }

    @When("the administrator searches for username {string}")
    public void theAdministratorSearchesForUsername(String username) {
        adminPage.searchByUsername(username);
    }

    @Then("the search results should contain username {string}")
    public void theSearchResultsShouldContainUsername(String expectedUsername) {
        List<String> displayedUsernames = adminPage.getDisplayedUsernames();

        Assert.assertFalse(displayedUsernames.isEmpty(),
                "The search results were empty; expected username: " + expectedUsername);
        Assert.assertTrue(displayedUsernames.contains(expectedUsername),
                "Expected username '" + expectedUsername
                        + "' was not found in results: " + displayedUsernames);
    }

    @Then("all displayed usernames should match {string}")
    public void allDisplayedUsernamesShouldMatch(String expectedUsername) {
        List<String> displayedUsernames = adminPage.getDisplayedUsernames();

        Assert.assertFalse(displayedUsernames.isEmpty(),
                "The search results were empty; expected username: " + expectedUsername);
        for (String username : displayedUsernames) {
            Assert.assertEquals(username, expectedUsername,
                    "A displayed result did not match the searched username.");
        }
    }

    @Given("the administrator is on the system user page")
    public void theAdministratorIsOnTheSystemUserPage() {
        adminPage = new AdminPage(DriverFactory.getDriver());
        adminPage.navigateToAdmin();
        Assert.assertTrue(adminPage.isSystemUsersPageDisplayed(),
                "The System Users page was not displayed.");
    }

    @When("the administrator filter using Role {string}")
    public void theAdministratorFilterUsingRole(String role) {
        adminPage.selectUserRole(role);
        adminPage.clickSearch();
    }
    @Then("the filter results should contain Role {string}")
    public void theSearchResultsShouldContainRole(String expectedRole) {
        List<String> displayedRoles = adminPage.getDisplayedRoles();
        Assert.assertFalse(displayedRoles.isEmpty(),
                "The search results were empty; expected role: " + expectedRole);
        Assert.assertTrue(displayedRoles.contains(expectedRole),
                "Expected role '" + expectedRole
                        + "' was not found in results: " + displayedRoles);
    }

    @Given("the administrator is on the Add User page")
    public void theAdministratorIsOnTheAddUserPage() {
        addUserPage = new AddUserPage(DriverFactory.getDriver());
        addUserPage.open();
        Assert.assertTrue(addUserPage.isAddUserPageDisplayed(),
                "The Add User form was not displayed.");
    }

    @When("the administrator submits the Add User form without filling required fields")
    public void submitEmptyAddUserForm() {
        addUserPage.clickSave();
    }

    @Then("validation messages should appear for all required user fields")
    public void validationMessagesShouldAppearForAllRequiredUserFields() {
        var messages = addUserPage.getRequiredFieldValidationMessages();
        Assert.assertFalse(messages.isEmpty(),
                "No required-field validation messages were displayed.");
        Assert.assertTrue(addUserPage.areRequiredFieldsShowingErrors(),
                "Required-field validation messages did not contain the expected 'Required' text: " + messages);
        Assert.assertEquals(messages.keySet().size(), 6,
                "The Add User form did not show validation for all 6 required fields: " + messages);
    }

    @Then("the administrator should remain on the Add User page")
    public void administratorShouldRemainOnAddUserPage() {
        Assert.assertTrue(addUserPage.isAddUserPageDisplayed(),
                "The Add User form closed or navigation moved away after validation.");
    }

    @Then("all displayed role should match {string}")
    public void allDisplayedRolesShouldMatch(String expectedRole) {
        List<String> displayedRoles = adminPage.getDisplayedRoles();
        Assert.assertFalse(displayedRoles.isEmpty(),
                "The search results were empty; expected role: " + expectedRole);
        for (String role : displayedRoles) {
            Assert.assertEquals(role, expectedRole,
                    "A displayed result did not match the searched role.");
        }
    }
}
