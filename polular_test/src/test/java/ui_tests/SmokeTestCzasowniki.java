package ui_tests;

import core.*;
import org.junit.Assert;
import org.testng.annotations.Test;
import pages.CzasownikiPage;
import pages.HomePage;

/**
 * Created with IntelliJ IDEA.
 * User: u
 * Date: 8/27/14
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmokeTestCzasowniki extends TestBase {
    HomePage homepage = new HomePage();
    CzasownikiPage czasowniki = new CzasownikiPage();


    @Test
    public void homePageActions() {
        Assert.assertNotNull(
                SmokeTestCzasowniki.class.getResource("/log4j.properties")
        );


        homepage.open();
        homepage.isOpened();
        homepage.quizeListSelect();
        homepage.quizList();
        homepage.czasownikiSelection();
    }


    @Test(dependsOnMethods = {"homePageActions"})
    public void czasownikiPageActions() {
        czasowniki.czasownikiPageOpened();
        czasowniki.VerifyNumberOfRowsDisplayed();
    }
}
