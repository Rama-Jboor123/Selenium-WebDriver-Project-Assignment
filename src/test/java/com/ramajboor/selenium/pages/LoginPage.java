package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private static final String LOGIN_TOKEN_ERROR = "CSRF token validation failed";
    private static final By LOGIN_ALERT = By.cssSelector(".oxd-alert-content-text");

    // TODO: Verify these locators against the active OrangeHRM login DOM before production scenarios.
    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By loginButton = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/auth/login");
    }

    public void login(String username, String password) {
        sendKeys(usernameInput, username);
        sendKeys(passwordInput, password);
        click(loginButton);
    }

    public void loginWithConfiguredCredentials() {
        login(
                com.ramajboor.selenium.utils.ConfigReader.getCredential(
                        "ORANGEHRM_USERNAME", "orangehrm.username"),
                com.ramajboor.selenium.utils.ConfigReader.getCredential(
                        "ORANGEHRM_PASSWORD", "orangehrm.password")
        );
    }

    public void waitUntilAuthenticated() {
        String error = waitForLoginOutcome();
        if (LOGIN_TOKEN_ERROR.equals(error)) {
            open();
            loginWithConfiguredCredentials();
            error = waitForLoginOutcome();
        }
        if (!error.isEmpty()) {
            throw new IllegalStateException("OrangeHRM login failed: " + error);
        }
    }

    private String waitForLoginOutcome() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.not(ExpectedConditions.urlContains("/auth/")),
                ExpectedConditions.visibilityOfElementLocated(LOGIN_ALERT)));
        return driver.getCurrentUrl().contains("/auth/") ? getText(LOGIN_ALERT) : "";
    }
}
