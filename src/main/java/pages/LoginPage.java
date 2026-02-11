package pages;

import base.DriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final String url = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    // Locators
    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By loginBtn = By.xpath("//button[@type='submit']");

    // Required error (blank fields)
    private final By requiredError =
            By.xpath("//*[contains(@class,'oxd-input-group__message') and normalize-space()='Required']");

    // Invalid credentials message (can be alert text)
    private final By alertError =
            By.xpath("//*[contains(@class,'oxd-alert-content-text') and string-length(normalize-space())>0] | " +
                    "//p[contains(@class,'oxd-text') and contains(normalize-space(),'Invalid')]");

    // Post-login identifiers
    private final By sidePanel = By.cssSelector("aside.oxd-sidepanel");
    private final By dashboardHeader =
            By.xpath("//*[self::h6 or self::h5][contains(normalize-space(),'Dashboard')]");

    public LoginPage() {
        WebDriver d = DriverSetup.getDriver();
        this.driver = d;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(35));
    }

    public LoginPage(WebDriver driver) {
        // IMPORTANT: Steps sometimes pass null driver.
        WebDriver d = (driver != null) ? driver : DriverSetup.getDriver();
        this.driver = d;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(35));
    }

    public void openLoginPage() {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
    }

    public void enterUsername(String username) {
        WebElement u = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        u.clear();
        if (username != null) u.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement p = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        p.clear();
        if (password != null) p.sendKeys(password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
    }

    public String getRequiredErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(requiredError)).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getAlertErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(alertError)).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoggedIn() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(sidePanel),
                    ExpectedConditions.visibilityOfElementLocated(dashboardHeader)
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
