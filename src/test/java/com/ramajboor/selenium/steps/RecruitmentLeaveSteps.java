package com.ramajboor.selenium.steps;

import com.ramajboor.selenium.base.DriverFactory;
import com.ramajboor.selenium.pages.AddCandidatePage;
import com.ramajboor.selenium.pages.LeaveListPage;
import com.ramajboor.selenium.pages.RecruitmentPage;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RecruitmentLeaveSteps {

    private static final String CANDIDATE_LAST_NAME = "Candidate";

    private RecruitmentPage recruitmentPage;
    private AddCandidatePage addCandidatePage;
    private String candidateFirstName;
    private String candidateEmail;
    private LeaveListPage leaveListPage;
    private String previousLeaveResults;
    private long previousLeaveSearchRequestCount;

    @Given("the administrator is on the Add Candidate form")
    public void openAddCandidateForm() {
        WebDriver driver = DriverFactory.getDriver();
        recruitmentPage = new RecruitmentPage(driver);
        addCandidatePage = new AddCandidatePage(driver);
        recruitmentPage.openFromMainMenu();
        recruitmentPage.clickAddCandidate();
    }

    @When("the administrator submits the required candidate details with email {string}")
    public void submitCandidateDetails(String email) {
        candidateFirstName = uniqueCandidateFirstName();
        saveCandidate(email);
    }

    @Then("the candidate email field should show {string}")
    public void verifyCandidateEmailValidation(String expectedMessage) {
        Assert.assertEquals(addCandidatePage.getEmailValidationMessage(), expectedMessage,
                "Unexpected validation message for the candidate email field.");
    }

    @Then("the candidate should not be saved")
    public void verifyCandidateWasNotSaved() {
        Assert.assertTrue(addCandidatePage.isFormDisplayed(),
                "The Add Candidate form should remain open after the invalid submission.");

        recruitmentPage.open();
        Assert.assertEquals(recruitmentPage.lookUpCandidateName(candidateFirstName), List.of("No Records Found"),
                "The candidate lookup should find no matches for " + candidateFirstName);
    }

    @When("the administrator saves a candidate with a unique name and valid email")
    public void saveCandidateWithValidDetails() {
        candidateFirstName = uniqueCandidateFirstName();
        candidateEmail = candidateFirstName + "@example.com";
        saveCandidate(candidateEmail);
        addCandidatePage.waitForCandidateProfile();
    }

    @When("the administrator searches the Candidates list for the new candidate")
    public void searchForNewCandidate() {
        recruitmentPage.open();
        Assert.assertTrue(recruitmentPage.searchForCandidate(candidateFirstName, candidateFullName()),
                "The candidate name was missing from the search suggestions: " + candidateFullName());
    }

    @Then("the candidate is listed with the submitted name")
    public void verifyListedCandidateName() {
        Assert.assertEquals(recruitmentPage.getListedCandidateName(), candidateFullName(),
                "The Candidates list showed a different name.");
    }

    @Then("the candidate profile shows the submitted email")
    public void verifySavedCandidateEmail() {
        recruitmentPage.openListedCandidateProfile();
        Assert.assertEquals(addCandidatePage.getSavedEmail(), candidateEmail,
                "The candidate profile showed a different email address.");
    }

    @Given("the administrator is on the Leave List page")
    public void openLeaveList() {
        leaveListPage = new LeaveListPage(DriverFactory.getDriver());
        leaveListPage.openFromMainMenu();
        previousLeaveResults = leaveListPage.getDisplayedResults();
        previousLeaveSearchRequestCount = leaveListPage.getSearchRequestCount();
    }

    @When("the administrator searches for leave from {string} to {string}")
    public void searchForLeaveDates(String fromDate, String toDate) {
        leaveListPage.setDateRange(LocalDate.parse(fromDate), LocalDate.parse(toDate));
        leaveListPage.clickSearch();
    }

    @Then("the To Date field should show {string}")
    public void verifyToDateValidation(String expectedMessage) {
        Assert.assertEquals(leaveListPage.getToDateValidationMessage(), expectedMessage,
                "The To Date field showed a different validation message.");
    }

    @Then("the Leave List should keep the previous results")
    public void verifyInvalidLeaveSearchWasRejected() {
        Assert.assertTrue(previousLeaveResults.equals(leaveListPage.getDisplayedResults()),
                "The displayed Leave List results changed after searching with reversed dates.");
        Assert.assertEquals(leaveListPage.getSearchRequestCount(), previousLeaveSearchRequestCount,
                "A new Leave List search request completed after submitting reversed dates.");
    }

    @After(value = "@TC09", order = 2)
    public void removeCreatedCandidate(Scenario scenario) {
        if (candidateEmail == null) {
            return;
        }

        scenario.log("Cleaning up test candidate " + candidateFullName());
        recruitmentPage.open();
        if (recruitmentPage.searchForCandidate(candidateFirstName, candidateFullName())) {
            recruitmentPage.deleteListedCandidate(candidateFullName());
            recruitmentPage.open();
            Assert.assertEquals(recruitmentPage.lookUpCandidateName(candidateFirstName), List.of("No Records Found"),
                    "The test candidate remained after cleanup: " + candidateFullName());
            scenario.log("Removed test candidate " + candidateFullName());
        }
    }

    private void saveCandidate(String email) {
        addCandidatePage.fillRequiredDetails(candidateFirstName, CANDIDATE_LAST_NAME, email);
        addCandidatePage.clickSave();
    }

    private String candidateFullName() {
        return candidateFirstName + " " + CANDIDATE_LAST_NAME;
    }

    private static String uniqueCandidateFirstName() {
        return "Selenium" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
