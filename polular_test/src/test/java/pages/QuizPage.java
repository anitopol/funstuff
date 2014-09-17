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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class QuizPage extends Page {
    public final Response.PageInfo info;
    public List<Response.ColumnInfo> categoriesList;
    protected By categoriesDivLinks = By.xpath("//th/div[@class='ng-binding']");
    protected By rowsInQuiz = By.xpath("//tbody/tr");
    protected By activeCountRowsButton = By.xpath("//button[@class='btn btn-default ng-scope active']/span");
    protected By forQuizesNumberOrGoButton = By.xpath("//button[@class='btn btn-sm btn-primary ng-binding']");
    protected By someElementForQuiz = By.xpath("//tr[2]/td[3]");
    protected By selectedRowsInQuiz = By.xpath("//tr[@class='ng-scope info']");
    protected By randomQuizAddingButton = By.xpath("//button[@ng-click=\"addRandom()\"]");
    protected By deselectingButton = By.xpath("//button[@ng-click=\"deselect()\"]");
    protected By numberDisplayedButton = By.xpath("//button/span[text() = '10']");
    protected By nextButton = By.xpath("//a[@ng-switch-when='next']");
    protected By russianTranslation = By.xpath("//form//div[@class='ng-binding']/i");
    protected By divWithPolishWordsAndNumberOfWordsInQuiz = By.xpath("//form//div[@class='ng-binding']");
    protected By cellsWithRussianTransForSelectedRows = By.xpath("//tr[@class='ng-scope info']/td[@class='schemaTable_right schemaTable_id']");
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


    public void verifyNumberOfSelectedRowsDisplayed() {
        Log4Test.info("Verify number of rows selected is equal to active number on quiz button");
        webDriver.findElement(someElementForQuiz).click();
        List<WebElement> rowsOfSelectedWords = webDriver.findElements(selectedRowsInQuiz);
        Assert.assertEquals(String.valueOf(rowsOfSelectedWords.size()), webDriver.findElement(forQuizesNumberOrGoButton).getText().replace("Quiz for ", ""));
    }

    public void verifyDeselectionOfRows() {
        Log4Test.info("Verify rows deselected by none button");
        webDriver.findElement(deselectingButton).click();
        List<WebElement> rowsOfSelectedWords = webDriver.findElements(selectedRowsInQuiz);
        Assert.assertEquals(String.valueOf(rowsOfSelectedWords.size()), webDriver.findElement(forQuizesNumberOrGoButton).getText().replace("Quiz for ", ""));
    }

    public void randomButtonUsage() {
        Log4Test.info("Verify rows correctly selected by random button");
        webDriver.findElement(numberDisplayedButton).click();
        webDriver.findElement(randomQuizAddingButton).click();
        int size = webDriver.findElements(selectedRowsInQuiz).size();
        while (
                !webDriver.findElement(nextButton).findElement(By.xpath("..")).getAttribute("class").contains("disabled")
                ) {
            webDriver.findElement(nextButton).click();
            webDriver.manage().timeouts().implicitlyWait(250, TimeUnit.MILLISECONDS);

            size += webDriver.findElements(selectedRowsInQuiz).size();
        }
        Assert.assertEquals(String.valueOf(size), webDriver.findElement(forQuizesNumberOrGoButton).getText().replace("Quiz for ", ""));
    }

    public void quizStartOpportunity() {
        Log4Test.info("Verify quiz could not be started before some words adding");
        webDriver.findElement(someElementForQuiz).click();
        webDriver.findElement(deselectingButton).click();
        Assert.assertTrue(webDriver.findElement(forQuizesNumberOrGoButton).getAttribute("disabled") != null);
        webDriver.findElement(someElementForQuiz).click();
        Assert.assertTrue(webDriver.findElement(forQuizesNumberOrGoButton).getAttribute("disabled") == null);
    }

    public void forQuizPreparations() {
        Log4Test.info("elements for quiz setup");
        if (webDriver.findElement(deselectingButton).getAttribute("disabled") != null) {
            webDriver.findElement(deselectingButton).click();
        }
        webDriver.findElement(randomQuizAddingButton).click();
        while (

                !webDriver.findElement(nextButton).findElement(By.xpath("..")).getAttribute("class").contains("disabled")
                ) {
            List<WebElement> selectedRussianTranslations = webDriver.findElements(cellsWithRussianTransForSelectedRows);
            webDriver.findElement(nextButton).click();
            webDriver.manage().timeouts().implicitlyWait(250, TimeUnit.MILLISECONDS);
            selectedRussianTranslations.addAll(webDriver.findElements(cellsWithRussianTransForSelectedRows));
        }
        webDriver.findElement(forQuizesNumberOrGoButton).click();
        String divTotal = webDriver.findElement(divWithPolishWordsAndNumberOfWordsInQuiz).getText();
        String russianWord = webDriver.findElement(russianTranslation).getText();
        String polishCategoryName = divTotal.substring(divTotal.indexOf(':') + 1).replace(russianWord, "");
        String quantityOfQuizItems = divTotal.substring(divTotal.indexOf("of ") + 3, divTotal.indexOf(":") - 1);
        String itemCurrentNumber = divTotal.substring(0, 1);
    }


}