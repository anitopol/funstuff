package pages;

import core.HttpTransport;
import core.Response;
import fun.Fun;
import fun.Fun1;
import fun.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import ui_tests.TestData;
import utils.Log4Test;

import java.util.List;


public class QuizPage extends Page {
    public final Response.PageInfo info;
    protected By categoriesDivLinks = By.xpath("//th/div[@class='ng-binding']");
    protected By rowsInQuiz = By.xpath("//tbody/tr");
    protected By activeCountRowsButton = By.xpath("//button[@class='btn btn-default ng-scope active']/span");
    protected By forQuizesNumberbutton = By.xpath("//button[@class='btn btn-sm btn-primary ng-binding']");
    private List<Response.ColumnInfo> categoriesList;
    private List<String> categoriesTitles;

    public QuizPage(WebDriver webDriver, Response.PageInfo info) {
        super(webDriver);
        this.info = info;
    }

    @Override
    public QuizPage init() {
        Log4Test.info("Getting data from url: " + TestData.schemaUrl(info));
        categoriesList = Response.indexSchema(HttpTransport.retrieve(TestData.schemaUrl(info)));
        categoriesTitles = Fun.map(categoriesList, new Fun1<Response.ColumnInfo, String>() {
            @Override
            public String apply(Response.ColumnInfo columnInfo) {
                return columnInfo.title;
            }
        });
        System.out.println(categoriesTitles);
        Log4Test.info(info.id + " page opened");
        Assert.assertTrue(webDriver.getCurrentUrl().toString().contains(info.id));

        return this;
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
        categoriesNames = Fun.filterNot(categoriesNames, new Predicate<String>() {
            @Override
            public boolean apply(String value) {
                return value.contains("Stats");
            }
        });
        System.out.println(categoriesNames);
        Assert.assertEquals(categoriesNames, categoriesTitles);
    }

    public void verifyNumberOfRowsDisplayed() {
        Log4Test.info("Verify number of rows displayed is equal to active number button");
        List<WebElement> rowsOfPolisVerbs = webDriver.findElements(rowsInQuiz);
        Assert.assertEquals(String.valueOf(rowsOfPolisVerbs.size()), webDriver.findElement(activeCountRowsButton).getText());
    }
}
