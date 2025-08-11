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

public class TC06_TestCasesPage extends TestBase {

    @Epic("General UI/UX")
    @Feature("Navigation")
    @Story("Test Cases Page Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Steps:
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Click on 'Test Cases' button
            5. Verify user is navigated to test cases page successfully
            """)
    @Test
    public void VerifyTestCasesPage() throws IOException {
        LogUtility.info("Verifying home page URL");
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        new P00_HeaderAndFooter(getDriver())
                .clickTestCases();
        LogUtility.info("Verifying Test Cases page URL");

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "testcases_Url")),
                "Test Cases page is not opened."
        );
    }


}
