package pages;

import core.HttpTransport;
import core.Response;
import core.TestBase;
import fun.Fun;
import fun.Fun1;
import fun.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import ui_tests.TestData;
import utils.Log4Test;
import java.util.List;


public class SlowaPage extends TestBase {
    protected By categoriesDivLinks = By.xpath("//th/div[@class='ng-binding']");
    private List<Response.ColumnInfo> categoriesList;
    private List<String> categoriesTitles;

    public void slowaSchemaDataGetting() {
        Log4Test.info("Getting data from url: " + TestData.SlowaSchemaUrl);
        categoriesList = Response.indexSchema(HttpTransport.retrieve(TestData.SlowaSchemaUrl));
        categoriesTitles = Fun.map(categoriesList, new Fun1<Response.ColumnInfo, String>() {
            @Override
            public String apply(Response.ColumnInfo columnInfo) {
                return columnInfo.title;
            }
        });
        System.out.println(categoriesTitles);
    }

    public void slowaPageOpened() {
        Log4Test.info("slowaPage opened");
        Assert.assertTrue(webDriver.getCurrentUrl().toString().contains("slowa"));
    }

    public void allCategoriesLinksPresent() {
        Log4Test.info("Verify all categories names are present");
        List<WebElement> categoriesLinksTextActual = webDriver.findElements(categoriesDivLinks);
        List<String> categoriesNames = Fun.map(categoriesLinksTextActual, new Fun1<WebElement, String>() {
            @Override
            public String apply(WebElement webElement) {
                return webElement.getText();
            }
        });
        categoriesNames = Fun.filterNot(categoriesNames , new Predicate<String>() {
            @Override
            public boolean apply(String value) {
                return value.contains("Stats");
            }
        });
        System.out.println(categoriesNames);
        Assert.assertEquals(categoriesNames, categoriesTitles);
    }
}
