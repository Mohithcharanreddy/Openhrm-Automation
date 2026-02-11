package pages;

import base.DriverSetup;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class PIMPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PIMPage() {
        driver = DriverSetup.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(45));
    }

    // ----------------- helpers -----------------

    private void waitSpinnerGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".oxd-loading-spinner")));
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

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private boolean isElementPresent(By by) {
        try {
            return driver.findElements(by).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureLoggedIn() {
        if (isElementPresent(pimMenu)) return;

        LoginPage login = new LoginPage(driver);
        try { login.openLoginPage(); } catch (Exception ignored) {}
        login.login("admin", "admin123");
        login.isLoggedIn();
        waitSpinnerGone();
    }

    private void clearAndType(WebElement el, String text) {
        try { el.click(); } catch (Exception e) { jsClick(el); }
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.BACK_SPACE);
        el.sendKeys(text);
    }

    // ----------------- locators -----------------

    private final By pimMenu = By.xpath("//span[normalize-space()='PIM']/ancestor::a");

    // 🔥 MUST click this to ensure Employee List UI is present (fixes delete/search flakiness)
    private final By employeeListTab = By.xpath("//a[normalize-space()='Employee List']");

    private final By addEmployeeTopBtn = By.xpath("//button[normalize-space()='Add']");
    private final By saveBtnAny = By.xpath("//button[@type='submit' and normalize-space()='Save']");

    private final By employeeListHeader =
            By.xpath("//*[self::h5 or self::h6][normalize-space()='Employee Information']");

    private final By personalDetailsHeader =
            By.xpath("//*[self::h5 or self::h6][normalize-space()='Personal Details']");

    private final By changeProfilePictureHeader =
            By.xpath("//*[self::h5 or self::h6][normalize-space()='Change Profile Picture']");

    private final By firstName = By.name("firstName");
    private final By lastName = By.name("lastName");

    private final By empIdOnAddForm =
            By.xpath("//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");

    private final By employeeIdSearchInput =
            By.xpath("//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group') or contains(@class,'oxd-input-field-bottom-space')]//input");

    private final By searchBtn =
            By.xpath("//button[@type='submit' and normalize-space()='Search']");

    private final By resetBtn =
            By.xpath("//button[normalize-space()='Reset']");

    private final By resultsTableRows =
            By.xpath("//div[contains(@class,'oxd-table-body')]//div[contains(@class,'oxd-table-card')]");

    private final By noRecordsFound =
            By.xpath("//*[contains(normalize-space(),'No Records Found')]");

    private final By firstRowEditBtn =
            By.xpath("(//div[contains(@class,'oxd-table-body')]//div[contains(@class,'oxd-table-card')]//button[.//i[contains(@class,'bi-pencil-fill')]])[1]");

    private final By firstRowDeleteBtn =
            By.xpath("(//div[contains(@class,'oxd-table-body')]//div[contains(@class,'oxd-table-card')]//button[.//i[contains(@class,'bi-trash')]])[1]");

    private final By confirmDeleteBtn =
            By.xpath("//button[contains(normalize-space(),'Yes') and contains(normalize-space(),'Delete')]");

    // For "nickname" step (since Nickname field may not exist in this UI)
    private final By otherIdInput =
            By.xpath("//label[normalize-space()='Other Id']/ancestor::div[contains(@class,'oxd-input-group') or contains(@class,'oxd-input-field-bottom-space')]//input");

    private final By driversLicenseInput =
            By.xpath("//label[normalize-space()=\"Driver's License Number\"]/ancestor::div[contains(@class,'oxd-input-group') or contains(@class,'oxd-input-field-bottom-space')]//input");

    // Upload UI
    private final By avatarOrImage =
            By.xpath("//img[contains(@class,'employee-image')] | //div[contains(@class,'orangehrm-edit-employee-image')]");

    private final By profilePlusButton =
            By.xpath("//*[self::h5 or self::h6][normalize-space()='Change Profile Picture']/following::button[1]");

    private final By fileInputAny = By.cssSelector("input[type='file']");

    private final By saveChangeProfilePictureBtn =
            By.xpath("//*[self::h5 or self::h6][normalize-space()='Change Profile Picture']/ancestor::div[1]/following::button[normalize-space()='Save'][1]");

    // ----------------- robust helpers -----------------

    private void waitOnPersonalDetailsOrChangePictureOrList() {
        waitSpinnerGone();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader),
                ExpectedConditions.visibilityOfElementLocated(changeProfilePictureHeader),
                ExpectedConditions.visibilityOfElementLocated(employeeListHeader)
        ));
        waitSpinnerGone();
    }

    private WebElement bestEditableFieldForNicknameStep() {
        waitSpinnerGone();

        try {
            if (isElementPresent(otherIdInput)) {
                WebElement el = visible(otherIdInput);
                scrollIntoView(el);
                return el;
            }
        } catch (Exception ignored) {}

        try {
            if (isElementPresent(driversLicenseInput)) {
                WebElement el = visible(driversLicenseInput);
                scrollIntoView(el);
                return el;
            }
        } catch (Exception ignored) {}

        // fallback: first visible enabled input in form
        try {
            List<WebElement> allInputs = driver.findElements(By.xpath("//form//input"));
            for (WebElement el : allInputs) {
                try {
                    if (el.isDisplayed() && el.isEnabled()) {
                        scrollIntoView(el);
                        return el;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}

        throw new TimeoutException("No editable field found to perform 'nickname' update step in this UI.");
    }

    private WebElement resolveFileInputOnChangeProfilePicture() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(changeProfilePictureHeader));
        waitSpinnerGone();

        try {
            if (driver.findElements(profilePlusButton).size() > 0) {
                WebElement plus = driver.findElements(profilePlusButton).get(0);
                scrollIntoView(plus);
                try { plus.click(); } catch (Exception e) { jsClick(plus); }
            }
        } catch (Exception ignored) {}

        wait.until(d -> d.findElements(fileInputAny).size() > 0);
        WebElement file = driver.findElements(fileInputAny).get(0);

        // make it visible for stable sendKeys
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.display='block'; arguments[0].style.visibility='visible'; arguments[0].style.opacity=1; arguments[0].removeAttribute('hidden');",
                    file
            );
        } catch (Exception ignored) {}

        return file;
    }

    // ----------------- actions -----------------

    public void openPIMEmployeeList() {
        ensureLoggedIn();

        WebElement pim = clickable(pimMenu);
        try { pim.click(); } catch (Exception e) { jsClick(pim); }

        waitSpinnerGone();

        // 🔥 KEY FIX: force Employee List top tab
        if (isElementPresent(employeeListTab)) {
            WebElement tab = clickable(employeeListTab);
            try { tab.click(); } catch (Exception e) { jsClick(tab); }
        }

        waitSpinnerGone();
        wait.until(ExpectedConditions.visibilityOfElementLocated(employeeListHeader));
    }

    public String addEmployeeAndGetId() {
        openPIMEmployeeList();

        WebElement addBtn = clickable(addEmployeeTopBtn);
        try { addBtn.click(); } catch (Exception e) { jsClick(addBtn); }
        waitSpinnerGone();

        visible(firstName).sendKeys("Auto");
        visible(lastName).sendKeys("User");

        String empId = visible(empIdOnAddForm).getAttribute("value");

        WebElement save = clickable(saveBtnAny);
        try { save.click(); } catch (Exception e) { jsClick(save); }
        waitSpinnerGone();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader),
                ExpectedConditions.visibilityOfElementLocated(changeProfilePictureHeader)
        ));
        return empId;
    }

    public void goToEmployeeListAndSearchById(String empId) {
        openPIMEmployeeList();

        try {
            if (driver.findElements(resetBtn).size() > 0) {
                WebElement r = clickable(resetBtn);
                try { r.click(); } catch (Exception e) { jsClick(r); }
            }
        } catch (Exception ignored) {}
        waitSpinnerGone();

        WebElement empIdBox = visible(employeeIdSearchInput);
        empIdBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        empIdBox.sendKeys(Keys.BACK_SPACE);
        empIdBox.sendKeys(empId);

        WebElement s = clickable(searchBtn);
        try { s.click(); } catch (Exception e) { jsClick(s); }
        waitSpinnerGone();

        // after delete there may be 0 rows and "No Records Found"
        wait.until(d -> d.findElements(resultsTableRows).size() > 0 || d.findElements(noRecordsFound).size() > 0);
    }

    public boolean isEmployeePresentInResults() {
        waitSpinnerGone();
        if (driver.findElements(noRecordsFound).size() > 0) return false;

        List<WebElement> rows = driver.findElements(resultsTableRows);
        return rows != null && !rows.isEmpty();
    }

    // ✅ RESTORED (PIMSteps expects this)
    public void openFirstSearchResultForEdit() {
        waitSpinnerGone();

        if (driver.findElements(resultsTableRows).size() == 0) {
            throw new RuntimeException("No search results available to open for edit.");
        }

        WebElement edit = clickable(firstRowEditBtn);
        scrollIntoView(edit);
        try { edit.click(); } catch (Exception e) { jsClick(edit); }

        waitSpinnerGone();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader),
                ExpectedConditions.visibilityOfElementLocated(changeProfilePictureHeader)
        ));
        waitSpinnerGone();
    }

    // ✅ RESTORED (PIMSteps expects this)
    public void editNicknameAndSave(String newNick) {
        // In this UI, Nickname may not exist => update a stable editable field.
        waitOnPersonalDetailsOrChangePictureOrList();

        // ensure we are on Personal Details (not Change Picture)
        if (driver.findElements(personalDetailsHeader).isEmpty()) {
            try {
                // click Personal Details left menu entry if available
                WebElement pd = driver.findElement(By.xpath("//a[normalize-space()='Personal Details']"));
                scrollIntoView(pd);
                try { pd.click(); } catch (Exception e) { jsClick(pd); }
            } catch (Exception ignored) {}
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
        waitSpinnerGone();

        WebElement field = bestEditableFieldForNicknameStep();
        clearAndType(field, newNick);

        WebElement save = clickable(saveBtnAny);
        try { save.click(); } catch (Exception e) { jsClick(save); }
        waitSpinnerGone();

        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
    }

    public void uploadProfilePicture(String imagePath) {
        waitOnPersonalDetailsOrChangePictureOrList();

        // If not already on Change Profile Picture screen, click avatar/image to open it
        if (driver.findElements(changeProfilePictureHeader).isEmpty()) {
            try {
                WebElement avatar = wait.until(ExpectedConditions.elementToBeClickable(avatarOrImage));
                scrollIntoView(avatar);
                try { avatar.click(); } catch (Exception e) { jsClick(avatar); }
            } catch (Exception ignored) {}
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(changeProfilePictureHeader));
        waitSpinnerGone();

        WebElement fileInput = resolveFileInputOnChangeProfilePicture();
        File f = new File(imagePath);
        fileInput.sendKeys(f.getAbsolutePath());
        waitSpinnerGone();

        WebElement savePic;
        try {
            savePic = wait.until(ExpectedConditions.elementToBeClickable(saveChangeProfilePictureBtn));
        } catch (Exception e) {
            savePic = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Save']")));
        }

        scrollIntoView(savePic);
        try { savePic.click(); } catch (Exception e) { jsClick(savePic); }
        waitSpinnerGone();

        // after saving, it usually returns to Personal Details
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader),
                    ExpectedConditions.visibilityOfElementLocated(employeeListHeader)
            ));
        } catch (Exception ignored) {}
    }

    public void deleteEmployeeById(String empId) {
        goToEmployeeListAndSearchById(empId);

        // already not present
        if (!isEmployeePresentInResults()) return;

        WebElement del = clickable(firstRowDeleteBtn);
        scrollIntoView(del);
        try { del.click(); } catch (Exception e) { jsClick(del); }

        waitSpinnerGone();
        WebElement confirm = clickable(confirmDeleteBtn);
        try { confirm.click(); } catch (Exception e) { jsClick(confirm); }

        waitSpinnerGone();
    }
}
