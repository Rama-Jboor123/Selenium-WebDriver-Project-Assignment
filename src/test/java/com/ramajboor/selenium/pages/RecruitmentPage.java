package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.WebDriver;

public class RecruitmentPage extends BasePage {

    public RecruitmentPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/recruitment/viewCandidates");
    }

    // TODO: Add verified Recruitment locators and page actions here before implementing step definitions.
}
