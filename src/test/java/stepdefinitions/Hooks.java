package stepdefinitions;

import base.BaseClass;
import base.DriverSetup;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hooks {

    public static BaseClass base;
    public static WebDriver driver;

    @Before
    public void setUp() {
        driver = DriverSetup.getDriver();
        base = new BaseClass();
    }

    @After
    public void tearDown() {
        try {
            DriverSetup.quitDriver();
        } catch (Exception ignored) {}

        driver = null;
        base = null;
    }
}
