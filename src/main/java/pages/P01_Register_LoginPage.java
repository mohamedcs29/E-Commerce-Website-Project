package pages;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utilities.LogUtility;
import utilities.Utility;

import java.time.Duration;


public class P01_Register_LoginPage {
    private static final Faker faker = new Faker();


    private final WebDriver driver;
    private final By emailLogin = By.cssSelector("input[data-qa='login-email']");
    private final By password = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");
    private final By username = By.cssSelector("input[data-qa='signup-name']");
    private final By emailSignUp = By.cssSelector("input[type='email'][name='email'][data-qa='signup-email']");
    private final By newUserSignupTitle = By.xpath("//h2[normalize-space()='New User Signup!']");

    private final By signupButton = By.cssSelector("button[data-qa='signup-button']");
    private final By loginTitle = By.xpath("//h2[normalize-space()='Login to your account']");
    private final By loginErrorMessage = By.cssSelector("form[action='/login'] p[style='color: red;']");
    private final By registerErrorMessage = By.cssSelector("form[action='/signup'] p[style='color: red;']");


    public P01_Register_LoginPage(WebDriver driver) {

        this.driver = driver;
    }


    public P01_Register_LoginPage enterUsername(String usernameText) {
        Utility.sendData(driver, username, usernameText);
        return this;

    }

    public P01_Register_LoginPage enterEmailSignUp(String passwordText) {
        Utility.sendData(driver, emailSignUp, passwordText);
        return this;
    }


    public P02_CreateAccountPage clickSignupExpectingSuccess() {
        Utility.clickOnElement(driver, signupButton);
        return new P02_CreateAccountPage(driver);
    }

    public P01_Register_LoginPage clickSignupExpectingFailure() {
        Utility.clickOnElement(driver, signupButton);
        return this;
    }

    public P01_Register_LoginPage assertNewUserSignupIsVisible() {
        LogUtility.info("Verifying that 'New User Signup!' title is visible...");

        Assert.assertTrue(
                driver.findElement(newUserSignupTitle).isDisplayed(),
                "New User Signup!' is not visible on the page."
        );

        LogUtility.info("New User Signup!' title is visible.");
        return this;
    }

    public void assertRegisterErrorMessageIsVisible(String expectedMessage) {
        LogUtility.info("Checking if register error message is visible and correct...");

        String actualMessage = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(registerErrorMessage))
                .getText().trim();

        Assert.assertEquals(
                actualMessage,
                expectedMessage,
                "Register error message mismatch. Expected: '" + expectedMessage + "', but was: '" + actualMessage + "'"
        );

        LogUtility.info("Register error message is correct: '" + actualMessage + "'");
    }


    public P01_Register_LoginPage assertLoginTitleVisible() {
        LogUtility.info("Verifying 'Login to your account' is visible...");
        Assert.assertTrue(
                driver.findElement(loginTitle).isDisplayed(),
                "Login to your account' title is not visible."
        );
        LogUtility.info("Login to your account' is visible.");
        return this;
    }

    public P01_Register_LoginPage enterEmailLogin(String passwordText) {
        Utility.sendData(driver, emailLogin, passwordText);
        return this;
    }

    public P01_Register_LoginPage enterPassWordLogin(String passwordText) {
        Utility.sendData(driver, password, passwordText);
        return this;
    }

    public P04_HomeAfterLogin clickLoginExpectingSuccess() {
        Utility.clickOnElement(driver, loginButton);
        return new P04_HomeAfterLogin(driver);
    }

    public P01_Register_LoginPage clickLoginExpectingFailure() {
        Utility.clickOnElement(driver, loginButton);
        return this;
    }


    public void assertLoginErrorMessageIsVisible(String expectedMessage) {
        LogUtility.info("Checking if login error message is visible and correct...");

        String actualMessage = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(loginErrorMessage))
                .getText().trim();

        Assert.assertEquals(
                actualMessage,
                expectedMessage,
                "Login error message mismatch. Expected: '" + expectedMessage + "', but was: '" + actualMessage + "'"
        );

        LogUtility.info("Login error message is correct: '" + actualMessage + "'");

    }


}