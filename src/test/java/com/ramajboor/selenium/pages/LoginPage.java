package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

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
}
