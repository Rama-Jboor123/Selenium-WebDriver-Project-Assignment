package com.ramajboor.selenium.hooks;

import com.ramajboor.selenium.base.DriverFactory;
import com.ramajboor.selenium.pages.LoginPage;
import com.ramajboor.selenium.utils.ConfigReader;
import com.ramajboor.selenium.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @Before(order = 1)
    public void setUp(Scenario scenario) {
        WebDriver driver = DriverFactory.initializeDriver();
        driver.get(ConfigReader.get("baseUrl", "https://opensource-demo.orangehrmlive.com/"));
    }

    @Before(value = "@authenticated", order = 2)
    public void authenticate() {
        WebDriver driver = DriverFactory.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.loginWithConfiguredCredentials();
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        try {
            WebDriver driver = DriverFactory.getDriver();

            if (scenario.isFailed()) {
                byte[] screenshot = ScreenshotUtils.captureBytes(driver);
                scenario.attach(screenshot, "image/png", "failure-" + scenario.getName());
                ScreenshotUtils.capture(driver, scenario.getName());
            }
        } catch (IllegalStateException | org.openqa.selenium.WebDriverException e) {
            // Browser may already be unavailable during teardown.
        } finally {
            DriverFactory.quitDriver();
        }
    }
}
