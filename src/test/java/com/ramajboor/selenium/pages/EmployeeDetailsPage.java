package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class EmployeeDetailsPage extends BasePage {

    private static final By HEADING = By.xpath("//h6[normalize-space()='Personal Details']");
    private static final By FIRST_NAME = By.name("firstName");
    private static final By MIDDLE_NAME = By.name("middleName");
    private static final By LAST_NAME = By.name("lastName");
    private static final By EMPLOYEE_ID = By.xpath(
            "//form//label[normalize-space()='Employee Id']/../following-sibling::div//input");
    private static final By SAVE_BUTTON = By.xpath(
            "//form[.//input[@name='middleName']]//button[@type='submit' and normalize-space()='Save']");
    private static final By UPDATE_CONFIRMATION = By.xpath("//p[normalize-space()='Successfully Updated']");

    public EmployeeDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void openByEmployeeId(String employeeNumber) {
        navigateToPath("web/index.php/pim/viewPersonalDetails/empNumber/" + employeeNumber);
    }

    public void waitForDetails() {
        waitForVisibility(HEADING);
        wait.until(ExpectedConditions.refreshed(
                currentDriver -> !currentDriver.findElement(FIRST_NAME).getDomProperty("value").isBlank()));
    }

    public String getFirstName() {
        return waitForVisibility(FIRST_NAME).getDomProperty("value");
    }

    public String getMiddleName() {
        return waitForVisibility(MIDDLE_NAME).getDomProperty("value");
    }

    public String getLastName() {
        return waitForVisibility(LAST_NAME).getDomProperty("value");
    }

    public String getEmployeeId() {
        return waitForVisibility(EMPLOYEE_ID).getDomProperty("value");
    }

    public void updateMiddleName(String middleName) {
        WebElement input = waitForClickability(MIDDLE_NAME);
        Platform platform = ((HasCapabilities) driver).getCapabilities().getPlatformName();
        Keys selectAllModifier = Platform.MAC.is(platform) ? Keys.COMMAND : Keys.CONTROL;
        input.sendKeys(Keys.chord(selectAllModifier, "a"), middleName);
        wait.until(ExpectedConditions.domPropertyToBe(input, "value", middleName));
        click(SAVE_BUTTON);
        waitForVisibility(UPDATE_CONFIRMATION);
    }
}
