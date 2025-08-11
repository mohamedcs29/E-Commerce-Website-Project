package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utilities.LogUtility;
import utilities.Utility;

import java.time.Duration;

public class P11_ContactUsForm {
    private final WebDriver driver;

    private final By getInTouchTitle = By.cssSelector("div.contact-form > h2.title.text-center");
    private final By nameInput = By.cssSelector("input[data-qa='name']");
    private final By emailInput = By.cssSelector("input[data-qa='email']");
    private final By subjectInput = By.cssSelector("input[data-qa='subject']");
    private final By messageTextarea = By.cssSelector("textarea[data-qa='message']");
    private final By uploadFileInput = By.cssSelector("input[name='upload_file']");
    private final By submitButton = By.cssSelector("input[data-qa='submit-button']");
    private final By successMessage = By.cssSelector("div.status.alert.alert-success");
    private final By homeButton = By.cssSelector("a.btn.btn-success");

    public P11_ContactUsForm(WebDriver driver) {
        this.driver = driver;

    }

    public P11_ContactUsForm assertGetInTouchIsVisible() {
        LogUtility.info("Verifying that 'GET IN TOUCH' title is visible...");

        Assert.assertTrue(
                driver.findElement(getInTouchTitle).isDisplayed(),
                "'GET IN TOUCH' title is not visible on the page."
        );

        LogUtility.info("GET IN TOUCH' title is visible.");
        return this;
    }

    public P11_ContactUsForm enterName(String name) {
        Utility.sendData(driver, nameInput, name);
        return this;
    }

    public P11_ContactUsForm enterEmail(String email) {
        Utility.sendData(driver, emailInput, email);
        return this;
    }

    public P11_ContactUsForm enterSubject(String subject) {
        Utility.sendData(driver, subjectInput, subject);
        return this;
    }

    public P11_ContactUsForm enterMessage(String message) {
        Utility.sendData(driver, messageTextarea, message);
        return this;
    }

    public P11_ContactUsForm uploadFile(String filePath) {
        driver.findElement(uploadFileInput).sendKeys(filePath);
        Utility.giveWait(5);
// No Utility needed
        return this;
    }

    public P11_ContactUsForm clickSubmitButton() {
        Utility.clickOnElement(driver, submitButton);
        return this;
    }

    public P11_ContactUsForm acceptAlertAfterSubmit() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.alertIsPresent());

        driver.switchTo().alert().accept();
        LogUtility.info("JavaScript alert accepted successfully.");
        return this;
    }

    public P11_ContactUsForm assertSuccessMessageIsVisible() {
        Assert.assertTrue(
                driver.findElement(successMessage).isDisplayed(),
                "Success message is not visible after submitting the form."
        );
        LogUtility.info("Success message is visible.");
        return this;
    }


    public P00_HeaderAndFooter clickHomeButton() {
        Utility.clickOnElement(driver, homeButton);
        LogUtility.info("Clicked on 'Home' button after submitting Contact Us form.");
        return new P00_HeaderAndFooter(driver);
    }

}