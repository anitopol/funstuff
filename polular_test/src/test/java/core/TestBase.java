package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: u
 * Date: 8/27/14
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestBase {
    protected static WebDriver webDriver;

    protected static WebDriverWait wait;

    @BeforeSuite
    public static void setUp() throws IOException

    {

        webDriver = new FirefoxDriver();
        wait = new WebDriverWait(webDriver, 30);

        webDriver.manage().window().maximize();

        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        webDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

        webDriver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

    }


    @AfterSuite
    public void tearDown() throws InterruptedException

    {
        Thread.sleep(5);
        webDriver.quit();

    }
}

