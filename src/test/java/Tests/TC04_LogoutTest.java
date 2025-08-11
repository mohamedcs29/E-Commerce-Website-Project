package Tests;

import Listeners.IInvokedMethodListenerImpl;
import Listeners.ITest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.P00_HeaderAndFooter;
import utilities.DataUtility;
import utilities.Utility;

import java.io.IOException;

import static DriverFactory.DriverFactory.getDriver;

@Listeners({IInvokedMethodListenerImpl.class, ITest.class})

public class TC04_LogoutTest extends TestBase {

    @Test(description = "Logout User after Successful Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Steps:
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Click on 'Signup / Login' button
            5. Verify 'Login to your account' is visible
            6. Enter correct email address and password
            7. Click 'login' button
            8. Verify that 'Logged in as username' is visible
            9. Click 'Logout' button
            10. Verify that user is navigated to login page
            """)
    public void verifyLogoutUserAfterLogin() throws IOException {
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        new P00_HeaderAndFooter(getDriver())
                .clickSignupLogin()
                .assertLoginTitleVisible()
                .enterEmailLogin(DataUtility.getJsonData("ValidLoginData", "registeredEmail"))
                .enterPassWordLogin(DataUtility.getJsonData("ValidLoginData", "password"))
                .clickLoginExpectingSuccess()
                .assertLoggedInUserTextIsVisible("Mohamed Hamdy")
                .clickLogout();
        Assert.assertTrue(Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "LOGIN_Register_URL")));


    }


}
