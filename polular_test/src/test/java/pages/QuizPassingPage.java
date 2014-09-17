package pages;

import core.HttpTransport;
import core.Response;
import fun.Fun;
import fun.Fun1;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import ui_tests.TestData;
import utils.Log4Test;

public class QuizPassingPage extends Page {
    public final Response.PageInfo info;

    public QuizPassingPage(WebDriver webDriver, Response.PageInfo info) {
        super(webDriver);
        this.info = info;
    }

    @Override
    public QuizPassingPage init() {
        Log4Test.info("Getting data for quiz from url: " + TestData.dataUrl(info));
        Log4Test.info(info.id + " page opened");
        Assert.assertTrue(webDriver.getCurrentUrl().toString().contains(info.id));
        return this;
    }
}
