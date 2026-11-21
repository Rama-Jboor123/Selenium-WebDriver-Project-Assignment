package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.WebDriver;

public class AdminPage extends BasePage {

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/admin/viewSystemUsers");
    }

    // TODO: Add verified Admin page locators and page actions here before implementing step definitions.
}
