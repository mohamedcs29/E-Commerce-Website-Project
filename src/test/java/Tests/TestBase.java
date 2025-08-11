package Tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utilities.DataUtility;
import utilities.LogUtility;

import java.io.IOException;
import java.time.Duration;

import static DriverFactory.DriverFactory.*;

public class TestBase {

    @BeforeMethod(alwaysRun = true)
    public void setup() throws IOException {
        String browserType = (System.getProperty("browser") != null) ? System.getProperty("browser")
                : DataUtility.getPropertyValue("environment", "BROWSER");

        setupDriver(browserType);

        LogUtility.info("Driver is opened");

        getDriver().get(DataUtility.getPropertyValue("environment", "HOME_URL"));
        LogUtility.info("Page Redirected to the URL");
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        quitDriver();
        LogUtility.info("Driver is closed");
    }

}

