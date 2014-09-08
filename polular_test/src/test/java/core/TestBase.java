package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import utils.Log4Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestBase {
    protected static WebDriver webDriver;

    protected static WebDriverWait wait;

    @BeforeSuite
    public static void setUp() throws IOException

    {
        webDriver = new FirefoxDriver();
        wait = new WebDriverWait(webDriver, 20);
        webDriver.manage().window().maximize();
    }

    @BeforeMethod(alwaysRun = true)
    public void openBlankPage() {
        Log4Test.info("blank before test");
        webDriver.navigate().to("about:blank");
        webDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        webDriver.manage().timeouts().setScriptTimeout(2, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @AfterMethod(alwaysRun = true)
    public void navigateToBlankPage() {
        Log4Test.info("blank after test");
        webDriver.navigate().to("about:blank");
    }

    @AfterSuite
    public void tearDown() throws InterruptedException

    {
        Thread.sleep(5);
        webDriver.quit();

    }
}

