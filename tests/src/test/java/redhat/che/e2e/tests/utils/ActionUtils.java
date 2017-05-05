package redhat.che.e2e.tests.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import redhat.che.e2e.tests.selenium.ide.Timeouts;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

public class ActionUtils {

    public static void writeIntoElement(WebDriver driver, WebElement element, String text) {
        waitAjax().until().element(element).is().present();
        new Actions(driver).moveToElement(element).click().sendKeys(text).perform();
    }

    public static void click(WebDriver driver,WebElement element) {
        waitGui().until().element(element).is().visible();
        new WebDriverWait(driver, Timeouts.REDRAW).until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }
}
