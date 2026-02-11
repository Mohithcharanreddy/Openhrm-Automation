package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForgotPasswordPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Same as LoginPage URL
    private final String loginUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    // More flexible locator: sometimes <p>, sometimes <a>, class can vary
    private final By forgotPasswordLink =
            By.xpath("//*[self::p or self::a][contains(normalize-space(),'Forgot your password')]");

    private final By forgotPasswordHeader = By.xpath("//h6[normalize-space()='Reset Password']");
    private final By usernameInput = By.name("username");
    private final By resetPasswordBtn = By.xpath("//button[@type='submit']");

    private final By successTitle =
            By.xpath("//h6[contains(normalize-space(),'Reset Password link sent successfully') or contains(normalize-space(),'Reset Password')]");

    private final By successMessage =
            By.xpath("//*[contains(normalize-space(),'Reset Password link sent successfully') or contains(normalize-space(),'successfully')]");

    private final By errorMessage =
            By.xpath("//*[contains(@class,'oxd-alert-content-text') or contains(@class,'oxd-input-field-error-message') or contains(@class,'oxd-text--toast-message')]");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public void openLoginPage() {
        driver.get(loginUrl);
        // wait any login element stable enough
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
    }

    public void clickForgotPassword() {
        // Ensure we are on login page always
        openLoginPage();
        wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
    }

    public boolean isForgotPasswordPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(forgotPasswordHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterUsername(String username) {
        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        user.clear();
        user.sendKeys(username);
    }

    public void clickResetPassword() {
        wait.until(ExpectedConditions.elementToBeClickable(resetPasswordBtn)).click();
    }

    public String getResetSuccessMessage() {
        try {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
                String t = driver.findElement(successMessage).getText();
                return t == null ? "" : t.trim();
            } catch (Exception ignored) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(successTitle));
                String t = driver.findElement(successTitle).getText();
                return t == null ? "" : t.trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getResetErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String t = driver.findElement(errorMessage).getText();
            return t == null ? "" : t.trim();
        } catch (Exception e) {
            return "";
        }
    }
}
