package sesame;

import org.junit.Assert;
import org.apache.commons.configuration.CompositeConfiguration;
import org.junit.*;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sesame.tools.DriverFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Basic API:
 * http://docs.seleniumhq.org/docs/03_webdriver.jsp
 * <p/>
 * Possible extensions:
 * https://github.com/FluentLenium/FluentLenium
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SmokeTestSuite {
    private static final Logger log = LoggerFactory.getLogger(SmokeTestSuite.class);

    protected static CompositeConfiguration conf;
    protected static String projectUrl;
    protected static WebDriver driver;

    @BeforeClass
    public static void setUp() throws Exception {
        conf = DriverFactory.getConf();
        projectUrl =
                conf.getString("project.url")
                        .replaceAll("\\$\\{user\\.dir\\}", System.getProperty("user.dir"));

        log.info("projectUrl is '{}'", projectUrl);

        driver = DriverFactory.getOrCreate();
        driver.get("about:blank");  //  or some kind of log-out page
    }

    @AfterClass
    public static void after() {
        DriverFactory.dispose(
                "about:blank"   //  or some kind of log-out page
        );
    }

    @Test
    @Ignore
    public void test00_minimal() throws Exception {
        driver.get(projectUrl);

        WebElement pass = driver.findElement(By.id("password"));
        DriverFactory.waitVisible(pass);

        Select what = new Select(driver.findElement(By.id("what")));
        WebElement custom = driver.findElement(By.id("custom"));
        WebElement key = driver.findElement(By.id("key"));

        pass.sendKeys("password");
        what.selectByVisibleText("t/mus13");
        Assert.assertEquals(
                "uberSalt::password::t/mus13",
                key.getText()
        );

        Thread.sleep(3 * 1000);

        what.selectByVisibleText("Custom...");
        custom.sendKeys("custom resource");
        Assert.assertEquals(
                "uberSalt::password::custom resource",
                key.getText()
        );

        Thread.sleep(3 * 1000);


    }

    @Test
    @Ignore
    public void test01_minimal() throws Exception {
        driver.get(projectUrl);
        WebElement pass = driver.findElement(By.id("password"));
        DriverFactory.waitVisible(pass);
        pass.click();

        Thread.sleep(1 * 1000);
        WebElement what = driver.findElement(By.id("what"));
        DriverFactory.waitVisible(what);
        what.click();

        Thread.sleep(1 * 1000);
        WebElement custom = driver.findElement(By.id("custom"));
        DriverFactory.waitVisible(custom);
        custom.click();

        Thread.sleep(1 * 1000);
    }

    @Test
    @Ignore
    public void test02_goog() throws Exception {
        driver.get("http://google.com");
        WebElement query = driver.findElement(By.xpath("//input[@name='q']"));
        DriverFactory.waitVisible(query);
        query.sendKeys("wheee ololo");
        Thread.sleep(1 * 1000);
        WebElement btnG = driver.findElement(By.xpath("//button[@name='btnG']"));
        btnG.click();
        Thread.sleep(1 * 1000);
    }

    @Test
    @Ignore
    public void test03_wiki() throws Exception {
        driver.get("http://en.wikipedia.org/wiki/List_of_FIPS_region_codes_%28S%E2%80%93U%29#UK:_United_Kingdom");
        Thread.sleep(1 * 1000);
        List<WebElement> allElements = driver.findElements(By.xpath("//html/body/div[3]/div[3]/div[4]/ul[29]/li/a"));

        int count = allElements.size();

        String[] elemhref = new String[count];
        int i = 0;
        for (WebElement element : allElements) {
            elemhref[i] = element.getAttribute("href");
            i++;
        }
        for (int j = 0; j < elemhref.length; j++) {
            String href = elemhref[j];
            driver.get(href);
            Thread.sleep(5 * 1000);
            List<WebElement> alllat = driver.findElements(By.xpath("//span[@class='geo-dms']/span[@class='latitude']"));
            List<WebElement> allong = driver.findElements(By.xpath("//span[@class='geo-dms']/span[@class='longitude']"));
            if ((alllat.size() > 0) && (allong.size() > 0) && alllat.get(0).isDisplayed() && allong.get(0).isDisplayed()) {
                System.out.println("geo-dms:" + alllat.get(0).getText() + " " + allong.get(0).getText());
            } else {
                List<WebElement> allal = driver.findElements(By.xpath("//span[@class='geo-dec']"));
                if (allal.size() > 0) {
                    System.out.println("geo-dec:" + allal.get(0).getText());
                }
            }
        }
    }

    @Test
    @Ignore
    public void testUntitled() throws Exception {
        driver.get("file:///home/u/s/w/sesame/proj/btsync.html");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("qw");
        driver.findElement(By.id("custom")).clear();
        driver.findElement(By.id("custom")).sendKeys("qw");
        assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*uberSalt::qw::qw[\\s\\S]*$"));
    }

    @Test

    public void testProjector() throws Exception {
        driver.get("http://simplydo.com/projector/");

        ProjPage projPage = new ProjPage(driver);
        projPage.addRegularIncome("bank robberies", -1000, "daily");
        Thread.sleep(500);

        projPage.addRegularIncome("bank robberies", -1000, "daily");
        Thread.sleep(500);

        projPage.addRegularIncome("bank robberies", 1000, "daily");
        Thread.sleep(500);

        projPage.addRegularExpence("rent", -4000, "Monthly");
        Thread.sleep(500);
        
        projPage.addRegularExpence("food", 100, "Daily");
        Thread.sleep(500);

         projPage.addNonRecuringIncome("bonus",10000,"December 2014");
        Thread.sleep(500);
        projPage.findBalanceByIndex(5);
    }

    @Test
    public void testMonthByNumber() throws Exception {
        driver.get("http://simplydo.com/projector/");
        ProjPage projPage = new ProjPage(driver);
        assertEquals("May 2014", projPage.findMonthByNumber(2));
    }

    @Test
    public void testBalanceByIndex() throws Exception {
        int month_index = 3;
        int month_income = 1000;
        driver.get("http://simplydo.com/projector/");
        ProjPage projPage = new ProjPage(driver);
        projPage.addRegularIncome("bank robberies", month_income, "monthly");
        Thread.sleep(2000);
        assertEquals(String.valueOf(month_income * month_index), projPage.findBalanceByIndex(month_index));
    }

    @Test

    public void testBalanceByMonth() throws Exception {
        driver.get("http://simplydo.com/projector/");
        ProjPage projPage = new ProjPage(driver);
        projPage.addRegularIncome("bank robberies", 1000, "monthly");
        assertEquals("1000", projPage.findBalanceByMonth("April 2014"));
    }
}
