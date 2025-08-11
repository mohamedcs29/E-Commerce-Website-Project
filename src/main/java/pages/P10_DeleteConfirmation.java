package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utilities.LogUtility;
import utilities.Utility;

public class P10_DeleteConfirmation {
    private final WebDriver driver;
    private final By accountDeletedMessage = By.cssSelector("h2[data-qa='account-deleted']");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");

    public P10_DeleteConfirmation(WebDriver driver) {
        this.driver = driver;

    }


    public P10_DeleteConfirmation assertAccountDeletedMessageVisible() {
        LogUtility.info("Checking visibility of 'ACCOUNT DELETED!' message...");
        Assert.assertTrue(
                driver.findElement(accountDeletedMessage).isDisplayed(),
                "ACCOUNT DELETED!' message is not visible on the page."
        );
        LogUtility.info("'ACCOUNT DELETED!' message is visible.");
        return this;
    }

    public P04_HomeAfterLogin clickContinueButton() {
        LogUtility.info("Clicking on 'Continue' button after account deletion...");
        Utility.clickOnElement(driver, continueButton);
        return new P04_HomeAfterLogin(driver);
    }


}