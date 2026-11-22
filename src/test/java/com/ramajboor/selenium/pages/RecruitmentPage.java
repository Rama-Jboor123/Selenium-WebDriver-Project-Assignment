package com.ramajboor.selenium.pages;

import com.ramajboor.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class RecruitmentPage extends BasePage {

    private static final By RECRUITMENT_MENU = By.cssSelector("a[href$='/recruitment/viewRecruitmentModule']");
    private static final By ADD_BUTTON = By.xpath("//button[normalize-space()='Add']");
    private static final By FILTER_TOGGLE = By.cssSelector(".oxd-table-filter-header button");
    private static final By COLLAPSED_FILTER_ICON = By.cssSelector(".bi-caret-down-fill");
    private static final By CANDIDATE_NAME_INPUT = By.cssSelector("form input[placeholder='Type for hints...']");
    private static final By CANDIDATE_NAME_SUGGESTIONS = By.xpath("//form//*[@role='listbox']"
            + "//*[@role='option' and not(starts-with(normalize-space(), 'Searching'))]");

    public RecruitmentPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateToPath("web/index.php/recruitment/viewCandidates");
    }

    public void openFromMainMenu() {
        click(RECRUITMENT_MENU);
    }

    public void clickAddCandidate() {
        click(ADD_BUTTON);
    }

    public List<String> lookUpCandidateName(String name) {
        if (!waitForVisibility(FILTER_TOGGLE).findElements(COLLAPSED_FILTER_ICON).isEmpty()) {
            click(FILTER_TOGGLE);
        }
        sendKeys(CANDIDATE_NAME_INPUT, name);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(CANDIDATE_NAME_SUGGESTIONS))
                .stream()
                .map(WebElement::getText)
                .toList();
    }
}
