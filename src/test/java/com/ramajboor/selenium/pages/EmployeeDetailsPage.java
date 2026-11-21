package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.WebDriver;

public class EmployeeDetailsPage extends BasePage {

    public EmployeeDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void openByEmployeeId(String employeeId) {
        navigateToPath("web/index.php/pim/viewPersonalDetails/empNumber/" + employeeId);
    }
}
