package Tests;

import Listeners.IInvokedMethodListenerImpl;
import Listeners.ITest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.P00_HeaderAndFooter;
import utilities.DataUtility;
import utilities.LogUtility;
import utilities.Utility;

import java.io.IOException;

import static DriverFactory.DriverFactory.getDriver;

@Epic("AutomationExercise")
@Feature("Registration")
@Listeners({IInvokedMethodListenerImpl.class, ITest.class})

public class TC02_RegisterTest extends TestBase {


    @Test(description = "Valid Register User")
    @Story("Register a new user successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Steps:
            1. Launch browser and navigate to home page
            2. Click on 'Signup/Login' button
            3. Verify 'New User Signup!' is visible
            4. Enter name and unique email
            5. Click 'Signup' and verify 'ENTER ACCOUNT INFORMATION'
            6. Fill details: Title, Password, DOB
            7. Select checkboxes
            8. Fill personal and address details
            9. Click 'Create Account'
            10. Verify account created, logged in, then deleted
            """)
    public void verifyValidRegisterUser() throws IOException {
        String email = "mohamed" + Utility.getTimeStamp() + "@gmail.com";

        LogUtility.info("Verifying home page URL");
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        new P00_HeaderAndFooter(getDriver())
                .clickSignupLogin()
                .assertNewUserSignupIsVisible()
                .enterUsername(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterEmailSignUp(email)
                .clickSignupExpectingSuccess()
                .assertAccountInformationIsVisible("ENTER ACCOUNT INFORMATION")
                .clickOnRadioButton()
                .enterPassword(DataUtility.getJsonData("ValidLoginData", "password"))
                .checkNewsletter()
                .checkSpecialOffers()
                .selectDayDropdown(DataUtility.getJsonData("ValidLoginData", "days"))
                .selectMonthDropdown(DataUtility.getJsonData("ValidLoginData", "month"))
                .selectYearDropdown(DataUtility.getJsonData("ValidLoginData", "year"))
                .enterFirstName(DataUtility.getJsonData("ValidLoginData", "firstname"))
                .enterLastName(DataUtility.getJsonData("ValidLoginData", "lastname"))
                .enterAddress(DataUtility.getJsonData("ValidLoginData", "address"))
                .selectDropDownCountry(DataUtility.getJsonData("ValidLoginData", "country"))
                .enterState(DataUtility.getJsonData("ValidLoginData", "state"))
                .enterCity(DataUtility.getJsonData("ValidLoginData", "city"))
                .enterZipcode(DataUtility.getJsonData("ValidLoginData", "zipcode"))
                .enterPhoneNumber(DataUtility.getJsonData("ValidLoginData", "phoneNumber"))
                .clickOnCreateAccountButton()
                .assertTextIsVisible("Account Created!")
                .clickOnContinueButton()
                .assertLoggedInUserTextIsVisible("Mohamed Hamdy")
                .clickDeleteAccount()
                .assertAccountDeletedMessageVisible()
                .clickContinueButton();


    }


    @Test(description = "TC1 Register with existing email should show error")
    @Story("User tries to register with an existing email")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Steps:
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Click on 'Signup / Login' button
            5. Verify 'New User Signup!' is visible
            6. Enter name and already registered email address
            7. Click 'Signup' button
            8. Verify error 'Email Address already exist!' is visible
            """)
    public void verifyRegisterUserWithExistingEmail() throws IOException {

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        new P00_HeaderAndFooter(getDriver())
                .clickSignupLogin()
                .assertNewUserSignupIsVisible()
                .enterUsername(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterEmailSignUp(DataUtility.getJsonData("ValidLoginData", "registeredEmail"))
                .clickSignupExpectingFailure()
                .assertRegisterErrorMessageIsVisible("Email Address already exist!");

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "REGISTER_URL")),
                "Home page is not visible."
        );
    }


}