package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.WebDriver;

public class EmployeeListPage extends BasePage {

    public EmployeeListPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/pim/viewEmployeeList");
    }
}
