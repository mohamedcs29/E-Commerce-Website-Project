package DriverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * @param browser (chrome, firefox, edge).
     */
    public static void setupDriver(String browser) {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.credit_card_enabled", false);

        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("prefs", prefs);
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

                driverThreadLocal.set(new ChromeDriver(chromeOptions));
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("signon.rememberSignons", false);
                firefoxOptions.setProfile(profile);

                driverThreadLocal.set(new FirefoxDriver(firefoxOptions));
                break;

            default:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setExperimentalOption("prefs", prefs);
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});


                driverThreadLocal.set(new EdgeDriver(edgeOptions));
                break;
        }
    }

    /**
     * @return WebDriver instance.
     */
    public static WebDriver getDriver() {

        return driverThreadLocal.get();
    }


    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }
}
