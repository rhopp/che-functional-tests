package redhat.che.e2e.tests.fragments.topmenu;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

/**
 * id = "gwt-debug-mainMenuPanel"
 */
public class MainMenuPanel {

    @Drone
    private WebDriver driver;

    @FindBy(id = "gwt-debug-MenuItem/profileGroup-true")
    private WebElement profileItem;

    @FindBy(id = "gwt-debug-MenuItem/git-true")
    private WebElement gitItem;

    public void clickProfile(){
        click(profileItem);
    }

    public void clickGit(){
        click(gitItem);
    }

    private void click(WebElement element){
        waitGui().until().element(element).is().visible();
        element.click();
    }
}
