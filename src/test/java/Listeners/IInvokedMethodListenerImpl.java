package Listeners;

import io.qameta.allure.Allure;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import utilities.LogUtility;
import utilities.Utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static DriverFactory.DriverFactory.getDriver;

public class IInvokedMethodListenerImpl implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        System.out.println("Starting test method: " + method.getTestMethod().getMethodName());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        File logFile = Utility.getLatestFile("test_outputs/Logs");
        try {
            if (logFile != null) {
                Allure.addAttachment("logs.log", Files.readString(Path.of(logFile.getPath())));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if (testResult.getStatus() == ITestResult.FAILURE) {
            LogUtility.info("Test Case " + testResult.getName() + " Failed");

            if (getDriver() != null) {
                Utility.takeScreenshot(getDriver(), testResult.getName());
            } else {
                LogUtility.error("Driver is null. Cannot take screenshot for " + testResult.getName());
            }
        }
    }
}
