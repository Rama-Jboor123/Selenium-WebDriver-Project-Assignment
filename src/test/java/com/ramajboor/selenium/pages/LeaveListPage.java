package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LeaveListPage extends BasePage {

    private static final By LEAVE_MENU = By.cssSelector("a[href$='/leave/viewLeaveModule']");
    private static final By LEAVE_LIST_TITLE = By.xpath("//h5[normalize-space()='Leave List']");
    private static final String DATE_GROUP = "//form//label[normalize-space()='%s']"
            + "/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' oxd-input-group ')][1]";
    private static final By FROM_DATE = By.xpath(DATE_GROUP.formatted("From Date") + "//input");
    private static final By TO_DATE = By.xpath(DATE_GROUP.formatted("To Date") + "//input");
    private static final By TO_DATE_ERROR = By.xpath(DATE_GROUP.formatted("To Date")
            + "//span[contains(@class, 'oxd-input-field-error-message')]");
    private static final By SEARCH_BUTTON = By.xpath("//form//button[@type='submit' and normalize-space()='Search']");
    private static final By RESULTS = By.cssSelector(".orangehrm-paper-container");
    private static final By RESULTS_SUMMARY = By.cssSelector(".orangehrm-header-container span.oxd-text");

    public LeaveListPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/leave/viewLeaveList");
    }

    public void openFromMainMenu() {
        click(LEAVE_MENU);
        waitForVisibility(LEAVE_LIST_TITLE);
        expandTableFilters();
        wait.until(driver -> getSearchRequestCount() > 0);
    }

    public void setDateRange(LocalDate fromDate, LocalDate toDate) {
        enterDate(FROM_DATE, fromDate);
        enterDate(TO_DATE, toDate);
    }

    public void clickSearch() {
        click(SEARCH_BUTTON);
    }

    public String getToDateValidationMessage() {
        return getText(TO_DATE_ERROR);
    }

    public String getDisplayedResults() {
        waitForVisibility(RESULTS_SUMMARY);
        return getText(RESULTS);
    }

    public long getSearchRequestCount() {
        Number count = (Number) ((JavascriptExecutor) driver).executeScript("""
                return performance.getEntriesByType('resource').filter(entry =>
                    new URL(entry.name).pathname.endsWith('/api/v2/leave/employees/leave-requests')
                ).length;
                """);
        return count.longValue();
    }

    private void enterDate(By locator, LocalDate date) {
        WebElement input = waitForVisibility(locator);
        String pattern = input.getDomAttribute("placeholder").replace("mm", "MM");
        String formattedDate = date.format(DateTimeFormatter.ofPattern(pattern, Locale.ROOT));
        Platform platform = ((HasCapabilities) driver).getCapabilities().getPlatformName();
        Keys selectAllModifier = Platform.MAC.is(platform) ? Keys.COMMAND : Keys.CONTROL;
        // Replace the date in one edit so the date picker does not restore its default on clear.
        input.sendKeys(Keys.chord(selectAllModifier, "a"), formattedDate, Keys.TAB);
        wait.until(ExpectedConditions.domPropertyToBe(input, "value", formattedDate));
    }
}
