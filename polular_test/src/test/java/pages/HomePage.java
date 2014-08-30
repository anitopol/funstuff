package pages;

import core.TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.collections.Lists;
import ui_tests.TestData;
import utils.Log4Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: u
 * Date: 8/27/14
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomePage extends TestBase {

    protected By quizArrow = By.xpath("//b[@class='caret']");

    protected By czasownikSelection = By.xpath("//li/a[text()='Czasowniki: czas terazneiszy']");
    /*
        protected By slowaSelection = By.xpath("//li/a[text()='" + quizList().get(3) + "']");
    */
    protected By qiuzeSelectionMenu = By.className("ng-binding");

    public void open() {
        webDriver.get(TestData.HomeURL);
        Log4Test.info("Open WebUrl " + TestData.HomeURL);
    }

    public boolean isOpened() {
        return webDriver.getCurrentUrl().equals(TestData.HomeURL);
    }


    public void quizeListSelect() {
        Log4Test.info("List of quizes displayed");
        webDriver.findElement(quizArrow).click();
        webDriver.findElement(quizArrow).click();
        /*WebElement tableQuizes = webDriver.findElement(quizArrow);
        Actions builder = new Actions(webDriver);
        builder.doubleClick(tableQuizes);*/
    }

    public List<String> quizList() {
        List<WebElement> qiuzes = webDriver.findElements(qiuzeSelectionMenu);
        int size = qiuzes.size();
        List<String> qiuzList = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            qiuzList.add(qiuzes.get(i).getText());
        }

        return qiuzList;
    }

    public CzasownikiPage czasownikiSelection() {
        Log4Test.info("czasowniki page opened");
        webDriver.findElement(czasownikSelection).click();
        return new CzasownikiPage();
    }

    public SlowaPage slowaSelection() {
        Log4Test.info("slowa page opened");
        webDriver.findElement(By.xpath("//li/a[text()='" + quizList().get(2) + "']")).click();
        return new SlowaPage();
    }

    public LiczebnikiPage liczebnikiSelection() {
        Log4Test.info("liczebniki page opened");
        webDriver.findElement(By.xpath("//li/a[text()='" + quizList().get(1) + "']")).click();
        return new LiczebnikiPage();
    }

    public ZaimkiPage zaimkiSelection() {
        Log4Test.info("zaimki page opened");
        webDriver.findElement(By.xpath("//li/a[text()='" + quizList().get(3) + "']")).click();
        return new ZaimkiPage();
    }
}