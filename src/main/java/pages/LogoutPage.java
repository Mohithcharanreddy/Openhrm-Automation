package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LogoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // user dropdown + logout
    private final By userDropdown =
            By.xpath("//p[contains(@class,'oxd-userdropdown-name')] | //span[contains(@class,'oxd-userdropdown-tab')]");

    private final By logoutLink =
            By.xpath("//a[normalize-space()='Logout']");

    // login page identifier
    private final By loginUsername = By.name("username");

    private final By loadingSpinner = By.cssSelector(".oxd-loading-spinner");

    public LogoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    private void waitSpinnerGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (Exception ignored) {}
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void logout() {
        waitSpinnerGone();

        WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(userDropdown));
        try { dd.click(); } catch (Exception e) { jsClick(dd); }

        WebElement lo = wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        try { lo.click(); } catch (Exception e) { jsClick(lo); }

        wait.until(ExpectedConditions.visibilityOfElementLocated(loginUsername));
        waitSpinnerGone();
    }

    public boolean isOnLoginPage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginUsername)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
