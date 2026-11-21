package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
        waitForVisibility(tableRows);
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
        click(searchButton);
        waitForSearchResults();
    }

    public void searchByUsername(String username) {
        enterUsername(username);
        clickSearch();
    }

    public List<String> getDisplayedUsernames() {
        waitForSearchResults();

        int usernameColumnIndex = findUsernameColumnIndex();
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

    private int findUsernameColumnIndex() {
        // Verified in the live DOM: Employee Name, blank selection column,
        // Username, User Role, and Status are returned in that order.
        return 2;
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

    private void waitForSearchResults() {
        wait.until(currentDriver -> {
            List<WebElement> loaders = currentDriver.findElements(tableLoader);
            for (WebElement loader : loaders) {
                if (loader.isDisplayed()) {
                    return false;
                }
            }
            return true;
        });
        wait.until(currentDriver -> !currentDriver.findElements(tableRows).isEmpty());
    }
}
