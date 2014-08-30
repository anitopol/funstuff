package pages;

import core.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.collections.Lists;
import utils.Log4Test;

import java.util.List;

/**
 * Created by a on 28.08.14.
 */
public class CzasownikiPage extends TestBase {
    protected By polishczasowniki = By.xpath("//td[@class='schemaTable_left schemaTable_legend']");
    protected By activeCountRowsButton = By.xpath("//button[@class='btn btn-default ng-scope active']/span");

    public void czasownikiPageOpened() {
        Log4Test.info("Czasowniki page opened");
        Assert.assertTrue(webDriver.getCurrentUrl().toString().contains("czasowniki_teraz_odmiany"));
    }

    public void VerifyNumberOfRowsDisplayed() {
        Log4Test.info("Verify number of rows displayed is equal to active number button");
        List<WebElement> rowsOfPolisVerbs = webDriver.findElements(polishczasowniki);
        Assert.assertEquals(String.valueOf(rowsOfPolisVerbs.size()), webDriver.findElement(activeCountRowsButton).getText());
    }
}
