package ui_tests;

import core.Response;
import core.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TestData extends TestBase {
    public static final String HomeURL = "http://anitopol.github.io/polular/#/view1";
    public static final String IndexMenuUrl = "http://anitopol.github.io/polular/data/index.csv.txt";
    public static final String schemaUrl(Response.PageInfo page) {
        return "http://anitopol.github.io/polular/data/"+page.id+"--schema.csv.txt";
    }
}
