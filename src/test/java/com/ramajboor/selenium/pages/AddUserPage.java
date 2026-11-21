package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.WebDriver;

public class AddUserPage extends BasePage {

    public AddUserPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/admin/saveSystemUser");
    }

    // TODO: Add verified Add User page locators and page actions here before implementing step definitions.
}
