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
    private static final By CANDIDATE_NAME_INPUT = By.cssSelector("form input[placeholder='Type for hints...']");
    private static final By CANDIDATE_NAME_SUGGESTIONS = By.xpath("//form//*[@role='listbox']"
            + "//*[@role='option' and not(starts-with(normalize-space(), 'Searching'))]");
    private static final By SEARCH_BUTTON = By.xpath("//form//button[@type='submit' and normalize-space()='Search']");
    private static final By RESULTS_SUMMARY = By.xpath("//span[normalize-space()='No Records Found'"
            + " or contains(normalize-space(), ') Record Found')"
            + " or contains(normalize-space(), ') Records Found')]");
    private static final By TABLE_LOADER = By.cssSelector(".oxd-loading-spinner, .oxd-table-loader");
    private static final By SINGLE_RECORD_COUNT = By.xpath("//span[normalize-space()='(1) Record Found']");
    private static final By CANDIDATE_ROWS = By.cssSelector(".oxd-table-card");
    // The compact layout labels its cells. The desktop Candidate column is the third cell.
    private static final By CANDIDATE_NAME_CELL = By.xpath(".//div[@class='header' and normalize-space()='Candidate']"
            + "/following-sibling::div[@class='data'] | .//*[@role='cell'][3]/div[not(@class)]");
    private static final By VIEW_CANDIDATE = By.cssSelector("button:has(.bi-eye-fill)");
    private static final By DELETE_CANDIDATE = By.cssSelector("button:has(.bi-trash)");
    private static final By CONFIRM_DELETE = By.xpath("//button[normalize-space()='Yes, Delete']");

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
        return findCandidateNameSuggestions(name).stream().map(WebElement::getText).toList();
    }

    public boolean searchForCandidate(String firstName, String fullName) {
        var matchingSuggestion = findCandidateNameSuggestions(firstName).stream()
                .filter(suggestion -> fullName.equals(suggestion.getText()))
                .findFirst();
        if (matchingSuggestion.isEmpty()) {
            return false;
        }

        matchingSuggestion.get().click();
        click(SEARCH_BUTTON);
        waitForVisibility(SINGLE_RECORD_COUNT);
        return true;
    }

    public String getListedCandidateName() {
        return getSingleCandidateRow().findElement(CANDIDATE_NAME_CELL).getText();
    }

    public void openListedCandidateProfile() {
        getSingleCandidateRow().findElement(VIEW_CANDIDATE).click();
    }

    public void deleteListedCandidate(String fullName) {
        WebElement row = getSingleCandidateRow();
        String listedName = row.findElement(CANDIDATE_NAME_CELL).getText();
        if (!fullName.equals(listedName)) {
            throw new IllegalStateException("Expected to delete " + fullName + " but found " + listedName);
        }

        row.findElement(DELETE_CANDIDATE).click();
        click(CONFIRM_DELETE);
        wait.until(ExpectedConditions.numberOfElementsToBe(CANDIDATE_ROWS, 0));
    }

    private WebElement getSingleCandidateRow() {
        return wait.until(ExpectedConditions.numberOfElementsToBe(CANDIDATE_ROWS, 1)).getFirst();
    }

    private List<WebElement> findCandidateNameSuggestions(String name) {
        waitForVisibility(RESULTS_SUMMARY);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(TABLE_LOADER));
        expandTableFilters();
        WebElement nameInput = waitForClickability(CANDIDATE_NAME_INPUT);
        nameInput.clear();
        nameInput.click();
        nameInput.sendKeys(name);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(CANDIDATE_NAME_SUGGESTIONS));
    }
}
