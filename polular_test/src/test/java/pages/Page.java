package pages;

import fun.Fun;
import fun.Fun1;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;

public abstract class Page {
    protected final WebDriver webDriver;

    public Page(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public abstract Page init();

    public List<WebElement> find(By... byArr) {
        return Fun.map(Arrays.asList(byArr), new Fun1<By, WebElement>() {
            @Override
            public WebElement apply(By by) {
                try {
                    return webDriver.findElement(by);
                } catch (NoSuchElementException e) {
                    Assert.fail("element not found by " + by, e);
                    throw new IllegalStateException("not reachable");
                }
            }
        });
    }
}
