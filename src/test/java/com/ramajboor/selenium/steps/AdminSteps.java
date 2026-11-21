package com.ramajboor.selenium.steps;

import com.ramajboor.selenium.base.DriverFactory;
import com.ramajboor.selenium.pages.AdminPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;

public class AdminSteps {

    private AdminPage adminPage;

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
}
