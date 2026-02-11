package base;

import org.openqa.selenium.WebDriver;

public class BaseClass {

    // SINGLE SOURCE OF TRUTH
    public WebDriver driver;

    public BaseClass() {
        // Always reuse DriverSetup driver
        this.driver = DriverSetup.getDriver();
    }

    public void openLoginPage() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }
}
