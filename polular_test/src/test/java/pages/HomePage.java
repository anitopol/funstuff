package pages;

import core.HttpTransport;
import core.Response;
import fun.Fun;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import ui_tests.TestData;
import utils.Log4Test;

import java.util.List;

public class HomePage extends Page {
    public List<Response.PageInfo> quizPages;
    protected By quizArrow = By.xpath("//b[@class='caret']");

    public HomePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public HomePage init() {
        quizPages = Response.index(HttpTransport.retrieve(TestData.IndexMenuUrl));

        Log4Test.info("Open WebUrl " + TestData.HomeURL);
        webDriver.get(TestData.HomeURL);
        Assert.assertTrue(webDriver.getCurrentUrl().equals(TestData.HomeURL));
        find(quizArrow);
        return this;
    }

    public List<Response.PageInfo> quizes() {
        return quizPages;
    }

    public QuizPage quizPageSelection(String q) {
        Log4Test.info("List of quizes displayed");
        webDriver.findElement(quizArrow).click();
        webDriver.findElement(quizArrow).click();
        Response.PageInfo page = Fun.find(quizPages, Response.PageInfo.nameEq(q));
        {
            Log4Test.info(page.id + " page opening");
            webDriver.findElement(By.xpath("//li/a[text()='" + page.title + "']")).click();
            return new QuizPage(webDriver, page).init();
        }
    }

}