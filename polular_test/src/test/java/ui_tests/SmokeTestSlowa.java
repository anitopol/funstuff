package ui_tests;

import core.TestBase;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SlowaPage;

public class SmokeTestSlowa extends TestBase {
    HomePage homepage = new HomePage();
    SlowaPage slowa = new SlowaPage();

    @Test
    public void HomepageActions() {
        homepage.open();
        homepage.isOpened();
        homepage.quizeListSelect();
        homepage.slowaSelection();
    }
    @Test(dependsOnMethods ={"HomepageActions" })
    public void slowaPageActions(){
        slowa.slowaSchemaDataGetting();
        slowa.slowaPageOpened();
        slowa.allCategoriesLinksPresent();
    }
}
