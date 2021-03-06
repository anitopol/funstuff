package ui_tests;

import core.Response;
import core.TestBase;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.QuizPage;

public class SmokeTest extends TestBase {

    @Test
    public void generalTest() {
        HomePage homepage = new HomePage(webDriver).init();
        for (Response.PageInfo pageInfo : homepage.quizes()) {
            QuizPage w = homepage.quizPageSelection(pageInfo.id);
            w.allCategoriesLinksPresent();
            w.verifyNumberOfRowsDisplayed();
            w.verifyNumberOfSelectedRowsDisplayed();
            w.verifyDeselectionOfRows();
            w.randomButtonUsage();
            w.quizStartOpportunity();
        }
    }

    @Test
    public void forRandomCzasownikiOnly() {
        HomePage homepage = new HomePage(webDriver).init();
        QuizPage w = homepage.quizPageSelection("czasowniki_teraz_odmiany");

        w.forQuizPreparations();
    }
}
