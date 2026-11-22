package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class EmployeeListPage extends BasePage {

    private static final By EMPLOYEE_ID = By.xpath(
            "//form//label[normalize-space()='Employee Id']/../following-sibling::div//input");
    private static final By SEARCH_BUTTON = By.xpath(
            "//form//button[@type='submit' and normalize-space()='Search']");
    private static final By RESULTS_SUMMARY = By.cssSelector(
            ".orangehrm-paper-container .orangehrm-horizontal-padding.orangehrm-vertical-padding > span.oxd-text");
    private static final By TABLE_LOADER = By.cssSelector(
            ".orangehrm-paper-container .oxd-table-loader, .orangehrm-paper-container .oxd-loading-spinner");
    private static final By EMPLOYEE_ROWS = By.cssSelector(".orangehrm-employee-list .oxd-table-card");

    public EmployeeListPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/pim/viewEmployeeList");
        try {
            waitForVisibility(RESULTS_SUMMARY);
        } catch (TimeoutException timeout) {
            if (ExpectedConditions.visibilityOfElementLocated(TABLE_LOADER).apply(driver) == null) {
                throw timeout;
            }
            refresh();
            waitForVisibility(RESULTS_SUMMARY);
        }
    }

    public void searchByEmployeeId(String employeeId) {
        WebElement previousSummary = waitForVisibility(RESULTS_SUMMARY);
        expandTableFilters();
        sendKeys(EMPLOYEE_ID, employeeId);
        click(SEARCH_BUTTON);
        // The list removes the old summary while fetching results, even when both searches return zero rows.
        wait.until(ExpectedConditions.stalenessOf(previousSummary));
        waitForVisibility(RESULTS_SUMMARY);
    }

    public String getResultsSummary() {
        return getText(RESULTS_SUMMARY);
    }

    public int getDisplayedEmployeeCount() {
        return (int) driver.findElements(EMPLOYEE_ROWS).stream().filter(WebElement::isDisplayed).count();
    }
}
