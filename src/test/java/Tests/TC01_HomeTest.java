package Tests;

import Listeners.IInvokedMethodListenerImpl;
import Listeners.ITest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.P00_HeaderAndFooter;
import pages.P04_HomeAfterLogin;
import utilities.DataUtility;
import utilities.LogUtility;
import utilities.Utility;

import java.io.IOException;

import static DriverFactory.DriverFactory.getDriver;

@Epic("AutomationExercise")
@Listeners({IInvokedMethodListenerImpl.class, ITest.class})

public class TC01_HomeTest extends TestBase {


    @Feature("Subscription")
    @Story("Verify Subscription in Footer")
    @Description("""
                Steps:
                1. Launch browser
                2. Navigate to url 'http://automationexercise.com'
                3. Verify that home page is visible successfully
                4. Scroll down to footer
                5. Verify text 'SUBSCRIPTION'
                6. Enter email address in input and click arrow button
                7. Verify success message 'You have been successfully subscribed!' is visible
            """)
    @Test
    public void verifySubscriptionInHomepage() throws IOException {

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        new P04_HomeAfterLogin(getDriver())
                .scrollToSubscriptionSection()
                .subscribeWithEmail(DataUtility.getJsonData("ValidLoginData", "registeredEmail"));

        Assert.assertTrue(
                new P04_HomeAfterLogin(getDriver()).isSubscriptionTextVisible(),
                "'SUBSCRIPTION' text is not visible."
        );

        Assert.assertTrue(
                new P04_HomeAfterLogin(getDriver()).isSubscriptionSuccessVisible(),
                "Success message for subscription is not visible."
        );

    }


    @Feature("General UI/UX")
    @Story("Verify Scroll Up and Down Functionality")
    @Severity(SeverityLevel.NORMAL)
    @Link(name = "AutomationExercise", url = "https://automationexercise.com/")
    @Description("""
            Test Steps:
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Scroll down page to bottom
            5. Verify 'SUBSCRIPTION' is visible
            6. Click on arrow at bottom right side to move upward
            7. Verify that page is scrolled up and 'Full-Fledged practice website for Automation Engineers' text is visible on screen
            """)


    @Test
    public void verifyScrollUpUsingArrowButton() throws IOException {
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        Utility.scrollToBottom(getDriver());
        LogUtility.info("Scrolled to bottom");

        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        Assert.assertTrue(header.isSubscriptionTitleVisible(), "'SUBSCRIPTION' title not visible");
        LogUtility.info("'SUBSCRIPTION' section is visible");

        header.clickScrollUpArrow();
        LogUtility.info("Clicked on scroll-up arrow");

        Assert.assertTrue(header.isHomeBannerTextVisible(), "Scroll up did not work, banner text not visible");
        LogUtility.info("Scroll up successful, top banner is visible");
    }

    @Feature("General UI/UX")
    @Test(description = "Test Case 26: Verify Scroll Up without 'Arrow' button and Scroll Down functionality")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Test Steps:
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Scroll down page to bottom
            5. Verify 'SUBSCRIPTION' is visible
            6. Scroll up page to top
            7. Verify that page is scrolled up and 'Full-Fledged practice website for Automation Engineers' text is visible on screen
            """)
    public void verifyScrollUpWithoutArrowButton() throws IOException {
        LogUtility.info("Test started: verifyScrollUpWithoutArrowButton");

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        LogUtility.info("Home page is visible");

        Utility.scrollToBottom(getDriver());
        LogUtility.info("Scrolled to bottom");

        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        Assert.assertTrue(header.isSubscriptionTitleVisible(), "'SUBSCRIPTION' title not visible");
        LogUtility.info("'SUBSCRIPTION' section is visible");

        Utility.scrollToTop(getDriver());
        LogUtility.info("Scrolled up to top");

        Assert.assertTrue(header.isHomeBannerTextVisible(), "Scroll up failed, top banner not visible");
    }


}
