package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.WebDriver;

public class AddCandidatePage extends BasePage {

    public AddCandidatePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/recruitment/addCandidate");
    }
}
