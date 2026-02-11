package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverSetup {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // IMPORTANT: avoid driver.manage().window().maximize() (your Runtime.evaluate error)
            options.addArguments("--start-maximized");

            // extra stability (optional but safe)
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);

            // DO NOT maximize via driver.manage().window().maximize()
            // It caused: Runtime.evaluate wasn't found on Chrome 144 in your logs
        }
        return driver;
    }

    public static void quitDriver() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception ignored) {
        } finally {
            driver = null;
        }
    }
}
