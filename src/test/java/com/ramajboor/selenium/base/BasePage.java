package com.ramajboor.selenium.base;

import com.ramajboor.selenium.utils.ConfigReader;
import com.ramajboor.selenium.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        int timeout = Integer.parseInt(ConfigReader.get("timeout.seconds", "10"));
        this.wait = WaitUtils.createWait(driver, timeout);
    }

    protected WebElement waitForVisibility(By locator) {
        return WaitUtils.waitForVisibility(wait, locator);
    }

    protected WebElement waitForClickability(By locator) {
        return WaitUtils.waitForClickability(wait, locator);
    }

    protected void click(By locator) {
        waitForClickability(locator).click();
    }

    protected void sendKeys(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    protected void navigateTo(String url) {
        driver.navigate().to(url);
    }

    protected void navigateToPath(String path) {
        String baseUrl = ConfigReader.get("baseUrl", "https://opensource-demo.orangehrmlive.com/");
        navigateTo(baseUrl.endsWith("/") ? baseUrl + path.replaceFirst("^/", "") : baseUrl + "/" + path);
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
