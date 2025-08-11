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

@Listeners({IInvokedMethodListenerImpl.class, ITest.class})
public class TC05_ContactUsFormTest extends TestBase {

    @Epic("AutomationExercise")
    @Feature("Contact Us Form")
    @Story("User fills and submits contact us form")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Steps:
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Click on 'Contact Us' button
            5. Verify 'GET IN TOUCH' is visible
            6. Enter name, email, subject and message
            7. Upload file
            8. Click 'Submit' button
            9. Click OK button
            10. Verify success message 'Success! Your details have been submitted successfully.' is visible
            11. Click 'Home' button and verify that landed to home page successfully
            """)

    @Test(description = "Verify user can submit contact form successfully")
    public void verifyContactUsFormSubmission() throws IOException {
        LogUtility.info("Verifying home page URL");
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        new P00_HeaderAndFooter(getDriver())
                .clickContactUs()
                .assertGetInTouchIsVisible()
                .enterName(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterEmail(DataUtility.getJsonData("ValidLoginData", "registeredEmail"))
                .enterSubject("test")
                .enterMessage("test contact")
                .uploadFile(System.getProperty("user.dir") + DataUtility.getPropertyValue("environment", "pathFile"))
                .clickSubmitButton()
                .acceptAlertAfterSubmit()
                .assertSuccessMessageIsVisible()
                .clickHomeButton();
        Assert.assertTrue(Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "Home_URL_Expected")));


    }

}
