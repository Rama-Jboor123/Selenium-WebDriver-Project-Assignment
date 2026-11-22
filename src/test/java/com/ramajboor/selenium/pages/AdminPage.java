package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class AdminPage extends BasePage {

    // TC01 locators below use OrangeHRM's relative OXD component structure and were
    // verified against the live OrangeHRM System Users page during execution.
    private final By adminMenu = By.cssSelector("a.oxd-main-menu-item[href*='/admin/viewSystemUsers']");
    private final By systemUsersHeading = By.xpath("//*[self::h5 or self::h6][normalize-space()='System Users']");
    private final By filterToggle = By.cssSelector(".oxd-table-filter-header-options button.oxd-icon-button");
    private final By usernameInput = By.cssSelector(".oxd-form-row input.oxd-input");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By tableRows = By.cssSelector(".oxd-table-body .oxd-table-card");
    private final By tableLoader = By.cssSelector(".oxd-loading-spinner, .oxd-table-loader");
    private final By resultsSummary = By.cssSelector(
            ".orangehrm-paper-container .orangehrm-horizontal-padding.orangehrm-vertical-padding > span.oxd-text");
    private final By userRoleDropdown = By.xpath(
            "//label[text()='User Role']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]"
    );

    private final By adminRoleOption = By.xpath(
            "//div[@role='option']//span[normalize-space()='Admin']"
    );


    public AdminPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToAdmin();
    }

    public void navigateToAdmin() {
        try {
            click(adminMenu);
        } catch (RuntimeException exception) {
            navigateToPath("web/index.php/admin/viewSystemUsers");
        }
        waitForVisibility(systemUsersHeading);
        waitForSearchResults();
    }

    public boolean isSystemUsersPageDisplayed() {
        return waitForVisibility(systemUsersHeading).isDisplayed();
    }

    public void enterUsername(String username) {
        if (driver.findElements(usernameInput).stream().noneMatch(WebElement::isDisplayed)) {
            click(filterToggle);
        }
        sendKeys(usernameInput, username);
    }

    public void clickSearch() {
        WebElement previousSummary = waitForSearchResults();
        click(searchButton);
        // The summary is replaced after a search even when both sets of rows are identical.
        wait.until(ExpectedConditions.stalenessOf(previousSummary));
        waitForSearchResults();
    }

    public void searchByUsername(String username) {
        enterUsername(username);
        clickSearch();
    }

    public void selectUserRole(String role) {
        if (driver.findElements(userRoleDropdown).stream().noneMatch(WebElement::isDisplayed)) {
            click(filterToggle);
        }
        click(userRoleDropdown);

        By roleOption = By.xpath(
                "//div[@role='option']//span[normalize-space()='" + role + "']"
        );

        click(roleOption);
    }

    public List<String> getDisplayedRoles() {
        waitForSearchResults();

        int roleColumnIndex = findColumnIndex("User Role");
        List<String> roles = new ArrayList<>();
        for (WebElement row : driver.findElements(tableRows)) {
            try {
                List<WebElement> cells = row.findElements(By.cssSelector(".oxd-table-cell"));
                if (roleColumnIndex >= 0 && roleColumnIndex < cells.size()) {
                    String value = extractLastLine(cells.get(roleColumnIndex).getText());
                    if (!value.isEmpty() && !value.equalsIgnoreCase("User Role")) {
                        roles.add(value);
                    }
                } else {
                    for (WebElement cell : cells) {
                        String cellText = cell.getText().replace("\u00a0", " ").trim();
                        if (cellText.startsWith("User Role")) {
                            String value = extractLastLine(cellText);
                            if (!value.isEmpty() && !value.equalsIgnoreCase("User Role")) {
                                roles.add(value);
                            }
                            break;
                        }
                    }
                }

            } catch (StaleElementReferenceException ignored) {
                // The row is re-read from the table on the next search execution.
            }
        }
        return roles;
    }

    public List<String> getDisplayedUsernames() {
        waitForSearchResults();

        int usernameColumnIndex = findColumnIndex("Username");
        List<String> usernames = new ArrayList<>();
        for (WebElement row : driver.findElements(tableRows)) {
            try {
                List<WebElement> cells = row.findElements(By.cssSelector(".oxd-table-cell"));
                if (!cells.isEmpty() && usernameColumnIndex < cells.size()) {
                    String value = extractLastLine(cells.get(usernameColumnIndex).getText());
                    if (!value.isEmpty()) {
                        usernames.add(value);
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // The row is re-read from the table on the next search execution.
            }
        }
        return usernames;
    }

    private int findColumnIndex(String expectedHeading) {
        List<WebElement> headers = driver.findElements(By.cssSelector(
                ".oxd-table-header-cell"));
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(expectedHeading)) {
                return i;
            }
        }

        List<WebElement> rows = driver.findElements(tableRows);
        if (!rows.isEmpty()) {
            List<WebElement> firstRowCells = rows.get(0)
                    .findElements(By.cssSelector(".oxd-table-cell"));
            for (int i = 0; i < firstRowCells.size(); i++) {
                if (firstRowCells.get(i).getText().trim().toLowerCase()
                        .startsWith(expectedHeading.toLowerCase())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private String extractLastLine(String cellText) {
        String[] lines = cellText.split("\\R");
        for (int i = lines.length - 1; i >= 0; i--) {
            String value = lines[i].trim();
            if (!value.isEmpty()) {
                return value;
            }
        }
        return "";
    }

    private WebElement waitForSearchResults() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(tableLoader));
        return waitForVisibility(resultsSummary);
    }
}
