package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MyInfoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Left menu
    private final By myInfoMenu =
            By.xpath("//span[normalize-space()='My Info']/ancestor::a[1] | //a[contains(@href,'/pim/viewMyDetails')]");

    // Page headers/tabs
    private final By personalDetailsHeader =
            By.xpath("//*[self::h6 or self::h5][normalize-space()='Personal Details']");
    private final By contactDetailsTab =
            By.xpath("//a[contains(@href,'contactDetails') and normalize-space()='Contact Details'] | //a[normalize-space()='Contact Details']");
    private final By contactDetailsHeader =
            By.xpath("//*[self::h6 or self::h5][normalize-space()='Contact Details']");

    // Personal Details fields (stable)
    private final By otherIdInput =
            By.xpath("//label[normalize-space()='Other Id']/ancestor::div[contains(@class,'oxd-input-group') or contains(@class,'oxd-input-field-bottom-space')]//input");

    private final By driversLicenseInput =
            By.xpath("//label[normalize-space()=\"Driver's License Number\"]/ancestor::div[contains(@class,'oxd-input-group') or contains(@class,'oxd-input-field-bottom-space')]//input");

    // Contact Details fields (stable)
    private final By mobileInput =
            By.xpath("//label[normalize-space()='Mobile']/ancestor::div[contains(@class,'oxd-input-group') or contains(@class,'oxd-input-field-bottom-space')]//input");

    // Save buttons (multiple exist; pick nearest visible/clickable)
    private final By saveButtons =
            By.xpath("//button[@type='submit' and normalize-space()='Save']");

    // Loading spinner
    private final By loadingSpinner = By.cssSelector(".oxd-loading-spinner");

    public MyInfoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(35));
    }

    // ----------------- helpers -----------------

    private void waitSpinnerGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (Exception ignored) {}
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {}
    }

    private WebElement visible(By by) {
        waitSpinnerGone();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private WebElement clickable(By by) {
        waitSpinnerGone();
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    private void clearAndType(WebElement el, String text) {
        try { el.click(); } catch (Exception e) { jsClick(el); }
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.BACK_SPACE);
        if (text != null) el.sendKeys(text);
    }

    private void clickBestSaveButton() {
        waitSpinnerGone();
        try {
            for (WebElement b : driver.findElements(saveButtons)) {
                try {
                    if (b != null && b.isDisplayed() && b.isEnabled()) {
                        scrollIntoView(b);
                        try { b.click(); } catch (Exception e) { jsClick(b); }
                        waitSpinnerGone();
                        return;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}

        // fallback: first clickable Save
        WebElement s = clickable(saveButtons);
        try { s.click(); } catch (Exception e) { jsClick(s); }
        waitSpinnerGone();
    }

    // ----------------- actions -----------------

    public void openMyInfo() {
        waitSpinnerGone();
        WebElement mi = clickable(myInfoMenu);
        try { mi.click(); } catch (Exception e) { jsClick(mi); }
        waitSpinnerGone();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader),
                ExpectedConditions.visibilityOfElementLocated(contactDetailsHeader)
        ));
        waitSpinnerGone();
    }

    public boolean isPersonalDetailsDisplayed() {
        try {
            return visible(personalDetailsHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void updatePersonalDetailsOtherId(String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
        WebElement other = visible(otherIdInput);
        scrollIntoView(other);
        clearAndType(other, value);
        clickBestSaveButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
    }

    public String getPersonalDetailsOtherIdValue() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
        try {
            WebElement other = visible(otherIdInput);
            return other.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public void updatePersonalDetailsDriversLicense(String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
        WebElement dl = visible(driversLicenseInput);
        scrollIntoView(dl);
        clearAndType(dl, value);
        clickBestSaveButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
    }

    public void openContactDetails() {
        waitSpinnerGone();
        WebElement tab = clickable(contactDetailsTab);
        try { tab.click(); } catch (Exception e) { jsClick(tab); }
        waitSpinnerGone();
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsHeader));
        waitSpinnerGone();
    }

    public boolean isContactDetailsDisplayed() {
        try {
            return visible(contactDetailsHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void updateContactMobile(String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsHeader));
        WebElement mob = visible(mobileInput);
        scrollIntoView(mob);
        clearAndType(mob, value);
        clickBestSaveButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsHeader));
    }

    public String getContactMobileValue() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsHeader));
        try {
            WebElement mob = visible(mobileInput);
            return mob.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }
}
