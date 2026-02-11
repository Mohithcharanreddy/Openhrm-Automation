package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdminPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Common
    private final By loadingSpinner = By.cssSelector(".oxd-loading-spinner");

    // Left menu (handle collapsed menu / different DOM)
    private final By sidePanel = By.cssSelector("aside.oxd-sidepanel");
    private final By menuToggleBtn = By.cssSelector("button.oxd-main-menu-button");

    // Admin menu - keep multiple robust locators
    private final By adminMenuA = By.xpath("//aside[contains(@class,'oxd-sidepanel')]//span[normalize-space()='Admin']/ancestor::a[1]");
    private final By adminMenuB = By.xpath("//a[contains(@href,'/admin') and .//span[normalize-space()='Admin']]");
    private final By adminMenuC = By.xpath("//a[.//span[normalize-space()='Admin']]");

    // Page identifiers
    private final By usersHeader = By.xpath("//h6[contains(normalize-space(),'System Users')]");
    private final By adminHeader = By.xpath("//h6[contains(normalize-space(),'Admin')]");
    private final By addUserHeader = By.xpath("//h6[normalize-space()='Add User']");

    // ===== System Users (Search) =====
    private final By usernameFilterInput =
            By.xpath("(//label[normalize-space()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//input)[1]");

    private final By searchButton =
            By.xpath("//button[@type='submit' and normalize-space()='Search']");

    private final By resetButton =
            By.xpath("//button[@type='button' and normalize-space()='Reset']");

    // Search results container
    private final By tableBody = By.cssSelector(".oxd-table-body");
    private final By noRecords = By.xpath("//*[contains(normalize-space(),'No Records Found')]");

    // ===== Add User =====
    private final By addButton = By.xpath("//button[normalize-space()='Add']");

    private final By userRoleDropdown =
            By.xpath("//*[normalize-space()='Add User']/ancestor::div[contains(@class,'orangehrm-card-container')][1]" +
                    "//label[contains(normalize-space(),'User Role')]/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");

    private final By statusDropdown =
            By.xpath("//*[normalize-space()='Add User']/ancestor::div[contains(@class,'orangehrm-card-container')][1]" +
                    "//label[contains(normalize-space(),'Status')]/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");

    private final By employeeNameInput =
            By.xpath("//*[normalize-space()='Add User']/ancestor::div[contains(@class,'orangehrm-card-container')][1]" +
                    "//label[contains(normalize-space(),'Employee Name')]/ancestor::div[contains(@class,'oxd-input-group')]//input");

    private final By addUserUsernameInput =
            By.xpath("//*[normalize-space()='Add User']/ancestor::div[contains(@class,'orangehrm-card-container')][1]" +
                    "//label[normalize-space()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//input");

    private final By passwordInput =
            By.xpath("//*[normalize-space()='Add User']/ancestor::div[contains(@class,'orangehrm-card-container')][1]" +
                    "//label[normalize-space()='Password']/ancestor::div[contains(@class,'oxd-input-group')]//input");

    private final By confirmPasswordInput =
            By.xpath("//*[normalize-space()='Add User']/ancestor::div[contains(@class,'orangehrm-card-container')][1]" +
                    "//label[contains(normalize-space(),'Confirm Password')]/ancestor::div[contains(@class,'oxd-input-group')]//input");

    // Save button (works on Add User and Edit User forms)
    private final By saveButton =
            By.xpath("//button[@type='submit' and normalize-space()='Save']");

    // Delete confirm
    private final By confirmDeleteAny =
            By.xpath("//button[contains(normalize-space(),'Yes') and contains(normalize-space(),'Delete')] | " +
                    "//button[contains(@class,'oxd-button--label-danger')]");

    // More robust toast selector (OrangeHRM versions differ)
    private final By toastMessage =
            By.cssSelector(".oxd-toast-container, .oxd-toast, .oxd-toast-content, .oxd-toast--success, .oxd-toast--info");

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    // ================== HELPERS ==================
    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void waitSpinnerGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (Exception ignored) {}
    }

    private void waitAdminPageLoaded() {
        waitSpinnerGone();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(usersHeader),
                ExpectedConditions.visibilityOfElementLocated(adminHeader),
                ExpectedConditions.visibilityOfElementLocated(addUserHeader)
        ));
        waitSpinnerGone();
    }

    private void ensureSideMenuReady() {
        waitSpinnerGone();
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(sidePanel));
        } catch (Exception ignored) {}

        // If menu items are not visible (collapsed), toggle once.
        try {
            if (driver.findElements(adminMenuA).isEmpty() && driver.findElements(adminMenuB).isEmpty()) {
                List<WebElement> toggle = driver.findElements(menuToggleBtn);
                if (!toggle.isEmpty()) {
                    try { toggle.get(0).click(); } catch (Exception e) { jsClick(toggle.get(0)); }
                    waitSpinnerGone();
                }
            }
        } catch (Exception ignored) {}
    }

    private WebElement findAdminMenuElement() {
        ensureSideMenuReady();

        By[] candidates = new By[]{adminMenuA, adminMenuB, adminMenuC};
        for (By by : candidates) {
            try {
                List<WebElement> els = driver.findElements(by);
                if (els != null && !els.isEmpty()) {
                    WebElement el = els.get(0);
                    wait.until(ExpectedConditions.visibilityOf(el));
                    return el;
                }
            } catch (Exception ignored) {}
        }

        // One more try after toggling menu
        try {
            List<WebElement> toggle = driver.findElements(menuToggleBtn);
            if (!toggle.isEmpty()) {
                try { toggle.get(0).click(); } catch (Exception e) { jsClick(toggle.get(0)); }
                waitSpinnerGone();
            }
        } catch (Exception ignored) {}

        for (By by : candidates) {
            try {
                List<WebElement> els = driver.findElements(by);
                if (els != null && !els.isEmpty()) {
                    WebElement el = els.get(0);
                    wait.until(ExpectedConditions.visibilityOf(el));
                    return el;
                }
            } catch (Exception ignored) {}
        }

        throw new TimeoutException("Admin menu not found in side panel (UI collapsed/changed).");
    }

    private void selectFromDropdown(By dropdown, String visibleText) {
        waitSpinnerGone();

        WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
        try { dd.click(); } catch (Exception e) { jsClick(dd); }

        By option = By.xpath("//div[@role='listbox']//span[normalize-space()='" + visibleText + "']");
        wait.until(ExpectedConditions.elementToBeClickable(option));
        driver.findElement(option).click();

        waitSpinnerGone();
    }

    private boolean isNoRecordsFoundVisibleInAutocomplete() {
        By noRecordsFound = By.xpath("//div[@role='listbox']//span[normalize-space()='No Records Found']");
        try {
            return driver.findElements(noRecordsFound).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean clickFirstEmployeeSuggestion() {
        try {
            By listbox = By.xpath("//div[@role='listbox']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(listbox));

            if (isNoRecordsFoundVisibleInAutocomplete()) return false;

            By firstSuggestion = By.xpath("(//div[@role='listbox']//span)[1]");
            WebElement sug = wait.until(ExpectedConditions.elementToBeClickable(firstSuggestion));
            try { sug.click(); } catch (Exception e) { jsClick(sug); }

            waitSpinnerGone();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void clearAndType(WebElement el, String text) {
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.DELETE);
        el.sendKeys(text);
    }

    private void clickResetIfPresent() {
        try {
            waitAdminPageLoaded();
            if (driver.findElements(resetButton).size() > 0) {
                WebElement r = wait.until(ExpectedConditions.elementToBeClickable(resetButton));
                try { r.click(); } catch (Exception e) { jsClick(r); }
                waitSpinnerGone();
            }
        } catch (Exception ignored) {}
    }

    private WebElement getFirstResultRowOrNull() {
        waitSpinnerGone();

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(tableBody),
                    ExpectedConditions.visibilityOfElementLocated(noRecords)
            ));
        } catch (Exception ignored) {}

        if (driver.findElements(noRecords).size() > 0) return null;

        By firstRow = By.xpath("//div[contains(@class,'oxd-table-body')]//div[contains(@class,'oxd-table-row')][1] | " +
                "//div[contains(@class,'oxd-table-body')]//div[contains(@class,'oxd-table-card')][1]");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));
        } catch (Exception e) {
            return null;
        }
    }

    private void clickRowActionByIcon(WebElement row, String iconClassContains, int fallbackIndex) {
        try {
            By iconBtn = By.xpath(".//button[.//i[contains(@class,'" + iconClassContains + "')]] | .//i[contains(@class,'" + iconClassContains + "')]/ancestor::button");
            List<WebElement> iconButtons = row.findElements(iconBtn);
            if (iconButtons != null && !iconButtons.isEmpty()) {
                WebElement btn = iconButtons.get(0);
                wait.until(ExpectedConditions.elementToBeClickable(btn));
                try { btn.click(); } catch (Exception e) { jsClick(btn); }
                waitSpinnerGone();
                return;
            }
        } catch (Exception ignored) {}

        By lastCellButtons = By.xpath(".//div[contains(@class,'oxd-table-cell')][last()]//button | .//button");
        List<WebElement> buttons = row.findElements(lastCellButtons);
        if (buttons == null || buttons.isEmpty() || buttons.size() <= fallbackIndex) {
            throw new RuntimeException("Could not find expected row action button. Table UI changed.");
        }

        WebElement btn = buttons.get(fallbackIndex);
        wait.until(ExpectedConditions.elementToBeClickable(btn));
        try { btn.click(); } catch (Exception e) { jsClick(btn); }
        waitSpinnerGone();
    }

    // ================== ACTIONS ==================
    public void clickAdminMenu() {
        waitSpinnerGone();

        WebElement admin = findAdminMenuElement();
        wait.until(ExpectedConditions.elementToBeClickable(admin));
        try { admin.click(); } catch (Exception e) { jsClick(admin); }

        waitAdminPageLoaded();
    }

    public boolean isAdminPageDisplayed() {
        try {
            waitAdminPageLoaded();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void searchUser(String username) {
        waitAdminPageLoaded();
        clickResetIfPresent();

        WebElement userInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameFilterInput));
        userInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        userInput.sendKeys(Keys.DELETE);
        userInput.sendKeys(username);

        WebElement s = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        try { s.click(); } catch (Exception e) { jsClick(s); }

        waitSpinnerGone();
    }

    public boolean hasAnySearchResultRow() {
        return getFirstResultRowOrNull() != null;
    }

    public void clickAdd() {
        waitAdminPageLoaded();
        WebElement a = wait.until(ExpectedConditions.elementToBeClickable(addButton));
        try { a.click(); } catch (Exception e) { jsClick(a); }
        wait.until(ExpectedConditions.visibilityOfElementLocated(addUserHeader));
        waitSpinnerGone();
    }

    public void selectUserRole(String role) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addUserHeader));
        selectFromDropdown(userRoleDropdown, role);
    }

    public void selectStatus(String status) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addUserHeader));
        selectFromDropdown(statusDropdown, status);
    }

    // Select employee dynamically from autocomplete (demo DB changes)
    public void enterEmployeeName(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addUserHeader));
        waitSpinnerGone();

        WebElement emp = wait.until(ExpectedConditions.elementToBeClickable(employeeNameInput));
        emp.click();

        String firstTry = (name != null && name.trim().length() > 0) ? name.trim() : "a";
        String typed = firstTry.length() >= 4 ? firstTry.substring(0, 4) : firstTry;

        clearAndType(emp, typed);

        if (clickFirstEmployeeSuggestion()) return;

        String[] prefixes = new String[]{"a", "an", "b", "c", "d", "e", "jo", "ma", "mi", "ra", "sa"};
        for (String p : prefixes) {
            try {
                emp.click();
                clearAndType(emp, p);
                if (clickFirstEmployeeSuggestion()) return;
            } catch (Exception ignored) {}
        }

        throw new RuntimeException("Employee autocomplete returned 'No Records Found' for all fallbacks. Demo DB unstable.");
    }

    public void enterUsername(String username) {
        WebElement u = wait.until(ExpectedConditions.elementToBeClickable(addUserUsernameInput));
        u.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        u.sendKeys(Keys.DELETE);
        u.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement p = wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
        p.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        p.sendKeys(Keys.DELETE);
        p.sendKeys(password);

        WebElement c = wait.until(ExpectedConditions.elementToBeClickable(confirmPasswordInput));
        c.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        c.sendKeys(Keys.DELETE);
        c.sendKeys(password);
    }

    public void clickSave() {
        waitSpinnerGone();
        WebElement s = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        try { s.click(); } catch (Exception e) { jsClick(s); }
        waitSpinnerGone();
    }

    public void clickEdit() {
        WebElement row = getFirstResultRowOrNull();
        if (row == null) throw new RuntimeException("No search results available to click Edit.");
        clickRowActionByIcon(row, "bi-pencil-fill", 0);
    }

    public void clickDelete() {
        WebElement row = getFirstResultRowOrNull();
        if (row == null) throw new RuntimeException("No search results available to click Delete.");
        clickRowActionByIcon(row, "bi-trash", 1);
    }

    public void confirmDelete() {
        waitSpinnerGone();
        WebElement y = wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteAny));
        try { y.click(); } catch (Exception ex) { jsClick(y); }
        waitSpinnerGone();
    }

    public boolean isToastMessageDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(toastMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
