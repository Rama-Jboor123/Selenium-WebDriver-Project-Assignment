package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.WebDriver;

public class LeaveListPage extends BasePage {

    public LeaveListPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/leave/viewLeaveList");
    }

    // TODO: Add verified Leave List locators and page actions here before implementing step definitions.
}
