package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pojo.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class XyzTransactionPage extends BasePage {

    private final By tableRowsLocator = By.cssSelector("tr.ng-scope");
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.ENGLISH);
    private final SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);

    public XyzTransactionPage(WebDriver browser) {
        super(browser);
    }

    public List<Transaction> getTransactions() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRowsLocator));
        return browser
                .findElements(tableRowsLocator)
                .stream()
                .map(this::parseTransactionFromRow)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Transaction parseTransactionFromRow(WebElement row) {
        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() == 3) {
            try {
                Transaction transaction = new Transaction();
                transaction.setDate(outputFormat.format(inputFormat.parse(cells.get(0).getText())));
                transaction.setAmount(Integer.parseInt(cells.get(1).getText()));
                transaction.setTransactionType(cells.get(2).getText());
                return transaction;
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}