package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utilities.Utility;

public class P03_ConfirmationRegisterPage {
    private final WebDriver driver;
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");
    private final By textSuccess = By.xpath("//b[normalize-space()='Account Created!']");

    public P03_ConfirmationRegisterPage(WebDriver driver) {
        this.driver = driver;
    }


    public P04_HomeAfterLogin clickOnContinueButton() {
        Utility.clickOnElement(driver, continueButton);
        return new P04_HomeAfterLogin(driver);

    }

    public P03_ConfirmationRegisterPage assertTextIsVisible(String expectedText) {
        Assert.assertTrue(
                Utility.verifyText(driver, textSuccess, expectedText),
                "Text mismatch! Expected: '" + expectedText + "'"
        );
        return this;
    }

}
