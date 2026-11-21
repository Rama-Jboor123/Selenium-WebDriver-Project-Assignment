package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.WebDriver;

public class AddEmployeePage extends BasePage {

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/pim/addEmployee");
    }

    // TODO: Add verified Add Employee locators and page actions here before implementing step definitions.
}
