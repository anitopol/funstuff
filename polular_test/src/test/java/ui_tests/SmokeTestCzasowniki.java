package ui_tests;

import core.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.testng.annotations.Test;
import pages.CzasownikiPage;
import pages.HomePage;

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
        homepage.czasownikiSelection();
    }

    @Test(dependsOnMethods = {"homePageActions"})
    public void czasownikiPageActions() {
        czasowniki.czasownikiPageOpened();
        czasowniki.VerifyNumberOfRowsDisplayed();
    }
}
