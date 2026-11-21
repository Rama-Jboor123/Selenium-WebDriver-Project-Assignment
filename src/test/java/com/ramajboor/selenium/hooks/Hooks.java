package com.ramajboor.selenium.hooks;

import com.ramajboor.selenium.base.DriverFactory;
import com.ramajboor.selenium.utils.ConfigReader;
import com.ramajboor.selenium.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        WebDriver driver = DriverFactory.initializeDriver();
        driver.get(ConfigReader.get("baseUrl", "https://opensource-demo.orangehrmlive.com/"));
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver;
        try {
            driver = DriverFactory.getDriver();
        } catch (IllegalStateException e) {
            return;
        }

        if (scenario.isFailed()) {
            ScreenshotUtils.capture(driver, scenario.getName());
        }
        DriverFactory.quitDriver();
    }
}
