package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddUserPage extends BasePage {

    private final By addUserHeading = By.xpath("//h6[normalize-space()='Add User']");
    private final By saveButton = By.xpath("//button[@type='submit' and normalize-space()='Save']");
    private final By form = By.cssSelector("form.oxd-form");

    private static final List<String> REQUIRED_LABELS = List.of(
            "User Role", "Employee Name", "Status", "Username", "Password", "Confirm Password");

    public AddUserPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToAdmin();
        waitForVisibility(addUserHeading);
    }

    private void navigateToAdmin() {
        navigateToPath("web/index.php/admin/saveSystemUser");
    }

    public boolean isAddUserPageDisplayed() {
        return waitForVisibility(addUserHeading).isDisplayed()
                && waitForVisibility(form).isDisplayed()
                && driver.getCurrentUrl().contains("/admin/saveSystemUser");
    }

    public void clickSave() {
        wait.until(driver -> {
            List<WebElement> fields = new ArrayList<>(driver.findElements(form));
            return !fields.isEmpty();
        });
        click(saveButton);
    }

    public Map<String, String> getRequiredFieldValidationMessages() {
        wait.until(driver -> !getRequiredFieldValidationMessagesNow().isEmpty());
        return getRequiredFieldValidationMessagesNow();
    }

    private Map<String, String> getRequiredFieldValidationMessagesNow() {
        Map<String, String> messages = new LinkedHashMap<>();

        for (String label : REQUIRED_LABELS) {
            By labelLocator = By.xpath("//label[normalize-space()='" + label
                    + "']/ancestor::div[contains(@class,'oxd-input-group')]"
                    + "//span[contains(@class,'oxd-input-field-error-message')]");

            List<WebElement> errorElements = driver.findElements(labelLocator);
            if (!errorElements.isEmpty()) {
                String message = errorElements.stream()
                        .filter(WebElement::isDisplayed)
                        .map(WebElement::getText)
                        .map(String::trim)
                        .filter(text -> !text.isEmpty())
                        .findFirst()
                        .orElse("");
                if (!message.isEmpty()) {
                    messages.put(label, message);
                }
            }
        }
        return messages;
    }

    public boolean areRequiredFieldsShowingErrors() {
        Map<String, String> messages = getRequiredFieldValidationMessages();
        if (messages.isEmpty()) {
            return false;
        }

        for (Map.Entry<String, String> entry : messages.entrySet()) {
            String message = entry.getValue();
            boolean requiredMessage = "Required".equalsIgnoreCase(message);
            boolean confirmPasswordMessage = "Confirm Password".equals(entry.getKey())
                    && "Passwords do not match".equalsIgnoreCase(message);
            if (!requiredMessage && !confirmPasswordMessage) {
                return false;
            }
        }
        return true;
    }
}
