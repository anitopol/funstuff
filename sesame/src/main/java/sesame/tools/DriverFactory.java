package sesame.tools;


import junit.framework.AssertionFailedError;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class DriverFactory {
    protected final static Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }
    
    public static CompositeConfiguration getConf() {
        return ConfigFactory.getConfiguration("sesame");
    }

    public static WebDriver create() throws Exception {
        final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        final WebDriver driver = new FirefoxDriver(capabilities);
        final CompositeConfiguration conf = getConf();
        browserSize(driver, conf.getInt("win.w"), conf.getInt("win.h"));
        browserPosition(driver, conf.getInt("win.x"), conf.getInt("win.y"));
        return driver;
    }

    public static synchronized boolean dispose(final String cleanupNavigate) {
        if (driver != null) {
            try {
                driver.navigate().to(cleanupNavigate);
            } catch (Exception e) {
                //  https://code.google.com/p/chromedriver/issues/detail?id=161
                driver.switchTo().alert().accept();
            }
            driver.close();
            driver.quit();
            driver = null;
            return true;
        }

        return false;
    }

    public static synchronized WebDriver getOrCreate() throws Exception {
        if (driver != null) {
            return driver;
        }
        driver = create();
        return driver;
    }

    public static void browserPosition(WebDriver driver, int x, int y) {
        driver.manage().window().setPosition(new Point(x, y));
    }

    public static void browserSize(WebDriver driver, int horizontal, int vertical) {
        Dimension WindowSize = new Dimension(horizontal, vertical);
        driver.manage().window().setSize(WindowSize);
    }

    public static void waitUrl(String urlMatch) {
        waitUrl(getDriver(), urlMatch);
    }

    public static void waitUrl(WebDriver driver, String urlMatch) {
        log.info("waiting for navigation to {}", urlMatch);
        long timeout = System.currentTimeMillis() + 15000;
        while (!driver.getCurrentUrl().contains(urlMatch)){
            if (System.currentTimeMillis() > timeout) {
                throw new AssertionFailedError(
                        "This is not desired page " + driver.getCurrentUrl() + "; expecting " + urlMatch
                );
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static void waitVisible(WebElement element) {
        new WebDriverWait(DriverFactory.getDriver(), 15) //  SECONDS
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitClickable(By by) {
        new WebDriverWait(DriverFactory.getDriver(), 1000)
                .until(ExpectedConditions.elementToBeClickable(by));
    }

    public static void waitInvisible(WebElement element) {
        new WebDriverWait(DriverFactory.getDriver(), 15) //  SECONDS
                .until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
    }

    public static void waitInvisibleByLocator(By by) {
        new WebDriverWait(DriverFactory.getDriver(), 1000)
                .until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public static void waitForElementByLocation(int syncTime, By fndBy){
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), syncTime);
        wait.until(ExpectedConditions.visibilityOfElementLocated(fndBy));
    }

    public static String getCookie(String cookieName) {
        return getDriver().manage().getCookieNamed(cookieName).getValue();
    }

    public static void takeScreenshot(WebDriver driver, String path) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(path));
    }
}
