package utilities;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;


public class Utility {
    private static final String SCREEN_SHOT = "test_outputs/screenshots/";

    /**
     * @param driver  The WebDriver instance.
     * @param locator The By locator to find the element.
     */

//ToDo: clicking on element

    /**
     * @param driver  The WebDriver instance.
     * @param locator The By locator of the element to click.
     */
    public static void clickOnElement(WebDriver driver, By locator) {
        // إنشاء كائن الانتظار
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        wait.until(ExpectedConditions.elementToBeClickable(locator)); // تأكد من أنه قابل للنقر

        WebElement elementToClick = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementToClick);
        elementToClick.click();
    }


    /**
     * Helper method to close Google ads if they appear.
     *
     * @param driver The WebDriver instance.
     */
    private static void handleAds(WebDriver driver) {
        try {
            // Ads often appear in an iframe with id starting with 'aswift_'
            // We switch to it, look for a close button, and switch back.
            List<WebElement> adFrames = driver.findElements(By.cssSelector("iframe[id^='aswift_']"));
            if (!adFrames.isEmpty()) {
                driver.switchTo().frame(adFrames.get(0));
                // Inside the first iframe, there might be another one
                List<WebElement> innerFrames = driver.findElements(By.id("ad_iframe"));
                if (!innerFrames.isEmpty()) {
                    driver.switchTo().frame(innerFrames.get(0));
                }

                // Look for a close button (selectors can vary)
                List<WebElement> closeButtons = driver.findElements(By.id("dismiss-button"));
                if (closeButtons.isEmpty()) {
                    // Alternative selector for close button
                    closeButtons = driver.findElements(By.xpath("//div[contains(text(), 'Close')] | //span[contains(text(), 'Close')]"));
                }

                if (!closeButtons.isEmpty()) {
                    closeButtons.get(0).click();
                    System.out.println("Ad closed successfully.");
                }

                driver.switchTo().defaultContent(); // IMPORTANT: Always switch back to the main page
            }
        } catch (NoSuchFrameException | NoSuchElementException e) {
            // If ad or close button not found, that's fine. Just switch back and continue.
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            // Catch any other exceptions to prevent test failure
            System.err.println("Could not handle ad, but continuing test. Error: " + e.getMessage());
            driver.switchTo().defaultContent();
        }
    }


    public static void sendData(WebDriver driver, By locator, String text) {

        new WebDriverWait(driver, Duration.ofSeconds(20))

                .until(ExpectedConditions.visibilityOfElementLocated(locator));

        driver.findElement(locator).sendKeys(text);

    }

    //TODO: selecting from drop down
    public static void selectingFromDropDown(WebDriver driver, By locator, String option) {
        new Select(findWebElement(driver, locator)).selectByVisibleText(option);
    }


    //TODO: Taking screenshots for all page
    public static void takeScreenshot(WebDriver driver, String screenshotName) {
        try {
            File dir = new File(SCREEN_SHOT);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Screenshot fpScreenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .takeScreenshot(driver);

            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String imagePath = SCREEN_SHOT + screenshotName + "-" + timestamp + ".png";

            ImageIO.write(fpScreenshot.getImage(), "PNG", new File(imagePath));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(fpScreenshot.getImage(), "PNG", baos);
            baos.flush();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            Allure.addAttachment(screenshotName, bais);
            baos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Taking screenshots
    public static void takeScreenshotForPart(WebDriver driver, String screenshotName) {
        try {
            File screenSrc = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File screenDest = new File(SCREEN_SHOT + screenshotName + "-" + getTimeStamp() + ".png");
            FileUtils.copyFile(screenSrc, screenDest);
            Allure.addAttachment(screenshotName, Files.newInputStream(Paths.get(screenDest.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //TODO: converting by to web element
    public static WebElement findWebElement(
            WebDriver driver, By location) {
        return driver.findElement(location);
    }

    //TODO: Scrolling to element
    public static void scrolling(WebDriver driver, By locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", findWebElement(driver, locator));
    }


    //TODO: general wait
    public static WebDriverWait generalWait(WebDriver driver, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    //TODO: general wait
    public static void giveWait(int seconds) {
        await().pollDelay(seconds, TimeUnit.SECONDS).until(() -> true);
    }


//TODO: uploading files using ROBOT

    //TODO: get timestamp
    public static String getTimeStamp() {

        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-a").format(new Date());
    }

    //TODO: verifyLink
    public static boolean isCurrentUrlEquals(WebDriver driver, String expectedUrl) {

        return driver.getCurrentUrl().equals(expectedUrl);
    }


    //TODO: verifyText
    public static boolean verifyText(WebDriver driver, By locator, String expectedText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            String actualText = wait.until(ExpectedConditions
                            .visibilityOfElementLocated(locator))
                    .getText()
                    .replaceAll("\\s+", " ")
                    .trim();

            String normalizedExpected = expectedText
                    .replaceAll("\\s+", " ")
                    .trim();

            return actualText.equalsIgnoreCase(normalizedExpected);

        } catch (TimeoutException e) {
            System.err.println("❌ Timeout: Element not visible - " + locator);
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error during text verification for: " + locator);
            e.printStackTrace();
            return false;
        }
    }


    public static String getElementText(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(locator))
                .getText().trim();
    }

    public static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static void waitForVisibility(WebDriver driver, By locator, int timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static void scrollToTop(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");
    }


    public static File getLatestFile(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        assert files != null;
        if (files.length == 0)
            return null;
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        return files[0];
    }


}
