package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddCandidatePage extends BasePage {

    private static final String ADD_CANDIDATE_PATH = "/recruitment/addCandidate";
    private static final By HEADING = By.xpath("//h6[normalize-space()='Add Candidate']");
    private static final By FIRST_NAME = By.name("firstName");
    private static final By LAST_NAME = By.name("lastName");
    private static final String EMAIL_GROUP = "//label[normalize-space()='Email']"
            + "/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' oxd-input-group ')][1]";
    private static final By EMAIL = By.xpath(EMAIL_GROUP + "//input");
    private static final By EMAIL_ERROR = By.xpath(EMAIL_GROUP
            + "//span[contains(@class, 'oxd-input-field-error-message')]");
    private static final By SAVE_BUTTON = By.xpath("//form//button[@type='submit' and normalize-space()='Save']");

    public AddCandidatePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php" + ADD_CANDIDATE_PATH);
    }

    public void fillRequiredDetails(String firstName, String lastName, String email) {
        sendKeys(FIRST_NAME, firstName);
        sendKeys(LAST_NAME, lastName);
        sendKeys(EMAIL, email);
    }

    public void clickSave() {
        click(SAVE_BUTTON);
    }

    public String getEmailValidationMessage() {
        return getText(EMAIL_ERROR);
    }

    public boolean isFormDisplayed() {
        return driver.getCurrentUrl().endsWith(ADD_CANDIDATE_PATH)
                && waitForVisibility(HEADING).isDisplayed();
    }
}
