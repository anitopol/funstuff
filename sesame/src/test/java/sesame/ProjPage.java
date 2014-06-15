package sesame;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.NoSuchElementException;

public class ProjPage {
    private By currentBalance = By.xpath("//input[" +
            "@type='text' and " +
            "contains(@placeholder, 'Enter your current balance') and " +
            "contains(@ng-model, 'startBalance')" +
            "]");
    private By addIncome = By.xpath("//button[contains(@ng-click, 'addIncome')]");
    private By newIncomeWhatIsIt = By.xpath(
            "//li[contains(@ng-repeat, 'income')]" +
                    "//input[" +
                    "  @type='text' and " +
                    "  contains(@class, 'ng-pristine') and " +
                    "  contains(@placeholder, 'What is it')" +
                    "]"
    );
    private By newIncomeAmount = By.xpath(
            "//li[contains(@ng-repeat, 'income')]" +
                    "//input[" +
                    "  @type='text' and " +
                    "  contains(@class, 'ng-pristine') and " +
                    "  contains(@placeholder, 'Enter Amount')" +
                    "]"
    );
    private By newIncomeFrequency = By.xpath(
            "//li[contains(@ng-repeat, 'income')]" +
                    "//select[" +
                    "  contains(@class, 'ng-pristine') and " +
                    "  contains(@ng-model, 'income.frequency')" +
                    "]"
    );
    private By removeIncome = By.xpath("//a[contains(@ng-click,'removeIncome')]");

    private String balanceByName = ("//td[contains(@class,'ng-binding') and contains(text(),'month_value')]/../td[3]");
    private String monthByNumber = ("//tr[contains(@class, 'ng-scope') and " +
            "position()=" +
            "'month_number'"
            + "]/td[1]");
    private String incomePerMonth = ("//tr/td[contains(text(),'Income')]/../td[2]");
    private String incomePerYear = ("//tr/td[contains(text(),'Income')]/../td[3]");
    private String expencePerMonth = ("//tr/td[contains(text(),'Expenses')]/../td[2]");
    private String expencePerYear = ("//tr/td[contains(text(),'Expenses')]/../td[3]");
    private String nonRecurringPerYear = ("//tr/td[contains(text(),'Non-Recurring')]/../td[3]");
    private String netIncomePerMonth = ("//tr/td/*[contains(text(),'Net income')]/../../td[2]");
    private String netIncomePerYear = ("//tr/td/*[contains(text(),'Net income')]/../../td[3]");
    private By addExpence = By.xpath("//button[contains(@ng-click, 'addExpense')]");
    private By newExpenceName = By.xpath(
            "//li[contains(@ng-repeat, 'expense')]" +
                    "//input[" +
                    "  @type='text' and " +
                    "  contains(@class, 'ng-pristine') and " +
                    "  contains(@placeholder, 'e.g. Rent...')" +
                    "]"
    );

    private By newExpenceAmount = By.xpath(
            "//li[contains(@ng-repeat, 'expense')]" +
                    "//input[" +
                    "  @type='text' and " +
                    "  contains(@class, 'ng-pristine') and " +
                    "  contains(@placeholder, 'Enter Amount')" +
                    "]"
    );
    private By newExpenceFrequency = By.xpath(
            "//li[contains(@ng-repeat, 'expense')]" +
                    "//select[" +
                    "  contains(@class, 'ng-pristine') and " +
                    "  contains(@ng-model, 'expense.frequency')" +
                    "]"
    );
    private By removeExpence = By.xpath("//a[contains(@ng-click,'removeExpense')]");
    private By addNonRecuringIncome = By.xpath("//button[contains(@ng-click, 'addTransaction')]");
    private By newNonRecurringIncomeName = By.xpath(
            "//li[contains(@ng-repeat, 'transaction in nonRecurring')]" +
                    "//input[" +
                    "  @type='text' and " +
                    "  contains(@class, 'ng-pristine') and " +
                    "  contains(@placeholder, 'e.g. Bonus...')" +
                    "]"
    );
    private By newNonRecurringIncomeAmount = By.xpath(
            "//li[contains(@ng-repeat, 'transaction')]" +
                    "//input[" +
                    "  @type='text' and " +
                    "  contains(@class, 'ng-pristine') and " +
                    "  contains(@placeholder, 'Enter Amount')" +
                    "]"
    );
    private By newNonRecurringIncomeDate = By.xpath(
            "//li[contains(@ng-repeat, 'transaction')]" +
                    "//select[" +
                    "  contains(@class, 'ng-pristine') and " +
                    "  contains(@ng-model, 'transaction.month')" +
                    "]"
    );
    private By removeNonRecurringIncome = By.xpath("//a[contains(@ng-click,'removeTransaction')]");

    private WebDriver driver;

    public ProjPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addCurrentBalance(
            int amount
    ) {
        WebElement amountElem = lastOf(driver.findElements(currentBalance));
        amountElem.clear();
        amountElem.sendKeys(String.valueOf(amount));
    }

    public void addRegularIncome(
            String whatIsIt,
            int amount,
            String period
    ) {
        WebElement whatElem = lastOf(driver.findElements(newIncomeWhatIsIt));
        whatElem.clear();
        whatElem.sendKeys(whatIsIt);

        // WebElement whatElem = driver.findElement(newIncomeWhatIsIt);


        WebElement amountElem = lastOf(driver.findElements(newIncomeAmount));
        //WebElement amountElem = driver.findElement(newIncomeAmount);
        amountElem.clear();
        amountElem.sendKeys(String.valueOf(amount));

        WebElement incomeFreqElem = lastOf(driver.findElements(newIncomeFrequency));
        // WebElement incomeFreqElem = driver.findElement(newIncomeFrequency);
        Select incomeFreqSelect = new Select(incomeFreqElem);
        incomeFreqSelect.selectByVisibleText(period);
        driver.findElement(addIncome).click();


    }

    public void removeRegularIncome(

    ) {
        driver.findElement(removeIncome).click();
    }

    public void addRegularExpence(
            String whatIsIt,
            int amount,
            String period
    ) {

        WebElement whatElem = lastOf(driver.findElements(newExpenceName));
        whatElem.clear();
        whatElem.sendKeys(whatIsIt);

        WebElement amountElem = lastOf(driver.findElements(newExpenceAmount));
        amountElem.clear();
        amountElem.sendKeys(String.valueOf(amount));

        WebElement expenceFreqElem = lastOf(driver.findElements(newExpenceFrequency));
        Select expenceFreqSelect = new Select(expenceFreqElem);
        expenceFreqSelect.selectByVisibleText(period);
        driver.findElement(addExpence).click();
    }

    public void removeRegularExpence(

    ) {
        driver.findElement(removeExpence).click();
    }

    public void addNonRecuringIncome(
            String whatIsIt,
            int amount,
            String period
    ) {

        WebElement whatElem = lastOf(driver.findElements(newNonRecurringIncomeName));
        whatElem.clear();
        whatElem.sendKeys(whatIsIt);

        WebElement amountElem = lastOf(driver.findElements(newNonRecurringIncomeAmount));
        amountElem.clear();
        amountElem.sendKeys(String.valueOf(amount));


        WebElement nonRecurringIncomeFreqElem = lastOf(driver.findElements(newNonRecurringIncomeDate));
        Select nonRecurringIncomeFreqSelect = new Select(nonRecurringIncomeFreqElem);
        nonRecurringIncomeFreqSelect.selectByVisibleText(period);
        driver.findElement(addNonRecuringIncome).click();
    }

    public void removeNonRecurringIncome(

    ) {
        driver.findElement(removeNonRecurringIncome).click();
    }

    public String findBalanceByMonth(
            String currentMonth
    ) {

        String balance = balanceByName.replaceAll("month_value", currentMonth);
        return driver.findElement(By.xpath(balance)).getText();

    }

    public String findMonthByNumber(
            int month_number
    ) {

        String monthNumber = monthByNumber.replaceAll("month_number", String.valueOf(month_number));
        return driver.findElement(By.xpath(monthNumber)).getText();
    }

    public String findBalanceByIndex(
            int month_number
    ) {
        return findBalanceByMonth(findMonthByNumber(month_number));
    }

    private WebElement lastOf(List<WebElement> elems) {
        if (elems.size() == 0) {
            throw new NoSuchElementException("selector gave empty element list");
        }
        return elems.get(elems.size() - 1);
    }
}
