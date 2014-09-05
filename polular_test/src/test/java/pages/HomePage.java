package pages;

import core.HttpTransport;
import core.Response;
import core.TestBase;

import fun.Fun;
import fun.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.collections.Lists;
import ui_tests.TestData;
import utils.Log4Test;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends TestBase {

    protected By quizArrow = By.xpath("//b[@class='caret']");

    /*
        protected By qiuzeSelectionMenu = By.className("ng-binding");
    */
    private List<Response.PageInfo> quizPages;

    public void open() {
        webDriver.get(TestData.HomeURL);
        Log4Test.info("Open WebUrl " + TestData.HomeURL);

        quizPages = Response.index(HttpTransport.retrieve(TestData.IndexMenuUrl));
    }

    public boolean isOpened() {
        return webDriver.getCurrentUrl().equals(TestData.HomeURL);
    }


    public void quizeListSelect() {
        Log4Test.info("List of quizes displayed");
        webDriver.findElement(quizArrow).click();
        webDriver.findElement(quizArrow).click();
    }

/*
    public List<String> quizList() {
        List<WebElement> qiuzes = webDriver.findElements(qiuzeSelectionMenu);
        int size = qiuzes.size();
        List<String> qiuzList = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            qiuzList.add(qiuzes.get(i).getText());
        }

        return qiuzList;
    }
*/

    public CzasownikiPage czasownikiSelection() {
        Response.PageInfo page = Fun.find(quizPages, Response.PageInfo.nameEq("czasowniki"));
        {

            Log4Test.info("czasowniki page opening");

            webDriver.findElement(By.xpath("//li/a[text()='" + page.title + "']")).click();
            return new CzasownikiPage();
        }
    }

   /* public CzasownikiPage CzasownikiPageOpening() {
        Response.PageInfo page = Fun.find(quizPages, new Predicate<Response.PageInfo>() {
            @Override
            public boolean apply(Response.PageInfo value) {
                return value.id.toLowerCase().contains("czasowniki");
            }
        });

        Log4Test.info("czasowniki page opened");
        webDriver.findElement(By.xpath("//li/a[text()='" + page.title + "']")).click();
        return new CzasownikiPage();

    }*/

    public SlowaPage slowaSelection() {
        Response.PageInfo page = Fun.find(quizPages, Response.PageInfo.nameEq("slowa"));
        {
            Log4Test.info("slowa page opening");
            webDriver.findElement(By.xpath("//li/a[text()='" + page.title + "']")).click();
            return new SlowaPage();
        }
    }

    public LiczebnikiPage liczebnikiSelection() {
        Response.PageInfo page = Fun.find(quizPages, Response.PageInfo.nameEq("liczebniki"));

        Log4Test.info("liczebniki page opening");
        webDriver.findElement(By.xpath("//li/a[text()='" + page.title + "']")).click();
        return new LiczebnikiPage();
    }

    public ZaimkiPage zaimkiSelection() {
        Response.PageInfo page = Fun.find(quizPages, Response.PageInfo.nameEq("zaimki"));
        Log4Test.info("zaimki page opening");
        webDriver.findElement(By.xpath("//li/a[text()='" + page.title + "']")).click();
        return new ZaimkiPage();
    }
}