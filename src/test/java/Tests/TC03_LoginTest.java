package Tests;

import Listeners.IInvokedMethodListenerImpl;
import Listeners.ITest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.P00_HeaderAndFooter;
import utilities.DataUtility;
import utilities.Utility;

import java.io.IOException;

import static DriverFactory.DriverFactory.getDriver;

@Epic("AutomationExercise")
@Feature("Login ")
@Listeners({IInvokedMethodListenerImpl.class, ITest.class})
public class TC03_LoginTest extends TestBase {

    @Test(description = "Login User with Correct Email and Password")
    @Story("Login with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
                Steps:
                1. Launch browser and navigate to Home Page
                2. Click 'Signup/Login'
                3. Verify Login section is visible
                4. Enter valid email and password
                5. Click Login and verify user is logged in
            """)
    public void verifyLoginWithValidCredentials() throws IOException {

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        new P00_HeaderAndFooter(getDriver())
                .clickSignupLogin()
                .assertLoginTitleVisible()
                .enterEmailLogin(DataUtility.getJsonData("ValidLoginData", "registeredEmail"))
                .enterPassWordLogin(DataUtility.getJsonData("ValidLoginData", "password"))
                .clickLoginExpectingSuccess()
                .assertLoggedInUserTextIsVisible("Mohamed Hamdy");
        //.clickDeleteAccount()
        //.assertAccountDeletedMessageVisible()
        //.clickContinueButton();


    }


    @Test(description = "Login User with Incorrect Email and Password")
    @Story("Login with invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
                Steps:
                1. Launch browser and navigate to Home Page
                2. Click 'Signup/Login'
                3. Verify Login section is visible
                4. Enter invalid email and password
                5. Click Login and verify error message is displayed
            """)
    public void verifyLoginUserWithIncorrectEmailAndPassword() throws IOException {

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );


        new P00_HeaderAndFooter(getDriver())
                .clickSignupLogin()
                .assertLoginTitleVisible()
                .enterEmailLogin(DataUtility.getJsonData("ValidLoginData", "incorrectEmail"))
                .enterPassWordLogin(DataUtility.getJsonData("ValidLoginData", "incorrectPassword"))
                .clickLoginExpectingFailure()
                .assertLoginErrorMessageIsVisible("Your email or password is incorrect!");

    }


}


