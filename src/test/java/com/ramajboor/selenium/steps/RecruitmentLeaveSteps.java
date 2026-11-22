package com.ramajboor.selenium.steps;

import com.ramajboor.selenium.base.DriverFactory;
import com.ramajboor.selenium.pages.AddCandidatePage;
import com.ramajboor.selenium.pages.RecruitmentPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.List;
import java.util.UUID;

public class RecruitmentLeaveSteps {

    private RecruitmentPage recruitmentPage;
    private AddCandidatePage addCandidatePage;
    private String candidateFirstName;

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
        candidateFirstName = "Selenium" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        addCandidatePage.fillRequiredDetails(candidateFirstName, "Candidate", email);
        addCandidatePage.clickSave();
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
}
