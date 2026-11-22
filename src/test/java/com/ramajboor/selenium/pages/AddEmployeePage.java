package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEmployeePage extends BasePage {

    private static final String ADD_EMPLOYEE_PATH = "/pim/addEmployee";
    private static final Pattern SAVED_EMPLOYEE_PATH = Pattern.compile("/pim/viewPersonalDetails/empNumber/(\\d+)");
    private static final By HEADING = By.xpath("//h6[normalize-space()='Add Employee']");
    private static final By FIRST_NAME = By.name("firstName");
    private static final By MIDDLE_NAME = By.name("middleName");
    private static final By LAST_NAME = By.name("lastName");
    private static final By EMPLOYEE_ID = By.xpath(
            "//form//label[normalize-space()='Employee Id']/../following-sibling::div//input");
    private static final By SAVE_BUTTON = By.xpath(
            "//form//button[@type='submit' and normalize-space()='Save']");
    private static final By FORM_LOADER = By.cssSelector(".oxd-form-loader");
    private static final By FORM_ERROR = By.cssSelector("form .oxd-input-field-error-message");
    private static final By FIRST_NAME_ERROR = nameError("firstName");
    private static final By LAST_NAME_ERROR = nameError("lastName");

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php" + ADD_EMPLOYEE_PATH);
        waitForVisibility(HEADING);
    }

    public void fillName(String firstName, String middleName, String lastName) {
        sendKeys(FIRST_NAME, firstName);
        sendKeys(MIDDLE_NAME, middleName);
        sendKeys(LAST_NAME, lastName);
    }

    public void setEmployeeId(String employeeId) {
        WebElement input = waitForClickability(EMPLOYEE_ID);
        Platform platform = ((HasCapabilities) driver).getCapabilities().getPlatformName();
        Keys selectAllModifier = Platform.MAC.is(platform) ? Keys.COMMAND : Keys.CONTROL;
        // Replace the generated ID in one edit so the form's input model updates with the new value.
        input.sendKeys(Keys.chord(selectAllModifier, "a"), employeeId);
        wait.until(ExpectedConditions.domPropertyToBe(input, "value", employeeId));
    }

    public void clickSave() {
        try {
            waitForSaveButton().click();
        } catch (ElementClickInterceptedException intercepted) {
            waitForSaveButton().click();
        }
    }

    public String saveAndGetEmployeeNumber() {
        clickSave();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlMatches(SAVED_EMPLOYEE_PATH.pattern()),
                ExpectedConditions.visibilityOfElementLocated(FORM_ERROR)));

        Matcher profile = SAVED_EMPLOYEE_PATH.matcher(driver.getCurrentUrl());
        if (!profile.find()) {
            throw new IllegalStateException("Add Employee rejected the submission: " + getText(FORM_ERROR));
        }
        return profile.group(1);
    }

    public String getFirstNameValidationMessage() {
        return getText(FIRST_NAME_ERROR);
    }

    public String getLastNameValidationMessage() {
        return getText(LAST_NAME_ERROR);
    }

    public boolean isFormDisplayed() {
        return driver.getCurrentUrl().endsWith(ADD_EMPLOYEE_PATH)
                && waitForVisibility(HEADING).isDisplayed();
    }

    private WebElement waitForSaveButton() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(FORM_LOADER));
        return waitForClickability(SAVE_BUTTON);
    }

    private static By nameError(String inputName) {
        return By.xpath(("//form//input[@name='%s']"
                + "/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' oxd-input-group ')][1]"
                + "//span[contains(@class, 'oxd-input-field-error-message')]").formatted(inputName));
    }
}
